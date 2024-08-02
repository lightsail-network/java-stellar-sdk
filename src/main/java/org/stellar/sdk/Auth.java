package org.stellar.sdk;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.LinkedHashMap;
import org.stellar.sdk.exception.UnexpectedException;
import org.stellar.sdk.scval.Scv;
import org.stellar.sdk.xdr.EnvelopeType;
import org.stellar.sdk.xdr.Hash;
import org.stellar.sdk.xdr.HashIDPreimage;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SorobanAddressCredentials;
import org.stellar.sdk.xdr.SorobanAuthorizationEntry;
import org.stellar.sdk.xdr.SorobanAuthorizedInvocation;
import org.stellar.sdk.xdr.SorobanCredentials;
import org.stellar.sdk.xdr.SorobanCredentialsType;
import org.stellar.sdk.xdr.Uint32;
import org.stellar.sdk.xdr.XdrUnsignedInteger;

/**
 * This class contains helper methods to sign {@link SorobanAuthorizationEntry}.
 *
 * @see <a href="https://developers.stellar.org/docs/learn/encyclopedia/security/authorization/"
 *     target="_blank">Smart Contract Authorization</a>
 */
public class Auth {
  /**
   * Actually authorizes an existing authorization entry using the given the credentials and
   * expiration details, returning a signed copy.
   *
   * <p>This "fills out" the authorization entry with a signature, indicating to the {@link
   * org.stellar.sdk.operations.InvokeHostFunctionOperation} it's attached to that:
   *
   * <ul>
   *   <li>a particular identity (i.e. signing {@link KeyPair} or {@link Signer})
   *   <li>approving the execution of an invocation tree (i.e. a simulation-acquired {@link
   *       SorobanAuthorizedInvocation} or otherwise built)
   *   <li>on a particular network (uniquely identified by its passphrase, see {@link Network})
   *   <li>until a particular ledger sequence is reached.
   * </ul>
   *
   * @param entry a base64 encoded unsigned Soroban authorization entry
   * @param signer a {@link KeyPair} which should correspond to the address in the `entry`
   * @param validUntilLedgerSeq the (exclusive) future ledger sequence number until which this
   *     authorization entry should be valid (if `currentLedgerSeq==validUntil`, this is expired)
   * @param network the network is incorprated into the signature
   * @return a signed Soroban authorization entry
   */
  public static SorobanAuthorizationEntry authorizeEntry(
      String entry, KeyPair signer, Long validUntilLedgerSeq, Network network) {
    SorobanAuthorizationEntry entryXdr;
    try {
      entryXdr = SorobanAuthorizationEntry.fromXdrBase64(entry);
    } catch (IOException e) {
      throw new IllegalArgumentException("Unable to convert entry to SorobanAuthorizationEntry", e);
    }
    return authorizeEntry(entryXdr, signer, validUntilLedgerSeq, network);
  }

  /**
   * Actually authorizes an existing authorization entry using the given the credentials and
   * expiration details, returning a signed copy.
   *
   * <p>This "fills out" the authorization entry with a signature, indicating to the {@link
   * org.stellar.sdk.operations.InvokeHostFunctionOperation} it's attached to that:
   *
   * <ul>
   *   <li>a particular identity (i.e. signing {@link KeyPair} or {@link Signer})
   *   <li>approving the execution of an invocation tree (i.e. a simulation-acquired {@link
   *       SorobanAuthorizedInvocation} or otherwise built)
   *   <li>on a particular network (uniquely identified by its passphrase, see {@link Network})
   *   <li>until a particular ledger sequence is reached.
   * </ul>
   *
   * @param entry a base64 encoded unsigned Soroban authorization entry
   * @param signer a {@link KeyPair} which should correspond to the address in the `entry`
   * @param validUntilLedgerSeq the (exclusive) future ledger sequence number until which this
   *     authorization entry should be valid (if `currentLedgerSeq==validUntil`, this is expired)
   * @param network the network is incorprated into the signature
   * @return a signed Soroban authorization entry
   */
  public static SorobanAuthorizationEntry authorizeEntry(
      SorobanAuthorizationEntry entry, KeyPair signer, Long validUntilLedgerSeq, Network network) {
    Signer entrySigner =
        preimage -> {
          byte[] data;
          try {
            data = preimage.toXdrByteArray();
          } catch (IOException e) {
            throw new IllegalArgumentException("Unable to convert preimage to bytes", e);
          }
          byte[] payload = Util.hash(data);
          return signer.sign(payload);
        };

    return authorizeEntry(entry, entrySigner, validUntilLedgerSeq, network);
  }

  /**
   * Actually authorizes an existing authorization entry using the given the credentials and
   * expiration details, returning a signed copy.
   *
   * <p>This "fills out" the authorization entry with a signature, indicating to the {@link
   * org.stellar.sdk.operations.InvokeHostFunctionOperation} it's attached to that:
   *
   * <ul>
   *   <li>a particular identity (i.e. signing {@link KeyPair} or {@link Signer})
   *   <li>approving the execution of an invocation tree (i.e. a simulation-acquired {@link
   *       SorobanAuthorizedInvocation} or otherwise built)
   *   <li>on a particular network (uniquely identified by its passphrase, see {@link Network})
   *   <li>until a particular ledger sequence is reached.
   * </ul>
   *
   * @param entry a base64 encoded unsigned Soroban authorization entry
   * @param signer A function which takes a payload (a {@link HashIDPreimage}) and returns the
   *     signature of the hash of the raw payload bytes, see {@link Signer}
   * @param validUntilLedgerSeq the (exclusive) future ledger sequence number until which this
   *     authorization entry should be valid (if `currentLedgerSeq==validUntil`, this is expired)
   * @param network the network is incorprated into the signature
   * @return a signed Soroban authorization entry
   */
  public static SorobanAuthorizationEntry authorizeEntry(
      String entry, Signer signer, Long validUntilLedgerSeq, Network network) {
    SorobanAuthorizationEntry entryXdr;
    try {
      entryXdr = SorobanAuthorizationEntry.fromXdrBase64(entry);
    } catch (IOException e) {
      throw new IllegalArgumentException("Unable to convert entry to SorobanAuthorizationEntry", e);
    }
    return authorizeEntry(entryXdr, signer, validUntilLedgerSeq, network);
  }

  /**
   * Actually authorizes an existing authorization entry using the given the credentials and
   * expiration details, returning a signed copy.
   *
   * <p>This "fills out" the authorization entry with a signature, indicating to the {@link
   * org.stellar.sdk.operations.InvokeHostFunctionOperation} it's attached to that:
   *
   * <ul>
   *   <li>a particular identity (i.e. signing {@link KeyPair} or {@link Signer})
   *   <li>approving the execution of an invocation tree (i.e. a simulation-acquired {@link
   *       SorobanAuthorizedInvocation} or otherwise built)
   *   <li>on a particular network (uniquely identified by its passphrase, see {@link Network})
   *   <li>until a particular ledger sequence is reached.
   * </ul>
   *
   * @param entry an unsigned Soroban authorization entry
   * @param signer A function which takes a payload (a {@link HashIDPreimage}) and returns the
   *     signature of the hash of the raw payload bytes, see {@link Signer}
   * @param validUntilLedgerSeq the (exclusive) future ledger sequence number until which this
   *     authorization entry should be valid (if `currentLedgerSeq==validUntil`, this is expired)
   * @param network the network is incorprated into the signature
   * @return a signed Soroban authorization entry
   */
  public static SorobanAuthorizationEntry authorizeEntry(
      SorobanAuthorizationEntry entry, Signer signer, Long validUntilLedgerSeq, Network network) {
    SorobanAuthorizationEntry clone;
    try {
      clone = SorobanAuthorizationEntry.fromXdrByteArray(entry.toXdrByteArray());
    } catch (IOException e) {
      throw new IllegalArgumentException("Unable to clone SorobanAuthorizationEntry", e);
    }

    if (clone.getCredentials().getDiscriminant()
        != SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS) {
      return clone;
    }

    SorobanAddressCredentials addressCredentials = clone.getCredentials().getAddress();
    addressCredentials.setSignatureExpirationLedger(
        new Uint32(new XdrUnsignedInteger(validUntilLedgerSeq)));

    HashIDPreimage preimage =
        HashIDPreimage.builder()
            .discriminant(EnvelopeType.ENVELOPE_TYPE_SOROBAN_AUTHORIZATION)
            .sorobanAuthorization(
                HashIDPreimage.HashIDPreimageSorobanAuthorization.builder()
                    .networkID(new Hash(network.getNetworkId()))
                    .nonce(addressCredentials.getNonce())
                    .invocation(clone.getRootInvocation())
                    .signatureExpirationLedger(addressCredentials.getSignatureExpirationLedger())
                    .build())
            .build();
    byte[] signature = signer.sign(preimage);
    byte[] publicKey = Address.fromSCAddress(addressCredentials.getAddress()).getBytes();

    byte[] data;
    try {
      data = preimage.toXdrByteArray();
    } catch (IOException e) {
      throw new IllegalArgumentException("Unable to convert preimage to bytes", e);
    }
    byte[] payload = Util.hash(data);
    if (!KeyPair.fromPublicKey(publicKey).verify(payload, signature)) {
      throw new IllegalArgumentException("signature does not match payload");
    }

    // This structure is defined here:
    // https://developers.stellar.org/docs/learn/encyclopedia/contract-development/contract-interactions/stellar-transaction#stellar-account-signatures
    // https://github.com/stellar/rs-soroban-env/blob/99d8c92cdc7e5cd0f5311df8f88d04658ecde7d2/soroban-env-host/src/native_contract/account_contract.rs#L51
    SCVal sigScVal =
        Scv.toMap(
            new LinkedHashMap<SCVal, SCVal>() {
              {
                put(Scv.toSymbol("public_key"), Scv.toBytes(publicKey));
                put(Scv.toSymbol("signature"), Scv.toBytes(signature));
              }
            });
    addressCredentials.setSignature(Scv.toVec(Collections.singleton(sigScVal)));
    return clone;
  }

  /**
   * This builds an entry from scratch, allowing you to express authorization as a function of:
   *
   * <ul>
   *   <li>a particular identity (i.e. signing {@link KeyPair} or {@link Signer})
   *   <li>approving the execution of an invocation tree (i.e. a simulation-acquired {@link
   *       SorobanAuthorizedInvocation} or otherwise built)
   *   <li>on a particular network (uniquely identified by its passphrase, see {@link Network})
   *   <li>until a particular ledger sequence is reached.
   * </ul>
   *
   * <p>This is in contrast to {@link Auth#authorizeEntry}, which signs an existing entry "in
   * place".
   *
   * @param signer a {@link KeyPair} used to sign the entry
   * @param validUntilLedgerSeq the (exclusive) future ledger sequence number until which this
   *     authorization entry should be valid (if `currentLedgerSeq==validUntil`, this is expired)
   * @param invocation invocation the invocation tree that we're authorizing (likely, this comes
   *     from transaction simulation)
   * @param network the network is incorprated into the signature
   * @return a signed Soroban authorization entry
   */
  public static SorobanAuthorizationEntry authorizeInvocation(
      KeyPair signer,
      Long validUntilLedgerSeq,
      SorobanAuthorizedInvocation invocation,
      Network network) {
    Signer entrySigner =
        preimage -> {
          try {
            byte[] payload = Util.hash(preimage.toXdrByteArray());
            return signer.sign(payload);
          } catch (IOException e) {
            throw new UnexpectedException(e);
          }
        };
    return authorizeInvocation(
        entrySigner, signer.getAccountId(), validUntilLedgerSeq, invocation, network);
  }

  /**
   * This builds an entry from scratch, allowing you to express authorization as a function of:
   *
   * <ul>
   *   <li>a particular identity (i.e. signing {@link KeyPair} or {@link Signer})
   *   <li>approving the execution of an invocation tree (i.e. a simulation-acquired {@link
   *       SorobanAuthorizedInvocation} or otherwise built)
   *   <li>on a particular network (uniquely identified by its passphrase, see {@link Network})
   *   <li>until a particular ledger sequence is reached.
   * </ul>
   *
   * <p>This is in contrast to {@link Auth#authorizeEntry}, which signs an existing entry "in
   * place".
   *
   * @param signer A function which takes a payload (a {@link HashIDPreimage}) and returns the
   *     signature of the hash of the raw payload bytes, see {@link Signer}
   * @param publicKey the public identity of the signer
   * @param validUntilLedgerSeq the (exclusive) future ledger sequence number until which this
   *     authorization entry should be valid (if `currentLedgerSeq==validUntil`, this is expired)
   * @param invocation invocation the invocation tree that we're authorizing (likely, this comes
   *     from transaction simulation)
   * @param network the network is incorprated into the signature
   * @return a signed Soroban authorization entry
   */
  public static SorobanAuthorizationEntry authorizeInvocation(
      Signer signer,
      String publicKey,
      Long validUntilLedgerSeq,
      SorobanAuthorizedInvocation invocation,
      Network network) {
    long nonce = new SecureRandom().nextLong();
    SorobanAuthorizationEntry entry =
        SorobanAuthorizationEntry.builder()
            .credentials(
                SorobanCredentials.builder()
                    .discriminant(SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS)
                    .address(
                        SorobanAddressCredentials.builder()
                            .address(new Address(publicKey).toSCAddress())
                            .nonce(new Int64(nonce))
                            .signatureExpirationLedger(
                                new Uint32(new XdrUnsignedInteger(validUntilLedgerSeq)))
                            .signature(Scv.toVoid())
                            .build())
                    .build())
            .rootInvocation(invocation)
            .build();
    return authorizeEntry(entry, signer, validUntilLedgerSeq, network);
  }

  /** An interface for signing a {@link HashIDPreimage} to produce a signature. */
  public interface Signer {
    byte[] sign(HashIDPreimage preimage);
  }
}
