package org.stellar.sdk;

/**
 * Default transaction builder strategy implementation for obtaining the sequence number that a transaction will
 * take and how the transaction's source account sequence number will be updated after the builder finishes
 * building the transaction instance.
 *
 */
public class SequentialSequenceNumberStrategy implements SequenceNumberStrategy{
    @Override
    public long getSequenceNumber(TransactionBuilderAccount account) {
        return account.getIncrementedSequenceNumber();
    }

    @Override
    public void updateSourceAccount(long newSequenceNumber, TransactionBuilderAccount account) {
        account.setSequenceNumber(newSequenceNumber);
    }
}
