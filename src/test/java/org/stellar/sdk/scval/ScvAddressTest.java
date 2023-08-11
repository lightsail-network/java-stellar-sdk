package org.stellar.sdk.scval;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stellar.sdk.Address;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

public class ScvAddressTest {
  @Test
  public void testScvAddress() {
    String publicKey = "GAHJJJKMOKYE4RVPZEWZTKH5FVI4PA3VL7GK2LFNUBSGBV6OJP7TQSLX";
    Address address = new Address(publicKey);
    ScvAddress scvAddress = new ScvAddress(address);
    SCVal scVal = scvAddress.toSCVal();

    assertEquals(scvAddress.getValue(), address);
    assertEquals(ScvAddress.fromSCVal(scVal), scvAddress);

    SCVal expectedScVal =
        new SCVal.Builder()
            .discriminant(SCValType.SCV_ADDRESS)
            .address(address.toSCAddress())
            .build();
    assertEquals(expectedScVal, scVal);

    assertEquals(Scv.toAddress(address), scVal);
    assertEquals(Scv.toAddress(publicKey), scVal);
    assertEquals(Scv.fromAddress(scVal), address);
  }
}
