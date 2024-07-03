package org.stellar.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import lombok.Value;
import org.junit.Test;

public class LiquidityPoolParametersTest {
  @Test
  public void testLiquidityPool() throws IOException {
    Asset assetA = Asset.create("ARST:GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO");
    Asset assetB = Asset.create("USD:GCEZWKCA5VLDNRLN3RPRJMRZOX3Z6G5CHCGSNFHEYVXM3XOJMDS674JZ");
    LiquidityPoolParameters liquidityPoolParameters = new LiquidityPoolParameters(assetA, assetB);
    String expectedXdr =
        "AAAAAAAAAAFBUlNUAAAAAH8wYjTJienWf2nf2TEZi2APPWzmtkwiQHAftisIgyuHAAAAAVVTRAAAAAAAiZsoQO1WNsVt3F8Usjl1958bojiNJpTkxW7N3clg5e8AAAAe";
    assertEquals(liquidityPoolParameters.toXdr().toXdrBase64(), expectedXdr);
    org.stellar.sdk.xdr.LiquidityPoolParameters xdr = liquidityPoolParameters.toXdr();
    LiquidityPoolParameters parsedLiquidityPoolParameters = LiquidityPoolParameters.fromXdr(xdr);
    assertEquals(liquidityPoolParameters, parsedLiquidityPoolParameters);
    assertEquals(
        liquidityPoolParameters.getLiquidityPoolId().getPoolId(),
        "dd7b1ab831c273310ddbec6f97870aa83c2fbd78ce22aded37ecbf4f3380fac7");
    assertEquals(liquidityPoolParameters.getAssetA(), assetA);
    assertEquals(liquidityPoolParameters.getAssetB(), assetB);
    assertEquals(liquidityPoolParameters.getFee(), LiquidityPoolParameters.FEE);
    assertEquals(liquidityPoolParameters.getFee(), 30);
  }

  @Test
  public void testLexicographicOrderAssetTypes() {
    Asset anum4 = Asset.create("USD:GCEZWKCA5VLDNRLN3RPRJMRZOX3Z6G5CHCGSNFHEYVXM3XOJMDS674JZ");
    Asset anum12 = Asset.create("BANANA:GCEZWKCA5VLDNRLN3RPRJMRZOX3Z6G5CHCGSNFHEYVXM3XOJMDS674JZ");
    Asset assetNative = new AssetTypeNative();
    OrderTestCase[] testCases = {
      new OrderTestCase(assetNative, assetNative, false),
      new OrderTestCase(assetNative, anum4, true),
      new OrderTestCase(assetNative, anum12, true),
      new OrderTestCase(anum4, assetNative, false),
      new OrderTestCase(anum4, anum4, false),
      new OrderTestCase(anum4, anum12, true),
      new OrderTestCase(anum12, assetNative, false),
      new OrderTestCase(anum12, anum4, false),
      new OrderTestCase(anum12, anum12, false),
    };
    for (OrderTestCase testCase : testCases) {
      try {
        new LiquidityPoolParameters(testCase.assetA, testCase.assetB);
        if (!testCase.valid) {
          fail(
              "Expected exception for invalid asset order: "
                  + testCase.assetA
                  + ", "
                  + testCase.assetB);
        }
      } catch (IllegalArgumentException e) {
        if (testCase.valid) {
          fail(
              "Unexpected exception for valid asset order: "
                  + testCase.assetA
                  + ", "
                  + testCase.assetB);
        }
      }
    }
  }

  @Test
  public void testLexicographicOrderAssetCode() {
    Asset assetA = Asset.create("ARST:GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO");
    Asset assetB = Asset.create("USDX:GCEZWKCA5VLDNRLN3RPRJMRZOX3Z6G5CHCGSNFHEYVXM3XOJMDS674JZ");
    OrderTestCase[] testCases = {
      new OrderTestCase(assetA, assetA, false),
      new OrderTestCase(assetA, assetB, true),
      new OrderTestCase(assetB, assetA, false),
      new OrderTestCase(assetB, assetB, false),
    };
    for (OrderTestCase testCase : testCases) {
      try {
        new LiquidityPoolParameters(testCase.assetA, testCase.assetB);
        if (!testCase.valid) {
          fail(
              "Expected exception for invalid asset code order: "
                  + testCase.assetA
                  + ", "
                  + testCase.assetB);
        }
      } catch (IllegalArgumentException e) {
        if (testCase.valid) {
          fail(
              "Unexpected exception for valid asset code order: "
                  + testCase.assetA
                  + ", "
                  + testCase.assetB);
        }
      }
    }
  }

  @Test
  public void testLexicographicOrderAssetIssuer() {
    Asset assetA = Asset.create("ARST:GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO");
    Asset assetB = Asset.create("ARST:GCEZWKCA5VLDNRLN3RPRJMRZOX3Z6G5CHCGSNFHEYVXM3XOJMDS674JZ");
    OrderTestCase[] testCases = {
      new OrderTestCase(assetA, assetB, true),
      new OrderTestCase(assetA, assetA, false),
      new OrderTestCase(assetB, assetA, false),
      new OrderTestCase(assetB, assetB, false),
    };
    for (OrderTestCase testCase : testCases) {
      try {
        new LiquidityPoolParameters(testCase.assetA, testCase.assetB);
        if (!testCase.valid) {
          fail(
              "Expected exception for invalid asset issuer order: "
                  + testCase.assetA
                  + ", "
                  + testCase.assetB);
        }
      } catch (IllegalArgumentException e) {
        if (testCase.valid) {
          fail(
              "Unexpected exception for valid asset issuer order: "
                  + testCase.assetA
                  + ", "
                  + testCase.assetB);
        }
      }
    }
  }

  @Value
  static class OrderTestCase {
    Asset assetA;
    Asset assetB;
    Boolean valid;
  }
}
