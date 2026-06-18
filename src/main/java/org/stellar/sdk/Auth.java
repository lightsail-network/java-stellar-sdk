package org.stellar.sdk;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;
import org.stellar.sdk.exception.UnexpectedException;
import org.stellar.sdk.scval.Scv;
import org.stellar.sdk.xdr.EnvelopeType;
import org.stellar.sdk.xdr.Hash;
import org.stellar.sdk.xdr.HashIDPreimage;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.SCAddress;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SorobanAddressCredentials;
import org.stellar.sdk.xdr.SorobanAddressCredentialsWithDelegates;
import org.stellar.sdk.xdr.SorobanAuthorizationEntry;
import org.stellar.sdk.xdr.SorobanAuthorizedInvocation;
import org.stellar.sdk.xdr.SorobanCredentials;
import org.stellar.sdk.xdr.SorobanCredentialsType;
import org.stellar.sdk.xdr.SorobanDelegateSignature;
import org.stellar.sdk.xdr.Uint32;
import org.stellar.sdk.xdr.XdrUnsignedInteger;

/**
 * This class contains helper methods to sign {@link SorobanAuthorizationEntry}.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/learn/fundamentals/contract-development/authorization"
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
    return authorizeEntry(entry, signer, validUntilLedgerSeq, network, null);
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
   * @param forAddress which credential node the signature should be written to, see {@link
   *     Auth#authorizeEntry(SorobanAuthorizationEntry, Signer, Long, Network, String)}
   * @return a signed Soroban authorization entry
   */
  public static SorobanAuthorizationEntry authorizeEntry(
      String entry,
      KeyPair signer,
      Long validUntilLedgerSeq,
      Network network,
      @Nullable String forAddress) {
    SorobanAuthorizationEntry entryXdr;
    try {
      entryXdr = SorobanAuthorizationEntry.fromXdrBase64(entry);
    } catch (IOException e) {
      throw new IllegalArgumentException("Unable to convert entry to SorobanAuthorizationEntry", e);
    }
    return authorizeEntry(entryXdr, signer, validUntilLedgerSeq, network, forAddress);
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
   * @param signer a {@link KeyPair} which should correspond to the address in the `entry`
   * @param validUntilLedgerSeq the (exclusive) future ledger sequence number until which this
   *     authorization entry should be valid (if `currentLedgerSeq==validUntil`, this is expired)
   * @param network the network is incorprated into the signature
   * @return a signed Soroban authorization entry
   */
  public static SorobanAuthorizationEntry authorizeEntry(
      SorobanAuthorizationEntry entry, KeyPair signer, Long validUntilLedgerSeq, Network network) {
    return authorizeEntry(entry, signer, validUntilLedgerSeq, network, null);
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
   * @param signer a {@link KeyPair} which should correspond to the address in the `entry`
   * @param validUntilLedgerSeq the (exclusive) future ledger sequence number until which this
   *     authorization entry should be valid (if `currentLedgerSeq==validUntil`, this is expired)
   * @param network the network is incorprated into the signature
   * @param forAddress which credential node the signature should be written to, see {@link
   *     Auth#authorizeEntry(SorobanAuthorizationEntry, Signer, Long, Network, String)}
   * @return a signed Soroban authorization entry
   */
  public static SorobanAuthorizationEntry authorizeEntry(
      SorobanAuthorizationEntry entry,
      KeyPair signer,
      Long validUntilLedgerSeq,
      Network network,
      @Nullable String forAddress) {
    Signer entrySigner =
        preimage ->
            defaultAccountSignatureScVal(
                signer.getPublicKey(), signer.sign(authorizationPayloadHash(preimage)));

    return authorizeEntry(entry, entrySigner, validUntilLedgerSeq, network, forAddress);
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
   * @param signer a {@link Signer} that takes the authorization preimage (a {@link HashIDPreimage})
   *     and returns the signature {@link SCVal} the account at the entry's address expects
   * @param validUntilLedgerSeq the (exclusive) future ledger sequence number until which this
   *     authorization entry should be valid (if `currentLedgerSeq==validUntil`, this is expired)
   * @param network the network is incorprated into the signature
   * @return a signed Soroban authorization entry
   */
  public static SorobanAuthorizationEntry authorizeEntry(
      String entry, Signer signer, Long validUntilLedgerSeq, Network network) {
    return authorizeEntry(entry, signer, validUntilLedgerSeq, network, null);
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
   * @param signer a {@link Signer} that takes the authorization preimage (a {@link HashIDPreimage})
   *     and returns the signature {@link SCVal} the account at the entry's address expects
   * @param validUntilLedgerSeq the (exclusive) future ledger sequence number until which this
   *     authorization entry should be valid (if `currentLedgerSeq==validUntil`, this is expired)
   * @param network the network is incorprated into the signature
   * @param forAddress which credential node the signature should be written to, see {@link
   *     Auth#authorizeEntry(SorobanAuthorizationEntry, Signer, Long, Network, String)}
   * @return a signed Soroban authorization entry
   */
  public static SorobanAuthorizationEntry authorizeEntry(
      String entry,
      Signer signer,
      Long validUntilLedgerSeq,
      Network network,
      @Nullable String forAddress) {
    SorobanAuthorizationEntry entryXdr;
    try {
      entryXdr = SorobanAuthorizationEntry.fromXdrBase64(entry);
    } catch (IOException e) {
      throw new IllegalArgumentException("Unable to convert entry to SorobanAuthorizationEntry", e);
    }
    return authorizeEntry(entryXdr, signer, validUntilLedgerSeq, network, forAddress);
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
   * @param signer a {@link Signer} that takes the authorization preimage (a {@link HashIDPreimage})
   *     and returns the signature {@link SCVal} the account at the entry's address expects
   * @param validUntilLedgerSeq the (exclusive) future ledger sequence number until which this
   *     authorization entry should be valid (if `currentLedgerSeq==validUntil`, this is expired)
   * @param network the network is incorprated into the signature
   * @return a signed Soroban authorization entry
   */
  public static SorobanAuthorizationEntry authorizeEntry(
      SorobanAuthorizationEntry entry, Signer signer, Long validUntilLedgerSeq, Network network) {
    return authorizeEntry(entry, signer, validUntilLedgerSeq, network, null);
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
   * <p>All address-based credential types are supported: {@code SOROBAN_CREDENTIALS_ADDRESS},
   * {@code SOROBAN_CREDENTIALS_ADDRESS_V2}, and {@code SOROBAN_CREDENTIALS_ADDRESS_WITH_DELEGATES}.
   * The signature payload differs per type, see {@link
   * Auth#buildAuthorizationEntryPreimage(SorobanAuthorizationEntry, long, Network)}. Source-account
   * credentials are returned unchanged (they are covered by the transaction envelope signature).
   *
   * @param entry an unsigned Soroban authorization entry
   * @param signer a {@link Signer} that takes the authorization preimage (a {@link HashIDPreimage})
   *     and returns the signature {@link SCVal} the account at the entry's address expects
   * @param validUntilLedgerSeq the (exclusive) future ledger sequence number until which this
   *     authorization entry should be valid (if `currentLedgerSeq==validUntil`, this is expired)
   * @param network the network is incorprated into the signature
   * @param forAddress which credential node the signature should be written to. Only relevant for
   *     {@code SOROBAN_CREDENTIALS_ADDRESS_WITH_DELEGATES}, where a single entry can be signed by
   *     the top-level account and/or any of its (possibly nested) delegates. Per CAP-71-01 every
   *     one of these signers signs the <em>same</em> payload (bound to the top-level address), so
   *     the signature produced here is written to whichever node(s) carry {@code forAddress}.
   *     Because that shared payload commits to {@code validUntilLedgerSeq}, every signer of one
   *     entry must use the same value — signing with a different value invalidates the signatures
   *     collected so far. When {@code null}, the signature is written to the top-level credentials,
   *     which preserves the behavior for {@code SOROBAN_CREDENTIALS_ADDRESS} / {@code
   *     SOROBAN_CREDENTIALS_ADDRESS_V2} and for accounts whose signing key differs from the
   *     credential address (e.g. multisig).
   * @return a signed Soroban authorization entry
   */
  public static SorobanAuthorizationEntry authorizeEntry(
      SorobanAuthorizationEntry entry,
      Signer signer,
      Long validUntilLedgerSeq,
      Network network,
      @Nullable String forAddress) {
    SorobanAuthorizationEntry clone;
    try {
      clone = SorobanAuthorizationEntry.fromXdrByteArray(entry.toXdrByteArray());
    } catch (IOException e) {
      throw new IllegalArgumentException("Unable to clone SorobanAuthorizationEntry", e);
    }

    if (clone.getCredentials().getDiscriminant()
        == SorobanCredentialsType.SOROBAN_CREDENTIALS_SOURCE_ACCOUNT) {
      return clone;
    }

    SorobanAddressCredentials addressCredentials = getAddressCredentials(clone.getCredentials());
    if (addressCredentials == null) {
      throw new IllegalArgumentException(
          "Unsupported credentials type: " + clone.getCredentials().getDiscriminant());
    }

    // Set the expiration before building the preimage, so the hash that gets signed commits to
    // the same expiration ledger stored in the credentials. Otherwise the network reconstructs
    // the preimage from the (updated) credentials and the signature no longer matches.
    addressCredentials.setSignatureExpirationLedger(
        new Uint32(new XdrUnsignedInteger(validUntilLedgerSeq)));

    HashIDPreimage preimage = buildAuthorizationEntryPreimage(clone, validUntilLedgerSeq, network);

    // The signer returns the final signature SCVal accepted by the account contract at this
    // credential node — the default Stellar Account shape for a G... account, or whatever a custom
    // account contract's __check_auth expects. It is attached verbatim: the network performs the
    // authoritative check via __check_auth, so there is nothing to verify client-side.
    SCVal signatureScVal = signer.sign(preimage);
    if (signatureScVal == null) {
      throw new IllegalArgumentException("signer returned a null signature");
    }

    // CAP-71-01: the signature payload is shared across the top-level address and every
    // (possibly nested) delegate, so this signer's signature is written to whichever credential
    // node(s) carry `forAddress`. When no `forAddress` is given the signature goes to the
    // top-level credentials.
    if (forAddress == null) {
      addressCredentials.setSignature(signatureScVal);
    } else {
      SCAddress forScAddress = new Address(forAddress).toSCAddress();
      int filled = fillMatchingSignatureNodes(clone.getCredentials(), forScAddress, signatureScVal);
      if (filled == 0) {
        throw new IllegalArgumentException(
            "the authorization entry has no credential node for address " + forAddress);
      }
    }
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
   * <p>The returned entry uses legacy {@code SOROBAN_CREDENTIALS_ADDRESS} credentials, which are
   * valid on every network. To opt in to the address-bound {@code SOROBAN_CREDENTIALS_ADDRESS_V2}
   * credentials (CAP-71-02, requires a protocol 27 network), use {@link
   * Auth#authorizeInvocation(KeyPair, Long, SorobanAuthorizedInvocation, Network,
   * SorobanCredentialsType)}. The default will flip to V2 once protocol 28 makes it mandatory.
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
    return authorizeInvocation(
        signer,
        validUntilLedgerSeq,
        invocation,
        network,
        SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS);
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
   * @param credentialsType the credential type for the new entry, either the legacy {@code
   *     SOROBAN_CREDENTIALS_ADDRESS} (the default of the shorter overloads, valid on every network)
   *     or the address-bound {@code SOROBAN_CREDENTIALS_ADDRESS_V2} (CAP-71-02, requires a protocol
   *     27 network). To build a {@code SOROBAN_CREDENTIALS_ADDRESS_WITH_DELEGATES} entry, use
   *     {@link Auth#buildWithDelegatesEntry(SorobanAuthorizationEntry, long, List, SCVal)} instead
   * @return a signed Soroban authorization entry
   */
  public static SorobanAuthorizationEntry authorizeInvocation(
      KeyPair signer,
      Long validUntilLedgerSeq,
      SorobanAuthorizedInvocation invocation,
      Network network,
      SorobanCredentialsType credentialsType) {
    Signer entrySigner =
        preimage ->
            defaultAccountSignatureScVal(
                signer.getPublicKey(), signer.sign(authorizationPayloadHash(preimage)));
    return authorizeInvocation(
        entrySigner,
        signer.getAccountId(),
        validUntilLedgerSeq,
        invocation,
        network,
        credentialsType);
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
   * <p>The returned entry uses legacy {@code SOROBAN_CREDENTIALS_ADDRESS} credentials, which are
   * valid on every network. To opt in to the address-bound {@code SOROBAN_CREDENTIALS_ADDRESS_V2}
   * credentials (CAP-71-02, requires a protocol 27 network), use {@link
   * Auth#authorizeInvocation(Signer, String, Long, SorobanAuthorizedInvocation, Network,
   * SorobanCredentialsType)}. The default will flip to V2 once protocol 28 makes it mandatory.
   *
   * @param signer a {@link Signer} that takes the authorization preimage (a {@link HashIDPreimage})
   *     and returns the signature {@link SCVal} the account at the entry's address expects
   * @param address the address being authorized — a classic {@code G...} account or a {@code C...}
   *     contract address (the typical custom-account case)
   * @param validUntilLedgerSeq the (exclusive) future ledger sequence number until which this
   *     authorization entry should be valid (if `currentLedgerSeq==validUntil`, this is expired)
   * @param invocation invocation the invocation tree that we're authorizing (likely, this comes
   *     from transaction simulation)
   * @param network the network is incorprated into the signature
   * @return a signed Soroban authorization entry
   */
  public static SorobanAuthorizationEntry authorizeInvocation(
      Signer signer,
      String address,
      Long validUntilLedgerSeq,
      SorobanAuthorizedInvocation invocation,
      Network network) {
    return authorizeInvocation(
        signer,
        address,
        validUntilLedgerSeq,
        invocation,
        network,
        SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS);
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
   * @param signer a {@link Signer} that takes the authorization preimage (a {@link HashIDPreimage})
   *     and returns the signature {@link SCVal} the account at the entry's address expects
   * @param address the address being authorized — a classic {@code G...} account or a {@code C...}
   *     contract address (the typical custom-account case)
   * @param validUntilLedgerSeq the (exclusive) future ledger sequence number until which this
   *     authorization entry should be valid (if `currentLedgerSeq==validUntil`, this is expired)
   * @param invocation invocation the invocation tree that we're authorizing (likely, this comes
   *     from transaction simulation)
   * @param network the network is incorprated into the signature
   * @param credentialsType the credential type for the new entry, either the legacy {@code
   *     SOROBAN_CREDENTIALS_ADDRESS} (the default of the shorter overloads, valid on every network)
   *     or the address-bound {@code SOROBAN_CREDENTIALS_ADDRESS_V2} (CAP-71-02, requires a protocol
   *     27 network). To build a {@code SOROBAN_CREDENTIALS_ADDRESS_WITH_DELEGATES} entry, use
   *     {@link Auth#buildWithDelegatesEntry(SorobanAuthorizationEntry, long, List, SCVal)} instead
   * @return a signed Soroban authorization entry
   */
  public static SorobanAuthorizationEntry authorizeInvocation(
      Signer signer,
      String address,
      Long validUntilLedgerSeq,
      SorobanAuthorizedInvocation invocation,
      Network network,
      SorobanCredentialsType credentialsType) {
    long nonce = new SecureRandom().nextLong();
    SorobanAddressCredentials addressCredentials =
        SorobanAddressCredentials.builder()
            .address(new Address(address).toSCAddress())
            .nonce(new Int64(nonce))
            .signatureExpirationLedger(new Uint32(new XdrUnsignedInteger(validUntilLedgerSeq)))
            .signature(Scv.toVoid())
            .build();
    SorobanCredentials credentials;
    if (credentialsType == SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS) {
      credentials =
          SorobanCredentials.builder()
              .discriminant(credentialsType)
              .address(addressCredentials)
              .build();
    } else if (credentialsType == SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS_V2) {
      credentials =
          SorobanCredentials.builder()
              .discriminant(credentialsType)
              .addressV2(addressCredentials)
              .build();
    } else {
      throw new IllegalArgumentException(
          "credentialsType must be SOROBAN_CREDENTIALS_ADDRESS or SOROBAN_CREDENTIALS_ADDRESS_V2; use buildWithDelegatesEntry to build SOROBAN_CREDENTIALS_ADDRESS_WITH_DELEGATES entries");
    }
    SorobanAuthorizationEntry entry =
        SorobanAuthorizationEntry.builder()
            .credentials(credentials)
            .rootInvocation(invocation)
            .build();
    return authorizeEntry(entry, signer, validUntilLedgerSeq, network);
  }

  /**
   * Extracts the {@link SorobanAddressCredentials} from any address-based Soroban credential,
   * regardless of which credential type variant is used.
   *
   * <p>This unifies access across {@code SOROBAN_CREDENTIALS_ADDRESS}, {@code
   * SOROBAN_CREDENTIALS_ADDRESS_V2} (which carries identical fields but binds the address into the
   * signature payload, CAP-71-02), and {@code SOROBAN_CREDENTIALS_ADDRESS_WITH_DELEGATES} (which
   * wraps the same address credentials alongside a set of delegate signatures, CAP-71-01).
   *
   * @param credentials the credentials to inspect
   * @return the inner address credentials, or {@code null} for source-account credentials (which
   *     carry no address payload)
   */
  @Nullable
  public static SorobanAddressCredentials getAddressCredentials(SorobanCredentials credentials) {
    switch (credentials.getDiscriminant()) {
      case SOROBAN_CREDENTIALS_ADDRESS:
        return credentials.getAddress();
      case SOROBAN_CREDENTIALS_ADDRESS_V2:
        return credentials.getAddressV2();
      case SOROBAN_CREDENTIALS_ADDRESS_WITH_DELEGATES:
        return credentials.getAddressWithDelegates().getAddressCredentials();
      default:
        return null;
    }
  }

  /**
   * Returns the 32-byte payload that account contracts receive in {@code __check_auth}: the SHA-256
   * hash of the authorization preimage's XDR bytes.
   *
   * <p>Use this inside a custom {@link Signer} to obtain the exact bytes the host asks the account
   * contract to verify, then return whatever signature {@link SCVal} the contract expects. It is
   * the same payload the {@link KeyPair} signing path signs.
   *
   * @param preimage the Soroban authorization preimage, see {@link
   *     Auth#buildAuthorizationEntryPreimage(SorobanAuthorizationEntry, long, Network)}
   * @return the SHA-256 hash of the preimage XDR bytes
   */
  public static byte[] authorizationPayloadHash(HashIDPreimage preimage) {
    try {
      return Util.hash(preimage.toXdrByteArray());
    } catch (IOException e) {
      throw new IllegalArgumentException("Unable to convert preimage to bytes", e);
    }
  }

  /**
   * Builds the signature {@link SCVal} shape expected by the default Stellar Account contract:
   * {@code Vec<Map{public_key: Bytes, signature: Bytes}>}.
   *
   * <p>This is the building block for ed25519 {@link Signer} implementations that sign elsewhere (a
   * hardware module or remote signing service) yet target a classic {@code G...} account — the same
   * shape the {@link KeyPair} signing path produces. Use this {@code byte[]} overload when you
   * already hold the raw public key (e.g. from an HSM); use {@link
   * Auth#defaultAccountSignatureScVal(String, byte[])} to pass a {@code G...} address instead.
   *
   * @param publicKey the raw 32-byte ed25519 public key that produced {@code signature}
   * @param signature the 64-byte ed25519 signature over {@link
   *     Auth#authorizationPayloadHash(HashIDPreimage)}
   * @return the default Stellar Account signature value
   * @see <a
   *     href="https://developers.stellar.org/docs/learn/fundamentals/contract-development/contract-interactions/stellar-transaction#stellar-account-signatures"
   *     target="_blank">Stellar Account signatures</a>
   */
  public static SCVal defaultAccountSignatureScVal(byte[] publicKey, byte[] signature) {
    SCVal signatureStruct =
        Scv.toMap(
            new LinkedHashMap<SCVal, SCVal>() {
              {
                put(Scv.toSymbol("public_key"), Scv.toBytes(publicKey));
                put(Scv.toSymbol("signature"), Scv.toBytes(signature));
              }
            });
    return Scv.toVec(Collections.singleton(signatureStruct));
  }

  /**
   * Builds the default Stellar Account signature {@link SCVal} from a classic {@code G...} account
   * address — a convenience wrapper over {@link Auth#defaultAccountSignatureScVal(byte[], byte[])}
   * that decodes {@code accountId} to its raw ed25519 public key.
   *
   * @param accountId the {@code G...} account whose ed25519 public key produced {@code signature}
   * @param signature the 64-byte ed25519 signature over {@link
   *     Auth#authorizationPayloadHash(HashIDPreimage)}
   * @return the default Stellar Account signature value
   */
  public static SCVal defaultAccountSignatureScVal(String accountId, byte[] signature) {
    return defaultAccountSignatureScVal(KeyPair.fromAccountId(accountId).getPublicKey(), signature);
  }

  /**
   * Builds the {@link HashIDPreimage} whose hash a signer must sign to authorize {@code entry}.
   * This is the low-level signature payload used by {@link Auth#authorizeEntry}, exposed for
   * callers that drive signing themselves — most notably for {@code
   * SOROBAN_CREDENTIALS_ADDRESS_WITH_DELEGATES}, where the client (not simulation) decides which
   * delegates sign and how.
   *
   * <p>For {@code SOROBAN_CREDENTIALS_ADDRESS} this is the legacy, non-address-bound {@code
   * ENVELOPE_TYPE_SOROBAN_AUTHORIZATION} preimage. For {@code SOROBAN_CREDENTIALS_ADDRESS_V2} and
   * {@code SOROBAN_CREDENTIALS_ADDRESS_WITH_DELEGATES} it is the address-bound {@code
   * ENVELOPE_TYPE_SOROBAN_AUTHORIZATION_WITH_ADDRESS} preimage (CAP-71). For the delegates variant
   * this single payload — bound to the <em>top-level</em> address — is what the top-level account
   * and every (nested) delegate each sign.
   *
   * <p>To get the raw bytes to sign, hash the XDR: {@code Util.hash(preimage.toXdrByteArray())}.
   *
   * @param entry the authorization entry to build the payload for
   * @param validUntilLedgerSeq the expiration ledger committed into the payload (must match the
   *     {@code signatureExpirationLedger} on the credentials you submit)
   * @param network the network whose id is mixed into the payload
   * @return the preimage to hash and sign
   * @throws IllegalArgumentException if {@code entry} carries source-account or otherwise
   *     non-address credentials
   */
  public static HashIDPreimage buildAuthorizationEntryPreimage(
      SorobanAuthorizationEntry entry, long validUntilLedgerSeq, Network network) {
    SorobanCredentials credentials = entry.getCredentials();
    SorobanAddressCredentials addressCredentials = getAddressCredentials(credentials);
    if (addressCredentials == null) {
      throw new IllegalArgumentException(
          "cannot build a signature payload for credentials type: "
              + credentials.getDiscriminant());
    }

    Uint32 signatureExpirationLedger = new Uint32(new XdrUnsignedInteger(validUntilLedgerSeq));
    switch (credentials.getDiscriminant()) {
      // legacy address credentials are not address-bound
      case SOROBAN_CREDENTIALS_ADDRESS:
        return HashIDPreimage.builder()
            .discriminant(EnvelopeType.ENVELOPE_TYPE_SOROBAN_AUTHORIZATION)
            .sorobanAuthorization(
                HashIDPreimage.HashIDPreimageSorobanAuthorization.builder()
                    .networkID(new Hash(network.getNetworkId()))
                    .nonce(addressCredentials.getNonce())
                    .signatureExpirationLedger(signatureExpirationLedger)
                    .invocation(entry.getRootInvocation())
                    .build())
            .build();
      // ADDRESS_V2 and ADDRESS_WITH_DELEGATES bind the address into the signed payload via the
      // WithAddress preimage (CAP-71)
      case SOROBAN_CREDENTIALS_ADDRESS_V2:
      case SOROBAN_CREDENTIALS_ADDRESS_WITH_DELEGATES:
        return HashIDPreimage.builder()
            .discriminant(EnvelopeType.ENVELOPE_TYPE_SOROBAN_AUTHORIZATION_WITH_ADDRESS)
            .sorobanAuthorizationWithAddress(
                HashIDPreimage.HashIDPreimageSorobanAuthorizationWithAddress.builder()
                    .networkID(new Hash(network.getNetworkId()))
                    .nonce(addressCredentials.getNonce())
                    .signatureExpirationLedger(signatureExpirationLedger)
                    .address(addressCredentials.getAddress())
                    .invocation(entry.getRootInvocation())
                    .build())
            .build();
      default:
        throw new IllegalArgumentException(
            "unsupported credentials type: " + credentials.getDiscriminant());
    }
  }

  /**
   * Builds a {@code SOROBAN_CREDENTIALS_ADDRESS_WITH_DELEGATES} authorization entry (CAP-71-01) by
   * wrapping the address credentials of an existing {@code SOROBAN_CREDENTIALS_ADDRESS} or {@code
   * SOROBAN_CREDENTIALS_ADDRESS_V2} entry (e.g. one returned by simulation) together with a
   * caller-provided set of delegate signers.
   *
   * <p>Simulation never emits the delegates variant on its own — which accounts use delegated
   * authentication is account-specific policy known only to the client (much like a multisig
   * policy). This helper just assembles the wrapper XDR; you supply the delegate tree (addresses
   * and, optionally, signatures). To produce the signatures, build the shared payload with {@link
   * Auth#buildAuthorizationEntryPreimage(SorobanAuthorizationEntry, long, Network)} on the returned
   * entry and sign it, or fill each node afterwards with {@link
   * Auth#authorizeEntry(SorobanAuthorizationEntry, Signer, Long, Network, String)} (passing the
   * signer's address as {@code forAddress} and the same {@code validUntilLedgerSeq} as given here —
   * the shared payload commits to it).
   *
   * <p>Which delegates must actually sign is decided by the top-level account contract: per
   * CAP-71-01 the host verifies a listed delegate only when the account's {@code __check_auth}
   * consumes it, so a delegate left with its void placeholder is valid as long as the account's
   * policy does not require it.
   *
   * <p>Each delegates list (the top-level set and every {@code nestedDelegates}) is sorted by
   * address in ascending order, and duplicate addresses within a list are rejected, as the protocol
   * requires (CAP-71-01) — otherwise the host rejects the entry.
   *
   * @param entry an existing {@code SOROBAN_CREDENTIALS_ADDRESS} or {@code
   *     SOROBAN_CREDENTIALS_ADDRESS_V2} entry whose address credentials should be wrapped
   * @param validUntilLedgerSeq the expiration ledger sequence stored on the top-level credentials
   * @param delegates the delegate signers to attach
   * @param signature the top-level account's signature, or {@code null} for an {@code SCV_VOID}
   *     placeholder, which is valid for accounts that authorize purely via delegated signers
   * @return a new {@code SOROBAN_CREDENTIALS_ADDRESS_WITH_DELEGATES} authorization entry
   * @throws IllegalArgumentException if {@code entry} does not carry {@code ADDRESS} / {@code
   *     ADDRESS_V2} credentials, or if any delegates list contains a duplicate address
   */
  public static SorobanAuthorizationEntry buildWithDelegatesEntry(
      SorobanAuthorizationEntry entry,
      long validUntilLedgerSeq,
      List<DelegateSignature> delegates,
      @Nullable SCVal signature) {
    SorobanCredentials credentials = entry.getCredentials();
    if (credentials.getDiscriminant() != SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS
        && credentials.getDiscriminant() != SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS_V2) {
      throw new IllegalArgumentException(
          "buildWithDelegatesEntry expects SOROBAN_CREDENTIALS_ADDRESS or SOROBAN_CREDENTIALS_ADDRESS_V2 credentials, got "
              + credentials.getDiscriminant());
    }
    SorobanAddressCredentials addressCredentials = getAddressCredentials(credentials);

    return SorobanAuthorizationEntry.builder()
        .credentials(
            SorobanCredentials.builder()
                .discriminant(SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS_WITH_DELEGATES)
                .addressWithDelegates(
                    SorobanAddressCredentialsWithDelegates.builder()
                        .addressCredentials(
                            SorobanAddressCredentials.builder()
                                .address(addressCredentials.getAddress())
                                .nonce(addressCredentials.getNonce())
                                .signatureExpirationLedger(
                                    new Uint32(new XdrUnsignedInteger(validUntilLedgerSeq)))
                                .signature(signature != null ? signature : Scv.toVoid())
                                .build())
                        .delegates(buildDelegateNodes(delegates))
                        .build())
                .build())
        .rootInvocation(entry.getRootInvocation())
        .build();
  }

  /**
   * Recursively converts {@link DelegateSignature} descriptors into {@link
   * SorobanDelegateSignature} nodes, sorting each level by address and rejecting duplicates
   * (CAP-71-01).
   */
  private static SorobanDelegateSignature[] buildDelegateNodes(
      @Nullable List<DelegateSignature> delegates) {
    if (delegates == null || delegates.isEmpty()) {
      return new SorobanDelegateSignature[0];
    }

    // Sort keys are the XDR encoding of each address; serialize each address once up front
    // instead of inside the comparator.
    List<Map.Entry<byte[], SorobanDelegateSignature>> keyedNodes =
        new ArrayList<>(delegates.size());
    for (DelegateSignature delegate : delegates) {
      SorobanDelegateSignature node =
          SorobanDelegateSignature.builder()
              .address(new Address(delegate.getAddress()).toSCAddress())
              .signature(delegate.getSignature() != null ? delegate.getSignature() : Scv.toVoid())
              .nestedDelegates(buildDelegateNodes(delegate.getNestedDelegates()))
              .build();
      keyedNodes.add(
          new AbstractMap.SimpleImmutableEntry<>(scAddressXdrBytes(node.getAddress()), node));
    }

    keyedNodes.sort((a, b) -> Util.compareBytesUnsigned(a.getKey(), b.getKey()));

    SorobanDelegateSignature[] nodes = new SorobanDelegateSignature[keyedNodes.size()];
    for (int i = 0; i < keyedNodes.size(); i++) {
      if (i > 0
          && Util.compareBytesUnsigned(keyedNodes.get(i - 1).getKey(), keyedNodes.get(i).getKey())
              == 0) {
        throw new IllegalArgumentException(
            "duplicate delegate address "
                + Address.fromSCAddress(keyedNodes.get(i).getValue().getAddress()).toString());
      }
      nodes[i] = keyedNodes.get(i).getValue();
    }

    return nodes;
  }

  /**
   * Writes {@code signature} to every credential node whose address equals {@code forAddress}: the
   * top-level address credentials and, for the delegates variant, each (possibly nested) {@link
   * SorobanDelegateSignature}. Returns the number of nodes filled.
   */
  private static int fillMatchingSignatureNodes(
      SorobanCredentials credentials, SCAddress forAddress, SCVal signature) {
    int filled = 0;
    SorobanAddressCredentials addressCredentials = getAddressCredentials(credentials);
    if (addressCredentials != null && addressCredentials.getAddress().equals(forAddress)) {
      addressCredentials.setSignature(signature);
      filled++;
    }
    if (credentials.getDiscriminant()
        == SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS_WITH_DELEGATES) {
      for (SorobanDelegateSignature delegate :
          credentials.getAddressWithDelegates().getDelegates()) {
        filled += fillMatchingDelegateNodes(delegate, forAddress, signature);
      }
    }
    return filled;
  }

  private static int fillMatchingDelegateNodes(
      SorobanDelegateSignature node, SCAddress forAddress, SCVal signature) {
    int filled = 0;
    if (node.getAddress().equals(forAddress)) {
      node.setSignature(signature);
      filled++;
    }
    if (node.getNestedDelegates() != null) {
      for (SorobanDelegateSignature nested : node.getNestedDelegates()) {
        filled += fillMatchingDelegateNodes(nested, forAddress, signature);
      }
    }
    return filled;
  }

  private static byte[] scAddressXdrBytes(SCAddress address) {
    try {
      return address.toXdrByteArray();
    } catch (IOException e) {
      throw new UnexpectedException(e);
    }
  }

  /**
   * A delegate signer to attach to a {@code SOROBAN_CREDENTIALS_ADDRESS_WITH_DELEGATES} entry via
   * {@link Auth#buildWithDelegatesEntry(SorobanAuthorizationEntry, long, List, SCVal)} (CAP-71-01).
   */
  @Value
  @Builder
  public static class DelegateSignature {
    /** The delegate's address ({@code G...} account or {@code C...} contract). */
    String address;

    /**
     * The delegate's signature value, or {@code null} for an {@code SCV_VOID} placeholder, which
     * you can fill afterwards with {@link Auth#authorizeEntry(SorobanAuthorizationEntry, Signer,
     * Long, Network, String)} (passing this address as {@code forAddress}) or by editing the entry
     * directly.
     */
    @Nullable SCVal signature;

    /** Signers this delegate in turn delegates to (recursive), or {@code null} for none. */
    @Nullable List<DelegateSignature> nestedDelegates;
  }

  /**
   * Signs a Soroban authorization preimage, returning the signature {@link SCVal} accepted by the
   * account contract at the credential node being signed — the default Stellar Account shape for a
   * classic {@code G...} account (see {@link Auth#defaultAccountSignatureScVal(String, byte[])}),
   * or whatever the custom account contract's {@code __check_auth} expects (BLS, WebAuthn /
   * secp256r1, threshold, policy contracts, ...).
   *
   * <p>Use {@link Auth#authorizationPayloadHash(HashIDPreimage)} to obtain the 32-byte payload the
   * host hashes from the preimage and hands to {@code __check_auth}.
   */
  @FunctionalInterface
  public interface Signer {
    SCVal sign(HashIDPreimage preimage);
  }
}
