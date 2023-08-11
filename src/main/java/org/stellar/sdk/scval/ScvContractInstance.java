package org.stellar.sdk.scval;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.stellar.sdk.xdr.SCContractInstance;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

/** Represents an {@link SCVal} with the type of {@link SCValType#SCV_CONTRACT_INSTANCE}. */
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
@Value
class ScvContractInstance extends Scv {
  private static final SCValType TYPE = SCValType.SCV_CONTRACT_INSTANCE;

  SCContractInstance value;

  @Override
  public SCVal toSCVal() {
    return new SCVal.Builder().discriminant(TYPE).instance(value).build();
  }

  public static ScvContractInstance fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }

    return new ScvContractInstance(scVal.getInstance());
  }
}
