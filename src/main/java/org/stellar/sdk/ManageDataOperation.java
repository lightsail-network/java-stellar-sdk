package org.stellar.sdk;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.stellar.sdk.xdr.*;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/fundamentals-and-concepts/list-of-operations#manage-data"
 * target="_blank">ManageData</a> operation.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class ManageDataOperation extends Operation {
  /** The name of the data value */
  private final String name;

  /** Data value */
  private final byte[] value;

  private ManageDataOperation(@NonNull String name, byte[] value) {
    this.name = name;
    this.value = value;

    if (new XdrString(this.name).getBytes().length > 64) {
      throw new IllegalArgumentException("name cannot exceed 64 bytes");
    }
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody(AccountConverter accountConverter) {
    ManageDataOp op = new ManageDataOp();
    String64 name = new String64();
    name.setString64(new XdrString(this.name));
    op.setDataName(name);

    if (value != null) {
      DataValue dataValue = new DataValue();
      dataValue.setDataValue(this.value);
      op.setDataValue(dataValue);
    }

    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.MANAGE_DATA);
    body.setManageDataOp(op);

    return body;
  }

  public static class Builder {

    private final String name;
    private final byte[] value;

    private String sourceAccount;

    /**
     * Construct a new ManageOffer builder from a ManageDataOp XDR.
     *
     * @param op {@link ManageDataOp}
     */
    Builder(ManageDataOp op) {
      name = op.getDataName().getString64().toString();
      if (op.getDataValue() != null) {
        value = op.getDataValue().getDataValue();
      } else {
        value = null;
      }
    }

    /**
     * Creates a new ManageData builder. If you want to delete data entry pass null as a <code>value
     * </code> param.
     *
     * @param name The name of data entry
     * @param value The value of data entry. <code>null</code>null will delete data entry.
     */
    public Builder(@NonNull String name, byte[] value) {
      this.name = name;
      this.value = value;
    }

    /**
     * Sets the source account for this operation.
     *
     * @param sourceAccount The operation's source account.
     * @return Builder object so you can chain methods.
     */
    public Builder setSourceAccount(@NonNull String sourceAccount) {
      this.sourceAccount = sourceAccount;
      return this;
    }

    /** Builds an operation */
    public ManageDataOperation build() {
      ManageDataOperation operation = new ManageDataOperation(name, value);
      if (sourceAccount != null) {
        operation.setSourceAccount(sourceAccount);
      }
      return operation;
    }
  }
}
