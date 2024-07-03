package org.stellar.sdk.operations;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeCreditAlphaNum12;
import org.stellar.sdk.AssetTypeCreditAlphaNum4;
import org.stellar.sdk.AssetTypeNative;
import org.stellar.sdk.KeyPair;

public class PathPaymentStrictSendOperationTest {

  @Test
  public void testPathPaymentStrictSendOperation() {
    // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
    KeyPair source =
        KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");
    // GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR
    KeyPair destination =
        KeyPair.fromSecretSeed("SDHZGHURAYXKU2KMVHPOXI6JG2Q4BSQUQCEOY72O3QQTCLR2T455PMII");
    // GCGZLB3X2B3UFOFSHHQ6ZGEPEX7XYPEH6SBFMIV74EUDOFZJA3VNL6X4
    KeyPair issuer =
        KeyPair.fromSecretSeed("SBOBVZUN6WKVMI6KIL2GHBBEETEV6XKQGILITNH6LO6ZA22DBMSDCPAG");

    // GAVAQKT2M7B4V3NN7RNNXPU5CWNDKC27MYHKLF5UNYXH4FNLFVDXKRSV
    KeyPair pathIssuer1 =
        KeyPair.fromSecretSeed("SALDLG5XU5AEJWUOHAJPSC4HJ2IK3Z6BXXP4GWRHFT7P7ILSCFFQ7TC5");
    // GBCP5W2VS7AEWV2HFRN7YYC623LTSV7VSTGIHFXDEJU7S5BAGVCSETRR
    KeyPair pathIssuer2 =
        KeyPair.fromSecretSeed("SA64U7C5C7BS5IHWEPA7YWFN3Z6FE5L6KAMYUIT4AQ7KVTVLD23C6HEZ");

    Asset sendAsset = new AssetTypeNative();
    String sendAmount = "0.0001";
    Asset destAsset = new AssetTypeCreditAlphaNum4("USD", issuer.getAccountId());
    String destMin = "0.0009";
    Asset[] path = {
      new AssetTypeCreditAlphaNum4("USD", pathIssuer1.getAccountId()),
      new AssetTypeCreditAlphaNum12("TESTTEST", pathIssuer2.getAccountId())
    };

    PathPaymentStrictSendOperation operation =
        PathPaymentStrictSendOperation.builder()
            .sendAsset(sendAsset)
            .sendAmount(sendAmount)
            .destination(destination.getAccountId())
            .destAsset(destAsset)
            .destMin(destMin)
            .path(path)
            .sourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr();
    PathPaymentStrictSendOperation parsedOperation =
        (PathPaymentStrictSendOperation) Operation.fromXdr(xdr);

    assertEquals(
        1000L, xdr.getBody().getPathPaymentStrictSendOp().getSendAmount().getInt64().longValue());
    assertEquals(
        9000L, xdr.getBody().getPathPaymentStrictSendOp().getDestMin().getInt64().longValue());
    assertTrue(parsedOperation.getSendAsset() instanceof AssetTypeNative);
    assertEquals(source.getAccountId(), parsedOperation.getSourceAccount());
    assertEquals(destination.getAccountId(), parsedOperation.getDestination());
    assertEquals(sendAmount, parsedOperation.getSendAmount());
    assertTrue(parsedOperation.getDestAsset() instanceof AssetTypeCreditAlphaNum4);
    assertEquals(destMin, parsedOperation.getDestMin());
    assertArrayEquals(path, parsedOperation.getPath());

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAAA0AAAAAAAAAAAAAA+gAAAAA7eBSYbzcL5UKo7oXO24y1ckX+XuCtkDsyNHOp1n1bxAAAAABVVNEAAAAAACNlYd30HdCuLI54eyYjyX/fDyH9IJWIr/hKDcXKQbq1QAAAAAAACMoAAAAAgAAAAFVU0QAAAAAACoIKnpnw8rtrfxa276dFZo1C19mDqWXtG4ufhWrLUd1AAAAAlRFU1RURVNUAAAAAAAAAABE/ttVl8BLV0csW/xgXtbXOVf1lMyDluMiafl0IDVFIg==",
        operation.toXdrBase64());
  }

  @Test
  public void testPathPaymentStrictSendEmptyPathOperation() {
    // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
    KeyPair source =
        KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");
    // GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR
    KeyPair destination =
        KeyPair.fromSecretSeed("SDHZGHURAYXKU2KMVHPOXI6JG2Q4BSQUQCEOY72O3QQTCLR2T455PMII");
    // GCGZLB3X2B3UFOFSHHQ6ZGEPEX7XYPEH6SBFMIV74EUDOFZJA3VNL6X4
    KeyPair issuer =
        KeyPair.fromSecretSeed("SBOBVZUN6WKVMI6KIL2GHBBEETEV6XKQGILITNH6LO6ZA22DBMSDCPAG");

    // GAVAQKT2M7B4V3NN7RNNXPU5CWNDKC27MYHKLF5UNYXH4FNLFVDXKRSV
    KeyPair pathIssuer1 =
        KeyPair.fromSecretSeed("SALDLG5XU5AEJWUOHAJPSC4HJ2IK3Z6BXXP4GWRHFT7P7ILSCFFQ7TC5");
    // GBCP5W2VS7AEWV2HFRN7YYC623LTSV7VSTGIHFXDEJU7S5BAGVCSETRR
    KeyPair pathIssuer2 =
        KeyPair.fromSecretSeed("SA64U7C5C7BS5IHWEPA7YWFN3Z6FE5L6KAMYUIT4AQ7KVTVLD23C6HEZ");

    Asset sendAsset = new AssetTypeNative();
    String sendAmount = "0.0001";
    Asset destAsset = new AssetTypeCreditAlphaNum4("USD", issuer.getAccountId());
    String destMin = "0.0009";

    PathPaymentStrictSendOperation operation =
        PathPaymentStrictSendOperation.builder()
            .sendAsset(sendAsset)
            .sendAmount(sendAmount)
            .destination(destination.getAccountId())
            .destAsset(destAsset)
            .destMin(destMin)
            .sourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr();
    PathPaymentStrictSendOperation parsedOperation =
        (PathPaymentStrictSendOperation) Operation.fromXdr(xdr);

    assertEquals(
        1000L, xdr.getBody().getPathPaymentStrictSendOp().getSendAmount().getInt64().longValue());
    assertEquals(
        9000L, xdr.getBody().getPathPaymentStrictSendOp().getDestMin().getInt64().longValue());
    assertTrue(parsedOperation.getSendAsset() instanceof AssetTypeNative);
    assertEquals(source.getAccountId(), parsedOperation.getSourceAccount());
    assertEquals(destination.getAccountId(), parsedOperation.getDestination());
    assertEquals(sendAmount, parsedOperation.getSendAmount());
    assertTrue(parsedOperation.getDestAsset() instanceof AssetTypeCreditAlphaNum4);
    assertEquals(destMin, parsedOperation.getDestMin());
    assertEquals(0, parsedOperation.getPath().length);

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAAA0AAAAAAAAAAAAAA+gAAAAA7eBSYbzcL5UKo7oXO24y1ckX+XuCtkDsyNHOp1n1bxAAAAABVVNEAAAAAACNlYd30HdCuLI54eyYjyX/fDyH9IJWIr/hKDcXKQbq1QAAAAAAACMoAAAAAA==",
        operation.toXdrBase64());
  }

  @Test
  public void testMuxedPathPaymentStrictSendOperation() {
    String source = "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVAAAAAAAAAAAAAJLK";
    String destination = "MDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKAAAAAAMV7V2XYGQO";

    // GCGZLB3X2B3UFOFSHHQ6ZGEPEX7XYPEH6SBFMIV74EUDOFZJA3VNL6X4
    KeyPair issuer =
        KeyPair.fromSecretSeed("SBOBVZUN6WKVMI6KIL2GHBBEETEV6XKQGILITNH6LO6ZA22DBMSDCPAG");

    Asset sendAsset = new AssetTypeNative();
    String destMin = "0.0001";
    Asset destAsset = new AssetTypeCreditAlphaNum4("USD", issuer.getAccountId());
    String sendAmount = "0.0001";

    PathPaymentStrictSendOperation operation =
        PathPaymentStrictSendOperation.builder()
            .sendAsset(sendAsset)
            .sendAmount(sendAmount)
            .destination(destination)
            .destAsset(destAsset)
            .destMin(destMin)
            .sourceAccount(source)
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr();
    PathPaymentStrictSendOperation parsedOperation =
        (PathPaymentStrictSendOperation) Operation.fromXdr(xdr);

    assertEquals(destination, parsedOperation.getDestination());
    assertEquals(source, parsedOperation.getSourceAccount());

    parsedOperation = (PathPaymentStrictSendOperation) Operation.fromXdr(xdr);
    assertEquals(
        "MDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKAAAAAAMV7V2XYGQO",
        parsedOperation.getDestination());
    assertEquals(
        "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVAAAAAAAAAAAAAJLK",
        parsedOperation.getSourceAccount());
  }
}
