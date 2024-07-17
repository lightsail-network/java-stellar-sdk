package org.stellar.sdk;

import static org.stellar.sdk.TransactionPreconditions.TIMEOUT_INFINITE;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.NonNull;
import org.stellar.sdk.operations.Operation;
import org.stellar.sdk.xdr.SorobanTransactionData;

/** Builds a new Transaction object. */
public class TransactionBuilder {
  private final TransactionBuilderAccount sourceAccount;
  private Memo memo;
  private final List<Operation> operations;
  private Long baseFee;
  private final Network network;
  @NonNull private TransactionPreconditions preconditions;
  private SorobanTransactionData sorobanData;
  private BigInteger txTimeout;

  /**
   * Construct a new transaction builder.
   *
   * @param sourceAccount the source account for this transaction. This account is the account who
   *     will use a sequence number. When build() is called, the account object's sequence number
   *     will be incremented.
   * @param network the testnet or pubnet network to use
   */
  public TransactionBuilder(
      @NonNull TransactionBuilderAccount sourceAccount, @NonNull Network network) {
    this.sourceAccount = sourceAccount;
    this.network = network;
    operations = new ArrayList<>();
    preconditions = TransactionPreconditions.builder().build();
  }

  public int getOperationsCount() {
    return operations.size();
  }

  /**
   * Adds a new <a href="https://developers.stellar.org/docs/start/list-of-operations/"
   * target="_blank">operation</a> to this transaction.
   *
   * @param operation the operation to add
   * @return Builder object so you can chain methods.
   * @see Operation
   */
  public TransactionBuilder addOperation(@NonNull Operation operation) {
    operations.add(operation);
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
  public TransactionBuilder addOperations(@NonNull Collection<Operation> operations) {
    this.operations.addAll(operations);
    return this;
  }

  /**
   * Adds preconditions. For details of all preconditions on transaction refer to <a
   * href="https://github.com/stellar/stellar-protocol/blob/master/core/cap-0021.md#specification">CAP-21</a>
   *
   * @param preconditions the tx PreConditions
   * @return updated Builder object
   */
  public TransactionBuilder addPreconditions(@NonNull TransactionPreconditions preconditions) {
    this.preconditions = preconditions;
    return this;
  }

  /**
   * Adds a <a href="https://developers.stellar.org/docs/glossary/transactions/#memo"
   * target="_blank">memo</a> to this transaction.
   *
   * @param memo memo to add
   * @return Builder object so you can chain methods.
   * @see Memo
   */
  public TransactionBuilder addMemo(@NonNull Memo memo) {
    if (this.memo != null) {
      throw new IllegalArgumentException("Memo has been already added.");
    }
    this.memo = memo;
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
   * TIMEOUT_INFINITE</code>. In general, you should set <code>TIMEOUT_INFINITE</code> only in smart
   * contracts. Please note that Horizon may still return <code>504 Gateway Timeout</code> error,
   * even for short timeouts. In such case you need to resubmit the same transaction again without
   * making any changes to receive a status. This method is using the machine system time (UTC),
   * make sure it is set correctly.
   *
   * @param timeout Timeout in seconds.
   * @return updated Builder
   * @see TimeBounds
   */
  public TransactionBuilder setTimeout(BigInteger timeout) {
    if (timeout.compareTo(BigInteger.ZERO) < 0) {
      throw new IllegalArgumentException("timeout cannot be negative");
    }

    txTimeout = timeout;
    return this;
  }

  /**
   * An alias for {@link #setTimeout(BigInteger)} with <code>timeout</code> in seconds.
   *
   * @param timeout Timeout in seconds.
   * @return updated Builder
   */
  public TransactionBuilder setTimeout(long timeout) {
    return setTimeout(BigInteger.valueOf(timeout));
  }

  public TransactionBuilder setBaseFee(long baseFee) {
    if (baseFee < AbstractTransaction.MIN_BASE_FEE) {
      throw new IllegalArgumentException(
          "baseFee cannot be smaller than the BASE_FEE ("
              + AbstractTransaction.MIN_BASE_FEE
              + "): "
              + baseFee);
    }

    this.baseFee = baseFee;
    return this;
  }

  /**
   * Builds a transaction and increments the sequence number on the source account after transaction
   * is constructed.
   */
  public Transaction build() {
    // ensure that the preconditions are valid
    if (preconditions.getTimeBounds() != null && txTimeout != null) {
      throw new IllegalArgumentException(
          "Can not set both TransactionPreconditions.timeBounds and timeout.");
    }

    if (txTimeout != null) {
      BigInteger maxTime =
          !TIMEOUT_INFINITE.equals(txTimeout)
              ? txTimeout.add(BigInteger.valueOf(System.currentTimeMillis() / 1000L))
              : TIMEOUT_INFINITE;
      preconditions =
          preconditions.toBuilder().timeBounds(new TimeBounds(BigInteger.ZERO, maxTime)).build();
    }

    preconditions.isValid();

    if (baseFee == null) {
      throw new IllegalArgumentException("baseFee has to be set. you must call setBaseFee().");
    }

    if (network == null) {
      throw new IllegalArgumentException("network has to be set. you must call setNetwork().");
    }

    long sequenceNumber = sourceAccount.getIncrementedSequenceNumber();
    Operation[] operations = new Operation[this.operations.size()];
    operations = this.operations.toArray(operations);

    long fee = operations.length * baseFee;
    if (sorobanData != null) {
      fee += sorobanData.getResourceFee().getInt64();
    }

    Transaction transaction =
        new Transaction(
            sourceAccount.getAccountId(),
            fee,
            sequenceNumber,
            operations,
            memo,
            preconditions,
            sorobanData,
            network);
    sourceAccount.setSequenceNumber(sequenceNumber);
    return transaction;
  }

  /**
   * Sets the transaction's internal Soroban transaction data (resources, footprint, etc.).
   *
   * <p>For non-contract(non-Soroban) transactions, this setting has no effect. In the case of
   * Soroban transactions, this is either an instance of {@link SorobanTransactionData} or a
   * base64-encoded string of said structure. This is usually obtained from the simulation response
   * based on a transaction with a Soroban operation (e.g. {@link
   * org.stellar.sdk.operations.InvokeHostFunctionOperation}, providing necessary resource and
   * storage footprint estimations for contract invocation.
   *
   * @param sorobanData Soroban data to set
   * @return Builder object so you can chain methods.
   */
  public TransactionBuilder setSorobanData(SorobanTransactionData sorobanData) {
    this.sorobanData = new SorobanDataBuilder(sorobanData).build();
    return this;
  }

  /**
   * Sets the transaction's internal Soroban transaction data (resources, footprint, etc.).
   *
   * <p>For non-contract(non-Soroban) transactions, this setting has no effect. In the case of
   * Soroban transactions, this is either an instance of {@link SorobanTransactionData} or a
   * base64-encoded string of said structure. This is usually obtained from the simulation response
   * based on a transaction with a Soroban operation (e.g. {@link
   * org.stellar.sdk.operations.InvokeHostFunctionOperation}, providing necessary resource and
   * storage footprint estimations for contract invocation.
   *
   * @param sorobanData Soroban data to set
   * @return Builder object so you can chain methods.
   */
  public TransactionBuilder setSorobanData(String sorobanData) {
    this.sorobanData = new SorobanDataBuilder(sorobanData).build();
    return this;
  }
}
