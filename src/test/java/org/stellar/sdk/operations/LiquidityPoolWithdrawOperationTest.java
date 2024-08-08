package org.stellar.sdk.operations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.stellar.sdk.Asset.create;

import java.math.BigDecimal;
import org.junit.Test;
import org.stellar.sdk.Asset;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.LiquidityPool;

public class LiquidityPoolWithdrawOperationTest {
  // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
  KeyPair source =
      KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");
  Asset nativeAsset = create("native");
  Asset creditAsset =
      create(null, "ABC", "GCRA6COW27CY5MTKIA7POQ2326C5ABYCXODBN4TFF5VL4FMBRHOT3YHU");
  String liquidityPoolID = new LiquidityPool(nativeAsset, creditAsset).getLiquidityPoolId();

  @Test
  public void testLiquidityPoolWithdrawOperationValid() {
    BigDecimal amount = BigDecimal.valueOf(5);
    BigDecimal minAmountA = BigDecimal.valueOf(1000);
    BigDecimal minAmountB = BigDecimal.valueOf(2000);
    BigDecimal formattedAmount = new BigDecimal("5.0000000");
    BigDecimal formattedMinAmountA = new BigDecimal("1000.0000000");
    BigDecimal formattedMinAmountB = new BigDecimal("2000.0000000");
    LiquidityPoolWithdrawOperation operation =
        LiquidityPoolWithdrawOperation.builder()
            .liquidityPoolID(liquidityPoolID)
            .amount(amount)
            .minAmountA(minAmountA)
            .minAmountB(minAmountB)
            .sourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr();
    LiquidityPoolWithdrawOperation parsedOperation =
        (LiquidityPoolWithdrawOperation) Operation.fromXdr(xdr);

    assertEquals(source.getAccountId(), parsedOperation.getSourceAccount());
    assertEquals(liquidityPoolID, parsedOperation.getLiquidityPoolID());
    assertEquals(formattedAmount, parsedOperation.getAmount());
    assertEquals(formattedMinAmountA, parsedOperation.getMinAmountA());
    assertEquals(formattedMinAmountB, parsedOperation.getMinAmountB());

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAABf5NUX4ubtAgBk7zCsrbB/oCH2ADwtupaNB0FyfhxedxwAAAAAC+vCAAAAAAlQL5AAAAAAEqBfIAA==",
        operation.toXdrBase64());
  }

  @Test
  public void testLiquidityPoolWithdrawOperation_UpperLiquidityPoolId() {
    BigDecimal amount = BigDecimal.valueOf(5);
    BigDecimal minAmountA = BigDecimal.valueOf(1000);
    BigDecimal minAmountB = BigDecimal.valueOf(2000);
    BigDecimal formattedAmount = new BigDecimal("5.0000000");
    BigDecimal formattedMinAmountA = new BigDecimal("1000.0000000");
    BigDecimal formattedMinAmountB = new BigDecimal("2000.0000000");
    LiquidityPoolWithdrawOperation operation =
        LiquidityPoolWithdrawOperation.builder()
            .liquidityPoolID(liquidityPoolID.toUpperCase())
            .amount(amount)
            .minAmountA(minAmountA)
            .minAmountB(minAmountB)
            .sourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr();
    LiquidityPoolWithdrawOperation parsedOperation =
        (LiquidityPoolWithdrawOperation) Operation.fromXdr(xdr);

    assertEquals(source.getAccountId(), parsedOperation.getSourceAccount());
    assertEquals(liquidityPoolID, parsedOperation.getLiquidityPoolID());
    assertEquals(formattedAmount, parsedOperation.getAmount());
    assertEquals(formattedMinAmountA, parsedOperation.getMinAmountA());
    assertEquals(formattedMinAmountB, parsedOperation.getMinAmountB());

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAABf5NUX4ubtAgBk7zCsrbB/oCH2ADwtupaNB0FyfhxedxwAAAAAC+vCAAAAAAlQL5AAAAAAEqBfIAA==",
        operation.toXdrBase64());
  }

  @Test
  public void testConstructorPairs() {
    BigDecimal amount = BigDecimal.valueOf(5);
    BigDecimal minAmountA = BigDecimal.valueOf(1000);
    BigDecimal minAmountB = BigDecimal.valueOf(2000);
    BigDecimal formattedAmount = new BigDecimal("5.0000000");
    BigDecimal formattedMinAmountA = new BigDecimal("1000.0000000");
    BigDecimal formattedMinAmountB = new BigDecimal("2000.0000000");
    LiquidityPoolWithdrawOperation operation =
        new LiquidityPoolWithdrawOperation(
            nativeAsset, minAmountA, creditAsset, minAmountB, amount);
    operation.setSourceAccount(source.getAccountId());

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr();
    LiquidityPoolWithdrawOperation parsedOperation =
        (LiquidityPoolWithdrawOperation) Operation.fromXdr(xdr);

    assertEquals(source.getAccountId(), parsedOperation.getSourceAccount());
    assertEquals(liquidityPoolID, parsedOperation.getLiquidityPoolID());
    assertEquals(formattedAmount, parsedOperation.getAmount());
    assertEquals(formattedMinAmountA, parsedOperation.getMinAmountA());
    assertEquals(formattedMinAmountB, parsedOperation.getMinAmountB());
    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAABf5NUX4ubtAgBk7zCsrbB/oCH2ADwtupaNB0FyfhxedxwAAAAAC+vCAAAAAAlQL5AAAAAAEqBfIAA==",
        operation.toXdrBase64());
  }

  @Test
  public void testConstructorPairsMisorderedAssets() {
    BigDecimal amount = BigDecimal.valueOf(5);
    BigDecimal minAmountA = BigDecimal.valueOf(1000);
    BigDecimal minAmountB = BigDecimal.valueOf(2000);
    try {
      new LiquidityPoolWithdrawOperation(creditAsset, minAmountA, nativeAsset, minAmountB, amount)
          .setSourceAccount(source.getAccountId());
      fail();
    } catch (RuntimeException e) {
      assertEquals("Assets are not in lexicographic order", e.getMessage());
    }
  }
}
