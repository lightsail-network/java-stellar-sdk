package org.stellar.sdk;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import org.junit.Test;
import org.stellar.sdk.scval.Scv;
import org.stellar.sdk.xdr.EnvelopeType;
import org.stellar.sdk.xdr.Hash;
import org.stellar.sdk.xdr.HashIDPreimage;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.InvokeContractArgs;
import org.stellar.sdk.xdr.SCAddress;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;
import org.stellar.sdk.xdr.SorobanAddressCredentials;
import org.stellar.sdk.xdr.SorobanAddressCredentialsWithDelegates;
import org.stellar.sdk.xdr.SorobanAuthorizationEntry;
import org.stellar.sdk.xdr.SorobanAuthorizedFunction;
import org.stellar.sdk.xdr.SorobanAuthorizedFunctionType;
import org.stellar.sdk.xdr.SorobanAuthorizedInvocation;
import org.stellar.sdk.xdr.SorobanCredentials;
import org.stellar.sdk.xdr.SorobanCredentialsType;
import org.stellar.sdk.xdr.SorobanDelegateSignature;
import org.stellar.sdk.xdr.Uint32;
import org.stellar.sdk.xdr.XdrUnsignedInteger;

public class AuthTest {
  @Test
  public void testSignAuthorizeEntryWithBase64EntryAndKeypairSigner() throws IOException {
    String contractId = "CDCYWK73YTYFJZZSJ5V7EDFNHYBG4QN3VUNG2IGD27KJDDPNCZKBCBXK";
    KeyPair signer =
        KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");
    long validUntilLedgerSeq = 654656L;
    Network network = Network.TESTNET;

    SorobanCredentials credentials =
        SorobanCredentials.builder()
            .discriminant(SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS)
            .address(
                SorobanAddressCredentials.builder()
                    .address(new Address(signer.getAccountId()).toSCAddress())
                    .nonce(new Int64(123456789L))
                    .signatureExpirationLedger(new Uint32(new XdrUnsignedInteger(0L)))
                    .signature(Scv.toVoid())
                    .build())
            .build();
    SorobanAuthorizedInvocation invocation =
        SorobanAuthorizedInvocation.builder()
            .function(
                SorobanAuthorizedFunction.builder()
                    .discriminant(
                        SorobanAuthorizedFunctionType.SOROBAN_AUTHORIZED_FUNCTION_TYPE_CONTRACT_FN)
                    .contractFn(
                        InvokeContractArgs.builder()
                            .contractAddress(new Address(contractId).toSCAddress())
                            .functionName(Scv.toSymbol("increment").getSym())
                            .args(new SCVal[0])
                            .build())
                    .build())
            .subInvocations(new SorobanAuthorizedInvocation[0])
            .build();
    SorobanAuthorizationEntry entry =
        SorobanAuthorizationEntry.builder()
            .credentials(credentials)
            .rootInvocation(invocation)
            .build();

    SorobanAuthorizationEntry signedEntry =
        Auth.authorizeEntry(entry.toXdrBase64(), signer, validUntilLedgerSeq, network);

    HashIDPreimage preimage =
        HashIDPreimage.builder()
            .discriminant(EnvelopeType.ENVELOPE_TYPE_SOROBAN_AUTHORIZATION)
            .sorobanAuthorization(
                HashIDPreimage.HashIDPreimageSorobanAuthorization.builder()
                    .networkID(new Hash(network.getNetworkId()))
                    .nonce(new Int64(123456789L))
                    .invocation(invocation)
                    .signatureExpirationLedger(
                        new Uint32(new XdrUnsignedInteger(validUntilLedgerSeq)))
                    .build())
            .build();
    byte[] signature = signer.sign(Util.hash(preimage.toXdrByteArray()));
    SorobanCredentials expectedCredentials =
        SorobanCredentials.builder()
            .discriminant(SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS)
            .address(
                SorobanAddressCredentials.builder()
                    .address(new Address(signer.getAccountId()).toSCAddress())
                    .nonce(new Int64(123456789L))
                    .signatureExpirationLedger(
                        new Uint32(new XdrUnsignedInteger(validUntilLedgerSeq)))
                    .signature(
                        Scv.toVec(
                            Collections.singleton(
                                Scv.toMap(
                                    new LinkedHashMap<SCVal, SCVal>() {
                                      {
                                        put(
                                            Scv.toSymbol("public_key"),
                                            Scv.toBytes(signer.getPublicKey()));
                                        put(Scv.toSymbol("signature"), Scv.toBytes(signature));
                                      }
                                    }))))
                    .build())
            .build();

    SorobanAuthorizationEntry expectedEntry =
        SorobanAuthorizationEntry.builder()
            .credentials(expectedCredentials)
            .rootInvocation(invocation)
            .build();
    assertEquals(expectedEntry, signedEntry);
    assertNotSame(entry, signedEntry);
  }

  @Test
  public void testSignAuthorizeEntryWithXdrEntryAndKeypairSigner() throws IOException {
    String contractId = "CDCYWK73YTYFJZZSJ5V7EDFNHYBG4QN3VUNG2IGD27KJDDPNCZKBCBXK";
    KeyPair signer =
        KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");
    long validUntilLedgerSeq = 654656L;
    Network network = Network.TESTNET;

    SorobanCredentials credentials =
        SorobanCredentials.builder()
            .discriminant(SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS)
            .address(
                SorobanAddressCredentials.builder()
                    .address(new Address(signer.getAccountId()).toSCAddress())
                    .nonce(new Int64(123456789L))
                    .signatureExpirationLedger(new Uint32(new XdrUnsignedInteger(0L)))
                    .signature(Scv.toVoid())
                    .build())
            .build();
    SorobanAuthorizedInvocation invocation =
        SorobanAuthorizedInvocation.builder()
            .function(
                SorobanAuthorizedFunction.builder()
                    .discriminant(
                        SorobanAuthorizedFunctionType.SOROBAN_AUTHORIZED_FUNCTION_TYPE_CONTRACT_FN)
                    .contractFn(
                        InvokeContractArgs.builder()
                            .contractAddress(new Address(contractId).toSCAddress())
                            .functionName(Scv.toSymbol("increment").getSym())
                            .args(new SCVal[0])
                            .build())
                    .build())
            .subInvocations(new SorobanAuthorizedInvocation[0])
            .build();
    SorobanAuthorizationEntry entry =
        SorobanAuthorizationEntry.builder()
            .credentials(credentials)
            .rootInvocation(invocation)
            .build();

    SorobanAuthorizationEntry signedEntry =
        Auth.authorizeEntry(entry, signer, validUntilLedgerSeq, network);

    HashIDPreimage preimage =
        HashIDPreimage.builder()
            .discriminant(EnvelopeType.ENVELOPE_TYPE_SOROBAN_AUTHORIZATION)
            .sorobanAuthorization(
                HashIDPreimage.HashIDPreimageSorobanAuthorization.builder()
                    .networkID(new Hash(network.getNetworkId()))
                    .nonce(new Int64(123456789L))
                    .invocation(invocation)
                    .signatureExpirationLedger(
                        new Uint32(new XdrUnsignedInteger(validUntilLedgerSeq)))
                    .build())
            .build();
    byte[] signature = signer.sign(Util.hash(preimage.toXdrByteArray()));
    SorobanCredentials expectedCredentials =
        SorobanCredentials.builder()
            .discriminant(SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS)
            .address(
                SorobanAddressCredentials.builder()
                    .address(new Address(signer.getAccountId()).toSCAddress())
                    .nonce(new Int64(123456789L))
                    .signatureExpirationLedger(
                        new Uint32(new XdrUnsignedInteger(validUntilLedgerSeq)))
                    .signature(
                        Scv.toVec(
                            Collections.singleton(
                                Scv.toMap(
                                    new LinkedHashMap<SCVal, SCVal>() {
                                      {
                                        put(
                                            Scv.toSymbol("public_key"),
                                            Scv.toBytes(signer.getPublicKey()));
                                        put(Scv.toSymbol("signature"), Scv.toBytes(signature));
                                      }
                                    }))))
                    .build())
            .build();

    SorobanAuthorizationEntry expectedEntry =
        SorobanAuthorizationEntry.builder()
            .credentials(expectedCredentials)
            .rootInvocation(invocation)
            .build();
    assertEquals(expectedEntry, signedEntry);
    assertNotSame(entry, signedEntry);
  }

  @Test
  public void testSignAuthorizeEntryWithBase64EntryAndFunctionSigner() throws IOException {
    String contractId = "CDCYWK73YTYFJZZSJ5V7EDFNHYBG4QN3VUNG2IGD27KJDDPNCZKBCBXK";
    KeyPair signer =
        KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");
    Auth.Signer entrySigner =
        preimage ->
            Auth.defaultAccountSignatureScVal(
                signer.getAccountId(), signer.sign(Auth.authorizationPayloadHash(preimage)));
    long validUntilLedgerSeq = 654656L;
    Network network = Network.TESTNET;

    SorobanCredentials credentials =
        SorobanCredentials.builder()
            .discriminant(SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS)
            .address(
                SorobanAddressCredentials.builder()
                    .address(new Address(signer.getAccountId()).toSCAddress())
                    .nonce(new Int64(123456789L))
                    .signatureExpirationLedger(new Uint32(new XdrUnsignedInteger(0L)))
                    .signature(Scv.toVoid())
                    .build())
            .build();
    SorobanAuthorizedInvocation invocation =
        SorobanAuthorizedInvocation.builder()
            .function(
                SorobanAuthorizedFunction.builder()
                    .discriminant(
                        SorobanAuthorizedFunctionType.SOROBAN_AUTHORIZED_FUNCTION_TYPE_CONTRACT_FN)
                    .contractFn(
                        InvokeContractArgs.builder()
                            .contractAddress(new Address(contractId).toSCAddress())
                            .functionName(Scv.toSymbol("increment").getSym())
                            .args(new SCVal[0])
                            .build())
                    .build())
            .subInvocations(new SorobanAuthorizedInvocation[0])
            .build();
    SorobanAuthorizationEntry entry =
        SorobanAuthorizationEntry.builder()
            .credentials(credentials)
            .rootInvocation(invocation)
            .build();

    SorobanAuthorizationEntry signedEntry =
        Auth.authorizeEntry(entry.toXdrBase64(), entrySigner, validUntilLedgerSeq, network);

    HashIDPreimage preimage =
        HashIDPreimage.builder()
            .discriminant(EnvelopeType.ENVELOPE_TYPE_SOROBAN_AUTHORIZATION)
            .sorobanAuthorization(
                HashIDPreimage.HashIDPreimageSorobanAuthorization.builder()
                    .networkID(new Hash(network.getNetworkId()))
                    .nonce(new Int64(123456789L))
                    .invocation(invocation)
                    .signatureExpirationLedger(
                        new Uint32(new XdrUnsignedInteger(validUntilLedgerSeq)))
                    .build())
            .build();
    byte[] signature = signer.sign(Util.hash(preimage.toXdrByteArray()));
    SorobanCredentials expectedCredentials =
        SorobanCredentials.builder()
            .discriminant(SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS)
            .address(
                SorobanAddressCredentials.builder()
                    .address(new Address(signer.getAccountId()).toSCAddress())
                    .nonce(new Int64(123456789L))
                    .signatureExpirationLedger(
                        new Uint32(new XdrUnsignedInteger(validUntilLedgerSeq)))
                    .signature(
                        Scv.toVec(
                            Collections.singleton(
                                Scv.toMap(
                                    new LinkedHashMap<SCVal, SCVal>() {
                                      {
                                        put(
                                            Scv.toSymbol("public_key"),
                                            Scv.toBytes(signer.getPublicKey()));
                                        put(Scv.toSymbol("signature"), Scv.toBytes(signature));
                                      }
                                    }))))
                    .build())
            .build();

    SorobanAuthorizationEntry expectedEntry =
        SorobanAuthorizationEntry.builder()
            .credentials(expectedCredentials)
            .rootInvocation(invocation)
            .build();
    assertEquals(expectedEntry, signedEntry);
    assertNotSame(entry, signedEntry);
  }

  @Test
  public void testSignAuthorizeEntryWithXdrEntryAndFunctionSigner() throws IOException {
    String contractId = "CDCYWK73YTYFJZZSJ5V7EDFNHYBG4QN3VUNG2IGD27KJDDPNCZKBCBXK";
    KeyPair signer =
        KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");
    Auth.Signer entrySigner =
        preimage ->
            Auth.defaultAccountSignatureScVal(
                signer.getAccountId(), signer.sign(Auth.authorizationPayloadHash(preimage)));
    long validUntilLedgerSeq = 654656L;
    Network network = Network.TESTNET;

    SorobanCredentials credentials =
        SorobanCredentials.builder()
            .discriminant(SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS)
            .address(
                SorobanAddressCredentials.builder()
                    .address(new Address(signer.getAccountId()).toSCAddress())
                    .nonce(new Int64(123456789L))
                    .signatureExpirationLedger(new Uint32(new XdrUnsignedInteger(0L)))
                    .signature(Scv.toVoid())
                    .build())
            .build();
    SorobanAuthorizedInvocation invocation =
        SorobanAuthorizedInvocation.builder()
            .function(
                SorobanAuthorizedFunction.builder()
                    .discriminant(
                        SorobanAuthorizedFunctionType.SOROBAN_AUTHORIZED_FUNCTION_TYPE_CONTRACT_FN)
                    .contractFn(
                        InvokeContractArgs.builder()
                            .contractAddress(new Address(contractId).toSCAddress())
                            .functionName(Scv.toSymbol("increment").getSym())
                            .args(new SCVal[0])
                            .build())
                    .build())
            .subInvocations(new SorobanAuthorizedInvocation[0])
            .build();
    SorobanAuthorizationEntry entry =
        SorobanAuthorizationEntry.builder()
            .credentials(credentials)
            .rootInvocation(invocation)
            .build();

    SorobanAuthorizationEntry signedEntry =
        Auth.authorizeEntry(entry, entrySigner, validUntilLedgerSeq, network);

    HashIDPreimage preimage =
        HashIDPreimage.builder()
            .discriminant(EnvelopeType.ENVELOPE_TYPE_SOROBAN_AUTHORIZATION)
            .sorobanAuthorization(
                HashIDPreimage.HashIDPreimageSorobanAuthorization.builder()
                    .networkID(new Hash(network.getNetworkId()))
                    .nonce(new Int64(123456789L))
                    .invocation(invocation)
                    .signatureExpirationLedger(
                        new Uint32(new XdrUnsignedInteger(validUntilLedgerSeq)))
                    .build())
            .build();
    byte[] signature = signer.sign(Util.hash(preimage.toXdrByteArray()));
    SorobanCredentials expectedCredentials =
        SorobanCredentials.builder()
            .discriminant(SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS)
            .address(
                SorobanAddressCredentials.builder()
                    .address(new Address(signer.getAccountId()).toSCAddress())
                    .nonce(new Int64(123456789L))
                    .signatureExpirationLedger(
                        new Uint32(new XdrUnsignedInteger(validUntilLedgerSeq)))
                    .signature(
                        Scv.toVec(
                            Collections.singleton(
                                Scv.toMap(
                                    new LinkedHashMap<SCVal, SCVal>() {
                                      {
                                        put(
                                            Scv.toSymbol("public_key"),
                                            Scv.toBytes(signer.getPublicKey()));
                                        put(Scv.toSymbol("signature"), Scv.toBytes(signature));
                                      }
                                    }))))
                    .build())
            .build();

    SorobanAuthorizationEntry expectedEntry =
        SorobanAuthorizationEntry.builder()
            .credentials(expectedCredentials)
            .rootInvocation(invocation)
            .build();
    assertEquals(expectedEntry, signedEntry);
    assertNotSame(entry, signedEntry);
  }

  @Test
  public void testSignAuthorizeEntryWithSourceCredentialsEntry() {
    String contractId = "CDCYWK73YTYFJZZSJ5V7EDFNHYBG4QN3VUNG2IGD27KJDDPNCZKBCBXK";
    KeyPair signer =
        KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");
    long validUntilLedgerSeq = 654656L;
    Network network = Network.TESTNET;

    SorobanCredentials credentials =
        SorobanCredentials.builder()
            .discriminant(SorobanCredentialsType.SOROBAN_CREDENTIALS_SOURCE_ACCOUNT)
            .build();
    SorobanAuthorizedInvocation invocation =
        SorobanAuthorizedInvocation.builder()
            .function(
                SorobanAuthorizedFunction.builder()
                    .discriminant(
                        SorobanAuthorizedFunctionType.SOROBAN_AUTHORIZED_FUNCTION_TYPE_CONTRACT_FN)
                    .contractFn(
                        InvokeContractArgs.builder()
                            .contractAddress(new Address(contractId).toSCAddress())
                            .functionName(Scv.toSymbol("increment").getSym())
                            .args(new SCVal[0])
                            .build())
                    .build())
            .subInvocations(new SorobanAuthorizedInvocation[0])
            .build();
    SorobanAuthorizationEntry entry =
        SorobanAuthorizationEntry.builder()
            .credentials(credentials)
            .rootInvocation(invocation)
            .build();

    SorobanAuthorizationEntry signedEntry =
        Auth.authorizeEntry(entry, signer, validUntilLedgerSeq, network);
    assertEquals(entry, signedEntry);
    assertNotSame(entry, signedEntry);
  }

  @Test
  public void testSignAuthorizeEntryWithNullSignatureThrows() {
    KeyPair signer =
        KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");
    // A Signer may now return any signature SCVal, attached verbatim with no client-side
    // verification (the network checks it via __check_auth). A null return is the one thing we
    // can sanity-check.
    Auth.Signer nullSigner = preimage -> null;
    SorobanAuthorizationEntry entry =
        SorobanAuthorizationEntry.builder()
            .credentials(
                buildAddressCredentials(new Address(signer.getAccountId()).toSCAddress(), 0L))
            .rootInvocation(buildInvocation())
            .build();

    try {
      Auth.authorizeEntry(entry, nullSigner, 654656L, Network.TESTNET);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("signer returned a null signature", e.getMessage());
    }
  }

  @Test
  public void authorizeInvocationWithKeypairSigner() {
    String contractId = "CDCYWK73YTYFJZZSJ5V7EDFNHYBG4QN3VUNG2IGD27KJDDPNCZKBCBXK";
    KeyPair signer =
        KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");
    long validUntilLedgerSeq = 654656L;
    Network network = Network.TESTNET;

    SorobanAuthorizedInvocation invocation =
        SorobanAuthorizedInvocation.builder()
            .function(
                SorobanAuthorizedFunction.builder()
                    .discriminant(
                        SorobanAuthorizedFunctionType.SOROBAN_AUTHORIZED_FUNCTION_TYPE_CONTRACT_FN)
                    .contractFn(
                        InvokeContractArgs.builder()
                            .contractAddress(new Address(contractId).toSCAddress())
                            .functionName(Scv.toSymbol("increment").getSym())
                            .args(new SCVal[0])
                            .build())
                    .build())
            .subInvocations(new SorobanAuthorizedInvocation[0])
            .build();

    SorobanAuthorizationEntry signedEntry =
        Auth.authorizeInvocation(signer, validUntilLedgerSeq, invocation, network);

    assertEquals(signedEntry.getRootInvocation(), invocation);
    assertEquals(
        signedEntry.getCredentials().getDiscriminant(),
        SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS);
    assertEquals(
        signedEntry.getCredentials().getAddress().getAddress(),
        new Address(signer.getAccountId()).toSCAddress());
    assertEquals(
        signedEntry
            .getCredentials()
            .getAddress()
            .getSignatureExpirationLedger()
            .getUint32()
            .getNumber()
            .longValue(),
        validUntilLedgerSeq);
    assertEquals(
        signedEntry.getCredentials().getAddress().getSignature().getDiscriminant(),
        SCValType.SCV_VEC);
    assertEquals(
        signedEntry.getCredentials().getAddress().getSignature().getVec().getSCVec().length, 1);
  }

  @Test
  public void authorizeInvocationWithFunctionSigner() {
    String contractId = "CDCYWK73YTYFJZZSJ5V7EDFNHYBG4QN3VUNG2IGD27KJDDPNCZKBCBXK";
    KeyPair signer =
        KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");
    Auth.Signer entrySigner =
        preimage ->
            Auth.defaultAccountSignatureScVal(
                signer.getAccountId(), signer.sign(Auth.authorizationPayloadHash(preimage)));
    long validUntilLedgerSeq = 654656L;
    Network network = Network.TESTNET;

    SorobanAuthorizedInvocation invocation =
        SorobanAuthorizedInvocation.builder()
            .function(
                SorobanAuthorizedFunction.builder()
                    .discriminant(
                        SorobanAuthorizedFunctionType.SOROBAN_AUTHORIZED_FUNCTION_TYPE_CONTRACT_FN)
                    .contractFn(
                        InvokeContractArgs.builder()
                            .contractAddress(new Address(contractId).toSCAddress())
                            .functionName(Scv.toSymbol("increment").getSym())
                            .args(new SCVal[0])
                            .build())
                    .build())
            .subInvocations(new SorobanAuthorizedInvocation[0])
            .build();

    SorobanAuthorizationEntry signedEntry =
        Auth.authorizeInvocation(
            entrySigner, signer.getAccountId(), validUntilLedgerSeq, invocation, network);

    assertEquals(signedEntry.getRootInvocation(), invocation);
    assertEquals(
        signedEntry.getCredentials().getDiscriminant(),
        SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS);
    assertEquals(
        signedEntry.getCredentials().getAddress().getAddress(),
        new Address(signer.getAccountId()).toSCAddress());
    assertEquals(
        signedEntry
            .getCredentials()
            .getAddress()
            .getSignatureExpirationLedger()
            .getUint32()
            .getNumber()
            .longValue(),
        validUntilLedgerSeq);
    assertEquals(
        signedEntry.getCredentials().getAddress().getSignature().getDiscriminant(),
        SCValType.SCV_VEC);
    assertEquals(
        signedEntry.getCredentials().getAddress().getSignature().getVec().getSCVec().length, 1);
  }

  @Test
  public void testSignAuthorizeEntryWithKeypairSignerNotEqualCredentialAddress()
      throws IOException {
    String contractId = "CDCYWK73YTYFJZZSJ5V7EDFNHYBG4QN3VUNG2IGD27KJDDPNCZKBCBXK";
    KeyPair signer =
        KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");
    long validUntilLedgerSeq = 654656L;
    Network network = Network.TESTNET;
    String credentialAddress = "GADBBY4WFXKKFJ7CMTG3J5YAUXMQDBILRQ6W3U5IWN5TQFZU4MWZ5T4K";

    SorobanCredentials credentials =
        SorobanCredentials.builder()
            .discriminant(SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS)
            .address(
                SorobanAddressCredentials.builder()
                    .address(new Address(credentialAddress).toSCAddress())
                    .nonce(new Int64(123456789L))
                    .signatureExpirationLedger(new Uint32(new XdrUnsignedInteger(0L)))
                    .signature(Scv.toVoid())
                    .build())
            .build();
    SorobanAuthorizedInvocation invocation =
        SorobanAuthorizedInvocation.builder()
            .function(
                SorobanAuthorizedFunction.builder()
                    .discriminant(
                        SorobanAuthorizedFunctionType.SOROBAN_AUTHORIZED_FUNCTION_TYPE_CONTRACT_FN)
                    .contractFn(
                        InvokeContractArgs.builder()
                            .contractAddress(new Address(contractId).toSCAddress())
                            .functionName(Scv.toSymbol("increment").getSym())
                            .args(new SCVal[0])
                            .build())
                    .build())
            .subInvocations(new SorobanAuthorizedInvocation[0])
            .build();
    SorobanAuthorizationEntry entry =
        SorobanAuthorizationEntry.builder()
            .credentials(credentials)
            .rootInvocation(invocation)
            .build();

    SorobanAuthorizationEntry signedEntry =
        Auth.authorizeEntry(entry, signer, validUntilLedgerSeq, network);

    HashIDPreimage preimage =
        HashIDPreimage.builder()
            .discriminant(EnvelopeType.ENVELOPE_TYPE_SOROBAN_AUTHORIZATION)
            .sorobanAuthorization(
                HashIDPreimage.HashIDPreimageSorobanAuthorization.builder()
                    .networkID(new Hash(network.getNetworkId()))
                    .nonce(new Int64(123456789L))
                    .invocation(invocation)
                    .signatureExpirationLedger(
                        new Uint32(new XdrUnsignedInteger(validUntilLedgerSeq)))
                    .build())
            .build();
    byte[] signature = signer.sign(Util.hash(preimage.toXdrByteArray()));
    SorobanCredentials expectedCredentials =
        SorobanCredentials.builder()
            .discriminant(SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS)
            .address(
                SorobanAddressCredentials.builder()
                    .address(new Address(credentialAddress).toSCAddress())
                    .nonce(new Int64(123456789L))
                    .signatureExpirationLedger(
                        new Uint32(new XdrUnsignedInteger(validUntilLedgerSeq)))
                    .signature(
                        Scv.toVec(
                            Collections.singleton(
                                Scv.toMap(
                                    new LinkedHashMap<SCVal, SCVal>() {
                                      {
                                        put(
                                            Scv.toSymbol("public_key"),
                                            Scv.toBytes(signer.getPublicKey()));
                                        put(Scv.toSymbol("signature"), Scv.toBytes(signature));
                                      }
                                    }))))
                    .build())
            .build();

    SorobanAuthorizationEntry expectedEntry =
        SorobanAuthorizationEntry.builder()
            .credentials(expectedCredentials)
            .rootInvocation(invocation)
            .build();
    assertEquals(expectedEntry, signedEntry);
    assertNotSame(entry, signedEntry);
  }

  @Test
  public void testSignAuthorizeEntryWithFunctionSignerNotEqualCredentialAddress()
      throws IOException {
    String contractId = "CDCYWK73YTYFJZZSJ5V7EDFNHYBG4QN3VUNG2IGD27KJDDPNCZKBCBXK";
    KeyPair signer =
        KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");
    Auth.Signer entrySigner =
        preimage ->
            Auth.defaultAccountSignatureScVal(
                signer.getAccountId(), signer.sign(Auth.authorizationPayloadHash(preimage)));
    long validUntilLedgerSeq = 654656L;
    Network network = Network.TESTNET;
    String credentialAddress = "GADBBY4WFXKKFJ7CMTG3J5YAUXMQDBILRQ6W3U5IWN5TQFZU4MWZ5T4K";

    SorobanCredentials credentials =
        SorobanCredentials.builder()
            .discriminant(SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS)
            .address(
                SorobanAddressCredentials.builder()
                    .address(new Address(credentialAddress).toSCAddress())
                    .nonce(new Int64(123456789L))
                    .signatureExpirationLedger(new Uint32(new XdrUnsignedInteger(0L)))
                    .signature(Scv.toVoid())
                    .build())
            .build();
    SorobanAuthorizedInvocation invocation =
        SorobanAuthorizedInvocation.builder()
            .function(
                SorobanAuthorizedFunction.builder()
                    .discriminant(
                        SorobanAuthorizedFunctionType.SOROBAN_AUTHORIZED_FUNCTION_TYPE_CONTRACT_FN)
                    .contractFn(
                        InvokeContractArgs.builder()
                            .contractAddress(new Address(contractId).toSCAddress())
                            .functionName(Scv.toSymbol("increment").getSym())
                            .args(new SCVal[0])
                            .build())
                    .build())
            .subInvocations(new SorobanAuthorizedInvocation[0])
            .build();
    SorobanAuthorizationEntry entry =
        SorobanAuthorizationEntry.builder()
            .credentials(credentials)
            .rootInvocation(invocation)
            .build();

    SorobanAuthorizationEntry signedEntry =
        Auth.authorizeEntry(entry, entrySigner, validUntilLedgerSeq, network);

    HashIDPreimage preimage =
        HashIDPreimage.builder()
            .discriminant(EnvelopeType.ENVELOPE_TYPE_SOROBAN_AUTHORIZATION)
            .sorobanAuthorization(
                HashIDPreimage.HashIDPreimageSorobanAuthorization.builder()
                    .networkID(new Hash(network.getNetworkId()))
                    .nonce(new Int64(123456789L))
                    .invocation(invocation)
                    .signatureExpirationLedger(
                        new Uint32(new XdrUnsignedInteger(validUntilLedgerSeq)))
                    .build())
            .build();
    byte[] signature = signer.sign(Util.hash(preimage.toXdrByteArray()));
    SorobanCredentials expectedCredentials =
        SorobanCredentials.builder()
            .discriminant(SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS)
            .address(
                SorobanAddressCredentials.builder()
                    .address(new Address(credentialAddress).toSCAddress())
                    .nonce(new Int64(123456789L))
                    .signatureExpirationLedger(
                        new Uint32(new XdrUnsignedInteger(validUntilLedgerSeq)))
                    .signature(
                        Scv.toVec(
                            Collections.singleton(
                                Scv.toMap(
                                    new LinkedHashMap<SCVal, SCVal>() {
                                      {
                                        put(
                                            Scv.toSymbol("public_key"),
                                            Scv.toBytes(signer.getPublicKey()));
                                        put(Scv.toSymbol("signature"), Scv.toBytes(signature));
                                      }
                                    }))))
                    .build())
            .build();

    SorobanAuthorizationEntry expectedEntry =
        SorobanAuthorizationEntry.builder()
            .credentials(expectedCredentials)
            .rootInvocation(invocation)
            .build();
    assertEquals(expectedEntry, signedEntry);
    assertNotSame(entry, signedEntry);
  }

  @Test
  public void testSignAuthorizeEntryWithAddressV2Credentials() throws IOException {
    KeyPair signer =
        KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");
    long validUntilLedgerSeq = 654656L;
    Network network = Network.TESTNET;

    SorobanAuthorizedInvocation invocation = buildInvocation();
    SorobanAuthorizationEntry entry =
        SorobanAuthorizationEntry.builder()
            .credentials(
                buildAddressV2Credentials(new Address(signer.getAccountId()).toSCAddress(), 0L))
            .rootInvocation(invocation)
            .build();

    SorobanAuthorizationEntry signedEntry =
        Auth.authorizeEntry(entry, signer, validUntilLedgerSeq, network);

    // CAP-71-02: the V2 payload is the address-bound
    // ENVELOPE_TYPE_SOROBAN_AUTHORIZATION_WITH_ADDRESS preimage.
    HashIDPreimage preimage =
        HashIDPreimage.builder()
            .discriminant(EnvelopeType.ENVELOPE_TYPE_SOROBAN_AUTHORIZATION_WITH_ADDRESS)
            .sorobanAuthorizationWithAddress(
                HashIDPreimage.HashIDPreimageSorobanAuthorizationWithAddress.builder()
                    .networkID(new Hash(network.getNetworkId()))
                    .nonce(new Int64(123456789L))
                    .signatureExpirationLedger(
                        new Uint32(new XdrUnsignedInteger(validUntilLedgerSeq)))
                    .address(new Address(signer.getAccountId()).toSCAddress())
                    .invocation(invocation)
                    .build())
            .build();
    byte[] signature = signer.sign(Util.hash(preimage.toXdrByteArray()));

    SorobanCredentials expectedCredentials =
        SorobanCredentials.builder()
            .discriminant(SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS_V2)
            .addressV2(
                SorobanAddressCredentials.builder()
                    .address(new Address(signer.getAccountId()).toSCAddress())
                    .nonce(new Int64(123456789L))
                    .signatureExpirationLedger(
                        new Uint32(new XdrUnsignedInteger(validUntilLedgerSeq)))
                    .signature(buildSignatureScVal(signer, signature))
                    .build())
            .build();
    SorobanAuthorizationEntry expectedEntry =
        SorobanAuthorizationEntry.builder()
            .credentials(expectedCredentials)
            .rootInvocation(invocation)
            .build();
    assertEquals(expectedEntry, signedEntry);
    assertNotSame(entry, signedEntry);
  }

  @Test
  public void testBuildAuthorizationEntryPreimageWithAddressCredentials() throws IOException {
    KeyPair signer =
        KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");
    long validUntilLedgerSeq = 654656L;
    Network network = Network.TESTNET;
    SorobanAuthorizedInvocation invocation = buildInvocation();

    SorobanAuthorizationEntry entry =
        SorobanAuthorizationEntry.builder()
            .credentials(
                buildAddressCredentials(new Address(signer.getAccountId()).toSCAddress(), 0L))
            .rootInvocation(invocation)
            .build();

    HashIDPreimage preimage =
        Auth.buildAuthorizationEntryPreimage(entry, validUntilLedgerSeq, network);
    assertEquals(EnvelopeType.ENVELOPE_TYPE_SOROBAN_AUTHORIZATION, preimage.getDiscriminant());
    assertEquals(new Int64(123456789L), preimage.getSorobanAuthorization().getNonce());
    assertEquals(invocation, preimage.getSorobanAuthorization().getInvocation());
    assertEquals(
        validUntilLedgerSeq,
        preimage
            .getSorobanAuthorization()
            .getSignatureExpirationLedger()
            .getUint32()
            .getNumber()
            .longValue());
  }

  @Test
  public void testBuildAuthorizationEntryPreimageWithAddressV2Credentials() throws IOException {
    KeyPair signer =
        KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");
    long validUntilLedgerSeq = 654656L;
    Network network = Network.TESTNET;
    SorobanAuthorizedInvocation invocation = buildInvocation();

    SorobanAuthorizationEntry entry =
        SorobanAuthorizationEntry.builder()
            .credentials(
                buildAddressV2Credentials(new Address(signer.getAccountId()).toSCAddress(), 0L))
            .rootInvocation(invocation)
            .build();

    HashIDPreimage preimage =
        Auth.buildAuthorizationEntryPreimage(entry, validUntilLedgerSeq, network);
    assertEquals(
        EnvelopeType.ENVELOPE_TYPE_SOROBAN_AUTHORIZATION_WITH_ADDRESS, preimage.getDiscriminant());
    assertEquals(
        new Address(signer.getAccountId()).toSCAddress(),
        preimage.getSorobanAuthorizationWithAddress().getAddress());
    assertEquals(new Int64(123456789L), preimage.getSorobanAuthorizationWithAddress().getNonce());
    assertEquals(invocation, preimage.getSorobanAuthorizationWithAddress().getInvocation());
    assertEquals(
        validUntilLedgerSeq,
        preimage
            .getSorobanAuthorizationWithAddress()
            .getSignatureExpirationLedger()
            .getUint32()
            .getNumber()
            .longValue());
  }

  @Test
  public void testBuildAuthorizationEntryPreimageWithSourceAccountCredentialsThrows() {
    SorobanAuthorizationEntry entry =
        SorobanAuthorizationEntry.builder()
            .credentials(
                SorobanCredentials.builder()
                    .discriminant(SorobanCredentialsType.SOROBAN_CREDENTIALS_SOURCE_ACCOUNT)
                    .build())
            .rootInvocation(buildInvocation())
            .build();
    try {
      Auth.buildAuthorizationEntryPreimage(entry, 654656L, Network.TESTNET);
      fail();
    } catch (IllegalArgumentException ignored) {
    }
  }

  @Test
  public void testAddressAndAddressV2ProduceDifferentPayloads() throws IOException {
    KeyPair signer =
        KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");
    long validUntilLedgerSeq = 654656L;
    Network network = Network.TESTNET;
    SorobanAuthorizedInvocation invocation = buildInvocation();
    SCAddress address = new Address(signer.getAccountId()).toSCAddress();

    SorobanAuthorizationEntry legacyEntry =
        SorobanAuthorizationEntry.builder()
            .credentials(buildAddressCredentials(address, 0L))
            .rootInvocation(invocation)
            .build();
    SorobanAuthorizationEntry v2Entry =
        SorobanAuthorizationEntry.builder()
            .credentials(buildAddressV2Credentials(address, 0L))
            .rootInvocation(invocation)
            .build();

    byte[] legacyPayload =
        Util.hash(
            Auth.buildAuthorizationEntryPreimage(legacyEntry, validUntilLedgerSeq, network)
                .toXdrByteArray());
    byte[] v2Payload =
        Util.hash(
            Auth.buildAuthorizationEntryPreimage(v2Entry, validUntilLedgerSeq, network)
                .toXdrByteArray());
    assertFalse(Arrays.equals(legacyPayload, v2Payload));
  }

  @Test
  public void testBuildAuthorizationEntryPreimageMatchesAuthorizeEntryPayload() {
    KeyPair signer =
        KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");
    long validUntilLedgerSeq = 654656L;
    Network network = Network.TESTNET;

    SorobanAuthorizationEntry entry =
        SorobanAuthorizationEntry.builder()
            .credentials(
                buildAddressV2Credentials(new Address(signer.getAccountId()).toSCAddress(), 0L))
            .rootInvocation(buildInvocation())
            .build();

    HashIDPreimage[] captured = new HashIDPreimage[1];
    Auth.Signer entrySigner =
        preimage -> {
          captured[0] = preimage;
          return Auth.defaultAccountSignatureScVal(
              signer.getAccountId(), signer.sign(Auth.authorizationPayloadHash(preimage)));
        };

    Auth.authorizeEntry(entry, entrySigner, validUntilLedgerSeq, network);
    assertEquals(
        Auth.buildAuthorizationEntryPreimage(entry, validUntilLedgerSeq, network), captured[0]);
  }

  @Test
  public void testSignAuthorizeEntryWithDelegatesCredentialsSignsTopLevel() throws IOException {
    KeyPair signer =
        KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");
    KeyPair delegate = KeyPair.fromAccountId(StrKey.encodeEd25519PublicKey(new byte[32]));
    long validUntilLedgerSeq = 654656L;
    Network network = Network.TESTNET;
    SorobanAuthorizedInvocation invocation = buildInvocation();

    SorobanAuthorizationEntry entry =
        SorobanAuthorizationEntry.builder()
            .credentials(
                buildWithDelegatesCredentials(
                    new Address(signer.getAccountId()).toSCAddress(),
                    0L,
                    new SCAddress[] {new Address(delegate.getAccountId()).toSCAddress()}))
            .rootInvocation(invocation)
            .build();

    SorobanAuthorizationEntry signedEntry =
        Auth.authorizeEntry(entry, signer, validUntilLedgerSeq, network);

    // The payload is bound to the *top-level* address via the WithAddress preimage.
    HashIDPreimage preimage =
        HashIDPreimage.builder()
            .discriminant(EnvelopeType.ENVELOPE_TYPE_SOROBAN_AUTHORIZATION_WITH_ADDRESS)
            .sorobanAuthorizationWithAddress(
                HashIDPreimage.HashIDPreimageSorobanAuthorizationWithAddress.builder()
                    .networkID(new Hash(network.getNetworkId()))
                    .nonce(new Int64(123456789L))
                    .signatureExpirationLedger(
                        new Uint32(new XdrUnsignedInteger(validUntilLedgerSeq)))
                    .address(new Address(signer.getAccountId()).toSCAddress())
                    .invocation(invocation)
                    .build())
            .build();
    byte[] signature = signer.sign(Util.hash(preimage.toXdrByteArray()));

    SorobanAddressCredentialsWithDelegates withDelegates =
        signedEntry.getCredentials().getAddressWithDelegates();
    // top-level credentials carry the signature ...
    assertEquals(
        buildSignatureScVal(signer, signature),
        withDelegates.getAddressCredentials().getSignature());
    assertEquals(
        validUntilLedgerSeq,
        withDelegates
            .getAddressCredentials()
            .getSignatureExpirationLedger()
            .getUint32()
            .getNumber()
            .longValue());
    // ... while the delegate node remains untouched
    assertEquals(1, withDelegates.getDelegates().length);
    assertEquals(
        SCValType.SCV_VOID, withDelegates.getDelegates()[0].getSignature().getDiscriminant());
  }

  @Test
  public void testSignAuthorizeEntryWithDelegatesForAddressFillsDelegateNode() throws IOException {
    KeyPair signer =
        KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");
    KeyPair delegate = deterministicKeyPair((byte) 1);
    long validUntilLedgerSeq = 654656L;
    Network network = Network.TESTNET;
    SorobanAuthorizedInvocation invocation = buildInvocation();

    SorobanAuthorizationEntry entry =
        SorobanAuthorizationEntry.builder()
            .credentials(
                buildWithDelegatesCredentials(
                    new Address(signer.getAccountId()).toSCAddress(),
                    0L,
                    new SCAddress[] {new Address(delegate.getAccountId()).toSCAddress()}))
            .rootInvocation(invocation)
            .build();

    SorobanAuthorizationEntry signedEntry =
        Auth.authorizeEntry(entry, delegate, validUntilLedgerSeq, network, delegate.getAccountId());

    // CAP-71-01: the delegate signs the same payload, bound to the *top-level* address.
    HashIDPreimage preimage =
        Auth.buildAuthorizationEntryPreimage(signedEntry, validUntilLedgerSeq, network);
    assertEquals(
        new Address(signer.getAccountId()).toSCAddress(),
        preimage.getSorobanAuthorizationWithAddress().getAddress());
    byte[] payload = Util.hash(preimage.toXdrByteArray());

    SorobanAddressCredentialsWithDelegates withDelegates =
        signedEntry.getCredentials().getAddressWithDelegates();
    // the top-level signature remains a void placeholder ...
    assertEquals(
        SCValType.SCV_VOID, withDelegates.getAddressCredentials().getSignature().getDiscriminant());
    // ... and the delegate node carries a verifiable signature over the shared payload
    SCVal delegateSignature = withDelegates.getDelegates()[0].getSignature();
    SCVal sigStruct = Scv.fromVec(delegateSignature).iterator().next();
    byte[] signatureBytes = Scv.fromBytes(Scv.fromMap(sigStruct).get(Scv.toSymbol("signature")));
    assertTrue(delegate.verify(payload, signatureBytes));
  }

  @Test
  public void testSignAuthorizeEntryWithDelegatesForAddressFillsNestedDelegateNode()
      throws IOException {
    KeyPair signer =
        KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");
    KeyPair delegate = deterministicKeyPair((byte) 1);
    KeyPair nestedDelegate = deterministicKeyPair((byte) 2);
    long validUntilLedgerSeq = 654656L;
    Network network = Network.TESTNET;

    SorobanDelegateSignature nestedNode =
        SorobanDelegateSignature.builder()
            .address(new Address(nestedDelegate.getAccountId()).toSCAddress())
            .signature(Scv.toVoid())
            .nestedDelegates(new SorobanDelegateSignature[0])
            .build();
    SorobanDelegateSignature delegateNode =
        SorobanDelegateSignature.builder()
            .address(new Address(delegate.getAccountId()).toSCAddress())
            .signature(Scv.toVoid())
            .nestedDelegates(new SorobanDelegateSignature[] {nestedNode})
            .build();
    SorobanAuthorizationEntry entry =
        SorobanAuthorizationEntry.builder()
            .credentials(
                SorobanCredentials.builder()
                    .discriminant(SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS_WITH_DELEGATES)
                    .addressWithDelegates(
                        SorobanAddressCredentialsWithDelegates.builder()
                            .addressCredentials(
                                SorobanAddressCredentials.builder()
                                    .address(new Address(signer.getAccountId()).toSCAddress())
                                    .nonce(new Int64(123456789L))
                                    .signatureExpirationLedger(
                                        new Uint32(new XdrUnsignedInteger(0L)))
                                    .signature(Scv.toVoid())
                                    .build())
                            .delegates(new SorobanDelegateSignature[] {delegateNode})
                            .build())
                    .build())
            .rootInvocation(buildInvocation())
            .build();

    SorobanAuthorizationEntry signedEntry =
        Auth.authorizeEntry(
            entry, nestedDelegate, validUntilLedgerSeq, network, nestedDelegate.getAccountId());

    byte[] payload =
        Util.hash(
            Auth.buildAuthorizationEntryPreimage(signedEntry, validUntilLedgerSeq, network)
                .toXdrByteArray());

    SorobanAddressCredentialsWithDelegates withDelegates =
        signedEntry.getCredentials().getAddressWithDelegates();
    assertEquals(
        SCValType.SCV_VOID, withDelegates.getAddressCredentials().getSignature().getDiscriminant());
    assertEquals(
        SCValType.SCV_VOID, withDelegates.getDelegates()[0].getSignature().getDiscriminant());
    SCVal nestedSignature = withDelegates.getDelegates()[0].getNestedDelegates()[0].getSignature();
    SCVal sigStruct = Scv.fromVec(nestedSignature).iterator().next();
    byte[] signatureBytes = Scv.fromBytes(Scv.fromMap(sigStruct).get(Scv.toSymbol("signature")));
    assertTrue(nestedDelegate.verify(payload, signatureBytes));
  }

  @Test
  public void testSignAuthorizeEntryWithForAddressNoMatchThrows() {
    KeyPair signer =
        KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");
    KeyPair other = deterministicKeyPair((byte) 1);
    SorobanAuthorizationEntry entry =
        SorobanAuthorizationEntry.builder()
            .credentials(
                buildAddressV2Credentials(new Address(signer.getAccountId()).toSCAddress(), 0L))
            .rootInvocation(buildInvocation())
            .build();
    try {
      Auth.authorizeEntry(entry, other, 654656L, Network.TESTNET, other.getAccountId());
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("no credential node for address"));
    }
  }

  @Test
  public void testBuildWithDelegatesEntryWrapsAddressV2Credentials() {
    KeyPair signer =
        KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");
    KeyPair delegate = deterministicKeyPair((byte) 1);
    long validUntilLedgerSeq = 654656L;
    SorobanAuthorizedInvocation invocation = buildInvocation();

    SorobanAuthorizationEntry entry =
        SorobanAuthorizationEntry.builder()
            .credentials(
                buildAddressV2Credentials(new Address(signer.getAccountId()).toSCAddress(), 0L))
            .rootInvocation(invocation)
            .build();

    SorobanAuthorizationEntry wrapped =
        Auth.buildWithDelegatesEntry(
            entry,
            validUntilLedgerSeq,
            Collections.singletonList(
                new Auth.DelegateSignature(delegate.getAccountId(), null, null)),
            null);

    assertEquals(
        SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS_WITH_DELEGATES,
        wrapped.getCredentials().getDiscriminant());
    assertEquals(invocation, wrapped.getRootInvocation());

    SorobanAddressCredentialsWithDelegates withDelegates =
        wrapped.getCredentials().getAddressWithDelegates();
    assertEquals(
        new Address(signer.getAccountId()).toSCAddress(),
        withDelegates.getAddressCredentials().getAddress());
    assertEquals(new Int64(123456789L), withDelegates.getAddressCredentials().getNonce());
    assertEquals(
        validUntilLedgerSeq,
        withDelegates
            .getAddressCredentials()
            .getSignatureExpirationLedger()
            .getUint32()
            .getNumber()
            .longValue());
    // the top-level signature defaults to a void placeholder, valid for accounts that
    // authorize purely via delegated signers
    assertEquals(
        SCValType.SCV_VOID, withDelegates.getAddressCredentials().getSignature().getDiscriminant());
    assertEquals(1, withDelegates.getDelegates().length);
    assertEquals(
        new Address(delegate.getAccountId()).toSCAddress(),
        withDelegates.getDelegates()[0].getAddress());
    assertEquals(
        SCValType.SCV_VOID, withDelegates.getDelegates()[0].getSignature().getDiscriminant());
    assertEquals(0, withDelegates.getDelegates()[0].getNestedDelegates().length);
  }

  @Test
  public void testBuildWithDelegatesEntrySortsDelegatesByAddress() throws IOException {
    KeyPair signer =
        KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");
    String contractId = "CDCYWK73YTYFJZZSJ5V7EDFNHYBG4QN3VUNG2IGD27KJDDPNCZKBCBXK";
    byte[] lowKey = new byte[32];
    byte[] highKey = new byte[32];
    highKey[0] = (byte) 0xff;
    String gLow = StrKey.encodeEd25519PublicKey(lowKey);
    String gHigh = StrKey.encodeEd25519PublicKey(highKey);

    SorobanAuthorizationEntry entry =
        SorobanAuthorizationEntry.builder()
            .credentials(
                buildAddressV2Credentials(new Address(signer.getAccountId()).toSCAddress(), 0L))
            .rootInvocation(buildInvocation())
            .build();

    // accounts (discriminant 0) sort before contracts (discriminant 1), and accounts sort by
    // their ed25519 bytes
    SorobanAuthorizationEntry wrapped =
        Auth.buildWithDelegatesEntry(
            entry,
            654656L,
            Arrays.asList(
                new Auth.DelegateSignature(contractId, null, null),
                new Auth.DelegateSignature(
                    gHigh,
                    null,
                    Arrays.asList(
                        new Auth.DelegateSignature(gHigh, null, null),
                        new Auth.DelegateSignature(gLow, null, null))),
                new Auth.DelegateSignature(gLow, null, null)),
            null);

    SorobanDelegateSignature[] delegates =
        wrapped.getCredentials().getAddressWithDelegates().getDelegates();
    assertEquals(3, delegates.length);
    assertEquals(new Address(gLow).toSCAddress(), delegates[0].getAddress());
    assertEquals(new Address(gHigh).toSCAddress(), delegates[1].getAddress());
    assertEquals(new Address(contractId).toSCAddress(), delegates[2].getAddress());
    // nested levels are sorted too
    SorobanDelegateSignature[] nested = delegates[1].getNestedDelegates();
    assertEquals(2, nested.length);
    assertEquals(new Address(gLow).toSCAddress(), nested[0].getAddress());
    assertEquals(new Address(gHigh).toSCAddress(), nested[1].getAddress());
  }

  @Test
  public void testBuildWithDelegatesEntryWithDuplicateDelegateAddressThrows() {
    KeyPair signer =
        KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");
    KeyPair delegate = deterministicKeyPair((byte) 1);
    SorobanAuthorizationEntry entry =
        SorobanAuthorizationEntry.builder()
            .credentials(
                buildAddressV2Credentials(new Address(signer.getAccountId()).toSCAddress(), 0L))
            .rootInvocation(buildInvocation())
            .build();
    try {
      Auth.buildWithDelegatesEntry(
          entry,
          654656L,
          Arrays.asList(
              new Auth.DelegateSignature(delegate.getAccountId(), null, null),
              new Auth.DelegateSignature(delegate.getAccountId(), null, null)),
          null);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("duplicate delegate address"));
    }
  }

  @Test
  public void testBuildWithDelegatesEntryRejectsWithDelegatesCredentials() {
    KeyPair signer =
        KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");
    SorobanAuthorizationEntry entry =
        SorobanAuthorizationEntry.builder()
            .credentials(
                buildWithDelegatesCredentials(
                    new Address(signer.getAccountId()).toSCAddress(), 0L, new SCAddress[0]))
            .rootInvocation(buildInvocation())
            .build();
    try {
      Auth.buildWithDelegatesEntry(entry, 654656L, Collections.emptyList(), null);
      fail();
    } catch (IllegalArgumentException ignored) {
    }
  }

  @Test
  public void testBuildWithDelegatesEntryComposesWithAuthorizeEntry() throws IOException {
    KeyPair signer =
        KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");
    KeyPair delegate = deterministicKeyPair((byte) 1);
    long validUntilLedgerSeq = 654656L;
    Network network = Network.TESTNET;

    SorobanAuthorizationEntry simulatedEntry =
        SorobanAuthorizationEntry.builder()
            .credentials(
                buildAddressV2Credentials(new Address(signer.getAccountId()).toSCAddress(), 0L))
            .rootInvocation(buildInvocation())
            .build();

    SorobanAuthorizationEntry wrapped =
        Auth.buildWithDelegatesEntry(
            simulatedEntry,
            validUntilLedgerSeq,
            Collections.singletonList(
                new Auth.DelegateSignature(delegate.getAccountId(), null, null)),
            null);
    SorobanAuthorizationEntry signedEntry =
        Auth.authorizeEntry(
            wrapped, delegate, validUntilLedgerSeq, network, delegate.getAccountId());

    byte[] payload =
        Util.hash(
            Auth.buildAuthorizationEntryPreimage(signedEntry, validUntilLedgerSeq, network)
                .toXdrByteArray());
    SCVal delegateSignature =
        signedEntry.getCredentials().getAddressWithDelegates().getDelegates()[0].getSignature();
    SCVal sigStruct = Scv.fromVec(delegateSignature).iterator().next();
    byte[] signatureBytes = Scv.fromBytes(Scv.fromMap(sigStruct).get(Scv.toSymbol("signature")));
    assertTrue(delegate.verify(payload, signatureBytes));
  }

  @Test
  public void testWithDelegatesCredentialsXdrRoundTrip() throws IOException {
    KeyPair signer =
        KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");
    KeyPair delegate = deterministicKeyPair((byte) 1);
    KeyPair nestedDelegate = deterministicKeyPair((byte) 2);

    SorobanAuthorizationEntry entry =
        SorobanAuthorizationEntry.builder()
            .credentials(
                SorobanCredentials.builder()
                    .discriminant(SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS_WITH_DELEGATES)
                    .addressWithDelegates(
                        SorobanAddressCredentialsWithDelegates.builder()
                            .addressCredentials(
                                SorobanAddressCredentials.builder()
                                    .address(new Address(signer.getAccountId()).toSCAddress())
                                    .nonce(new Int64(123456789L))
                                    .signatureExpirationLedger(
                                        new Uint32(new XdrUnsignedInteger(654656L)))
                                    .signature(Scv.toVoid())
                                    .build())
                            .delegates(
                                new SorobanDelegateSignature[] {
                                  SorobanDelegateSignature.builder()
                                      .address(new Address(delegate.getAccountId()).toSCAddress())
                                      .signature(Scv.toVoid())
                                      .nestedDelegates(
                                          new SorobanDelegateSignature[] {
                                            SorobanDelegateSignature.builder()
                                                .address(
                                                    new Address(nestedDelegate.getAccountId())
                                                        .toSCAddress())
                                                .signature(Scv.toVoid())
                                                .nestedDelegates(new SorobanDelegateSignature[0])
                                                .build()
                                          })
                                      .build()
                                })
                            .build())
                    .build())
            .rootInvocation(buildInvocation())
            .build();

    assertEquals(entry, SorobanAuthorizationEntry.fromXdrBase64(entry.toXdrBase64()));
  }

  @Test
  public void testGetAddressCredentials() {
    KeyPair signer =
        KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");
    SCAddress address = new Address(signer.getAccountId()).toSCAddress();

    assertEquals(
        address, Auth.getAddressCredentials(buildAddressCredentials(address, 0L)).getAddress());
    assertEquals(
        address, Auth.getAddressCredentials(buildAddressV2Credentials(address, 0L)).getAddress());
    assertEquals(
        address,
        Auth.getAddressCredentials(buildWithDelegatesCredentials(address, 0L, new SCAddress[0]))
            .getAddress());
    assertNull(
        Auth.getAddressCredentials(
            SorobanCredentials.builder()
                .discriminant(SorobanCredentialsType.SOROBAN_CREDENTIALS_SOURCE_ACCOUNT)
                .build()));
  }

  @Test
  public void testAuthorizeEntryMatchesJsSdkVectors() throws IOException {
    // Vectors generated with @stellar/stellar-sdk v16.0.0-rc.1 (CAP-71): the exact same inputs
    // (signer, nonce 123456789, expiration ledger 654656, TESTNET, "increment" invocation) must
    // produce byte-identical signed entries in both SDKs.
    String addressSignedVector =
        "AAAAAQAAAAAAAAAAWLfEosjyl6qPPSRxKB/fzOyv5I5WYzE+wY4Spz7KmKEAAAAAB1vNFQAJ/UAAAAAQAAAAAQAAAAEAAAARAAAAAQAAAAIAAAAPAAAACnB1YmxpY19rZXkAAAAAAA0AAAAgWLfEosjyl6qPPSRxKB/fzOyv5I5WYzE+wY4Spz7KmKEAAAAPAAAACXNpZ25hdHVyZQAAAAAAAA0AAABACOR9aTfdmCyXluY4UFurnM36CX91IL7qogZxFJ+fsFzKssUpDyHB0kxfPZVq9plTLfB14HoboD5tB71CgdWuBgAAAAAAAAABxYsr+8TwVOcyT2vyDK0+Am5Bu60abSDD19SRje0WVBEAAAAJaW5jcmVtZW50AAAAAAAAAAAAAAA=";
    String addressV2SignedVector =
        "AAAAAgAAAAAAAAAAWLfEosjyl6qPPSRxKB/fzOyv5I5WYzE+wY4Spz7KmKEAAAAAB1vNFQAJ/UAAAAAQAAAAAQAAAAEAAAARAAAAAQAAAAIAAAAPAAAACnB1YmxpY19rZXkAAAAAAA0AAAAgWLfEosjyl6qPPSRxKB/fzOyv5I5WYzE+wY4Spz7KmKEAAAAPAAAACXNpZ25hdHVyZQAAAAAAAA0AAABAUT1dqOx7TCk7ZHTruqLGInEw9QkSI7XrxlE1fVwbiU+viyQtrvIAP6vPPHOexFGnqmoPjkMwdeK0kWWUt90xBgAAAAAAAAABxYsr+8TwVOcyT2vyDK0+Am5Bu60abSDD19SRje0WVBEAAAAJaW5jcmVtZW50AAAAAAAAAAAAAAA=";
    String delegatesSignedVector =
        "AAAAAwAAAAAAAAAAWLfEosjyl6qPPSRxKB/fzOyv5I5WYzE+wY4Spz7KmKEAAAAAB1vNFQAJ/UAAAAABAAAAAQAAAAAAAAAAiojj3XQJ8ZX9UtstPLpdcspnCb8dlBIb83SIAbQPb1wAAAAQAAAAAQAAAAEAAAARAAAAAQAAAAIAAAAPAAAACnB1YmxpY19rZXkAAAAAAA0AAAAgiojj3XQJ8ZX9UtstPLpdcspnCb8dlBIb83SIAbQPb1wAAAAPAAAACXNpZ25hdHVyZQAAAAAAAA0AAABAvI04jRWOZ3J6mWQPMcXE0s26eR9FrpZ9iTcqBic4trJ3FcViAU4qpRoue3Ew2ViqvYKbzHwHtNbdFLYR5MXfCwAAAAAAAAAAAAAAAcWLK/vE8FTnMk9r8gytPgJuQbutGm0gw9fUkY3tFlQRAAAACWluY3JlbWVudAAAAAAAAAAAAAAA";

    KeyPair signer =
        KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");
    KeyPair delegate = deterministicKeyPair((byte) 1);
    assertEquals(
        "GCFIRY65OQE7DFP5KLNS2PF2LVZMUZYJX4OZIEQ36N2IQANUB5XVYOJR", delegate.getAccountId());
    long validUntilLedgerSeq = 654656L;
    Network network = Network.TESTNET;
    SCAddress signerAddress = new Address(signer.getAccountId()).toSCAddress();
    SorobanAuthorizedInvocation invocation = buildInvocation();

    SorobanAuthorizationEntry legacyEntry =
        SorobanAuthorizationEntry.builder()
            .credentials(buildAddressCredentials(signerAddress, 0L))
            .rootInvocation(invocation)
            .build();
    assertEquals(
        addressSignedVector,
        Auth.authorizeEntry(legacyEntry, signer, validUntilLedgerSeq, network).toXdrBase64());

    SorobanAuthorizationEntry v2Entry =
        SorobanAuthorizationEntry.builder()
            .credentials(buildAddressV2Credentials(signerAddress, 0L))
            .rootInvocation(invocation)
            .build();
    assertEquals(
        addressV2SignedVector,
        Auth.authorizeEntry(v2Entry, signer, validUntilLedgerSeq, network).toXdrBase64());

    SorobanAuthorizationEntry wrapped =
        Auth.buildWithDelegatesEntry(
            v2Entry,
            validUntilLedgerSeq,
            Collections.singletonList(
                new Auth.DelegateSignature(delegate.getAccountId(), null, null)),
            null);
    assertEquals(
        delegatesSignedVector,
        Auth.authorizeEntry(
                wrapped, delegate, validUntilLedgerSeq, network, delegate.getAccountId())
            .toXdrBase64());
  }

  @Test
  public void authorizeInvocationWithAddressV2CredentialsType() throws IOException {
    KeyPair signer =
        KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");
    long validUntilLedgerSeq = 654656L;
    Network network = Network.TESTNET;
    SorobanAuthorizedInvocation invocation = buildInvocation();

    SorobanAuthorizationEntry signedEntry =
        Auth.authorizeInvocation(
            signer,
            validUntilLedgerSeq,
            invocation,
            network,
            SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS_V2);

    assertEquals(signedEntry.getRootInvocation(), invocation);
    assertEquals(
        SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS_V2,
        signedEntry.getCredentials().getDiscriminant());
    SorobanAddressCredentials addressCredentials = signedEntry.getCredentials().getAddressV2();
    assertEquals(new Address(signer.getAccountId()).toSCAddress(), addressCredentials.getAddress());
    assertEquals(SCValType.SCV_VEC, addressCredentials.getSignature().getDiscriminant());

    // The signature verifies against the address-bound (CAP-71-02) payload.
    HashIDPreimage preimage =
        Auth.buildAuthorizationEntryPreimage(signedEntry, validUntilLedgerSeq, network);
    assertEquals(
        EnvelopeType.ENVELOPE_TYPE_SOROBAN_AUTHORIZATION_WITH_ADDRESS, preimage.getDiscriminant());
    byte[] payload = Util.hash(preimage.toXdrByteArray());
    SCVal sigStruct = Scv.fromVec(addressCredentials.getSignature()).iterator().next();
    byte[] signatureBytes = Scv.fromBytes(Scv.fromMap(sigStruct).get(Scv.toSymbol("signature")));
    assertTrue(signer.verify(payload, signatureBytes));
  }

  @Test
  public void authorizeInvocationWithUnsupportedCredentialsTypeThrows() {
    KeyPair signer =
        KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");
    try {
      Auth.authorizeInvocation(
          signer,
          654656L,
          buildInvocation(),
          Network.TESTNET,
          SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS_WITH_DELEGATES);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("credentialsType must be"));
    }
  }

  @Test
  public void testSignAuthorizeEntryWithCustomContractSignature() {
    // A custom account contract whose __check_auth expects an arbitrary structure. The signer
    // returns that SCVal and it is attached verbatim — no ed25519 wrapping, no client-side
    // verification.
    String contractId = "CDCYWK73YTYFJZZSJ5V7EDFNHYBG4QN3VUNG2IGD27KJDDPNCZKBCBXK";
    long validUntilLedgerSeq = 654656L;
    Network network = Network.TESTNET;

    SCVal customSignature =
        Scv.toMap(
            new LinkedHashMap<SCVal, SCVal>() {
              {
                put(
                    Scv.toSymbol("bls"),
                    Scv.toBytes("bls-signature".getBytes(StandardCharsets.UTF_8)));
                put(
                    Scv.toSymbol("webauthn"),
                    Scv.toMap(
                        new LinkedHashMap<SCVal, SCVal>() {
                          {
                            put(
                                Scv.toSymbol("authenticator_data"),
                                Scv.toBytes("authenticator-data".getBytes(StandardCharsets.UTF_8)));
                            put(
                                Scv.toSymbol("client_data_json"),
                                Scv.toBytes(
                                    "{\"type\":\"webauthn.get\"}"
                                        .getBytes(StandardCharsets.UTF_8)));
                            put(
                                Scv.toSymbol("signature"),
                                Scv.toBytes("webauthn-signature".getBytes(StandardCharsets.UTF_8)));
                          }
                        }));
              }
            });

    SorobanAuthorizationEntry entry =
        SorobanAuthorizationEntry.builder()
            .credentials(buildAddressCredentials(new Address(contractId).toSCAddress(), 0L))
            .rootInvocation(buildInvocation())
            .build();

    HashIDPreimage[] seen = new HashIDPreimage[1];
    int[] calls = {0};
    Auth.Signer signer =
        preimage -> {
          seen[0] = preimage;
          calls[0]++;
          return customSignature;
        };

    SorobanAuthorizationEntry signedEntry =
        Auth.authorizeEntry(entry, signer, validUntilLedgerSeq, network);

    SorobanAddressCredentials credentials = signedEntry.getCredentials().getAddress();
    assertEquals(customSignature, credentials.getSignature());
    assertEquals(
        validUntilLedgerSeq,
        credentials.getSignatureExpirationLedger().getUint32().getNumber().longValue());
    // The signer is invoked exactly once, with the same preimage authorizeEntry builds.
    assertEquals(1, calls[0]);
    assertEquals(
        Auth.buildAuthorizationEntryPreimage(signedEntry, validUntilLedgerSeq, network), seen[0]);
  }

  @Test
  public void testSignAuthorizeInvocationWithContractAddressAndCustomSignature() {
    // A custom (non-KeyPair) signer authorizing an invocation for a C... contract address.
    String contractId = "CDCYWK73YTYFJZZSJ5V7EDFNHYBG4QN3VUNG2IGD27KJDDPNCZKBCBXK";
    long validUntilLedgerSeq = 654656L;
    Network network = Network.TESTNET;

    SCVal customSignature =
        Scv.toVec(
            Arrays.asList(
                Scv.toSymbol("passkey"),
                Scv.toBytes("webauthn-signature".getBytes(StandardCharsets.UTF_8))));

    Auth.Signer signer = preimage -> customSignature;
    SorobanAuthorizationEntry signedEntry =
        Auth.authorizeInvocation(
            signer, contractId, validUntilLedgerSeq, buildInvocation(), network);

    SorobanAddressCredentials credentials = signedEntry.getCredentials().getAddress();
    assertEquals(new Address(contractId).toSCAddress(), credentials.getAddress());
    assertEquals(customSignature, credentials.getSignature());
  }

  @Test
  public void testForAddressWritesCustomScValIntoDelegateNode() {
    // CAP-71-01: a delegate that is itself a custom account contract. The custom signature SCVal is
    // written verbatim into the delegate node selected by forAddress — the case that motivated
    // returning SCVal from Signer.
    KeyPair account =
        KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");
    String delegateContract = "CDCYWK73YTYFJZZSJ5V7EDFNHYBG4QN3VUNG2IGD27KJDDPNCZKBCBXK";
    long validUntilLedgerSeq = 654656L;
    Network network = Network.TESTNET;

    SorobanAuthorizationEntry baseEntry =
        SorobanAuthorizationEntry.builder()
            .credentials(
                buildAddressV2Credentials(new Address(account.getAccountId()).toSCAddress(), 0L))
            .rootInvocation(buildInvocation())
            .build();
    SorobanAuthorizationEntry wrapped =
        Auth.buildWithDelegatesEntry(
            baseEntry,
            validUntilLedgerSeq,
            Collections.singletonList(new Auth.DelegateSignature(delegateContract, null, null)),
            null);

    SCVal customSignature =
        Scv.toBytes("delegate-contract-signature".getBytes(StandardCharsets.UTF_8));
    Auth.Signer signer = preimage -> customSignature;

    SorobanAuthorizationEntry signedEntry =
        Auth.authorizeEntry(wrapped, signer, validUntilLedgerSeq, network, delegateContract);

    SorobanAddressCredentialsWithDelegates withDelegates =
        signedEntry.getCredentials().getAddressWithDelegates();
    // Top-level node untouched (we targeted the delegate) ...
    assertEquals(
        SCValType.SCV_VOID, withDelegates.getAddressCredentials().getSignature().getDiscriminant());
    // ... the delegate node carries the custom SCVal verbatim.
    SorobanDelegateSignature[] delegates = withDelegates.getDelegates();
    assertEquals(1, delegates.length);
    assertEquals(new Address(delegateContract).toSCAddress(), delegates[0].getAddress());
    assertEquals(customSignature, delegates[0].getSignature());
  }

  @Test
  public void testDefaultAccountSignatureScVal() {
    KeyPair signer =
        KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");
    byte[] signature = new byte[64];
    Arrays.fill(signature, (byte) 7);
    // Byte-identical to the hand-built default Stellar Account shape used elsewhere in this suite,
    // from both the accountId and the raw-publicKey overloads.
    SCVal expected = buildSignatureScVal(signer, signature);
    assertEquals(expected, Auth.defaultAccountSignatureScVal(signer.getAccountId(), signature));
    assertEquals(expected, Auth.defaultAccountSignatureScVal(signer.getPublicKey(), signature));
  }

  @Test
  public void testAuthorizationPayloadHash() throws IOException {
    KeyPair signer =
        KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN");
    SorobanAuthorizationEntry entry =
        SorobanAuthorizationEntry.builder()
            .credentials(
                buildAddressV2Credentials(new Address(signer.getAccountId()).toSCAddress(), 0L))
            .rootInvocation(buildInvocation())
            .build();
    HashIDPreimage preimage = Auth.buildAuthorizationEntryPreimage(entry, 654656L, Network.TESTNET);
    assertArrayEquals(
        Util.hash(preimage.toXdrByteArray()), Auth.authorizationPayloadHash(preimage));
  }

  @Test
  public void testAuthorizeEntryWithCustomSignatureMatchesVector() throws IOException {
    // A fixed vector pinning byte-stable output: with these inputs (contract address credentials,
    // nonce 123456789, expiration 654656, TESTNET, "increment" invocation) and a deterministic
    // custom signature SCVal, authorizeEntry attaches it to produce this exact signed entry. No
    // crypto is involved — the SDK attaches, the host verifies.
    String contractId = "CDCYWK73YTYFJZZSJ5V7EDFNHYBG4QN3VUNG2IGD27KJDDPNCZKBCBXK";
    String customSignatureVector =
        "AAAAAQAAAAHFiyv7xPBU5zJPa/IMrT4CbkG7rRptIMPX1JGN7RZUEQAAAAAHW80VAAn9QAAAAA0AAAASd2ViYXV0aG4tc2lnbmF0dXJlAAAAAAAAAAAAAcWLK/vE8FTnMk9r8gytPgJuQbutGm0gw9fUkY3tFlQRAAAACWluY3JlbWVudAAAAAAAAAAAAAAA";

    SorobanAuthorizationEntry entry =
        SorobanAuthorizationEntry.builder()
            .credentials(buildAddressCredentials(new Address(contractId).toSCAddress(), 0L))
            .rootInvocation(buildInvocation())
            .build();
    SCVal customSignature = Scv.toBytes("webauthn-signature".getBytes(StandardCharsets.UTF_8));

    assertEquals(
        customSignatureVector,
        Auth.authorizeEntry(entry, preimage -> customSignature, 654656L, Network.TESTNET)
            .toXdrBase64());
  }

  private static KeyPair deterministicKeyPair(byte fill) {
    byte[] seed = new byte[32];
    Arrays.fill(seed, fill);
    return KeyPair.fromSecretSeed(seed);
  }

  private static SorobanAuthorizedInvocation buildInvocation() {
    String contractId = "CDCYWK73YTYFJZZSJ5V7EDFNHYBG4QN3VUNG2IGD27KJDDPNCZKBCBXK";
    return SorobanAuthorizedInvocation.builder()
        .function(
            SorobanAuthorizedFunction.builder()
                .discriminant(
                    SorobanAuthorizedFunctionType.SOROBAN_AUTHORIZED_FUNCTION_TYPE_CONTRACT_FN)
                .contractFn(
                    InvokeContractArgs.builder()
                        .contractAddress(new Address(contractId).toSCAddress())
                        .functionName(Scv.toSymbol("increment").getSym())
                        .args(new SCVal[0])
                        .build())
                .build())
        .subInvocations(new SorobanAuthorizedInvocation[0])
        .build();
  }

  private static SorobanAddressCredentials buildInnerAddressCredentials(
      SCAddress address, long signatureExpirationLedger) {
    return SorobanAddressCredentials.builder()
        .address(address)
        .nonce(new Int64(123456789L))
        .signatureExpirationLedger(new Uint32(new XdrUnsignedInteger(signatureExpirationLedger)))
        .signature(Scv.toVoid())
        .build();
  }

  private static SorobanCredentials buildAddressCredentials(
      SCAddress address, long signatureExpirationLedger) {
    return SorobanCredentials.builder()
        .discriminant(SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS)
        .address(buildInnerAddressCredentials(address, signatureExpirationLedger))
        .build();
  }

  private static SorobanCredentials buildAddressV2Credentials(
      SCAddress address, long signatureExpirationLedger) {
    return SorobanCredentials.builder()
        .discriminant(SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS_V2)
        .addressV2(buildInnerAddressCredentials(address, signatureExpirationLedger))
        .build();
  }

  private static SorobanCredentials buildWithDelegatesCredentials(
      SCAddress address, long signatureExpirationLedger, SCAddress[] delegateAddresses) {
    SorobanDelegateSignature[] delegates = new SorobanDelegateSignature[delegateAddresses.length];
    for (int i = 0; i < delegateAddresses.length; i++) {
      delegates[i] =
          SorobanDelegateSignature.builder()
              .address(delegateAddresses[i])
              .signature(Scv.toVoid())
              .nestedDelegates(new SorobanDelegateSignature[0])
              .build();
    }
    return SorobanCredentials.builder()
        .discriminant(SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS_WITH_DELEGATES)
        .addressWithDelegates(
            SorobanAddressCredentialsWithDelegates.builder()
                .addressCredentials(
                    buildInnerAddressCredentials(address, signatureExpirationLedger))
                .delegates(delegates)
                .build())
        .build();
  }

  private static SCVal buildSignatureScVal(KeyPair signer, byte[] signature) {
    return Scv.toVec(
        Collections.singleton(
            Scv.toMap(
                new LinkedHashMap<SCVal, SCVal>() {
                  {
                    put(Scv.toSymbol("public_key"), Scv.toBytes(signer.getPublicKey()));
                    put(Scv.toSymbol("signature"), Scv.toBytes(signature));
                  }
                })));
  }
}
