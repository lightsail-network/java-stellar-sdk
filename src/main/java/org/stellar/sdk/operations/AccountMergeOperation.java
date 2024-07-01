package org.stellar.sdk.operations;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.stellar.sdk.AccountConverter;
import org.stellar.sdk.xdr.MuxedAccount;
import org.stellar.sdk.xdr.Operation.OperationBody;
import org.stellar.sdk.xdr.OperationType;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/learn/fundamentals/transactions/list-of-operations#account-merge"
 * target="_blank">AccountMerge</a> operation.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public class AccountMergeOperation extends Operation {
  /** The account that receives the remaining XLM balance of the source account. */
  @NonNull private final String destination;

  /**
   * Construct a new {@link AccountMergeOperation} from the {@link AccountConverter} object and the
   * destination account.
   *
   * @param accountConverter AccountConverter
   * @param destination The account that receives the remaining XLM balance of the source account.
   * @return {@link AccountMergeOperation} object
   */
  public static AccountMergeOperation fromXdr(
      AccountConverter accountConverter, MuxedAccount destination) {
    return new AccountMergeOperation(accountConverter.decode(destination));
  }

  @Override
  OperationBody toOperationBody(AccountConverter accountConverter) {
    OperationBody body = new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDestination(accountConverter.encode(this.destination));
    body.setDiscriminant(OperationType.ACCOUNT_MERGE);
    return body;
  }
}
