// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.IOException;

// === xdr source ============================================================

//  enum SCAddressType
//  {
//      SC_ADDRESS_TYPE_ACCOUNT = 0,
//      SC_ADDRESS_TYPE_CONTRACT = 1
//  };

//  ===========================================================================
public enum SCAddressType implements XdrElement {
  SC_ADDRESS_TYPE_ACCOUNT(0),
  SC_ADDRESS_TYPE_CONTRACT(1),
  ;
  private int mValue;

  SCAddressType(int value) {
    mValue = value;
  }

  public int getValue() {
    return mValue;
  }

  public static SCAddressType decode(XdrDataInputStream stream) throws IOException {
    int value = stream.readInt();
    switch (value) {
      case 0:
        return SC_ADDRESS_TYPE_ACCOUNT;
      case 1:
        return SC_ADDRESS_TYPE_CONTRACT;
      default:
        throw new RuntimeException("Unknown enum value: " + value);
    }
  }

  public static void encode(XdrDataOutputStream stream, SCAddressType value) throws IOException {
    stream.writeInt(value.getValue());
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }
}