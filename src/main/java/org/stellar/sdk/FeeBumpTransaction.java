package org.stellar.sdk;

import java.util.ArrayList;
import java.util.Arrays;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = true)
public class FeeBumpTransaction extends AbstractTransaction {
  /** The max fee willing to be paid for this transaction. */
  private final long fee;

  /** The account paying for the transaction fee. */
  @NonNull private final String feeAccount;

  /** The inner transaction that is being wrapped by this fee bump transaction. */
  @NonNull private final Transaction innerTransaction;

  FeeBumpTransaction(
      AccountConverter accountConverter,
      @NonNull String feeAccount,
      long fee,
      @NonNull Transaction innerTransaction) {
    super(accountConverter, innerTransaction.getNetwork());
    this.feeAccount = feeAccount;
    this.innerTransaction = innerTransaction;
    this.fee = fee;
  }

  public static FeeBumpTransaction fromFeeBumpTransactionEnvelope(
      AccountConverter accountConverter, FeeBumpTransactionEnvelope envelope, Network network) {
    Transaction inner =
        Transaction.fromV1EnvelopeXdr(
            accountConverter, envelope.getTx().getInnerTx().getV1(), network);
    String feeAccount = accountConverter.decode(envelope.getTx().getFeeSource());

    long fee = envelope.getTx().getFee().getInt64();

    FeeBumpTransaction feeBump = new FeeBumpTransaction(accountConverter, feeAccount, fee, inner);
    feeBump.signatures.addAll(Arrays.asList(envelope.getSignatures()));

    return feeBump;
  }

  public static FeeBumpTransaction fromFeeBumpTransactionEnvelope(
      FeeBumpTransactionEnvelope envelope, Network network) {
    return fromFeeBumpTransactionEnvelope(AccountConverter.enableMuxed(), envelope, network);
  }

  private org.stellar.sdk.xdr.FeeBumpTransaction toXdr() {
    org.stellar.sdk.xdr.FeeBumpTransaction xdr = new org.stellar.sdk.xdr.FeeBumpTransaction();
    xdr.setExt(new org.stellar.sdk.xdr.FeeBumpTransaction.FeeBumpTransactionExt());
    xdr.getExt().setDiscriminant(0);

    Int64 xdrFee = new Int64();
    xdrFee.setInt64(fee);
    xdr.setFee(xdrFee);

    xdr.setFeeSource(accountConverter.encode(this.feeAccount));

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

  /** Builds a new FeeBumpTransaction object. */
  public static class Builder {
    private final Transaction innerTransaction;
    private Long baseFee;
    private String feeAccount;
    private final AccountConverter accountConverter;

    /**
     * Construct a new fee bump transaction builder.
     *
     * @param accountConverter The AccountConverter which will be used to encode the fee account.
     * @param inner The inner transaction which will be fee bumped. read-only, the
     */
    public Builder(@NonNull AccountConverter accountConverter, @NonNull final Transaction inner) {
      EnvelopeType txType = inner.toEnvelopeXdr().getDiscriminant();
      this.accountConverter = accountConverter;
      if (txType == EnvelopeType.ENVELOPE_TYPE_TX_V0) {
        this.innerTransaction =
            new TransactionBuilder(
                    inner.accountConverter,
                    new Account(inner.getSourceAccount(), inner.getSequenceNumber() - 1),
                    inner.getNetwork())
                .setBaseFee((int) inner.getFee())
                .addOperations(Arrays.asList(inner.getOperations()))
                .addMemo(inner.getMemo())
                .addPreconditions(
                    new TransactionPreconditions.TransactionPreconditionsBuilder()
                        .timeBounds(inner.getTimeBounds())
                        .build())
                .build();
        this.innerTransaction.signatures = new ArrayList<>(inner.signatures);
      } else {
        this.innerTransaction = inner;
      }
    }

    /**
     * Construct a new fee bump transaction builder.
     *
     * @param inner The inner transaction which will be fee bumped.
     */
    public Builder(Transaction inner) {
      this(AccountConverter.enableMuxed(), inner);
    }

    public FeeBumpTransaction.Builder setBaseFee(long baseFee) {
      if (this.baseFee != null) {
        throw new RuntimeException("base fee has been already set.");
      }

      if (baseFee < MIN_BASE_FEE) {
        throw new IllegalArgumentException(
            "baseFee cannot be smaller than the BASE_FEE (" + MIN_BASE_FEE + "): " + baseFee);
      }

      long innerBaseFee = this.innerTransaction.getFee();
      long numOperations = this.innerTransaction.getOperations().length;
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

      this.baseFee = maxFee;
      return this;
    }

    public FeeBumpTransaction.Builder setFeeAccount(@NonNull String feeAccount) {
      if (this.feeAccount != null) {
        throw new RuntimeException("fee account has been already been set.");
      }

      this.feeAccount = feeAccount;
      return this;
    }

    public FeeBumpTransaction build() {
      if (this.feeAccount == null) {
        throw new NullPointerException("fee account has to be set. you must call setFeeAccount().");
      }
      if (this.baseFee == null) {
        throw new NullPointerException("base fee has to be set. you must call setBaseFee().");
      }
      return new FeeBumpTransaction(
          this.accountConverter, this.feeAccount, this.baseFee, this.innerTransaction);
    }
  }
}
