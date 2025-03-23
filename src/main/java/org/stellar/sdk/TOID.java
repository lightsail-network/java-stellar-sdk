package org.stellar.sdk;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;

/**
 * ID represents the total order of Ledgers, Transactions and
 * Operations. This is an implementation of <a href="https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0035.md">SEP-0035</a>.
 */
@Getter
@EqualsAndHashCode
@ToString
public class TOID {
    private int ledgerSequence;
    private int transactionOrder;
    private int operationIndex;

    /**
     * Constructor for TOID.
     *
     * @param ledgerSequence   The ledger sequence the operation was validated in.
     * @param transactionOrder The order that the transaction was applied within the ledger where it was validated.
     *                         The application order value starts at 1. The maximum supported number of transactions
     *                         per operation is 1,048,575.
     * @param operationIndex   The index of the operation within that parent transaction. The operation index value
     *                         starts at 1. The maximum supported number of operations per transaction is 4095.
     */
    public TOID(int ledgerSequence, int transactionOrder, int operationIndex) {
        if (ledgerSequence < 0) {
            throw new IllegalArgumentException("Invalid ledger sequence, it must be between 0 and 2147483647.");
        }

        if (transactionOrder < 0 || transactionOrder > 0xFFFFF) {
            throw new IllegalArgumentException("Invalid transaction order, it must be between 0 and 1048575.");
        }

        if (operationIndex < 0 || operationIndex > 0xFFF) {
            throw new IllegalArgumentException("Invalid operation index, it must be between 0 and 4095.");
        }

        this.ledgerSequence = ledgerSequence;
        this.transactionOrder = transactionOrder;
        this.operationIndex = operationIndex;
    }

    /**
     * Converts the TOID to a signed 64-bit integer.
     *
     * @return The signed 64-bit integer representation of the TOID.
     */
    public long toInt64() {
        return ((long) ledgerSequence << 32)
                | ((long) transactionOrder << 12)
                | operationIndex;
    }

    /**
     * Converts a signed 64-bit integer to a TOID.
     *
     * @param value The signed 64-bit integer to convert.
     * @return A new TOID instance.
     */
    public static TOID fromInt64(long value) {
        if (value < 0) {
            throw new IllegalArgumentException(
                    "Invalid `value`, it must be between 0 and 9223372036854775807."
            );
        }
        return new TOID((int) (value >> 32), (int) ((value >> 12) & 0xFFFFF), (int) (value & 0xFFF));
    }

    /**
     * Increments the operation order by 1, rolling over to the next ledger if overflow occurs.
     * This allows queries to easily advance a cursor to the next operation.
     */
    public void incrementOperationIndex() {
        if (operationIndex == (1 << 12) - 1) {
            operationIndex = 0;
            ledgerSequence++;
        } else {
            operationIndex++;
        }
    }

    /**
     * Creates a new TOID that represents the ledger time **after** any
     * contents (e.g. transactions, operations) that occur within the specified ledger.
     *
     * @param ledgerSequence The ledger sequence.
     * @return A new TOID instance.
     */
    public static TOID afterLedger(int ledgerSequence) {
        return new TOID(ledgerSequence, (1 << 20) - 1, (1 << 12) - 1);
    }

    /**
     * The inclusive range representation between two
     * ledgers inclusive. The end value points at the end+1 ledger so when using
     * this range make sure < comparison is used for the upper bound.
     *
     * @param from The start ledger sequence.
     * @param to   The end ledger sequence.
     * @return The TOIDRange representing the inclusive range between two ledgers.
     */
    public static TOIDRange ledgerRangeInclusive(int from, int to) {
        if (from > to) {
            throw new IllegalArgumentException(
                    "Invalid `from` and `to` values, `from` must be less than or equal to `to`."
            );
        }
        if (from <= 0) {
            throw new IllegalArgumentException(
                    "Invalid `from` value, it must be greater than 0."
            );
        }

        long toidFrom = 0;
        if (from != 1) {
            toidFrom = new TOID(from, 0, 0).toInt64();
        }
        long toidTo = new TOID(to + 1, 0, 0).toInt64();
        return new TOIDRange(toidFrom, toidTo);
    }

    @Value
    public static class TOIDRange {
        long start;
        long end;
    }
}
