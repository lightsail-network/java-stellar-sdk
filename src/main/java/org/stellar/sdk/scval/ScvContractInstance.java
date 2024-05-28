package org.stellar.sdk.scval;

import org.stellar.sdk.xdr.SCContractInstance;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

/** Represents an {@link SCVal} with the type of {@link SCValType#SCV_CONTRACT_INSTANCE}. */
class ScvContractInstance {
  private static final SCValType TYPE = SCValType.SCV_CONTRACT_INSTANCE;

  static SCVal toSCVal(SCContractInstance value) {
    return SCVal.builder().discriminant(TYPE).instance(value).build();
  }

  static SCContractInstance fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }

    return scVal.getInstance();
  }
}
