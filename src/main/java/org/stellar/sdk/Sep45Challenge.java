package org.stellar.sdk;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.jetbrains.annotations.Nullable;
import org.stellar.sdk.exception.InvalidSep45ChallengeException;
import org.stellar.sdk.operations.InvokeHostFunctionOperation;
import org.stellar.sdk.responses.sorobanrpc.SimulateTransactionResponse;
import org.stellar.sdk.scval.Scv;
import org.stellar.sdk.xdr.HostFunction;
import org.stellar.sdk.xdr.HostFunctionType;
import org.stellar.sdk.xdr.InvokeContractArgs;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;
import org.stellar.sdk.xdr.SorobanAddressCredentials;
import org.stellar.sdk.xdr.SorobanAuthorizationEntries;
import org.stellar.sdk.xdr.SorobanAuthorizationEntry;
import org.stellar.sdk.xdr.SorobanAuthorizedFunctionType;
import org.stellar.sdk.xdr.SorobanAuthorizedInvocation;
import org.stellar.sdk.xdr.SorobanCredentials;
import org.stellar.sdk.xdr.SorobanCredentialsType;

/**
 * Stellar Web Authentication Utilities for Contract Accounts (SEP-0045).
 *
 * <p>This class provides utilities for building, reading, and verifying SEP-45 challenge
 * authorization entries for contract account authentication on the Stellar network.
 *
 * @see <a
 *     href="https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0045.md">SEP-0045</a>
 */
public class Sep45Challenge {
  /** The expected function name for SEP-45 web authentication. */
  public static final String WEB_AUTH_VERIFY_FUNCTION_NAME = "web_auth_verify";

  /**
   * A null account used for simulation purposes. This is the account
   * "GAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAWHF".
   */
  public static final String NULL_ACCOUNT =
      "GAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAWHF";

  private Sep45Challenge() {
    // no instance
  }

  /** Default number of ledgers until authorization expires (~15 minutes at 5 seconds/ledger). */
  public static final long DEFAULT_EXPIRE_IN_LEDGERS = 180;

  /**
   * Builds a SEP-45 challenge authorization entries for a client contract account.
   *
   * <p>This method creates challenge authorization entries that can be used to authenticate a
   * contract account.
   *
   * @param server The Soroban RPC server to use for simulating the transaction.
   * @param serverSigner The server's signing keypair.
   * @param clientContractId The client's contract account ID (C... address).
   * @param homeDomain The home domain of the service requiring authentication.
   * @param webAuthDomain The domain of the web authentication service.
   * @param webAuthContractId The contract ID for the web authentication contract.
   * @param network The Stellar network.
   * @param nonce Optional nonce value. If null, a random 48-byte value will be generated and
   *     base64-encoded.
   * @param expireInLedgers Number of ledgers from current ledger until authorization expires. If
   *     null, defaults to {@link #DEFAULT_EXPIRE_IN_LEDGERS} (~15 minutes).
   * @return A SorobanAuthorizationEntries containing the authentication entries.
   * @throws InvalidSep45ChallengeException If building the challenge fails.
   */
  public static SorobanAuthorizationEntries buildChallengeAuthorizationEntries(
      @NonNull SorobanServer server,
      @NonNull KeyPair serverSigner,
      @NonNull String clientContractId,
      @NonNull String homeDomain,
      @NonNull String webAuthDomain,
      @NonNull String webAuthContractId,
      @NonNull Network network,
      @Nullable String nonce,
      @Nullable Long expireInLedgers) {
    return buildChallengeAuthorizationEntries(
        server,
        serverSigner,
        clientContractId,
        homeDomain,
        webAuthDomain,
        webAuthContractId,
        network,
        nonce,
        expireInLedgers,
        null,
        null);
  }

  /**
   * Builds a SEP-45 challenge authorization entries for a client contract account with optional
   * client domain verification.
   *
   * <p>This method creates challenge authorization entries that can be used to authenticate a
   * contract account, optionally including client domain verification.
   *
   * @param server The Soroban RPC server to use for simulating the transaction.
   * @param serverSigner The server's signing keypair.
   * @param clientContractId The client's contract account ID (C... address).
   * @param homeDomain The home domain of the service requiring authentication.
   * @param webAuthDomain The domain of the web authentication service.
   * @param webAuthContractId The contract ID for the web authentication contract.
   * @param network The Stellar network.
   * @param nonce Optional nonce value. If null, a random 48-byte value will be generated and
   *     base64-encoded.
   * @param expireInLedgers Number of ledgers from current ledger until authorization expires. If
   *     null, defaults to {@link #DEFAULT_EXPIRE_IN_LEDGERS} (~15 minutes).
   * @param clientDomain Optional client domain for client domain verification.
   * @param clientDomainAccountId Optional client domain account ID (G... address) for verification.
   * @return A SorobanAuthorizationEntries containing the authentication entries.
   * @throws InvalidSep45ChallengeException If building the challenge fails.
   */
  public static SorobanAuthorizationEntries buildChallengeAuthorizationEntries(
      @NonNull SorobanServer server,
      @NonNull KeyPair serverSigner,
      @NonNull String clientContractId,
      @NonNull String homeDomain,
      @NonNull String webAuthDomain,
      @NonNull String webAuthContractId,
      @NonNull Network network,
      @Nullable String nonce,
      @Nullable Long expireInLedgers,
      @Nullable String clientDomain,
      @Nullable String clientDomainAccountId) {

    // Use default expiration if not specified
    if (expireInLedgers == null) {
      expireInLedgers = DEFAULT_EXPIRE_IN_LEDGERS;
    }

    // Validate client contract ID
    if (!StrKey.isValidContract(clientContractId)) {
      throw new InvalidSep45ChallengeException(
          "clientContractId: " + clientContractId + " is not a valid contract id");
    }

    // Validate web auth contract ID
    if (!StrKey.isValidContract(webAuthContractId)) {
      throw new InvalidSep45ChallengeException(
          "webAuthContractId: " + webAuthContractId + " is not a valid contract id");
    }

    // Validate client domain and client domain account ID consistency
    if ((clientDomain == null) != (clientDomainAccountId == null)) {
      throw new InvalidSep45ChallengeException(
          "clientDomain and clientDomainAccountId must both be provided or both be null");
    }

    // Generate nonce if not provided (48 random bytes, base64-encoded)
    if (nonce == null) {
      byte[] nonceBytes = new byte[48];
      new SecureRandom().nextBytes(nonceBytes);
      nonce = Base64Factory.getInstance().encodeToString(nonceBytes);
    }

    // Build the function arguments as a Map<Symbol, String>
    LinkedHashMap<SCVal, SCVal> argsMap = new LinkedHashMap<>();
    argsMap.put(Scv.toSymbol("account"), Scv.toString(clientContractId));
    if (clientDomain != null) {
      argsMap.put(Scv.toSymbol("client_domain"), Scv.toString(clientDomain));
      argsMap.put(Scv.toSymbol("client_domain_account"), Scv.toString(clientDomainAccountId));
    }
    argsMap.put(Scv.toSymbol("home_domain"), Scv.toString(homeDomain));
    argsMap.put(Scv.toSymbol("nonce"), Scv.toString(nonce));
    argsMap.put(Scv.toSymbol("web_auth_domain"), Scv.toString(webAuthDomain));
    argsMap.put(Scv.toSymbol("web_auth_domain_account"), Scv.toString(serverSigner.getAccountId()));

    // Build the invocation with a single Map argument
    InvokeHostFunctionOperation operation =
        InvokeHostFunctionOperation.invokeContractFunctionOperationBuilder(
                webAuthContractId,
                WEB_AUTH_VERIFY_FUNCTION_NAME,
                Collections.singletonList(Scv.toMap(argsMap)))
            .build();

    // Create a transaction for simulation
    Account nullAccount = new Account(NULL_ACCOUNT, 0L);
    Transaction transaction =
        new TransactionBuilder(nullAccount, network)
            .setBaseFee(100)
            .addOperation(operation)
            .addPreconditions(
                TransactionPreconditions.builder().timeBounds(new TimeBounds(0, 0)).build())
            .build();

    // Simulate the transaction
    SimulateTransactionResponse simulateResponse = server.simulateTransaction(transaction);

    if (simulateResponse.getError() != null) {
      throw new InvalidSep45ChallengeException(
          "Transaction simulation failed: " + simulateResponse.getError());
    }

    if (simulateResponse.getResults() == null || simulateResponse.getResults().isEmpty()) {
      throw new InvalidSep45ChallengeException("Transaction simulation did not return any results");
    }

    SimulateTransactionResponse.SimulateHostFunctionResult result =
        simulateResponse.getResults().get(0);

    if (result.getAuth() == null || result.getAuth().isEmpty()) {
      throw new InvalidSep45ChallengeException(
          "Transaction simulation did not return any authorization entries");
    }

    // Calculate the absolute signature expiration ledger
    long signatureExpirationLedger = simulateResponse.getLatestLedger() + expireInLedgers;

    // Parse and sign the authorization entries
    List<SorobanAuthorizationEntry> signedEntries = new ArrayList<>();
    for (String authXdr : result.getAuth()) {
      SorobanAuthorizationEntry entry;
      try {
        entry = SorobanAuthorizationEntry.fromXdrBase64(authXdr);
      } catch (IOException e) {
        throw new InvalidSep45ChallengeException(
            "Failed to parse authorization entry: " + authXdr, e);
      }

      // Check if this entry needs to be signed by the server
      if (entry.getCredentials().getDiscriminant()
          == SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS) {
        Address address = Address.fromSCAddress(entry.getCredentials().getAddress().getAddress());
        if (address.getAddressType() == Address.AddressType.ACCOUNT
            && address.getEncodedAddress().equals(serverSigner.getAccountId())) {
          // Sign this entry with the server signer
          entry = Auth.authorizeEntry(entry, serverSigner, signatureExpirationLedger, network);
        }
      }

      signedEntries.add(entry);
    }

    return new SorobanAuthorizationEntries(signedEntries.toArray(new SorobanAuthorizationEntry[0]));
  }

  /**
   * Reads and validates a SEP-45 challenge authorization entries without verifying signatures.
   *
   * <p>This method decodes the authorization entries, validates their structure, and extracts the
   * challenge data. It does not verify the signatures; use {@link
   * #verifyChallengeAuthorizationEntries} for full verification.
   *
   * @param authorizationEntriesXdr The base64 XDR-encoded authorization entries.
   * @param serverAccountId The expected server account ID (G... address).
   * @param webAuthContractId The expected web authentication contract ID (C... address).
   * @param homeDomains A list of acceptable home domains.
   * @param webAuthDomain The expected web auth domain.
   * @return A {@link ChallengeAuthorizationEntries} object containing the parsed challenge data.
   * @throws InvalidSep45ChallengeException If the challenge is invalid.
   */
  public static ChallengeAuthorizationEntries readChallengeAuthorizationEntries(
      @NonNull String authorizationEntriesXdr,
      @NonNull String serverAccountId,
      @NonNull String webAuthContractId,
      @NonNull String[] homeDomains,
      @NonNull String webAuthDomain) {

    if (homeDomains.length == 0) {
      throw new InvalidSep45ChallengeException(
          "At least one domain name must be included in homeDomains.");
    }

    // Validate server account ID
    if (!StrKey.isValidEd25519PublicKey(serverAccountId)) {
      throw new InvalidSep45ChallengeException(
          "serverAccountId: " + serverAccountId + " is not a valid account id");
    }

    // Validate web auth contract ID
    if (!StrKey.isValidContract(webAuthContractId)) {
      throw new InvalidSep45ChallengeException(
          "webAuthContractId: " + webAuthContractId + " is not a valid contract id");
    }

    // Decode the authorization entries
    List<SorobanAuthorizationEntry> entries;
    try {
      org.stellar.sdk.xdr.SorobanAuthorizationEntries xdrEntries =
          org.stellar.sdk.xdr.SorobanAuthorizationEntries.fromXdrBase64(authorizationEntriesXdr);
      entries = Arrays.asList(xdrEntries.getSorobanAuthorizationEntries());
    } catch (IOException e) {
      throw new InvalidSep45ChallengeException("Failed to parse authorization entries XDR", e);
    }

    // Validate we have at least 2 entries (server + client)
    if (entries.size() < 2) {
      throw new InvalidSep45ChallengeException(
          "Authorization entries must contain at least 2 entries (server and client)");
    }

    // Get the reference invocation from the first entry
    SorobanAuthorizedInvocation referenceInvocation = entries.get(0).getRootInvocation();

    // Validate and extract the contract call information
    if (referenceInvocation.getFunction().getDiscriminant()
        != SorobanAuthorizedFunctionType.SOROBAN_AUTHORIZED_FUNCTION_TYPE_CONTRACT_FN) {
      throw new InvalidSep45ChallengeException(
          "Authorization entry function type must be SOROBAN_AUTHORIZED_FUNCTION_TYPE_CONTRACT_FN");
    }

    // Validate no sub-invocations
    if (referenceInvocation.getSubInvocations() != null
        && referenceInvocation.getSubInvocations().length > 0) {
      throw new InvalidSep45ChallengeException("Authorization entry must not have sub-invocations");
    }

    InvokeContractArgs contractArgs = referenceInvocation.getFunction().getContractFn();

    // Validate contract address
    Address contractAddress = Address.fromSCAddress(contractArgs.getContractAddress());
    if (!contractAddress.getEncodedAddress().equals(webAuthContractId)) {
      throw new InvalidSep45ChallengeException(
          String.format(
              "Contract address mismatch: expected %s, got %s",
              webAuthContractId, contractAddress.getEncodedAddress()));
    }

    // Validate function name
    String functionName = contractArgs.getFunctionName().getSCSymbol().toString();
    if (!WEB_AUTH_VERIFY_FUNCTION_NAME.equals(functionName)) {
      throw new InvalidSep45ChallengeException(
          String.format(
              "Function name mismatch: expected %s, got %s",
              WEB_AUTH_VERIFY_FUNCTION_NAME, functionName));
    }

    // Parse the function arguments - expect exactly one Map argument
    SCVal[] args = contractArgs.getArgs();
    if (args.length != 1) {
      throw new InvalidSep45ChallengeException(
          "Expected exactly one argument in contract function call");
    }

    SCVal argsVal = args[0];
    if (argsVal.getDiscriminant() != SCValType.SCV_MAP) {
      throw new InvalidSep45ChallengeException("Expected Map argument in contract function call");
    }

    LinkedHashMap<SCVal, SCVal> argsMap = Scv.fromMap(argsVal);

    String account = null;
    String homeDomain = null;
    String nonce = null;
    String webAuthDomainValue = null;
    String webAuthDomainAccount = null;
    String clientDomain = null;
    String clientDomainAccount = null;

    for (java.util.Map.Entry<SCVal, SCVal> entry : argsMap.entrySet()) {
      SCVal keyVal = entry.getKey();
      SCVal valueVal = entry.getValue();

      if (keyVal.getDiscriminant() != SCValType.SCV_SYMBOL) {
        throw new InvalidSep45ChallengeException("Argument key must be a symbol");
      }

      String key = Scv.fromSymbol(keyVal);

      switch (key) {
        case "account":
          account = new String(Scv.fromString(valueVal), StandardCharsets.UTF_8);
          break;
        case "home_domain":
          homeDomain = new String(Scv.fromString(valueVal), StandardCharsets.UTF_8);
          break;
        case "nonce":
          nonce = new String(Scv.fromString(valueVal), StandardCharsets.UTF_8);
          break;
        case "web_auth_domain":
          webAuthDomainValue = new String(Scv.fromString(valueVal), StandardCharsets.UTF_8);
          break;
        case "web_auth_domain_account":
          webAuthDomainAccount = new String(Scv.fromString(valueVal), StandardCharsets.UTF_8);
          break;
        case "client_domain":
          clientDomain = new String(Scv.fromString(valueVal), StandardCharsets.UTF_8);
          break;
        case "client_domain_account":
          clientDomainAccount = new String(Scv.fromString(valueVal), StandardCharsets.UTF_8);
          break;
        default:
          // Unknown argument, ignore
          break;
      }
    }

    // Validate required fields
    if (account == null) {
      throw new InvalidSep45ChallengeException("Missing required argument: account");
    }
    if (homeDomain == null) {
      throw new InvalidSep45ChallengeException("Missing required argument: home_domain");
    }
    if (nonce == null) {
      throw new InvalidSep45ChallengeException("Missing required argument: nonce");
    }
    if (webAuthDomainValue == null) {
      throw new InvalidSep45ChallengeException("Missing required argument: web_auth_domain");
    }
    if (webAuthDomainAccount == null) {
      throw new InvalidSep45ChallengeException(
          "Missing required argument: web_auth_domain_account");
    }

    // Validate client contract ID
    if (!StrKey.isValidContract(account)) {
      throw new InvalidSep45ChallengeException(
          "account: " + account + " is not a valid contract id");
    }

    // Validate home domain
    boolean homeDomainMatched = false;
    for (String domain : homeDomains) {
      if (domain.equals(homeDomain)) {
        homeDomainMatched = true;
        break;
      }
    }
    if (!homeDomainMatched) {
      throw new InvalidSep45ChallengeException(
          "home_domain: " + homeDomain + " is not in the list of allowed home domains");
    }

    // Validate web auth domain
    if (!webAuthDomain.equals(webAuthDomainValue)) {
      throw new InvalidSep45ChallengeException(
          String.format(
              "web_auth_domain mismatch: expected %s, got %s", webAuthDomain, webAuthDomainValue));
    }

    // Validate web auth domain account
    if (!serverAccountId.equals(webAuthDomainAccount)) {
      throw new InvalidSep45ChallengeException(
          String.format(
              "web_auth_domain_account mismatch: expected %s, got %s",
              serverAccountId, webAuthDomainAccount));
    }

    // Validate client domain and client domain account consistency
    if ((clientDomain == null) != (clientDomainAccount == null)) {
      throw new InvalidSep45ChallengeException(
          "client_domain and client_domain_account must both be present or both be absent");
    }

    // Validate all entries have the same root invocation
    for (int i = 1; i < entries.size(); i++) {
      SorobanAuthorizedInvocation currentInvocation = entries.get(i).getRootInvocation();
      if (!referenceInvocation.equals(currentInvocation)) {
        throw new InvalidSep45ChallengeException(
            "All authorization entries must have the same root invocation");
      }
    }

    // Validate credentials types
    boolean foundServerCredential = false;
    boolean foundClientCredential = false;
    boolean foundClientDomainCredential = false;

    for (SorobanAuthorizationEntry entry : entries) {
      SorobanCredentials credentials = entry.getCredentials();
      if (credentials.getDiscriminant() != SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS) {
        throw new InvalidSep45ChallengeException(
            "All authorization entries must have SOROBAN_CREDENTIALS_ADDRESS type");
      }

      SorobanAddressCredentials addressCredentials = credentials.getAddress();
      Address credentialAddress = Address.fromSCAddress(addressCredentials.getAddress());
      String encodedAddress = credentialAddress.getEncodedAddress();

      if (encodedAddress.equals(serverAccountId)) {
        foundServerCredential = true;
      } else if (encodedAddress.equals(account)) {
        foundClientCredential = true;
      } else if (encodedAddress.equals(clientDomainAccount)) {
        foundClientDomainCredential = true;
      }
    }

    if (!foundServerCredential) {
      throw new InvalidSep45ChallengeException(
          "Authorization entries must include a credential for the server account");
    }

    if (!foundClientCredential) {
      throw new InvalidSep45ChallengeException(
          "Authorization entries must include a credential for the client account");
    }

    if (clientDomainAccount != null && !foundClientDomainCredential) {
      throw new InvalidSep45ChallengeException(
          "Authorization entries must include a credential for client_domain_account: "
              + clientDomainAccount);
    }

    return ChallengeAuthorizationEntries.builder()
        .authorizationEntries(entries)
        .clientContractId(account)
        .serverAccountId(serverAccountId)
        .webAuthContractId(webAuthContractId)
        .homeDomain(homeDomain)
        .webAuthDomain(webAuthDomainValue)
        .nonce(nonce)
        .clientDomain(clientDomain)
        .clientDomainAccountId(clientDomainAccount)
        .build();
  }

  /**
   * Reads and validates a SEP-45 challenge authorization entries without verifying signatures.
   *
   * @param authorizationEntriesXdr The base64 XDR-encoded authorization entries.
   * @param serverAccountId The expected server account ID (G... address).
   * @param webAuthContractId The expected web authentication contract ID (C... address).
   * @param homeDomain The expected home domain.
   * @param webAuthDomain The expected web auth domain.
   * @return A {@link ChallengeAuthorizationEntries} object containing the parsed challenge data.
   * @throws InvalidSep45ChallengeException If the challenge is invalid.
   */
  public static ChallengeAuthorizationEntries readChallengeAuthorizationEntries(
      @NonNull String authorizationEntriesXdr,
      @NonNull String serverAccountId,
      @NonNull String webAuthContractId,
      @NonNull String homeDomain,
      @NonNull String webAuthDomain) {
    return readChallengeAuthorizationEntries(
        authorizationEntriesXdr,
        serverAccountId,
        webAuthContractId,
        new String[] {homeDomain},
        webAuthDomain);
  }

  /**
   * Verifies a SEP-45 challenge authorization entries by simulating the transaction.
   *
   * <p>Since contract accounts cannot be queried for signers like traditional Stellar accounts, we
   * verify signatures by simulating the transaction. A successful simulation indicates valid
   * signatures.
   *
   * @param server The Soroban RPC server to use for simulating the transaction.
   * @param authorizationEntriesXdr The base64 XDR-encoded authorization entries.
   * @param serverAccountId The expected server account ID (G... address).
   * @param webAuthContractId The expected web authentication contract ID (C... address).
   * @param homeDomains A list of acceptable home domains.
   * @param webAuthDomain The expected web auth domain.
   * @param network The Stellar network.
   * @return A {@link ChallengeAuthorizationEntries} object containing the verified challenge data.
   * @throws InvalidSep45ChallengeException If the challenge is invalid or verification fails.
   */
  public static ChallengeAuthorizationEntries verifyChallengeAuthorizationEntries(
      @NonNull SorobanServer server,
      @NonNull String authorizationEntriesXdr,
      @NonNull String serverAccountId,
      @NonNull String webAuthContractId,
      @NonNull String[] homeDomains,
      @NonNull String webAuthDomain,
      @NonNull Network network) {

    // First, read and validate the challenge structure
    ChallengeAuthorizationEntries challenge =
        readChallengeAuthorizationEntries(
            authorizationEntriesXdr,
            serverAccountId,
            webAuthContractId,
            homeDomains,
            webAuthDomain);

    // Build a transaction with the authorization entries
    List<SorobanAuthorizationEntry> entries = challenge.getAuthorizationEntries();
    SorobanAuthorizedInvocation invocation = entries.get(0).getRootInvocation();
    InvokeContractArgs contractArgs = invocation.getFunction().getContractFn();

    InvokeHostFunctionOperation operation =
        InvokeHostFunctionOperation.builder()
            .hostFunction(
                HostFunction.builder()
                    .discriminant(HostFunctionType.HOST_FUNCTION_TYPE_INVOKE_CONTRACT)
                    .invokeContract(contractArgs)
                    .build())
            .auth(entries)
            .build();

    Account nullAccount = new Account(NULL_ACCOUNT, 0L);
    Transaction transaction =
        new TransactionBuilder(nullAccount, network)
            .setBaseFee(100)
            .addOperation(operation)
            .addPreconditions(
                TransactionPreconditions.builder().timeBounds(new TimeBounds(0, 0)).build())
            .build();

    // Simulate the transaction to verify signatures
    SimulateTransactionResponse simulateResponse = server.simulateTransaction(transaction);

    if (simulateResponse.getError() != null) {
      throw new InvalidSep45ChallengeException(
          "Signature verification failed: " + simulateResponse.getError());
    }

    return challenge;
  }

  /**
   * Verifies a SEP-45 challenge authorization entries by simulating the transaction.
   *
   * @param server The Soroban RPC server to use for simulating the transaction.
   * @param authorizationEntriesXdr The base64 XDR-encoded authorization entries.
   * @param serverAccountId The expected server account ID (G... address).
   * @param webAuthContractId The expected web authentication contract ID (C... address).
   * @param homeDomain The expected home domain.
   * @param webAuthDomain The expected web auth domain.
   * @param network The Stellar network.
   * @return A {@link ChallengeAuthorizationEntries} object containing the verified challenge data.
   * @throws InvalidSep45ChallengeException If the challenge is invalid or verification fails.
   */
  public static ChallengeAuthorizationEntries verifyChallengeAuthorizationEntries(
      @NonNull SorobanServer server,
      @NonNull String authorizationEntriesXdr,
      @NonNull String serverAccountId,
      @NonNull String webAuthContractId,
      @NonNull String homeDomain,
      @NonNull String webAuthDomain,
      @NonNull Network network) {
    return verifyChallengeAuthorizationEntries(
        server,
        authorizationEntriesXdr,
        serverAccountId,
        webAuthContractId,
        new String[] {homeDomain},
        webAuthDomain,
        network);
  }

  /** Contains the parsed data from a SEP-45 challenge. */
  @Value
  @Builder
  public static class ChallengeAuthorizationEntries {
    /** The list of authorization entries. */
    @NonNull List<SorobanAuthorizationEntry> authorizationEntries;

    /** The client contract account ID (C... address). */
    @NonNull String clientContractId;

    /** The server account ID (G... address). */
    @NonNull String serverAccountId;

    /** The web authentication contract ID (C... address). */
    @NonNull String webAuthContractId;

    /** The home domain. */
    @NonNull String homeDomain;

    /** The web auth domain. */
    @NonNull String webAuthDomain;

    /** The nonce value. */
    @NonNull String nonce;

    /** The client domain (optional, for client domain verification). */
    @Nullable String clientDomain;

    /** The client domain account ID (optional, for client domain verification). */
    @Nullable String clientDomainAccountId;
  }
}
