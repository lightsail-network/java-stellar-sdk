package org.stellar.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import lombok.Value;
import org.junit.Test;

public class LiquidityPoolTest {
  @Test
  public void testLiquidityPool() throws IOException {
    Asset assetA = Asset.create("ARST:GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO");
    Asset assetB = Asset.create("USD:GCEZWKCA5VLDNRLN3RPRJMRZOX3Z6G5CHCGSNFHEYVXM3XOJMDS674JZ");
    LiquidityPool liquidityPool = new LiquidityPool(assetA, assetB);
    String expectedXdr =
        "AAAAAAAAAAFBUlNUAAAAAH8wYjTJienWf2nf2TEZi2APPWzmtkwiQHAftisIgyuHAAAAAVVTRAAAAAAAiZsoQO1WNsVt3F8Usjl1958bojiNJpTkxW7N3clg5e8AAAAe";
    assertEquals(liquidityPool.toXdr().toXdrBase64(), expectedXdr);
    org.stellar.sdk.xdr.LiquidityPoolParameters xdr = liquidityPool.toXdr();
    LiquidityPool parsedLiquidityPool = LiquidityPool.fromXdr(xdr);
    assertEquals(liquidityPool, parsedLiquidityPool);
    assertEquals(
        liquidityPool.getLiquidityPoolId(),
        "dd7b1ab831c273310ddbec6f97870aa83c2fbd78ce22aded37ecbf4f3380fac7");
    assertEquals(liquidityPool.getAssetA(), assetA);
    assertEquals(liquidityPool.getAssetB(), assetB);
    assertEquals(liquidityPool.getFee(), LiquidityPool.FEE);
    assertEquals(liquidityPool.getFee(), 30);
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
        new LiquidityPool(testCase.assetA, testCase.assetB);
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
        new LiquidityPool(testCase.assetA, testCase.assetB);
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
        new LiquidityPool(testCase.assetA, testCase.assetB);
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

  @Test
  public void testIssuersAreOrderedByRawKeyBytes() {
    // Issuers of testnet pool c2a730127d006897b021d6fcb279da68b4f65f9b6a408c74eb2cdd85ea862d5e.
    // GDRQ sorts after GD32 as strkey text, but before it as raw key bytes (e305... < f7a4...).
    for (String code : new String[] {"USD", "BANANA"}) {
      Asset assetA =
          Asset.create(code + ":GDRQL6EF772CANMYX55HXMU463OLAX46ZRCPCWS4KVL66ZS4BYA7OYDL");
      Asset assetB =
          Asset.create(code + ":GD32JQHFWLIROCNQ564GLCB2M7T4GMQAP5UKHMVT64OQISWL2LHYK2HO");
      assertTrue(assetA.compareTo(assetB) < 0);
      assertTrue(assetB.compareTo(assetA) > 0);
      new LiquidityPool(assetA, assetB);
      assertThrows(IllegalArgumentException.class, () -> new LiquidityPool(assetB, assetA));
    }
  }

  @Test
  public void testLiquidityPoolIdWithIssuersOrderedByRawKeyBytes() {
    Asset assetA = Asset.create("USD:GDRQL6EF772CANMYX55HXMU463OLAX46ZRCPCWS4KVL66ZS4BYA7OYDL");
    Asset assetB = Asset.create("USD:GD32JQHFWLIROCNQ564GLCB2M7T4GMQAP5UKHMVT64OQISWL2LHYK2HO");
    assertEquals(
        "c2a730127d006897b021d6fcb279da68b4f65f9b6a408c74eb2cdd85ea862d5e",
        new LiquidityPool(assetA, assetB).getLiquidityPoolId());
  }

  @Test
  public void testDecodeTransactionWithIssuersOrderedByRawKeyBytes() throws IOException {
    // Testnet transaction 0126748091930959a3aefac9296e1773b947f964a9b73f37ce856c4df32d346f
    String envelopeXdr =
        "AAAAAgAAAAADeR2SwEvKvthWOdthGC/c1Bxym7fafJmtjj5EyvmvxAAAASwAVEAXAAAAAQAAAAAAAAAAAAAAAwAAAAAAAAAGAAAAAVVTRAAAAAAA4wX4hf/0IDWYv3p7spz23LBfnsxE8VpcVVfvZlwOAfd//////////wAAAAAAAAAGAAAAAVVTRAAAAAAA96TA5bLRFwmw77hliDpn58MyAH9oo7Kz9x0ESsvSz4V//////////wAAAAAAAAAGAAAAAwAAAAAAAAABVVNEAAAAAADjBfiF//QgNZi/enuynPbcsF+ezETxWlxVV+9mXA4B9wAAAAFVU0QAAAAAAPekwOWy0RcJsO+4ZYg6Z+fDMgB/aKOys/cdBErL0s+FAAAAHn//////////AAAAAAAAAAHK+a/EAAAAQMtA5QpAy6I3ILVmPZcRA3SJhGo4CiMT7K7dXQt7Qo1Prip/OedNocMY2QloSRle4hYvhJilIiOyoD85l3+WrQM=";
    AbstractTransaction transaction =
        AbstractTransaction.fromEnvelopeXdr(envelopeXdr, Network.TESTNET);
    assertEquals(envelopeXdr, transaction.toEnvelopeXdrBase64());
    assertEquals(
        "0126748091930959a3aefac9296e1773b947f964a9b73f37ce856c4df32d346f", transaction.hashHex());
  }

  @Value
  static class OrderTestCase {
    Asset assetA;
    Asset assetB;
    Boolean valid;
  }
}
