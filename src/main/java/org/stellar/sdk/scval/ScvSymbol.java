package org.stellar.sdk.scval;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.stellar.sdk.xdr.SCSymbol;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;
import org.stellar.sdk.xdr.XdrString;

/** Represents an {@link SCVal} with the type of {@link SCValType#SCV_SYMBOL}. */
@Value
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
class ScvSymbol extends Scv {
  private static final SCValType TYPE = SCValType.SCV_SYMBOL;

  String value;

  @Override
  public SCVal toSCVal() {
    return new SCVal.Builder().discriminant(TYPE).sym(new SCSymbol(new XdrString(value))).build();
  }

  public static ScvSymbol fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }
    return new ScvSymbol(scVal.getSym().getSCSymbol().toString());
  }
}
