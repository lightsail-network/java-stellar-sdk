package org.stellar.sdk.operations;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class ClawbackClaimableBalanceOperationTest {

  @Test
  public void testBuilder() {
    String source = "GA2N7NI5WEMJILMK4UPDTF2ZX2BIRQUM3HZUE27TRUNRFN5M5EXU6RQV";
    String balanceId = "00000000929b20b72e5890ab51c24f1cc46fa01c4f318d8d33367d24dd614cfdf5491072";
    ClawbackClaimableBalanceOperation op =
        ClawbackClaimableBalanceOperation.builder()
            .balanceId(balanceId)
            .sourceAccount(source)
            .build();
    TestCase.assertEquals(balanceId, op.getBalanceId());
    TestCase.assertEquals(source, op.getSourceAccount());
    String expectXdr =
        "AAAAAQAAAAA037UdsRiULYrlHjmXWb6CiMKM2fNCa/ONGxK3rOkvTwAAABQAAAAAkpsgty5YkKtRwk8cxG+gHE8xjY0zNn0k3WFM/fVJEHI=";
    TestCase.assertEquals(expectXdr, op.toXdrBase64());
  }

  @Test
  public void testBuilder_UpperBalanceId() {
    String source = "GA2N7NI5WEMJILMK4UPDTF2ZX2BIRQUM3HZUE27TRUNRFN5M5EXU6RQV";
    String balanceId = "00000000929b20b72e5890ab51c24f1cc46fa01c4f318d8d33367d24dd614cfdf5491072";
    ClawbackClaimableBalanceOperation op =
        ClawbackClaimableBalanceOperation.builder()
            .balanceId(balanceId.toUpperCase())
            .sourceAccount(source)
            .build();
    TestCase.assertEquals(balanceId, op.getBalanceId());
    TestCase.assertEquals(source, op.getSourceAccount());
    String expectXdr =
        "AAAAAQAAAAA037UdsRiULYrlHjmXWb6CiMKM2fNCa/ONGxK3rOkvTwAAABQAAAAAkpsgty5YkKtRwk8cxG+gHE8xjY0zNn0k3WFM/fVJEHI=";
    TestCase.assertEquals(expectXdr, op.toXdrBase64());
  }

  @Test
  public void testFromXdr() {
    String source = "GA2N7NI5WEMJILMK4UPDTF2ZX2BIRQUM3HZUE27TRUNRFN5M5EXU6RQV";
    String balanceId = "00000000929b20b72e5890ab51c24f1cc46fa01c4f318d8d33367d24dd614cfdf5491072";
    ClawbackClaimableBalanceOperation op =
        ClawbackClaimableBalanceOperation.builder()
            .balanceId(balanceId)
            .sourceAccount(source)
            .build();
    org.stellar.sdk.xdr.Operation xdrObject = op.toXdr();
    ClawbackClaimableBalanceOperation restoreOp =
        (ClawbackClaimableBalanceOperation) Operation.fromXdr(xdrObject);
    Assert.assertEquals(restoreOp, op);
  }
}
