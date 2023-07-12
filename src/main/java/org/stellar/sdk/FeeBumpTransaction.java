package org.stellar.sdk;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import java.util.Arrays;
import org.stellar.sdk.xdr.DecoratedSignature;
import org.stellar.sdk.xdr.EnvelopeType;
import org.stellar.sdk.xdr.FeeBumpTransactionEnvelope;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.TransactionEnvelope;
import org.stellar.sdk.xdr.TransactionSignaturePayload;

/**
 * Represents <a href="https://github.com/stellar/stellar-protocol/blob/master/core/cap-0015.md"
 * target="_blank">Fee Bump Transaction</a> in Stellar network.
 */
public class FeeBumpTransaction extends AbstractTransaction {
  private final long mFee;
  private final String mFeeAccount;
  private final Transaction mInner;

  FeeBumpTransaction(
      AccountConverter accountConverter,
      String feeAccount,
      long fee,
      Transaction innerTransaction) {
    super(accountConverter, innerTransaction.getNetwork());
    this.mFeeAccount = checkNotNull(feeAccount, "feeAccount cannot be null");
    this.mInner = checkNotNull(innerTransaction, "innerTransaction cannot be null");
    this.mFee = fee;
  }

  public long getFee() {
    return mFee;
  }

  public String getFeeAccount() {
    return mFeeAccount;
  }

  public Transaction getInnerTransaction() {
    return mInner;
  }

  public static FeeBumpTransaction fromFeeBumpTransactionEnvelope(
      AccountConverter accountConverter, FeeBumpTransactionEnvelope envelope, Network network) {
    Transaction inner =
        Transaction.fromV1EnvelopeXdr(
            accountConverter, envelope.getTx().getInnerTx().getV1(), network);
    String feeAccount = accountConverter.decode(envelope.getTx().getFeeSource());

    long fee = envelope.getTx().getFee().getInt64();

    FeeBumpTransaction feeBump = new FeeBumpTransaction(accountConverter, feeAccount, fee, inner);
    feeBump.mSignatures.addAll(Arrays.asList(envelope.getSignatures()));

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
    xdrFee.setInt64(mFee);
    xdr.setFee(xdrFee);

    xdr.setFeeSource(accountConverter.encode(this.mFeeAccount));

    org.stellar.sdk.xdr.FeeBumpTransaction.FeeBumpTransactionInnerTx innerXDR =
        new org.stellar.sdk.xdr.FeeBumpTransaction.FeeBumpTransactionInnerTx();
    innerXDR.setDiscriminant(EnvelopeType.ENVELOPE_TYPE_TX);
    innerXDR.setV1(this.mInner.toEnvelopeXdr().getV1());
    xdr.setInnerTx(innerXDR);
    return xdr;
  }

  @Override
  public byte[] signatureBase() {
    TransactionSignaturePayload.TransactionSignaturePayloadTaggedTransaction taggedTransaction =
        new TransactionSignaturePayload.TransactionSignaturePayloadTaggedTransaction();
    taggedTransaction.setDiscriminant(EnvelopeType.ENVELOPE_TYPE_TX_FEE_BUMP);
    taggedTransaction.setFeeBump(this.toXdr());
    return getTransactionSignatureBase(taggedTransaction, mNetwork);
  }

  /** Generates TransactionEnvelope XDR object. */
  @Override
  public TransactionEnvelope toEnvelopeXdr() {
    TransactionEnvelope xdr = new TransactionEnvelope();
    FeeBumpTransactionEnvelope feeBumpEnvelope = new FeeBumpTransactionEnvelope();
    xdr.setDiscriminant(EnvelopeType.ENVELOPE_TYPE_TX_FEE_BUMP);

    feeBumpEnvelope.setTx(this.toXdr());

    DecoratedSignature[] signatures = new DecoratedSignature[mSignatures.size()];
    signatures = mSignatures.toArray(signatures);
    feeBumpEnvelope.setSignatures(signatures);

    xdr.setFeeBump(feeBumpEnvelope);
    return xdr;
  }

  /** Builds a new FeeBumpTransaction object. */
  public static class Builder {
    private final Transaction mInner;
    private Long mBaseFee;
    private String mFeeAccount;
    private final AccountConverter mAccountConverter;

    /**
     * Construct a new fee bump transaction builder.
     *
     * @param accountConverter The AccountConverter which will be used to encode the fee account.
     * @param inner The inner transaction which will be fee bumped. read-only, the
     */
    public Builder(AccountConverter accountConverter, final Transaction inner) {
      checkNotNull(inner, "inner cannot be null");
      EnvelopeType txType = inner.toEnvelopeXdr().getDiscriminant();
      this.mAccountConverter = checkNotNull(accountConverter, "accountConverter cannot be null");
      if (inner.toEnvelopeXdr().getDiscriminant() == EnvelopeType.ENVELOPE_TYPE_TX_V0) {
        this.mInner =
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

        this.mInner.mSignatures = Lists.newArrayList(inner.mSignatures);
      } else {
        this.mInner = inner;
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
      if (this.mBaseFee != null) {
        throw new RuntimeException("base fee has been already set.");
      }

      if (baseFee < MIN_BASE_FEE) {
        throw new IllegalArgumentException(
            "baseFee cannot be smaller than the BASE_FEE (" + MIN_BASE_FEE + "): " + baseFee);
      }

      long innerBaseFee = this.mInner.getFee();
      long numOperations = this.mInner.getOperations().length;
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

      this.mBaseFee = maxFee;
      return this;
    }

    public FeeBumpTransaction.Builder setFeeAccount(String feeAccount) {
      if (this.mFeeAccount != null) {
        throw new RuntimeException("fee account has been already been set.");
      }

      this.mFeeAccount = checkNotNull(feeAccount, "feeAccount cannot be null");
      return this;
    }

    public FeeBumpTransaction build() {
      return new FeeBumpTransaction(
          this.mAccountConverter,
          checkNotNull(
              this.mFeeAccount, "fee account has to be set. you must call setFeeAccount()."),
          checkNotNull(this.mBaseFee, "base fee has to be set. you must call setBaseFee()."),
          this.mInner);
    }
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(
        this.mFee, this.mInner, this.mNetwork, this.mFeeAccount, this.mSignatures);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof FeeBumpTransaction)) {
      return false;
    }

    FeeBumpTransaction other = (FeeBumpTransaction) object;
    return Objects.equal(this.mFee, other.mFee)
        && Objects.equal(this.mFeeAccount, other.mFeeAccount)
        && Objects.equal(this.mInner, other.mInner)
        && Objects.equal(this.mNetwork, other.mNetwork)
        && Objects.equal(this.mSignatures, other.mSignatures);
  }
}
