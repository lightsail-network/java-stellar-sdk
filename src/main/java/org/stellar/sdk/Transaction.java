package org.stellar.sdk;

import com.google.common.io.BaseEncoding;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.stellar.sdk.xdr.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents <a href="https://www.stellar.org/developers/learn/concepts/transactions.html" target="_blank">Transaction</a> in Stellar network.
 */
public class Transaction {
  private static final int BASE_FEE = 100;

  private final int mFee;
  private final KeyPair mSourceAccount;
  private final long mSequenceNumber;
  private final Operation[] mOperations;
  private final Memo mMemo;
  private final TimeBounds mTimeBounds;
  private List<DecoratedSignature> mSignatures;

  Transaction(KeyPair sourceAccount, long sequenceNumber, Operation[] operations, Memo memo, TimeBounds timeBounds) {
    mSourceAccount = checkNotNull(sourceAccount, "sourceAccount cannot be null");
    mSequenceNumber = checkNotNull(sequenceNumber, "sequenceNumber cannot be null");
    mOperations = checkNotNull(operations, "operations cannot be null");
    checkArgument(operations.length > 0, "At least one operation required");

    mFee = operations.length * BASE_FEE;
    mSignatures = new ArrayList<DecoratedSignature>();
    mMemo = memo != null ? memo : Memo.none();
    mTimeBounds = timeBounds;
  }

  /**
   * Adds a new signature ed25519PublicKey to this transaction.
   * @param signer {@link KeyPair} object representing a signer
   */
  public void sign(KeyPair signer) {
    checkNotNull(signer, "signer cannot be null");
    byte[] txHash = this.hash();
    mSignatures.add(signer.signDecorated(txHash));
  }

  /**
   * Adds a new sha256Hash signature to this transaction by revealing preimage.
   * @param preimage the sha256 hash of preimage should be equal to signer hash
   */
  public void sign(byte[] preimage) {
    checkNotNull(preimage, "preimage cannot be null");
    org.stellar.sdk.xdr.Signature signature = new org.stellar.sdk.xdr.Signature();
    signature.setSignature(preimage);

    byte[] hash = Util.hash(preimage);
    byte[] signatureHintBytes = Arrays.copyOfRange(hash, hash.length - 4, hash.length);
    SignatureHint signatureHint = new SignatureHint();
    signatureHint.setSignatureHint(signatureHintBytes);

    DecoratedSignature decoratedSignature = new DecoratedSignature();
    decoratedSignature.setHint(signatureHint);
    decoratedSignature.setSignature(signature);

    mSignatures.add(decoratedSignature);
  }

  /**
   * Returns transaction hash.
   */
  public byte[] hash() {
    return Util.hash(this.signatureBase());
  }

  /**
   * Returns signature base.
   */
  public byte[] signatureBase() {
    if (Network.current() == null) {
      throw new NoNetworkSelectedException();
    }

    try {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      // Hashed NetworkID
      outputStream.write(Network.current().getNetworkId());
      // Envelope Type - 4 bytes
      outputStream.write(ByteBuffer.allocate(4).putInt(EnvelopeType.ENVELOPE_TYPE_TX.getValue()).array());
      // Transaction XDR bytes
      ByteArrayOutputStream txOutputStream = new ByteArrayOutputStream();
      XdrDataOutputStream xdrOutputStream = new XdrDataOutputStream(txOutputStream);
      org.stellar.sdk.xdr.Transaction.encode(xdrOutputStream, this.toXdr());
      outputStream.write(txOutputStream.toByteArray());

      return outputStream.toByteArray();
    } catch (IOException exception) {
      return null;
    }
  }

  public KeyPair getSourceAccount() {
    return mSourceAccount;
  }

  public long getSequenceNumber() {
    return mSequenceNumber;
  }

  public List<DecoratedSignature> getSignatures() {
    return mSignatures;
  }

  public Memo getMemo() {
    return mMemo;
  }
  
  /**
   * @return TimeBounds, or null (representing no time restrictions)
   */
  public TimeBounds getTimeBounds() {
    return mTimeBounds;
  }

  /**
   * Returns fee paid for transaction in stroops (1 stroop = 0.0000001 XLM).
   */
  public int getFee() {
    return mFee;
  }

  /**
   * Generates Transaction XDR object.
   */
  public org.stellar.sdk.xdr.Transaction toXdr() {
    // fee
    org.stellar.sdk.xdr.Uint32 fee = new org.stellar.sdk.xdr.Uint32();
    fee.setUint32(mFee);
    // sequenceNumber
    org.stellar.sdk.xdr.Uint64 sequenceNumberUint = new org.stellar.sdk.xdr.Uint64();
    sequenceNumberUint.setUint64(mSequenceNumber);
    org.stellar.sdk.xdr.SequenceNumber sequenceNumber = new org.stellar.sdk.xdr.SequenceNumber();
    sequenceNumber.setSequenceNumber(sequenceNumberUint);
    // sourceAccount
    org.stellar.sdk.xdr.AccountID sourceAccount = new org.stellar.sdk.xdr.AccountID();
    sourceAccount.setAccountID(mSourceAccount.getXdrPublicKey());
    // operations
    org.stellar.sdk.xdr.Operation[] operations = new org.stellar.sdk.xdr.Operation[mOperations.length];
    for (int i = 0; i < mOperations.length; i++) {
      operations[i] = mOperations[i].toXdr();
    }
    // ext
    org.stellar.sdk.xdr.Transaction.TransactionExt ext = new org.stellar.sdk.xdr.Transaction.TransactionExt();
    ext.setDiscriminant(0);

    org.stellar.sdk.xdr.Transaction transaction = new org.stellar.sdk.xdr.Transaction();
    transaction.setFee(fee);
    transaction.setSeqNum(sequenceNumber);
    transaction.setSourceAccount(sourceAccount);
    transaction.setOperations(operations);
    transaction.setMemo(mMemo.toXdr());
    transaction.setTimeBounds(mTimeBounds == null ? null : mTimeBounds.toXdr());
    transaction.setExt(ext);
    return transaction;
  }

  /**
   * Generates a Transaction from a Transaction XDR instance.
   */
  public static Transaction fromXdr(org.stellar.sdk.xdr.Transaction tx) {
    Account account = new Account(
        KeyPair.fromPublicKey(tx.getSourceAccount().getAccountID().getEd25519().getUint256()),
        tx.getSeqNum().getSequenceNumber().getUint64()
    );

    Operation[] operations = new Operation[tx.getOperations().length];
    for (int i = 0; i < operations.length; i++) {
      operations[i] = Operation.fromXdr(tx.getOperations()[i]);
    }

    return new Transaction(
            account.getKeypair(),
            account.getSequenceNumber(),
            operations,
            Memo.fromXdr(tx.getMemo()),
            TimeBounds.fromXdr(tx.getTimeBounds()));
  }

  /**
   * Generates TransactionEnvelope XDR object. Transaction need to have at least one signature.
   */
  public org.stellar.sdk.xdr.TransactionEnvelope toEnvelopeXdr() {
    if (mSignatures.size() == 0) {
      throw new NotEnoughSignaturesException("Transaction must be signed by at least one signer. Use transaction.sign().");
    }

    org.stellar.sdk.xdr.TransactionEnvelope xdr = new org.stellar.sdk.xdr.TransactionEnvelope();
    org.stellar.sdk.xdr.Transaction transaction = this.toXdr();
    xdr.setTx(transaction);

    DecoratedSignature[] signatures = new DecoratedSignature[mSignatures.size()];
    signatures = mSignatures.toArray(signatures);
    xdr.setSignatures(signatures);
    return xdr;
  }

  /**
   * Generates a Transaction from a TransactionEnvelope XDR instance.
   */
  public static Transaction fromEnvelopeXdr(TransactionEnvelope envelope) {
    Transaction tx = fromXdr(envelope.getTx());
    tx.mSignatures = new ArrayList<DecoratedSignature>();
    tx.mSignatures.addAll(Arrays.asList(envelope.getSignatures()));
    return tx;
  }

  /**
   * Returns base64-encoded TransactionEnvelope XDR object. Transaction need to have at least one signature.
   */
  public String toEnvelopeXdrBase64() {
    try {
      org.stellar.sdk.xdr.TransactionEnvelope envelope = this.toEnvelopeXdr();
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      XdrDataOutputStream xdrOutputStream = new XdrDataOutputStream(outputStream);
      org.stellar.sdk.xdr.TransactionEnvelope.encode(xdrOutputStream, envelope);

      BaseEncoding base64Encoding = BaseEncoding.base64();
      return base64Encoding.encode(outputStream.toByteArray());
    } catch (IOException e) {
      throw new AssertionError(e);
    }
  }

  /**
   * Generates a Transaction from a base64-encoded Transaction XDR instance.
   */
  public static Transaction fromEnvelopeXdrBase64(String base64) {
    try {
      BaseEncoding base64Encoding = BaseEncoding.base64();
      byte[] bytes = base64Encoding.decode(base64);
      XdrDataInputStream in = new XdrDataInputStream(new ByteArrayInputStream(bytes));
      TransactionEnvelope envelope = TransactionEnvelope.decode(in);
      return fromEnvelopeXdr(envelope);
    } catch (IOException e) {
      throw new AssertionError(e);
    }
  }

  /**
   * Builds a new Transaction object.
   */
  public static class Builder {
    private final TransactionBuilderAccount mSourceAccount;
    private Memo mMemo;
    private TimeBounds mTimeBounds;
    List<Operation> mOperations;

    /**
     * Construct a new transaction builder.
     * @param sourceAccount The source account for this transaction. This account is the account
     * who will use a sequence number. When build() is called, the account object's sequence number
     * will be incremented.
     */
    public Builder(TransactionBuilderAccount sourceAccount) {
      checkNotNull(sourceAccount, "sourceAccount cannot be null");
      mSourceAccount = sourceAccount;
      mOperations = Collections.synchronizedList(new ArrayList<Operation>());
    }

    public int getOperationsCount() {
      return mOperations.size();
    }

    /**
     * Adds a new <a href="https://www.stellar.org/developers/learn/concepts/list-of-operations.html" target="_blank">operation</a> to this transaction.
     * @param operation
     * @return Builder object so you can chain methods.
     * @see Operation
     */
    public Builder addOperation(Operation operation) {
      checkNotNull(operation, "operation cannot be null");
      mOperations.add(operation);
      return this;
    }

    /**
     * Adds a <a href="https://www.stellar.org/developers/learn/concepts/transactions.html" target="_blank">memo</a> to this transaction.
     * @param memo
     * @return Builder object so you can chain methods.
     * @see Memo
     */
    public Builder addMemo(Memo memo) {
      if (mMemo != null) {
        throw new RuntimeException("Memo has been already added.");
      }
      checkNotNull(memo, "memo cannot be null");
      mMemo = memo;
      return this;
    }
    
    /**
     * Adds a <a href="https://www.stellar.org/developers/learn/concepts/transactions.html" target="_blank">time-bounds</a> to this transaction.
     * @param timeBounds
     * @return Builder object so you can chain methods.
     * @see TimeBounds
     */
    public Builder addTimeBounds(TimeBounds timeBounds) {
      if (mTimeBounds != null) {
        throw new RuntimeException("TimeBounds has been already added.");
      }
      checkNotNull(timeBounds, "timeBounds cannot be null");
      mTimeBounds = timeBounds;
      return this;
    }

    /**
     * Builds a transaction. It will increment sequence number of the source account.
     */
    public Transaction build() {
      Operation[] operations = new Operation[mOperations.size()];
      operations = mOperations.toArray(operations);
      Transaction transaction = new Transaction(mSourceAccount.getKeypair(), mSourceAccount.getIncrementedSequenceNumber(), operations, mMemo, mTimeBounds);
      // Increment sequence number when there were no exceptions when creating a transaction
      mSourceAccount.incrementSequenceNumber();
      return transaction;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    Transaction that = (Transaction) o;

    return new EqualsBuilder()
            .append(mFee, that.mFee)
            .append(mSequenceNumber, that.mSequenceNumber)
            .append(mSourceAccount.getAccountId(), that.mSourceAccount.getAccountId())
            .append(mOperations, that.mOperations)
            .append(mMemo, that.mMemo)
            .append(mTimeBounds, that.mTimeBounds)
            .append(signatureStrings(mSignatures), signatureStrings(that.mSignatures))
            .isEquals();
  }

  // To assist with deep equality checking on XDR DecoratedSignatures
  private List<String> signatureStrings(List<DecoratedSignature> signatures) {
    BaseEncoding b64 = BaseEncoding.base64();
    List<String> signatureStrings = new ArrayList<String>(signatures.size());
    for (DecoratedSignature sig: signatures) {
      signatureStrings.add(String.format("hint=%s,sig=%s",
              b64.encode(sig.getHint().getSignatureHint()),
              b64.encode(sig.getSignature().getSignature())));
    }
    return signatureStrings;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
            .append(mFee)
            .append(mSourceAccount.getAccountId())
            .append(mSequenceNumber)
            .append(mOperations)
            .append(mMemo)
            .append(mTimeBounds)
            .append(signatureStrings(mSignatures))
            .toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
            .append("mFee", mFee)
            .append("mSourceAccount", mSourceAccount)
            .append("mSequenceNumber", mSequenceNumber)
            .append("mOperations", mOperations)
            .append("mMemo", mMemo)
            .append("mTimeBounds", mTimeBounds)
            .append("mSignatures", signatureStrings(mSignatures))
            .toString();
  }
}
