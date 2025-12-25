package org.stellar.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import lombok.NonNull;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Test;
import org.stellar.sdk.exception.InvalidSep45ChallengeException;
import org.stellar.sdk.scval.Scv;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.InvokeContractArgs;
import org.stellar.sdk.xdr.SCSymbol;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SorobanAddressCredentials;
import org.stellar.sdk.xdr.SorobanAuthorizationEntries;
import org.stellar.sdk.xdr.SorobanAuthorizationEntry;
import org.stellar.sdk.xdr.SorobanAuthorizedFunction;
import org.stellar.sdk.xdr.SorobanAuthorizedFunctionType;
import org.stellar.sdk.xdr.SorobanAuthorizedInvocation;
import org.stellar.sdk.xdr.SorobanCredentials;
import org.stellar.sdk.xdr.SorobanCredentialsType;
import org.stellar.sdk.xdr.Uint32;
import org.stellar.sdk.xdr.XdrString;
import org.stellar.sdk.xdr.XdrUnsignedInteger;

public class Sep45ChallengeTest {

  private static final String WEB_AUTH_CONTRACT =
      "CCSZMK2C2B7UVP3Q4JRWLS3JY7ZB4YTTRSWQXGWQVS24W3PPZXOUHH4R";
  private static final String SERVER_ACCOUNT =
      "GCSNRCUM6EDEKSSBQNIOP66ONIM26FVCYP3GHYGD4NR3DK4F635Z32WQ";
  private static final String CLIENT_CONTRACT =
      "CBUCJMHBZHQ3EXQ2LMSFVZUWCPH7BCTCYGOQ6LIIO2OUVKU3XDDOO2HN";
  private static final String CLIENT_DOMAIN_ACCOUNT =
      "GAFQLAZMGGZT4KSDIGBEIC5IXOVH6ITIFKUQ5ZTDWP3MQWLGVJWTH3TX";
  private static final String HOME_DOMAIN = "example.com";
  private static final String WEB_AUTH_DOMAIN = "auth.example.com";
  private static final String CLIENT_DOMAIN = "client.example.com";
  private static final String NONCE =
      "dGVzdG5vbmNlMTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU=";

  /**
   * Builds a valid SorobanAuthorizedInvocation for the web_auth_verify function.
   *
   * @param includeClientDomain whether to include client domain fields
   * @return the authorized invocation
   */
  private SorobanAuthorizedInvocation buildValidInvocation(boolean includeClientDomain) {
    LinkedHashMap<SCVal, SCVal> argsMap = new LinkedHashMap<>();
    argsMap.put(Scv.toSymbol("account"), Scv.toString(CLIENT_CONTRACT));
    if (includeClientDomain) {
      argsMap.put(Scv.toSymbol("client_domain"), Scv.toString(CLIENT_DOMAIN));
      argsMap.put(Scv.toSymbol("client_domain_account"), Scv.toString(CLIENT_DOMAIN_ACCOUNT));
    }
    argsMap.put(Scv.toSymbol("home_domain"), Scv.toString(HOME_DOMAIN));
    argsMap.put(Scv.toSymbol("nonce"), Scv.toString(NONCE));
    argsMap.put(Scv.toSymbol("web_auth_domain"), Scv.toString(WEB_AUTH_DOMAIN));
    argsMap.put(Scv.toSymbol("web_auth_domain_account"), Scv.toString(SERVER_ACCOUNT));

    InvokeContractArgs contractArgs =
        InvokeContractArgs.builder()
            .contractAddress(new Address(WEB_AUTH_CONTRACT).toSCAddress())
            .functionName(new SCSymbol(new XdrString(Sep45Challenge.WEB_AUTH_VERIFY_FUNCTION_NAME)))
            .args(new SCVal[] {Scv.toMap(argsMap)})
            .build();

    SorobanAuthorizedFunction function =
        SorobanAuthorizedFunction.builder()
            .discriminant(
                SorobanAuthorizedFunctionType.SOROBAN_AUTHORIZED_FUNCTION_TYPE_CONTRACT_FN)
            .contractFn(contractArgs)
            .build();

    return SorobanAuthorizedInvocation.builder()
        .function(function)
        .subInvocations(new SorobanAuthorizedInvocation[0])
        .build();
  }

  /**
   * Builds a SorobanAuthorizationEntry with address credentials.
   *
   * @param address the address for the credential
   * @param invocation the root invocation
   * @return the authorization entry
   */
  private SorobanAuthorizationEntry buildAuthorizationEntry(
      String address, SorobanAuthorizedInvocation invocation) {
    Address addr = new Address(address);

    SorobanAddressCredentials addressCredentials =
        SorobanAddressCredentials.builder()
            .address(addr.toSCAddress())
            .nonce(new Int64(0L))
            .signatureExpirationLedger(new Uint32(new XdrUnsignedInteger(0L)))
            .signature(Scv.toVoid())
            .build();

    SorobanCredentials credentials =
        SorobanCredentials.builder()
            .discriminant(SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS)
            .address(addressCredentials)
            .build();

    return SorobanAuthorizationEntry.builder()
        .credentials(credentials)
        .rootInvocation(invocation)
        .build();
  }

  /**
   * Builds valid authorization entries for testing.
   *
   * @param includeClientDomain whether to include client domain
   * @return list of authorization entries
   */
  private List<SorobanAuthorizationEntry> buildValidEntries(boolean includeClientDomain) {
    SorobanAuthorizedInvocation invocation = buildValidInvocation(includeClientDomain);

    List<SorobanAuthorizationEntry> entries = new ArrayList<>();
    // Client entry
    entries.add(buildAuthorizationEntry(CLIENT_CONTRACT, invocation));
    // Server entry
    entries.add(buildAuthorizationEntry(SERVER_ACCOUNT, invocation));

    if (includeClientDomain) {
      // Client domain entry
      entries.add(buildAuthorizationEntry(CLIENT_DOMAIN_ACCOUNT, invocation));
    }

    return entries;
  }

  @Test
  public void testReadChallengeSuccessWithClientDomain() {
    List<SorobanAuthorizationEntry> entries = buildValidEntries(true);
    String xdr = authorizationEntriesToXdr(entries);

    Sep45Challenge.ChallengeAuthorizationEntries result =
        Sep45Challenge.readChallengeAuthorizationEntries(
            xdr, SERVER_ACCOUNT, WEB_AUTH_CONTRACT, HOME_DOMAIN, WEB_AUTH_DOMAIN);

    assertEquals(CLIENT_CONTRACT, result.getClientContractId());
    assertEquals(SERVER_ACCOUNT, result.getServerAccountId());
    assertEquals(WEB_AUTH_CONTRACT, result.getWebAuthContractId());
    assertEquals(HOME_DOMAIN, result.getHomeDomain());
    assertEquals(WEB_AUTH_DOMAIN, result.getWebAuthDomain());
    assertEquals(NONCE, result.getNonce());
    assertEquals(CLIENT_DOMAIN, result.getClientDomain());
    assertEquals(CLIENT_DOMAIN_ACCOUNT, result.getClientDomainAccountId());
    assertEquals(3, result.getAuthorizationEntries().size());
  }

  @Test
  public void testReadChallengeSuccessWithoutClientDomain() {
    List<SorobanAuthorizationEntry> entries = buildValidEntries(false);
    String xdr = authorizationEntriesToXdr(entries);

    Sep45Challenge.ChallengeAuthorizationEntries result =
        Sep45Challenge.readChallengeAuthorizationEntries(
            xdr, SERVER_ACCOUNT, WEB_AUTH_CONTRACT, HOME_DOMAIN, WEB_AUTH_DOMAIN);

    assertEquals(CLIENT_CONTRACT, result.getClientContractId());
    assertEquals(SERVER_ACCOUNT, result.getServerAccountId());
    assertEquals(WEB_AUTH_CONTRACT, result.getWebAuthContractId());
    assertEquals(HOME_DOMAIN, result.getHomeDomain());
    assertEquals(WEB_AUTH_DOMAIN, result.getWebAuthDomain());
    assertEquals(NONCE, result.getNonce());
    assertNull(result.getClientDomain());
    assertNull(result.getClientDomainAccountId());
    assertEquals(2, result.getAuthorizationEntries().size());
  }

  @Test
  public void testReadChallengeSuccessWithMultipleHomeDomains() {
    List<SorobanAuthorizationEntry> entries = buildValidEntries(false);
    String xdr = authorizationEntriesToXdr(entries);

    String[] homeDomains = new String[] {"other.com", HOME_DOMAIN, "another.com"};

    Sep45Challenge.ChallengeAuthorizationEntries result =
        Sep45Challenge.readChallengeAuthorizationEntries(
            xdr, SERVER_ACCOUNT, WEB_AUTH_CONTRACT, homeDomains, WEB_AUTH_DOMAIN);

    assertEquals(HOME_DOMAIN, result.getHomeDomain());
    assertEquals(CLIENT_CONTRACT, result.getClientContractId());
  }

  @Test(expected = InvalidSep45ChallengeException.class)
  public void testReadChallengeInvalidXdrFormat() {
    // Use empty XDR which should fail to parse
    Sep45Challenge.readChallengeAuthorizationEntries(
        "AAAAAAAAAA==", SERVER_ACCOUNT, WEB_AUTH_CONTRACT, HOME_DOMAIN, WEB_AUTH_DOMAIN);
  }

  @Test
  public void testReadChallengeLessThanTwoEntries() {
    SorobanAuthorizedInvocation invocation = buildValidInvocation(false);
    List<SorobanAuthorizationEntry> entries = new ArrayList<>();
    entries.add(buildAuthorizationEntry(CLIENT_CONTRACT, invocation));

    String xdr = authorizationEntriesToXdr(entries);

    try {
      Sep45Challenge.readChallengeAuthorizationEntries(
          xdr, SERVER_ACCOUNT, WEB_AUTH_CONTRACT, HOME_DOMAIN, WEB_AUTH_DOMAIN);
      fail("Expected InvalidSep45ChallengeException");
    } catch (InvalidSep45ChallengeException e) {
      assertTrue(
          e.getMessage()
              .contains(
                  "Authorization entries must contain at least 2 entries (server and client)"));
    }
  }

  @Test
  public void testReadChallengeInconsistentRootInvocation() {
    SorobanAuthorizedInvocation invocation1 = buildValidInvocation(false);

    // Create a different invocation with different nonce
    LinkedHashMap<SCVal, SCVal> argsMap = new LinkedHashMap<>();
    argsMap.put(Scv.toSymbol("account"), Scv.toString(CLIENT_CONTRACT));
    argsMap.put(Scv.toSymbol("home_domain"), Scv.toString(HOME_DOMAIN));
    argsMap.put(Scv.toSymbol("nonce"), Scv.toString("different_nonce"));
    argsMap.put(Scv.toSymbol("web_auth_domain"), Scv.toString(WEB_AUTH_DOMAIN));
    argsMap.put(Scv.toSymbol("web_auth_domain_account"), Scv.toString(SERVER_ACCOUNT));

    InvokeContractArgs contractArgs2 =
        InvokeContractArgs.builder()
            .contractAddress(new Address(WEB_AUTH_CONTRACT).toSCAddress())
            .functionName(new SCSymbol(new XdrString(Sep45Challenge.WEB_AUTH_VERIFY_FUNCTION_NAME)))
            .args(new SCVal[] {Scv.toMap(argsMap)})
            .build();

    SorobanAuthorizedFunction function2 =
        SorobanAuthorizedFunction.builder()
            .discriminant(
                SorobanAuthorizedFunctionType.SOROBAN_AUTHORIZED_FUNCTION_TYPE_CONTRACT_FN)
            .contractFn(contractArgs2)
            .build();

    SorobanAuthorizedInvocation invocation2 =
        SorobanAuthorizedInvocation.builder()
            .function(function2)
            .subInvocations(new SorobanAuthorizedInvocation[0])
            .build();

    List<SorobanAuthorizationEntry> entries = new ArrayList<>();
    entries.add(buildAuthorizationEntry(CLIENT_CONTRACT, invocation1));
    entries.add(buildAuthorizationEntry(SERVER_ACCOUNT, invocation2));

    String xdr = authorizationEntriesToXdr(entries);

    try {
      Sep45Challenge.readChallengeAuthorizationEntries(
          xdr, SERVER_ACCOUNT, WEB_AUTH_CONTRACT, HOME_DOMAIN, WEB_AUTH_DOMAIN);
      fail("Expected InvalidSep45ChallengeException");
    } catch (InvalidSep45ChallengeException e) {
      assertTrue(
          e.getMessage().contains("All authorization entries must have the same root invocation"));
    }
  }

  @Test
  public void testReadChallengeHasSubInvocations() {
    LinkedHashMap<SCVal, SCVal> argsMap = new LinkedHashMap<>();
    argsMap.put(Scv.toSymbol("account"), Scv.toString(CLIENT_CONTRACT));
    argsMap.put(Scv.toSymbol("home_domain"), Scv.toString(HOME_DOMAIN));
    argsMap.put(Scv.toSymbol("nonce"), Scv.toString(NONCE));
    argsMap.put(Scv.toSymbol("web_auth_domain"), Scv.toString(WEB_AUTH_DOMAIN));
    argsMap.put(Scv.toSymbol("web_auth_domain_account"), Scv.toString(SERVER_ACCOUNT));

    InvokeContractArgs contractArgs =
        InvokeContractArgs.builder()
            .contractAddress(new Address(WEB_AUTH_CONTRACT).toSCAddress())
            .functionName(new SCSymbol(new XdrString(Sep45Challenge.WEB_AUTH_VERIFY_FUNCTION_NAME)))
            .args(new SCVal[] {Scv.toMap(argsMap)})
            .build();

    SorobanAuthorizedFunction function =
        SorobanAuthorizedFunction.builder()
            .discriminant(
                SorobanAuthorizedFunctionType.SOROBAN_AUTHORIZED_FUNCTION_TYPE_CONTRACT_FN)
            .contractFn(contractArgs)
            .build();

    // Create a sub-invocation
    SorobanAuthorizedInvocation subInvocation =
        SorobanAuthorizedInvocation.builder()
            .function(function)
            .subInvocations(new SorobanAuthorizedInvocation[0])
            .build();

    // Create invocation with sub-invocations
    SorobanAuthorizedInvocation invocationWithSubs =
        SorobanAuthorizedInvocation.builder()
            .function(function)
            .subInvocations(new SorobanAuthorizedInvocation[] {subInvocation})
            .build();

    List<SorobanAuthorizationEntry> entries = new ArrayList<>();
    entries.add(buildAuthorizationEntry(CLIENT_CONTRACT, invocationWithSubs));
    entries.add(buildAuthorizationEntry(SERVER_ACCOUNT, invocationWithSubs));

    String xdr = authorizationEntriesToXdr(entries);

    try {
      Sep45Challenge.readChallengeAuthorizationEntries(
          xdr, SERVER_ACCOUNT, WEB_AUTH_CONTRACT, HOME_DOMAIN, WEB_AUTH_DOMAIN);
      fail("Expected InvalidSep45ChallengeException");
    } catch (InvalidSep45ChallengeException e) {
      assertTrue(e.getMessage().contains("Authorization entry must not have sub-invocations"));
    }
  }

  @Test
  public void testReadChallengeWrongContractAddress() {
    List<SorobanAuthorizationEntry> entries = buildValidEntries(false);
    String xdr = authorizationEntriesToXdr(entries);

    String wrongContract = "CDLZFC3SYJYDZT7K67VZ75HPJVIEUVNIXF47ZG2FB2RMQQVU2HHGCYSC";

    try {
      Sep45Challenge.readChallengeAuthorizationEntries(
          xdr, SERVER_ACCOUNT, wrongContract, HOME_DOMAIN, WEB_AUTH_DOMAIN);
      fail("Expected InvalidSep45ChallengeException");
    } catch (InvalidSep45ChallengeException e) {
      assertTrue(e.getMessage().contains("Contract address mismatch"));
    }
  }

  @Test
  public void testReadChallengeWrongFunctionName() {
    LinkedHashMap<SCVal, SCVal> argsMap = new LinkedHashMap<>();
    argsMap.put(Scv.toSymbol("account"), Scv.toString(CLIENT_CONTRACT));
    argsMap.put(Scv.toSymbol("home_domain"), Scv.toString(HOME_DOMAIN));
    argsMap.put(Scv.toSymbol("nonce"), Scv.toString(NONCE));
    argsMap.put(Scv.toSymbol("web_auth_domain"), Scv.toString(WEB_AUTH_DOMAIN));
    argsMap.put(Scv.toSymbol("web_auth_domain_account"), Scv.toString(SERVER_ACCOUNT));

    InvokeContractArgs contractArgs =
        InvokeContractArgs.builder()
            .contractAddress(new Address(WEB_AUTH_CONTRACT).toSCAddress())
            .functionName(new SCSymbol(new XdrString("wrong_function")))
            .args(new SCVal[] {Scv.toMap(argsMap)})
            .build();

    SorobanAuthorizedFunction function =
        SorobanAuthorizedFunction.builder()
            .discriminant(
                SorobanAuthorizedFunctionType.SOROBAN_AUTHORIZED_FUNCTION_TYPE_CONTRACT_FN)
            .contractFn(contractArgs)
            .build();

    SorobanAuthorizedInvocation invocation =
        SorobanAuthorizedInvocation.builder()
            .function(function)
            .subInvocations(new SorobanAuthorizedInvocation[0])
            .build();

    List<SorobanAuthorizationEntry> entries = new ArrayList<>();
    entries.add(buildAuthorizationEntry(CLIENT_CONTRACT, invocation));
    entries.add(buildAuthorizationEntry(SERVER_ACCOUNT, invocation));

    String xdr = authorizationEntriesToXdr(entries);

    try {
      Sep45Challenge.readChallengeAuthorizationEntries(
          xdr, SERVER_ACCOUNT, WEB_AUTH_CONTRACT, HOME_DOMAIN, WEB_AUTH_DOMAIN);
      fail("Expected InvalidSep45ChallengeException");
    } catch (InvalidSep45ChallengeException e) {
      assertTrue(e.getMessage().contains("Function name mismatch"));
    }
  }

  @Test
  public void testReadChallengeWrongArgsCount() {
    InvokeContractArgs contractArgs =
        InvokeContractArgs.builder()
            .contractAddress(new Address(WEB_AUTH_CONTRACT).toSCAddress())
            .functionName(new SCSymbol(new XdrString(Sep45Challenge.WEB_AUTH_VERIFY_FUNCTION_NAME)))
            .args(new SCVal[] {Scv.toSymbol("arg1"), Scv.toSymbol("arg2")})
            .build();

    SorobanAuthorizedFunction function =
        SorobanAuthorizedFunction.builder()
            .discriminant(
                SorobanAuthorizedFunctionType.SOROBAN_AUTHORIZED_FUNCTION_TYPE_CONTRACT_FN)
            .contractFn(contractArgs)
            .build();

    SorobanAuthorizedInvocation invocation =
        SorobanAuthorizedInvocation.builder()
            .function(function)
            .subInvocations(new SorobanAuthorizedInvocation[0])
            .build();

    List<SorobanAuthorizationEntry> entries = new ArrayList<>();
    entries.add(buildAuthorizationEntry(CLIENT_CONTRACT, invocation));
    entries.add(buildAuthorizationEntry(SERVER_ACCOUNT, invocation));

    String xdr = authorizationEntriesToXdr(entries);

    try {
      Sep45Challenge.readChallengeAuthorizationEntries(
          xdr, SERVER_ACCOUNT, WEB_AUTH_CONTRACT, HOME_DOMAIN, WEB_AUTH_DOMAIN);
      fail("Expected InvalidSep45ChallengeException");
    } catch (InvalidSep45ChallengeException e) {
      assertTrue(e.getMessage().contains("Expected exactly one argument"));
    }
  }

  @Test
  public void testReadChallengeInvalidStruct() {
    // Use a symbol instead of a map as argument
    InvokeContractArgs contractArgs =
        InvokeContractArgs.builder()
            .contractAddress(new Address(WEB_AUTH_CONTRACT).toSCAddress())
            .functionName(new SCSymbol(new XdrString(Sep45Challenge.WEB_AUTH_VERIFY_FUNCTION_NAME)))
            .args(new SCVal[] {Scv.toSymbol("not_a_map")})
            .build();

    SorobanAuthorizedFunction function =
        SorobanAuthorizedFunction.builder()
            .discriminant(
                SorobanAuthorizedFunctionType.SOROBAN_AUTHORIZED_FUNCTION_TYPE_CONTRACT_FN)
            .contractFn(contractArgs)
            .build();

    SorobanAuthorizedInvocation invocation =
        SorobanAuthorizedInvocation.builder()
            .function(function)
            .subInvocations(new SorobanAuthorizedInvocation[0])
            .build();

    List<SorobanAuthorizationEntry> entries = new ArrayList<>();
    entries.add(buildAuthorizationEntry(CLIENT_CONTRACT, invocation));
    entries.add(buildAuthorizationEntry(SERVER_ACCOUNT, invocation));

    String xdr = authorizationEntriesToXdr(entries);

    try {
      Sep45Challenge.readChallengeAuthorizationEntries(
          xdr, SERVER_ACCOUNT, WEB_AUTH_CONTRACT, HOME_DOMAIN, WEB_AUTH_DOMAIN);
      fail("Expected InvalidSep45ChallengeException");
    } catch (InvalidSep45ChallengeException e) {
      assertTrue(e.getMessage().contains("Expected Map argument"));
    }
  }

  @Test
  public void testReadChallengeMissingAccount() {
    LinkedHashMap<SCVal, SCVal> argsMap = new LinkedHashMap<>();
    // Missing "account" field
    argsMap.put(Scv.toSymbol("home_domain"), Scv.toString(HOME_DOMAIN));
    argsMap.put(Scv.toSymbol("nonce"), Scv.toString(NONCE));
    argsMap.put(Scv.toSymbol("web_auth_domain"), Scv.toString(WEB_AUTH_DOMAIN));
    argsMap.put(Scv.toSymbol("web_auth_domain_account"), Scv.toString(SERVER_ACCOUNT));

    String xdr = buildEntriesWithCustomArgs(argsMap);

    try {
      Sep45Challenge.readChallengeAuthorizationEntries(
          xdr, SERVER_ACCOUNT, WEB_AUTH_CONTRACT, HOME_DOMAIN, WEB_AUTH_DOMAIN);
      fail("Expected InvalidSep45ChallengeException");
    } catch (InvalidSep45ChallengeException e) {
      assertTrue(e.getMessage().contains("Missing required argument: account"));
    }
  }

  @Test
  public void testReadChallengeMissingHomeDomain() {
    LinkedHashMap<SCVal, SCVal> argsMap = new LinkedHashMap<>();
    argsMap.put(Scv.toSymbol("account"), Scv.toString(CLIENT_CONTRACT));
    // Missing "home_domain" field
    argsMap.put(Scv.toSymbol("nonce"), Scv.toString(NONCE));
    argsMap.put(Scv.toSymbol("web_auth_domain"), Scv.toString(WEB_AUTH_DOMAIN));
    argsMap.put(Scv.toSymbol("web_auth_domain_account"), Scv.toString(SERVER_ACCOUNT));

    String xdr = buildEntriesWithCustomArgs(argsMap);

    try {
      Sep45Challenge.readChallengeAuthorizationEntries(
          xdr, SERVER_ACCOUNT, WEB_AUTH_CONTRACT, HOME_DOMAIN, WEB_AUTH_DOMAIN);
      fail("Expected InvalidSep45ChallengeException");
    } catch (InvalidSep45ChallengeException e) {
      assertTrue(e.getMessage().contains("Missing required argument: home_domain"));
    }
  }

  @Test
  public void testReadChallengeMissingNonce() {
    LinkedHashMap<SCVal, SCVal> argsMap = new LinkedHashMap<>();
    argsMap.put(Scv.toSymbol("account"), Scv.toString(CLIENT_CONTRACT));
    argsMap.put(Scv.toSymbol("home_domain"), Scv.toString(HOME_DOMAIN));
    // Missing "nonce" field
    argsMap.put(Scv.toSymbol("web_auth_domain"), Scv.toString(WEB_AUTH_DOMAIN));
    argsMap.put(Scv.toSymbol("web_auth_domain_account"), Scv.toString(SERVER_ACCOUNT));

    String xdr = buildEntriesWithCustomArgs(argsMap);

    try {
      Sep45Challenge.readChallengeAuthorizationEntries(
          xdr, SERVER_ACCOUNT, WEB_AUTH_CONTRACT, HOME_DOMAIN, WEB_AUTH_DOMAIN);
      fail("Expected InvalidSep45ChallengeException");
    } catch (InvalidSep45ChallengeException e) {
      assertTrue(e.getMessage().contains("Missing required argument: nonce"));
    }
  }

  @Test
  public void testReadChallengeMissingWebAuthDomain() {
    LinkedHashMap<SCVal, SCVal> argsMap = new LinkedHashMap<>();
    argsMap.put(Scv.toSymbol("account"), Scv.toString(CLIENT_CONTRACT));
    argsMap.put(Scv.toSymbol("home_domain"), Scv.toString(HOME_DOMAIN));
    argsMap.put(Scv.toSymbol("nonce"), Scv.toString(NONCE));
    // Missing "web_auth_domain" field
    argsMap.put(Scv.toSymbol("web_auth_domain_account"), Scv.toString(SERVER_ACCOUNT));

    String xdr = buildEntriesWithCustomArgs(argsMap);

    try {
      Sep45Challenge.readChallengeAuthorizationEntries(
          xdr, SERVER_ACCOUNT, WEB_AUTH_CONTRACT, HOME_DOMAIN, WEB_AUTH_DOMAIN);
      fail("Expected InvalidSep45ChallengeException");
    } catch (InvalidSep45ChallengeException e) {
      assertTrue(e.getMessage().contains("Missing required argument: web_auth_domain"));
    }
  }

  @Test
  public void testReadChallengeMissingWebAuthDomainAccount() {
    LinkedHashMap<SCVal, SCVal> argsMap = new LinkedHashMap<>();
    argsMap.put(Scv.toSymbol("account"), Scv.toString(CLIENT_CONTRACT));
    argsMap.put(Scv.toSymbol("home_domain"), Scv.toString(HOME_DOMAIN));
    argsMap.put(Scv.toSymbol("nonce"), Scv.toString(NONCE));
    argsMap.put(Scv.toSymbol("web_auth_domain"), Scv.toString(WEB_AUTH_DOMAIN));
    // Missing "web_auth_domain_account" field

    String xdr = buildEntriesWithCustomArgs(argsMap);

    try {
      Sep45Challenge.readChallengeAuthorizationEntries(
          xdr, SERVER_ACCOUNT, WEB_AUTH_CONTRACT, HOME_DOMAIN, WEB_AUTH_DOMAIN);
      fail("Expected InvalidSep45ChallengeException");
    } catch (InvalidSep45ChallengeException e) {
      assertTrue(e.getMessage().contains("Missing required argument: web_auth_domain_account"));
    }
  }

  @Test
  public void testReadChallengeServerAccountMismatch() {
    List<SorobanAuthorizationEntry> entries = buildValidEntries(false);
    String xdr = authorizationEntriesToXdr(entries);

    String wrongServer = "GAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAWHF";

    try {
      Sep45Challenge.readChallengeAuthorizationEntries(
          xdr, wrongServer, WEB_AUTH_CONTRACT, HOME_DOMAIN, WEB_AUTH_DOMAIN);
      fail("Expected InvalidSep45ChallengeException");
    } catch (InvalidSep45ChallengeException e) {
      assertTrue(e.getMessage().contains("web_auth_domain_account mismatch"));
    }
  }

  @Test
  public void testReadChallengeWebAuthDomainMismatch() {
    List<SorobanAuthorizationEntry> entries = buildValidEntries(false);
    String xdr = authorizationEntriesToXdr(entries);

    try {
      Sep45Challenge.readChallengeAuthorizationEntries(
          xdr, SERVER_ACCOUNT, WEB_AUTH_CONTRACT, HOME_DOMAIN, "wrong.domain.com");
      fail("Expected InvalidSep45ChallengeException");
    } catch (InvalidSep45ChallengeException e) {
      assertTrue(e.getMessage().contains("web_auth_domain mismatch"));
    }
  }

  @Test
  public void testReadChallengeHomeDomainMismatchString() {
    List<SorobanAuthorizationEntry> entries = buildValidEntries(false);
    String xdr = authorizationEntriesToXdr(entries);

    try {
      Sep45Challenge.readChallengeAuthorizationEntries(
          xdr, SERVER_ACCOUNT, WEB_AUTH_CONTRACT, "wrong.domain.com", WEB_AUTH_DOMAIN);
      fail("Expected InvalidSep45ChallengeException");
    } catch (InvalidSep45ChallengeException e) {
      assertTrue(e.getMessage().contains("is not in the list of allowed home domains"));
    }
  }

  @Test
  public void testReadChallengeHomeDomainMismatchList() {
    List<SorobanAuthorizationEntry> entries = buildValidEntries(false);
    String xdr = authorizationEntriesToXdr(entries);

    String[] homeDomains = new String[] {"other.com", "another.com"};

    try {
      Sep45Challenge.readChallengeAuthorizationEntries(
          xdr, SERVER_ACCOUNT, WEB_AUTH_CONTRACT, homeDomains, WEB_AUTH_DOMAIN);
      fail("Expected InvalidSep45ChallengeException");
    } catch (InvalidSep45ChallengeException e) {
      assertTrue(e.getMessage().contains("is not in the list of allowed home domains"));
    }
  }

  @Test
  public void testReadChallengeClientDomainWithoutAccount() {
    // Create entries where client_domain is present but client_domain_account is missing
    LinkedHashMap<SCVal, SCVal> argsMap = new LinkedHashMap<>();
    argsMap.put(Scv.toSymbol("account"), Scv.toString(CLIENT_CONTRACT));
    argsMap.put(Scv.toSymbol("client_domain"), Scv.toString(CLIENT_DOMAIN));
    // Missing client_domain_account
    argsMap.put(Scv.toSymbol("home_domain"), Scv.toString(HOME_DOMAIN));
    argsMap.put(Scv.toSymbol("nonce"), Scv.toString(NONCE));
    argsMap.put(Scv.toSymbol("web_auth_domain"), Scv.toString(WEB_AUTH_DOMAIN));
    argsMap.put(Scv.toSymbol("web_auth_domain_account"), Scv.toString(SERVER_ACCOUNT));

    String xdr = buildEntriesWithCustomArgs(argsMap);

    try {
      Sep45Challenge.readChallengeAuthorizationEntries(
          xdr, SERVER_ACCOUNT, WEB_AUTH_CONTRACT, HOME_DOMAIN, WEB_AUTH_DOMAIN);
      fail("Expected InvalidSep45ChallengeException");
    } catch (InvalidSep45ChallengeException e) {
      assertTrue(
          e.getMessage()
              .contains(
                  "client_domain and client_domain_account must both be present or both be absent"));
    }
  }

  @Test
  public void testReadChallengeClientDomainAccountWithoutDomain() {
    LinkedHashMap<SCVal, SCVal> argsMap = new LinkedHashMap<>();
    argsMap.put(Scv.toSymbol("account"), Scv.toString(CLIENT_CONTRACT));
    // Missing client_domain
    argsMap.put(Scv.toSymbol("client_domain_account"), Scv.toString(CLIENT_DOMAIN_ACCOUNT));
    argsMap.put(Scv.toSymbol("home_domain"), Scv.toString(HOME_DOMAIN));
    argsMap.put(Scv.toSymbol("nonce"), Scv.toString(NONCE));
    argsMap.put(Scv.toSymbol("web_auth_domain"), Scv.toString(WEB_AUTH_DOMAIN));
    argsMap.put(Scv.toSymbol("web_auth_domain_account"), Scv.toString(SERVER_ACCOUNT));

    String xdr = buildEntriesWithCustomArgs(argsMap);

    try {
      Sep45Challenge.readChallengeAuthorizationEntries(
          xdr, SERVER_ACCOUNT, WEB_AUTH_CONTRACT, HOME_DOMAIN, WEB_AUTH_DOMAIN);
      fail("Expected InvalidSep45ChallengeException");
    } catch (InvalidSep45ChallengeException e) {
      assertTrue(
          e.getMessage()
              .contains(
                  "client_domain and client_domain_account must both be present or both be absent"));
    }
  }

  @Test
  public void testReadChallengeMissingServerEntry() {
    SorobanAuthorizedInvocation invocation = buildValidInvocation(false);

    List<SorobanAuthorizationEntry> entries = new ArrayList<>();
    // Only client entry, missing server entry
    entries.add(buildAuthorizationEntry(CLIENT_CONTRACT, invocation));
    // Add another random entry that's not the server (use a valid random account)
    String randomAccount = KeyPair.random().getAccountId();
    entries.add(buildAuthorizationEntry(randomAccount, invocation));

    String xdr = authorizationEntriesToXdr(entries);

    try {
      Sep45Challenge.readChallengeAuthorizationEntries(
          xdr, SERVER_ACCOUNT, WEB_AUTH_CONTRACT, HOME_DOMAIN, WEB_AUTH_DOMAIN);
      fail("Expected InvalidSep45ChallengeException");
    } catch (InvalidSep45ChallengeException e) {
      assertTrue(
          e.getMessage()
              .contains("Authorization entries must include a credential for the server account"));
    }
  }

  @Test
  public void testReadChallengeMissingClientEntry() {
    SorobanAuthorizedInvocation invocation = buildValidInvocation(false);

    List<SorobanAuthorizationEntry> entries = new ArrayList<>();
    // Only server entry, missing client entry
    entries.add(buildAuthorizationEntry(SERVER_ACCOUNT, invocation));
    // Add another random entry that's not the client (use a valid random account)
    String randomAccount = KeyPair.random().getAccountId();
    entries.add(buildAuthorizationEntry(randomAccount, invocation));

    String xdr = authorizationEntriesToXdr(entries);

    try {
      Sep45Challenge.readChallengeAuthorizationEntries(
          xdr, SERVER_ACCOUNT, WEB_AUTH_CONTRACT, HOME_DOMAIN, WEB_AUTH_DOMAIN);
      fail("Expected InvalidSep45ChallengeException");
    } catch (InvalidSep45ChallengeException e) {
      assertTrue(
          e.getMessage()
              .contains("Authorization entries must include a credential for the client account"));
    }
  }

  @Test
  public void testReadChallengeMissingClientDomainEntry() {
    // Create entries with client_domain in args but missing the client_domain_account entry
    LinkedHashMap<SCVal, SCVal> argsMap = new LinkedHashMap<>();
    argsMap.put(Scv.toSymbol("account"), Scv.toString(CLIENT_CONTRACT));
    argsMap.put(Scv.toSymbol("client_domain"), Scv.toString(CLIENT_DOMAIN));
    argsMap.put(Scv.toSymbol("client_domain_account"), Scv.toString(CLIENT_DOMAIN_ACCOUNT));
    argsMap.put(Scv.toSymbol("home_domain"), Scv.toString(HOME_DOMAIN));
    argsMap.put(Scv.toSymbol("nonce"), Scv.toString(NONCE));
    argsMap.put(Scv.toSymbol("web_auth_domain"), Scv.toString(WEB_AUTH_DOMAIN));
    argsMap.put(Scv.toSymbol("web_auth_domain_account"), Scv.toString(SERVER_ACCOUNT));

    InvokeContractArgs contractArgs =
        InvokeContractArgs.builder()
            .contractAddress(new Address(WEB_AUTH_CONTRACT).toSCAddress())
            .functionName(new SCSymbol(new XdrString(Sep45Challenge.WEB_AUTH_VERIFY_FUNCTION_NAME)))
            .args(new SCVal[] {Scv.toMap(argsMap)})
            .build();

    SorobanAuthorizedFunction function =
        SorobanAuthorizedFunction.builder()
            .discriminant(
                SorobanAuthorizedFunctionType.SOROBAN_AUTHORIZED_FUNCTION_TYPE_CONTRACT_FN)
            .contractFn(contractArgs)
            .build();

    SorobanAuthorizedInvocation invocation =
        SorobanAuthorizedInvocation.builder()
            .function(function)
            .subInvocations(new SorobanAuthorizedInvocation[0])
            .build();

    List<SorobanAuthorizationEntry> entries = new ArrayList<>();
    entries.add(buildAuthorizationEntry(CLIENT_CONTRACT, invocation));
    entries.add(buildAuthorizationEntry(SERVER_ACCOUNT, invocation));
    // Missing client domain account entry!

    String xdr = authorizationEntriesToXdr(entries);

    try {
      Sep45Challenge.readChallengeAuthorizationEntries(
          xdr, SERVER_ACCOUNT, WEB_AUTH_CONTRACT, HOME_DOMAIN, WEB_AUTH_DOMAIN);
      fail("Expected InvalidSep45ChallengeException");
    } catch (InvalidSep45ChallengeException e) {
      assertTrue(
          e.getMessage()
              .contains(
                  "Authorization entries must include a credential for client_domain_account"));
    }
  }

  @Test
  public void testReadChallengeUnsupportedCredentialsType() {
    SorobanAuthorizedInvocation invocation = buildValidInvocation(false);

    // Create entry with SOURCE_ACCOUNT credentials type
    SorobanCredentials sourceCredentials =
        SorobanCredentials.builder()
            .discriminant(SorobanCredentialsType.SOROBAN_CREDENTIALS_SOURCE_ACCOUNT)
            .build();

    SorobanAuthorizationEntry sourceEntry =
        SorobanAuthorizationEntry.builder()
            .credentials(sourceCredentials)
            .rootInvocation(invocation)
            .build();

    List<SorobanAuthorizationEntry> entries = new ArrayList<>();
    entries.add(sourceEntry);
    entries.add(buildAuthorizationEntry(SERVER_ACCOUNT, invocation));

    String xdr = authorizationEntriesToXdr(entries);

    try {
      Sep45Challenge.readChallengeAuthorizationEntries(
          xdr, SERVER_ACCOUNT, WEB_AUTH_CONTRACT, HOME_DOMAIN, WEB_AUTH_DOMAIN);
      fail("Expected InvalidSep45ChallengeException");
    } catch (InvalidSep45ChallengeException e) {
      assertTrue(
          e.getMessage()
              .contains("All authorization entries must have SOROBAN_CREDENTIALS_ADDRESS type"));
    }
  }

  @Test
  public void testReadChallengeInvalidServerAccountId() {
    List<SorobanAuthorizationEntry> entries = buildValidEntries(false);
    String xdr = authorizationEntriesToXdr(entries);

    // Use a contract address instead of account address for server account
    try {
      Sep45Challenge.readChallengeAuthorizationEntries(
          xdr, CLIENT_CONTRACT, WEB_AUTH_CONTRACT, HOME_DOMAIN, WEB_AUTH_DOMAIN);
      fail("Expected InvalidSep45ChallengeException");
    } catch (InvalidSep45ChallengeException e) {
      assertTrue(e.getMessage().contains("is not a valid account id"));
    }
  }

  @Test
  public void testReadChallengeInvalidWebAuthContractId() {
    List<SorobanAuthorizationEntry> entries = buildValidEntries(false);
    String xdr = authorizationEntriesToXdr(entries);

    try {
      Sep45Challenge.readChallengeAuthorizationEntries(
          xdr, SERVER_ACCOUNT, "invalid_contract", HOME_DOMAIN, WEB_AUTH_DOMAIN);
      fail("Expected InvalidSep45ChallengeException");
    } catch (InvalidSep45ChallengeException e) {
      assertTrue(e.getMessage().contains("is not a valid contract id"));
    }
  }

  @Test
  public void testReadChallengeEmptyHomeDomains() {
    List<SorobanAuthorizationEntry> entries = buildValidEntries(false);
    String xdr = authorizationEntriesToXdr(entries);

    try {
      Sep45Challenge.readChallengeAuthorizationEntries(
          xdr, SERVER_ACCOUNT, WEB_AUTH_CONTRACT, new String[] {}, WEB_AUTH_DOMAIN);
      fail("Expected InvalidSep45ChallengeException");
    } catch (InvalidSep45ChallengeException e) {
      assertTrue(e.getMessage().contains("At least one domain name must be included"));
    }
  }

  @Test
  public void testBuildChallengeAuthorizationEntriesWithClientDomain() throws IOException {
    KeyPair serverSigner = KeyPair.random();
    String serverAccountId = serverSigner.getAccountId();

    // Mock the simulate transaction response
    String mockResponse = buildMockSimulateResponse(serverAccountId, true);

    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(mockResponse));
    mockWebServer.start();

    HttpUrl baseUrl = mockWebServer.url("");
    SorobanServer server = new SorobanServer(baseUrl.toString());

    SorobanAuthorizationEntries result =
        Sep45Challenge.buildChallengeAuthorizationEntries(
            server,
            serverSigner,
            CLIENT_CONTRACT,
            HOME_DOMAIN,
            WEB_AUTH_DOMAIN,
            WEB_AUTH_CONTRACT,
            Network.TESTNET,
            NONCE,
            180L,
            CLIENT_DOMAIN,
            CLIENT_DOMAIN_ACCOUNT);

    assertNotNull(result);
    assertTrue(result.getSorobanAuthorizationEntries().length >= 2);

    mockWebServer.close();
    server.close();
  }

  @Test
  public void testBuildChallengeAuthorizationEntriesWithoutClientDomain() throws IOException {
    KeyPair serverSigner = KeyPair.random();
    String serverAccountId = serverSigner.getAccountId();

    // Mock the simulate transaction response
    String mockResponse = buildMockSimulateResponse(serverAccountId, false);

    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(mockResponse));
    mockWebServer.start();

    HttpUrl baseUrl = mockWebServer.url("");
    SorobanServer server = new SorobanServer(baseUrl.toString());

    SorobanAuthorizationEntries result =
        Sep45Challenge.buildChallengeAuthorizationEntries(
            server,
            serverSigner,
            CLIENT_CONTRACT,
            HOME_DOMAIN,
            WEB_AUTH_DOMAIN,
            WEB_AUTH_CONTRACT,
            Network.TESTNET,
            null,
            null);

    assertNotNull(result);
    assertTrue(result.getSorobanAuthorizationEntries().length >= 2);

    mockWebServer.close();
    server.close();
  }

  @Test
  public void testBuildChallengeAuthorizationEntriesClientDomainWithoutAccount()
      throws IOException {
    KeyPair serverSigner = KeyPair.random();

    try (MockWebServer mockWebServer = new MockWebServer()) {
      mockWebServer.start();
      HttpUrl baseUrl = mockWebServer.url("");
      try (SorobanServer server = new SorobanServer(baseUrl.toString())) {
        Sep45Challenge.buildChallengeAuthorizationEntries(
            server,
            serverSigner,
            CLIENT_CONTRACT,
            HOME_DOMAIN,
            WEB_AUTH_DOMAIN,
            WEB_AUTH_CONTRACT,
            Network.TESTNET,
            null,
            null,
            CLIENT_DOMAIN,
            null);
        fail("Expected InvalidSep45ChallengeException");
      } catch (InvalidSep45ChallengeException e) {
        assertTrue(
            e.getMessage()
                .contains(
                    "clientDomain and clientDomainAccountId must both be provided or both be null"));
      }
    }
  }

  @Test
  public void testBuildChallengeAuthorizationEntriesClientDomainAccountWithoutDomain()
      throws IOException {
    KeyPair serverSigner = KeyPair.random();

    try (MockWebServer mockWebServer = new MockWebServer()) {
      mockWebServer.start();
      HttpUrl baseUrl = mockWebServer.url("");
      try (SorobanServer server = new SorobanServer(baseUrl.toString())) {
        Sep45Challenge.buildChallengeAuthorizationEntries(
            server,
            serverSigner,
            CLIENT_CONTRACT,
            HOME_DOMAIN,
            WEB_AUTH_DOMAIN,
            WEB_AUTH_CONTRACT,
            Network.TESTNET,
            null,
            null,
            null,
            CLIENT_DOMAIN_ACCOUNT);
        fail("Expected InvalidSep45ChallengeException");
      } catch (InvalidSep45ChallengeException e) {
        assertTrue(
            e.getMessage()
                .contains(
                    "clientDomain and clientDomainAccountId must both be provided or both be null"));
      }
    }
  }

  @Test
  public void testBuildChallengeAuthorizationEntriesInvalidClientContractId() throws IOException {
    KeyPair serverSigner = KeyPair.random();

    try (MockWebServer mockWebServer = new MockWebServer()) {
      mockWebServer.start();
      HttpUrl baseUrl = mockWebServer.url("");
      try (SorobanServer server = new SorobanServer(baseUrl.toString())) {
        Sep45Challenge.buildChallengeAuthorizationEntries(
            server,
            serverSigner,
            "invalid_contract_id",
            HOME_DOMAIN,
            WEB_AUTH_DOMAIN,
            WEB_AUTH_CONTRACT,
            Network.TESTNET,
            null,
            null);
        fail("Expected InvalidSep45ChallengeException");
      } catch (InvalidSep45ChallengeException e) {
        assertTrue(e.getMessage().contains("is not a valid contract id"));
      }
    }
  }

  @Test
  public void testBuildChallengeAuthorizationEntriesInvalidWebAuthContractId() throws IOException {
    KeyPair serverSigner = KeyPair.random();

    try (MockWebServer mockWebServer = new MockWebServer()) {
      mockWebServer.start();
      HttpUrl baseUrl = mockWebServer.url("");
      try (SorobanServer server = new SorobanServer(baseUrl.toString())) {
        Sep45Challenge.buildChallengeAuthorizationEntries(
            server,
            serverSigner,
            CLIENT_CONTRACT,
            HOME_DOMAIN,
            WEB_AUTH_DOMAIN,
            "invalid_contract_id",
            Network.TESTNET,
            null,
            null);
        fail("Expected InvalidSep45ChallengeException");
      } catch (InvalidSep45ChallengeException e) {
        assertTrue(e.getMessage().contains("is not a valid contract id"));
      }
    }
  }

  @Test
  public void testBuildChallengeAuthorizationEntriesSimulationError() throws IOException {
    KeyPair serverSigner = KeyPair.random();

    String mockResponse =
        new String(
            Files.readAllBytes(
                Paths.get("src/test/resources/sep45/simulate_transaction_build_error.json")));

    try (MockWebServer mockWebServer = new MockWebServer()) {
      mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(mockResponse));
      mockWebServer.start();

      HttpUrl baseUrl = mockWebServer.url("");
      try (SorobanServer server = new SorobanServer(baseUrl.toString())) {
        Sep45Challenge.buildChallengeAuthorizationEntries(
            server,
            serverSigner,
            CLIENT_CONTRACT,
            HOME_DOMAIN,
            WEB_AUTH_DOMAIN,
            WEB_AUTH_CONTRACT,
            Network.TESTNET,
            null,
            null);
        fail("Expected InvalidSep45ChallengeException");
      } catch (InvalidSep45ChallengeException e) {
        assertTrue(e.getMessage().contains("Transaction simulation failed"));
        assertTrue(e.getMessage().contains("HostError: Error(WasmVm, MissingValue)"));
      }
    }
  }

  @Test
  public void testVerifyChallengeAuthorizationEntriesSuccess() throws IOException {
    List<SorobanAuthorizationEntry> entries = buildValidEntries(false);
    String xdr = authorizationEntriesToXdr(entries);

    String mockResponse =
        new String(
            Files.readAllBytes(
                Paths.get("src/test/resources/sep45/simulate_transaction_success.json")));

    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(mockResponse));
    mockWebServer.start();

    HttpUrl baseUrl = mockWebServer.url("");
    SorobanServer server = new SorobanServer(baseUrl.toString());

    Sep45Challenge.ChallengeAuthorizationEntries result =
        Sep45Challenge.verifyChallengeAuthorizationEntries(
            server,
            xdr,
            SERVER_ACCOUNT,
            WEB_AUTH_CONTRACT,
            HOME_DOMAIN,
            WEB_AUTH_DOMAIN,
            Network.TESTNET);

    assertEquals(CLIENT_CONTRACT, result.getClientContractId());
    assertEquals(SERVER_ACCOUNT, result.getServerAccountId());

    mockWebServer.close();
    server.close();
  }

  @Test
  public void testVerifyChallengeAuthorizationEntriesFailed() throws IOException {
    List<SorobanAuthorizationEntry> entries = buildValidEntries(false);
    String xdr = authorizationEntriesToXdr(entries);

    String mockResponse =
        new String(
            Files.readAllBytes(
                Paths.get("src/test/resources/sep45/simulate_transaction_failed.json")));

    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(mockResponse));
    mockWebServer.start();

    HttpUrl baseUrl = mockWebServer.url("");
    SorobanServer server = new SorobanServer(baseUrl.toString());

    try {
      Sep45Challenge.verifyChallengeAuthorizationEntries(
          server,
          xdr,
          SERVER_ACCOUNT,
          WEB_AUTH_CONTRACT,
          HOME_DOMAIN,
          WEB_AUTH_DOMAIN,
          Network.TESTNET);
      fail("Expected InvalidSep45ChallengeException");
    } catch (InvalidSep45ChallengeException e) {
      assertTrue(e.getMessage().contains("Signature verification failed"));
    }

    mockWebServer.close();
    server.close();
  }

  @Test
  public void testVerifyChallengeAuthorizationEntriesWithMultipleHomeDomains() throws IOException {
    List<SorobanAuthorizationEntry> entries = buildValidEntries(false);
    String xdr = authorizationEntriesToXdr(entries);

    String mockResponse =
        new String(
            Files.readAllBytes(
                Paths.get("src/test/resources/sep45/simulate_transaction_success.json")));

    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(mockResponse));
    mockWebServer.start();

    HttpUrl baseUrl = mockWebServer.url("");
    SorobanServer server = new SorobanServer(baseUrl.toString());

    String[] homeDomains = new String[] {"other.com", HOME_DOMAIN};

    Sep45Challenge.ChallengeAuthorizationEntries result =
        Sep45Challenge.verifyChallengeAuthorizationEntries(
            server,
            xdr,
            SERVER_ACCOUNT,
            WEB_AUTH_CONTRACT,
            homeDomains,
            WEB_AUTH_DOMAIN,
            Network.TESTNET);

    assertEquals(HOME_DOMAIN, result.getHomeDomain());

    mockWebServer.close();
    server.close();
  }

  /**
   * Builds entries with custom args map for testing validation errors.
   *
   * @param argsMap the custom args map
   * @return XDR string of the entries
   */
  private String buildEntriesWithCustomArgs(LinkedHashMap<SCVal, SCVal> argsMap) {
    InvokeContractArgs contractArgs =
        InvokeContractArgs.builder()
            .contractAddress(new Address(WEB_AUTH_CONTRACT).toSCAddress())
            .functionName(new SCSymbol(new XdrString(Sep45Challenge.WEB_AUTH_VERIFY_FUNCTION_NAME)))
            .args(new SCVal[] {Scv.toMap(argsMap)})
            .build();

    SorobanAuthorizedFunction function =
        SorobanAuthorizedFunction.builder()
            .discriminant(
                SorobanAuthorizedFunctionType.SOROBAN_AUTHORIZED_FUNCTION_TYPE_CONTRACT_FN)
            .contractFn(contractArgs)
            .build();

    SorobanAuthorizedInvocation invocation =
        SorobanAuthorizedInvocation.builder()
            .function(function)
            .subInvocations(new SorobanAuthorizedInvocation[0])
            .build();

    List<SorobanAuthorizationEntry> entries = new ArrayList<>();
    entries.add(buildAuthorizationEntry(CLIENT_CONTRACT, invocation));
    entries.add(buildAuthorizationEntry(SERVER_ACCOUNT, invocation));

    return authorizationEntriesToXdr(entries);
  }

  /**
   * Builds a mock simulate transaction response.
   *
   * @param serverAccountId the server account ID
   * @param includeClientDomain whether to include client domain
   * @return JSON string of the mock response
   */
  private String buildMockSimulateResponse(String serverAccountId, boolean includeClientDomain) {
    // Build mock authorization entries for the response
    List<SorobanAuthorizationEntry> mockEntries = new ArrayList<>();

    // Build invocation with the server account
    LinkedHashMap<SCVal, SCVal> argsMap = new LinkedHashMap<>();
    argsMap.put(Scv.toSymbol("account"), Scv.toString(CLIENT_CONTRACT));
    if (includeClientDomain) {
      argsMap.put(Scv.toSymbol("client_domain"), Scv.toString(CLIENT_DOMAIN));
      argsMap.put(Scv.toSymbol("client_domain_account"), Scv.toString(CLIENT_DOMAIN_ACCOUNT));
    }
    argsMap.put(Scv.toSymbol("home_domain"), Scv.toString(HOME_DOMAIN));
    argsMap.put(Scv.toSymbol("nonce"), Scv.toString(NONCE));
    argsMap.put(Scv.toSymbol("web_auth_domain"), Scv.toString(WEB_AUTH_DOMAIN));
    argsMap.put(Scv.toSymbol("web_auth_domain_account"), Scv.toString(serverAccountId));

    InvokeContractArgs contractArgs =
        InvokeContractArgs.builder()
            .contractAddress(new Address(WEB_AUTH_CONTRACT).toSCAddress())
            .functionName(new SCSymbol(new XdrString(Sep45Challenge.WEB_AUTH_VERIFY_FUNCTION_NAME)))
            .args(new SCVal[] {Scv.toMap(argsMap)})
            .build();

    SorobanAuthorizedFunction function =
        SorobanAuthorizedFunction.builder()
            .discriminant(
                SorobanAuthorizedFunctionType.SOROBAN_AUTHORIZED_FUNCTION_TYPE_CONTRACT_FN)
            .contractFn(contractArgs)
            .build();

    SorobanAuthorizedInvocation invocation =
        SorobanAuthorizedInvocation.builder()
            .function(function)
            .subInvocations(new SorobanAuthorizedInvocation[0])
            .build();

    // Client entry
    mockEntries.add(buildAuthorizationEntry(CLIENT_CONTRACT, invocation));
    // Server entry
    mockEntries.add(buildAuthorizationEntry(serverAccountId, invocation));
    if (includeClientDomain) {
      mockEntries.add(buildAuthorizationEntry(CLIENT_DOMAIN_ACCOUNT, invocation));
    }

    // Convert entries to XDR base64 strings
    List<String> authXdrList = new ArrayList<>();
    for (SorobanAuthorizationEntry entry : mockEntries) {
      try {
        authXdrList.add(entry.toXdrBase64());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    StringBuilder authArrayJson = new StringBuilder("[");
    for (int i = 0; i < authXdrList.size(); i++) {
      authArrayJson.append("\"").append(authXdrList.get(i)).append("\"");
      if (i < authXdrList.size() - 1) {
        authArrayJson.append(",");
      }
    }
    authArrayJson.append("]");

    return "{\n"
        + "  \"jsonrpc\": \"2.0\",\n"
        + "  \"id\": 1,\n"
        + "  \"result\": {\n"
        + "    \"transactionData\": \"AAAAAAAAAAIAAAAGAAAAAem354u9STQWq5b3Ed1j9tOemvL7xV0NPwhn4gXg0AP8AAAAFAAAAAEAAAAH8dTe2OoI0BnhlDbH0fWvXmvprkBvBAgKIcL9busuuMEAAAABAAAABgAAAAHpt+eLvUk0FquW9xHdY/bTnpry+8VdDT8IZ+IF4NAD/AAAABAAAAABAAAAAgAAAA8AAAAHQ291bnRlcgAAAAASAAAAAAAAAABYt8SiyPKXqo89JHEoH9/M7K/kjlZjMT7BjhKnPsqYoQAAAAEAHifGAAAFlAAAAIgAAAAAAAAAAg==\",\n"
        + "    \"minResourceFee\": \"58181\",\n"
        + "    \"events\": [],\n"
        + "    \"results\": [{\"auth\": "
        + authArrayJson
        + ", \"xdr\": \"AAAAAwAAABQ=\"}],\n"
        + "    \"latestLedger\": 14245\n"
        + "  }\n"
        + "}";
  }

  /**
   * Converts a list of {@link SorobanAuthorizationEntry} to a base64-encoded XDR string.
   *
   * @param entries The list of authorization entries to encode.
   * @return The base64-encoded XDR string.
   */
  public static String authorizationEntriesToXdr(
      @NonNull Collection<SorobanAuthorizationEntry> entries) {
    org.stellar.sdk.xdr.SorobanAuthorizationEntries xdrEntries =
        new org.stellar.sdk.xdr.SorobanAuthorizationEntries();
    xdrEntries.setSorobanAuthorizationEntries(entries.toArray(new SorobanAuthorizationEntry[0]));
    try {
      return xdrEntries.toXdrBase64();
    } catch (IOException e) {
      throw new IllegalArgumentException("Failed to encode authorization entries", e);
    }
  }
}
