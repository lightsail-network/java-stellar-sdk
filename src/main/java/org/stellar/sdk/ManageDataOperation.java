package org.stellar.sdk;

import org.stellar.sdk.xdr.DataValue;
import org.stellar.sdk.xdr.ManageDataOp;
import org.stellar.sdk.xdr.OperationType;
import org.stellar.sdk.xdr.String64;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents <a href="https://www.stellar.org/developers/learn/concepts/list-of-operations.html#manage-data" target="_blank">ManageData</a> operation.
 * @see <a href="https://www.stellar.org/developers/learn/concepts/list-of-operations.html" target="_blank">List of Operations</a>
 */
public class ManageDataOperation extends Operation {
  private final String name;
  private final byte[] value;

  private ManageDataOperation(String name, byte[] value) {
    this.name = checkNotNull(name, "name cannot be null");
    this.value = value;
  }

  /**
   * The name of the data value
   */
  public String getName() {
    return name;
  }

  /**
   * Data value
   */
  public byte[] getValue() {
    return value;
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody() {
    ManageDataOp op = new ManageDataOp();
    String64 name = new String64();
    name.setString64(this.name);
    op.setDataName(name);

    if (value != null) {
      DataValue dataValue = new DataValue();
      dataValue.setDataValue(this.value);
      op.setDataValue(dataValue);
    }

    org.stellar.sdk.xdr.Operation.OperationBody body = new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.MANAGE_DATA);
    body.setManageDataOp(op);

    return body;
  }

  public static class Builder {
    private final String name;
    private final byte[] value;

    private KeyPair mSourceAccount;

    /**
     * Construct a new ManageOffer builder from a ManageDataOp XDR.
     * @param op {@link ManageDataOp}
     */
    Builder(ManageDataOp op) {
      name = op.getDataName().getString64();
      if (op.getDataValue() != null) {
        value = op.getDataValue().getDataValue();
      } else {
        value = null;
      }
    }

    /**
     * Creates a new ManageData builder. If you want to delete data entry pass null as a <code>value</code> param.
     * @param name The name of data entry
     * @param value The value of data entry. <code>null</code>null will delete data entry.
     */
    public Builder(String name, byte[] value) {
      this.name = checkNotNull(name, "name cannot be null");
      this.value = value;
    }

    /**
     * Sets the source account for this operation.
     * @param sourceAccount The operation's source account.
     * @return Builder object so you can chain methods.
     */
    public Builder setSourceAccount(KeyPair sourceAccount) {
      mSourceAccount = checkNotNull(sourceAccount, "sourceAccount cannot be null");
      return this;
    }

    /**
     * Builds an operation
     */
    public ManageDataOperation build() {
      ManageDataOperation operation = new ManageDataOperation(name, value);
      if (mSourceAccount != null) {
        operation.setSourceAccount(mSourceAccount);
      }
      return operation;
    }
  }
}
