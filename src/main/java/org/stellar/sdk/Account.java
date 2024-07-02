package org.stellar.sdk;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

/**
 * Represents an account in Stellar network with its sequence number. Account object is required to
 * build a {@link Transaction}.
 *
 * @see TransactionBuilder
 */
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Account implements TransactionBuilderAccount {

  /** The account ID. */
  @NonNull private final String accountId;

  /** The sequence number of the account. */
  @NonNull private Long sequenceNumber;

  @Override
  public KeyPair getKeyPair() {
    return KeyPair.fromAccountId(accountId);
  }

  @Override
  public void setSequenceNumber(long seqNum) {
    sequenceNumber = seqNum;
  }

  @Override
  public Long getIncrementedSequenceNumber() {
    return sequenceNumber + 1;
  }

  /** Increments sequence number in this object by one. */
  public void incrementSequenceNumber() {
    sequenceNumber++;
  }
}
