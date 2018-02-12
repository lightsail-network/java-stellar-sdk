package org.stellar.sdk;

import com.google.common.io.BaseEncoding;
import org.stellar.sdk.xdr.DecoratedSignature;
import org.stellar.sdk.xdr.EnvelopeType;
import org.stellar.sdk.xdr.SignatureHint;
import org.stellar.sdk.xdr.TransactionEnvelope;
import org.stellar.sdk.xdr.XdrDataInputStream;
import org.stellar.sdk.xdr.XdrDataOutputStream;

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
  private final int BASE_FEE = 100;

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
   * Returns base64-encoded TransactionEnvelope XDR object. Transaction need to have at least one signature.
   */
  public String toEnvelopeXdrBase64() {
    try {
      TransactionEnvelope envelope = this.toEnvelopeXdr();
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      XdrDataOutputStream xdrOutputStream = new XdrDataOutputStream(outputStream);
      TransactionEnvelope.encode(xdrOutputStream, envelope);
      BaseEncoding base64Encoding = BaseEncoding.base64();
      return base64Encoding.encode(outputStream.toByteArray());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Decodes a base64-encoded XDR {@link TransactionEnvelope}.
   *
   * @bug Does not work on Android, as it performs a call to the method
   *      decode() of class org.apache.commons.codec.binary.Base64, which
   *      conflicts with a legacy Apache class included in Android.
   *      To decode a base64-encoded {@link TransactionEnvelope} on Android,
   *      use {@code android.util.Base64.decode()}, then pass the result to
   *      the method {@link #decodeXdrEnvelope(byte[])}.
   *
   * @param encodedXdrTxEnvelope the base64-encoding of an XDR-encoded {@link TransactionEnvelope}.
   * @return the decoded {@link TransactionEnvelope}
   */
  public static TransactionEnvelope decodeBase64XdrEnvelope(String encodedXdrTxEnvelope) {
    checkNotNull(encodedXdrTxEnvelope, "Transaction envelope cannot be null");
    Base64 base64Codec = new Base64();
    byte[] xdrTxEnvelope = base64Codec.decode(encodedXdrTxEnvelope);
    return decodeXdrEnvelope(xdrTxEnvelope);
  }

  /**
   * Decodes an XDR {@link TransactionEnvelope}.
   * @param xdrTxEnvelope the XDR encoded {@link TransactionEnvelope}
   * @return the decoded {@link TransactionEnvelope}
   */
  public static TransactionEnvelope decodeXdrEnvelope(byte[] xdrTxEnvelope) {
    ByteArrayInputStream inputStream = new ByteArrayInputStream(xdrTxEnvelope);
    XdrDataInputStream xdrInputStream = new XdrDataInputStream(inputStream);
    try {
      return TransactionEnvelope.decode(xdrInputStream);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  /**
   * Constructs a Transaction object from a given {@link TransactionEnvelope} object.
   * @param txEnv
   * @return the new Transaction object
   */
  public static Transaction fromEnvelope(org.stellar.sdk.xdr.TransactionEnvelope txEnv) {
    checkNotNull(txEnv, "Transaction envelope cannot be null");
    org.stellar.sdk.xdr.Transaction xdrTx = txEnv.getTx();
    KeyPair sourceAccount = KeyPair.fromXdrPublicKey(xdrTx.getSourceAccount().getAccountID());
    long sequenceNumber = xdrTx.getSeqNum().getSequenceNumber().getUint64();
    org.stellar.sdk.xdr.Operation[] xdrOps = xdrTx.getOperations();
    Operation[] operations = new Operation[xdrOps.length];
    for (int i = 0; i < xdrOps.length; i++) {
      operations[i] = Operation.fromXdr(xdrOps[i]);
    }
    Memo memo = Memo.fromXdr(xdrTx.getMemo());
    org.stellar.sdk.xdr.TimeBounds xdrTimeBounds = xdrTx.getTimeBounds();
    TimeBounds timeBounds = null;
    if (xdrTimeBounds != null) {
      timeBounds = new TimeBounds(xdrTimeBounds.getMinTime().getUint64(),
                                  xdrTimeBounds.getMaxTime().getUint64());
    }
    Transaction tx = new Transaction(sourceAccount, sequenceNumber, operations, memo, timeBounds);
    DecoratedSignature[] signatures = txEnv.getSignatures();
    for (int i = 0; i < signatures.length; i++) {
      tx.mSignatures.add(signatures[i]);
    }
    return tx;
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
}
