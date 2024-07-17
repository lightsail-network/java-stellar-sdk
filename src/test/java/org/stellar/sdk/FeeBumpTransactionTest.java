package org.stellar.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.stellar.sdk.operations.PaymentOperation;
import org.stellar.sdk.xdr.EnvelopeType;

public class FeeBumpTransactionTest {

  private Transaction createInnerTransaction(int baseFee, Network network) {
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");

    Account account = new Account(source.getAccountId(), 2908908335136768L);
    Transaction inner =
        new TransactionBuilder(account, network)
            .addOperation(
                PaymentOperation.builder()
                    .destination("GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVSGZ")
                    .asset(new AssetTypeNative())
                    .amount("200")
                    .build())
            .setBaseFee(baseFee)
            .addPreconditions(
                TransactionPreconditions.builder().timeBounds(new TimeBounds(10, 11)).build())
            .build();

    inner.sign(source);
    return inner;
  }

  private Transaction createInnerTransaction() {
    return createInnerTransaction(Transaction.MIN_BASE_FEE, Network.TESTNET);
  }

  private Transaction createInnerTransaction(int baseFee) {
    return createInnerTransaction(baseFee, Network.TESTNET);
  }

  private Transaction createInnerTransaction(Network network) {
    return createInnerTransaction(Transaction.MIN_BASE_FEE, network);
  }

  @Test
  public void testSetBaseFeeBelowNetworkMinimum() {
    Transaction inner = createInnerTransaction();

    try {
      FeeBumpTransaction.createWithBaseFee(
          "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3",
          Transaction.MIN_BASE_FEE - 1,
          inner);
      fail();
    } catch (RuntimeException e) {
      assertEquals("baseFee cannot be smaller than the BASE_FEE (100): 99", e.getMessage());
    }
  }

  @Test
  public void testSetBaseFeeBelowInner() {
    Transaction inner = createInnerTransaction(Transaction.MIN_BASE_FEE + 1);

    try {
      FeeBumpTransaction.createWithBaseFee(
          "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3",
          Transaction.MIN_BASE_FEE,
          inner);
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
      FeeBumpTransaction.createWithBaseFee(
          "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3", Long.MAX_VALUE, inner);
      fail();
    } catch (RuntimeException e) {
      assertEquals("fee overflows 64 bit int", e.getMessage());
    }
  }

  @Test
  public void testSetBaseFeeEqualToInner() {
    Transaction inner = createInnerTransaction();

    FeeBumpTransaction feeBump =
        FeeBumpTransaction.createWithBaseFee(
            "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3",
            Transaction.MIN_BASE_FEE,
            inner);

    assertEquals(Transaction.MIN_BASE_FEE * 2, feeBump.getFee());
  }

  @Test
  public void testHash() {
    Transaction inner = createInnerTransaction();
    assertEquals(
        "2a8ead3351faa7797b284f59027355ddd69c21adb8e4da0b9bb95531f7f32681", inner.hashHex());

    FeeBumpTransaction feeBump =
        FeeBumpTransaction.createWithBaseFee(
            "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3",
            Transaction.MIN_BASE_FEE * 2,
            inner);
    assertEquals(
        "58266712c0c1d1cd98faa0e0159605a361cf2a5ca44ad69650eeb1d27ee62334", feeBump.hashHex());
  }

  @Test
  public void testRoundTripXdr() {
    Transaction inner = createInnerTransaction();

    FeeBumpTransaction feeBump =
        FeeBumpTransaction.createWithBaseFee(
            "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3",
            Transaction.MIN_BASE_FEE * 2,
            inner);

    assertEquals(Transaction.MIN_BASE_FEE * 4, feeBump.getFee());
    assertEquals(
        "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3", feeBump.getFeeSource());
    assertEquals(inner, feeBump.getInnerTransaction());
    assertEquals(0, feeBump.getSignatures().size());

    FeeBumpTransaction fromXdr =
        (FeeBumpTransaction)
            AbstractTransaction.fromEnvelopeXdr(feeBump.toEnvelopeXdrBase64(), Network.TESTNET);

    assertEquals(feeBump, fromXdr);

    KeyPair signer = KeyPair.random();
    feeBump.sign(signer);
    fromXdr =
        (FeeBumpTransaction)
            AbstractTransaction.fromEnvelopeXdr(feeBump.toEnvelopeXdrBase64(), Network.TESTNET);
    assertEquals(feeBump, fromXdr);
    assertEquals(inner, fromXdr.getInnerTransaction());

    assertEquals(1, feeBump.getSignatures().size());
    signer.verify(feeBump.hash(), feeBump.getSignatures().get(0).getSignature().getSignature());
  }

  @Test
  public void testFeeBumpUpgradesInnerToV1() {
    Transaction innerV0 = createInnerTransaction();
    innerV0.setEnvelopeType(EnvelopeType.ENVELOPE_TYPE_TX_V0);

    FeeBumpTransaction feeBump =
        FeeBumpTransaction.createWithBaseFee(
            "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3",
            Transaction.MIN_BASE_FEE * 2,
            innerV0);

    assertEquals(Transaction.MIN_BASE_FEE * 4, feeBump.getFee());
    assertEquals(
        "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3", feeBump.getFeeSource());
    assertEquals(0, feeBump.getSignatures().size());

    assertEquals(EnvelopeType.ENVELOPE_TYPE_TX_V0, innerV0.toEnvelopeXdr().getDiscriminant());
    innerV0.setEnvelopeType(EnvelopeType.ENVELOPE_TYPE_TX);
    assertEquals(innerV0, feeBump.getInnerTransaction());

    FeeBumpTransaction fromXdr =
        (FeeBumpTransaction)
            AbstractTransaction.fromEnvelopeXdr(feeBump.toEnvelopeXdrBase64(), Network.TESTNET);

    assertEquals(feeBump, fromXdr);

    KeyPair signer = KeyPair.random();
    feeBump.sign(signer);
    fromXdr =
        (FeeBumpTransaction)
            AbstractTransaction.fromEnvelopeXdr(feeBump.toEnvelopeXdrBase64(), Network.TESTNET);
    assertEquals(feeBump, fromXdr);
    assertEquals(feeBump.getInnerTransaction(), fromXdr.getInnerTransaction());

    assertEquals(1, feeBump.getSignatures().size());
    signer.verify(feeBump.hash(), feeBump.getSignatures().get(0).getSignature().getSignature());
  }

  @Test
  public void testHashCodeAndEquals() {
    Transaction inner = createInnerTransaction();
    FeeBumpTransaction feeBump0 =
        FeeBumpTransaction.createWithBaseFee(
            "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3",
            Transaction.MIN_BASE_FEE * 2,
            inner);

    // they get different base fee
    FeeBumpTransaction feeBump2 =
        FeeBumpTransaction.createWithBaseFee(
            "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3",
            Transaction.MIN_BASE_FEE * 3,
            createInnerTransaction(Network.PUBLIC));
    System.out.println(feeBump2.toEnvelopeXdr());
    assertNotEquals(feeBump0, feeBump2);

    // they get different network
    FeeBumpTransaction feeBump3 =
        FeeBumpTransaction.createWithBaseFee(
            "GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3",
            Transaction.MIN_BASE_FEE * 2,
            createInnerTransaction(Network.PUBLIC));

    assertNotEquals(feeBump0, feeBump3);
  }
}
