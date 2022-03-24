package org.stellar.sdk;

public interface SequenceNumberStrategy {

    /**
     * callback to provide a custom routine to calculate the desired tx sequnce nuymber
     * @param account the source account of transaction.
     * @return the sequnce number to apply on transaction.
     */
    long getSequenceNumber(TransactionBuilderAccount account);

    void setSequenceNumber(long newSequenceNumber, TransactionBuilderAccount account);
}
