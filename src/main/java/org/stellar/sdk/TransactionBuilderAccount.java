package org.stellar.sdk;

/** Specifies interface for Account object used in {@link TransactionBuilder} */
public interface TransactionBuilderAccount {
  /**
   * Returns ID associated with this Account.
   *
   * @return the account ID as a Stellar public key string (starting with {@code G})
   */
  String getAccountId();

  /**
   * Returns keypair associated with this Account.
   *
   * @return the {@link KeyPair} for this account
   */
  KeyPair getKeyPair();

  /**
   * Returns current sequence number of this Account.
   *
   * @return the current sequence number
   */
  Long getSequenceNumber();

  /**
   * Sets current sequence number on this Account.
   *
   * @param seqNum the new sequence number to set
   */
  void setSequenceNumber(long seqNum);

  /**
   * Returns sequence number incremented by one, but does not increment internal counter.
   *
   * @return the sequence number incremented by one
   */
  Long getIncrementedSequenceNumber();

  /** Increments sequence number in this object by one. */
  void incrementSequenceNumber();
}
