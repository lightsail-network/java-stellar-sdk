package org.stellar.sdk;

import org.junit.Test;
import org.stellar.sdk.xdr.EnvelopeType;
import org.stellar.sdk.xdr.XdrDataInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;

import static org.junit.Assert.*;

public class TransactionTest {

    @Test
    public void testMissingOperationFee() {
        long sequenceNumber = 2908908335136768L;
        Account account = new Account("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR", sequenceNumber);
        try {
            new Transaction.Builder(AccountConverter.enableMuxed(), account, Network.TESTNET)
                .addOperation(new CreateAccountOperation.Builder("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR", "2000").build())
                .setTimeout(Transaction.Builder.TIMEOUT_INFINITE)
                .build();
            fail("expected RuntimeException");
        } catch (RuntimeException e) {
            // expected
        }
    }

        @Test
    public void testBuilderSuccessTestnet() throws FormatException {
        // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
        KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
        KeyPair destination = KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

        long sequenceNumber = 2908908335136768L;
        Account account = new Account(source.getAccountId(), sequenceNumber);
        Transaction transaction = new Transaction.Builder(AccountConverter.enableMuxed(), account, Network.TESTNET)
                .addOperation(new CreateAccountOperation.Builder(destination.getAccountId(), "2000").build())
                .setTimeout(Transaction.Builder.TIMEOUT_INFINITE)
                .setBaseFee(Transaction.MIN_BASE_FEE)
                .build();

        transaction.sign(source);

        assertEquals(
                "AAAAAgAAAABexSIg06FtXzmFBQQtHZsrnyWxUzmthkBEhs/ktoeVYgAAAGQAClWjAAAAAQAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAO3gUmG83C+VCqO6FztuMtXJF/l7grZA7MjRzqdZ9W8QAAAABKgXyAAAAAAAAAAAAbaHlWIAAABAy5IvTou9NDetC6PIFJhBR2yr2BuEEql4iyLfU9K7tjuQaYVZf40fbWLRwA/lHg2IYFzYMFMakxLtVrpLxMmHAw==",
                transaction.toEnvelopeXdrBase64());

        assertEquals(transaction.getSourceAccount(), source.getAccountId());
        assertEquals(transaction.getSequenceNumber(), sequenceNumber + 1);
        assertEquals(transaction.getFee(), 100);

        Transaction transaction2 = (Transaction)Transaction.fromEnvelopeXdr(AccountConverter.enableMuxed(), transaction.toEnvelopeXdr(), Network.TESTNET);

        assertEquals(transaction.getSourceAccount(), transaction2.getSourceAccount());
        assertEquals(transaction.getSequenceNumber(), transaction2.getSequenceNumber());
        assertEquals(transaction.getFee(), transaction2.getFee());
        assertEquals(
                ((CreateAccountOperation) transaction.getOperations()[0]).getStartingBalance(),
                ((CreateAccountOperation) transaction2.getOperations()[0]).getStartingBalance()
        );

        assertEquals(transaction.getSignatures(), transaction2.getSignatures());
    }

    @Test
    public void testBuilderMemoText() throws FormatException {
        // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
        KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
        KeyPair destination = KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

        Account account = new Account(source.getAccountId(), 2908908335136768L);
        Transaction transaction = new Transaction.Builder(AccountConverter.enableMuxed(), account, Network.TESTNET)
                .addOperation(new CreateAccountOperation.Builder(destination.getAccountId(), "2000").build())
                .addMemo(Memo.text("Hello world!"))
                .setTimeout(Transaction.Builder.TIMEOUT_INFINITE)
                .setBaseFee(Transaction.MIN_BASE_FEE)
                .build();

        transaction.sign(source);

        assertEquals(
                "AAAAAgAAAABexSIg06FtXzmFBQQtHZsrnyWxUzmthkBEhs/ktoeVYgAAAGQAClWjAAAAAQAAAAAAAAABAAAADEhlbGxvIHdvcmxkIQAAAAEAAAAAAAAAAAAAAADt4FJhvNwvlQqjuhc7bjLVyRf5e4K2QOzI0c6nWfVvEAAAAASoF8gAAAAAAAAAAAG2h5ViAAAAQMc6HwYaGsrlJ8/LdE9VDVq04JifpQofSmnjhrtqaTTs/VBsNGmxi4b/vaFkLLLWh8emI8FsS/vBgb8AVFVkZQU=",
                transaction.toEnvelopeXdrBase64());

        Transaction transaction2 = (Transaction)Transaction.fromEnvelopeXdr(AccountConverter.enableMuxed(), transaction.toEnvelopeXdr(), Network.TESTNET);

        assertEquals(transaction.getSourceAccount(), transaction2.getSourceAccount());
        assertEquals(transaction.getSequenceNumber(), transaction2.getSequenceNumber());
        assertEquals(transaction.getMemo(), transaction2.getMemo());
        assertEquals(transaction.getFee(), transaction2.getFee());
        assertEquals(
                ((CreateAccountOperation) transaction.getOperations()[0]).getStartingBalance(),
                ((CreateAccountOperation) transaction2.getOperations()[0]).getStartingBalance()
        );
    }

    @Test
    public void testBuilderTimeBounds() throws FormatException, IOException {
        // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
        KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
        KeyPair destination = KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

        Account account = new Account(source.getAccountId(), 2908908335136768L);
        Transaction transaction = new Transaction.Builder(AccountConverter.enableMuxed(), account, Network.TESTNET)
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

        assertEquals(decodedTransaction.getV1().getTx().getTimeBounds().getMinTime().getTimePoint().getUint64().longValue(), 42);
        assertEquals(decodedTransaction.getV1().getTx().getTimeBounds().getMaxTime().getTimePoint().getUint64().longValue(), 1337);

        Transaction transaction2 = (Transaction)Transaction.fromEnvelopeXdr(AccountConverter.enableMuxed(), transaction.toEnvelopeXdr(), Network.TESTNET);

        assertEquals(transaction.getSourceAccount(), transaction2.getSourceAccount());
        assertEquals(transaction.getSequenceNumber(), transaction2.getSequenceNumber());
        assertEquals(transaction.getMemo(), transaction2.getMemo());
        assertEquals(transaction.getTimeBounds(), transaction2.getTimeBounds());
        assertEquals(transaction.getFee(), transaction2.getFee());
        assertEquals(
                ((CreateAccountOperation) transaction.getOperations()[0]).getStartingBalance(),
                ((CreateAccountOperation) transaction2.getOperations()[0]).getStartingBalance()
        );
    }

    @Test
    public void testBuilderBaseFee() throws FormatException {
        // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
        KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
        KeyPair destination = KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

        Account account = new Account(source.getAccountId(), 2908908335136768L);
        Transaction transaction = new Transaction.Builder(AccountConverter.enableMuxed(), account, Network.TESTNET)
                .addOperation(new CreateAccountOperation.Builder(destination.getAccountId(), "2000").build())
                .setBaseFee(200)
                .setTimeout(Transaction.Builder.TIMEOUT_INFINITE)
                .build();

        transaction.sign(source);

        assertEquals(
                "AAAAAgAAAABexSIg06FtXzmFBQQtHZsrnyWxUzmthkBEhs/ktoeVYgAAAMgAClWjAAAAAQAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAO3gUmG83C+VCqO6FztuMtXJF/l7grZA7MjRzqdZ9W8QAAAABKgXyAAAAAAAAAAAAbaHlWIAAABA9TG3dKKLtLHzRUbsbEqr68CfUc800p1/LE5pWzCnFdFdypdXgyqHqw/sWdaTUMDiWawBtsmqV8oOtD0Hw1HDDQ==",
                transaction.toEnvelopeXdrBase64());

        Transaction transaction2 = (Transaction)Transaction.fromEnvelopeXdr(AccountConverter.enableMuxed(), transaction.toEnvelopeXdr(), Network.TESTNET);

        assertEquals(transaction.getSourceAccount(), transaction2.getSourceAccount());
        assertEquals(transaction.getSequenceNumber(), transaction2.getSequenceNumber());
        assertEquals(transaction.getFee(), transaction2.getFee());
        assertEquals(
                ((CreateAccountOperation) transaction.getOperations()[0]).getStartingBalance(),
                ((CreateAccountOperation) transaction2.getOperations()[0]).getStartingBalance()
        );
    }

    @Test
    public void testBuilderBaseFeeThrows() throws FormatException {
        // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
        KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");

        Account account = new Account(source.getAccountId(), 2908908335136768L);
        Transaction.Builder builder = new Transaction.Builder(AccountConverter.enableMuxed(), account, Network.TESTNET);
        try {
            builder.setBaseFee(99);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testBuilderWithTimeBoundsButNoTimeout() throws IOException {
        Account account = new Account(KeyPair.random().getAccountId(), 2908908335136768L);
        new Transaction.Builder(AccountConverter.enableMuxed(), account, Network.TESTNET)
                .addOperation(new CreateAccountOperation.Builder(KeyPair.random().getAccountId(), "2000").build())
                .addTimeBounds(new TimeBounds(42, 1337))
                .addMemo(Memo.hash("abcdef"))
                .setBaseFee(Transaction.MIN_BASE_FEE)
                .build();
    }

    @Test
    public void testBuilderRequiresTimeoutOrTimeBounds() throws IOException {
        Account account = new Account(KeyPair.random().getAccountId(), 2908908335136768L);
        try {
            new Transaction.Builder(AccountConverter.enableMuxed(), account, Network.TESTNET)
                    .addOperation(new CreateAccountOperation.Builder(KeyPair.random().getAccountId(), "2000").build())
                    .addMemo(Memo.hash("abcdef"))
                    .setBaseFee(Transaction.MIN_BASE_FEE)
                    .build();
            fail();
        } catch (RuntimeException exception) {
            assertEquals(
                    exception.getMessage(),
                    "TimeBounds has to be set or you must call setTimeout(TIMEOUT_INFINITE)."
            );
        }
    }


    @Test
    public void testBuilderTimeoutNegative() throws IOException {
        Account account = new Account(KeyPair.random().getAccountId(), 2908908335136768L);
        try {
            new Transaction.Builder(AccountConverter.enableMuxed(), account, Network.TESTNET)
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
    public void testBuilderTimeoutSetsTimeBounds() throws IOException {
        Account account = new Account(KeyPair.random().getAccountId(), 2908908335136768L);
        Transaction transaction = new Transaction.Builder(AccountConverter.enableMuxed(), account, Network.TESTNET)
                .addOperation(new CreateAccountOperation.Builder(KeyPair.random().getAccountId(), "2000").build())
                .setTimeout(10)
                .setBaseFee(Transaction.MIN_BASE_FEE)
                .build();

        assertEquals(0, transaction.getTimeBounds().getMinTime());
        long currentUnix = System.currentTimeMillis() / 1000L;
        assertEquals(currentUnix + 10, transaction.getTimeBounds().getMaxTime());
    }

    @Test
    public void testBuilderFailsWhenSettingTimeoutAndMaxTimeAlreadySet() throws IOException {
        Account account = new Account(KeyPair.random().getAccountId(), 2908908335136768L);
        try {
            new Transaction.Builder(AccountConverter.enableMuxed(), account, Network.TESTNET)
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
    public void testBuilderFailsWhenSettingTimeoutAndMaxTimeNotSet() throws IOException {
        Account account = new Account(KeyPair.random().getAccountId(), 2908908335136768L);
        Transaction transaction = new Transaction.Builder(AccountConverter.enableMuxed(), account, Network.TESTNET)
                .addOperation(new CreateAccountOperation.Builder(KeyPair.random().getAccountId(), "2000").build())
                .addTimeBounds(new TimeBounds(42, 0))
                .setTimeout(10)
                .setBaseFee(Transaction.MIN_BASE_FEE)
                .build();

        assertEquals(42, transaction.getTimeBounds().getMinTime());
        // Should add max_time
        long currentUnix = System.currentTimeMillis() / 1000L;
        assertEquals(currentUnix + 10, transaction.getTimeBounds().getMaxTime());
    }

    @Test
    public void testBuilderTimeBoundsNoMaxTime() throws FormatException, IOException {
        // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
        KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
        KeyPair destination = KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

        Account account = new Account(source.getAccountId(), 2908908335136768L);
        Transaction transaction = new Transaction.Builder(AccountConverter.enableMuxed(), account, Network.TESTNET)
                .addOperation(new CreateAccountOperation.Builder(destination.getAccountId(), "2000").build())
                .addTimeBounds(new TimeBounds(42, 0))
                .setTimeout(Transaction.Builder.TIMEOUT_INFINITE)
                .addMemo(Memo.hash("abcdef"))
                .setBaseFee(100)
                .build();

        transaction.sign(source);

        // Convert transaction to binary XDR and back again to make sure timebounds are correctly de/serialized.
        XdrDataInputStream is = new XdrDataInputStream(
                new ByteArrayInputStream(
                        javax.xml.bind.DatatypeConverter.parseBase64Binary(transaction.toEnvelopeXdrBase64())
                )
        );
        org.stellar.sdk.xdr.TransactionEnvelope decodedTransaction = org.stellar.sdk.xdr.TransactionEnvelope.decode(is);

        assertEquals(decodedTransaction.getV1().getTx().getTimeBounds().getMinTime().getTimePoint().getUint64().longValue(), 42);
        assertEquals(decodedTransaction.getV1().getTx().getTimeBounds().getMaxTime().getTimePoint().getUint64().longValue(), 0);
    }

    @Test
    public void testBuilderSuccessPublic() throws FormatException, IOException {

        // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
        KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
        KeyPair destination = KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

        Account account = new Account(source.getAccountId(), 2908908335136768L);
        Transaction transaction = new Transaction.Builder(AccountConverter.enableMuxed(), account, Network.PUBLIC)
                .addOperation(new CreateAccountOperation.Builder(destination.getAccountId(), "2000").build())
                .setTimeout(Transaction.Builder.TIMEOUT_INFINITE)
                .setBaseFee(Transaction.MIN_BASE_FEE)
                .build();

        transaction.sign(source);

        XdrDataInputStream is = new XdrDataInputStream(
            new ByteArrayInputStream(
                javax.xml.bind.DatatypeConverter.parseBase64Binary(transaction.toEnvelopeXdrBase64())
            )
        );
        org.stellar.sdk.xdr.TransactionEnvelope decodedTransaction = org.stellar.sdk.xdr.TransactionEnvelope.decode(is);
        assertEquals(EnvelopeType.ENVELOPE_TYPE_TX, decodedTransaction.getDiscriminant());

        assertEquals(
                "AAAAAgAAAABexSIg06FtXzmFBQQtHZsrnyWxUzmthkBEhs/ktoeVYgAAAGQAClWjAAAAAQAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAO3gUmG83C+VCqO6FztuMtXJF/l7grZA7MjRzqdZ9W8QAAAABKgXyAAAAAAAAAAAAbaHlWIAAABA830eT4ERYpuVnb6vSJnWTVhgcQVsGJED2vZeYogXbTy8wGb+qo/ojn0q6op7KBdF6y5MHSHGUFbad1UR4UaFBA==",
                transaction.toEnvelopeXdrBase64());
    }

    @Test
    public void testParseV0Transaction() throws FormatException, IOException {

        // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
        KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
        KeyPair destination = KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

        Account account = new Account(source.getAccountId(), 2908908335136768L);
        Transaction transaction = new Transaction.Builder(AccountConverter.enableMuxed(), account, Network.PUBLIC)
            .addOperation(new CreateAccountOperation.Builder(destination.getAccountId(), "2000").build())
            .setTimeout(Transaction.Builder.TIMEOUT_INFINITE)
            .setBaseFee(Transaction.MIN_BASE_FEE)
            .build();
        transaction.setEnvelopeType(EnvelopeType.ENVELOPE_TYPE_TX_V0);
        transaction.sign(source);

        XdrDataInputStream is = new XdrDataInputStream(
            new ByteArrayInputStream(
                javax.xml.bind.DatatypeConverter.parseBase64Binary(transaction.toEnvelopeXdrBase64())
            )
        );
        org.stellar.sdk.xdr.TransactionEnvelope decodedTransaction = org.stellar.sdk.xdr.TransactionEnvelope.decode(is);
        assertEquals(EnvelopeType.ENVELOPE_TYPE_TX_V0, decodedTransaction.getDiscriminant());

        Transaction parsed = (Transaction) Transaction.fromEnvelopeXdr(AccountConverter.enableMuxed(), transaction.toEnvelopeXdrBase64(), Network.PUBLIC);
        assertTrue(parsed.equals(transaction));
        assertEquals(EnvelopeType.ENVELOPE_TYPE_TX_V0, parsed.toEnvelopeXdr().getDiscriminant());

        assertEquals(
            "AAAAAF7FIiDToW1fOYUFBC0dmyufJbFTOa2GQESGz+S2h5ViAAAAZAAKVaMAAAABAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAA7eBSYbzcL5UKo7oXO24y1ckX+XuCtkDsyNHOp1n1bxAAAAAEqBfIAAAAAAAAAAABtoeVYgAAAEDzfR5PgRFim5Wdvq9ImdZNWGBxBWwYkQPa9l5iiBdtPLzAZv6qj+iOfSrqinsoF0XrLkwdIcZQVtp3VRHhRoUE",
            transaction.toEnvelopeXdrBase64());
        assertEquals(transaction.toEnvelopeXdrBase64(), parsed.toEnvelopeXdrBase64());
    }

    @Test
    public void testSha256HashSigning() throws FormatException {
        KeyPair source = KeyPair.fromAccountId("GBBM6BKZPEHWYO3E3YKREDPQXMS4VK35YLNU7NFBRI26RAN7GI5POFBB");
        KeyPair destination = KeyPair.fromAccountId("GDJJRRMBK4IWLEPJGIE6SXD2LP7REGZODU7WDC3I2D6MR37F4XSHBKX2");

        Account account = new Account(source.getAccountId(), 0L);
        Transaction transaction = new Transaction.Builder(AccountConverter.enableMuxed(), account, Network.PUBLIC)
                .addOperation(new PaymentOperation.Builder(destination.getAccountId(), new AssetTypeNative(), "2000").build())
                .setTimeout(Transaction.Builder.TIMEOUT_INFINITE)
                .setBaseFee(Transaction.MIN_BASE_FEE)
                .build();

        byte[] preimage = new byte[64];
        new SecureRandom().nextBytes(preimage);
        byte[] hash = Util.hash(preimage);

        transaction.sign(preimage);

        assertArrayEquals(transaction.getSignatures().get(0).getSignature().getSignature(), preimage);
        assertArrayEquals(
                transaction.getSignatures().get(0).getHint().getSignatureHint(),
                Arrays.copyOfRange(hash, hash.length - 4, hash.length)
        );
    }

    @Test
    public void testToBase64EnvelopeXdrBuilderNoSignatures() throws FormatException, IOException {
        // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
        KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
        KeyPair destination = KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

        Account account = new Account(source.getAccountId(), 2908908335136768L);
        Transaction transaction = new Transaction.Builder(AccountConverter.enableMuxed(), account, Network.TESTNET)
                .addOperation(new CreateAccountOperation.Builder(destination.getAccountId(), "2000").build())
                .setTimeout(Transaction.Builder.TIMEOUT_INFINITE)
                .setBaseFee(Transaction.MIN_BASE_FEE)
                .build();

        assertEquals(
                "AAAAAgAAAABexSIg06FtXzmFBQQtHZsrnyWxUzmthkBEhs/ktoeVYgAAAGQAClWjAAAAAQAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAO3gUmG83C+VCqO6FztuMtXJF/l7grZA7MjRzqdZ9W8QAAAABKgXyAAAAAAAAAAAAA==",
                transaction.toEnvelopeXdrBase64()
        );
    }

    @Test
    public void testNoOperations() throws FormatException, IOException {
        // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
        KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");

        Account account = new Account(source.getAccountId(), 2908908335136768L);
        try {
            Transaction transaction = new Transaction.Builder(AccountConverter.enableMuxed(), account, Network.TESTNET)
                .setTimeout(Transaction.Builder.TIMEOUT_INFINITE)
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
            new Transaction.Builder(AccountConverter.enableMuxed(), account, Network.TESTNET)
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
            new Transaction.Builder(AccountConverter.enableMuxed(), account, null)
                    .addOperation(new CreateAccountOperation.Builder(destination.getAccountId(), "2000").build())
                    .addMemo(Memo.none())
                    .setTimeout(Transaction.Builder.TIMEOUT_INFINITE)
                    .build();
            fail();
        } catch (NullPointerException e) {
            assertTrue(e.getMessage().contains("Network cannot be null"));
        }
    }
}