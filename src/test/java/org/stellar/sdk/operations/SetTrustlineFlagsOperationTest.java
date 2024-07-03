package org.stellar.sdk.operations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.stellar.sdk.Asset.create;

import java.util.EnumSet;
import org.junit.Test;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeCreditAlphaNum4;
import org.stellar.sdk.xdr.TrustLineFlags;

public class SetTrustlineFlagsOperationTest {

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
        SetTrustlineFlagsOperation.builder()
            .trustor(accountId)
            .asset(asset)
            .setFlags(toSet)
            .clearFlags(toClear)
            .sourceAccount(source)
            .build();
    assertEquals(
        "AAAAAQAAAAA037UdsRiULYrlHjmXWb6CiMKM2fNCa/ONGxK3rOkvTwAAABUAAAAADQ+NsKVhHzrYcjvtr6RiSeUIB3wkUWSrx1444ZEQO0sAAAABREVNTwAAAACs9Aq+RXfSw0yuZEGt0TkYxAHDjB9RkQrLraMLSTGGKwAAAAEAAAAG",
        operation.toXdrBase64());

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr();
    assertEquals(
        TrustLineFlags.AUTHORIZED_FLAG.getValue(),
        xdr.getBody().getSetTrustLineFlagsOp().getClearFlags().getUint32().getNumber().intValue());
    assertEquals(
        TrustLineFlags.AUTHORIZED_TO_MAINTAIN_LIABILITIES_FLAG.getValue()
            | TrustLineFlags.TRUSTLINE_CLAWBACK_ENABLED_FLAG.getValue(),
        xdr.getBody().getSetTrustLineFlagsOp().getSetFlags().getUint32().getNumber().intValue());
    SetTrustlineFlagsOperation parsedOperation =
        (SetTrustlineFlagsOperation) Operation.fromXdr(xdr);
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

      SetTrustlineFlagsOperation.builder()
          .trustor(accountId)
          .asset(create("native"))
          .setFlags(toSet)
          .clearFlags(toClear)
          .sourceAccount(source)
          .build();
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("native assets are not supported", e.getMessage());
    }
  }
}
