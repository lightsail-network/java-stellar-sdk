package org.stellar.sdk.scval;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.xdr.SCString;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;
import org.stellar.sdk.xdr.XdrString;

/** Represents an {@link SCVal} with the type of {@link SCValType#SCV_STRING}. */
@Value
@EqualsAndHashCode(callSuper = false)
class ScvString extends Scv {
  private static final SCValType TYPE = SCValType.SCV_STRING;

  byte[] value;

  public ScvString(String value) {
    this.value = value.getBytes();
  }

  public ScvString(byte[] value) {
    this.value = value;
  }

  @Override
  public SCVal toSCVal() {
    return new SCVal.Builder().discriminant(TYPE).str(new SCString(new XdrString(value))).build();
  }

  public static ScvString fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }

    return new ScvString(scVal.getStr().getSCString().getBytes());
  }
}
