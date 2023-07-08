package org.stellar.sdk;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;
import static org.stellar.sdk.TransactionPreconditions.TIMEOUT_INFINITE;

import com.google.common.base.Function;
import java.util.Collection;
import java.util.List;
import org.stellar.sdk.TransactionPreconditions.TransactionPreconditionsBuilder;
import org.stellar.sdk.xdr.TimePoint;
import org.stellar.sdk.xdr.Uint64;

/** Builds a new Transaction object. */
public class TransactionBuilder {
  private final TransactionBuilderAccount mSourceAccount;
  private final AccountConverter mAccountConverter;
  private Memo mMemo;
  private List<Operation> mOperations;
  private Integer mBaseFee;
  private Network mNetwork;
  private TransactionPreconditions mPreconditions;

  /**
   * Construct a new transaction builder.
   *
   * @param accountConverter the account id formatter, choose to support muxed or not.
   * @param sourceAccount the source account for this transaction. This account is the account who
   *     will use a sequence number. When build() is called, the account object's sequence number
   *     will be incremented.
   * @param network the testnet or pubnet network to use
   */
  public TransactionBuilder(
      AccountConverter accountConverter, TransactionBuilderAccount sourceAccount, Network network) {
    mAccountConverter = checkNotNull(accountConverter, "accountConverter cannot be null");
    mSourceAccount = checkNotNull(sourceAccount, "sourceAccount cannot be null");
    mNetwork = checkNotNull(network, "Network cannot be null");
    mOperations = newArrayList();
    mPreconditions = TransactionPreconditions.builder().build();
  }

  /**
   * Construct a new transaction builder.
   *
   * @param sourceAccount the source account for this transaction. This account is the account who
   *     will use a sequence number. When build() is called, the account object's sequence number
   *     will be incremented.
   * @param network the testnet or pubnet network to use
   */
  public TransactionBuilder(TransactionBuilderAccount sourceAccount, Network network) {
    this(AccountConverter.enableMuxed(), sourceAccount, network);
  }

  public int getOperationsCount() {
    return mOperations.size();
  }

  /**
   * Adds a new <a href="https://developers.stellar.org/docs/start/list-of-operations/"
   * target="_blank">operation</a> to this transaction.
   *
   * @param operation
   * @return Builder object so you can chain methods.
   * @see Operation
   */
  public TransactionBuilder addOperation(Operation operation) {
    checkNotNull(operation, "operation cannot be null");
    mOperations.add(operation);
    return this;
  }

  /**
   * Adds <a href="https://developers.stellar.org/docs/start/list-of-operations/"
   * target="_blank">operation</a> to this transaction.
   *
   * @param operations list of operations
   * @return Builder object so you can chain methods.
   * @see Operation
   */
  public TransactionBuilder addOperations(Collection<Operation> operations) {
    checkNotNull(operations, "operations cannot be null");
    mOperations.addAll(operations);
    return this;
  }

  /**
   * Adds preconditions. For details of all preconditions on transaction refer to <a
   * href="https://github.com/stellar/stellar-protocol/blob/master/core/cap-0021.md#specification">CAP-21<a/>
   *
   * @param preconditions the tx PreConditions
   * @return updated Builder object
   */
  public TransactionBuilder addPreconditions(TransactionPreconditions preconditions) {
    checkNotNull(preconditions, "preconditions cannot be null");
    this.mPreconditions = preconditions;
    return this;
  }

  /**
   * Adds a <a href="https://developers.stellar.org/docs/glossary/transactions/#memo"
   * target="_blank">memo</a> to this transaction.
   *
   * @param memo
   * @return Builder object so you can chain methods.
   * @see Memo
   */
  public TransactionBuilder addMemo(Memo memo) {
    if (mMemo != null) {
      throw new RuntimeException("Memo has been already added.");
    }
    checkNotNull(memo, "memo cannot be null");
    mMemo = memo;
    return this;
  }

  /**
   * Adds a <a href="https://developers.stellar.org/docs/glossary/transactions/"
   * target="_blank">time-bounds</a> to this transaction.
   *
   * @param timeBounds tx can be accepted within this time bound range
   * @return Builder object so you can chain methods.
   * @see TimeBounds
   * @deprecated this method will be removed in upcoming releases, use <code>addPreconditions()
   *     </code> instead for more control over preconditions.
   */
  public TransactionBuilder addTimeBounds(TimeBounds timeBounds) {
    checkNotNull(timeBounds, "timeBounds cannot be null");
    if (mPreconditions.getTimeBounds() != null) {
      throw new RuntimeException("TimeBounds already set.");
    }

    mPreconditions = mPreconditions.toBuilder().timeBounds(timeBounds).build();
    return this;
  }

  /**
   * Because of the distributed nature of the Stellar network it is possible that the status of your
   * transaction will be determined after a long time if the network is highly congested. If you
   * want to be sure to receive the status of the transaction within a given period you should set
   * the {@link TimeBounds} with <code>maxTime</code> on the transaction (this is what <code>
   * setTimeout</code> does internally; if there's <code>minTime</code> set but no <code>maxTime
   * </code> it will be added). Call to <code>Builder.setTimeout</code> is required if Transaction
   * does not have <code>max_time</code> set. If you don't want to set timeout, use <code>
   * TIMEOUT_INFINITE</code>. In general you should set <code>TIMEOUT_INFINITE</code> only in smart
   * contracts. Please note that Horizon may still return <code>504 Gateway Timeout</code> error,
   * even for short timeouts. In such case you need to resubmit the same transaction again without
   * making any changes to receive a status. This method is using the machine system time (UTC),
   * make sure it is set correctly.
   *
   * @param timeout Timeout in seconds.
   * @return updated Builder
   * @see TimeBounds
   * @deprecated this method will be removed in upcoming releases, use <code>addPreconditions()
   *     </code> with TimeBound set instead for more control over preconditions.
   */
  public TransactionBuilder setTimeout(long timeout) {
    if (mPreconditions.getTimeBounds() != null
        && mPreconditions.getTimeBounds().getMaxTime() != TIMEOUT_INFINITE) {
      throw new RuntimeException(
          "TimeBounds.max_time has been already set - setting timeout would overwrite it.");
    }

    if (timeout < 0) {
      throw new RuntimeException("timeout cannot be negative");
    }

    long timeoutTimestamp =
        timeout != TIMEOUT_INFINITE
            ? System.currentTimeMillis() / 1000L + timeout
            : TIMEOUT_INFINITE;

    TransactionPreconditionsBuilder preconditionsBuilder = mPreconditions.toBuilder();
    if (mPreconditions.getTimeBounds() == null) {
      preconditionsBuilder.timeBounds(new TimeBounds(0, timeoutTimestamp));
    } else {
      preconditionsBuilder.timeBounds(
          new TimeBounds(mPreconditions.getTimeBounds().getMinTime(), timeoutTimestamp));
    }
    mPreconditions = preconditionsBuilder.build();
    return this;
  }

  public TransactionBuilder setBaseFee(int baseFee) {
    if (baseFee < AbstractTransaction.MIN_BASE_FEE) {
      throw new IllegalArgumentException(
          "baseFee cannot be smaller than the BASE_FEE ("
              + AbstractTransaction.MIN_BASE_FEE
              + "): "
              + baseFee);
    }

    this.mBaseFee = baseFee;
    return this;
  }

  /**
   * Builds a transaction and increments the sequence number on the source account after transaction
   * is constructed.
   */
  public Transaction build() {
    mPreconditions.isValid();

    if (mBaseFee == null) {
      throw new FormatException("mBaseFee has to be set. you must call setBaseFee().");
    }

    if (mNetwork == null) {
      throw new NoNetworkSelectedException();
    }

    long sequenceNumber = mSourceAccount.getIncrementedSequenceNumber();
    Operation[] operations = new Operation[mOperations.size()];
    operations = mOperations.toArray(operations);
    Transaction transaction =
        new Transaction(
            mAccountConverter,
            mSourceAccount.getAccountId(),
            operations.length * mBaseFee,
            sequenceNumber,
            operations,
            mMemo,
            mPreconditions,
            mNetwork);
    mSourceAccount.setSequenceNumber(sequenceNumber);
    return transaction;
  }

  /**
   * A default implementation of sequence number generation which will derive a sequence number
   * based on sourceAccount's current sequence number + 1.
   */
  public static Function<TransactionBuilderAccount, Long> IncrementedSequenceNumberFunc =
      new Function<TransactionBuilderAccount, Long>() {
        @Override
        public Long apply(TransactionBuilderAccount sourceAccount) {
          return sourceAccount.getIncrementedSequenceNumber();
        }
      };

  public static org.stellar.sdk.xdr.TimeBounds buildTimeBounds(long minTime, long maxTime) {
    return new org.stellar.sdk.xdr.TimeBounds.Builder()
        .minTime(new TimePoint(new Uint64(minTime)))
        .maxTime(new TimePoint(new Uint64(maxTime)))
        .build();
  }
}
