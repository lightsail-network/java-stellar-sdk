package org.stellar.sdk;

import org.junit.Test;
import org.stellar.sdk.xdr.EnvelopeType;

import java.io.IOException;

import static org.junit.Assert.*;

public class FeeBumpTransactionTest {

  private Transaction createInnerTransaction(int baseFee) {
    KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");

    Account account = new Account(source.getAccountId(), 2908908335136768L);
    Transaction inner = new Transaction.Builder(account, Network.TESTNET)
        .addOperation(new PaymentOperation.Builder(
            "MCAAAAAAAAAAAAB7BQ2L7E5NBWMXDUCMZSIPOBKRDSBYVLMXGSSKF6YNPIB7Y77ITKNOG",
            new AssetTypeNative(),
            "200"
        ).build())
        .setBaseFee(baseFee)
        .addTimeBounds(new TimeBounds(10, 11))
        .build();

    inner.setEnvelopeType(EnvelopeType.ENVELOPE_TYPE_TX);
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
      new FeeBumpTransaction.Builder(inner)
          .setBaseFee(Transaction.MIN_BASE_FEE * 2)
          .build();
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
          .setBaseFee(Transaction.MIN_BASE_FEE -1)
          .setFeeAccount("GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3")
          .build();
      fail();
    } catch (RuntimeException e) {
      assertEquals("baseFee cannot be smaller than the BASE_FEE (100): 99", e.getMessage());
    }
  }

  @Test
  public void testSetBaseFeeBelowInner() {
    Transaction inner = createInnerTransaction(Transaction.MIN_BASE_FEE+1);

    try {
      new FeeBumpTransaction.Builder(inner)
          .setBaseFee(Transaction.MIN_BASE_FEE)
          .setFeeAccount("GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3")
          .build();
      fail();
    } catch (RuntimeException e) {
      assertEquals("base fee cannot be lower than provided inner transaction base fee", e.getMessage());
    }
  }


  @Test
  public void testSetBaseFeeOverflowsLong() {
    Transaction inner = createInnerTransaction(Transaction.MIN_BASE_FEE+1);

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

    FeeBumpTransaction feeBump = new FeeBumpTransaction.Builder(inner)
        .setBaseFee(Transaction.MIN_BASE_FEE)
        .setFeeAccount("GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3")
        .build();

    assertEquals(Transaction.MIN_BASE_FEE*2, feeBump.getFee());
  }

  @Test
  public void testHash() {
    Transaction inner = createInnerTransaction();
    assertEquals("95dcf35a43a1a05bcd50f3eb148b31127829a9460dc32a17c4a7f7c4677409d4", inner.hashHex());

    FeeBumpTransaction feeBump = new FeeBumpTransaction.Builder(inner)
        .setBaseFee(Transaction.MIN_BASE_FEE * 2)
        .setFeeAccount("GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3")
        .build();

    assertEquals("382b1588ee8b315177a34ae96ebcaeb81c0ad3e04fee7c6b5a583b826517e1e4", feeBump.hashHex());
  }

  @Test
  public void testRoundTripXdr() throws IOException {
    Transaction inner = createInnerTransaction();

    FeeBumpTransaction feeBump = new FeeBumpTransaction.Builder(inner)
        .setBaseFee(Transaction.MIN_BASE_FEE * 2)
        .setFeeAccount("GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3")
        .build();

    assertEquals(Transaction.MIN_BASE_FEE * 4, feeBump.getFee());
    assertEquals("GDQNY3PBOJOKYZSRMK2S7LHHGWZIUISD4QORETLMXEWXBI7KFZZMKTL3", feeBump.getFeeAccount());
    assertEquals(inner, feeBump.getInnerTransaction());
    assertEquals(0, feeBump.getSignatures().size());

    FeeBumpTransaction fromXdr = (FeeBumpTransaction) AbstractTransaction.fromEnvelopeXdr(
        feeBump.toEnvelopeXdrBase64(), Network.TESTNET
    );

    assertEquals(feeBump, fromXdr);


    KeyPair signer = KeyPair.random();
    feeBump.sign(signer);
    fromXdr = (FeeBumpTransaction) AbstractTransaction.fromEnvelopeXdr(
        feeBump.toEnvelopeXdrBase64(), Network.TESTNET
    );
    assertEquals(feeBump, fromXdr);

    assertEquals(1, feeBump.getSignatures().size());
    signer.verify(feeBump.hash(), feeBump.getSignatures().get(0).getSignature().getSignature());
  }
}
