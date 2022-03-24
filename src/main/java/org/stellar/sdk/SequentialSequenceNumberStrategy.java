package org.stellar.sdk;

public class SequentialSequenceNumberStrategy implements SequenceNumberStrategy{
    @Override
    public long getSequenceNumber(TransactionBuilderAccount account) {
        return account.getIncrementedSequenceNumber();
    }

    @Override
    public void setSequenceNumber(long newSequnceNumber, TransactionBuilderAccount account) {
        account.incrementSequenceNumber();
    }
}
