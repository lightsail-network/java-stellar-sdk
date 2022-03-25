package org.stellar.sdk;

public interface SequenceNumberStrategy {

    /**
     * Derives the desired sequence number for a newly built transaction.
     *
     * @param account the source account of transaction.
     * @return the sequence number to be applied on new transaction.
     */
    long getSequenceNumber(TransactionBuilderAccount account);

    /**
     * Update a given in-memory account instance <b>after</b> a new transaction has been built.
     * The Implementation can determine what fields on the account to update,
     * however, the account sequence number is most likely account field to update,
     * since the new sequence number used on transaction is provided in parameters.
     *
     * Does not invoke any server updates, this just provides a way to update the in-memory
     * instance of an account.
     *
     * @param newSequenceNumber    the sequence number returned from <code>getSequenceNumber</code>
     * @param account              the account instance in memmory
     */
    void updateSourceAccount(long newSequenceNumber, TransactionBuilderAccount account);
}
