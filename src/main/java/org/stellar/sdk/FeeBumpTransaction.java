package org.stellar.sdk;

import java.util.ArrayList;
import java.util.Arrays;
import lombok.Getter;
import lombok.NonNull;
import org.stellar.sdk.xdr.DecoratedSignature;
import org.stellar.sdk.xdr.EnvelopeType;
import org.stellar.sdk.xdr.FeeBumpTransactionEnvelope;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.TransactionEnvelope;
import org.stellar.sdk.xdr.TransactionSignaturePayload;

/**
 * Represents <a href="https://github.com/stellar/stellar-protocol/blob/master/core/cap-0015.md"
 * target="_blank">Fee Bump Transaction</a> in Stellar network.
 *
 * @see <a href="https://developers.stellar.org/docs/encyclopedia/fee-bump-transactions">Fee-bump
 *     Transactions</a>
 */
@Getter
public class FeeBumpTransaction extends AbstractTransaction {
  /** The account paying for the transaction fee. */
  @NonNull private final String feeSource;

  /** The max fee willing to be paid for this transaction. */
  private final long fee;

  /** The inner transaction that is being wrapped by this fee bump transaction. */
  @NonNull private final Transaction innerTransaction;

  /**
   * Creates a new FeeBumpTransaction object, enabling you to resubmit an existing transaction with
   * a higher fee.
   *
   * @param feeSource The account paying for the transaction fee.
   * @param baseFee Max fee willing to pay per operation in inner transaction (in stroops)
   * @param innerTransaction The inner transaction that is being wrapped by this fee bump
   *     transaction.
   */
  public FeeBumpTransaction(
      @NonNull String feeSource, long baseFee, @NonNull Transaction innerTransaction) {
    super(innerTransaction.getNetwork());
    this.feeSource = feeSource;

    // set fee
    if (baseFee < MIN_BASE_FEE) {
      throw new IllegalArgumentException(
          "baseFee cannot be smaller than the BASE_FEE (" + MIN_BASE_FEE + "): " + baseFee);
    }

    long innerBaseFee = innerTransaction.getFee();
    long numOperations = innerTransaction.getOperations().length;
    if (numOperations > 0) {
      innerBaseFee = innerBaseFee / numOperations;
    }

    if (baseFee < innerBaseFee) {
      throw new IllegalArgumentException(
          "base fee cannot be lower than provided inner transaction base fee");
    }

    long maxFee = baseFee * (numOperations + 1);
    if (maxFee < 0) {
      throw new IllegalArgumentException("fee overflows 64 bit int");
    }
    fee = maxFee;

    // set inner transaction
    EnvelopeType txType = innerTransaction.toEnvelopeXdr().getDiscriminant();
    if (txType == EnvelopeType.ENVELOPE_TYPE_TX_V0) {
      this.innerTransaction =
          new TransactionBuilder(
                  new Account(
                      innerTransaction.getSourceAccount(),
                      innerTransaction.getSequenceNumber() - 1),
                  innerTransaction.getNetwork())
              .setBaseFee((int) innerTransaction.getFee())
              .addOperations(Arrays.asList(innerTransaction.getOperations()))
              .addMemo(innerTransaction.getMemo())
              .addPreconditions(
                  new TransactionPreconditions.TransactionPreconditionsBuilder()
                      .timeBounds(innerTransaction.getTimeBounds())
                      .build())
              .build();
      this.innerTransaction.signatures = new ArrayList<>(innerTransaction.signatures);
    } else {
      this.innerTransaction = innerTransaction;
    }
  }

  public static FeeBumpTransaction fromFeeBumpTransactionEnvelope(
      FeeBumpTransactionEnvelope envelope, Network network) {
    Transaction inner =
        Transaction.fromV1EnvelopeXdr(envelope.getTx().getInnerTx().getV1(), network);
    String feeSource = StrKey.encodeMuxedAccount(envelope.getTx().getFeeSource());

    long fee = envelope.getTx().getFee().getInt64();
    long baseFee = fee / (inner.getOperations().length + 1);

    FeeBumpTransaction feeBump = new FeeBumpTransaction(feeSource, baseFee, inner);
    feeBump.signatures.addAll(Arrays.asList(envelope.getSignatures()));

    return feeBump;
  }

  private org.stellar.sdk.xdr.FeeBumpTransaction toXdr() {
    org.stellar.sdk.xdr.FeeBumpTransaction xdr = new org.stellar.sdk.xdr.FeeBumpTransaction();
    xdr.setExt(new org.stellar.sdk.xdr.FeeBumpTransaction.FeeBumpTransactionExt());
    xdr.getExt().setDiscriminant(0);

    Int64 xdrFee = new Int64();
    xdrFee.setInt64(fee);
    xdr.setFee(xdrFee);
    xdr.setFeeSource(StrKey.decodeMuxedAccount(this.feeSource));
    org.stellar.sdk.xdr.FeeBumpTransaction.FeeBumpTransactionInnerTx innerXDR =
        new org.stellar.sdk.xdr.FeeBumpTransaction.FeeBumpTransactionInnerTx();
    innerXDR.setDiscriminant(EnvelopeType.ENVELOPE_TYPE_TX);
    innerXDR.setV1(this.innerTransaction.toEnvelopeXdr().getV1());
    xdr.setInnerTx(innerXDR);
    return xdr;
  }

  @Override
  public byte[] signatureBase() {
    TransactionSignaturePayload.TransactionSignaturePayloadTaggedTransaction taggedTransaction =
        new TransactionSignaturePayload.TransactionSignaturePayloadTaggedTransaction();
    taggedTransaction.setDiscriminant(EnvelopeType.ENVELOPE_TYPE_TX_FEE_BUMP);
    taggedTransaction.setFeeBump(this.toXdr());
    return getTransactionSignatureBase(taggedTransaction, network);
  }

  /** Generates TransactionEnvelope XDR object. */
  @Override
  public TransactionEnvelope toEnvelopeXdr() {
    TransactionEnvelope xdr = new TransactionEnvelope();
    FeeBumpTransactionEnvelope feeBumpEnvelope = new FeeBumpTransactionEnvelope();
    xdr.setDiscriminant(EnvelopeType.ENVELOPE_TYPE_TX_FEE_BUMP);

    feeBumpEnvelope.setTx(this.toXdr());

    DecoratedSignature[] signatures = new DecoratedSignature[this.signatures.size()];
    signatures = this.signatures.toArray(signatures);
    feeBumpEnvelope.setSignatures(signatures);

    xdr.setFeeBump(feeBumpEnvelope);
    return xdr;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    FeeBumpTransaction that = (FeeBumpTransaction) object;
    return Arrays.equals(signatureBase(), that.signatureBase());
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(signatureBase());
  }
}
