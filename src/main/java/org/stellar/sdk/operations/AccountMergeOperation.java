package org.stellar.sdk.operations;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.stellar.sdk.AccountConverter;
import org.stellar.sdk.xdr.Operation.OperationBody;
import org.stellar.sdk.xdr.OperationType;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/fundamentals-and-concepts/list-of-operations#account-merge"
 * target="_blank">AccountMerge</a> operation.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class AccountMergeOperation extends Operation {

  /** The account that receives the remaining XLM balance of the source account. */
  @NonNull private final String destination;

  @Override
  OperationBody toOperationBody(AccountConverter accountConverter) {
    OperationBody body = new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDestination(accountConverter.encode(this.destination));
    body.setDiscriminant(OperationType.ACCOUNT_MERGE);
    return body;
  }

  /**
   * Builds AccountMerge operation.
   *
   * @see AccountMergeOperation
   */
  public static class Builder {

    private final String destination;

    private String sourceAccount;

    Builder(AccountConverter accountConverter, OperationBody op) {
      destination = accountConverter.decode(op.getDestination());
    }

    /**
     * Creates a new AccountMerge builder.
     *
     * @param destination The account that receives the remaining XLM balance of the source account.
     */
    public Builder(String destination) {
      this.destination = destination;
    }

    /**
     * Set source account of this operation
     *
     * @param sourceAccount Source account
     * @return Builder object so you can chain methods.
     */
    public Builder setSourceAccount(String sourceAccount) {
      this.sourceAccount = sourceAccount;
      return this;
    }

    /** Builds an operation */
    public AccountMergeOperation build() {
      AccountMergeOperation operation = new AccountMergeOperation(destination);
      if (sourceAccount != null) {
        operation.setSourceAccount(sourceAccount);
      }
      return operation;
    }
  }
}
