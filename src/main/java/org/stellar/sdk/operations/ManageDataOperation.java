package org.stellar.sdk.operations;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.Nullable;
import org.stellar.sdk.xdr.DataValue;
import org.stellar.sdk.xdr.ManageDataOp;
import org.stellar.sdk.xdr.OperationType;
import org.stellar.sdk.xdr.String64;
import org.stellar.sdk.xdr.XdrString;

/**
 * Represents <a
 * href="https://developers.stellar.org/docs/learn/fundamentals/transactions/list-of-operations#manage-data"
 * target="_blank">ManageData</a> operation.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
public class ManageDataOperation extends Operation {
  /** The name of the data value */
  @NonNull private final String name;

  /** Data value */
  @Nullable private final byte[] value;

  /**
   * Construct a new {@link ManageDataOperation} object from a {@link ManageDataOp} XDR object.
   *
   * @param op {@link ManageDataOp} XDR object
   * @return {@link ManageDataOperation} object
   */
  public static ManageDataOperation fromXdr(ManageDataOp op) {
    String name = op.getDataName().getString64().toString();
    byte[] value = null;
    if (op.getDataValue() != null) {
      value = op.getDataValue().getDataValue();
    }
    return ManageDataOperation.builder().name(name).value(value).build();
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody() {
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

  private static final class ManageDataOperationBuilderImpl
      extends ManageDataOperationBuilder<ManageDataOperation, ManageDataOperationBuilderImpl> {
    public ManageDataOperation build() {
      ManageDataOperation op = new ManageDataOperation(this);
      if (new XdrString(op.name).getBytes().length > 64) {
        throw new IllegalArgumentException("name cannot exceed 64 bytes");
      }
      if (op.value != null && op.value.length > 64) {
        throw new IllegalArgumentException("value cannot exceed 64 bytes");
      }
      return op;
    }
  }
}
