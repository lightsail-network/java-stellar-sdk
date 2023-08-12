package org.stellar.sdk.scval;

import org.stellar.sdk.xdr.SCSymbol;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;
import org.stellar.sdk.xdr.XdrString;

/** Represents an {@link SCVal} with the type of {@link SCValType#SCV_SYMBOL}. */
class ScvSymbol {
  private static final SCValType TYPE = SCValType.SCV_SYMBOL;

  static SCVal toSCVal(String value) {
    return new SCVal.Builder().discriminant(TYPE).sym(new SCSymbol(new XdrString(value))).build();
  }

  static String fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }
    return scVal.getSym().getSCSymbol().toString();
  }
}
