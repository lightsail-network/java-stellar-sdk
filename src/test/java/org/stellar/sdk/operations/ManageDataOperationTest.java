package org.stellar.sdk.operations;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import org.junit.Test;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.exception.StrKeyException;

public class ManageDataOperationTest {
  @Test
  public void testManageDataOperation() throws IOException, StrKeyException {
    // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
    KeyPair source =
        KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");

    ManageDataOperation operation =
        ManageDataOperation.builder()
            .name("test")
            .value(new byte[] {0, 1, 2, 3, 4})
            .sourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr();

    ManageDataOperation parsedOperation = (ManageDataOperation) Operation.fromXdr(xdr);

    assertEquals("test", parsedOperation.getName());
    assertArrayEquals(new byte[] {0, 1, 2, 3, 4}, parsedOperation.getValue());

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAAAoAAAAEdGVzdAAAAAEAAAAFAAECAwQAAAA=",
        operation.toXdrBase64());
  }

  @Test
  public void testManageDataOperationEmptyValue() throws IOException, StrKeyException {
    // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
    KeyPair source =
        KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");

    ManageDataOperation operation =
        ManageDataOperation.builder()
            .name("test")
            .value(null)
            .sourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr();

    ManageDataOperation parsedOperation = (ManageDataOperation) Operation.fromXdr(xdr);

    assertEquals("test", parsedOperation.getName());
    assertNull(parsedOperation.getValue());

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAAAoAAAAEdGVzdAAAAAA=",
        operation.toXdrBase64());
  }
}
