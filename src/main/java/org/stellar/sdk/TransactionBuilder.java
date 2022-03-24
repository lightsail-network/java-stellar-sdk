package org.stellar.sdk;

import org.stellar.sdk.xdr.PreconditionsV2;
import org.stellar.sdk.xdr.TimePoint;
import org.stellar.sdk.xdr.Uint64;

import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;

/**
 * Builds a new Transaction object.
 */
public class TransactionBuilder {
    private final TransactionBuilderAccount mSourceAccount;
    private final AccountConverter mAccountConverter;
    private Memo mMemo;
    private List<Operation> mOperations;
    private Integer mBaseFee;
    private Network mNetwork;
    private SequenceNumberStrategy sequenceNumberStrategy;
    private PreconditionsV2 preconditions;

    public static final long TIMEOUT_INFINITE = 0;
    public static final long MAX_EXTRA_SIGNERS_COUNT = 2;

    /**
     * Construct a new transaction builder.
     *
     * @param accountConverter the account id formatter, choose to support muxed or not.
     * @param sourceAccount    the source account for this transaction. This account is the account
     *                         who will use a sequence number. When build() is called, the account object's sequence number
     *                         will be incremented.
     * @param network          the testnet or pubnet network to use
     */
    public TransactionBuilder(AccountConverter accountConverter, TransactionBuilderAccount sourceAccount, Network network) {
        mAccountConverter = checkNotNull(accountConverter, "accountConverter cannot be null");
        mSourceAccount = checkNotNull(sourceAccount, "sourceAccount cannot be null");
        mNetwork = checkNotNull(network, "Network cannot be null");
        mOperations = newArrayList();
        preconditions = new PreconditionsV2();
        sequenceNumberStrategy = new SequentialSequenceNumberStrategy();
    }

    /**
     * Construct a new transaction builder.
     *
     * @param sourceAccount the source account for this transaction. This account is the account
     *                      who will use a sequence number. When build() is called, the account object's sequence number
     *                      will be incremented.
     * @param network       the testnet or pubnet network to use
     */
    public TransactionBuilder(TransactionBuilderAccount sourceAccount, Network network) {
        this(AccountConverter.enableMuxed(), sourceAccount, network);
    }

    public int getOperationsCount() {
        return mOperations.size();
    }

    /**
     * Adds a new <a href="https://developers.stellar.org/docs/start/list-of-operations/" target="_blank">operation</a> to this transaction.
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
     * Adds <a href="https://developers.stellar.org/docs/start/list-of-operations/" target="_blank">operation</a> to this transaction.
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
     * Adds preconditions.
     * For details of all preconditions on transaction refer to <a href="https://github.com/stellar/stellar-protocol/blob/master/core/cap-0021.md#specification">CAP-21<a/>
     *
     * @param preconditions the tx PreConditions
     * @return updated Builder object
     */
    public TransactionBuilder addPreconditions(PreconditionsV2 preconditions) {
        checkNotNull(preconditions, "preconditions cannot be null");
        if (preconditions.getExtraSigners() != null && preconditions.getExtraSigners().length > MAX_EXTRA_SIGNERS_COUNT) {
            throw new FormatException("Invalid preconditions, too many extra signers, can only have up to " + MAX_EXTRA_SIGNERS_COUNT);
        }
        this.preconditions = preconditions;
        return this;
    }

    /**
     * Add a sequnce number resolution strategy. The Strategy is a callback that can determine the
     * actual sequence number applied to the tx being built.
     *
     * @param sequenceNumberStrategy the sequence number strategy that defines how to get a sequence number for the tx
     *                               and if/how to set source account sequence number after tx is built.
     * @return updated Builder object
     */
    public TransactionBuilder addSequenceNumberStrategy(SequenceNumberStrategy sequenceNumberStrategy) {
        this.sequenceNumberStrategy = sequenceNumberStrategy;
        return this;
    }

    /**
     * Adds a <a href="https://developers.stellar.org/docs/glossary/transactions/#memo" target="_blank">memo</a> to this transaction.
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
     * Adds a <a href="https://developers.stellar.org/docs/glossary/transactions/" target="_blank">time-bounds</a> to this transaction.
     *
     * @param timeBounds tx can be accepted within this time bound range
     * @return Builder object so you can chain methods.
     * @see TimeBounds
     * @deprecated this method will be removed in upcoming releases, use <code>addPreconditions()</code> instead for more control over preconditions.
     */
    public TransactionBuilder addTimeBounds(TimeBounds timeBounds) {
        checkNotNull(timeBounds, "timeBounds cannot be null");
        preconditions.setTimeBounds(timeBounds.toXdr());
        return this;
    }

    /**
     * Because of the distributed nature of the Stellar network it is possible that the status of your transaction
     * will be determined after a long time if the network is highly congested.
     * If you want to be sure to receive the status of the transaction within a given period you should set the
     * {@link TimeBounds} with <code>maxTime</code> on the transaction (this is what <code>setTimeout</code> does
     * internally; if there's <code>minTime</code> set but no <code>maxTime</code> it will be added).
     * Call to <code>Builder.setTimeout</code> is required if Transaction does not have <code>max_time</code> set.
     * If you don't want to set timeout, use <code>TIMEOUT_INFINITE</code>. In general you should set
     * <code>TIMEOUT_INFINITE</code> only in smart contracts.
     * Please note that Horizon may still return <code>504 Gateway Timeout</code> error, even for short timeouts.
     * In such case you need to resubmit the same transaction again without making any changes to receive a status.
     * This method is using the machine system time (UTC), make sure it is set correctly.
     *
     * @param timeout Timeout in seconds.
     * @return updated Builder
     * @see TimeBounds
     * @deprecated this method will be removed in upcoming releases, use <code>addPreconditions()</code> instead
     * for more control over preconditions.
     */
    public TransactionBuilder setTimeout(long timeout) {
        if (preconditions.getTimeBounds() != null && preconditions.getTimeBounds().getMaxTime().getTimePoint().getUint64() > 0) {
            throw new RuntimeException("TimeBounds.max_time has been already set - setting timeout would overwrite it.");
        }

        if (timeout < 0) {
            throw new RuntimeException("timeout cannot be negative");
        }

        if (timeout > 0) {
            long timeoutTimestamp = System.currentTimeMillis() / 1000L + timeout;
            if (preconditions.getTimeBounds() == null) {
                // no timebounds settings yet, so timeout is conveyed on the tx as a new timebounds in precondition
                // with a range of genesis to (current time + timeout interval)
                preconditions.setTimeBounds(buildTimeBounds(0L, timeoutTimestamp));
            } else {
                // an existing timebounds in precondition, so timeout is conveyed on the tx as the current timebounds
                // min time to a max time of (current time + timeout interval)
                preconditions.getTimeBounds().setMaxTime(new TimePoint(new Uint64(timeoutTimestamp)));
            }
        }

        if (timeout == 0) {
            if (preconditions.getTimeBounds() == null) {
                // no timebounds settings yet, so infinite timeout is conveyed on the tx in a new timebounds precondition
                // with a range of genesis to infinite
                preconditions.setTimeBounds(buildTimeBounds(0L, 0L));
            } else {
                // an existing timebounds in precondition, so timeout is conveyed on the tx as the current timebounds min time
                // to a max time of infinite
                preconditions.getTimeBounds().setMaxTime(new TimePoint(new Uint64(0L)));
            }
        }

        return this;
    }

    public TransactionBuilder setBaseFee(int baseFee) {
        if (baseFee < AbstractTransaction.MIN_BASE_FEE) {
            throw new IllegalArgumentException("baseFee cannot be smaller than the BASE_FEE (" + AbstractTransaction.MIN_BASE_FEE + "): " + baseFee);
        }

        this.mBaseFee = baseFee;
        return this;
    }

    /**
     * Builds a transaction. It will use the SequenceNumberStrategy to determine how to update source account
     * sequence number
     */
    public Transaction build() {
        if (preconditions.getTimeBounds() == null) {
            throw new FormatException("Timebounds is a mandatory precondition that must be set. Use TimeBounds(0,0) for infinite timeout");
        }

        if (mBaseFee == null) {
            throw new FormatException("mBaseFee has to be set. you must call setBaseFee().");
        }

        if (mNetwork == null) {
            throw new NoNetworkSelectedException();
        }

        long sequenceNumber = sequenceNumberStrategy.getSequenceNumber(mSourceAccount);

        Operation[] operations = new Operation[mOperations.size()];
        operations = mOperations.toArray(operations);
        Transaction transaction = new Transaction(
                mAccountConverter,
                mSourceAccount.getAccountId(),
                operations.length * mBaseFee,
                sequenceNumber,
                operations,
                mMemo,
                preconditions,
                mNetwork
        );
        // Increment sequence number when there were no exceptions when creating a transaction
        sequenceNumberStrategy.setSequenceNumber(sequenceNumber, mSourceAccount);
        return transaction;
    }

    public static org.stellar.sdk.xdr.TimeBounds buildTimeBounds(long minTime, long maxTime) {
        return new org.stellar.sdk.xdr.TimeBounds.Builder().minTime(new TimePoint(new Uint64(minTime)))
                .maxTime(new TimePoint(new Uint64(maxTime))).build();
    }
}
