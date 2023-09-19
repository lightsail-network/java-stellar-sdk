package org.stellar.sdk.scval;

import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

/** Represents an {@link SCVal} with the type of {@link SCValType#SCV_CONTRACT_INSTANCE}. */
class ScvLedgerKeyContractInstance {
  private static final SCValType TYPE = SCValType.SCV_LEDGER_KEY_CONTRACT_INSTANCE;

  static SCVal toSCVal() {
    return new SCVal.Builder().discriminant(TYPE).build();
  }

  static void fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }
  }
}
