package org.stellar.sdk;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import java.util.Arrays;
import org.stellar.sdk.xdr.*;

/**
 * Represents <a href="https://developers.stellar.org/docs/start/list-of-operations/#manage-data"
 * target="_blank">ManageData</a> operation.
 *
 * @see <a href="https://developers.stellar.org/docs/start/list-of-operations/" target="_blank">List
 *     of Operations</a>
 */
public class ManageDataOperation extends Operation {
  private final String name;
  private final byte[] value;

  private ManageDataOperation(String name, byte[] value) {
    this.name = checkNotNull(name, "name cannot be null");
    this.value = value;

    if (new XdrString(this.name).getBytes().length > 64) {
      throw new IllegalArgumentException("name cannot exceed 64 bytes");
    }
  }

  /** The name of the data value */
  public String getName() {
    return name;
  }

  /** Data value */
  public byte[] getValue() {
    return value;
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

    private String mSourceAccount;

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
    public Builder(String name, byte[] value) {
      this.name = checkNotNull(name, "name cannot be null");
      this.value = value;
    }

    /**
     * Sets the source account for this operation.
     *
     * @param sourceAccount The operation's source account.
     * @return Builder object so you can chain methods.
     */
    public Builder setSourceAccount(String sourceAccount) {
      mSourceAccount = checkNotNull(sourceAccount, "sourceAccount cannot be null");
      return this;
    }

    /** Builds an operation */
    public ManageDataOperation build() {
      ManageDataOperation operation = new ManageDataOperation(name, value);
      if (mSourceAccount != null) {
        operation.setSourceAccount(mSourceAccount);
      }
      return operation;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.getSourceAccount(), this.name, Arrays.hashCode(this.value));
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof ManageDataOperation)) {
      return false;
    }

    ManageDataOperation other = (ManageDataOperation) object;
    return Objects.equal(this.getSourceAccount(), other.getSourceAccount())
        && Objects.equal(this.name, other.name)
        && Arrays.equals(this.value, other.value);
  }
}
