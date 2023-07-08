package org.stellar.sdk;

/** Specifies interface for Account object used in {@link TransactionBuilder} */
public interface TransactionBuilderAccount {
  /** Returns ID associated with this Account */
  String getAccountId();

  /** Returns keypair associated with this Account */
  KeyPair getKeyPair();

  /** Returns current sequence number ot this Account. */
  Long getSequenceNumber();

  /** Set current sequence number on this Account. */
  void setSequenceNumber(long seqNum);

  /** Returns sequence number incremented by one, but does not increment internal counter. */
  Long getIncrementedSequenceNumber();

  /** Increments sequence number in this object by one. */
  void incrementSequenceNumber();
}
