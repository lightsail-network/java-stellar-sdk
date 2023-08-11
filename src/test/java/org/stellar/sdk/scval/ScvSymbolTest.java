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
    ScvSymbol scvSymbol = new ScvSymbol(value);
    SCVal scVal = scvSymbol.toSCVal();

    assertEquals(scvSymbol.getValue(), value);
    assertEquals(ScvSymbol.fromSCVal(scVal), scvSymbol);

    SCVal expectedScVal =
        new SCVal.Builder()
            .discriminant(SCValType.SCV_SYMBOL)
            .sym(new SCSymbol(new XdrString(value)))
            .build();
    assertEquals(expectedScVal, scVal);

    assertEquals(Scv.toSymbol(value), scVal);
    assertEquals(Scv.fromSymbol(scVal), value);
  }
}
