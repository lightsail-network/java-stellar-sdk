package org.stellar.sdk;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AccountFlagTest {
  @Test
  public void testValues() {
    assertEquals(1, AccountFlag.AUTH_REQUIRED_FLAG.getValue());
    assertEquals(2, AccountFlag.AUTH_REVOCABLE_FLAG.getValue());
    assertEquals(4, AccountFlag.AUTH_IMMUTABLE_FLAG.getValue());
  }
}
