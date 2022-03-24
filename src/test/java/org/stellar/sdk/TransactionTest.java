package org.stellar.sdk;

import com.google.common.io.BaseEncoding;
import org.junit.Test;
import org.stellar.sdk.xdr.EnvelopeType;
import org.stellar.sdk.xdr.PreconditionsV2;
import org.stellar.sdk.xdr.XdrDataInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TransactionTest {

    @Test
    public void testParseV0Transaction() throws FormatException, IOException {

        // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
        KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
        KeyPair destination = KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

        Account account = new Account(source.getAccountId(), 2908908335136768L);

        Transaction transaction = new Transaction(
                AccountConverter.disableMuxed(),
                account.getAccountId(),
                Transaction.MIN_BASE_FEE,
                account.getIncrementedSequenceNumber(),
                new org.stellar.sdk.Operation[]{new CreateAccountOperation.Builder(destination.getAccountId(), "2000").build()},
                Memo.none(),
                new PreconditionsV2.Builder().timeBounds(new TimeBounds(0L,TransactionBuilder.TIMEOUT_INFINITE).toXdr()).build(),
                Network.PUBLIC
        );

        transaction.setEnvelopeType(EnvelopeType.ENVELOPE_TYPE_TX_V0);
        transaction.sign(source);

        XdrDataInputStream is = new XdrDataInputStream(new ByteArrayInputStream(BaseEncoding.base64().decode(transaction.toEnvelopeXdrBase64())));
        org.stellar.sdk.xdr.TransactionEnvelope decodedTransaction = org.stellar.sdk.xdr.TransactionEnvelope.decode(is);
        assertEquals(EnvelopeType.ENVELOPE_TYPE_TX_V0, decodedTransaction.getDiscriminant());

        Transaction parsed = (Transaction) Transaction.fromEnvelopeXdr(AccountConverter.enableMuxed(), transaction.toEnvelopeXdrBase64(), Network.PUBLIC);
        assertTrue(parsed.equals(transaction));
        assertEquals(EnvelopeType.ENVELOPE_TYPE_TX_V0, parsed.toEnvelopeXdr().getDiscriminant());
        assertEquals(transaction.toEnvelopeXdrBase64(), parsed.toEnvelopeXdrBase64());
    }

    @Test
    public void testSha256HashSigning() throws FormatException {
        KeyPair source = KeyPair.fromAccountId("GBBM6BKZPEHWYO3E3YKREDPQXMS4VK35YLNU7NFBRI26RAN7GI5POFBB");
        KeyPair destination = KeyPair.fromAccountId("GDJJRRMBK4IWLEPJGIE6SXD2LP7REGZODU7WDC3I2D6MR37F4XSHBKX2");

        Account account = new Account(source.getAccountId(), 0L);

        Transaction transaction = new Transaction(
                AccountConverter.disableMuxed(),
                account.getAccountId(),
                Transaction.MIN_BASE_FEE,
                account.getIncrementedSequenceNumber(),
                new org.stellar.sdk.Operation[]{new PaymentOperation.Builder(destination.getAccountId(), new AssetTypeNative(), "2000").build()},
                Memo.none(),
                new PreconditionsV2.Builder().timeBounds(new TimeBounds(0L,TransactionBuilder.TIMEOUT_INFINITE).toXdr()).build(),
                Network.PUBLIC
        );

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

        Transaction transaction = new Transaction(
                AccountConverter.disableMuxed(),
                account.getAccountId(),
                Transaction.MIN_BASE_FEE,
                account.getIncrementedSequenceNumber(),
                new org.stellar.sdk.Operation[]{new CreateAccountOperation.Builder(destination.getAccountId(), "2000").build()},
                Memo.none(),
                new PreconditionsV2.Builder().timeBounds(new TimeBounds(0L,TransactionBuilder.TIMEOUT_INFINITE).toXdr()).build(),
                Network.TESTNET
        );

        Transaction parsed = (Transaction) Transaction.fromEnvelopeXdr(AccountConverter.enableMuxed(), transaction.toEnvelopeXdrBase64(), Network.TESTNET);
        assertEquals(parsed, transaction);
    }
}