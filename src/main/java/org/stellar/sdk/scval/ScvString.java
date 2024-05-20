package org.stellar.sdk.scval;

import org.stellar.sdk.xdr.SCString;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;
import org.stellar.sdk.xdr.XdrString;

/** Represents an {@link SCVal} with the type of {@link SCValType#SCV_STRING}. */
class ScvString {
  private static final SCValType TYPE = SCValType.SCV_STRING;

  static SCVal toSCVal(byte[] value) {
    return SCVal.builder().discriminant(TYPE).str(new SCString(new XdrString(value))).build();
  }

  static byte[] fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }

    return scVal.getStr().getSCString().getBytes();
  }
}
