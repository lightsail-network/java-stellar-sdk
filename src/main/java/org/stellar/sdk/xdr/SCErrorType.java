// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.stellar.sdk.Base64Factory;

/**
 * SCErrorType's original definition in the XDR file is:
 *
 * <pre>
 * enum SCErrorType
 * {
 *     SCE_CONTRACT = 0,          // Contract-specific, user-defined codes.
 *     SCE_WASM_VM = 1,           // Errors while interpreting WASM bytecode.
 *     SCE_CONTEXT = 2,           // Errors in the contract&#39;s host context.
 *     SCE_STORAGE = 3,           // Errors accessing host storage.
 *     SCE_OBJECT = 4,            // Errors working with host objects.
 *     SCE_CRYPTO = 5,            // Errors in cryptographic operations.
 *     SCE_EVENTS = 6,            // Errors while emitting events.
 *     SCE_BUDGET = 7,            // Errors relating to budget limits.
 *     SCE_VALUE = 8,             // Errors working with host values or SCVals.
 *     SCE_AUTH = 9               // Errors from the authentication subsystem.
 * };
 * </pre>
 */
public enum SCErrorType implements XdrElement {
  SCE_CONTRACT(0),
  SCE_WASM_VM(1),
  SCE_CONTEXT(2),
  SCE_STORAGE(3),
  SCE_OBJECT(4),
  SCE_CRYPTO(5),
  SCE_EVENTS(6),
  SCE_BUDGET(7),
  SCE_VALUE(8),
  SCE_AUTH(9),
  ;
  private int mValue;

  SCErrorType(int value) {
    mValue = value;
  }

  public int getValue() {
    return mValue;
  }

  public static SCErrorType decode(XdrDataInputStream stream) throws IOException {
    int value = stream.readInt();
    switch (value) {
      case 0:
        return SCE_CONTRACT;
      case 1:
        return SCE_WASM_VM;
      case 2:
        return SCE_CONTEXT;
      case 3:
        return SCE_STORAGE;
      case 4:
        return SCE_OBJECT;
      case 5:
        return SCE_CRYPTO;
      case 6:
        return SCE_EVENTS;
      case 7:
        return SCE_BUDGET;
      case 8:
        return SCE_VALUE;
      case 9:
        return SCE_AUTH;
      default:
        throw new RuntimeException("Unknown enum value: " + value);
    }
  }

  public static void encode(XdrDataOutputStream stream, SCErrorType value) throws IOException {
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

  public static SCErrorType fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SCErrorType fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
