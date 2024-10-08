// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.stellar.sdk.Base64Factory;

/**
 * HostFunctionType's original definition in the XDR file is:
 *
 * <pre>
 * enum HostFunctionType
 * {
 *     HOST_FUNCTION_TYPE_INVOKE_CONTRACT = 0,
 *     HOST_FUNCTION_TYPE_CREATE_CONTRACT = 1,
 *     HOST_FUNCTION_TYPE_UPLOAD_CONTRACT_WASM = 2,
 *     HOST_FUNCTION_TYPE_CREATE_CONTRACT_V2 = 3
 * };
 * </pre>
 */
public enum HostFunctionType implements XdrElement {
  HOST_FUNCTION_TYPE_INVOKE_CONTRACT(0),
  HOST_FUNCTION_TYPE_CREATE_CONTRACT(1),
  HOST_FUNCTION_TYPE_UPLOAD_CONTRACT_WASM(2),
  HOST_FUNCTION_TYPE_CREATE_CONTRACT_V2(3);

  private final int value;

  HostFunctionType(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static HostFunctionType decode(XdrDataInputStream stream) throws IOException {
    int value = stream.readInt();
    switch (value) {
      case 0:
        return HOST_FUNCTION_TYPE_INVOKE_CONTRACT;
      case 1:
        return HOST_FUNCTION_TYPE_CREATE_CONTRACT;
      case 2:
        return HOST_FUNCTION_TYPE_UPLOAD_CONTRACT_WASM;
      case 3:
        return HOST_FUNCTION_TYPE_CREATE_CONTRACT_V2;
      default:
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    stream.writeInt(value);
  }

  public static HostFunctionType fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static HostFunctionType fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
