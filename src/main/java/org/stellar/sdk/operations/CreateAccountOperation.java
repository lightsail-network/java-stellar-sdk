package org.stellar.sdk.operations;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.stellar.sdk.StrKey;
import org.stellar.sdk.xdr.CreateAccountOp;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.OperationType;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/learn/fundamentals/transactions/list-of-operations#create-account"
 * target="_blank">CreateAccount</a> operation.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public class CreateAccountOperation extends Operation {

  /** Account that is created and funded */
  @NonNull private final String destination;

  /** Amount of XLM to send to the newly created account. */
  @NonNull private final String startingBalance;

  /**
   * Construct a new {@link CreateAccountOperation} object from a {@link CreateAccountOp} XDR
   * object.
   *
   * @param op {@link CreateAccountOp} XDR object
   * @return {@link CreateAccountOperation} object
   */
  public static CreateAccountOperation fromXdr(CreateAccountOp op) {
    String destination = StrKey.encodeEd25519PublicKey(op.getDestination());
    String startingBalance = Operation.fromXdrAmount(op.getStartingBalance().getInt64());
    return new CreateAccountOperation(destination, startingBalance);
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody() {
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
}
