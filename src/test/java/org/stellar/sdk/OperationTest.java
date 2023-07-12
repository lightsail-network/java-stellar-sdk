package org.stellar.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.stellar.sdk.Asset.create;

import com.google.common.collect.Lists;
import com.google.common.io.BaseEncoding;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import org.junit.Test;
import org.stellar.sdk.xdr.SignerKey;
import org.stellar.sdk.xdr.TrustLineFlags;
import org.stellar.sdk.xdr.XdrDataInputStream;

public class OperationTest {

  @Test
  public void testCreateAccountOperation()
      throws FormatException, IOException, AssetCodeLengthInvalidException {
    // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
    KeyPair source =
        KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");
    // GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR
    KeyPair destination =
        KeyPair.fromSecretSeed("SDHZGHURAYXKU2KMVHPOXI6JG2Q4BSQUQCEOY72O3QQTCLR2T455PMII");

    String startingAmount = "1000";
    CreateAccountOperation operation =
        new CreateAccountOperation.Builder(destination.getAccountId(), startingAmount)
            .setSourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    CreateAccountOperation parsedOperation =
        (CreateAccountOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);

    assertEquals(
        10000000000L,
        xdr.getBody().getCreateAccountOp().getStartingBalance().getInt64().longValue());
    assertEquals(source.getAccountId(), parsedOperation.getSourceAccount());
    assertEquals(destination.getAccountId(), parsedOperation.getDestination());
    assertEquals(startingAmount, parsedOperation.getStartingBalance());

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAAAAAAAAA7eBSYbzcL5UKo7oXO24y1ckX+XuCtkDsyNHOp1n1bxAAAAACVAvkAA==",
        operation.toXdrBase64(AccountConverter.enableMuxed()));
  }

  @Test
  public void testPaymentOperation()
      throws FormatException, IOException, AssetCodeLengthInvalidException {
    // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
    KeyPair source =
        KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");
    // GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR
    KeyPair destination =
        KeyPair.fromSecretSeed("SDHZGHURAYXKU2KMVHPOXI6JG2Q4BSQUQCEOY72O3QQTCLR2T455PMII");

    Asset asset = new AssetTypeNative();
    String amount = "1000";

    PaymentOperation operation =
        new PaymentOperation.Builder(destination.getAccountId(), asset, amount)
            .setSourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    PaymentOperation parsedOperation =
        (PaymentOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);

    assertEquals(10000000000L, xdr.getBody().getPaymentOp().getAmount().getInt64().longValue());
    assertEquals(source.getAccountId(), parsedOperation.getSourceAccount());
    assertEquals(destination.getAccountId(), parsedOperation.getDestination());
    assertTrue(parsedOperation.getAsset() instanceof AssetTypeNative);
    assertEquals(amount, parsedOperation.getAmount());

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAAAEAAAAA7eBSYbzcL5UKo7oXO24y1ckX+XuCtkDsyNHOp1n1bxAAAAAAAAAAAlQL5AA=",
        operation.toXdrBase64(AccountConverter.enableMuxed()));
  }

  @Test
  public void testMuxedPaymentOperation()
      throws FormatException, IOException, AssetCodeLengthInvalidException {
    String source = "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVAAAAAAAAAAAAAJLK";
    String destination = "MDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKAAAAAAMV7V2XYGQO";

    Asset asset = new AssetTypeNative();
    String amount = "1000";

    PaymentOperation operation =
        new PaymentOperation.Builder(destination, asset, amount).setSourceAccount(source).build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    PaymentOperation parsedOperation =
        (PaymentOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);
    assertEquals(destination, parsedOperation.getDestination());
    assertEquals(source, parsedOperation.getSourceAccount());

    parsedOperation = (PaymentOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);
    assertEquals(
        "MDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKAAAAAAMV7V2XYGQO",
        parsedOperation.getDestination());
    assertEquals(
        "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVAAAAAAAAAAAAAJLK",
        parsedOperation.getSourceAccount());
  }

  @Test
  public void testPathPaymentStrictReceiveOperation()
      throws FormatException, IOException, AssetCodeLengthInvalidException {
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
    String sendMax = "0.0001";
    Asset destAsset = new AssetTypeCreditAlphaNum4("USD", issuer.getAccountId());
    String destAmount = "0.0001";
    Asset[] path = {
      new AssetTypeCreditAlphaNum4("USD", pathIssuer1.getAccountId()),
      new AssetTypeCreditAlphaNum12("TESTTEST", pathIssuer2.getAccountId())
    };

    PathPaymentStrictReceiveOperation operation =
        new PathPaymentStrictReceiveOperation.Builder(
                sendAsset, sendMax, destination.getAccountId(), destAsset, destAmount)
            .setPath(path)
            .setSourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    PathPaymentStrictReceiveOperation parsedOperation =
        (PathPaymentStrictReceiveOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);

    assertEquals(
        1000L, xdr.getBody().getPathPaymentStrictReceiveOp().getSendMax().getInt64().longValue());
    assertEquals(
        1000L,
        xdr.getBody().getPathPaymentStrictReceiveOp().getDestAmount().getInt64().longValue());
    assertTrue(parsedOperation.getSendAsset() instanceof AssetTypeNative);
    assertEquals(source.getAccountId(), parsedOperation.getSourceAccount());
    assertEquals(destination.getAccountId(), parsedOperation.getDestination());
    assertEquals(sendMax, parsedOperation.getSendMax());
    assertTrue(parsedOperation.getDestAsset() instanceof AssetTypeCreditAlphaNum4);
    assertEquals(destAmount, parsedOperation.getDestAmount());
    assertEquals(Lists.newArrayList(path), Lists.newArrayList(parsedOperation.getPath()));

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAAAIAAAAAAAAAAAAAA+gAAAAA7eBSYbzcL5UKo7oXO24y1ckX+XuCtkDsyNHOp1n1bxAAAAABVVNEAAAAAACNlYd30HdCuLI54eyYjyX/fDyH9IJWIr/hKDcXKQbq1QAAAAAAAAPoAAAAAgAAAAFVU0QAAAAAACoIKnpnw8rtrfxa276dFZo1C19mDqWXtG4ufhWrLUd1AAAAAlRFU1RURVNUAAAAAAAAAABE/ttVl8BLV0csW/xgXtbXOVf1lMyDluMiafl0IDVFIg==",
        operation.toXdrBase64(AccountConverter.enableMuxed()));
  }

  @Test
  public void testPathPaymentStrictReceiveEmptyPathOperation()
      throws FormatException, IOException, AssetCodeLengthInvalidException {
    // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
    KeyPair source =
        KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");
    // GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR
    KeyPair destination =
        KeyPair.fromSecretSeed("SDHZGHURAYXKU2KMVHPOXI6JG2Q4BSQUQCEOY72O3QQTCLR2T455PMII");
    // GCGZLB3X2B3UFOFSHHQ6ZGEPEX7XYPEH6SBFMIV74EUDOFZJA3VNL6X4
    KeyPair issuer =
        KeyPair.fromSecretSeed("SBOBVZUN6WKVMI6KIL2GHBBEETEV6XKQGILITNH6LO6ZA22DBMSDCPAG");

    Asset sendAsset = new AssetTypeNative();
    String sendMax = "0.0001";
    Asset destAsset = new AssetTypeCreditAlphaNum4("USD", issuer.getAccountId());
    String destAmount = "0.0001";

    PathPaymentStrictReceiveOperation operation =
        new PathPaymentStrictReceiveOperation.Builder(
                sendAsset, sendMax, destination.getAccountId(), destAsset, destAmount)
            .setSourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    PathPaymentStrictReceiveOperation parsedOperation =
        (PathPaymentStrictReceiveOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);

    assertEquals(
        1000L, xdr.getBody().getPathPaymentStrictReceiveOp().getSendMax().getInt64().longValue());
    assertEquals(
        1000L,
        xdr.getBody().getPathPaymentStrictReceiveOp().getDestAmount().getInt64().longValue());
    assertTrue(parsedOperation.getSendAsset() instanceof AssetTypeNative);
    assertEquals(source.getAccountId(), parsedOperation.getSourceAccount());
    assertEquals(destination.getAccountId(), parsedOperation.getDestination());
    assertEquals(sendMax, parsedOperation.getSendMax());
    assertTrue(parsedOperation.getDestAsset() instanceof AssetTypeCreditAlphaNum4);
    assertEquals(destAmount, parsedOperation.getDestAmount());
    assertEquals(0, parsedOperation.getPath().length);

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAAAIAAAAAAAAAAAAAA+gAAAAA7eBSYbzcL5UKo7oXO24y1ckX+XuCtkDsyNHOp1n1bxAAAAABVVNEAAAAAACNlYd30HdCuLI54eyYjyX/fDyH9IJWIr/hKDcXKQbq1QAAAAAAAAPoAAAAAA==",
        operation.toXdrBase64(AccountConverter.enableMuxed()));
  }

  @Test
  public void testMuxedPathPaymentStrictReceiveOperation()
      throws FormatException, IOException, AssetCodeLengthInvalidException {
    String source = "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVAAAAAAAAAAAAAJLK";
    String destination = "MDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKAAAAAAMV7V2XYGQO";

    // GCGZLB3X2B3UFOFSHHQ6ZGEPEX7XYPEH6SBFMIV74EUDOFZJA3VNL6X4
    KeyPair issuer =
        KeyPair.fromSecretSeed("SBOBVZUN6WKVMI6KIL2GHBBEETEV6XKQGILITNH6LO6ZA22DBMSDCPAG");

    Asset sendAsset = new AssetTypeNative();
    String sendMax = "0.0001";
    Asset destAsset = new AssetTypeCreditAlphaNum4("USD", issuer.getAccountId());
    String destAmount = "0.0001";

    PathPaymentStrictReceiveOperation operation =
        new PathPaymentStrictReceiveOperation.Builder(
                sendAsset, sendMax, destination, destAsset, destAmount)
            .setSourceAccount(source)
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    PathPaymentStrictReceiveOperation parsedOperation =
        (PathPaymentStrictReceiveOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);

    assertEquals(destination, parsedOperation.getDestination());
    assertEquals(source, parsedOperation.getSourceAccount());

    parsedOperation =
        (PathPaymentStrictReceiveOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);
    assertEquals(
        "MDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKAAAAAAMV7V2XYGQO",
        parsedOperation.getDestination());
    assertEquals(
        "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVAAAAAAAAAAAAAJLK",
        parsedOperation.getSourceAccount());
  }

  @Test
  public void testPathPaymentStrictSendOperation()
      throws FormatException, IOException, AssetCodeLengthInvalidException {
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
        new PathPaymentStrictSendOperation.Builder(
                sendAsset, sendAmount, destination.getAccountId(), destAsset, destMin)
            .setPath(path)
            .setSourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    PathPaymentStrictSendOperation parsedOperation =
        (PathPaymentStrictSendOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);

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
    assertEquals(Lists.newArrayList(path), Lists.newArrayList(parsedOperation.getPath()));

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAAA0AAAAAAAAAAAAAA+gAAAAA7eBSYbzcL5UKo7oXO24y1ckX+XuCtkDsyNHOp1n1bxAAAAABVVNEAAAAAACNlYd30HdCuLI54eyYjyX/fDyH9IJWIr/hKDcXKQbq1QAAAAAAACMoAAAAAgAAAAFVU0QAAAAAACoIKnpnw8rtrfxa276dFZo1C19mDqWXtG4ufhWrLUd1AAAAAlRFU1RURVNUAAAAAAAAAABE/ttVl8BLV0csW/xgXtbXOVf1lMyDluMiafl0IDVFIg==",
        operation.toXdrBase64(AccountConverter.enableMuxed()));
  }

  @Test
  public void testPathPaymentStrictSendEmptyPathOperation()
      throws FormatException, IOException, AssetCodeLengthInvalidException {
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
        new PathPaymentStrictSendOperation.Builder(
                sendAsset, sendAmount, destination.getAccountId(), destAsset, destMin)
            .setSourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    PathPaymentStrictSendOperation parsedOperation =
        (PathPaymentStrictSendOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);

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
        operation.toXdrBase64(AccountConverter.enableMuxed()));
  }

  @Test
  public void testMuxedPathPaymentStrictSendOperation()
      throws FormatException, IOException, AssetCodeLengthInvalidException {
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
        new PathPaymentStrictSendOperation.Builder(
                sendAsset, sendAmount, destination, destAsset, destMin)
            .setSourceAccount(source)
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    PathPaymentStrictSendOperation parsedOperation =
        (PathPaymentStrictSendOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);

    assertEquals(destination, parsedOperation.getDestination());
    assertEquals(source, parsedOperation.getSourceAccount());

    parsedOperation =
        (PathPaymentStrictSendOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);
    assertEquals(
        "MDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKAAAAAAMV7V2XYGQO",
        parsedOperation.getDestination());
    assertEquals(
        "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVAAAAAAAAAAAAAJLK",
        parsedOperation.getSourceAccount());
  }

  @Test
  public void testChangeTrustOperation() throws FormatException, IOException {
    // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
    KeyPair source =
        KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");

    ChangeTrustAsset asset = ChangeTrustAsset.create(new AssetTypeNative());
    String limit = "922337203685.4775807";

    ChangeTrustOperation operation =
        new ChangeTrustOperation.Builder(asset, limit)
            .setSourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    ChangeTrustOperation parsedOperation =
        (ChangeTrustOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);

    assertEquals(
        9223372036854775807L, xdr.getBody().getChangeTrustOp().getLimit().getInt64().longValue());
    assertEquals(source.getAccountId(), parsedOperation.getSourceAccount());
    assertEquals("native", parsedOperation.getAsset().getType());
    assertEquals(limit, parsedOperation.getLimit());

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAAAYAAAAAf/////////8=",
        operation.toXdrBase64(AccountConverter.enableMuxed()));
  }

  @Test
  public void testAllowTrustOperation() throws IOException, FormatException {
    // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
    KeyPair source =
        KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");
    // GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR
    KeyPair trustor =
        KeyPair.fromSecretSeed("SDHZGHURAYXKU2KMVHPOXI6JG2Q4BSQUQCEOY72O3QQTCLR2T455PMII");

    String assetCode = "USDA";
    boolean authorize = true;

    AllowTrustOperation operation =
        new AllowTrustOperation.Builder(trustor.getAccountId(), assetCode, authorize)
            .setSourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    AllowTrustOperation parsedOperation =
        (AllowTrustOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);

    assertEquals(source.getAccountId(), parsedOperation.getSourceAccount());
    assertEquals(trustor.getAccountId(), parsedOperation.getTrustor());
    assertEquals(assetCode, parsedOperation.getAssetCode());
    assertEquals(authorize, parsedOperation.getAuthorize());

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAAAcAAAAA7eBSYbzcL5UKo7oXO24y1ckX+XuCtkDsyNHOp1n1bxAAAAABVVNEQQAAAAE=",
        operation.toXdrBase64(AccountConverter.enableMuxed()));
  }

  @Test
  public void testAllowTrustOperationAssetCodeBuffer() throws IOException, FormatException {
    // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
    KeyPair source =
        KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");
    // GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR
    KeyPair trustor =
        KeyPair.fromSecretSeed("SDHZGHURAYXKU2KMVHPOXI6JG2Q4BSQUQCEOY72O3QQTCLR2T455PMII");

    String assetCode = "USDABC";
    boolean authorize = true;

    AllowTrustOperation operation =
        new AllowTrustOperation.Builder(trustor.getAccountId(), assetCode, authorize)
            .setSourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    AllowTrustOperation parsedOperation =
        (AllowTrustOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);

    assertEquals(assetCode, parsedOperation.getAssetCode());
  }

  @Test
  public void testSetOptionsOperation() throws FormatException {
    // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
    KeyPair source =
        KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");
    // GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR
    KeyPair inflationDestination =
        KeyPair.fromSecretSeed("SDHZGHURAYXKU2KMVHPOXI6JG2Q4BSQUQCEOY72O3QQTCLR2T455PMII");
    // GBCP5W2VS7AEWV2HFRN7YYC623LTSV7VSTGIHFXDEJU7S5BAGVCSETRR
    SignerKey signer =
        Signer.ed25519PublicKey(
            KeyPair.fromSecretSeed("SA64U7C5C7BS5IHWEPA7YWFN3Z6FE5L6KAMYUIT4AQ7KVTVLD23C6HEZ"));

    Integer clearFlags = 1;
    Integer setFlags = 1;
    Integer masterKeyWeight = 1;
    Integer lowThreshold = 2;
    Integer mediumThreshold = 3;
    Integer highThreshold = 4;
    String homeDomain = "stellar.org";
    Integer signerWeight = 1;

    SetOptionsOperation operation =
        new SetOptionsOperation.Builder()
            .setInflationDestination(inflationDestination.getAccountId())
            .setClearFlags(clearFlags)
            .setSetFlags(setFlags)
            .setMasterKeyWeight(masterKeyWeight)
            .setLowThreshold(lowThreshold)
            .setMediumThreshold(mediumThreshold)
            .setHighThreshold(highThreshold)
            .setHomeDomain(homeDomain)
            .setSigner(signer, signerWeight)
            .setSourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    SetOptionsOperation parsedOperation =
        (SetOptionsOperation) SetOptionsOperation.fromXdr(AccountConverter.enableMuxed(), xdr);

    assertEquals(inflationDestination.getAccountId(), parsedOperation.getInflationDestination());
    assertEquals(clearFlags, parsedOperation.getClearFlags());
    assertEquals(setFlags, parsedOperation.getSetFlags());
    assertEquals(masterKeyWeight, parsedOperation.getMasterKeyWeight());
    assertEquals(lowThreshold, parsedOperation.getLowThreshold());
    assertEquals(mediumThreshold, parsedOperation.getMediumThreshold());
    assertEquals(highThreshold, parsedOperation.getHighThreshold());
    assertEquals(homeDomain, parsedOperation.getHomeDomain());
    assertEquals(
        signer.getDiscriminant().getValue(),
        parsedOperation.getSigner().getDiscriminant().getValue());
    assertEquals(
        signer.getEd25519().getUint256(), parsedOperation.getSigner().getEd25519().getUint256());
    assertEquals(signerWeight, parsedOperation.getSignerWeight());
    assertEquals(source.getAccountId(), parsedOperation.getSourceAccount());

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAAAUAAAABAAAAAO3gUmG83C+VCqO6FztuMtXJF/l7grZA7MjRzqdZ9W8QAAAAAQAAAAEAAAABAAAAAQAAAAEAAAABAAAAAQAAAAIAAAABAAAAAwAAAAEAAAAEAAAAAQAAAAtzdGVsbGFyLm9yZwAAAAABAAAAAET+21WXwEtXRyxb/GBe1tc5V/WUzIOW4yJp+XQgNUUiAAAAAQ==",
        operation.toXdrBase64(AccountConverter.enableMuxed()));
  }

  @Test
  public void testSetOptionsOperationSingleField() {
    // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
    KeyPair source =
        KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");

    String homeDomain = "stellar.org";

    SetOptionsOperation operation =
        new SetOptionsOperation.Builder()
            .setHomeDomain(homeDomain)
            .setSourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    SetOptionsOperation parsedOperation =
        (SetOptionsOperation) SetOptionsOperation.fromXdr(AccountConverter.enableMuxed(), xdr);

    assertEquals(null, parsedOperation.getInflationDestination());
    assertEquals(null, parsedOperation.getClearFlags());
    assertEquals(null, parsedOperation.getSetFlags());
    assertEquals(null, parsedOperation.getMasterKeyWeight());
    assertEquals(null, parsedOperation.getLowThreshold());
    assertEquals(null, parsedOperation.getMediumThreshold());
    assertEquals(null, parsedOperation.getHighThreshold());
    assertEquals(homeDomain, parsedOperation.getHomeDomain());
    assertEquals(null, parsedOperation.getSigner());
    assertEquals(null, parsedOperation.getSignerWeight());
    assertEquals(source.getAccountId(), parsedOperation.getSourceAccount());

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAAAUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAtzdGVsbGFyLm9yZwAAAAAA",
        operation.toXdrBase64(AccountConverter.enableMuxed()));
  }

  @Test
  public void testSetOptionsOperationSignerSha256() {
    // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
    KeyPair source =
        KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");

    byte[] preimage = "stellar.org".getBytes();
    byte[] hash = Util.hash(preimage);

    SetOptionsOperation operation =
        new SetOptionsOperation.Builder()
            .setSigner(Signer.sha256Hash(hash), 10)
            .setSourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    SetOptionsOperation parsedOperation =
        (SetOptionsOperation) SetOptionsOperation.fromXdr(AccountConverter.enableMuxed(), xdr);

    assertEquals(null, parsedOperation.getInflationDestination());
    assertEquals(null, parsedOperation.getClearFlags());
    assertEquals(null, parsedOperation.getSetFlags());
    assertEquals(null, parsedOperation.getMasterKeyWeight());
    assertEquals(null, parsedOperation.getLowThreshold());
    assertEquals(null, parsedOperation.getMediumThreshold());
    assertEquals(null, parsedOperation.getHighThreshold());
    assertEquals(null, parsedOperation.getHomeDomain());
    assertTrue(Arrays.equals(hash, parsedOperation.getSigner().getHashX().getUint256()));
    assertEquals(new Integer(10), parsedOperation.getSignerWeight());
    assertEquals(source.getAccountId(), parsedOperation.getSourceAccount());

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAAAUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAACbpRqMkaQAfCYSk/n3xIl4fCoHfKqxF34ht2iuvSYEJQAAAAK",
        operation.toXdrBase64(AccountConverter.enableMuxed()));
  }

  @Test
  public void testSetOptionsOperationPreAuthTxSigner() {
    // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    KeyPair destination =
        KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

    long sequenceNumber = 2908908335136768L;
    Account account = new Account(source.getAccountId(), sequenceNumber);
    Transaction transaction =
        new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.TESTNET)
            .addOperation(
                new CreateAccountOperation.Builder(destination.getAccountId(), "2000").build())
            .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE)
            .setBaseFee(Transaction.MIN_BASE_FEE)
            .build();

    // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
    KeyPair opSource =
        KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");

    SetOptionsOperation operation =
        new SetOptionsOperation.Builder()
            .setSigner(Signer.preAuthTx(transaction), 10)
            .setSourceAccount(opSource.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    SetOptionsOperation parsedOperation =
        (SetOptionsOperation) SetOptionsOperation.fromXdr(AccountConverter.enableMuxed(), xdr);

    assertEquals(operation.getInflationDestination(), parsedOperation.getInflationDestination());
    assertEquals(operation.getClearFlags(), parsedOperation.getClearFlags());
    assertEquals(operation.getSetFlags(), parsedOperation.getSetFlags());
    assertEquals(operation.getMasterKeyWeight(), parsedOperation.getMasterKeyWeight());
    assertEquals(operation.getLowThreshold(), parsedOperation.getLowThreshold());
    assertEquals(operation.getMediumThreshold(), parsedOperation.getMediumThreshold());
    assertEquals(operation.getHighThreshold(), parsedOperation.getHighThreshold());
    assertEquals(operation.getHomeDomain(), parsedOperation.getHomeDomain());
    assertTrue(
        Arrays.equals(transaction.hash(), parsedOperation.getSigner().getPreAuthTx().getUint256()));
    assertEquals(operation.getSignerWeight(), parsedOperation.getSignerWeight());
    assertEquals(operation.getSourceAccount(), parsedOperation.getSourceAccount());
  }

  @Test
  public void testManageSellOfferOperation() throws IOException, FormatException {
    // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
    KeyPair source =
        KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");
    // GBCP5W2VS7AEWV2HFRN7YYC623LTSV7VSTGIHFXDEJU7S5BAGVCSETRR
    KeyPair issuer =
        KeyPair.fromSecretSeed("SA64U7C5C7BS5IHWEPA7YWFN3Z6FE5L6KAMYUIT4AQ7KVTVLD23C6HEZ");

    Asset selling = new AssetTypeNative();
    Asset buying = create(null, "USD", issuer.getAccountId());
    String amount = "0.00001";
    String price = "0.85334384"; // n=5333399 d=6250000
    Price priceObj = Price.fromString(price);
    long offerId = 1;

    ManageSellOfferOperation operation =
        new ManageSellOfferOperation.Builder(selling, buying, amount, price)
            .setOfferId(offerId)
            .setSourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    ManageSellOfferOperation parsedOperation =
        (ManageSellOfferOperation)
            ManageSellOfferOperation.fromXdr(AccountConverter.enableMuxed(), xdr);

    assertEquals(100L, xdr.getBody().getManageSellOfferOp().getAmount().getInt64().longValue());
    assertTrue(parsedOperation.getSelling() instanceof AssetTypeNative);
    assertTrue(parsedOperation.getBuying() instanceof AssetTypeCreditAlphaNum4);
    assertTrue(parsedOperation.getBuying().equals(buying));
    assertEquals(amount, parsedOperation.getAmount());
    assertEquals(price, parsedOperation.getPrice());
    assertEquals(priceObj.getNumerator(), 5333399);
    assertEquals(priceObj.getDenominator(), 6250000);
    assertEquals(offerId, parsedOperation.getOfferId());

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAAAMAAAAAAAAAAVVTRAAAAAAARP7bVZfAS1dHLFv8YF7W1zlX9ZTMg5bjImn5dCA1RSIAAAAAAAAAZABRYZcAX14QAAAAAAAAAAE=",
        operation.toXdrBase64(AccountConverter.enableMuxed()));
  }

  @Test
  public void testManageBuyOfferOperation() throws IOException, FormatException {
    // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
    KeyPair source =
        KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");
    // GBCP5W2VS7AEWV2HFRN7YYC623LTSV7VSTGIHFXDEJU7S5BAGVCSETRR
    KeyPair issuer =
        KeyPair.fromSecretSeed("SA64U7C5C7BS5IHWEPA7YWFN3Z6FE5L6KAMYUIT4AQ7KVTVLD23C6HEZ");

    Asset selling = new AssetTypeNative();
    Asset buying = create(null, "USD", issuer.getAccountId());
    String amount = "0.00001";
    String price = "0.85334384"; // n=5333399 d=6250000
    Price priceObj = Price.fromString(price);
    long offerId = 1;

    ManageBuyOfferOperation operation =
        new ManageBuyOfferOperation.Builder(selling, buying, amount, price)
            .setOfferId(offerId)
            .setSourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    ManageBuyOfferOperation parsedOperation =
        (ManageBuyOfferOperation)
            ManageBuyOfferOperation.fromXdr(AccountConverter.enableMuxed(), xdr);

    assertEquals(100L, xdr.getBody().getManageBuyOfferOp().getBuyAmount().getInt64().longValue());
    assertTrue(parsedOperation.getSelling() instanceof AssetTypeNative);
    assertTrue(parsedOperation.getBuying() instanceof AssetTypeCreditAlphaNum4);
    assertTrue(parsedOperation.getBuying().equals(buying));
    assertEquals(amount, parsedOperation.getAmount());
    assertEquals(price, parsedOperation.getPrice());
    assertEquals(priceObj.getNumerator(), 5333399);
    assertEquals(priceObj.getDenominator(), 6250000);
    assertEquals(offerId, parsedOperation.getOfferId());

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAAAwAAAAAAAAAAVVTRAAAAAAARP7bVZfAS1dHLFv8YF7W1zlX9ZTMg5bjImn5dCA1RSIAAAAAAAAAZABRYZcAX14QAAAAAAAAAAE=",
        operation.toXdrBase64(AccountConverter.enableMuxed()));
  }

  @Test
  public void testManageSellOfferOperation_BadArithmeticRegression() throws IOException {
    // from https://github.com/stellar/java-stellar-sdk/issues/183

    String transactionEnvelopeToDecode =
        "AAAAAButy5zasS3DLZ5uFpZHL25aiHUfKRwdv1+3Wp12Ce7XAAAAZAEyGwYAAAAOAAAAAAAAAAAAAAABAAAAAQAAAAAbrcuc2rEtwy2ebhaWRy9uWoh1HykcHb9ft1qddgnu1wAAAAMAAAAAAAAAAUtJTgAAAAAARkrT28ebM6YQyhVZi1ttlwq/dk6ijTpyTNuHIMgUp+EAAAAAAAARPSfDKZ0AAv7oAAAAAAAAAAAAAAAAAAAAAXYJ7tcAAABAbE8rEoFt0Hcv41iwVCl74C1Hyr+Lj8ZyaYn7zTJhezClbc+pTW1KgYFIZOJiGVth2xFnBT1pMXuQkVdTlB3FCw==";
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] bytes = base64Encoding.decode(transactionEnvelopeToDecode);

    org.stellar.sdk.xdr.TransactionEnvelope transactionEnvelope =
        org.stellar.sdk.xdr.TransactionEnvelope.decode(
            new XdrDataInputStream(new ByteArrayInputStream(bytes)));
    assertEquals(1, transactionEnvelope.getV0().getTx().getOperations().length);

    ManageSellOfferOperation op =
        (ManageSellOfferOperation)
            Operation.fromXdr(
                AccountConverter.enableMuxed(),
                transactionEnvelope.getV0().getTx().getOperations()[0]);

    assertEquals("3397.893306099996", op.getPrice());
  }

  @Test
  public void testManageBuyOfferOperation_BadArithmeticRegression() throws IOException {
    // from https://github.com/stellar/java-stellar-sdk/issues/183

    String transactionEnvelopeToDecode =
        "AAAAAButy5zasS3DLZ5uFpZHL25aiHUfKRwdv1+3Wp12Ce7XAAAAZAEyGwYAAAAxAAAAAAAAAAAAAAABAAAAAQAAAAAbrcuc2rEtwy2ebhaWRy9uWoh1HykcHb9ft1qddgnu1wAAAAwAAAABS0lOAAAAAABGStPbx5szphDKFVmLW22XCr92TqKNOnJM24cgyBSn4QAAAAAAAAAAACNyOCfDKZ0AAv7oAAAAAAABv1IAAAAAAAAAAA==";
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] bytes = base64Encoding.decode(transactionEnvelopeToDecode);

    org.stellar.sdk.xdr.TransactionEnvelope transactionEnvelope =
        org.stellar.sdk.xdr.TransactionEnvelope.decode(
            new XdrDataInputStream(new ByteArrayInputStream(bytes)));
    assertEquals(1, transactionEnvelope.getV0().getTx().getOperations().length);

    ManageBuyOfferOperation op =
        (ManageBuyOfferOperation)
            Operation.fromXdr(
                AccountConverter.enableMuxed(),
                transactionEnvelope.getV0().getTx().getOperations()[0]);

    assertEquals("3397.893306099996", op.getPrice());
  }

  @Test
  public void testCreatePassiveSellOfferOperation() throws IOException, FormatException {
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
        new CreatePassiveSellOfferOperation.Builder(selling, buying, amount, price)
            .setSourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    CreatePassiveSellOfferOperation parsedOperation =
        (CreatePassiveSellOfferOperation)
            CreatePassiveSellOfferOperation.fromXdr(AccountConverter.enableMuxed(), xdr);

    assertEquals(
        100L, xdr.getBody().getCreatePassiveSellOfferOp().getAmount().getInt64().longValue());
    assertTrue(parsedOperation.getSelling() instanceof AssetTypeNative);
    assertTrue(parsedOperation.getBuying() instanceof AssetTypeCreditAlphaNum4);
    assertTrue(parsedOperation.getBuying().equals(buying));
    assertEquals(amount, parsedOperation.getAmount());
    assertEquals(price, parsedOperation.getPrice());
    assertEquals(priceObj.getNumerator(), 36731261);
    assertEquals(priceObj.getDenominator(), 12500000);

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAAAQAAAAAAAAAAVVTRAAAAAAARP7bVZfAS1dHLFv8YF7W1zlX9ZTMg5bjImn5dCA1RSIAAAAAAAAAZAIweX0Avrwg",
        operation.toXdrBase64(AccountConverter.enableMuxed()));
  }

  @Test
  public void testAccountMergeOperation() throws IOException, FormatException {
    // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
    KeyPair source =
        KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");
    // GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR
    KeyPair destination =
        KeyPair.fromSecretSeed("SDHZGHURAYXKU2KMVHPOXI6JG2Q4BSQUQCEOY72O3QQTCLR2T455PMII");

    AccountMergeOperation operation =
        new AccountMergeOperation.Builder(destination.getAccountId())
            .setSourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());

    AccountMergeOperation parsedOperation =
        (AccountMergeOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);

    assertEquals(destination.getAccountId(), parsedOperation.getDestination());

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAAAgAAAAA7eBSYbzcL5UKo7oXO24y1ckX+XuCtkDsyNHOp1n1bxA=",
        operation.toXdrBase64(AccountConverter.enableMuxed()));
  }

  @Test
  public void testMuxedAccountMergeOperation() throws IOException, FormatException {
    String source = "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVAAAAAAAAAAAAAJLK";
    String destination = "MDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKAAAAAAMV7V2XYGQO";

    AccountMergeOperation operation =
        new AccountMergeOperation.Builder(destination).setSourceAccount(source).build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());

    AccountMergeOperation parsedOperation =
        (AccountMergeOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);
    assertEquals(destination, parsedOperation.getDestination());
    assertEquals(source, parsedOperation.getSourceAccount());

    parsedOperation =
        (AccountMergeOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);
    assertEquals(
        "MDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKAAAAAAMV7V2XYGQO",
        parsedOperation.getDestination());
    assertEquals(
        "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVAAAAAAAAAAAAAJLK",
        parsedOperation.getSourceAccount());
  }

  @Test
  public void testManageDataOperation() throws IOException, FormatException {
    // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
    KeyPair source =
        KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");

    ManageDataOperation operation =
        new ManageDataOperation.Builder("test", new byte[] {0, 1, 2, 3, 4})
            .setSourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());

    ManageDataOperation parsedOperation =
        (ManageDataOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);

    assertEquals("test", parsedOperation.getName());
    assertTrue(Arrays.equals(new byte[] {0, 1, 2, 3, 4}, parsedOperation.getValue()));

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAAAoAAAAEdGVzdAAAAAEAAAAFAAECAwQAAAA=",
        operation.toXdrBase64(AccountConverter.enableMuxed()));
  }

  @Test
  public void testManageDataOperationEmptyValue() throws IOException, FormatException {
    // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
    KeyPair source =
        KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");

    ManageDataOperation operation =
        new ManageDataOperation.Builder("test", null)
            .setSourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());

    ManageDataOperation parsedOperation =
        (ManageDataOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);

    assertEquals("test", parsedOperation.getName());
    assertEquals(null, parsedOperation.getValue());

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAAAoAAAAEdGVzdAAAAAA=",
        operation.toXdrBase64(AccountConverter.enableMuxed()));
  }

  @Test
  public void testBumpSequence() throws IOException, FormatException {
    // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
    KeyPair source =
        KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");

    BumpSequenceOperation operation =
        new BumpSequenceOperation.Builder(156L).setSourceAccount(source.getAccountId()).build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());

    BumpSequenceOperation parsedOperation =
        (BumpSequenceOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);

    assertEquals(156L, parsedOperation.getBumpTo());

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAAAsAAAAAAAAAnA==",
        operation.toXdrBase64(AccountConverter.enableMuxed()));
  }

  @Test
  public void testToXdrAmount() {
    assertEquals(0L, Operation.toXdrAmount("0"));
    assertEquals(1L, Operation.toXdrAmount("0.0000001"));
    assertEquals(10000000L, Operation.toXdrAmount("1"));
    assertEquals(11234567L, Operation.toXdrAmount("1.1234567"));
    assertEquals(729912843007381L, Operation.toXdrAmount("72991284.3007381"));
    assertEquals(729912843007381L, Operation.toXdrAmount("72991284.30073810"));
    assertEquals(1014016711446800155L, Operation.toXdrAmount("101401671144.6800155"));
    assertEquals(9223372036854775807L, Operation.toXdrAmount("922337203685.4775807"));

    try {
      Operation.toXdrAmount("0.00000001");
      fail();
    } catch (ArithmeticException e) {
    } catch (Exception e) {
      fail();
    }

    try {
      Operation.toXdrAmount("72991284.30073811");
      fail();
    } catch (ArithmeticException e) {
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testFromXdrAmount() {
    assertEquals("0", Operation.fromXdrAmount(0L));
    assertEquals("0.0000001", Operation.fromXdrAmount(1L));
    assertEquals("1", Operation.fromXdrAmount(10000000L));
    assertEquals("1.1234567", Operation.fromXdrAmount(11234567L));
    assertEquals("72991284.3007381", Operation.fromXdrAmount(729912843007381L));
    assertEquals("101401671144.6800155", Operation.fromXdrAmount(1014016711446800155L));
    assertEquals("922337203685.4775807", Operation.fromXdrAmount(9223372036854775807L));
  }

  @Test
  public void testInflationOperation() {
    // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
    KeyPair source =
        KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");

    InflationOperation operation = new InflationOperation();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    InflationOperation parsedOperation =
        (InflationOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);

    assertEquals("AAAAAAAAAAk=", operation.toXdrBase64(AccountConverter.enableMuxed()));

    operation.setSourceAccount(source.getAccountId());

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAAAk=",
        operation.toXdrBase64(AccountConverter.enableMuxed()));
  }

  @Test
  public void testClaimClaimableBalanceOperationValid() {
    String balanceId = "000000006d6a0c142516a9cc7885a85c5aba3a1f4af5181cf9e7a809ac7ae5e4a58c825f";
    String accountId = "GABTTS6N4CT7AUN4LD7IFIUMRD5PSMCW6QTLIQNEFZDEI6ZQVUCQMCLN";
    ClaimClaimableBalanceOperation operation =
        new ClaimClaimableBalanceOperation.Builder(balanceId).setSourceAccount(accountId).build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    ClaimClaimableBalanceOperation parsedOperation =
        (ClaimClaimableBalanceOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);
    assertEquals(accountId, parsedOperation.getSourceAccount());
    assertEquals(balanceId, parsedOperation.getBalanceId());
    // Generated by js-stellar-base.
    assertEquals(
        "AAAAAQAAAAADOcvN4KfwUbxY/oKijIj6+TBW9Ca0QaQuRkR7MK0FBgAAAA8AAAAAbWoMFCUWqcx4hahcWro6H0r1GBz556gJrHrl5KWMgl8=",
        operation.toXdrBase64(AccountConverter.enableMuxed()));
  }

  @Test
  public void testClaimClaimableBalanceOperationInvalidEmptyBalanceId() {
    String balanceId = "";
    String accountId = "GABTTS6N4CT7AUN4LD7IFIUMRD5PSMCW6QTLIQNEFZDEI6ZQVUCQMCLN";
    ClaimClaimableBalanceOperation operation =
        new ClaimClaimableBalanceOperation.Builder(balanceId).setSourceAccount(accountId).build();
    try {
      operation.toXdr(AccountConverter.enableMuxed());
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("invalid balanceId: ", e.getMessage());
    }
  }

  @Test
  public void testClaimClaimableBalanceOperationInvalidClaimableBalanceIDTypeMissing() {
    String balanceId = "6d6a0c142516a9cc7885a85c5aba3a1f4af5181cf9e7a809ac7ae5e4a58c825f";
    String accountId = "GABTTS6N4CT7AUN4LD7IFIUMRD5PSMCW6QTLIQNEFZDEI6ZQVUCQMCLN";
    ClaimClaimableBalanceOperation operation =
        new ClaimClaimableBalanceOperation.Builder(balanceId).setSourceAccount(accountId).build();
    try {
      operation.toXdr(AccountConverter.enableMuxed());
      fail();
    } catch (RuntimeException ignored) {
    }
  }

  @Test
  public void testClaimClaimableBalanceOperationInvalidClaimableBalanceIDBodyMissing() {
    String balanceId = "00000000";
    String accountId = "GABTTS6N4CT7AUN4LD7IFIUMRD5PSMCW6QTLIQNEFZDEI6ZQVUCQMCLN";
    ClaimClaimableBalanceOperation operation =
        new ClaimClaimableBalanceOperation.Builder(balanceId).setSourceAccount(accountId).build();
    try {
      operation.toXdr(AccountConverter.enableMuxed());
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("invalid balanceId: " + balanceId, e.getMessage());
    }
  }

  @Test
  public void testRevokeAccountSponsorshipOperation() {
    String source = "GA2N7NI5WEMJILMK4UPDTF2ZX2BIRQUM3HZUE27TRUNRFN5M5EXU6RQV";
    String accountId = "GAGQ7DNQUVQR6OWYOI563L5EMJE6KCAHPQSFCZFLY5PDRYMRCA5UWCMP";
    RevokeAccountSponsorshipOperation operation =
        new RevokeAccountSponsorshipOperation.Builder(accountId).setSourceAccount(source).build();
    assertEquals(
        "AAAAAQAAAAA037UdsRiULYrlHjmXWb6CiMKM2fNCa/ONGxK3rOkvTwAAABIAAAAAAAAAAAAAAAAND42wpWEfOthyO+2vpGJJ5QgHfCRRZKvHXjjhkRA7Sw==",
        operation.toXdrBase64(AccountConverter.enableMuxed()));

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    RevokeAccountSponsorshipOperation parsedOperation =
        (RevokeAccountSponsorshipOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);
    assertEquals(accountId, parsedOperation.getAccountId());
    assertEquals(source, parsedOperation.getSourceAccount());
  }

  @Test
  public void testRevokeDataSponsorshipOperation() {
    String source = "GA2N7NI5WEMJILMK4UPDTF2ZX2BIRQUM3HZUE27TRUNRFN5M5EXU6RQV";
    String accountId = "GAGQ7DNQUVQR6OWYOI563L5EMJE6KCAHPQSFCZFLY5PDRYMRCA5UWCMP";
    String dataName = "data_name";
    RevokeDataSponsorshipOperation operation =
        new RevokeDataSponsorshipOperation.Builder(accountId, dataName)
            .setSourceAccount(source)
            .build();
    assertEquals(
        "AAAAAQAAAAA037UdsRiULYrlHjmXWb6CiMKM2fNCa/ONGxK3rOkvTwAAABIAAAAAAAAAAwAAAAAND42wpWEfOthyO+2vpGJJ5QgHfCRRZKvHXjjhkRA7SwAAAAlkYXRhX25hbWUAAAA=",
        operation.toXdrBase64(AccountConverter.enableMuxed()));

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    RevokeDataSponsorshipOperation parsedOperation =
        (RevokeDataSponsorshipOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);
    assertEquals(accountId, parsedOperation.getAccountId());
    assertEquals(source, parsedOperation.getSourceAccount());
    assertEquals(dataName, parsedOperation.getDataName());
  }

  @Test
  public void testRevokeClaimableBalanceSponsorshipOperation() {
    String source = "GA2N7NI5WEMJILMK4UPDTF2ZX2BIRQUM3HZUE27TRUNRFN5M5EXU6RQV";
    String balanceId = "00000000550e14acbdafcd3089289363b3b0c8bec9b4edd87298c690655b4b2456d68ba0";
    RevokeClaimableBalanceSponsorshipOperation operation =
        new RevokeClaimableBalanceSponsorshipOperation.Builder(balanceId)
            .setSourceAccount(source)
            .build();
    assertEquals(
        "AAAAAQAAAAA037UdsRiULYrlHjmXWb6CiMKM2fNCa/ONGxK3rOkvTwAAABIAAAAAAAAABAAAAABVDhSsva/NMIkok2OzsMi+ybTt2HKYxpBlW0skVtaLoA==",
        operation.toXdrBase64(AccountConverter.enableMuxed()));

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    RevokeClaimableBalanceSponsorshipOperation parsedOperation =
        (RevokeClaimableBalanceSponsorshipOperation)
            Operation.fromXdr(AccountConverter.enableMuxed(), xdr);
    assertEquals(balanceId, parsedOperation.getBalanceId());
    assertEquals(source, parsedOperation.getSourceAccount());
  }

  @Test
  public void testRevokeOfferSponsorshipOperation() {
    String source = "GA2N7NI5WEMJILMK4UPDTF2ZX2BIRQUM3HZUE27TRUNRFN5M5EXU6RQV";
    String seller = "GAGQ7DNQUVQR6OWYOI563L5EMJE6KCAHPQSFCZFLY5PDRYMRCA5UWCMP";
    Long offerId = 123456L;
    RevokeOfferSponsorshipOperation operation =
        new RevokeOfferSponsorshipOperation.Builder(seller, offerId)
            .setSourceAccount(source)
            .build();
    assertEquals(
        "AAAAAQAAAAA037UdsRiULYrlHjmXWb6CiMKM2fNCa/ONGxK3rOkvTwAAABIAAAAAAAAAAgAAAAAND42wpWEfOthyO+2vpGJJ5QgHfCRRZKvHXjjhkRA7SwAAAAAAAeJA",
        operation.toXdrBase64(AccountConverter.enableMuxed()));

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    RevokeOfferSponsorshipOperation parsedOperation =
        (RevokeOfferSponsorshipOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);
    assertEquals(offerId, parsedOperation.getOfferId());
    assertEquals(seller, parsedOperation.getSeller());
    assertEquals(source, parsedOperation.getSourceAccount());
  }

  @Test
  public void testRevokeSignerSponsorshipOperation() {
    String source = "GA2N7NI5WEMJILMK4UPDTF2ZX2BIRQUM3HZUE27TRUNRFN5M5EXU6RQV";
    String accountId = "GAGQ7DNQUVQR6OWYOI563L5EMJE6KCAHPQSFCZFLY5PDRYMRCA5UWCMP";
    SignerKey signerKey =
        KeyPair.fromAccountId("GBOSQJIV4VJMWMPVPB7EFVIRJT7A7SAAAB4FA23ZDJRUMXMYHBYWY57L")
            .getXdrSignerKey();
    RevokeSignerSponsorshipOperation operation =
        new RevokeSignerSponsorshipOperation.Builder(accountId, signerKey)
            .setSourceAccount(source)
            .build();
    assertEquals(
        "AAAAAQAAAAA037UdsRiULYrlHjmXWb6CiMKM2fNCa/ONGxK3rOkvTwAAABIAAAABAAAAAA0PjbClYR862HI77a+kYknlCAd8JFFkq8deOOGREDtLAAAAAF0oJRXlUssx9Xh+QtURTP4PyAAAeFBreRpjRl2YOHFs",
        operation.toXdrBase64(AccountConverter.enableMuxed()));

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    RevokeSignerSponsorshipOperation parsedOperation =
        (RevokeSignerSponsorshipOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);
    assertEquals(accountId, parsedOperation.getAccountId());
    assertEquals(source, parsedOperation.getSourceAccount());
    assertEquals(signerKey, parsedOperation.getSigner());
  }

  @Test
  public void testRevokeTrustlineSponsorshipOperation() {
    String source = "GA2N7NI5WEMJILMK4UPDTF2ZX2BIRQUM3HZUE27TRUNRFN5M5EXU6RQV";
    String accountId = "GAGQ7DNQUVQR6OWYOI563L5EMJE6KCAHPQSFCZFLY5PDRYMRCA5UWCMP";
    TrustLineAsset asset =
        TrustLineAsset.createNonNativeAsset(
            "DEMO", "GCWPICV6IV35FQ2MVZSEDLORHEMMIAODRQPVDEIKZOW2GC2JGGDCXVVV");
    RevokeTrustlineSponsorshipOperation operation =
        new RevokeTrustlineSponsorshipOperation.Builder(accountId, asset)
            .setSourceAccount(source)
            .build();
    assertEquals(
        "AAAAAQAAAAA037UdsRiULYrlHjmXWb6CiMKM2fNCa/ONGxK3rOkvTwAAABIAAAAAAAAAAQAAAAAND42wpWEfOthyO+2vpGJJ5QgHfCRRZKvHXjjhkRA7SwAAAAFERU1PAAAAAKz0Cr5Fd9LDTK5kQa3RORjEAcOMH1GRCsutowtJMYYr",
        operation.toXdrBase64(AccountConverter.enableMuxed()));

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    RevokeTrustlineSponsorshipOperation parsedOperation =
        (RevokeTrustlineSponsorshipOperation)
            Operation.fromXdr(AccountConverter.enableMuxed(), xdr);
    assertEquals(accountId, parsedOperation.getAccountId());
    assertEquals(source, parsedOperation.getSourceAccount());
    assertEquals(asset, parsedOperation.getAsset());
  }

  @Test
  public void testClawbackClaimableBalanceOperation() {
    String source = "GA2N7NI5WEMJILMK4UPDTF2ZX2BIRQUM3HZUE27TRUNRFN5M5EXU6RQV";
    String balanceId = "00000000929b20b72e5890ab51c24f1cc46fa01c4f318d8d33367d24dd614cfdf5491072";
    ClawbackClaimableBalanceOperation operation =
        new ClawbackClaimableBalanceOperation.Builder(balanceId).setSourceAccount(source).build();
    assertEquals(
        "AAAAAQAAAAA037UdsRiULYrlHjmXWb6CiMKM2fNCa/ONGxK3rOkvTwAAABQAAAAAkpsgty5YkKtRwk8cxG+gHE8xjY0zNn0k3WFM/fVJEHI=",
        operation.toXdrBase64(AccountConverter.enableMuxed()));

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    ClawbackClaimableBalanceOperation parsedOperation =
        (ClawbackClaimableBalanceOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);
    assertEquals(balanceId, parsedOperation.getBalanceId());
    assertEquals(source, parsedOperation.getSourceAccount());
    assertEquals(operation, parsedOperation);
  }

  @Test
  public void testClawbackOperation() {
    String source = "GA2N7NI5WEMJILMK4UPDTF2ZX2BIRQUM3HZUE27TRUNRFN5M5EXU6RQV";
    String accountId = "GAGQ7DNQUVQR6OWYOI563L5EMJE6KCAHPQSFCZFLY5PDRYMRCA5UWCMP";
    Asset asset =
        new AssetTypeCreditAlphaNum4(
            "DEMO", "GCWPICV6IV35FQ2MVZSEDLORHEMMIAODRQPVDEIKZOW2GC2JGGDCXVVV");
    String amt = "100";
    ClawbackOperation operation =
        new ClawbackOperation.Builder(accountId, asset, amt).setSourceAccount(source).build();
    assertEquals(
        "AAAAAQAAAAA037UdsRiULYrlHjmXWb6CiMKM2fNCa/ONGxK3rOkvTwAAABMAAAABREVNTwAAAACs9Aq+RXfSw0yuZEGt0TkYxAHDjB9RkQrLraMLSTGGKwAAAAAND42wpWEfOthyO+2vpGJJ5QgHfCRRZKvHXjjhkRA7SwAAAAA7msoA",
        operation.toXdrBase64(AccountConverter.enableMuxed()));

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    ClawbackOperation parsedOperation =
        (ClawbackOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);
    assertEquals(accountId, parsedOperation.getFrom());
    assertEquals(asset, parsedOperation.getAsset());
    assertEquals(amt, parsedOperation.getAmount());

    assertEquals(source, parsedOperation.getSourceAccount());
    assertEquals(operation, parsedOperation);
  }

  @Test
  public void testMuxedClawbackOperation() throws IOException, FormatException {
    String source = "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVAAAAAAAAAAAAAJLK";
    String from = "MDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKAAAAAAMV7V2XYGQO";

    Asset asset =
        new AssetTypeCreditAlphaNum4(
            "DEMO", "GCWPICV6IV35FQ2MVZSEDLORHEMMIAODRQPVDEIKZOW2GC2JGGDCXVVV");
    String amt = "100";
    ClawbackOperation operation =
        new ClawbackOperation.Builder(from, asset, amt).setSourceAccount(source).build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    ClawbackOperation parsedOperation =
        (ClawbackOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);

    assertEquals(from, parsedOperation.getFrom());
    assertEquals(source, parsedOperation.getSourceAccount());

    parsedOperation = (ClawbackOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);
    assertEquals(
        "MDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKAAAAAAMV7V2XYGQO",
        parsedOperation.getFrom());
    assertEquals(
        "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVAAAAAAAAAAAAAJLK",
        parsedOperation.getSourceAccount());
  }

  @Test
  public void testMixedMuxedClawbackOperation() throws IOException, FormatException {
    String source = "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVAAAAAAAAAAAAAJLK";
    String from = "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3";

    Asset asset =
        new AssetTypeCreditAlphaNum4(
            "DEMO", "GCWPICV6IV35FQ2MVZSEDLORHEMMIAODRQPVDEIKZOW2GC2JGGDCXVVV");
    String amt = "100";
    ClawbackOperation operation =
        new ClawbackOperation.Builder(from, asset, amt).setSourceAccount(source).build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    ClawbackOperation parsedOperation =
        (ClawbackOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);

    assertEquals(from, parsedOperation.getFrom());
    assertEquals(source, parsedOperation.getSourceAccount());

    parsedOperation = (ClawbackOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);
    assertEquals(from, parsedOperation.getFrom());
    assertEquals(
        "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVAAAAAAAAAAAAAJLK",
        parsedOperation.getSourceAccount());
  }

  @Test
  public void testCantClawbackNativeAsset() {
    try {
      String accountId = "GAGQ7DNQUVQR6OWYOI563L5EMJE6KCAHPQSFCZFLY5PDRYMRCA5UWCMP";
      String amt = "100";
      new ClawbackOperation.Builder(accountId, create("native"), amt);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("native assets are not supported", e.getMessage());
    }
  }

  @Test
  public void testSetTrustlineFlagsOperation() {
    String source = "GA2N7NI5WEMJILMK4UPDTF2ZX2BIRQUM3HZUE27TRUNRFN5M5EXU6RQV";
    String accountId = "GAGQ7DNQUVQR6OWYOI563L5EMJE6KCAHPQSFCZFLY5PDRYMRCA5UWCMP";
    Asset asset =
        new AssetTypeCreditAlphaNum4(
            "DEMO", "GCWPICV6IV35FQ2MVZSEDLORHEMMIAODRQPVDEIKZOW2GC2JGGDCXVVV");
    EnumSet<TrustLineFlags> toClear = EnumSet.of(TrustLineFlags.AUTHORIZED_FLAG);
    EnumSet<TrustLineFlags> toSet =
        EnumSet.of(
            TrustLineFlags.AUTHORIZED_TO_MAINTAIN_LIABILITIES_FLAG,
            TrustLineFlags.TRUSTLINE_CLAWBACK_ENABLED_FLAG);

    SetTrustlineFlagsOperation operation =
        new SetTrustlineFlagsOperation.Builder(accountId, asset, toClear, toSet)
            .setSourceAccount(source)
            .build();
    assertEquals(
        "AAAAAQAAAAA037UdsRiULYrlHjmXWb6CiMKM2fNCa/ONGxK3rOkvTwAAABUAAAAADQ+NsKVhHzrYcjvtr6RiSeUIB3wkUWSrx1444ZEQO0sAAAABREVNTwAAAACs9Aq+RXfSw0yuZEGt0TkYxAHDjB9RkQrLraMLSTGGKwAAAAEAAAAG",
        operation.toXdrBase64(AccountConverter.enableMuxed()));

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    assertEquals(
        TrustLineFlags.AUTHORIZED_FLAG.getValue(),
        xdr.getBody().getSetTrustLineFlagsOp().getClearFlags().getUint32().intValue());
    assertEquals(
        TrustLineFlags.AUTHORIZED_TO_MAINTAIN_LIABILITIES_FLAG.getValue()
            | TrustLineFlags.TRUSTLINE_CLAWBACK_ENABLED_FLAG.getValue(),
        xdr.getBody().getSetTrustLineFlagsOp().getSetFlags().getUint32().intValue());
    SetTrustlineFlagsOperation parsedOperation =
        (SetTrustlineFlagsOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);
    assertEquals(accountId, parsedOperation.getTrustor());
    assertEquals(asset, parsedOperation.getAsset());
    assertEquals(toClear, parsedOperation.getClearFlags());
    assertEquals(toSet, parsedOperation.getSetFlags());

    assertEquals(source, parsedOperation.getSourceAccount());
    assertEquals(operation, parsedOperation);
  }

  @Test
  public void testCantSetNativeTrustlineFlags() {
    try {
      String source = "GA2N7NI5WEMJILMK4UPDTF2ZX2BIRQUM3HZUE27TRUNRFN5M5EXU6RQV";
      String accountId = "GAGQ7DNQUVQR6OWYOI563L5EMJE6KCAHPQSFCZFLY5PDRYMRCA5UWCMP";
      EnumSet<TrustLineFlags> toClear = EnumSet.of(TrustLineFlags.AUTHORIZED_FLAG);
      EnumSet<TrustLineFlags> toSet =
          EnumSet.of(TrustLineFlags.AUTHORIZED_TO_MAINTAIN_LIABILITIES_FLAG);

      new SetTrustlineFlagsOperation.Builder(accountId, create("native"), toClear, toSet)
          .setSourceAccount(source)
          .build();
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("native assets are not supported", e.getMessage());
    }
  }
}
