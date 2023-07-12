package org.stellar.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class AccountTest {
  @Test
  public void testNullArguments() {
    try {
      new Account(null, 10L);
      fail();
    } catch (NullPointerException e) {
    }

    try {
      KeyPair random = KeyPair.random();
      new Account(random.getAccountId(), null);
      fail();
    } catch (NullPointerException e) {
    }
  }

  @Test
  public void testGetIncrementedSequenceNumber() {
    KeyPair random = KeyPair.random();
    Account account = new Account(random.getAccountId(), 100L);
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
    KeyPair random = KeyPair.random();
    Account account = new Account(random.getAccountId(), 100L);
    account.incrementSequenceNumber();
    assertEquals(account.getSequenceNumber(), new Long(101L));
  }

  @Test
  public void testGetters() {
    KeyPair keypair = KeyPair.random();
    Account account = new Account(keypair.getAccountId(), 100L);
    assertEquals(account.getKeyPair().getAccountId(), keypair.getAccountId());
    assertEquals(account.getAccountId(), keypair.getAccountId());
    assertEquals(account.getSequenceNumber(), new Long(100L));
  }
}
