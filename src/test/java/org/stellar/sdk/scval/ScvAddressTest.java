package org.stellar.sdk.scval;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stellar.sdk.Address;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

public class ScvAddressTest {
  @Test
  public void testScvAddressFromAddress() {
    String publicKey = "GAHJJJKMOKYE4RVPZEWZTKH5FVI4PA3VL7GK2LFNUBSGBV6OJP7TQSLX";
    Address address = new Address(publicKey);

    SCVal expectedScVal =
        new SCVal.Builder()
            .discriminant(SCValType.SCV_ADDRESS)
            .address(address.toSCAddress())
            .build();

    SCVal actualScVal = Scv.toAddress(address);
    assertEquals(expectedScVal, actualScVal);
    assertEquals(address, Scv.fromAddress(actualScVal));
  }

  @Test
  public void testScvAddressFromKeyString() {
    String publicKey = "GAHJJJKMOKYE4RVPZEWZTKH5FVI4PA3VL7GK2LFNUBSGBV6OJP7TQSLX";
    Address address = new Address(publicKey);

    SCVal expectedScVal =
        new SCVal.Builder()
            .discriminant(SCValType.SCV_ADDRESS)
            .address(address.toSCAddress())
            .build();

    SCVal actualScVal = Scv.toAddress(publicKey);
    assertEquals(expectedScVal, actualScVal);
    assertEquals(address, Scv.fromAddress(actualScVal));
  }
}
