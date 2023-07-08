package org.stellar.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import org.junit.Test;
import org.stellar.sdk.xdr.EnvelopeType;

public class FeeBumpTransactionTest {

  private Transaction createInnerTransaction(int baseFee) {
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");

    Account account = new Account(source.getAccountId(), 2908908335136768L);
    Transaction inner =
        new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.TESTNET)
            .addOperation(
                new PaymentOperation.Builder(
                        "GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVSGZ",
                        new AssetTypeNative(),
                        "200")
                    .build())
            .setBaseFee(baseFee)
            .addTimeBounds(new TimeBounds(10, 11))
            .build();

    inner.sign(source);
    return inner;
  }

  private Transaction createInnerTransaction() {
    return createInnerTransaction(Transaction.MIN_BASE_FEE);
  }

  @Test
  public void testRequiresFeeAccount() {
    Transaction inner = createInnerTransaction();

    try {
      new FeeBumpTransaction.Builder(inner).setBaseFee(Transaction.MIN_BASE_FEE * 2).build();
      fail();
    } catch (RuntimeException e) {
      assertEquals("fee account has to be set. you must call setFeeAccount().", e.getMessage());
    }
  }

  @Test
  public void testSetFeeAccountMultipleTimes() {
    Transaction inner = createInnerTransaction();

    try {
      new FeeBumpTransaction.Builder(inner)
          .setBaseFee(Transaction.MIN_BASE_FEE * 2)
          .setFeeAccount("GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3")
          .setFeeAccount("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR")
          .build();
      fail();
    } catch (RuntimeException e) {
      assertEquals("fee account has been already been set.", e.getMessage());
    }
  }

  @Test
  public void testRequiresBaseFee() {
    Transaction inner = createInnerTransaction();

    try {
      new FeeBumpTransaction.Builder(inner)
          .setFeeAccount("GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3")
          .build();
      fail();
    } catch (RuntimeException e) {
      assertEquals("base fee has to be set. you must call setBaseFee().", e.getMessage());
    }
  }

  @Test
  public void testSetBaseFeeMultipleTimes() {
    Transaction inner = createInnerTransaction();

    try {
      new FeeBumpTransaction.Builder(inner)
          .setBaseFee(Transaction.MIN_BASE_FEE * 2)
          .setFeeAccount("GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3")
          .setBaseFee(Transaction.MIN_BASE_FEE)
          .build();
      fail();
    } catch (RuntimeException e) {
      assertEquals("base fee has been already set.", e.getMessage());
    }
  }

  @Test
  public void testSetBaseFeeBelowNetworkMinimum() {
    Transaction inner = createInnerTransaction();

    try {
      new FeeBumpTransaction.Builder(inner)
          .setBaseFee(Transaction.MIN_BASE_FEE - 1)
          .setFeeAccount("GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3")
          .build();
      fail();
    } catch (RuntimeException e) {
      assertEquals("baseFee cannot be smaller than the BASE_FEE (100): 99", e.getMessage());
    }
  }

  @Test
  public void testSetBaseFeeBelowInner() {
    Transaction inner = createInnerTransaction(Transaction.MIN_BASE_FEE + 1);

    try {
      new FeeBumpTransaction.Builder(inner)
          .setBaseFee(Transaction.MIN_BASE_FEE)
          .setFeeAccount("GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3")
          .build();
      fail();
    } catch (RuntimeException e) {
      assertEquals(
          "base fee cannot be lower than provided inner transaction base fee", e.getMessage());
    }
  }

  @Test
  public void testSetBaseFeeOverflowsLong() {
    Transaction inner = createInnerTransaction(Transaction.MIN_BASE_FEE + 1);

    try {
      new FeeBumpTransaction.Builder(inner)
          .setBaseFee(Long.MAX_VALUE)
          .setFeeAccount("GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3")
          .build();
      fail();
    } catch (RuntimeException e) {
      assertEquals("fee overflows 64 bit int", e.getMessage());
    }
  }

  @Test
  public void testSetBaseFeeEqualToInner() {
    Transaction inner = createInnerTransaction();

    FeeBumpTransaction feeBump =
        new FeeBumpTransaction.Builder(inner)
            .setBaseFee(Transaction.MIN_BASE_FEE)
            .setFeeAccount("GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3")
            .build();

    assertEquals(Transaction.MIN_BASE_FEE * 2, feeBump.getFee());
  }

  @Test
  public void testHash() {
    Transaction inner = createInnerTransaction();
    assertEquals(
        "2a8ead3351faa7797b284f59027355ddd69c21adb8e4da0b9bb95531f7f32681", inner.hashHex());

    FeeBumpTransaction feeBump =
        new FeeBumpTransaction.Builder(inner)
            .setBaseFee(Transaction.MIN_BASE_FEE * 2)
            .setFeeAccount("GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3")
            .build();

    assertEquals(
        "58266712c0c1d1cd98faa0e0159605a361cf2a5ca44ad69650eeb1d27ee62334", feeBump.hashHex());
  }

  @Test
  public void testRoundTripXdr() throws IOException {
    Transaction inner = createInnerTransaction();

    FeeBumpTransaction feeBump =
        new FeeBumpTransaction.Builder(inner)
            .setBaseFee(Transaction.MIN_BASE_FEE * 2)
            .setFeeAccount("GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3")
            .build();

    assertEquals(Transaction.MIN_BASE_FEE * 4, feeBump.getFee());
    assertEquals(
        "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3", feeBump.getFeeAccount());
    assertEquals(inner, feeBump.getInnerTransaction());
    assertEquals(0, feeBump.getSignatures().size());

    FeeBumpTransaction fromXdr =
        (FeeBumpTransaction)
            AbstractTransaction.fromEnvelopeXdr(
                AccountConverter.enableMuxed(), feeBump.toEnvelopeXdrBase64(), Network.TESTNET);

    assertEquals(feeBump, fromXdr);

    KeyPair signer = KeyPair.random();
    feeBump.sign(signer);
    fromXdr =
        (FeeBumpTransaction)
            AbstractTransaction.fromEnvelopeXdr(
                AccountConverter.enableMuxed(), feeBump.toEnvelopeXdrBase64(), Network.TESTNET);
    assertEquals(feeBump, fromXdr);
    assertEquals(inner, fromXdr.getInnerTransaction());

    assertEquals(1, feeBump.getSignatures().size());
    signer.verify(feeBump.hash(), feeBump.getSignatures().get(0).getSignature().getSignature());
  }

  @Test
  public void testFeeBumpUpgradesInnerToV1() throws IOException {
    Transaction innerV0 = createInnerTransaction();
    innerV0.setEnvelopeType(EnvelopeType.ENVELOPE_TYPE_TX_V0);

    FeeBumpTransaction feeBump =
        new FeeBumpTransaction.Builder(innerV0)
            .setBaseFee(Transaction.MIN_BASE_FEE * 2)
            .setFeeAccount("GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3")
            .build();

    assertEquals(Transaction.MIN_BASE_FEE * 4, feeBump.getFee());
    assertEquals(
        "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3", feeBump.getFeeAccount());
    assertEquals(0, feeBump.getSignatures().size());

    assertEquals(EnvelopeType.ENVELOPE_TYPE_TX_V0, innerV0.toEnvelopeXdr().getDiscriminant());
    assertNotEquals(innerV0, feeBump.getInnerTransaction());
    innerV0.setEnvelopeType(EnvelopeType.ENVELOPE_TYPE_TX);
    assertEquals(innerV0, feeBump.getInnerTransaction());

    FeeBumpTransaction fromXdr =
        (FeeBumpTransaction)
            AbstractTransaction.fromEnvelopeXdr(
                AccountConverter.enableMuxed(), feeBump.toEnvelopeXdrBase64(), Network.TESTNET);

    assertEquals(feeBump, fromXdr);

    KeyPair signer = KeyPair.random();
    feeBump.sign(signer);
    fromXdr =
        (FeeBumpTransaction)
            AbstractTransaction.fromEnvelopeXdr(
                AccountConverter.enableMuxed(), feeBump.toEnvelopeXdrBase64(), Network.TESTNET);
    assertEquals(feeBump, fromXdr);
    assertEquals(feeBump.getInnerTransaction(), fromXdr.getInnerTransaction());

    assertEquals(1, feeBump.getSignatures().size());
    signer.verify(feeBump.hash(), feeBump.getSignatures().get(0).getSignature().getSignature());
  }
}
