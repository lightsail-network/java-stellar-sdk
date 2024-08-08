package org.stellar.sdk;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;
import org.stellar.sdk.exception.StrKeyException;
import org.stellar.sdk.operations.CreateAccountOperation;
import org.stellar.sdk.operations.InvokeHostFunctionOperation;
import org.stellar.sdk.xdr.*;

public class TransactionBuilderTest {

  @Test
  public void testMissingOperationFee() {
    long sequenceNumber = 2908908335136768L;
    Account account =
        new Account("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR", sequenceNumber);
    try {
      new TransactionBuilder(account, Network.TESTNET)
          .addOperation(
              CreateAccountOperation.builder()
                  .destination("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR")
                  .startingBalance(BigDecimal.valueOf(2000))
                  .build())
          .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE)
          .build();
      fail("expected RuntimeException");
    } catch (RuntimeException e) {
      // expected
    }
  }

  @Test
  public void testBuilderSuccessTestnet() throws Exception {
    // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    KeyPair destination =
        KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

    long sequenceNumber = 2908908335136768L;
    Account account = new Account(source.getAccountId(), sequenceNumber);
    Transaction transaction =
        new TransactionBuilder(account, Network.TESTNET)
            .addOperation(
                CreateAccountOperation.builder()
                    .destination(destination.getAccountId())
                    .startingBalance(BigDecimal.valueOf(2000))
                    .build())
            .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE)
            .setBaseFee(Transaction.MIN_BASE_FEE)
            .build();

    transaction.sign(source);

    assertEquals(transaction.getSourceAccount(), source.getAccountId());
    assertEquals(transaction.getSequenceNumber(), sequenceNumber + 1);
    assertEquals(transaction.getFee(), 100);

    assertEquals(
        "AAAAAgAAAABexSIg06FtXzmFBQQtHZsrnyWxUzmthkBEhs/ktoeVYgAAAGQAClWjAAAAAQAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAADt4FJhvNwvlQqjuhc7bjLVyRf5e4K2QOzI0c6nWfVvEAAAAASoF8gAAAAAAAAAAAG2h5ViAAAAQLJxvwao6eyNHaDX2QFhgdqlxJUqkpgA03UUOqf4DwOXSV9GN4ZWut2uzRuza4DWyVGBEHmmnQX+SQKFo0Sb/wA=",
        transaction.toEnvelopeXdrBase64());

    org.stellar.sdk.xdr.Transaction.TransactionExt expectedExt =
        org.stellar.sdk.xdr.Transaction.TransactionExt.builder().discriminant(0).build();
    assertEquals(expectedExt, transaction.toEnvelopeXdr().getV1().getTx().getExt());

    // Convert transaction to binary XDR and back again to make sure correctly xdr de/serialized.
    org.stellar.sdk.xdr.TransactionEnvelope decodedTransaction =
        org.stellar.sdk.xdr.TransactionEnvelope.fromXdrBase64(transaction.toEnvelopeXdrBase64());
    Transaction transaction2 =
        (Transaction) Transaction.fromEnvelopeXdr(decodedTransaction, Network.TESTNET);
    assertEquals(transaction, transaction2);
  }

  @Test
  public void testBuilderMemoText() throws IOException {
    // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    KeyPair destination =
        KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

    Account account = new Account(source.getAccountId(), 2908908335136768L);
    Transaction transaction =
        new TransactionBuilder(account, Network.TESTNET)
            .addOperation(
                CreateAccountOperation.builder()
                    .destination(destination.getAccountId())
                    .startingBalance(BigDecimal.valueOf(2000))
                    .build())
            .addMemo(Memo.text("Hello world!"))
            .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE)
            .setBaseFee(Transaction.MIN_BASE_FEE)
            .build();

    transaction.sign(source);

    // Convert transaction to binary XDR and back again to make sure correctly xdr de/serialized.
    org.stellar.sdk.xdr.TransactionEnvelope decodedTransaction =
        org.stellar.sdk.xdr.TransactionEnvelope.fromXdrBase64(transaction.toEnvelopeXdrBase64());
    Transaction transaction2 =
        (Transaction) Transaction.fromEnvelopeXdr(decodedTransaction, Network.TESTNET);
    assertEquals(transaction, transaction2);

    assertEquals(
        "AAAAAgAAAABexSIg06FtXzmFBQQtHZsrnyWxUzmthkBEhs/ktoeVYgAAAGQAClWjAAAAAQAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAxIZWxsbyB3b3JsZCEAAAABAAAAAAAAAAAAAAAA7eBSYbzcL5UKo7oXO24y1ckX+XuCtkDsyNHOp1n1bxAAAAAEqBfIAAAAAAAAAAABtoeVYgAAAEA2U3MBHRAxe/nYUTFsL14hgWxcq1Z0yFcEd2LSEt+TbXbVXHl77k/sjLdUGw/0qMZMOn2n50MY+w1pnx6mjfsM",
        transaction.toEnvelopeXdrBase64());
  }

  @Test
  public void testBuilderTimeBounds() throws IOException {
    // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    KeyPair destination =
        KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

    Account account = new Account(source.getAccountId(), 2908908335136768L);
    Transaction transaction =
        new TransactionBuilder(account, Network.TESTNET)
            .addOperation(
                CreateAccountOperation.builder()
                    .destination(destination.getAccountId())
                    .startingBalance(BigDecimal.valueOf(2000))
                    .build())
            .addPreconditions(
                TransactionPreconditions.builder().timeBounds(new TimeBounds(42, 1337)).build())
            .addMemo(Memo.hash(Util.hash("abcdef".getBytes())))
            .setBaseFee(Transaction.MIN_BASE_FEE)
            .build();

    transaction.sign(source);

    // Convert transaction to binary XDR and back again to make sure timebounds are correctly
    // de/serialized.
    org.stellar.sdk.xdr.TransactionEnvelope decodedTransaction =
        org.stellar.sdk.xdr.TransactionEnvelope.fromXdrBase64(transaction.toEnvelopeXdrBase64());

    assertEquals(
        decodedTransaction
            .getV1()
            .getTx()
            .getCond()
            .getTimeBounds()
            .getMinTime()
            .getTimePoint()
            .getUint64()
            .getNumber()
            .longValue(),
        42);
    assertEquals(
        decodedTransaction
            .getV1()
            .getTx()
            .getCond()
            .getTimeBounds()
            .getMaxTime()
            .getTimePoint()
            .getUint64()
            .getNumber()
            .longValue(),
        1337);

    Transaction transaction2 =
        (Transaction) Transaction.fromEnvelopeXdr(decodedTransaction, Network.TESTNET);
    assertEquals(transaction, transaction2);
  }

  @Test
  public void testBuilderBaseFee() throws IOException {
    // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    KeyPair destination =
        KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

    Account account = new Account(source.getAccountId(), 2908908335136768L);
    Transaction transaction =
        new TransactionBuilder(account, Network.TESTNET)
            .addOperation(
                CreateAccountOperation.builder()
                    .destination(destination.getAccountId())
                    .startingBalance(BigDecimal.valueOf(2000))
                    .build())
            .setBaseFee(200)
            .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE)
            .build();

    transaction.sign(source);

    // Convert transaction to binary XDR and back again to make sure correctly xdr de/serialized.
    org.stellar.sdk.xdr.TransactionEnvelope decodedTransaction =
        org.stellar.sdk.xdr.TransactionEnvelope.fromXdrBase64(transaction.toEnvelopeXdrBase64());
    Transaction transaction2 =
        (Transaction) Transaction.fromEnvelopeXdr(decodedTransaction, Network.TESTNET);
    assertEquals(transaction, transaction2);

    assertEquals(
        "AAAAAgAAAABexSIg06FtXzmFBQQtHZsrnyWxUzmthkBEhs/ktoeVYgAAAMgAClWjAAAAAQAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAADt4FJhvNwvlQqjuhc7bjLVyRf5e4K2QOzI0c6nWfVvEAAAAASoF8gAAAAAAAAAAAG2h5ViAAAAQAIeBLnUSyzqnFYWPm0Y06/v78VquGfjQokPrMBCeOZRM4WqPLOI5/Mgn1+djGFBOVCKB+HUevtqN2DMhnhYmg8=",
        transaction.toEnvelopeXdrBase64());
  }

  @Test
  public void testBuilderBaseFeeThrows() {
    // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");

    Account account = new Account(source.getAccountId(), 2908908335136768L);
    TransactionBuilder builder = new TransactionBuilder(account, Network.TESTNET);
    try {
      builder.setBaseFee(99);
      fail("expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test
  public void testBuilderTimebounds() throws IOException {
    Account account = new Account(KeyPair.random().getAccountId(), 2908908335136768L);
    Transaction transaction =
        new TransactionBuilder(account, Network.TESTNET)
            .addOperation(
                CreateAccountOperation.builder()
                    .destination(KeyPair.random().getAccountId())
                    .startingBalance(BigDecimal.valueOf(2000))
                    .build())
            .addPreconditions(
                TransactionPreconditions.builder().timeBounds(new TimeBounds(42, 1337)).build())
            .addMemo(Memo.hash(Util.hash("abcdef".getBytes())))
            .setBaseFee(Transaction.MIN_BASE_FEE)
            .build();

    assertEquals(42, transaction.getTimeBounds().getMinTime().intValue());
    assertEquals(1337, transaction.getTimeBounds().getMaxTime().intValue());

    // Convert transaction to binary XDR and back again to make sure correctly xdr de/serialized.
    org.stellar.sdk.xdr.TransactionEnvelope decodedTransaction =
        org.stellar.sdk.xdr.TransactionEnvelope.fromXdrBase64(transaction.toEnvelopeXdrBase64());
    Transaction transaction2 =
        (Transaction) Transaction.fromEnvelopeXdr(decodedTransaction, Network.TESTNET);
    assertEquals(transaction, transaction2);
  }

  @Test
  public void testBuilderRequiresTimeoutOrTimeBounds() {
    Account account = new Account(KeyPair.random().getAccountId(), 2908908335136768L);
    try {
      new TransactionBuilder(account, Network.TESTNET)
          .addOperation(
              CreateAccountOperation.builder()
                  .destination(KeyPair.random().getAccountId())
                  .startingBalance(BigDecimal.valueOf(2000))
                  .build())
          .addMemo(Memo.hash(Util.hash("abcdef".getBytes())))
          .setBaseFee(Transaction.MIN_BASE_FEE)
          .build();
      fail();
    } catch (IllegalArgumentException ignored) {
    }
  }

  @Test
  public void testBuilderTimeoutNegative() {
    Account account = new Account(KeyPair.random().getAccountId(), 2908908335136768L);
    try {
      new TransactionBuilder(account, Network.TESTNET)
          .addOperation(
              CreateAccountOperation.builder()
                  .destination(KeyPair.random().getAccountId())
                  .startingBalance(BigDecimal.valueOf(2000))
                  .build())
          .addMemo(Memo.hash(Util.hash("abcdef".getBytes())))
          .setTimeout(-1)
          .build();
      fail();
    } catch (RuntimeException exception) {
      assertTrue(exception.getMessage().contains("timeout cannot be negative"));
      assertEquals(2908908335136768L, account.getSequenceNumber().longValue());
    }
  }

  @Test
  public void testBuilderTimeout() throws IOException {
    Account account = new Account(KeyPair.random().getAccountId(), 2908908335136768L);
    long currentUnix = System.currentTimeMillis() / 1000L;
    Transaction transaction =
        new TransactionBuilder(account, Network.TESTNET)
            .addOperation(
                CreateAccountOperation.builder()
                    .destination(KeyPair.random().getAccountId())
                    .startingBalance(BigDecimal.valueOf(2000))
                    .build())
            .setTimeout(10)
            .setBaseFee(Transaction.MIN_BASE_FEE)
            .build();

    assertEquals(0, transaction.getTimeBounds().getMinTime().longValue());
    assertTrue(currentUnix + 10 <= transaction.getTimeBounds().getMaxTime().longValue());

    // Convert transaction to binary XDR and back again to make sure timebounds are correctly
    // de/serialized.
    org.stellar.sdk.xdr.TransactionEnvelope decodedTransaction =
        org.stellar.sdk.xdr.TransactionEnvelope.fromXdrBase64(transaction.toEnvelopeXdrBase64());
    Transaction transaction2 =
        (Transaction) Transaction.fromEnvelopeXdr(decodedTransaction, Network.TESTNET);
    assertEquals(transaction, transaction2);
  }

  @Test
  public void testBuilderSetsLedgerBounds() throws IOException {
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    Account account = new Account(source.getAccountId(), 2908908335136768L);
    KeyPair newAccount =
        KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

    Transaction transaction =
        new TransactionBuilder(account, Network.TESTNET)
            .addOperation(
                CreateAccountOperation.builder()
                    .destination(newAccount.getAccountId())
                    .startingBalance(BigDecimal.valueOf(2000))
                    .build())
            .addPreconditions(
                TransactionPreconditions.builder()
                    .timeBounds(
                        new TimeBounds(BigInteger.ZERO, TransactionPreconditions.TIMEOUT_INFINITE))
                    .ledgerBounds(new LedgerBounds(1, 2))
                    .build())
            .setBaseFee(Transaction.MIN_BASE_FEE)
            .build();

    assertEquals(1, transaction.getPreconditions().getLedgerBounds().getMinLedger());
    assertEquals(2, transaction.getPreconditions().getLedgerBounds().getMaxLedger());

    org.stellar.sdk.xdr.TransactionEnvelope decodedTransaction =
        org.stellar.sdk.xdr.TransactionEnvelope.fromXdrBase64(transaction.toEnvelopeXdrBase64());
    Transaction transaction2 =
        (Transaction) Transaction.fromEnvelopeXdr(decodedTransaction, Network.TESTNET);
    assertEquals(transaction, transaction2);

    assertEquals(
        "AAAAAgAAAABexSIg06FtXzmFBQQtHZsrnyWxUzmthkBEhs/ktoeVYgAAAGQAClWjAAAAAQAAAAIAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAABAAAAAgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAADt4FJhvNwvlQqjuhc7bjLVyRf5e4K2QOzI0c6nWfVvEAAAAASoF8gAAAAAAAAAAAA=",
        transaction.toEnvelopeXdrBase64());
  }

  @Test
  public void testBuilderSetsMinSeqNum() throws IOException {
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    Account account = new Account(source.getAccountId(), 2908908335136768L);
    KeyPair newAccount =
        KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");
    PreconditionsV2.PreconditionsV2Builder preconditionsV2 = PreconditionsV2.builder();

    SequenceNumber seqNum = new SequenceNumber();
    seqNum.setSequenceNumber(new Int64(5L));
    preconditionsV2.timeBounds(
        buildTimeBounds(BigInteger.ZERO, TransactionPreconditions.TIMEOUT_INFINITE));
    preconditionsV2.minSeqNum(seqNum);

    Transaction transaction =
        new TransactionBuilder(account, Network.TESTNET)
            .addOperation(
                CreateAccountOperation.builder()
                    .destination(newAccount.getAccountId())
                    .startingBalance(BigDecimal.valueOf(2000))
                    .build())
            .addPreconditions(
                TransactionPreconditions.builder()
                    .timeBounds(
                        new TimeBounds(BigInteger.ZERO, TransactionPreconditions.TIMEOUT_INFINITE))
                    .minSeqNumber(5L)
                    .build())
            .setBaseFee(Transaction.MIN_BASE_FEE)
            .build();

    assertEquals(Long.valueOf(5), transaction.getPreconditions().getMinSeqNumber());

    // Convert transaction to binary XDR and back again to make sure correctly xdr de/serialized.
    org.stellar.sdk.xdr.TransactionEnvelope decodedTransaction =
        org.stellar.sdk.xdr.TransactionEnvelope.fromXdrBase64(transaction.toEnvelopeXdrBase64());
    Transaction transaction2 =
        (Transaction) Transaction.fromEnvelopeXdr(decodedTransaction, Network.TESTNET);
    assertEquals(transaction, transaction2);

    assertEquals(
        "AAAAAgAAAABexSIg06FtXzmFBQQtHZsrnyWxUzmthkBEhs/ktoeVYgAAAGQAClWjAAAAAQAAAAIAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAADt4FJhvNwvlQqjuhc7bjLVyRf5e4K2QOzI0c6nWfVvEAAAAASoF8gAAAAAAAAAAAA=",
        transaction.toEnvelopeXdrBase64());
  }

  @Test
  public void testBuilderSetsMinSeqAge() throws IOException {
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    KeyPair newAccount =
        KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");
    Account account = new Account(source.getAccountId(), 2908908335136768L);

    Transaction transaction =
        new TransactionBuilder(account, Network.TESTNET)
            .addOperation(
                CreateAccountOperation.builder()
                    .destination(newAccount.getAccountId())
                    .startingBalance(BigDecimal.valueOf(2000))
                    .build())
            .addPreconditions(
                TransactionPreconditions.builder()
                    .timeBounds(
                        new TimeBounds(BigInteger.ZERO, TransactionPreconditions.TIMEOUT_INFINITE))
                    .minSeqAge(BigInteger.valueOf(5L))
                    .build())
            .setBaseFee(Transaction.MIN_BASE_FEE)
            .build();

    assertEquals(5, transaction.getPreconditions().getMinSeqAge().intValue());

    // Convert transaction to binary XDR and back again to make sure correctly xdr de/serialized.
    org.stellar.sdk.xdr.TransactionEnvelope decodedTransaction =
        org.stellar.sdk.xdr.TransactionEnvelope.fromXdrBase64(transaction.toEnvelopeXdrBase64());
    Transaction transaction2 =
        (Transaction) Transaction.fromEnvelopeXdr(decodedTransaction, Network.TESTNET);
    assertEquals(transaction, transaction2);

    assertEquals(
        "AAAAAgAAAABexSIg06FtXzmFBQQtHZsrnyWxUzmthkBEhs/ktoeVYgAAAGQAClWjAAAAAQAAAAIAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAUAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAA7eBSYbzcL5UKo7oXO24y1ckX+XuCtkDsyNHOp1n1bxAAAAAEqBfIAAAAAAAAAAAA",
        transaction.toEnvelopeXdrBase64());
  }

  @Test
  public void testBuilderSetsMinSeqLedgerGap() throws IOException {
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    KeyPair newAccount =
        KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");
    Account account = new Account(source.getAccountId(), 2908908335136768L);

    Transaction transaction =
        new TransactionBuilder(account, Network.TESTNET)
            .addOperation(
                CreateAccountOperation.builder()
                    .destination(newAccount.getAccountId())
                    .startingBalance(BigDecimal.valueOf(2000))
                    .build())
            .addPreconditions(
                TransactionPreconditions.builder()
                    .timeBounds(
                        new TimeBounds(BigInteger.ZERO, TransactionPreconditions.TIMEOUT_INFINITE))
                    .minSeqLedgerGap(5)
                    .build())
            .setBaseFee(Transaction.MIN_BASE_FEE)
            .build();

    assertEquals(5, transaction.getPreconditions().getMinSeqLedgerGap());

    // Convert transaction to binary XDR and back again to make sure correctly xdr de/serialized.
    org.stellar.sdk.xdr.TransactionEnvelope decodedTransaction =
        org.stellar.sdk.xdr.TransactionEnvelope.fromXdrBase64(transaction.toEnvelopeXdrBase64());
    Transaction transaction2 =
        (Transaction) Transaction.fromEnvelopeXdr(decodedTransaction, Network.TESTNET);
    assertEquals(transaction, transaction2);

    assertEquals(
        "AAAAAgAAAABexSIg06FtXzmFBQQtHZsrnyWxUzmthkBEhs/ktoeVYgAAAGQAClWjAAAAAQAAAAIAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAFAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAA7eBSYbzcL5UKo7oXO24y1ckX+XuCtkDsyNHOp1n1bxAAAAAEqBfIAAAAAAAAAAAA",
        transaction.toEnvelopeXdrBase64());
  }

  @Test
  public void testBuilderExtraSigners() throws IOException {
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    KeyPair newAccount =
        KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");
    Account account = new Account(source.getAccountId(), 2908908335136768L);

    String accountStrKey = "GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVSGZ";

    String encodedString = "0102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f20";
    byte[] payload = Util.hexToBytes(encodedString);

    SignerKey signerKey =
        SignerKey.builder()
            .discriminant(SignerKeyType.SIGNER_KEY_TYPE_ED25519_SIGNED_PAYLOAD)
            .ed25519SignedPayload(
                SignerKey.SignerKeyEd25519SignedPayload.builder()
                    .payload(payload)
                    .ed25519(new Uint256(StrKey.decodeEd25519PublicKey(accountStrKey)))
                    .build())
            .build();

    Transaction transaction =
        new TransactionBuilder(account, Network.TESTNET)
            .addOperation(
                CreateAccountOperation.builder()
                    .destination(newAccount.getAccountId())
                    .startingBalance(BigDecimal.valueOf(2000))
                    .build())
            .addPreconditions(
                TransactionPreconditions.builder()
                    .timeBounds(
                        new TimeBounds(BigInteger.ZERO, TransactionPreconditions.TIMEOUT_INFINITE))
                    .extraSigners(Collections.singletonList(signerKey))
                    .minSeqLedgerGap(5)
                    .build())
            .setBaseFee(Transaction.MIN_BASE_FEE)
            .build();

    assertEquals(
        SignerKeyType.SIGNER_KEY_TYPE_ED25519_SIGNED_PAYLOAD,
        transaction.getPreconditions().getExtraSigners().get(0).getDiscriminant());
    assertArrayEquals(
        payload,
        transaction
            .getPreconditions()
            .getExtraSigners()
            .get(0)
            .getEd25519SignedPayload()
            .getPayload());

    // Convert transaction to binary XDR and back again to make sure correctly xdr de/serialized.
    org.stellar.sdk.xdr.TransactionEnvelope decodedTransaction =
        org.stellar.sdk.xdr.TransactionEnvelope.fromXdrBase64(transaction.toEnvelopeXdrBase64());
    Transaction transaction2 =
        (Transaction) Transaction.fromEnvelopeXdr(decodedTransaction, Network.TESTNET);
    assertEquals(transaction, transaction2);

    assertEquals(
        "AAAAAgAAAABexSIg06FtXzmFBQQtHZsrnyWxUzmthkBEhs/ktoeVYgAAAGQAClWjAAAAAQAAAAIAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAFAAAAAQAAAAM/DDS/k60NmXHQTMyQ9wVRHIOKrZc0pKL7DXoD/H/omgAAACABAgMEBQYHCAkKCwwNDg8QERITFBUWFxgZGhscHR4fIAAAAAAAAAABAAAAAAAAAAAAAAAA7eBSYbzcL5UKo7oXO24y1ckX+XuCtkDsyNHOp1n1bxAAAAAEqBfIAAAAAAAAAAAA",
        transaction.toEnvelopeXdrBase64());
  }

  @Test
  public void testBuilderFailsWhenTooManyExtraSigners() {
    Account account = new Account(KeyPair.random().getAccountId(), 2908908335136768L);

    try {
      new TransactionBuilder(account, Network.TESTNET)
          .addOperation(
              CreateAccountOperation.builder()
                  .destination(KeyPair.random().getAccountId())
                  .startingBalance(BigDecimal.valueOf(2000))
                  .build())
          .addPreconditions(
              TransactionPreconditions.builder()
                  .timeBounds(
                      new TimeBounds(BigInteger.ZERO, TransactionPreconditions.TIMEOUT_INFINITE))
                  .extraSigners(
                      Arrays.asList(
                          SignerKey.builder().build(),
                          SignerKey.builder().build(),
                          SignerKey.builder().build()))
                  .minSeqLedgerGap(5)
                  .build())
          .setBaseFee(Transaction.MIN_BASE_FEE)
          .build();
      fail();
    } catch (IllegalArgumentException ignored) {
    }
  }

  @Test
  public void testBuilderUsesAccountSequence() {
    Account account = new Account(KeyPair.random().getAccountId(), 3L);
    Transaction transaction =
        new TransactionBuilder(account, Network.TESTNET)
            .addOperation(
                CreateAccountOperation.builder()
                    .destination(KeyPair.random().getAccountId())
                    .startingBalance(BigDecimal.valueOf(2000))
                    .build())
            .addPreconditions(
                TransactionPreconditions.builder()
                    .timeBounds(
                        new TimeBounds(BigInteger.ZERO, TransactionPreconditions.TIMEOUT_INFINITE))
                    .build())
            .setBaseFee(Transaction.MIN_BASE_FEE)
            .build();

    // check that the created tx has the sequence number which custom sequence handler sets,
    // rather than using default of sourceAccount.seqNum + 1
    assertEquals(4, transaction.getSequenceNumber());

    // check that the sourceAccount.seqNum gets updated to what the custom sequence handler sets,
    // rather than the default of sourceAccount.seqNum + 1
    assertEquals(4, account.getSequenceNumber().longValue());
  }

  @Test
  public void testBuilderFailsWhenNoTimeBoundsOrTimeoutSet() {
    Account account = new Account(KeyPair.random().getAccountId(), 2908908335136768L);
    try {
      new TransactionBuilder(account, Network.TESTNET)
          .addOperation(
              CreateAccountOperation.builder()
                  .destination(KeyPair.random().getAccountId())
                  .startingBalance(BigDecimal.valueOf(2000))
                  .build())
          .setBaseFee(Transaction.MIN_BASE_FEE)
          .build();
      fail();
    } catch (RuntimeException exception) {
      assertTrue(exception.getMessage().contains("Invalid preconditions, must define timebounds"));
      assertEquals(2908908335136768L, account.getSequenceNumber().longValue());
    }
  }

  @Test
  public void testBuilderFailsWhenTimeBoundsAndTimeoutBothSet() {
    Account account = new Account(KeyPair.random().getAccountId(), 2908908335136768L);
    try {
      new TransactionBuilder(account, Network.TESTNET)
          .addOperation(
              CreateAccountOperation.builder()
                  .destination(KeyPair.random().getAccountId())
                  .startingBalance(BigDecimal.valueOf(2000))
                  .build())
          .setBaseFee(Transaction.MIN_BASE_FEE)
          .addPreconditions(
              TransactionPreconditions.builder().timeBounds(new TimeBounds(10, 20)).build())
          .setTimeout(30)
          .build();
      fail();
    } catch (IllegalArgumentException exception) {
      assertTrue(
          exception
              .getMessage()
              .contains("Can not set both TransactionPreconditions.timeBounds and timeout."));
      assertEquals(Long.valueOf(2908908335136768L), account.getSequenceNumber());
    }
  }

  @Test
  public void testBuilderWhenTimeoutSetAndLedgerBoundsSet() throws IOException {
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    KeyPair destination =
        KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");
    Account account = new Account(source.getAccountId(), 2908908335136768L);
    long currentUnix = System.currentTimeMillis() / 1000L;

    Transaction transaction =
        new TransactionBuilder(account, Network.TESTNET)
            .addOperation(
                CreateAccountOperation.builder()
                    .destination(destination.getAccountId())
                    .startingBalance(BigDecimal.valueOf(2000))
                    .build())
            .setBaseFee(Transaction.MIN_BASE_FEE)
            .addPreconditions(
                TransactionPreconditions.builder().ledgerBounds(new LedgerBounds(123, 456)).build())
            .setTimeout(10)
            .build();

    transaction.sign(source);

    // Convert transaction to binary XDR and back again to make sure timeout is correctly
    // de/serialized.
    org.stellar.sdk.xdr.TransactionEnvelope decodedTransaction =
        org.stellar.sdk.xdr.TransactionEnvelope.fromXdrBase64(transaction.toEnvelopeXdrBase64());

    assertEquals(
        decodedTransaction
            .getV1()
            .getTx()
            .getCond()
            .getV2()
            .getLedgerBounds()
            .getMinLedger()
            .getUint32()
            .getNumber()
            .longValue(),
        123L);
    assertEquals(
        decodedTransaction
            .getV1()
            .getTx()
            .getCond()
            .getV2()
            .getLedgerBounds()
            .getMaxLedger()
            .getUint32()
            .getNumber()
            .longValue(),
        456L);
    assertEquals(
        decodedTransaction
            .getV1()
            .getTx()
            .getCond()
            .getV2()
            .getTimeBounds()
            .getMinTime()
            .getTimePoint()
            .getUint64()
            .getNumber()
            .longValue(),
        0);
    assertTrue(
        currentUnix + 10
            <= decodedTransaction
                .getV1()
                .getTx()
                .getCond()
                .getV2()
                .getTimeBounds()
                .getMaxTime()
                .getTimePoint()
                .getUint64()
                .getNumber()
                .longValue());
  }

  @Test
  public void testBuilderInfinteTimeoutOnly() throws IOException {
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    KeyPair destination =
        KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");
    Account account = new Account(source.getAccountId(), 2908908335136768L);

    Transaction transaction =
        new TransactionBuilder(account, Network.TESTNET)
            .addOperation(
                CreateAccountOperation.builder()
                    .destination(destination.getAccountId())
                    .startingBalance(BigDecimal.valueOf(2000))
                    .build())
            .setBaseFee(Transaction.MIN_BASE_FEE)
            .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE)
            .build();

    transaction.sign(source);

    // Convert transaction to binary XDR and back again to make sure timeout is correctly
    // de/serialized.
    org.stellar.sdk.xdr.TransactionEnvelope decodedTransaction =
        org.stellar.sdk.xdr.TransactionEnvelope.fromXdrBase64(transaction.toEnvelopeXdrBase64());

    assertEquals(
        decodedTransaction
            .getV1()
            .getTx()
            .getCond()
            .getTimeBounds()
            .getMinTime()
            .getTimePoint()
            .getUint64()
            .getNumber()
            .longValue(),
        0);
    assertEquals(
        decodedTransaction
            .getV1()
            .getTx()
            .getCond()
            .getTimeBounds()
            .getMaxTime()
            .getTimePoint()
            .getUint64()
            .getNumber()
            .longValue(),
        0);
  }

  @Test
  public void testBuilderTimeoutOnly() throws IOException {
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    KeyPair destination =
        KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");
    Account account = new Account(source.getAccountId(), 2908908335136768L);
    long currentUnix = System.currentTimeMillis() / 1000L;

    Transaction transaction =
        new TransactionBuilder(account, Network.TESTNET)
            .addOperation(
                CreateAccountOperation.builder()
                    .destination(destination.getAccountId())
                    .startingBalance(BigDecimal.valueOf(2000))
                    .build())
            .setBaseFee(Transaction.MIN_BASE_FEE)
            .setTimeout(10)
            .build();

    transaction.sign(source);

    // Convert transaction to binary XDR and back again to make sure timeout is correctly
    // de/serialized.
    org.stellar.sdk.xdr.TransactionEnvelope decodedTransaction =
        org.stellar.sdk.xdr.TransactionEnvelope.fromXdrBase64(transaction.toEnvelopeXdrBase64());

    assertEquals(
        decodedTransaction
            .getV1()
            .getTx()
            .getCond()
            .getTimeBounds()
            .getMinTime()
            .getTimePoint()
            .getUint64()
            .getNumber()
            .longValue(),
        0);
    assertTrue(
        currentUnix + 10
            <= decodedTransaction
                .getV1()
                .getTx()
                .getCond()
                .getTimeBounds()
                .getMaxTime()
                .getTimePoint()
                .getUint64()
                .getNumber()
                .longValue());
  }

  @Test
  public void testBuilderSuccessPublic() {

    // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    KeyPair destination =
        KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

    Account account = new Account(source.getAccountId(), 2908908335136768L);
    Transaction transaction =
        new TransactionBuilder(account, Network.PUBLIC)
            .addOperation(
                CreateAccountOperation.builder()
                    .destination(destination.getAccountId())
                    .startingBalance(BigDecimal.valueOf(2000))
                    .build())
            .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE)
            .setBaseFee(Transaction.MIN_BASE_FEE)
            .build();

    transaction.sign(source);

    Transaction decodedTransaction =
        (Transaction)
            Transaction.fromEnvelopeXdr(transaction.toEnvelopeXdrBase64(), Network.PUBLIC);
    assertEquals(
        "AAAAAgAAAABexSIg06FtXzmFBQQtHZsrnyWxUzmthkBEhs/ktoeVYgAAAGQAClWjAAAAAQAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAADt4FJhvNwvlQqjuhc7bjLVyRf5e4K2QOzI0c6nWfVvEAAAAASoF8gAAAAAAAAAAAG2h5ViAAAAQOFMVS3lQ1gtqY0a3VPsZXCNVGC/h8dANFiUS7hYPWSbyzi4Ob1ir5R256mOwX+B6vE552+y8JAFaAFPR0bDyAw=",
        transaction.toEnvelopeXdrBase64());

    assertEquals(decodedTransaction, transaction);
  }

  @Test
  public void testNoOperations() {
    // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");

    Account account = new Account(source.getAccountId(), 2908908335136768L);
    try {
      Transaction transaction =
          new TransactionBuilder(account, Network.TESTNET)
              .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE)
              .setBaseFee(Transaction.MIN_BASE_FEE)
              .build();
      fail();
    } catch (RuntimeException exception) {
      assertTrue(exception.getMessage().contains("At least one operation required"));
      assertEquals(2908908335136768L, account.getSequenceNumber().longValue());
    }
  }

  @Test
  public void testTryingToAddMemoTwice() {
    // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    KeyPair destination =
        KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

    try {
      Account account = new Account(source.getAccountId(), 2908908335136768L);
      new TransactionBuilder(account, Network.TESTNET)
          .addOperation(
              CreateAccountOperation.builder()
                  .destination(destination.getAccountId())
                  .startingBalance(BigDecimal.valueOf(2000))
                  .build())
          .setBaseFee(Transaction.MIN_BASE_FEE)
          .addMemo(Memo.none())
          .addMemo(Memo.none());
      fail();
    } catch (RuntimeException exception) {
      assertTrue(exception.getMessage().contains("Memo has been already added."));
    }
  }

  @Test
  public void testNoNetworkSet() throws StrKeyException {
    // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    KeyPair destination =
        KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

    Account account = new Account(source.getAccountId(), 2908908335136768L);
    try {
      new TransactionBuilder(account, null)
          .addOperation(
              CreateAccountOperation.builder()
                  .destination(destination.getAccountId())
                  .startingBalance(BigDecimal.valueOf(2000))
                  .build())
          .setBaseFee(Transaction.MIN_BASE_FEE)
          .addMemo(Memo.none())
          .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE)
          .build();
      fail();
    } catch (NullPointerException e) {
      assertTrue(e.getMessage().contains("network is marked non-null but is null"));
    }
  }

  @Test
  public void voidBuilderSorobanDataXdrString() {
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    KeyPair destination =
        KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

    LedgerKey ledgerKey =
        LedgerKey.builder()
            .discriminant(LedgerEntryType.ACCOUNT)
            .account(
                LedgerKey.LedgerKeyAccount.builder()
                    .accountID(
                        KeyPair.fromAccountId(
                                "GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO")
                            .getXdrAccountId())
                    .build())
            .build();
    SorobanTransactionData sorobanData =
        SorobanTransactionData.builder()
            .resources(
                SorobanResources.builder()
                    .footprint(
                        LedgerFootprint.builder()
                            .readOnly(new LedgerKey[] {ledgerKey})
                            .readWrite(new LedgerKey[] {})
                            .build())
                    .readBytes(new Uint32(new XdrUnsignedInteger(699)))
                    .writeBytes(new Uint32(new XdrUnsignedInteger(0)))
                    .instructions(new Uint32(new XdrUnsignedInteger(34567)))
                    .build())
            .resourceFee(new Int64(100L))
            .ext(ExtensionPoint.builder().discriminant(0).build())
            .build();

    long sequenceNumber = 2908908335136768L;
    Account account = new Account(source.getAccountId(), sequenceNumber);
    Transaction transaction =
        new TransactionBuilder(account, Network.TESTNET)
            .addOperation(
                CreateAccountOperation.builder()
                    .destination(destination.getAccountId())
                    .startingBalance(BigDecimal.valueOf(2000))
                    .build())
            .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE)
            .setBaseFee(Transaction.MIN_BASE_FEE)
            .setSorobanData(sorobanData)
            .build();

    assertEquals(sorobanData, transaction.getSorobanData());
    org.stellar.sdk.xdr.Transaction.TransactionExt expectedExt =
        org.stellar.sdk.xdr.Transaction.TransactionExt.builder()
            .discriminant(1)
            .sorobanData(sorobanData)
            .build();
    assertEquals(expectedExt, transaction.toEnvelopeXdr().getV1().getTx().getExt());
  }

  @Test
  public void voidBuilderSorobanDataXdrObject() throws IOException {
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    KeyPair destination =
        KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

    LedgerKey ledgerKey =
        LedgerKey.builder()
            .discriminant(LedgerEntryType.ACCOUNT)
            .account(
                LedgerKey.LedgerKeyAccount.builder()
                    .accountID(
                        KeyPair.fromAccountId(
                                "GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO")
                            .getXdrAccountId())
                    .build())
            .build();
    SorobanTransactionData sorobanData =
        SorobanTransactionData.builder()
            .resources(
                SorobanResources.builder()
                    .footprint(
                        LedgerFootprint.builder()
                            .readOnly(new LedgerKey[] {ledgerKey})
                            .readWrite(new LedgerKey[] {})
                            .build())
                    .readBytes(new Uint32(new XdrUnsignedInteger(699)))
                    .writeBytes(new Uint32(new XdrUnsignedInteger(0)))
                    .instructions(new Uint32(new XdrUnsignedInteger(34567)))
                    .build())
            .resourceFee(new Int64(100L))
            .ext(ExtensionPoint.builder().discriminant(0).build())
            .build();
    String sorobanDataString = sorobanData.toXdrBase64();

    CreateContractArgs createContractArgs =
        CreateContractArgs.builder()
            .contractIDPreimage(
                ContractIDPreimage.builder()
                    .discriminant(ContractIDPreimageType.CONTRACT_ID_PREIMAGE_FROM_ADDRESS)
                    .fromAddress(
                        ContractIDPreimage.ContractIDPreimageFromAddress.builder()
                            .address(
                                new Address(
                                        "GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO")
                                    .toSCAddress())
                            .salt(new Uint256(new byte[32]))
                            .build())
                    .build())
            .executable(
                ContractExecutable.builder()
                    .discriminant(ContractExecutableType.CONTRACT_EXECUTABLE_STELLAR_ASSET)
                    .build())
            .build();
    HostFunction hostFunction =
        HostFunction.builder()
            .discriminant(HostFunctionType.HOST_FUNCTION_TYPE_CREATE_CONTRACT)
            .createContract(createContractArgs)
            .build();
    InvokeHostFunctionOperation invokeHostFunctionOperation =
        InvokeHostFunctionOperation.builder().hostFunction(hostFunction).build();

    long sequenceNumber = 2908908335136768L;
    Account account = new Account(source.getAccountId(), sequenceNumber);
    Transaction transaction =
        new TransactionBuilder(account, Network.TESTNET)
            .addOperation(invokeHostFunctionOperation)
            .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE)
            .setBaseFee(Transaction.MIN_BASE_FEE)
            .setSorobanData(sorobanDataString)
            .build();

    assertEquals(sorobanData, transaction.getSorobanData());
    org.stellar.sdk.xdr.Transaction.TransactionExt expectedExt =
        org.stellar.sdk.xdr.Transaction.TransactionExt.builder()
            .discriminant(1)
            .sorobanData(sorobanData)
            .build();
    assertEquals(expectedExt, transaction.toEnvelopeXdr().getV1().getTx().getExt());
  }

  private static org.stellar.sdk.xdr.TimeBounds buildTimeBounds(
      BigInteger minTime, BigInteger maxTime) {
    return org.stellar.sdk.xdr.TimeBounds.builder()
        .minTime(new TimePoint(new Uint64(new XdrUnsignedHyperInteger(minTime))))
        .maxTime(new TimePoint(new Uint64(new XdrUnsignedHyperInteger(maxTime))))
        .build();
  }
}
