package org.stellar.sdk.scval;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stellar.sdk.xdr.SCSymbol;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;
import org.stellar.sdk.xdr.XdrString;

public class ScvSymbolTest {
  @Test
  public void testScvSymbol() {
    String value = "hello";

    SCVal expectedScVal =
        new SCVal.Builder()
            .discriminant(SCValType.SCV_SYMBOL)
            .sym(new SCSymbol(new XdrString(value)))
            .build();

    SCVal actualScVal = Scv.toSymbol(value);
    assertEquals(expectedScVal, actualScVal);
    assertEquals(value, Scv.fromSymbol(actualScVal));
  }
}
