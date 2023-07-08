package org.stellar.sdk;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PriceTest {
  @Test
  public void testFromDouble() {
    PriceTestCase[] tests = {
      new PriceTestCase("0", new Price(0, 1)),
      new PriceTestCase("0.1", new Price(1, 10)),
      new PriceTestCase("0.01", new Price(1, 100)),
      new PriceTestCase("0.001", new Price(1, 1000)),
      new PriceTestCase("543.01793", new Price(54301793, 100000)),
      new PriceTestCase("319.69983", new Price(31969983, 100000)),
      new PriceTestCase("0.93", new Price(93, 100)),
      new PriceTestCase("0.5", new Price(1, 2)),
      new PriceTestCase("1.730", new Price(173, 100)),
      new PriceTestCase("0.85334384", new Price(5333399, 6250000)),
      new PriceTestCase("5.5", new Price(11, 2)),
      new PriceTestCase("2.72783", new Price(272783, 100000)),
      new PriceTestCase("638082.0", new Price(638082, 1)),
      new PriceTestCase("2.93850088", new Price(36731261, 12500000)),
      new PriceTestCase("58.04", new Price(1451, 25)),
      new PriceTestCase("41.265", new Price(8253, 200)),
      new PriceTestCase("5.1476", new Price(12869, 2500)),
      new PriceTestCase("95.14", new Price(4757, 50)),
      new PriceTestCase("0.74580", new Price(3729, 5000)),
      new PriceTestCase("4119.0", new Price(4119, 1)),
      new PriceTestCase("1073742464.5", new Price(1073742464, 1)),
      new PriceTestCase("1635962526.2", new Price(1635962526, 1)),
      new PriceTestCase("2147483647", new Price(2147483647, 1))
    };

    for (PriceTestCase test : tests) {
      Price price = Price.fromString(test.input);
      assertEquals(
          test.expectedPrice.getNumerator() + "/" + test.expectedPrice.getDenominator(),
          price.getNumerator() + "/" + price.getDenominator());
    }
  }

  private class PriceTestCase {
    public String input;
    public Price expectedPrice;

    public PriceTestCase(String input, Price expectedPrice) {
      this.input = input;
      this.expectedPrice = expectedPrice;
    }
  }
}
