package org.stellar.sdk.scval;

import org.stellar.sdk.Address;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

/** Represents an {@link SCVal} with the type of {@link SCValType#SCV_ADDRESS}. */
class ScvAddress {
  private static final SCValType TYPE = SCValType.SCV_ADDRESS;

  static SCVal toSCVal(Address value) {
    return value.toSCVal();
  }

  static Address fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }

    return Address.fromSCVal(scVal);
  }
}
