package org.stellar.sdk;

import java.util.Objects;
import lombok.NonNull;

/**
 * Represents an account in Stellar network with it's sequence number. Account object is required to
 * build a {@link Transaction}.
 *
 * @see TransactionBuilder
 */
public class Account implements TransactionBuilderAccount {
  private final String mAccountId;
  private Long mSequenceNumber;

  /**
   * Class constructor.
   *
   * @param accountId ID associated with this Account
   * @param sequenceNumber Current sequence number of the account (can be obtained using
   *     java-stellar-sdk or horizon server)
   */
  public Account(@NonNull String accountId, @NonNull Long sequenceNumber) {
    mAccountId = accountId;
    mSequenceNumber = sequenceNumber;
  }

  @Override
  public String getAccountId() {
    return mAccountId;
  }

  @Override
  public KeyPair getKeyPair() {
    return KeyPair.fromAccountId(mAccountId);
  }

  @Override
  public Long getSequenceNumber() {
    return mSequenceNumber;
  }

  @Override
  public void setSequenceNumber(long seqNum) {
    mSequenceNumber = seqNum;
  }

  @Override
  public Long getIncrementedSequenceNumber() {
    return mSequenceNumber + 1;
  }

  /** Increments sequence number in this object by one. */
  public void incrementSequenceNumber() {
    mSequenceNumber++;
  }

  public int hashCode() {
    return Objects.hash(this.mAccountId, this.mSequenceNumber);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Account)) {
      return false;
    }

    Account other = (Account) object;
    return Objects.equals(this.mAccountId, other.mAccountId)
        && Objects.equals(this.mSequenceNumber, other.mSequenceNumber);
  }
}
