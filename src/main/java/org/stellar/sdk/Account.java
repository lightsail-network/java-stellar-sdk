package org.stellar.sdk;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents an account in Stellar network with it's sequence number.
 * Account object is required to build a {@link Transaction}.
 * @see org.stellar.sdk.Transaction.Builder
 */
public class Account implements TransactionBuilderAccount {
  private final KeyPair mKeyPair;
  private Long mSequenceNumber;

  /**
   * Class constructor.
   * @param keypair KeyPair associated with this Account
   * @param sequenceNumber Current sequence number of the account (can be obtained using java-stellar-sdk or horizon server)
   */
  public Account(KeyPair keypair, Long sequenceNumber) {
    mKeyPair = checkNotNull(keypair, "keypair cannot be null");
    mSequenceNumber = checkNotNull(sequenceNumber, "sequenceNumber cannot be null");
  }

  /**
   * Returns keypair associated with this Account
   */
  public KeyPair getKeypair() {
    return mKeyPair;
  }

  /**
   * Returns current sequence number ot this Account.
   */
  public Long getSequenceNumber() {
    return mSequenceNumber;
  }

  /**
   * Increments sequence number in this object by one.
   */
  public void incrementSequenceNumber() {
    mSequenceNumber++;
  }
}
