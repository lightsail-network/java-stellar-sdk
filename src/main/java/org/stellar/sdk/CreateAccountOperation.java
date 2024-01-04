package org.stellar.sdk;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.stellar.sdk.xdr.CreateAccountOp;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.OperationType;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/fundamentals-and-concepts/list-of-operations#create-account"
 * target="_blank">CreateAccount</a> operation.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class CreateAccountOperation extends Operation {

  /** Account that is created and funded */
  @NonNull private final String destination;

  /** Amount of XLM to send to the newly created account. */
  @NonNull private final String startingBalance;

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody(AccountConverter accountConverter) {
    CreateAccountOp op = new CreateAccountOp();
    op.setDestination(StrKey.encodeToXDRAccountId(this.destination));
    Int64 startingBalance = new Int64();
    startingBalance.setInt64(Operation.toXdrAmount(this.startingBalance));
    op.setStartingBalance(startingBalance);

    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.CREATE_ACCOUNT);
    body.setCreateAccountOp(op);
    return body;
  }

  /**
   * Builds CreateAccount operation.
   *
   * @see CreateAccountOperation
   */
  public static class Builder {

    private final String destination;
    private final String startingBalance;

    private String sourceAccount;

    /**
     * Construct a new CreateAccount builder from a CreateAccountOp XDR.
     *
     * @param op {@link CreateAccountOp}
     */
    Builder(CreateAccountOp op) {
      destination = StrKey.encodeEd25519PublicKey(op.getDestination());
      startingBalance = Operation.fromXdrAmount(op.getStartingBalance().getInt64());
    }

    /**
     * Creates a new CreateAccount builder.
     *
     * @param destination The destination keypair (uses only the public key).
     * @param startingBalance The initial balance to start with in lumens.
     * @throws ArithmeticException when startingBalance has more than 7 decimal places.
     */
    public Builder(String destination, String startingBalance) {
      this.destination = destination;
      this.startingBalance = startingBalance;
    }

    /**
     * Sets the source account for this operation.
     *
     * @param account The operation's source account.
     * @return Builder object so you can chain methods.
     */
    public Builder setSourceAccount(String account) {
      sourceAccount = account;
      return this;
    }

    /** Builds an operation */
    public CreateAccountOperation build() {
      CreateAccountOperation operation = new CreateAccountOperation(destination, startingBalance);
      if (sourceAccount != null) {
        operation.setSourceAccount(sourceAccount);
      }
      return operation;
    }
  }
}
