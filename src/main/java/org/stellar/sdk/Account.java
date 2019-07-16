package org.stellar.sdk;

import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents an account in Stellar network with it's sequence number.
 * Account object is required to build a {@link Transaction}.
 * @see org.stellar.sdk.Transaction.Builder
 */
public class Account implements TransactionBuilderAccount {
  private final String mAccountId;
  private Long mSequenceNumber;

  /**
   * Class constructor.
   * @param accountId ID associated with this Account
   * @param sequenceNumber Current sequence number of the account (can be obtained using java-stellar-sdk or horizon server)
   */
  public Account(String accountId, Long sequenceNumber) {
    mAccountId = checkNotNull(accountId, "accountId cannot be null");
    mSequenceNumber = checkNotNull(sequenceNumber, "sequenceNumber cannot be null");
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
  public Long getIncrementedSequenceNumber() {
    return new Long(mSequenceNumber + 1);
  }

  /**
   * Increments sequence number in this object by one.
   */
  public void incrementSequenceNumber() {
    mSequenceNumber++;
  }

  public int hashCode() {
    return Objects.hashCode(this.mAccountId, this.mSequenceNumber);
  }

  @Override
  public boolean equals(Object object) {
    if (object == null || !(object instanceof Account)) {
      return false;
    }

    Account other = (Account) object;
    return Objects.equal(this.mAccountId, other.mAccountId) &&
            Objects.equal(this.mSequenceNumber, other.mSequenceNumber);

  }
}
