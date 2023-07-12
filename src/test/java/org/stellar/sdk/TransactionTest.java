package org.stellar.sdk;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.common.io.BaseEncoding;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;
import org.stellar.sdk.xdr.DecoratedSignature;
import org.stellar.sdk.xdr.EnvelopeType;
import org.stellar.sdk.xdr.SignerKey;
import org.stellar.sdk.xdr.XdrDataInputStream;

public class TransactionTest {

  @Test
  public void testParseV0Transaction() throws FormatException, IOException {

    // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    KeyPair destination =
        KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

    Account account = new Account(source.getAccountId(), 2908908335136768L);

    Transaction transaction =
        new Transaction(
            AccountConverter.enableMuxed(),
            account.getAccountId(),
            Transaction.MIN_BASE_FEE,
            account.getIncrementedSequenceNumber(),
            new org.stellar.sdk.Operation[] {
              new CreateAccountOperation.Builder(destination.getAccountId(), "2000").build()
            },
            null,
            new TransactionPreconditions(null, null, 0, 0, new ArrayList<SignerKey>(), null),
            Network.PUBLIC);

    transaction.setEnvelopeType(EnvelopeType.ENVELOPE_TYPE_TX_V0);
    transaction.sign(source);

    XdrDataInputStream is =
        new XdrDataInputStream(
            new ByteArrayInputStream(
                BaseEncoding.base64().decode(transaction.toEnvelopeXdrBase64())));
    org.stellar.sdk.xdr.TransactionEnvelope decodedTransaction =
        org.stellar.sdk.xdr.TransactionEnvelope.decode(is);
    assertEquals(EnvelopeType.ENVELOPE_TYPE_TX_V0, decodedTransaction.getDiscriminant());

    Transaction parsed =
        (Transaction)
            Transaction.fromEnvelopeXdr(
                AccountConverter.enableMuxed(), transaction.toEnvelopeXdrBase64(), Network.PUBLIC);
    assertTrue(parsed.equals(transaction));
    assertEquals(EnvelopeType.ENVELOPE_TYPE_TX_V0, parsed.toEnvelopeXdr().getDiscriminant());
    assertEquals(transaction.toEnvelopeXdrBase64(), parsed.toEnvelopeXdrBase64());

    assertEquals(
        "AAAAAF7FIiDToW1fOYUFBC0dmyufJbFTOa2GQESGz+S2h5ViAAAAZAAKVaMAAAABAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAA7eBSYbzcL5UKo7oXO24y1ckX+XuCtkDsyNHOp1n1bxAAAAAEqBfIAAAAAAAAAAABtoeVYgAAAEDzfR5PgRFim5Wdvq9ImdZNWGBxBWwYkQPa9l5iiBdtPLzAZv6qj+iOfSrqinsoF0XrLkwdIcZQVtp3VRHhRoUE",
        transaction.toEnvelopeXdrBase64());
  }

  @Test
  public void testAddingSignaturesDirectly() {
    KeyPair source =
        KeyPair.fromAccountId("GBBM6BKZPEHWYO3E3YKREDPQXMS4VK35YLNU7NFBRI26RAN7GI5POFBB");
    KeyPair destination =
        KeyPair.fromAccountId("GDJJRRMBK4IWLEPJGIE6SXD2LP7REGZODU7WDC3I2D6MR37F4XSHBKX2");

    Account account = new Account(source.getAccountId(), 0L);

    Transaction transaction =
        new Transaction(
            AccountConverter.disableMuxed(),
            account.getAccountId(),
            Transaction.MIN_BASE_FEE,
            account.getIncrementedSequenceNumber(),
            new org.stellar.sdk.Operation[] {
              new CreateAccountOperation.Builder(destination.getAccountId(), "2000").build()
            },
            null,
            new TransactionPreconditions(null, null, 0, 0, new ArrayList<SignerKey>(), null),
            Network.PUBLIC);

    assertEquals(0, transaction.getSignatures().size());
    try {
      // should not be able to change the list of signatures directly
      transaction.getSignatures().add(new DecoratedSignature());
      fail();
    } catch (UnsupportedOperationException ignored) {
    }

    // should only be able to add signatures through interface
    transaction.addSignature(new DecoratedSignature());
    assertEquals(1, transaction.getSignatures().size());
  }

  @Test
  public void testSha256HashSigning() throws FormatException {
    KeyPair source =
        KeyPair.fromAccountId("GBBM6BKZPEHWYO3E3YKREDPQXMS4VK35YLNU7NFBRI26RAN7GI5POFBB");
    KeyPair destination =
        KeyPair.fromAccountId("GDJJRRMBK4IWLEPJGIE6SXD2LP7REGZODU7WDC3I2D6MR37F4XSHBKX2");

    Account account = new Account(source.getAccountId(), 0L);

    Transaction transaction =
        new Transaction(
            AccountConverter.disableMuxed(),
            account.getAccountId(),
            Transaction.MIN_BASE_FEE,
            account.getIncrementedSequenceNumber(),
            new org.stellar.sdk.Operation[] {
              new PaymentOperation.Builder(
                      destination.getAccountId(), new AssetTypeNative(), "2000")
                  .build()
            },
            null,
            new TransactionPreconditions(null, null, 0, 0, new ArrayList<SignerKey>(), null),
            Network.PUBLIC);

    byte[] preimage = new byte[64];
    new SecureRandom().nextBytes(preimage);
    byte[] hash = Util.hash(preimage);

    transaction.sign(preimage);

    assertArrayEquals(transaction.getSignatures().get(0).getSignature().getSignature(), preimage);
    assertArrayEquals(
        transaction.getSignatures().get(0).getHint().getSignatureHint(),
        Arrays.copyOfRange(hash, hash.length - 4, hash.length));
  }

  @Test
  public void testToBase64EnvelopeXdrBuilderNoSignatures() throws FormatException, IOException {
    // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    KeyPair destination =
        KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

    Account account = new Account(source.getAccountId(), 2908908335136768L);

    Transaction transaction =
        new Transaction(
            AccountConverter.enableMuxed(),
            account.getAccountId(),
            Transaction.MIN_BASE_FEE,
            account.getIncrementedSequenceNumber(),
            new org.stellar.sdk.Operation[] {
              new CreateAccountOperation.Builder(destination.getAccountId(), "2000").build()
            },
            null,
            new TransactionPreconditions(null, null, 0, 0, new ArrayList<SignerKey>(), null),
            Network.TESTNET);

    Transaction parsed =
        (Transaction)
            Transaction.fromEnvelopeXdr(
                AccountConverter.enableMuxed(), transaction.toEnvelopeXdrBase64(), Network.TESTNET);
    assertEquals(parsed, transaction);
    assertEquals(
        "AAAAAgAAAABexSIg06FtXzmFBQQtHZsrnyWxUzmthkBEhs/ktoeVYgAAAGQAClWjAAAAAQAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAO3gUmG83C+VCqO6FztuMtXJF/l7grZA7MjRzqdZ9W8QAAAABKgXyAAAAAAAAAAAAA==",
        transaction.toEnvelopeXdrBase64());
  }
}
