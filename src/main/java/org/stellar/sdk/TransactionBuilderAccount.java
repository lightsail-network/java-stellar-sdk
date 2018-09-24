package org.stellar.sdk;

/**
 * Specifies interface for Account object used in {@link org.stellar.sdk.Transaction.Builder}
 */
public interface TransactionBuilderAccount {
  /**
   * Returns keypair associated with this Account
   */
  KeyPair getKeypair();

  /**
   * Returns current sequence number ot this Account.
   */
  Long getSequenceNumber();

  /**
   * Returns sequence number incremented by one, but does not increment internal counter.
   */
  Long getIncrementedSequenceNumber();

  /**
   * Increments sequence number in this object by one.
   */
  void incrementSequenceNumber();
}
