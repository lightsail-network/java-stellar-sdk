package org.stellar.sdk.scval;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

/** Represents an {@link SCVal} with the type of {@link SCValType#SCV_CONTRACT_INSTANCE}. */
@Value
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
class ScvLedgerKeyContractInstance extends Scv {
  private static final SCValType TYPE = SCValType.SCV_LEDGER_KEY_CONTRACT_INSTANCE;

  @Override
  public SCVal toSCVal() {
    return new SCVal.Builder().discriminant(TYPE).build();
  }

  public static ScvLedgerKeyContractInstance fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }
    return new ScvLedgerKeyContractInstance();
  }
}
