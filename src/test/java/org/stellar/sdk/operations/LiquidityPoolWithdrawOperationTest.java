package org.stellar.sdk.operations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.stellar.sdk.Asset.create;

import org.junit.Test;
import org.stellar.sdk.AccountConverter;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetAmount;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.LiquidityPoolId;
import org.stellar.sdk.LiquidityPoolParameters;

public class LiquidityPoolWithdrawOperationTest {
  // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
  KeyPair source =
      KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");
  Asset nativeAsset = create("native");
  Asset creditAsset =
      create(null, "ABC", "GCRA6COW27CY5MTKIA7POQ2326C5ABYCXODBN4TFF5VL4FMBRHOT3YHU");
  LiquidityPoolId liquidityPoolID =
      new LiquidityPoolParameters(nativeAsset, creditAsset).getLiquidityPoolId();

  @Test
  public void testLiquidityPoolWithdrawOperationValid() {
    String amount = "5";
    String minAmountA = "1000";
    String minAmountB = "2000";
    LiquidityPoolWithdrawOperation operation =
        LiquidityPoolWithdrawOperation.builder()
            .liquidityPoolID(liquidityPoolID)
            .amount(amount)
            .minAmountA(minAmountA)
            .minAmountB(minAmountB)
            .sourceAccount(source.getAccountId())
            .build();

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
      assertEquals("Assets are not in lexicographic order", e.getMessage());
    }
  }
}
