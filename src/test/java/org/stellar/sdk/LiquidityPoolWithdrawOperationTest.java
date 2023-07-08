package org.stellar.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.stellar.sdk.Asset.create;

import org.junit.Test;
import org.stellar.sdk.xdr.LiquidityPoolType;

public class LiquidityPoolWithdrawOperationTest {
  // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
  KeyPair source =
      KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");
  Asset nativeAsset = create("native");
  Asset creditAsset =
      create(null, "ABC", "GCRA6COW27CY5MTKIA7POQ2326C5ABYCXODBN4TFF5VL4FMBRHOT3YHU");
  LiquidityPoolID liquidityPoolID =
      new LiquidityPoolID(
          LiquidityPoolType.LIQUIDITY_POOL_CONSTANT_PRODUCT,
          nativeAsset,
          creditAsset,
          LiquidityPoolParameters.Fee);

  @Test
  public void testLiquidityPoolWithdrawOperationValid() {
    String amount = "5";
    String minAmountA = "1000";
    String minAmountB = "2000";
    LiquidityPoolWithdrawOperation operation =
        new LiquidityPoolWithdrawOperation(liquidityPoolID, amount, minAmountA, minAmountB);
    operation.setSourceAccount(source.getAccountId());

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    LiquidityPoolWithdrawOperation parsedOperation =
        (LiquidityPoolWithdrawOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);

    assertEquals(source.getAccountId(), parsedOperation.getSourceAccount());
    assertEquals(liquidityPoolID, parsedOperation.getLiquidityPoolID());
    assertEquals(amount, parsedOperation.getAmount());
    assertEquals(minAmountA, parsedOperation.getMinAmountA());
    assertEquals(minAmountB, parsedOperation.getMinAmountB());

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAABf5NUX4ubtAgBk7zCsrbB/oCH2ADwtupaNB0FyfhxedxwAAAAAC+vCAAAAAAlQL5AAAAAAEqBfIAA==",
        operation.toXdrBase64(AccountConverter.enableMuxed()));
  }

  @Test
  public void testConstructorPairs() {
    String amount = "5";
    String minAmountA = "1000";
    String minAmountB = "2000";
    LiquidityPoolWithdrawOperation operation =
        new LiquidityPoolWithdrawOperation(
            new AssetAmount(nativeAsset, minAmountA),
            new AssetAmount(creditAsset, minAmountB),
            amount);
    operation.setSourceAccount(source.getAccountId());

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    LiquidityPoolWithdrawOperation parsedOperation =
        (LiquidityPoolWithdrawOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);

    assertEquals(source.getAccountId(), parsedOperation.getSourceAccount());
    assertEquals(liquidityPoolID, parsedOperation.getLiquidityPoolID());
    assertEquals(amount, parsedOperation.getAmount());
    assertEquals(minAmountA, parsedOperation.getMinAmountA());
    assertEquals(minAmountB, parsedOperation.getMinAmountB());

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAABf5NUX4ubtAgBk7zCsrbB/oCH2ADwtupaNB0FyfhxedxwAAAAAC+vCAAAAAAlQL5AAAAAAEqBfIAA==",
        operation.toXdrBase64(AccountConverter.enableMuxed()));
  }

  @Test
  public void testConstructorPairsMisorderedAssets() {
    String amount = "5";
    String minAmountA = "1000";
    String minAmountB = "2000";
    try {
      new LiquidityPoolWithdrawOperation(
              new AssetAmount(creditAsset, minAmountA),
              new AssetAmount(nativeAsset, minAmountB),
              amount)
          .setSourceAccount(source.getAccountId());
      fail();
    } catch (RuntimeException e) {
      assertEquals("AssetA must be < AssetB", e.getMessage());
    }
  }
}
