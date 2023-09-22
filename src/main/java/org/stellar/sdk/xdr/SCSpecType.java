// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.stellar.sdk.Base64;

// === xdr source ============================================================

//  enum SCSpecType
//  {
//      SC_SPEC_TYPE_VAL = 0,
//
//      // Types with no parameters.
//      SC_SPEC_TYPE_BOOL = 1,
//      SC_SPEC_TYPE_VOID = 2,
//      SC_SPEC_TYPE_ERROR = 3,
//      SC_SPEC_TYPE_U32 = 4,
//      SC_SPEC_TYPE_I32 = 5,
//      SC_SPEC_TYPE_U64 = 6,
//      SC_SPEC_TYPE_I64 = 7,
//      SC_SPEC_TYPE_TIMEPOINT = 8,
//      SC_SPEC_TYPE_DURATION = 9,
//      SC_SPEC_TYPE_U128 = 10,
//      SC_SPEC_TYPE_I128 = 11,
//      SC_SPEC_TYPE_U256 = 12,
//      SC_SPEC_TYPE_I256 = 13,
//      SC_SPEC_TYPE_BYTES = 14,
//      SC_SPEC_TYPE_STRING = 16,
//      SC_SPEC_TYPE_SYMBOL = 17,
//      SC_SPEC_TYPE_ADDRESS = 19,
//
//      // Types with parameters.
//      SC_SPEC_TYPE_OPTION = 1000,
//      SC_SPEC_TYPE_RESULT = 1001,
//      SC_SPEC_TYPE_VEC = 1002,
//      SC_SPEC_TYPE_MAP = 1004,
//      SC_SPEC_TYPE_TUPLE = 1005,
//      SC_SPEC_TYPE_BYTES_N = 1006,
//
//      // User defined types.
//      SC_SPEC_TYPE_UDT = 2000
//  };

//  ===========================================================================
public enum SCSpecType implements XdrElement {
  SC_SPEC_TYPE_VAL(0),
  SC_SPEC_TYPE_BOOL(1),
  SC_SPEC_TYPE_VOID(2),
  SC_SPEC_TYPE_ERROR(3),
  SC_SPEC_TYPE_U32(4),
  SC_SPEC_TYPE_I32(5),
  SC_SPEC_TYPE_U64(6),
  SC_SPEC_TYPE_I64(7),
  SC_SPEC_TYPE_TIMEPOINT(8),
  SC_SPEC_TYPE_DURATION(9),
  SC_SPEC_TYPE_U128(10),
  SC_SPEC_TYPE_I128(11),
  SC_SPEC_TYPE_U256(12),
  SC_SPEC_TYPE_I256(13),
  SC_SPEC_TYPE_BYTES(14),
  SC_SPEC_TYPE_STRING(16),
  SC_SPEC_TYPE_SYMBOL(17),
  SC_SPEC_TYPE_ADDRESS(19),
  SC_SPEC_TYPE_OPTION(1000),
  SC_SPEC_TYPE_RESULT(1001),
  SC_SPEC_TYPE_VEC(1002),
  SC_SPEC_TYPE_MAP(1004),
  SC_SPEC_TYPE_TUPLE(1005),
  SC_SPEC_TYPE_BYTES_N(1006),
  SC_SPEC_TYPE_UDT(2000),
  ;
  private int mValue;

  SCSpecType(int value) {
    mValue = value;
  }

  public int getValue() {
    return mValue;
  }

  public static SCSpecType decode(XdrDataInputStream stream) throws IOException {
    int value = stream.readInt();
    switch (value) {
      case 0:
        return SC_SPEC_TYPE_VAL;
      case 1:
        return SC_SPEC_TYPE_BOOL;
      case 2:
        return SC_SPEC_TYPE_VOID;
      case 3:
        return SC_SPEC_TYPE_ERROR;
      case 4:
        return SC_SPEC_TYPE_U32;
      case 5:
        return SC_SPEC_TYPE_I32;
      case 6:
        return SC_SPEC_TYPE_U64;
      case 7:
        return SC_SPEC_TYPE_I64;
      case 8:
        return SC_SPEC_TYPE_TIMEPOINT;
      case 9:
        return SC_SPEC_TYPE_DURATION;
      case 10:
        return SC_SPEC_TYPE_U128;
      case 11:
        return SC_SPEC_TYPE_I128;
      case 12:
        return SC_SPEC_TYPE_U256;
      case 13:
        return SC_SPEC_TYPE_I256;
      case 14:
        return SC_SPEC_TYPE_BYTES;
      case 16:
        return SC_SPEC_TYPE_STRING;
      case 17:
        return SC_SPEC_TYPE_SYMBOL;
      case 19:
        return SC_SPEC_TYPE_ADDRESS;
      case 1000:
        return SC_SPEC_TYPE_OPTION;
      case 1001:
        return SC_SPEC_TYPE_RESULT;
      case 1002:
        return SC_SPEC_TYPE_VEC;
      case 1004:
        return SC_SPEC_TYPE_MAP;
      case 1005:
        return SC_SPEC_TYPE_TUPLE;
      case 1006:
        return SC_SPEC_TYPE_BYTES_N;
      case 2000:
        return SC_SPEC_TYPE_UDT;
      default:
        throw new RuntimeException("Unknown enum value: " + value);
    }
  }

  public static void encode(XdrDataOutputStream stream, SCSpecType value) throws IOException {
    stream.writeInt(value.getValue());
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  @Override
  public String toXdrBase64() throws IOException {
    return Base64.encodeToString(toXdrByteArray());
  }

  @Override
  public byte[] toXdrByteArray() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    encode(xdrDataOutputStream);
    return byteArrayOutputStream.toByteArray();
  }

  public static SCSpecType fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SCSpecType fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
