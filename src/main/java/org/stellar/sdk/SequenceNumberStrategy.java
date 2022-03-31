package org.stellar.sdk;

public interface SequenceNumberStrategy {

    /**
     * Derives the desired sequence number for a newly built transaction.
     *
     * @param sourceAccount the source account of transaction.
     * @return the sequence number to be applied on new transaction.
     */
    long getSequenceNumber(TransactionBuilderAccount sourceAccount);
}
