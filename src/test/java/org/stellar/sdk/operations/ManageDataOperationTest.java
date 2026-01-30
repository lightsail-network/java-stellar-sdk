package org.stellar.sdk.operations;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;
import org.stellar.sdk.KeyPair;

public class ManageDataOperationTest {
  @Test
  public void testManageDataOperation() {
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
  public void testManageDataOperationEmptyValue() {
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

  @Test
  public void testManageDataOperationValueExceeds64Bytes() {
    byte[] value = new byte[65];
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> ManageDataOperation.builder().name("test").value(value).build());
    assertEquals("value cannot exceed 64 bytes", exception.getMessage());
  }

  @Test
  public void testManageDataOperationValueExactly64Bytes() {
    byte[] value = new byte[64];
    for (int i = 0; i < 64; i++) {
      value[i] = (byte) i;
    }
    ManageDataOperation operation = ManageDataOperation.builder().name("test").value(value).build();
    assertArrayEquals(value, operation.getValue());
  }

  @Test
  public void testManageDataOperationNameExceeds64Bytes() {
    String name = "12345678901234567890123456789012345678901234567890123456789012345";
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class, () -> ManageDataOperation.builder().name(name).build());
    assertEquals("name cannot exceed 64 bytes", exception.getMessage());
  }

  @Test
  public void testManageDataOperationNameExactly64Bytes() {
    String name = "1234567890123456789012345678901234567890123456789012345678901234";
    ManageDataOperation operation = ManageDataOperation.builder().name(name).build();
    assertEquals(name, operation.getName());
  }
}
