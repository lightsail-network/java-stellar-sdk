// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.stellar.sdk.Base64Factory;

/**
 * ContractEventType's original definition in the XDR file is:
 *
 * <pre>
 * enum ContractEventType
 * {
 *     SYSTEM = 0,
 *     CONTRACT = 1,
 *     DIAGNOSTIC = 2
 * };
 * </pre>
 */
public enum ContractEventType implements XdrElement {
  SYSTEM(0),
  CONTRACT(1),
  DIAGNOSTIC(2),
  ;
  private int mValue;

  ContractEventType(int value) {
    mValue = value;
  }

  public int getValue() {
    return mValue;
  }

  public static ContractEventType decode(XdrDataInputStream stream) throws IOException {
    int value = stream.readInt();
    switch (value) {
      case 0:
        return SYSTEM;
      case 1:
        return CONTRACT;
      case 2:
        return DIAGNOSTIC;
      default:
        throw new RuntimeException("Unknown enum value: " + value);
    }
  }

  public static void encode(XdrDataOutputStream stream, ContractEventType value)
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

  public static ContractEventType fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static ContractEventType fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
