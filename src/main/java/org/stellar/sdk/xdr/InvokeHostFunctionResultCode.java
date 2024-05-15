// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.stellar.sdk.Base64Factory;

/**
 * InvokeHostFunctionResultCode's original definition in the XDR file is:
 *
 * <pre>
 * enum InvokeHostFunctionResultCode
 * {
 *     // codes considered as &quot;success&quot; for the operation
 *     INVOKE_HOST_FUNCTION_SUCCESS = 0,
 *
 *     // codes considered as &quot;failure&quot; for the operation
 *     INVOKE_HOST_FUNCTION_MALFORMED = -1,
 *     INVOKE_HOST_FUNCTION_TRAPPED = -2,
 *     INVOKE_HOST_FUNCTION_RESOURCE_LIMIT_EXCEEDED = -3,
 *     INVOKE_HOST_FUNCTION_ENTRY_ARCHIVED = -4,
 *     INVOKE_HOST_FUNCTION_INSUFFICIENT_REFUNDABLE_FEE = -5
 * };
 * </pre>
 */
public enum InvokeHostFunctionResultCode implements XdrElement {
  INVOKE_HOST_FUNCTION_SUCCESS(0),
  INVOKE_HOST_FUNCTION_MALFORMED(-1),
  INVOKE_HOST_FUNCTION_TRAPPED(-2),
  INVOKE_HOST_FUNCTION_RESOURCE_LIMIT_EXCEEDED(-3),
  INVOKE_HOST_FUNCTION_ENTRY_ARCHIVED(-4),
  INVOKE_HOST_FUNCTION_INSUFFICIENT_REFUNDABLE_FEE(-5),
  ;
  private int mValue;

  InvokeHostFunctionResultCode(int value) {
    mValue = value;
  }

  public int getValue() {
    return mValue;
  }

  public static InvokeHostFunctionResultCode decode(XdrDataInputStream stream) throws IOException {
    int value = stream.readInt();
    switch (value) {
      case 0:
        return INVOKE_HOST_FUNCTION_SUCCESS;
      case -1:
        return INVOKE_HOST_FUNCTION_MALFORMED;
      case -2:
        return INVOKE_HOST_FUNCTION_TRAPPED;
      case -3:
        return INVOKE_HOST_FUNCTION_RESOURCE_LIMIT_EXCEEDED;
      case -4:
        return INVOKE_HOST_FUNCTION_ENTRY_ARCHIVED;
      case -5:
        return INVOKE_HOST_FUNCTION_INSUFFICIENT_REFUNDABLE_FEE;
      default:
        throw new RuntimeException("Unknown enum value: " + value);
    }
  }

  public static void encode(XdrDataOutputStream stream, InvokeHostFunctionResultCode value)
      throws IOException {
    stream.writeInt(value.getValue());
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  @Override
  public String toXdrBase64() throws IOException {
    return Base64Factory.getInstance().encodeToString(toXdrByteArray());
  }

  @Override
  public byte[] toXdrByteArray() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    encode(xdrDataOutputStream);
    return byteArrayOutputStream.toByteArray();
  }

  public static InvokeHostFunctionResultCode fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static InvokeHostFunctionResultCode fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
