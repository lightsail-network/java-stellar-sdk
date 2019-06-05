package org.stellar.sdk;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class AccountTest  {
  @Test
  public void testNullArguments() {
    assertThrows(NullPointerException.class, () -> new Account(null, 10L));

    assertThrows(NullPointerException.class, () -> new Account(KeyPair.random(), null));
  }

  @Test
  public void testGetIncrementedSequenceNumber() {
    Account account = new Account(KeyPair.random(), 100L);
    Long incremented;
    incremented = account.getIncrementedSequenceNumber();
    assertEquals(new Long(100L), account.getSequenceNumber());
    assertEquals(new Long(101L), incremented);
    incremented = account.getIncrementedSequenceNumber();
    assertEquals(new Long(100L), account.getSequenceNumber());
    assertEquals(new Long(101L), incremented);
  }

  @Test
  public void testIncrementSequenceNumber() {
    Account account = new Account(KeyPair.random(), 100L);
    account.incrementSequenceNumber();
    assertEquals(account.getSequenceNumber(), new Long(101L));
  }

  @Test
  public void testGetters() {
    KeyPair keypair = KeyPair.random();
    Account account = new Account(keypair, 100L);
    assertEquals(account.getKeypair().getAccountId(), keypair.getAccountId());
    assertEquals(account.getSequenceNumber(), new Long(100L));
  }
}