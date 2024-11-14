package org.stellar.sdk.operations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.stellar.sdk.Asset.create;

import java.math.BigDecimal;
import org.junit.Test;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeCreditAlphaNum4;

public class ClawbackOperationTest {
  @Test
  public void testClawbackOperation() {
    String source = "GA2N7NI5WEMJILMK4UPDTF2ZX2BIRQUM3HZUE27TRUNRFN5M5EXU6RQV";
    String accountId = "GAGQ7DNQUVQR6OWYOI563L5EMJE6KCAHPQSFCZFLY5PDRYMRCA5UWCMP";
    Asset asset =
        new AssetTypeCreditAlphaNum4(
            "DEMO", "GCWPICV6IV35FQ2MVZSEDLORHEMMIAODRQPVDEIKZOW2GC2JGGDCXVVV");
    BigDecimal amt = BigDecimal.valueOf(100);
    BigDecimal formattedAmt = new BigDecimal("100.0000000");
    ClawbackOperation operation =
        ClawbackOperation.builder()
            .from(accountId)
            .asset(asset)
            .amount(amt)
            .sourceAccount(source)
            .build();
    assertEquals(
        "AAAAAQAAAAA037UdsRiULYrlHjmXWb6CiMKM2fNCa/ONGxK3rOkvTwAAABMAAAABREVNTwAAAACs9Aq+RXfSw0yuZEGt0TkYxAHDjB9RkQrLraMLSTGGKwAAAAAND42wpWEfOthyO+2vpGJJ5QgHfCRRZKvHXjjhkRA7SwAAAAA7msoA",
        operation.toXdrBase64());

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr();
    ClawbackOperation parsedOperation = (ClawbackOperation) Operation.fromXdr(xdr);
    assertEquals(accountId, parsedOperation.getFrom());
    assertEquals(asset, parsedOperation.getAsset());
    assertEquals(formattedAmt, parsedOperation.getAmount());

    assertEquals(source, parsedOperation.getSourceAccount());
    assertEquals(operation, parsedOperation);
  }

  @Test
  public void testMuxedClawbackOperation() {
    String source = "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVAAAAAAAAAAAAAJLK";
    String from = "MDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKAAAAAAMV7V2XYGQO";

    Asset asset =
        new AssetTypeCreditAlphaNum4(
            "DEMO", "GCWPICV6IV35FQ2MVZSEDLORHEMMIAODRQPVDEIKZOW2GC2JGGDCXVVV");
    BigDecimal amt = BigDecimal.valueOf(100);
    ClawbackOperation operation =
        ClawbackOperation.builder()
            .from(from)
            .asset(asset)
            .amount(amt)
            .sourceAccount(source)
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr();
    ClawbackOperation parsedOperation = (ClawbackOperation) Operation.fromXdr(xdr);

    assertEquals(from, parsedOperation.getFrom());
    assertEquals(source, parsedOperation.getSourceAccount());

    parsedOperation = (ClawbackOperation) Operation.fromXdr(xdr);
    assertEquals(
        "MDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKAAAAAAMV7V2XYGQO",
        parsedOperation.getFrom());
    assertEquals(
        "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVAAAAAAAAAAAAAJLK",
        parsedOperation.getSourceAccount());
  }

  @Test
  public void testMixedMuxedClawbackOperation() {
    String source = "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVAAAAAAAAAAAAAJLK";
    String from = "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3";

    Asset asset =
        new AssetTypeCreditAlphaNum4(
            "DEMO", "GCWPICV6IV35FQ2MVZSEDLORHEMMIAODRQPVDEIKZOW2GC2JGGDCXVVV");
    BigDecimal amt = BigDecimal.valueOf(100);
    ClawbackOperation operation =
        ClawbackOperation.builder()
            .from(from)
            .asset(asset)
            .amount(amt)
            .sourceAccount(source)
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr();
    ClawbackOperation parsedOperation = (ClawbackOperation) Operation.fromXdr(xdr);

    assertEquals(from, parsedOperation.getFrom());
    assertEquals(source, parsedOperation.getSourceAccount());

    parsedOperation = (ClawbackOperation) Operation.fromXdr(xdr);
    assertEquals(from, parsedOperation.getFrom());
    assertEquals(
        "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVAAAAAAAAAAAAAJLK",
        parsedOperation.getSourceAccount());
  }

  @Test
  public void testCantClawbackNativeAsset() {
    try {
      String accountId = "GAGQ7DNQUVQR6OWYOI563L5EMJE6KCAHPQSFCZFLY5PDRYMRCA5UWCMP";
      BigDecimal amt = BigDecimal.valueOf(100);
      ClawbackOperation.builder().from(accountId).asset(create("native")).amount(amt).build();
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("native assets are not supported", e.getMessage());
    }
  }
}
