package org.stellar.sdk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import org.stellar.sdk.exception.UnexpectedException;
import org.stellar.sdk.xdr.DecoratedSignature;
import org.stellar.sdk.xdr.Hash;
import org.stellar.sdk.xdr.SignatureHint;
import org.stellar.sdk.xdr.TransactionEnvelope;
import org.stellar.sdk.xdr.TransactionSignaturePayload;

/** Abstract class for transaction classes. */
public abstract class AbstractTransaction {
  /** The network that the transaction is to be submitted to. */
  @NonNull @Getter protected final Network network;

  /** The {@link AccountConverter} for this transaction. */
  @Getter @NonNull protected final AccountConverter accountConverter;

  /** List of signatures attached to this transaction. */
  @NonNull protected List<DecoratedSignature> signatures;

  public static final int MIN_BASE_FEE = 100;

  AbstractTransaction(@NonNull AccountConverter accountConverter, @NonNull Network network) {
    this.accountConverter = accountConverter;
    this.network = network;
    this.signatures = new ArrayList<>();
  }

  /**
   * Adds a new signature ed25519PublicKey to this transaction.
   *
   * @param signer {@link KeyPair} object representing a signer
   */
  public void sign(@NonNull KeyPair signer) {
    byte[] txHash = this.hash();
    signatures.add(signer.signDecorated(txHash));
  }

  /**
   * Adds a new sha256Hash signature to this transaction by revealing preimage.
   *
   * @param preimage the sha256 hash of preimage should be equal to signer hash
   */
  public void sign(byte @NonNull [] preimage) {
    org.stellar.sdk.xdr.Signature signature = new org.stellar.sdk.xdr.Signature();
    signature.setSignature(preimage);

    byte[] hash = Util.hash(preimage);
    byte[] signatureHintBytes = Arrays.copyOfRange(hash, hash.length - 4, hash.length);
    SignatureHint signatureHint = new SignatureHint();
    signatureHint.setSignatureHint(signatureHintBytes);

    DecoratedSignature decoratedSignature = new DecoratedSignature();
    decoratedSignature.setHint(signatureHint);
    decoratedSignature.setSignature(signature);

    signatures.add(decoratedSignature);
  }

  /**
   * Returns transaction hash.
   *
   * @return the transaction hash
   */
  public byte[] hash() {
    return Util.hash(this.signatureBase());
  }

  /**
   * Returns transaction hash encoded as a hexadecimal string.
   *
   * @return the transaction hash as a hexadecimal string
   */
  public String hashHex() {
    return Util.bytesToHex(this.hash()).toLowerCase();
  }

  /**
   * Returns signature base.
   *
   * @return the signature base
   */
  public abstract byte[] signatureBase();

  /**
   * Gets read only list(immutable) of the signatures on transaction.
   *
   * @return immutable list of signatures
   */
  public List<DecoratedSignature> getSignatures() {
    return Collections.unmodifiableList(signatures);
  }

  /**
   * Adds a signature to the transaction's existing list of signatures.
   *
   * @param signature the signature to add
   */
  public void addSignature(DecoratedSignature signature) {
    signatures.add(signature);
  }

  public abstract TransactionEnvelope toEnvelopeXdr();

  /**
   * Returns base64-encoded TransactionEnvelope XDR object. Transaction need to have at least one
   * signature.
   *
   * @return the base64-encoded TransactionEnvelope XDR object.
   */
  public String toEnvelopeXdrBase64() {
    try {
      return toEnvelopeXdr().toXdrBase64();
    } catch (IOException e) {
      throw new UnexpectedException(e);
    }
  }

  /**
   * Creates a <code>AbstractTransaction</code> instance from previously build <code>
   * TransactionEnvelope</code>
   *
   * @param accountConverter the {@link AccountConverter} to use for this transaction
   * @param envelope the transaction envelope
   * @param network the network that the transaction is to be submitted to
   * @return the {@link Transaction} or {@link FeeBumpTransaction} instance
   */
  public static AbstractTransaction fromEnvelopeXdr(
      AccountConverter accountConverter, TransactionEnvelope envelope, Network network) {
    switch (envelope.getDiscriminant()) {
      case ENVELOPE_TYPE_TX:
        return Transaction.fromV1EnvelopeXdr(accountConverter, envelope.getV1(), network);
      case ENVELOPE_TYPE_TX_V0:
        return Transaction.fromV0EnvelopeXdr(accountConverter, envelope.getV0(), network);
      case ENVELOPE_TYPE_TX_FEE_BUMP:
        return FeeBumpTransaction.fromFeeBumpTransactionEnvelope(
            accountConverter, envelope.getFeeBump(), network);
      default:
        throw new IllegalArgumentException(
            "transaction type is not supported: " + envelope.getDiscriminant());
    }
  }

  /**
   * Creates a <code>AbstractTransaction</code> instance from previously build <code>
   * TransactionEnvelope</code>
   *
   * @param envelope the transaction envelope
   * @param network the network that the transaction is to be submitted to
   * @return the {@link Transaction} or {@link FeeBumpTransaction} instance
   */
  public static AbstractTransaction fromEnvelopeXdr(TransactionEnvelope envelope, Network network) {
    return fromEnvelopeXdr(AccountConverter.enableMuxed(), envelope, network);
  }

  /**
   * Creates a <code>Transaction</code> instance from previously build <code>TransactionEnvelope
   * </code>
   *
   * @param envelope Base-64 encoded <code>TransactionEnvelope</code>
   * @return the {@link Transaction} or {@link FeeBumpTransaction} instance
   * @throws IllegalArgumentException if the envelope is malformed
   */
  public static AbstractTransaction fromEnvelopeXdr(
      AccountConverter accountConverter, String envelope, Network network) {
    TransactionEnvelope transactionEnvelope = null;
    try {
      transactionEnvelope = TransactionEnvelope.fromXdrBase64(envelope);
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
    return fromEnvelopeXdr(accountConverter, transactionEnvelope, network);
  }

  /**
   * Creates a <code>Transaction</code> instance from previously build <code>TransactionEnvelope
   * </code>
   *
   * @param envelope Base-64 encoded <code>TransactionEnvelope</code>
   * @return the {@link Transaction} or {@link FeeBumpTransaction} instance
   * @throws IllegalArgumentException if the envelope is malformed
   */
  public static AbstractTransaction fromEnvelopeXdr(String envelope, Network network) {
    return fromEnvelopeXdr(AccountConverter.enableMuxed(), envelope, network);
  }

  /**
   * Get the signature base of this transaction envelope.
   *
   * @param taggedTransaction the tagged transaction for signing
   * @param network the network that the transaction is to be submitted to
   * @return the signature base of this transaction envelope
   */
  public static byte[] getTransactionSignatureBase(
      TransactionSignaturePayload.TransactionSignaturePayloadTaggedTransaction taggedTransaction,
      Network network) {
    try {
      Hash networkIdHash = new Hash(network.getNetworkId());
      TransactionSignaturePayload payload =
          TransactionSignaturePayload.builder()
              .networkId(networkIdHash)
              .taggedTransaction(taggedTransaction)
              .build();
      return payload.toXdrByteArray();
    } catch (IOException e) {
      throw new UnexpectedException(e);
    }
  }
}
