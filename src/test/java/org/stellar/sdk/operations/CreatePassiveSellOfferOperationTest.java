package org.stellar.sdk.operations;

import static org.junit.Assert.assertTrue;
import static org.stellar.sdk.Asset.create;

import org.junit.Assert;
import org.junit.Test;
import org.stellar.sdk.AccountConverter;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeCreditAlphaNum4;
import org.stellar.sdk.AssetTypeNative;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Price;

public class CreatePassiveSellOfferOperationTest {
  @Test
  public void testBuilder() {
    // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
    KeyPair source =
        KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");
    // GBCP5W2VS7AEWV2HFRN7YYC623LTSV7VSTGIHFXDEJU7S5BAGVCSETRR
    KeyPair issuer =
        KeyPair.fromSecretSeed("SA64U7C5C7BS5IHWEPA7YWFN3Z6FE5L6KAMYUIT4AQ7KVTVLD23C6HEZ");

    Asset selling = new AssetTypeNative();
    Asset buying = create(null, "USD", issuer.getAccountId());
    String amount = "0.00001";
    String price = "2.93850088"; // n=36731261 d=12500000
    Price priceObj = Price.fromString(price);

    CreatePassiveSellOfferOperation operation =
        CreatePassiveSellOfferOperation.builder()
            .selling(selling)
            .buying(buying)
            .amount(amount)
            .price(priceObj)
            .sourceAccount(source.getAccountId())
            .build();

    assertTrue(operation.getSelling() instanceof AssetTypeNative);
    assertTrue(operation.getBuying() instanceof AssetTypeCreditAlphaNum4);
    Assert.assertEquals(operation.getBuying(), buying);
    Assert.assertEquals(operation.getSelling(), selling);
    Assert.assertEquals(amount, operation.getAmount());
    Assert.assertEquals(new Price(36731261, 12500000), operation.getPrice());
    Assert.assertEquals(priceObj.getNumerator(), 36731261);
    Assert.assertEquals(priceObj.getDenominator(), 12500000);

    Assert.assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAAAQAAAAAAAAAAVVTRAAAAAAARP7bVZfAS1dHLFv8YF7W1zlX9ZTMg5bjImn5dCA1RSIAAAAAAAAAZAIweX0Avrwg",
        operation.toXdrBase64(AccountConverter.enableMuxed()));
  }

  @Test
  public void testFromXdr() {
    // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
    KeyPair source =
        KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");
    // GBCP5W2VS7AEWV2HFRN7YYC623LTSV7VSTGIHFXDEJU7S5BAGVCSETRR
    KeyPair issuer =
        KeyPair.fromSecretSeed("SA64U7C5C7BS5IHWEPA7YWFN3Z6FE5L6KAMYUIT4AQ7KVTVLD23C6HEZ");

    Asset selling = new AssetTypeNative();
    Asset buying = create(null, "USD", issuer.getAccountId());
    String amount = "0.00001";
    String price = "2.93850088"; // n=36731261 d=12500000
    Price priceObj = Price.fromString(price);

    CreatePassiveSellOfferOperation op =
        CreatePassiveSellOfferOperation.builder()
            .selling(selling)
            .buying(buying)
            .amount(amount)
            .price(priceObj)
            .sourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdrObject = op.toXdr();
    CreatePassiveSellOfferOperation restoreOp =
        (CreatePassiveSellOfferOperation) Operation.fromXdr(xdrObject);
    Assert.assertEquals(restoreOp, op);
  }
}
