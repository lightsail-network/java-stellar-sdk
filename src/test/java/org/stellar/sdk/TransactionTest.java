package org.stellar.sdk;

import org.junit.Before;
import org.junit.Test;
import org.stellar.sdk.xdr.XdrDataInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TransactionTest {

  @Before
  public void setupNetwork() {
    Network.useTestNetwork();
  }

  @Test
  public void testBuilderSuccessTestnet() throws FormatException {
    // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
    KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    KeyPair destination = KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

    long sequenceNumber = 2908908335136768L;
    Account account = new Account(source, sequenceNumber);
    Transaction transaction = new Transaction.Builder(account)
            .addOperation(new CreateAccountOperation.Builder(destination, "2000").build())
            .build();

    transaction.sign(source);

    assertEquals(
            "AAAAAF7FIiDToW1fOYUFBC0dmyufJbFTOa2GQESGz+S2h5ViAAAAZAAKVaMAAAABAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAA7eBSYbzcL5UKo7oXO24y1ckX+XuCtkDsyNHOp1n1bxAAAAAEqBfIAAAAAAAAAAABtoeVYgAAAEDLki9Oi700N60Lo8gUmEFHbKvYG4QSqXiLIt9T0ru2O5BphVl/jR9tYtHAD+UeDYhgXNgwUxqTEu1WukvEyYcD",
            transaction.toEnvelopeXdrBase64());

    assertEquals(transaction.getSourceAccount(), source);
    assertEquals(transaction.getSequenceNumber(), sequenceNumber+1);
    assertEquals(transaction.getFee(), 100);
  }

  @Test
  public void testBuilderMemoText() throws FormatException {
    // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
    KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    KeyPair destination = KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

    Account account = new Account(source, 2908908335136768L);
    Transaction transaction = new Transaction.Builder(account)
            .addOperation(new CreateAccountOperation.Builder(destination, "2000").build())
            .addMemo(Memo.text("Hello world!"))
            .build();

    transaction.sign(source);

    assertEquals(
            "AAAAAF7FIiDToW1fOYUFBC0dmyufJbFTOa2GQESGz+S2h5ViAAAAZAAKVaMAAAABAAAAAAAAAAEAAAAMSGVsbG8gd29ybGQhAAAAAQAAAAAAAAAAAAAAAO3gUmG83C+VCqO6FztuMtXJF/l7grZA7MjRzqdZ9W8QAAAABKgXyAAAAAAAAAAAAbaHlWIAAABAxzofBhoayuUnz8t0T1UNWrTgmJ+lCh9KaeOGu2ppNOz9UGw0abGLhv+9oWQsstaHx6YjwWxL+8GBvwBUVWRlBQ==",
            transaction.toEnvelopeXdrBase64());
  }
  
  @Test
  public void testBuilderTimeBounds() throws FormatException, IOException {
    // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
    KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    KeyPair destination = KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

    Account account = new Account(source, 2908908335136768L);
    Transaction transaction = new Transaction.Builder(account)
            .addOperation(new CreateAccountOperation.Builder(destination, "2000").build())
            .addTimeBounds(new TimeBounds(42, 1337))
            .build();

    transaction.sign(source);
    
    // Convert transaction to binary XDR and back again to make sure timebounds are correctly de/serialized.
    XdrDataInputStream is = new XdrDataInputStream(
    		new ByteArrayInputStream(
    				javax.xml.bind.DatatypeConverter.parseBase64Binary(transaction.toEnvelopeXdrBase64())
    		)
    );
    org.stellar.sdk.xdr.Transaction decodedTransaction = org.stellar.sdk.xdr.Transaction.decode(is);

    assertEquals(decodedTransaction.getTimeBounds().getMinTime().getUint64().longValue(), 42);
    assertEquals(decodedTransaction.getTimeBounds().getMaxTime().getUint64().longValue(), 1337);
  }

  @Test
  public void testBuilderSuccessPublic() throws FormatException {
    Network.usePublicNetwork();

    // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
    KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    KeyPair destination = KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

    Account account = new Account(source, 2908908335136768L);
    Transaction transaction = new Transaction.Builder(account)
            .addOperation(new CreateAccountOperation.Builder(destination, "2000").build())
            .build();

    transaction.sign(source);

    assertEquals(
            "AAAAAF7FIiDToW1fOYUFBC0dmyufJbFTOa2GQESGz+S2h5ViAAAAZAAKVaMAAAABAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAA7eBSYbzcL5UKo7oXO24y1ckX+XuCtkDsyNHOp1n1bxAAAAAEqBfIAAAAAAAAAAABtoeVYgAAAEDzfR5PgRFim5Wdvq9ImdZNWGBxBWwYkQPa9l5iiBdtPLzAZv6qj+iOfSrqinsoF0XrLkwdIcZQVtp3VRHhRoUE",
            transaction.toEnvelopeXdrBase64());
  }

  @Test
  public void testSha256HashSigning() throws FormatException {
    Network.usePublicNetwork();

    KeyPair source = KeyPair.fromAccountId("GBBM6BKZPEHWYO3E3YKREDPQXMS4VK35YLNU7NFBRI26RAN7GI5POFBB");
    KeyPair destination = KeyPair.fromAccountId("GDJJRRMBK4IWLEPJGIE6SXD2LP7REGZODU7WDC3I2D6MR37F4XSHBKX2");

    Account account = new Account(source, 0L);
    Transaction transaction = new Transaction.Builder(account)
            .addOperation(new PaymentOperation.Builder(destination, new AssetTypeNative(), "2000").build())
            .build();

    byte[] preimage = new byte[64];
    new SecureRandom().nextBytes(preimage);
    byte[] hash = Util.hash(preimage);

    transaction.sign(preimage);

    assertTrue(Arrays.equals(transaction.getSignatures().get(0).getSignature().getSignature(), preimage));
    assertTrue(Arrays.equals(transaction.getSignatures().get(0).getHint().getSignatureHint(), Arrays.copyOfRange(hash, hash.length - 4, hash.length)));
  }

  @Test
  public void testToBase64EnvelopeXdrBuilderNoSignatures() throws FormatException, IOException {
    // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
    KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    KeyPair destination = KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

    Account account = new Account(source, 2908908335136768L);
    Transaction transaction = new Transaction.Builder(account)
            .addOperation(new CreateAccountOperation.Builder(destination, "2000").build())
            .build();

    try {
      transaction.toEnvelopeXdrBase64();
      fail();
    } catch (RuntimeException exception) {
      assertTrue(exception.getMessage().contains("Transaction must be signed by at least one signer."));
    }
  }

  @Test
  public void testFromEnvelopeXdrBase64Success() {
    KeyPair source = KeyPair.random();
    KeyPair destination = KeyPair.random();

    Account account = new Account(source, Math.abs(new Random().nextLong()));
    Transaction transaction = new Transaction.Builder(account)
            .addOperation(new CreateAccountOperation.Builder(destination, "2000").build())
            .addOperation(new PaymentOperation.Builder(destination, new AssetTypeNative(), "5000").build())
            .build();
    transaction.sign(source);

    Transaction actual = Transaction.fromEnvelopeXdrBase64(transaction.toEnvelopeXdrBase64());

    assertEquals(transaction, actual);
  }

  @Test
  public void testNoOperations() throws FormatException, IOException {
    // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
    KeyPair source = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");

    Account account = new Account(source, 2908908335136768L);
    try {
      Transaction transaction = new Transaction.Builder(account).build();
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
      Account account = new Account(source, 2908908335136768L);
      new Transaction.Builder(account)
              .addOperation(new CreateAccountOperation.Builder(destination, "2000").build())
              .addMemo(Memo.none())
              .addMemo(Memo.none());
      fail();
    } catch (RuntimeException exception) {
      assertTrue(exception.getMessage().contains("Memo has been already added."));
    }
  }
}
