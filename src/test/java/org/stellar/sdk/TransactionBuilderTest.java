package org.stellar.sdk;

import com.google.common.io.BaseEncoding;
import org.junit.Test;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.PreconditionsV2;
import org.stellar.sdk.xdr.SequenceNumber;
import org.stellar.sdk.xdr.SignerKey;
import org.stellar.sdk.xdr.SignerKeyType;
import org.stellar.sdk.xdr.Uint256;
import org.stellar.sdk.xdr.XdrDataInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TransactionBuilderTest {

    @Test
    public void testMissingOperationFee() {
        long sequenceNumber = 2908908335136768L;
        Account account = new Account("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR", sequenceNumber);
        try {
            new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.TESTNET)
                .addOperation(new CreateAccountOperation.Builder("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR", "2000").build())
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
        KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
        KeyPair destination = KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

        long sequenceNumber = 2908908335136768L;
        Account account = new Account(source.getAccountId(), sequenceNumber);
        Transaction transaction = new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.TESTNET)
                .addOperation(new CreateAccountOperation.Builder(destination.getAccountId(), "2000").build())
                .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE)
                .setBaseFee(Transaction.MIN_BASE_FEE)
                .build();

        transaction.sign(source);

        assertEquals(transaction.getSourceAccount(), source.getAccountId());
        assertEquals(transaction.getSequenceNumber(), sequenceNumber + 1);
        assertEquals(transaction.getFee(), 100);

        assertEquals(
                "AAAAAgAAAABexSIg06FtXzmFBQQtHZsrnyWxUzmthkBEhs/ktoeVYgAAAGQAClWjAAAAAQAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAO3gUmG83C+VCqO6FztuMtXJF/l7grZA7MjRzqdZ9W8QAAAABKgXyAAAAAAAAAAAAbaHlWIAAABAy5IvTou9NDetC6PIFJhBR2yr2BuEEql4iyLfU9K7tjuQaYVZf40fbWLRwA/lHg2IYFzYMFMakxLtVrpLxMmHAw==",
                transaction.toEnvelopeXdrBase64());

        // Convert transaction to binary XDR and back again to make sure correctly xdr de/serialized.
        XdrDataInputStream is = new XdrDataInputStream(
                new ByteArrayInputStream(
                        javax.xml.bind.DatatypeConverter.parseBase64Binary(transaction.toEnvelopeXdrBase64())
                )
        );
        org.stellar.sdk.xdr.TransactionEnvelope decodedTransaction = org.stellar.sdk.xdr.TransactionEnvelope.decode(is);
        Transaction transaction2 = (Transaction)Transaction.fromEnvelopeXdr(AccountConverter.enableMuxed(), decodedTransaction, Network.TESTNET);
        assertEquals(transaction, transaction2);
    }

    @Test
    public void testBuilderMemoText() throws Exception {
        // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
        KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
        KeyPair destination = KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

        Account account = new Account(source.getAccountId(), 2908908335136768L);
        Transaction transaction = new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.TESTNET)
                .addOperation(new CreateAccountOperation.Builder(destination.getAccountId(), "2000").build())
                .addMemo(Memo.text("Hello world!"))
                .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE)
                .setBaseFee(Transaction.MIN_BASE_FEE)
                .build();

        transaction.sign(source);

        // Convert transaction to binary XDR and back again to make sure correctly xdr de/serialized.
        XdrDataInputStream is = new XdrDataInputStream(
                new ByteArrayInputStream(
                        javax.xml.bind.DatatypeConverter.parseBase64Binary(transaction.toEnvelopeXdrBase64())
                )
        );
        org.stellar.sdk.xdr.TransactionEnvelope decodedTransaction = org.stellar.sdk.xdr.TransactionEnvelope.decode(is);
        Transaction transaction2 = (Transaction)Transaction.fromEnvelopeXdr(AccountConverter.enableMuxed(), decodedTransaction, Network.TESTNET);
        assertEquals(transaction, transaction2);

        assertEquals(
                "AAAAAgAAAABexSIg06FtXzmFBQQtHZsrnyWxUzmthkBEhs/ktoeVYgAAAGQAClWjAAAAAQAAAAAAAAABAAAADEhlbGxvIHdvcmxkIQAAAAEAAAAAAAAAAAAAAADt4FJhvNwvlQqjuhc7bjLVyRf5e4K2QOzI0c6nWfVvEAAAAASoF8gAAAAAAAAAAAG2h5ViAAAAQMc6HwYaGsrlJ8/LdE9VDVq04JifpQofSmnjhrtqaTTs/VBsNGmxi4b/vaFkLLLWh8emI8FsS/vBgb8AVFVkZQU=",
                transaction.toEnvelopeXdrBase64());
    }

    @Test
    public void testBuilderTimeBounds() throws FormatException, IOException {
        // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
        KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
        KeyPair destination = KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

        Account account = new Account(source.getAccountId(), 2908908335136768L);
        Transaction transaction = new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.TESTNET)
                .addOperation(new CreateAccountOperation.Builder(destination.getAccountId(), "2000").build())
                .addTimeBounds(new TimeBounds(42, 1337))
                .addMemo(Memo.hash("abcdef"))
                .setBaseFee(Transaction.MIN_BASE_FEE)
                .build();

        transaction.sign(source);

        // Convert transaction to binary XDR and back again to make sure timebounds are correctly de/serialized.
        XdrDataInputStream is = new XdrDataInputStream(
                new ByteArrayInputStream(
                        javax.xml.bind.DatatypeConverter.parseBase64Binary(transaction.toEnvelopeXdrBase64())
                )
        );
        org.stellar.sdk.xdr.TransactionEnvelope decodedTransaction = org.stellar.sdk.xdr.TransactionEnvelope.decode(is);

        assertEquals(decodedTransaction.getV1().getTx().getCond().getTimeBounds().getMinTime().getTimePoint().getUint64().longValue(), 42);
        assertEquals(decodedTransaction.getV1().getTx().getCond().getTimeBounds().getMaxTime().getTimePoint().getUint64().longValue(), 1337);

        Transaction transaction2 = (Transaction)Transaction.fromEnvelopeXdr(AccountConverter.enableMuxed(), decodedTransaction, Network.TESTNET);
        assertEquals(transaction, transaction2);
    }

    @Test
    public void testBuilderBaseFee() throws Exception {
        // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
        KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
        KeyPair destination = KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

        Account account = new Account(source.getAccountId(), 2908908335136768L);
        Transaction transaction = new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.TESTNET)
                .addOperation(new CreateAccountOperation.Builder(destination.getAccountId(), "2000").build())
                .setBaseFee(200)
                .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE)
                .build();

        transaction.sign(source);

        // Convert transaction to binary XDR and back again to make sure correctly xdr de/serialized.
        XdrDataInputStream is = new XdrDataInputStream(
                new ByteArrayInputStream(
                        javax.xml.bind.DatatypeConverter.parseBase64Binary(transaction.toEnvelopeXdrBase64())
                )
        );
        org.stellar.sdk.xdr.TransactionEnvelope decodedTransaction = org.stellar.sdk.xdr.TransactionEnvelope.decode(is);
        Transaction transaction2 = (Transaction)Transaction.fromEnvelopeXdr(AccountConverter.enableMuxed(), decodedTransaction, Network.TESTNET);
        assertEquals(transaction, transaction2);

        assertEquals(
                "AAAAAgAAAABexSIg06FtXzmFBQQtHZsrnyWxUzmthkBEhs/ktoeVYgAAAMgAClWjAAAAAQAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAO3gUmG83C+VCqO6FztuMtXJF/l7grZA7MjRzqdZ9W8QAAAABKgXyAAAAAAAAAAAAbaHlWIAAABA9TG3dKKLtLHzRUbsbEqr68CfUc800p1/LE5pWzCnFdFdypdXgyqHqw/sWdaTUMDiWawBtsmqV8oOtD0Hw1HDDQ==",
                transaction.toEnvelopeXdrBase64());
    }

    @Test
    public void testBuilderBaseFeeThrows() throws FormatException {
        // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
        KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");

        Account account = new Account(source.getAccountId(), 2908908335136768L);
        TransactionBuilder builder = new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.TESTNET);
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
        Transaction transaction = new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.TESTNET)
                .addOperation(new CreateAccountOperation.Builder(KeyPair.random().getAccountId(), "2000").build())
                .addTimeBounds(new TimeBounds(42, 1337))
                .addMemo(Memo.hash("abcdef"))
                .setBaseFee(Transaction.MIN_BASE_FEE)
                .build();

        assertEquals(42, transaction.getTimeBounds().getMinTime());
        assertEquals(1337, transaction.getTimeBounds().getMaxTime());

        // Convert transaction to binary XDR and back again to make sure correctly xdr de/serialized.
        XdrDataInputStream is = new XdrDataInputStream(
                new ByteArrayInputStream(
                        javax.xml.bind.DatatypeConverter.parseBase64Binary(transaction.toEnvelopeXdrBase64())
                )
        );
        org.stellar.sdk.xdr.TransactionEnvelope decodedTransaction = org.stellar.sdk.xdr.TransactionEnvelope.decode(is);
        Transaction transaction2 = (Transaction)Transaction.fromEnvelopeXdr(AccountConverter.enableMuxed(), decodedTransaction, Network.TESTNET);
        assertEquals(transaction, transaction2);
    }

    @Test
    public void testBuilderRequiresTimeoutOrTimeBounds() throws IOException {
        Account account = new Account(KeyPair.random().getAccountId(), 2908908335136768L);
        try {
            new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.TESTNET)
                    .addOperation(new CreateAccountOperation.Builder(KeyPair.random().getAccountId(), "2000").build())
                    .addMemo(Memo.hash("abcdef"))
                    .setBaseFee(Transaction.MIN_BASE_FEE)
                    .build();
            fail();
        } catch (FormatException ignored) {}
    }


    @Test
    public void testBuilderTimeoutNegative() throws IOException {
        Account account = new Account(KeyPair.random().getAccountId(), 2908908335136768L);
        try {
            new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.TESTNET)
                    .addOperation(new CreateAccountOperation.Builder(KeyPair.random().getAccountId(), "2000").build())
                    .addMemo(Memo.hash("abcdef"))
                    .setTimeout(-1)
                    .build();
            fail();
        } catch (RuntimeException exception) {
            assertTrue(exception.getMessage().contains("timeout cannot be negative"));
            assertEquals(new Long(2908908335136768L), account.getSequenceNumber());
        }
    }

    @Test
    public void testBuilderTimeout() throws IOException {
        Account account = new Account(KeyPair.random().getAccountId(), 2908908335136768L);
        long currentUnix = System.currentTimeMillis() / 1000L;
        Transaction transaction = new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.TESTNET)
                .addOperation(new CreateAccountOperation.Builder(KeyPair.random().getAccountId(), "2000").build())
                .setTimeout(10)
                .setBaseFee(Transaction.MIN_BASE_FEE)
                .build();

        assertEquals(0, transaction.getTimeBounds().getMinTime());
        assertTrue(currentUnix + 10 <= transaction.getTimeBounds().getMaxTime());

        // Convert transaction to binary XDR and back again to make sure timebounds are correctly de/serialized.
        XdrDataInputStream is = new XdrDataInputStream(
                new ByteArrayInputStream(
                        javax.xml.bind.DatatypeConverter.parseBase64Binary(transaction.toEnvelopeXdrBase64())
                )
        );
        org.stellar.sdk.xdr.TransactionEnvelope decodedTransaction = org.stellar.sdk.xdr.TransactionEnvelope.decode(is);
        Transaction transaction2 = (Transaction)Transaction.fromEnvelopeXdr(AccountConverter.enableMuxed(), decodedTransaction, Network.TESTNET);
        assertEquals(transaction, transaction2);
    }

    @Test
    public void testBuilderSetsLedgerBounds() throws IOException {
        KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
        Account account = new Account(source.getAccountId(), 2908908335136768L);
        KeyPair newAccount = KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

        Transaction transaction = new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.TESTNET)
                .addOperation(new CreateAccountOperation.Builder(newAccount.getAccountId(), "2000").build())
                .addPreconditions(TransactionPreconditions.builder()
                        .timeBounds(new TimeBounds(0L,TransactionPreconditions.TIMEOUT_INFINITE))
                        .ledgerBounds(LedgerBounds.builder().minLedger(1).maxLedger(2).build())
                        .build())
                .setBaseFee(Transaction.MIN_BASE_FEE)
                .build();

        assertEquals(1, transaction.getPreconditions().getLedgerBounds().getMinLedger());
        assertEquals(2, transaction.getPreconditions().getLedgerBounds().getMaxLedger());

        XdrDataInputStream is = new XdrDataInputStream(
                new ByteArrayInputStream(
                        javax.xml.bind.DatatypeConverter.parseBase64Binary(transaction.toEnvelopeXdrBase64())
                )
        );
        org.stellar.sdk.xdr.TransactionEnvelope decodedTransaction = org.stellar.sdk.xdr.TransactionEnvelope.decode(is);
        Transaction transaction2 = (Transaction)Transaction.fromEnvelopeXdr(AccountConverter.enableMuxed(), decodedTransaction, Network.TESTNET);
        assertEquals(transaction, transaction2);

        assertEquals(
                "AAAAAgAAAABexSIg06FtXzmFBQQtHZsrnyWxUzmthkBEhs/ktoeVYgAAAGQAClWjAAAAAQAAAAIAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAABAAAAAgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAADt4FJhvNwvlQqjuhc7bjLVyRf5e4K2QOzI0c6nWfVvEAAAAASoF8gAAAAAAAAAAAA=",
                transaction.toEnvelopeXdrBase64());


    }

    @Test
    public void testBuilderSetsMinSeqNum() throws IOException {
        KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
        Account account = new Account(source.getAccountId(), 2908908335136768L);
        KeyPair newAccount = KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");
        PreconditionsV2.Builder preconditionsV2 = new PreconditionsV2.Builder();

        SequenceNumber seqNum = new SequenceNumber();
        seqNum.setSequenceNumber(new Int64(5L));
        preconditionsV2.timeBounds(TransactionBuilder.buildTimeBounds(0, TransactionPreconditions.TIMEOUT_INFINITE));
        preconditionsV2.minSeqNum(seqNum);

        Transaction transaction = new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.TESTNET)
                .addOperation(new CreateAccountOperation.Builder(newAccount.getAccountId(), "2000").build())
                .addPreconditions(TransactionPreconditions.builder()
                        .timeBounds(new TimeBounds(0L,TransactionPreconditions.TIMEOUT_INFINITE))
                        .minSeqNumber(5L)
                        .build())
                .setBaseFee(Transaction.MIN_BASE_FEE)
                .build();

        assertEquals(Long.valueOf(5), transaction.getPreconditions().getMinSeqNumber());

        // Convert transaction to binary XDR and back again to make sure correctly xdr de/serialized.
        XdrDataInputStream is = new XdrDataInputStream(
                new ByteArrayInputStream(
                        javax.xml.bind.DatatypeConverter.parseBase64Binary(transaction.toEnvelopeXdrBase64())
                )
        );
        org.stellar.sdk.xdr.TransactionEnvelope decodedTransaction = org.stellar.sdk.xdr.TransactionEnvelope.decode(is);
        Transaction transaction2 = (Transaction)Transaction.fromEnvelopeXdr(AccountConverter.enableMuxed(), decodedTransaction, Network.TESTNET);
        assertEquals(transaction, transaction2);

        assertEquals(
                "AAAAAgAAAABexSIg06FtXzmFBQQtHZsrnyWxUzmthkBEhs/ktoeVYgAAAGQAClWjAAAAAQAAAAIAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAADt4FJhvNwvlQqjuhc7bjLVyRf5e4K2QOzI0c6nWfVvEAAAAASoF8gAAAAAAAAAAAA=",
                transaction.toEnvelopeXdrBase64());
    }

    @Test
    public void testBuilderSetsMinSeqAge() throws IOException {
        KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
        KeyPair newAccount = KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");
        Account account = new Account(source.getAccountId(), 2908908335136768L);

        Transaction transaction = new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.TESTNET)
                .addOperation(new CreateAccountOperation.Builder(newAccount.getAccountId(), "2000").build())
                .addPreconditions(TransactionPreconditions.builder()
                        .timeBounds(new TimeBounds(0L,TransactionPreconditions.TIMEOUT_INFINITE))
                        .minSeqAge(5L)
                        .build())
                .setBaseFee(Transaction.MIN_BASE_FEE)
                .build();

        assertEquals(5, transaction.getPreconditions().getMinSeqAge());

        // Convert transaction to binary XDR and back again to make sure correctly xdr de/serialized.
        XdrDataInputStream is = new XdrDataInputStream(
                new ByteArrayInputStream(
                        javax.xml.bind.DatatypeConverter.parseBase64Binary(transaction.toEnvelopeXdrBase64())
                )
        );
        org.stellar.sdk.xdr.TransactionEnvelope decodedTransaction = org.stellar.sdk.xdr.TransactionEnvelope.decode(is);
        Transaction transaction2 = (Transaction)Transaction.fromEnvelopeXdr(AccountConverter.enableMuxed(), decodedTransaction, Network.TESTNET);
        assertEquals(transaction, transaction2);

        assertEquals(
                "AAAAAgAAAABexSIg06FtXzmFBQQtHZsrnyWxUzmthkBEhs/ktoeVYgAAAGQAClWjAAAAAQAAAAIAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAUAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAA7eBSYbzcL5UKo7oXO24y1ckX+XuCtkDsyNHOp1n1bxAAAAAEqBfIAAAAAAAAAAAA",
                transaction.toEnvelopeXdrBase64());
    }

    @Test
    public void testBuilderSetsMinSeqLedgerGap() throws IOException {
        KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
        KeyPair newAccount = KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");
        Account account = new Account(source.getAccountId(), 2908908335136768L);

        Transaction transaction = new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.TESTNET)
                .addOperation(new CreateAccountOperation.Builder(newAccount.getAccountId(), "2000").build())
                .addPreconditions(TransactionPreconditions.builder()
                        .timeBounds(new TimeBounds(0L,TransactionPreconditions.TIMEOUT_INFINITE))
                        .minSeqLedgerGap(5)
                        .build())
                .setBaseFee(Transaction.MIN_BASE_FEE)
                .build();

        assertEquals(5, transaction.getPreconditions().getMinSeqLedgerGap());

        // Convert transaction to binary XDR and back again to make sure correctly xdr de/serialized.
        XdrDataInputStream is = new XdrDataInputStream(
                new ByteArrayInputStream(
                        javax.xml.bind.DatatypeConverter.parseBase64Binary(transaction.toEnvelopeXdrBase64())
                )
        );
        org.stellar.sdk.xdr.TransactionEnvelope decodedTransaction = org.stellar.sdk.xdr.TransactionEnvelope.decode(is);
        Transaction transaction2 = (Transaction)Transaction.fromEnvelopeXdr(AccountConverter.enableMuxed(), decodedTransaction, Network.TESTNET);
        assertEquals(transaction, transaction2);

        assertEquals(
                "AAAAAgAAAABexSIg06FtXzmFBQQtHZsrnyWxUzmthkBEhs/ktoeVYgAAAGQAClWjAAAAAQAAAAIAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAFAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAA7eBSYbzcL5UKo7oXO24y1ckX+XuCtkDsyNHOp1n1bxAAAAAEqBfIAAAAAAAAAAAA",
                transaction.toEnvelopeXdrBase64());
    }

    @Test
    public void testBuilderExtraSigners() throws IOException {
        KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
        KeyPair newAccount = KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");
        Account account = new Account(source.getAccountId(), 2908908335136768L);

        String accountStrKey = "GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVSGZ";
        byte[] payload = BaseEncoding.base16().decode("0102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f20".toUpperCase());

        SignerKey signerKey = new SignerKey.Builder()
                .discriminant(SignerKeyType.SIGNER_KEY_TYPE_ED25519_SIGNED_PAYLOAD)
                .ed25519SignedPayload(new SignerKey.SignerKeyEd25519SignedPayload.Builder()
                        .payload(payload)
                        .ed25519(new Uint256(StrKey.decodeStellarAccountId(accountStrKey)))
                        .build())
                .build();

        Transaction transaction = new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.TESTNET)
                .addOperation(new CreateAccountOperation.Builder(newAccount.getAccountId(), "2000").build())
                .addPreconditions(TransactionPreconditions.builder()
                        .timeBounds(new TimeBounds(0L,TransactionPreconditions.TIMEOUT_INFINITE))
                        .extraSigners(newArrayList(signerKey))
                        .minSeqLedgerGap(5)
                        .build())
                .setBaseFee(Transaction.MIN_BASE_FEE)
                .build();

        assertEquals(SignerKeyType.SIGNER_KEY_TYPE_ED25519_SIGNED_PAYLOAD, newArrayList(transaction.getPreconditions().getExtraSigners()).get(0).getDiscriminant());
        assertArrayEquals(payload, newArrayList(transaction.getPreconditions().getExtraSigners()).get(0).getEd25519SignedPayload().getPayload());

        // Convert transaction to binary XDR and back again to make sure correctly xdr de/serialized.
        XdrDataInputStream is = new XdrDataInputStream(
                new ByteArrayInputStream(
                        javax.xml.bind.DatatypeConverter.parseBase64Binary(transaction.toEnvelopeXdrBase64())
                )
        );
        org.stellar.sdk.xdr.TransactionEnvelope decodedTransaction = org.stellar.sdk.xdr.TransactionEnvelope.decode(is);
        Transaction transaction2 = (Transaction)Transaction.fromEnvelopeXdr(AccountConverter.enableMuxed(), decodedTransaction, Network.TESTNET);
        assertEquals(transaction, transaction2);

        assertEquals(
                "AAAAAgAAAABexSIg06FtXzmFBQQtHZsrnyWxUzmthkBEhs/ktoeVYgAAAGQAClWjAAAAAQAAAAIAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAFAAAAAQAAAAM/DDS/k60NmXHQTMyQ9wVRHIOKrZc0pKL7DXoD/H/omgAAACABAgMEBQYHCAkKCwwNDg8QERITFBUWFxgZGhscHR4fIAAAAAAAAAABAAAAAAAAAAAAAAAA7eBSYbzcL5UKo7oXO24y1ckX+XuCtkDsyNHOp1n1bxAAAAAEqBfIAAAAAAAAAAAA",
                transaction.toEnvelopeXdrBase64());
    }

    @Test
    public void testBuilderFailsWhenTooManyExtraSigners() throws IOException {
        Account account = new Account(KeyPair.random().getAccountId(), 2908908335136768L);

        try {
            new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.TESTNET)
                    .addOperation(new CreateAccountOperation.Builder(KeyPair.random().getAccountId(), "2000").build())
                    .addPreconditions(TransactionPreconditions.builder()
                            .timeBounds(new TimeBounds(0L,TransactionPreconditions.TIMEOUT_INFINITE))
                            .extraSigners(newArrayList(new SignerKey.Builder().build(), new SignerKey.Builder().build(), new SignerKey.Builder().build()))
                            .minSeqLedgerGap(5)
                            .build())
                    .setBaseFee(Transaction.MIN_BASE_FEE)
                    .build();
            fail();
        } catch (FormatException ignored){}
    }

    @Test
    public void testBuilderFailsWhenTimeoutLessThanTimeBoundsMinimum() throws Exception {
        Account account = new Account(KeyPair.random().getAccountId(), 2908908335136768L);

        try {
            new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.TESTNET)
                    .addOperation(new CreateAccountOperation.Builder(KeyPair.random().getAccountId(), "2000").build())
                    .addPreconditions(TransactionPreconditions.builder()
                            // set min time to 120 seconds from now
                            .timeBounds(new TimeBounds((System.currentTimeMillis() / 1000 )+ 120, TransactionPreconditions.TIMEOUT_INFINITE))
                            .build())
                    .setBaseFee(Transaction.MIN_BASE_FEE)
                    .setTimeout(1); // sat max time to 1 second from now
            fail();
        } catch (IllegalArgumentException exception){
            assertTrue(exception.getMessage().contains("minTime must be >= maxTime"));
        }
    }

    @Test
    public void testBuilderUsesCustomSequence() throws IOException {
        Account account = new Account(KeyPair.random().getAccountId(), 2908908335136768L);
        Transaction transaction = new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.TESTNET)
                .addOperation(new CreateAccountOperation.Builder(KeyPair.random().getAccountId(), "2000").build())
                .addPreconditions(TransactionPreconditions.builder()
                        .timeBounds(new TimeBounds(0L,TransactionPreconditions.TIMEOUT_INFINITE)).build())
                .addSequenceNumberStrategy(new SequenceNumberStrategy() {
                    @Override
                    public long getSequenceNumber(TransactionBuilderAccount account) {
                        return 5;
                    }

                    @Override
                    public void updateSourceAccount(long newSequenceNumber, TransactionBuilderAccount account) {
                        account.setSequenceNumber(newSequenceNumber);
                    }
                })
                .setBaseFee(Transaction.MIN_BASE_FEE)
                .build();

        // check that the created tx has the sequence number which custom sequence handler sets,
        // rather than using default of sourceAccount.seqNum + 1
        assertEquals(5, transaction.getSequenceNumber());

        // check that the sourceAccount.seqNum gets updated to what the custom sequence handler sets,
        // rather than the default of sourceAccount.seqNum + 1
        assertEquals(5, account.getSequenceNumber().longValue());
    }

    @Test
    public void testBuilderFailsWhenSettingTimeoutAndMaxTimeAlreadySet() throws IOException {
        Account account = new Account(KeyPair.random().getAccountId(), 2908908335136768L);
        try {
            new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.TESTNET)
                    .addOperation(new CreateAccountOperation.Builder(KeyPair.random().getAccountId(), "2000").build())
                    .addTimeBounds(new TimeBounds(42, 1337))
                    .setTimeout(10)
                    .build();
            fail();
        } catch (RuntimeException exception) {
            assertTrue(exception.getMessage().contains("TimeBounds.max_time has been already set"));
            assertEquals(new Long(2908908335136768L), account.getSequenceNumber());
        }
    }

    @Test
    public void testBuilderFailsWhenSettingTimeboundsAndAlreadySet() throws IOException {
        Account account = new Account(KeyPair.random().getAccountId(), 2908908335136768L);
        try {
            new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.TESTNET)
                    .addOperation(new CreateAccountOperation.Builder(KeyPair.random().getAccountId(), "2000").build())
                    .setTimeout(10)
                    .addTimeBounds(new TimeBounds(42, 1337))
                    .build();
            fail();
        } catch (RuntimeException exception) {
            assertTrue(exception.getMessage().contains("TimeBounds already set."));
            assertEquals(new Long(2908908335136768L), account.getSequenceNumber());
        }
    }

    @Test
    public void testBuilderTimeoutAndMaxTimeNotSet() throws IOException {
        Account account = new Account(KeyPair.random().getAccountId(), 2908908335136768L);
        long currentUnix = System.currentTimeMillis() / 1000L;
        Transaction transaction = new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.TESTNET)
                .addOperation(new CreateAccountOperation.Builder(KeyPair.random().getAccountId(), "2000").build())
                .addTimeBounds(new TimeBounds(42, 0))
                .setTimeout(10)
                .setBaseFee(Transaction.MIN_BASE_FEE)
                .build();

        assertEquals(42, transaction.getTimeBounds().getMinTime());
        assertTrue(currentUnix + 10 <= transaction.getTimeBounds().getMaxTime());
    }

    @Test
    public void testBuilderInfinteTimeoutAndMaxTimeNotSet() throws FormatException, IOException {
        // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
        KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
        KeyPair destination = KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

        Account account = new Account(source.getAccountId(), 2908908335136768L);
        Transaction transaction = new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.TESTNET)
                .addOperation(new CreateAccountOperation.Builder(destination.getAccountId(), "2000").build())
                .addTimeBounds(new TimeBounds(42, 0))
                .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE)
                .addMemo(Memo.hash("abcdef"))
                .setBaseFee(100)
                .build();

        transaction.sign(source);

        // Convert transaction to binary XDR and back again to make sure timebounds are correctly de/serialized.
        XdrDataInputStream is = new XdrDataInputStream(new ByteArrayInputStream(BaseEncoding.base64().decode(transaction.toEnvelopeXdrBase64())));
        org.stellar.sdk.xdr.TransactionEnvelope decodedTransaction = org.stellar.sdk.xdr.TransactionEnvelope.decode(is);

        assertEquals(decodedTransaction.getV1().getTx().getCond().getTimeBounds().getMinTime().getTimePoint().getUint64().longValue(), 42);
        assertEquals(decodedTransaction.getV1().getTx().getCond().getTimeBounds().getMaxTime().getTimePoint().getUint64().longValue(), 0);
    }

    @Test
    public void testBuilderSuccessPublic() throws FormatException, IOException {

        // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
        KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
        KeyPair destination = KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

        Account account = new Account(source.getAccountId(), 2908908335136768L);
        Transaction transaction = new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.PUBLIC)
                .addOperation(new CreateAccountOperation.Builder(destination.getAccountId(), "2000").build())
                .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE)
                .setBaseFee(Transaction.MIN_BASE_FEE)
                .build();

        transaction.sign(source);

        Transaction decodedTransaction = (Transaction) Transaction.fromEnvelopeXdr(AccountConverter.enableMuxed(), transaction.toEnvelopeXdrBase64(), Network.PUBLIC);
        assertEquals(
                "AAAAAgAAAABexSIg06FtXzmFBQQtHZsrnyWxUzmthkBEhs/ktoeVYgAAAGQAClWjAAAAAQAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAO3gUmG83C+VCqO6FztuMtXJF/l7grZA7MjRzqdZ9W8QAAAABKgXyAAAAAAAAAAAAbaHlWIAAABA830eT4ERYpuVnb6vSJnWTVhgcQVsGJED2vZeYogXbTy8wGb+qo/ojn0q6op7KBdF6y5MHSHGUFbad1UR4UaFBA==",
                transaction.toEnvelopeXdrBase64());

        assertEquals(decodedTransaction, transaction);
    }

    @Test
    public void testNoOperations() throws FormatException, IOException {
        // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
        KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");

        Account account = new Account(source.getAccountId(), 2908908335136768L);
        try {
            Transaction transaction = new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.TESTNET)
                .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE)
                .setBaseFee(Transaction.MIN_BASE_FEE)
                .build();
            fail();
        } catch (RuntimeException exception) {
            assertTrue(exception.getMessage().contains("At least one operation required"));
            assertEquals(new Long(2908908335136768L), account.getSequenceNumber());
        }
    }

    @Test
    public void testTryingToAddMemoTwice() throws FormatException, IOException {
        // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
        KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
        KeyPair destination = KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

        try {
            Account account = new Account(source.getAccountId(), 2908908335136768L);
            new TransactionBuilder(AccountConverter.enableMuxed(), account, Network.TESTNET)
                    .addOperation(new CreateAccountOperation.Builder(destination.getAccountId(), "2000").build())
                    .addMemo(Memo.none())
                    .addMemo(Memo.none());
            fail();
        } catch (RuntimeException exception) {
            assertTrue(exception.getMessage().contains("Memo has been already added."));
        }
    }

    @Test
    public void testNoNetworkSet() throws FormatException {
        // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
        KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
        KeyPair destination = KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

        Account account = new Account(source.getAccountId(), 2908908335136768L);
        try {
            new TransactionBuilder(AccountConverter.enableMuxed(), account, null)
                    .addOperation(new CreateAccountOperation.Builder(destination.getAccountId(), "2000").build())
                    .addMemo(Memo.none())
                    .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE)
                    .build();
            fail();
        } catch (NullPointerException e) {
            assertTrue(e.getMessage().contains("Network cannot be null"));
        }
    }
}