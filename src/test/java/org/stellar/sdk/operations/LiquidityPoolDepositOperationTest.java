package org.stellar.sdk.operations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.stellar.sdk.Asset.create;

import java.math.BigDecimal;
import org.junit.Test;
import org.stellar.sdk.Asset;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.LiquidityPool;
import org.stellar.sdk.Price;

public class LiquidityPoolDepositOperationTest {
  // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
  KeyPair source =
      KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");
  Asset nativeAsset = create("native");
  Asset creditAsset =
      create(null, "ABC", "GCRA6COW27CY5MTKIA7POQ2326C5ABYCXODBN4TFF5VL4FMBRHOT3YHU");
  String liquidityPoolId = new LiquidityPool(nativeAsset, creditAsset).getLiquidityPoolId();

  @Test
  public void testLiquidityPoolDepositOperationValid() {
    BigDecimal maxAmountA = BigDecimal.valueOf(1000);
    BigDecimal maxAmountB = BigDecimal.valueOf(2000);
    Price minPrice = Price.fromString("0.01");
    Price maxPrice = Price.fromString("0.02");
    LiquidityPoolDepositOperation operation =
        LiquidityPoolDepositOperation.builder()
            .liquidityPoolId(liquidityPoolId)
            .maxAmountA(maxAmountA)
            .maxAmountB(maxAmountB)
            .minPrice(minPrice)
            .maxPrice(maxPrice)
            .sourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr();
    LiquidityPoolDepositOperation parsedOperation =
        (LiquidityPoolDepositOperation) Operation.fromXdr(xdr);

    assertEquals(source.getAccountId(), parsedOperation.getSourceAccount());
    assertEquals(liquidityPoolId, parsedOperation.getLiquidityPoolId());
    assertEquals(new BigDecimal("1000.0000000"), parsedOperation.getMaxAmountA());
    assertEquals(new BigDecimal("2000.0000000"), parsedOperation.getMaxAmountB());
    assertEquals(minPrice, parsedOperation.getMinPrice());
    assertEquals(maxPrice, parsedOperation.getMaxPrice());

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAABb5NUX4ubtAgBk7zCsrbB/oCH2ADwtupaNB0FyfhxedxwAAAAJUC+QAAAAABKgXyAAAAAABAAAAZAAAAAEAAAAy",
        operation.toXdrBase64());
  }

  @Test
  public void testLiquidityPoolDepositOperation_UpperLiquidityPoolId() {
    BigDecimal maxAmountA = BigDecimal.valueOf(1000);
    BigDecimal maxAmountB = BigDecimal.valueOf(2000);
    Price minPrice = Price.fromString("0.01");
    Price maxPrice = Price.fromString("0.02");
    LiquidityPoolDepositOperation operation =
        LiquidityPoolDepositOperation.builder()
            .liquidityPoolId(liquidityPoolId.toUpperCase())
            .maxAmountA(maxAmountA)
            .maxAmountB(maxAmountB)
            .minPrice(minPrice)
            .maxPrice(maxPrice)
            .sourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr();
    LiquidityPoolDepositOperation parsedOperation =
        (LiquidityPoolDepositOperation) Operation.fromXdr(xdr);

    assertEquals(source.getAccountId(), parsedOperation.getSourceAccount());
    assertEquals(liquidityPoolId, parsedOperation.getLiquidityPoolId());
    assertEquals(new BigDecimal("1000.0000000"), parsedOperation.getMaxAmountA());
    assertEquals(new BigDecimal("2000.0000000"), parsedOperation.getMaxAmountB());
    assertEquals(minPrice, parsedOperation.getMinPrice());
    assertEquals(maxPrice, parsedOperation.getMaxPrice());

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAABb5NUX4ubtAgBk7zCsrbB/oCH2ADwtupaNB0FyfhxedxwAAAAJUC+QAAAAABKgXyAAAAAABAAAAZAAAAAEAAAAy",
        operation.toXdrBase64());
  }

  @Test
  public void testConstructorPairs() {
    BigDecimal maxAmountA = BigDecimal.valueOf(1000);
    BigDecimal maxAmountB = BigDecimal.valueOf(2000);
    Price minPrice = Price.fromString("0.01");
    Price maxPrice = Price.fromString("0.02");
    LiquidityPoolDepositOperation operation =
        new LiquidityPoolDepositOperation(
            nativeAsset, maxAmountA, creditAsset, maxAmountB, minPrice, maxPrice);
    operation.setSourceAccount(source.getAccountId());

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr();
    LiquidityPoolDepositOperation parsedOperation =
        (LiquidityPoolDepositOperation) Operation.fromXdr(xdr);

    assertEquals(source.getAccountId(), parsedOperation.getSourceAccount());
    assertEquals(liquidityPoolId, parsedOperation.getLiquidityPoolId());
    assertEquals(new BigDecimal("1000.0000000"), parsedOperation.getMaxAmountA());
    assertEquals(new BigDecimal("2000.0000000"), parsedOperation.getMaxAmountB());
    assertEquals(minPrice, parsedOperation.getMinPrice());
    assertEquals(maxPrice, parsedOperation.getMaxPrice());

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAABb5NUX4ubtAgBk7zCsrbB/oCH2ADwtupaNB0FyfhxedxwAAAAJUC+QAAAAABKgXyAAAAAABAAAAZAAAAAEAAAAy",
        operation.toXdrBase64());
  }

  @Test
  public void testConstructorPairsMisorderedAssets() {
    BigDecimal maxAmountA = BigDecimal.valueOf(1000);
    BigDecimal maxAmountB = BigDecimal.valueOf(2000);
    Price minPrice = Price.fromString("0.01");
    Price maxPrice = Price.fromString("0.02");
    try {
      new LiquidityPoolDepositOperation(
              creditAsset, maxAmountB, nativeAsset, maxAmountA, minPrice, maxPrice)
          .setSourceAccount(source.getAccountId());
      fail();
    } catch (RuntimeException e) {
      assertEquals("Assets are not in lexicographic order", e.getMessage());
    }
  }
}
