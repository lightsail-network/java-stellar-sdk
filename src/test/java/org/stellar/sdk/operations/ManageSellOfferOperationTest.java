package org.stellar.sdk.operations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.stellar.sdk.Asset.create;

import java.io.IOException;
import java.math.BigDecimal;
import org.junit.Test;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeCreditAlphaNum4;
import org.stellar.sdk.AssetTypeNative;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Price;
import org.stellar.sdk.exception.StrKeyException;

public class ManageSellOfferOperationTest {

  @Test
  public void testManageSellOfferOperation() throws IOException, StrKeyException {
    // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
    KeyPair source =
        KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");
    // GBCP5W2VS7AEWV2HFRN7YYC623LTSV7VSTGIHFXDEJU7S5BAGVCSETRR
    KeyPair issuer =
        KeyPair.fromSecretSeed("SA64U7C5C7BS5IHWEPA7YWFN3Z6FE5L6KAMYUIT4AQ7KVTVLD23C6HEZ");

    Asset selling = new AssetTypeNative();
    Asset buying = create(null, "USD", issuer.getAccountId());
    BigDecimal amount = new BigDecimal("0.00001");
    BigDecimal formattedAmount = new BigDecimal("0.0000100");
    String price = "0.85334384"; // n=5333399 d=6250000
    Price priceObj = Price.fromString(price);
    long offerId = 1;

    ManageSellOfferOperation operation =
        ManageSellOfferOperation.builder()
            .selling(selling)
            .buying(buying)
            .amount(amount)
            .price(priceObj)
            .offerId(offerId)
            .sourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr();
    ManageSellOfferOperation parsedOperation =
        (ManageSellOfferOperation) ManageSellOfferOperation.fromXdr(xdr);

    assertEquals(100L, xdr.getBody().getManageSellOfferOp().getAmount().getInt64().longValue());
    assertTrue(parsedOperation.getSelling() instanceof AssetTypeNative);
    assertTrue(parsedOperation.getBuying() instanceof AssetTypeCreditAlphaNum4);
    assertTrue(parsedOperation.getBuying().equals(buying));
    assertEquals(formattedAmount, parsedOperation.getAmount());
    assertEquals(new Price(5333399, 6250000), parsedOperation.getPrice());
    assertEquals(priceObj.getNumerator(), 5333399);
    assertEquals(priceObj.getDenominator(), 6250000);
    assertEquals(offerId, parsedOperation.getOfferId());

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAAAMAAAAAAAAAAVVTRAAAAAAARP7bVZfAS1dHLFv8YF7W1zlX9ZTMg5bjImn5dCA1RSIAAAAAAAAAZABRYZcAX14QAAAAAAAAAAE=",
        operation.toXdrBase64());
  }

  @Test
  public void testManageSellOfferOperationWithConstructorPrice() throws StrKeyException {
    // See https://github.com/stellar/java-stellar-sdk/issues/292
    // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
    KeyPair source =
        KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");
    // GBCP5W2VS7AEWV2HFRN7YYC623LTSV7VSTGIHFXDEJU7S5BAGVCSETRR
    KeyPair issuer =
        KeyPair.fromSecretSeed("SA64U7C5C7BS5IHWEPA7YWFN3Z6FE5L6KAMYUIT4AQ7KVTVLD23C6HEZ");

    Asset selling = new AssetTypeNative();
    Asset buying = create(null, "USD", issuer.getAccountId());
    BigDecimal amount = new BigDecimal("0.00001");
    Price price = new Price(10000000, 50);
    long offerId = 1;

    ManageSellOfferOperation operation =
        ManageSellOfferOperation.builder()
            .selling(selling)
            .buying(buying)
            .amount(amount)
            .price(price)
            .offerId(offerId)
            .sourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr();
    ManageSellOfferOperation parsedOperation =
        (ManageSellOfferOperation) ManageSellOfferOperation.fromXdr(xdr);
    assertEquals(operation, parsedOperation);
  }

  @Test
  public void testManageSellOfferOperation_BadArithmeticRegression() throws IOException {
    // from https://github.com/stellar/java-stellar-sdk/issues/183

    String transactionEnvelopeToDecode =
        "AAAAAButy5zasS3DLZ5uFpZHL25aiHUfKRwdv1+3Wp12Ce7XAAAAZAEyGwYAAAAOAAAAAAAAAAAAAAABAAAAAQAAAAAbrcuc2rEtwy2ebhaWRy9uWoh1HykcHb9ft1qddgnu1wAAAAMAAAAAAAAAAUtJTgAAAAAARkrT28ebM6YQyhVZi1ttlwq/dk6ijTpyTNuHIMgUp+EAAAAAAAARPSfDKZ0AAv7oAAAAAAAAAAAAAAAAAAAAAXYJ7tcAAABAbE8rEoFt0Hcv41iwVCl74C1Hyr+Lj8ZyaYn7zTJhezClbc+pTW1KgYFIZOJiGVth2xFnBT1pMXuQkVdTlB3FCw==";

    org.stellar.sdk.xdr.TransactionEnvelope transactionEnvelope =
        org.stellar.sdk.xdr.TransactionEnvelope.fromXdrBase64(transactionEnvelopeToDecode);
    assertEquals(1, transactionEnvelope.getV0().getTx().getOperations().length);

    ManageSellOfferOperation op =
        (ManageSellOfferOperation)
            Operation.fromXdr(transactionEnvelope.getV0().getTx().getOperations()[0]);

    assertEquals(Price.fromString("3397.893306099996"), op.getPrice());
  }

  @Test
  public void testManageBuyOfferOperation_BadArithmeticRegression() throws IOException {
    // from https://github.com/stellar/java-stellar-sdk/issues/183

    String transactionEnvelopeToDecode =
        "AAAAAButy5zasS3DLZ5uFpZHL25aiHUfKRwdv1+3Wp12Ce7XAAAAZAEyGwYAAAAxAAAAAAAAAAAAAAABAAAAAQAAAAAbrcuc2rEtwy2ebhaWRy9uWoh1HykcHb9ft1qddgnu1wAAAAwAAAABS0lOAAAAAABGStPbx5szphDKFVmLW22XCr92TqKNOnJM24cgyBSn4QAAAAAAAAAAACNyOCfDKZ0AAv7oAAAAAAABv1IAAAAAAAAAAA==";

    org.stellar.sdk.xdr.TransactionEnvelope transactionEnvelope =
        org.stellar.sdk.xdr.TransactionEnvelope.fromXdrBase64(transactionEnvelopeToDecode);
    assertEquals(1, transactionEnvelope.getV0().getTx().getOperations().length);

    ManageBuyOfferOperation op =
        (ManageBuyOfferOperation)
            Operation.fromXdr(transactionEnvelope.getV0().getTx().getOperations()[0]);

    assertEquals(Price.fromString("3397.893306099996"), op.getPrice());
  }
}
