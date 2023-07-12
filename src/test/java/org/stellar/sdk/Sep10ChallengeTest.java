package org.stellar.sdk;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.common.collect.ImmutableList;
import com.google.common.io.BaseEncoding;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import org.stellar.sdk.xdr.DecoratedSignature;
import org.stellar.sdk.xdr.EnvelopeType;
import org.stellar.sdk.xdr.TransactionEnvelope;

public class Sep10ChallengeTest {

  @Test
  public void testChallenge() throws InvalidSep10ChallengeException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, Network.TESTNET, client.getAccountId(), domainName, webAuthDomain, timeBounds);

    assertEquals(Network.TESTNET, transaction.getNetwork());
    assertEquals(200, transaction.getFee());
    assertEquals(timeBounds, transaction.getTimeBounds());
    assertEquals(server.getAccountId(), transaction.getSourceAccount());
    assertEquals(0, transaction.getSequenceNumber());

    assertEquals(2, transaction.getOperations().length);
    ManageDataOperation homeDomainOp = (ManageDataOperation) transaction.getOperations()[0];
    assertEquals(client.getAccountId(), homeDomainOp.getSourceAccount());
    assertEquals(domainName + " auth", homeDomainOp.getName());

    assertEquals(64, homeDomainOp.getValue().length);
    BaseEncoding base64Encoding = BaseEncoding.base64();
    assertTrue(base64Encoding.canDecode(new String(homeDomainOp.getValue())));

    ManageDataOperation webAuthDomainOp = (ManageDataOperation) transaction.getOperations()[1];
    assertEquals(server.getAccountId(), webAuthDomainOp.getSourceAccount());
    assertEquals("web_auth_domain", webAuthDomainOp.getName());
    assertArrayEquals(webAuthDomain.getBytes(), webAuthDomainOp.getValue());

    assertEquals(1, transaction.getSignatures().size());
    assertTrue(
        server.verify(
            transaction.hash(), transaction.getSignatures().get(0).getSignature().getSignature()));
  }

  @Test
  public void testNewChallengeRejectsMuxedClientAccount() {
    try {
      KeyPair server = KeyPair.random();

      long now = System.currentTimeMillis() / 1000L;
      long end = now + 300;
      TimeBounds timeBounds = new TimeBounds(now, end);
      String domainName = "example.com";
      String webAuthDomain = "example.com";

      Sep10Challenge.newChallenge(
          server,
          Network.TESTNET,
          "MCAAAAAAAAAAAAB7BQ2L7E5NBWMXDUCMZSIPOBKRDSBYVLMXGSSKF6YNPIB7Y77ITKNOG",
          domainName,
          webAuthDomain,
          timeBounds);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals(
          "MCAAAAAAAAAAAAB7BQ2L7E5NBWMXDUCMZSIPOBKRDSBYVLMXGSSKF6YNPIB7Y77ITKNOG is not a valid account id",
          e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionValidSignedByServer()
      throws InvalidSep10ChallengeException, IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    Network network = Network.TESTNET;
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, network, client.getAccountId(), domainName, webAuthDomain, timeBounds);

    Sep10Challenge.ChallengeTransaction challengeTransaction =
        Sep10Challenge.readChallengeTransaction(
            transaction.toEnvelopeXdrBase64(),
            server.getAccountId(),
            Network.TESTNET,
            domainName,
            webAuthDomain);
    assertEquals(
        new Sep10Challenge.ChallengeTransaction(transaction, client.getAccountId(), domainName),
        challengeTransaction);
  }

  @Test
  public void testReadChallengeTransactionAcceptsBothV0AndV1()
      throws InvalidSep10ChallengeException, IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    Network network = Network.TESTNET;
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, network, client.getAccountId(), domainName, webAuthDomain, timeBounds);

    transaction.setEnvelopeType(EnvelopeType.ENVELOPE_TYPE_TX_V0);
    TransactionEnvelope v0 = transaction.toEnvelopeXdr();
    String v0Base64 = transaction.toEnvelopeXdrBase64();
    assertEquals(EnvelopeType.ENVELOPE_TYPE_TX_V0, v0.getDiscriminant());
    Sep10Challenge.ChallengeTransaction v0ChallengeTransaction =
        Sep10Challenge.readChallengeTransaction(
            v0Base64, server.getAccountId(), Network.TESTNET, domainName, webAuthDomain);

    transaction.setEnvelopeType(EnvelopeType.ENVELOPE_TYPE_TX);
    TransactionEnvelope v1 = transaction.toEnvelopeXdr();
    String v1Base64 = transaction.toEnvelopeXdrBase64();
    assertEquals(EnvelopeType.ENVELOPE_TYPE_TX, v1.getDiscriminant());
    Sep10Challenge.ChallengeTransaction v1ChallengeTransaction =
        Sep10Challenge.readChallengeTransaction(
            v1Base64, server.getAccountId(), Network.TESTNET, domainName, webAuthDomain);

    assertEquals(v0ChallengeTransaction, v1ChallengeTransaction);

    for (String envelopeBase64 : ImmutableList.of(v0Base64, v1Base64)) {
      Sep10Challenge.ChallengeTransaction challengeTransaction =
          Sep10Challenge.readChallengeTransaction(
              envelopeBase64, server.getAccountId(), Network.TESTNET, domainName, webAuthDomain);
      assertEquals(
          new Sep10Challenge.ChallengeTransaction(transaction, client.getAccountId(), domainName),
          challengeTransaction);
    }
  }

  @Test
  public void testReadChallengeTransactionRejectsMuxedServer()
      throws InvalidSep10ChallengeException, IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, network, client.getAccountId(), domainName, webAuthDomain, timeBounds);

    try {
      Sep10Challenge.readChallengeTransaction(
          transaction.toEnvelopeXdrBase64(),
          "MCAAAAAAAAAAAAB7BQ2L7E5NBWMXDUCMZSIPOBKRDSBYVLMXGSSKF6YNPIB7Y77ITKNOG",
          Network.TESTNET,
          domainName,
          webAuthDomain);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals(
          "serverAccountId: MCAAAAAAAAAAAAB7BQ2L7E5NBWMXDUCMZSIPOBKRDSBYVLMXGSSKF6YNPIB7Y77ITKNOG is not a valid account id",
          e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionRejectsMuxedClient()
      throws InvalidSep10ChallengeException, IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    Network network = Network.TESTNET;
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    final Transaction transaction =
        Sep10Challenge.newChallenge(
            server, network, client.getAccountId(), domainName, webAuthDomain, timeBounds);
    Operation[] operations = transaction.getOperations();
    operations[0].setSourceAccount(
        "MCAAAAAAAAAAAAB7BQ2L7E5NBWMXDUCMZSIPOBKRDSBYVLMXGSSKF6YNPIB7Y77ITKNOG");
    Transaction withMuxedClient =
        new TransactionBuilder(
                AccountConverter.disableMuxed(),
                new Account(transaction.getSourceAccount(), 100L),
                transaction.getNetwork())
            .setBaseFee((int) transaction.getFee())
            .addMemo(transaction.getMemo())
            .addOperations(Arrays.asList(operations))
            .addPreconditions(transaction.getPreconditions())
            .build();

    for (DecoratedSignature signature : transaction.mSignatures) {
      withMuxedClient.addSignature(signature);
    }

    try {
      Sep10Challenge.readChallengeTransaction(
          withMuxedClient.toEnvelopeXdrBase64(),
          "MCAAAAAAAAAAAAB7BQ2L7E5NBWMXDUCMZSIPOBKRDSBYVLMXGSSKF6YNPIB7Y77ITKNOG",
          Network.TESTNET,
          domainName,
          webAuthDomain);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals(
          "serverAccountId: MCAAAAAAAAAAAAB7BQ2L7E5NBWMXDUCMZSIPOBKRDSBYVLMXGSSKF6YNPIB7Y77ITKNOG is not a valid account id",
          e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionValidSignedByServerAndClient()
      throws InvalidSep10ChallengeException, IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, network, client.getAccountId(), domainName, webAuthDomain, timeBounds);

    transaction.sign(client);

    Sep10Challenge.ChallengeTransaction challengeTransaction =
        Sep10Challenge.readChallengeTransaction(
            transaction.toEnvelopeXdrBase64(),
            server.getAccountId(),
            Network.TESTNET,
            domainName,
            webAuthDomain);
    assertEquals(
        new Sep10Challenge.ChallengeTransaction(transaction, client.getAccountId(), domainName),
        challengeTransaction);
  }

  @Test
  public void testReadChallengeTransactionInvalidNotSignedByServer() throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    String domainName = "example.com";
    String webAuthDomain = "example.com";
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    byte[] nonce = new byte[48];
    SecureRandom random = new SecureRandom();
    random.nextBytes(nonce);
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] encodedNonce = base64Encoding.encode(nonce).getBytes();

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation manageDataOperation1 =
        new ManageDataOperation.Builder(domainName + " auth", encodedNonce)
            .setSourceAccount(client.getAccountId())
            .build();

    Operation[] operations = new Operation[] {manageDataOperation1};
    Transaction transaction =
        new TransactionBuilder(AccountConverter.disableMuxed(), sourceAccount, network)
            .setBaseFee(100 * operations.length)
            .addOperations(Arrays.asList(operations))
            .addMemo(Memo.none())
            .addPreconditions(TransactionPreconditions.builder().timeBounds(timeBounds).build())
            .build();

    transaction.sign(client);
    try {
      Sep10Challenge.readChallengeTransaction(
          transaction.toEnvelopeXdrBase64(),
          server.getAccountId(),
          network,
          domainName,
          webAuthDomain);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals(
          String.format("Transaction not signed by server: %s.", server.getAccountId()),
          e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionInvalidCorrupted()
      throws InvalidSep10ChallengeException, IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, network, client.getAccountId(), domainName, webAuthDomain, timeBounds);

    String challenge = transaction.toEnvelopeXdrBase64().replace("A", "B");

    try {
      Sep10Challenge.readChallengeTransaction(
          challenge, server.getAccountId(), Network.TESTNET, domainName, webAuthDomain);
      fail();
    } catch (RuntimeException ignored) {
      // TODO: I think we should add an exception signature about the failure to read XDR on
      // Transaction.fromEnvelopeXdr.
    }
  }

  @Test
  public void testReadChallengeTransactionInvalidServerAccountIDMismatch()
      throws InvalidSep10ChallengeException, IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, Network.TESTNET, client.getAccountId(), domainName, webAuthDomain, timeBounds);

    // assertThrows requires Java 8+
    try {
      Sep10Challenge.readChallengeTransaction(
          transaction.toEnvelopeXdrBase64(),
          KeyPair.random().getAccountId(),
          Network.TESTNET,
          domainName,
          webAuthDomain);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Transaction source account is not equal to server's account.", e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionInvalidSeqNoNotZero() throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    byte[] nonce = new byte[48];
    SecureRandom random = new SecureRandom();
    random.nextBytes(nonce);
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] encodedNonce = base64Encoding.encode(nonce).getBytes();

    Account sourceAccount = new Account(server.getAccountId(), 100L);
    ManageDataOperation manageDataOperation1 =
        new ManageDataOperation.Builder(domainName + " auth", encodedNonce)
            .setSourceAccount(client.getAccountId())
            .build();

    Operation[] operations = new Operation[] {manageDataOperation1};
    Transaction transaction =
        new TransactionBuilder(AccountConverter.disableMuxed(), sourceAccount, network)
            .setBaseFee(100 * operations.length)
            .addOperations(Arrays.asList(operations))
            .addMemo(Memo.none())
            .addPreconditions(TransactionPreconditions.builder().timeBounds(timeBounds).build())
            .build();

    transaction.sign(server);
    String challenge = transaction.toEnvelopeXdrBase64();

    try {
      Sep10Challenge.readChallengeTransaction(
          challenge, server.getAccountId(), Network.TESTNET, domainName, webAuthDomain);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("The transaction sequence number should be zero.", e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionInvalidTimeboundsInfinite() throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = 0L;
    TimeBounds timeBounds = new TimeBounds(now, end);

    byte[] nonce = new byte[48];
    SecureRandom random = new SecureRandom();
    random.nextBytes(nonce);
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] encodedNonce = base64Encoding.encode(nonce).getBytes();

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation operation =
        new ManageDataOperation.Builder(domainName + " auth", encodedNonce)
            .setSourceAccount(client.getAccountId())
            .build();
    Operation[] operations = new Operation[] {operation};

    Transaction transaction =
        new TransactionBuilder(AccountConverter.disableMuxed(), sourceAccount, network)
            .setBaseFee(100 * operations.length)
            .addOperations(Arrays.asList(operations))
            .addMemo(Memo.none())
            .addPreconditions(TransactionPreconditions.builder().timeBounds(timeBounds).build())
            .build();

    transaction.sign(server);
    String challenge = transaction.toEnvelopeXdrBase64();

    try {
      Sep10Challenge.readChallengeTransaction(
          challenge, server.getAccountId(), Network.TESTNET, domainName, webAuthDomain);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Transaction requires non-infinite timebounds.", e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionInvalidTimeBoundsTooEarly()
      throws InvalidSep10ChallengeException, IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();

    long current = System.currentTimeMillis() / 1000L;
    long start = current + Sep10Challenge.GRACE_PERIOD_SECONDS + 30;
    long end = current + 600;
    TimeBounds timeBounds = new TimeBounds(start, end);
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, Network.TESTNET, client.getAccountId(), domainName, webAuthDomain, timeBounds);

    try {
      Sep10Challenge.readChallengeTransaction(
          transaction.toEnvelopeXdrBase64(),
          server.getAccountId(),
          Network.TESTNET,
          domainName,
          webAuthDomain);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Transaction is not within range of the specified timebounds.", e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionGracePeriod()
      throws InvalidSep10ChallengeException, IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();

    long current = System.currentTimeMillis() / 1000L;
    long start = current + Sep10Challenge.GRACE_PERIOD_SECONDS - 30;
    long end = current + 600;
    TimeBounds timeBounds = new TimeBounds(start, end);
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, Network.TESTNET, client.getAccountId(), domainName, webAuthDomain, timeBounds);

    try {
      Sep10Challenge.readChallengeTransaction(
          transaction.toEnvelopeXdrBase64(),
          server.getAccountId(),
          Network.TESTNET,
          domainName,
          webAuthDomain);
    } catch (InvalidSep10ChallengeException e) {
      fail();
    }
  }

  @Test
  public void testReadChallengeTransactionInvalidTimeBoundsTooLate()
      throws InvalidSep10ChallengeException, IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();

    long current = System.currentTimeMillis() / 1000L;
    long start = current - 600;
    long end = current - 300;
    TimeBounds timeBounds = new TimeBounds(start, end);
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, Network.TESTNET, client.getAccountId(), domainName, webAuthDomain, timeBounds);

    try {
      Sep10Challenge.readChallengeTransaction(
          transaction.toEnvelopeXdrBase64(),
          server.getAccountId(),
          Network.TESTNET,
          domainName,
          webAuthDomain);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Transaction is not within range of the specified timebounds.", e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionInvalidOperationWrongType() throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    SetOptionsOperation setOptionsOperation =
        new SetOptionsOperation.Builder().setSourceAccount(client.getAccountId()).build();

    Operation[] operations = new Operation[] {setOptionsOperation};
    Transaction transaction =
        new TransactionBuilder(AccountConverter.disableMuxed(), sourceAccount, network)
            .setBaseFee(100 * operations.length)
            .addOperations(Arrays.asList(operations))
            .addMemo(Memo.none())
            .addPreconditions(TransactionPreconditions.builder().timeBounds(timeBounds).build())
            .build();

    transaction.sign(server);
    String challenge = transaction.toEnvelopeXdrBase64();

    try {
      Sep10Challenge.readChallengeTransaction(
          challenge, server.getAccountId(), Network.TESTNET, domainName, webAuthDomain);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Operation type should be ManageData.", e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionInvalidOperationNoSourceAccount() throws IOException {
    KeyPair server = KeyPair.random();
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    byte[] nonce = new byte[48];
    SecureRandom random = new SecureRandom();
    random.nextBytes(nonce);
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] encodedNonce = base64Encoding.encode(nonce).getBytes();

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation manageDataOperation1 =
        new ManageDataOperation.Builder(domainName + " auth", encodedNonce).build();

    Operation[] operations = new Operation[] {manageDataOperation1};
    Transaction transaction =
        new TransactionBuilder(AccountConverter.disableMuxed(), sourceAccount, network)
            .setBaseFee(100 * operations.length)
            .addOperations(Arrays.asList(operations))
            .addMemo(Memo.none())
            .addPreconditions(TransactionPreconditions.builder().timeBounds(timeBounds).build())
            .build();

    transaction.sign(server);
    String challenge = transaction.toEnvelopeXdrBase64();

    try {
      Sep10Challenge.readChallengeTransaction(
          challenge, server.getAccountId(), Network.TESTNET, domainName, webAuthDomain);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Operation should have a source account.", e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionInvalidDataValueWrongEncodedLength() throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    byte[] nonce = new byte[32];
    SecureRandom random = new SecureRandom();
    random.nextBytes(nonce);
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] encodedNonce = base64Encoding.encode(nonce).getBytes();

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation manageDataOperation1 =
        new ManageDataOperation.Builder(domainName + " auth", encodedNonce)
            .setSourceAccount(client.getAccountId())
            .build();

    Operation[] operations = new Operation[] {manageDataOperation1};
    Transaction transaction =
        new TransactionBuilder(AccountConverter.disableMuxed(), sourceAccount, network)
            .setBaseFee(100 * operations.length)
            .addOperations(Arrays.asList(operations))
            .addMemo(Memo.none())
            .addPreconditions(TransactionPreconditions.builder().timeBounds(timeBounds).build())
            .build();
    transaction.sign(server);
    String challenge = transaction.toEnvelopeXdrBase64();

    try {
      Sep10Challenge.readChallengeTransaction(
          challenge, server.getAccountId(), Network.TESTNET, domainName, webAuthDomain);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Random nonce encoded as base64 should be 64 bytes long.", e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionInvalidDataValueCorruptBase64() throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    byte[] encodedNonce =
        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA?AAAAAAAAAAAAAAAAAAAAAAAAAA".getBytes("UTF-8");
    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation manageDataOperation1 =
        new ManageDataOperation.Builder(domainName + " auth", encodedNonce)
            .setSourceAccount(client.getAccountId())
            .build();

    Operation[] operations = new Operation[] {manageDataOperation1};
    Transaction transaction =
        new TransactionBuilder(AccountConverter.disableMuxed(), sourceAccount, network)
            .setBaseFee(100 * operations.length)
            .addOperations(Arrays.asList(operations))
            .addMemo(Memo.none())
            .addPreconditions(TransactionPreconditions.builder().timeBounds(timeBounds).build())
            .build();
    transaction.sign(server);
    String challenge = transaction.toEnvelopeXdrBase64();

    try {
      Sep10Challenge.readChallengeTransaction(
          challenge, server.getAccountId(), Network.TESTNET, domainName, webAuthDomain);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals(
          "Failed to decode random nonce provided in ManageData operation.", e.getMessage());
      assertEquals(
          "com.google.common.io.BaseEncoding$DecodingException: Unrecognized character: ?",
          e.getCause().getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionInvalidDataValueWrongByteLength() throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    byte[] nonce = new byte[47];
    SecureRandom random = new SecureRandom();
    random.nextBytes(nonce);
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] encodedNonce = base64Encoding.encode(nonce).getBytes();

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation manageDataOperation1 =
        new ManageDataOperation.Builder(domainName + " auth", encodedNonce)
            .setSourceAccount(client.getAccountId())
            .build();

    Operation[] operations = new Operation[] {manageDataOperation1};
    Transaction transaction =
        new TransactionBuilder(AccountConverter.disableMuxed(), sourceAccount, network)
            .setBaseFee(100 * operations.length)
            .addOperations(Arrays.asList(operations))
            .addMemo(Memo.none())
            .addPreconditions(TransactionPreconditions.builder().timeBounds(timeBounds).build())
            .build();
    transaction.sign(server);
    String challenge = transaction.toEnvelopeXdrBase64();

    try {
      Sep10Challenge.readChallengeTransaction(
          challenge, server.getAccountId(), Network.TESTNET, domainName, webAuthDomain);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals(
          "Random nonce before encoding as base64 should be 48 bytes long.", e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionInvalidDataValueIsNull() throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation manageDataOperation1 =
        new ManageDataOperation.Builder(domainName + " auth", null)
            .setSourceAccount(client.getAccountId())
            .build();

    Operation[] operations = new Operation[] {manageDataOperation1};
    Transaction transaction =
        new TransactionBuilder(AccountConverter.disableMuxed(), sourceAccount, network)
            .setBaseFee(100 * operations.length)
            .addOperations(Arrays.asList(operations))
            .addMemo(Memo.none())
            .addPreconditions(TransactionPreconditions.builder().timeBounds(timeBounds).build())
            .build();
    transaction.sign(server);
    String challenge = transaction.toEnvelopeXdrBase64();

    try {
      Sep10Challenge.readChallengeTransaction(
          challenge, server.getAccountId(), Network.TESTNET, domainName, webAuthDomain);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("The transaction's operation value should not be null.", e.getMessage());
    }
  }

  @Test
  public void
      testReadChallengeTransactionValidAdditionalManageDataOpsWithSourceAccountSetToServerAccount()
          throws IOException, InvalidSep10ChallengeException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    byte[] nonce = new byte[48];
    SecureRandom random = new SecureRandom();
    random.nextBytes(nonce);
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] encodedNonce = base64Encoding.encode(nonce).getBytes();

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation operation1 =
        new ManageDataOperation.Builder(domainName + " auth", encodedNonce)
            .setSourceAccount(client.getAccountId())
            .build();
    ManageDataOperation operation2 =
        new ManageDataOperation.Builder("key", "value".getBytes())
            .setSourceAccount(server.getAccountId())
            .build();
    Operation[] operations = new Operation[] {operation1, operation2};
    Transaction transaction =
        new TransactionBuilder(AccountConverter.disableMuxed(), sourceAccount, network)
            .setBaseFee(100 * operations.length)
            .addOperations(Arrays.asList(operations))
            .addMemo(Memo.none())
            .addPreconditions(TransactionPreconditions.builder().timeBounds(timeBounds).build())
            .build();
    transaction.sign(server);
    String challenge = transaction.toEnvelopeXdrBase64();

    Sep10Challenge.ChallengeTransaction challengeTransaction =
        Sep10Challenge.readChallengeTransaction(
            challenge, server.getAccountId(), Network.TESTNET, domainName, webAuthDomain);
    assertEquals(
        new Sep10Challenge.ChallengeTransaction(transaction, client.getAccountId(), domainName),
        challengeTransaction);
  }

  @Test
  public void
      testReadChallengeTransactionInvalidAdditionalManageDataOpsWithoutSourceAccountSetToServerAccount()
          throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    byte[] nonce = new byte[48];
    SecureRandom random = new SecureRandom();
    random.nextBytes(nonce);
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] encodedNonce = base64Encoding.encode(nonce).getBytes();

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation operation1 =
        new ManageDataOperation.Builder(domainName + " auth", encodedNonce)
            .setSourceAccount(client.getAccountId())
            .build();
    ManageDataOperation operation2 =
        new ManageDataOperation.Builder("key", "value".getBytes())
            .setSourceAccount(client.getAccountId())
            .build();
    Operation[] operations = new Operation[] {operation1, operation2};
    Transaction transaction =
        new TransactionBuilder(AccountConverter.disableMuxed(), sourceAccount, network)
            .setBaseFee(100 * operations.length)
            .addOperations(Arrays.asList(operations))
            .addMemo(Memo.none())
            .addPreconditions(TransactionPreconditions.builder().timeBounds(timeBounds).build())
            .build();
    transaction.sign(server);
    String challenge = transaction.toEnvelopeXdrBase64();

    try {
      Sep10Challenge.readChallengeTransaction(
          challenge, server.getAccountId(), Network.TESTNET, domainName, webAuthDomain);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Subsequent operations are unrecognized.", e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionInvalidAdditionalManageDataOpsWithSourceAccountSetToNull()
      throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    byte[] nonce = new byte[48];
    SecureRandom random = new SecureRandom();
    random.nextBytes(nonce);
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] encodedNonce = base64Encoding.encode(nonce).getBytes();

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation operation1 =
        new ManageDataOperation.Builder(domainName + " auth", encodedNonce)
            .setSourceAccount(client.getAccountId())
            .build();
    ManageDataOperation operation2 =
        new ManageDataOperation.Builder("key", "value".getBytes()).build();
    Operation[] operations = new Operation[] {operation1, operation2};
    Transaction transaction =
        new TransactionBuilder(AccountConverter.disableMuxed(), sourceAccount, network)
            .setBaseFee(100 * operations.length)
            .addOperations(Arrays.asList(operations))
            .addMemo(Memo.none())
            .addPreconditions(TransactionPreconditions.builder().timeBounds(timeBounds).build())
            .build();
    transaction.sign(server);
    String challenge = transaction.toEnvelopeXdrBase64();

    try {
      Sep10Challenge.readChallengeTransaction(
          challenge, server.getAccountId(), Network.TESTNET, domainName, webAuthDomain);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Operation should have a source account.", e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionInvalidAdditionalOpsOfOtherTypes() throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    byte[] nonce = new byte[48];
    SecureRandom random = new SecureRandom();
    random.nextBytes(nonce);
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] encodedNonce = base64Encoding.encode(nonce).getBytes();

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation operation1 =
        new ManageDataOperation.Builder(domainName + " auth", encodedNonce)
            .setSourceAccount(client.getAccountId())
            .build();
    BumpSequenceOperation operation2 =
        new BumpSequenceOperation.Builder(0L).setSourceAccount(server.getAccountId()).build();
    Operation[] operations = new Operation[] {operation1, operation2};
    Transaction transaction =
        new TransactionBuilder(AccountConverter.disableMuxed(), sourceAccount, network)
            .setBaseFee(100 * operations.length)
            .addOperations(Arrays.asList(operations))
            .addMemo(Memo.none())
            .addPreconditions(TransactionPreconditions.builder().timeBounds(timeBounds).build())
            .build();
    transaction.sign(server);
    String challenge = transaction.toEnvelopeXdrBase64();

    try {
      Sep10Challenge.readChallengeTransaction(
          challenge, server.getAccountId(), Network.TESTNET, domainName, webAuthDomain);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Operation type should be ManageData.", e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionValidMultipleDomainNames()
      throws IOException, InvalidSep10ChallengeException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    Network network = Network.TESTNET;
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    Transaction transaction = null;
    try {
      transaction =
          Sep10Challenge.newChallenge(
              server, network, client.getAccountId(), domainName, webAuthDomain, timeBounds);
    } catch (InvalidSep10ChallengeException e) {
      fail("Should not have thrown any exception.");
    }

    Sep10Challenge.ChallengeTransaction challengeTransaction =
        Sep10Challenge.readChallengeTransaction(
            transaction.toEnvelopeXdrBase64(),
            server.getAccountId(),
            Network.TESTNET,
            new String[] {"example3.com", "example2.com", "example.com"},
            webAuthDomain);
    assertEquals(
        new Sep10Challenge.ChallengeTransaction(transaction, client.getAccountId(), domainName),
        challengeTransaction);
  }

  @Test
  public void testReadChallengeTransactionInvalidDomainNamesMismatch() throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    Network network = Network.TESTNET;
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    Transaction transaction = null;
    try {
      transaction =
          Sep10Challenge.newChallenge(
              server, network, client.getAccountId(), domainName, webAuthDomain, timeBounds);
    } catch (InvalidSep10ChallengeException e) {
      fail("Should not have thrown any exception.");
    }

    try {
      Sep10Challenge.readChallengeTransaction(
          transaction.toEnvelopeXdrBase64(),
          server.getAccountId(),
          Network.TESTNET,
          new String[] {"example2.com", "example1.com"},
          webAuthDomain);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals(
          "The transaction's operation key name does not include one of the expected home domains.",
          e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionInvalidDomainNamesEmpty()
      throws IOException, InvalidSep10ChallengeException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    Network network = Network.TESTNET;
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    Transaction transaction = null;
    try {
      transaction =
          Sep10Challenge.newChallenge(
              server, network, client.getAccountId(), domainName, webAuthDomain, timeBounds);
    } catch (InvalidSep10ChallengeException e) {
      fail("Should not have thrown any exception.");
    }

    try {
      String[] domainNames = new String[] {};
      Sep10Challenge.readChallengeTransaction(
          transaction.toEnvelopeXdrBase64(),
          server.getAccountId(),
          Network.TESTNET,
          domainNames,
          webAuthDomain);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("At least one domain name must be included in domainNames.", e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionInvalidDomainNamesNull()
      throws IOException, InvalidSep10ChallengeException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    Network network = Network.TESTNET;
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    Transaction transaction = null;
    try {
      transaction =
          Sep10Challenge.newChallenge(
              server, network, client.getAccountId(), domainName, webAuthDomain, timeBounds);
    } catch (InvalidSep10ChallengeException e) {
      fail("Should not have thrown any exception.");
    }

    try {
      String[] domainNames = null;
      Sep10Challenge.readChallengeTransaction(
          transaction.toEnvelopeXdrBase64(),
          server.getAccountId(),
          Network.TESTNET,
          domainNames,
          webAuthDomain);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("At least one domain name must be included in domainNames.", e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionValidWebAuthDomainNotEqualHomeDomain()
      throws IOException, InvalidSep10ChallengeException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "auth.example.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, network, client.getAccountId(), domainName, webAuthDomain, timeBounds);
    Sep10Challenge.ChallengeTransaction challengeTransaction =
        Sep10Challenge.readChallengeTransaction(
            transaction.toEnvelopeXdrBase64(),
            server.getAccountId(),
            network,
            domainName,
            webAuthDomain);
    assertEquals(transaction, challengeTransaction.getTransaction());
    assertEquals(client.getAccountId(), challengeTransaction.getClientAccountId());
    assertEquals(domainName, challengeTransaction.getMatchedHomeDomain());
  }

  @Test
  public void testReadChallengeTransactionInvalidWebAuthDomainMismatch() throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "example.com";
    String invalidWebAuthDomain = "invalid.example.com";

    Transaction transaction = null;
    try {
      transaction =
          Sep10Challenge.newChallenge(
              server, network, client.getAccountId(), domainName, invalidWebAuthDomain, timeBounds);
    } catch (InvalidSep10ChallengeException e) {
      fail("Should not have thrown any exception.");
    }

    try {
      Sep10Challenge.readChallengeTransaction(
          transaction.toEnvelopeXdrBase64(),
          server.getAccountId(),
          network,
          domainName,
          webAuthDomain);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("'web_auth_domain' operation value does not match example.com.", e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionInvalidWebAuthDomainOperationValueIsNull()
      throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    byte[] nonce = new byte[48];
    SecureRandom random = new SecureRandom();
    random.nextBytes(nonce);
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] encodedNonce = base64Encoding.encode(nonce).getBytes();

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation domainNameOperation =
        new ManageDataOperation.Builder(domainName + " auth", encodedNonce)
            .setSourceAccount(client.getAccountId())
            .build();
    ManageDataOperation webAuthDomainOperation =
        new ManageDataOperation.Builder("web_auth_domain", null)
            .setSourceAccount(server.getAccountId())
            .build();
    Operation[] operations = new Operation[] {domainNameOperation, webAuthDomainOperation};
    Transaction transaction =
        new TransactionBuilder(AccountConverter.disableMuxed(), sourceAccount, network)
            .setBaseFee(100 * operations.length)
            .addOperations(Arrays.asList(operations))
            .addMemo(Memo.none())
            .addPreconditions(TransactionPreconditions.builder().timeBounds(timeBounds).build())
            .build();
    transaction.sign(server);
    String challenge = transaction.toEnvelopeXdrBase64();

    try {
      Sep10Challenge.readChallengeTransaction(
          challenge, server.getAccountId(), network, domainName, webAuthDomain);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("'web_auth_domain' operation value should not be null.", e.getMessage());
    }
  }

  @Test
  public void
      testReadChallengeTransactionValidWebAuthDomainAllowSubsequentManageDataOperationsToHaveNullValue()
          throws IOException, InvalidSep10ChallengeException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    byte[] nonce = new byte[48];
    SecureRandom random = new SecureRandom();
    random.nextBytes(nonce);
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] encodedNonce = base64Encoding.encode(nonce).getBytes();

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation domainNameOperation =
        new ManageDataOperation.Builder(domainName + " auth", encodedNonce)
            .setSourceAccount(client.getAccountId())
            .build();
    ManageDataOperation webAuthDomainOperation =
        new ManageDataOperation.Builder("web_auth_domain", webAuthDomain.getBytes())
            .setSourceAccount(server.getAccountId())
            .build();
    ManageDataOperation otherDomainOperation =
        new ManageDataOperation.Builder("subsequent_op", null)
            .setSourceAccount(server.getAccountId())
            .build();
    Operation[] operations =
        new Operation[] {domainNameOperation, webAuthDomainOperation, otherDomainOperation};
    Transaction transaction =
        new TransactionBuilder(AccountConverter.disableMuxed(), sourceAccount, network)
            .setBaseFee(100 * operations.length)
            .addOperations(Arrays.asList(operations))
            .addMemo(Memo.none())
            .addPreconditions(TransactionPreconditions.builder().timeBounds(timeBounds).build())
            .build();
    transaction.sign(server);
    String challenge = transaction.toEnvelopeXdrBase64();

    Sep10Challenge.ChallengeTransaction challengeTransaction =
        Sep10Challenge.readChallengeTransaction(
            challenge, server.getAccountId(), network, domainName, webAuthDomain);
    assertEquals(transaction, challengeTransaction.getTransaction());
    assertEquals(client.getAccountId(), challengeTransaction.getClientAccountId());
    assertEquals(domainName, challengeTransaction.getMatchedHomeDomain());
  }

  @Test
  public void testChallengeWithClientDomain() throws InvalidSep10ChallengeException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    KeyPair clientDomainSigner = KeyPair.random();

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "auth.example.com";
    String clientDomain = "client.domain.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server,
            Network.TESTNET,
            client.getAccountId(),
            domainName,
            webAuthDomain,
            timeBounds,
            clientDomain,
            clientDomainSigner.getAccountId());

    assertEquals(Network.TESTNET, transaction.getNetwork());
    assertEquals(300, transaction.getFee());
    assertEquals(timeBounds, transaction.getTimeBounds());
    assertEquals(server.getAccountId(), transaction.getSourceAccount());
    assertEquals(0, transaction.getSequenceNumber());

    assertEquals(3, transaction.getOperations().length);
    ManageDataOperation homeDomainOp = (ManageDataOperation) transaction.getOperations()[0];
    assertEquals(client.getAccountId(), homeDomainOp.getSourceAccount());
    assertEquals(domainName + " auth", homeDomainOp.getName());

    assertEquals(64, homeDomainOp.getValue().length);
    BaseEncoding base64Encoding = BaseEncoding.base64();
    assertTrue(base64Encoding.canDecode(new String(homeDomainOp.getValue())));

    ManageDataOperation webAuthDomainOp = (ManageDataOperation) transaction.getOperations()[1];
    assertEquals(server.getAccountId(), webAuthDomainOp.getSourceAccount());
    assertEquals("web_auth_domain", webAuthDomainOp.getName());
    assertArrayEquals(webAuthDomain.getBytes(), webAuthDomainOp.getValue());

    ManageDataOperation clientAuthDomainOp = (ManageDataOperation) transaction.getOperations()[2];
    assertEquals(clientDomainSigner.getAccountId(), clientAuthDomainOp.getSourceAccount());
    assertEquals("client_domain", clientAuthDomainOp.getName());
    assertArrayEquals(clientDomain.getBytes(), clientAuthDomainOp.getValue());

    assertEquals(1, transaction.getSignatures().size());
    assertTrue(
        server.verify(
            transaction.hash(), transaction.getSignatures().get(0).getSignature().getSignature()));
  }

  @Test
  public void testChallengeWithClientDomainButWithoutClientDomainSigner() {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    KeyPair clientDomainSigner = KeyPair.random();

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "auth.example.com";
    String clientDomain = "client.domain.com";

    try {
      Sep10Challenge.newChallenge(
          server,
          Network.TESTNET,
          client.getAccountId(),
          domainName,
          webAuthDomain,
          timeBounds,
          "",
          clientDomainSigner.getAccountId());
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("clientDomain is required if clientSigningKey is provided", e.getMessage());
    }

    try {
      Sep10Challenge.newChallenge(
          server,
          Network.TESTNET,
          client.getAccountId(),
          domainName,
          webAuthDomain,
          timeBounds,
          clientDomain,
          "");
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("clientDomain is required if clientSigningKey is provided", e.getMessage());
    }
  }

  @Test
  public void testVerifyChallengeWithClientDomain()
      throws IOException, InvalidSep10ChallengeException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    KeyPair clientDomainSigner = KeyPair.random();

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "auth.example.com";
    String clientDomain = "client.domain.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server,
            Network.TESTNET,
            client.getAccountId(),
            domainName,
            webAuthDomain,
            timeBounds,
            clientDomain,
            clientDomainSigner.getAccountId());

    transaction.sign(client);
    transaction.sign(clientDomainSigner);

    // should pass if clientDomainSigner is omitted from signers set
    Set<String> signers = new HashSet<String>(Arrays.asList(client.getAccountId()));
    Sep10Challenge.verifyChallengeTransactionSigners(
        transaction.toEnvelopeXdrBase64(),
        server.getAccountId(),
        Network.TESTNET,
        domainName,
        webAuthDomain,
        signers);

    // should pass if clientDomainSigner is included in signers set
    signers =
        new HashSet<String>(
            Arrays.asList(client.getAccountId(), clientDomainSigner.getAccountId()));
    Sep10Challenge.verifyChallengeTransactionSigners(
        transaction.toEnvelopeXdrBase64(),
        server.getAccountId(),
        Network.TESTNET,
        domainName,
        webAuthDomain,
        signers);
  }

  @Test
  public void testVerifyChallengeWithClientDomainMissingSignature()
      throws IOException, InvalidSep10ChallengeException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    KeyPair clientDomainSigner = KeyPair.random();

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "auth.example.com";
    String clientDomain = "client.domain.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server,
            Network.TESTNET,
            client.getAccountId(),
            domainName,
            webAuthDomain,
            timeBounds,
            clientDomain,
            clientDomainSigner.getAccountId());

    transaction.sign(client);

    Set<String> signers = new HashSet<String>(Arrays.asList(client.getAccountId()));
    try {
      Sep10Challenge.verifyChallengeTransactionSigners(
          transaction.toEnvelopeXdrBase64(),
          server.getAccountId(),
          Network.TESTNET,
          domainName,
          webAuthDomain,
          signers);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals(
          String.format(
              "Transaction not signed by by the source account of the 'client_domain' ManageDataOperation: %s.",
              clientDomainSigner.getAccountId()),
          e.getMessage());
    }
  }

  @Test
  public void testVerifyChallengeTransactionThresholdInvalidNotSignedByServer() throws IOException {
    Network network = Network.TESTNET;
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    KeyPair signerClient2 = KeyPair.random();
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    byte[] nonce = new byte[48];
    SecureRandom random = new SecureRandom();
    random.nextBytes(nonce);
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] encodedNonce = base64Encoding.encode(nonce).getBytes();

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation manageDataOperation1 =
        new ManageDataOperation.Builder(domainName + " auth", encodedNonce)
            .setSourceAccount(masterClient.getAccountId())
            .build();

    Operation[] operations = new Operation[] {manageDataOperation1};
    Transaction transaction =
        new TransactionBuilder(AccountConverter.disableMuxed(), sourceAccount, network)
            .setBaseFee(100 * operations.length)
            .addOperations(Arrays.asList(operations))
            .addMemo(Memo.none())
            .addPreconditions(TransactionPreconditions.builder().timeBounds(timeBounds).build())
            .build();

    transaction.sign(masterClient);

    Set<Sep10Challenge.Signer> signers =
        new HashSet<Sep10Challenge.Signer>(
            Arrays.asList(
                new Sep10Challenge.Signer(masterClient.getAccountId(), 1),
                new Sep10Challenge.Signer(signerClient1.getAccountId(), 2),
                new Sep10Challenge.Signer(signerClient2.getAccountId(), 4)));

    int threshold = 6;
    try {
      Sep10Challenge.verifyChallengeTransactionThreshold(
          transaction.toEnvelopeXdrBase64(),
          server.getAccountId(),
          network,
          domainName,
          webAuthDomain,
          threshold,
          signers);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals(
          String.format("Transaction not signed by server: %s.", server.getAccountId()),
          e.getMessage());
    }
  }

  @Test
  public void testVerifyChallengeTransactionThresholdValidServerAndClientKeyMeetingThreshold()
      throws IOException, InvalidSep10ChallengeException {
    Network network = Network.TESTNET;
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, network, masterClient.getAccountId(), domainName, webAuthDomain, timeBounds);

    transaction.sign(masterClient);

    Set<Sep10Challenge.Signer> signers =
        new HashSet<Sep10Challenge.Signer>(
            Collections.singletonList(new Sep10Challenge.Signer(masterClient.getAccountId(), 255)));

    int threshold = 255;
    Set<String> signersFound =
        Sep10Challenge.verifyChallengeTransactionThreshold(
            transaction.toEnvelopeXdrBase64(),
            server.getAccountId(),
            network,
            domainName,
            webAuthDomain,
            threshold,
            signers);
    assertEquals(
        new HashSet<String>(Collections.singletonList(masterClient.getAccountId())), signersFound);
  }

  @Test
  public void testVerifyChallengeTransactionThresholdValidMultipleDomainNames()
      throws IOException, InvalidSep10ChallengeException {
    Network network = Network.TESTNET;
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, network, masterClient.getAccountId(), domainName, webAuthDomain, timeBounds);

    transaction.sign(masterClient);

    Set<Sep10Challenge.Signer> signers =
        new HashSet<Sep10Challenge.Signer>(
            Collections.singletonList(new Sep10Challenge.Signer(masterClient.getAccountId(), 255)));

    int threshold = 255;
    Set<String> signersFound =
        Sep10Challenge.verifyChallengeTransactionThreshold(
            transaction.toEnvelopeXdrBase64(),
            server.getAccountId(),
            network,
            new String[] {"example3.com", "example2.com", "example.com"},
            webAuthDomain,
            threshold,
            signers);
    assertEquals(
        new HashSet<String>(Collections.singletonList(masterClient.getAccountId())), signersFound);
  }

  @Test
  public void
      testVerifyChallengeTransactionThresholdValidServerAndMultipleClientKeyMeetingThreshold()
          throws IOException, InvalidSep10ChallengeException {
    Network network = Network.TESTNET;
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    KeyPair signerClient2 = KeyPair.random();
    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, network, masterClient.getAccountId(), domainName, webAuthDomain, timeBounds);

    transaction.sign(masterClient);
    transaction.sign(signerClient1);
    transaction.sign(signerClient2);

    Set<Sep10Challenge.Signer> signers =
        new HashSet<Sep10Challenge.Signer>(
            Arrays.asList(
                new Sep10Challenge.Signer(masterClient.getAccountId(), 1),
                new Sep10Challenge.Signer(signerClient1.getAccountId(), 2),
                new Sep10Challenge.Signer(signerClient2.getAccountId(), 4)));

    int threshold = 7;
    Set<String> signersFound =
        Sep10Challenge.verifyChallengeTransactionThreshold(
            transaction.toEnvelopeXdrBase64(),
            server.getAccountId(),
            network,
            domainName,
            webAuthDomain,
            threshold,
            signers);
    assertEquals(
        new HashSet<String>(
            Arrays.asList(
                masterClient.getAccountId(),
                signerClient1.getAccountId(),
                signerClient2.getAccountId())),
        signersFound);
  }

  @Test
  public void
      testVerifyChallengeTransactionThresholdValidServerAndMultipleClientKeyMeetingThresholdSomeUnused()
          throws IOException, InvalidSep10ChallengeException {
    Network network = Network.TESTNET;
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    KeyPair signerClient2 = KeyPair.random();
    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, network, masterClient.getAccountId(), domainName, webAuthDomain, timeBounds);

    transaction.sign(masterClient);
    transaction.sign(signerClient1);

    Set<Sep10Challenge.Signer> signers =
        new HashSet<Sep10Challenge.Signer>(
            Arrays.asList(
                new Sep10Challenge.Signer(masterClient.getAccountId(), 1),
                new Sep10Challenge.Signer(signerClient1.getAccountId(), 2),
                new Sep10Challenge.Signer(signerClient2.getAccountId(), 4)));

    int threshold = 3;
    Set<String> signersFound =
        Sep10Challenge.verifyChallengeTransactionThreshold(
            transaction.toEnvelopeXdrBase64(),
            server.getAccountId(),
            network,
            domainName,
            webAuthDomain,
            threshold,
            signers);
    assertEquals(
        new HashSet<String>(
            Arrays.asList(masterClient.getAccountId(), signerClient1.getAccountId())),
        signersFound);
  }

  @Test
  public void testVerifyChallengeTransactionWithAccountIdNonCompliantWithEd25519()
      throws IOException, InvalidSep10ChallengeException {
    Network network = Network.TESTNET;
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, network, masterClient.getAccountId(), domainName, webAuthDomain, timeBounds);

    transaction.sign(masterClient);

    Set<Sep10Challenge.Signer> signers =
        new HashSet<Sep10Challenge.Signer>(
            Arrays.asList(
                new Sep10Challenge.Signer(masterClient.getAccountId(), 2),
                new Sep10Challenge.Signer(
                    "GA2T6GR7VXXXBETTERSAFETHANSORRYXXXPROTECTEDBYLOBSTRVAULT", 1)));

    int threshold = 2;
    Set<String> signersFound =
        Sep10Challenge.verifyChallengeTransactionThreshold(
            transaction.toEnvelopeXdrBase64(),
            server.getAccountId(),
            network,
            domainName,
            webAuthDomain,
            threshold,
            signers);
    assertEquals(
        new HashSet<String>(Collections.singletonList(masterClient.getAccountId())), signersFound);
  }

  @Test
  public void
      testVerifyChallengeTransactionThresholdValidServerAndMultipleClientKeyMeetingThresholdSomeUnusedIgnorePreauthTxHashAndXHash()
          throws IOException, InvalidSep10ChallengeException {
    Network network = Network.TESTNET;
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    KeyPair signerClient2 = KeyPair.random();

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, network, masterClient.getAccountId(), domainName, webAuthDomain, timeBounds);

    transaction.sign(masterClient);
    transaction.sign(signerClient1);

    String preauthTxHash = "TAQCSRX2RIDJNHFIFHWD63X7D7D6TRT5Y2S6E3TEMXTG5W3OECHZ2OG4";
    String xHash = "XDRPF6NZRR7EEVO7ESIWUDXHAOMM2QSKIQQBJK6I2FB7YKDZES5UCLWD";
    String unknownSignerType = "?ARPF6NZRR7EEVO7ESIWUDXHAOMM2QSKIQQBJK6I2FB7YKDZES5UCLWD";
    Set<Sep10Challenge.Signer> signers =
        new HashSet<Sep10Challenge.Signer>(
            Arrays.asList(
                new Sep10Challenge.Signer(masterClient.getAccountId(), 1),
                new Sep10Challenge.Signer(signerClient1.getAccountId(), 2),
                new Sep10Challenge.Signer(signerClient2.getAccountId(), 4),
                new Sep10Challenge.Signer(preauthTxHash, 10),
                new Sep10Challenge.Signer(xHash, 10),
                new Sep10Challenge.Signer(unknownSignerType, 10)));

    int threshold = 3;
    Set<String> signersFound =
        Sep10Challenge.verifyChallengeTransactionThreshold(
            transaction.toEnvelopeXdrBase64(),
            server.getAccountId(),
            network,
            domainName,
            webAuthDomain,
            threshold,
            signers);
    assertEquals(
        new HashSet<String>(
            Arrays.asList(masterClient.getAccountId(), signerClient1.getAccountId())),
        signersFound);
  }

  @Test
  public void
      testVerifyChallengeTransactionThresholdInvalidServerAndMultipleClientKeyNotMeetingThreshold()
          throws InvalidSep10ChallengeException, IOException {
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    KeyPair signerClient2 = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, network, masterClient.getAccountId(), domainName, webAuthDomain, timeBounds);

    transaction.sign(masterClient);
    transaction.sign(signerClient1);

    Set<Sep10Challenge.Signer> signers =
        new HashSet<Sep10Challenge.Signer>(
            Arrays.asList(
                new Sep10Challenge.Signer(masterClient.getAccountId(), 1),
                new Sep10Challenge.Signer(signerClient1.getAccountId(), 2),
                new Sep10Challenge.Signer(signerClient2.getAccountId(), 4)));

    int threshold = 7;
    try {
      Sep10Challenge.verifyChallengeTransactionThreshold(
          transaction.toEnvelopeXdrBase64(),
          server.getAccountId(),
          network,
          domainName,
          webAuthDomain,
          threshold,
          signers);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Signers with weight 3 do not meet threshold 7.", e.getMessage());
    }
  }

  @Test
  public void testVerifyChallengeTransactionThresholdInvalidClientKeyUnrecognized()
      throws InvalidSep10ChallengeException, IOException {
    Network network = Network.TESTNET;
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    KeyPair signerClient2 = KeyPair.random();

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, network, masterClient.getAccountId(), domainName, webAuthDomain, timeBounds);

    transaction.sign(masterClient);
    transaction.sign(signerClient1);
    transaction.sign(signerClient2);
    transaction.sign(KeyPair.random());

    Set<Sep10Challenge.Signer> signers =
        new HashSet<Sep10Challenge.Signer>(
            Arrays.asList(
                new Sep10Challenge.Signer(masterClient.getAccountId(), 1),
                new Sep10Challenge.Signer(signerClient1.getAccountId(), 2),
                new Sep10Challenge.Signer(signerClient2.getAccountId(), 4)));

    int threshold = 7;
    try {
      Sep10Challenge.verifyChallengeTransactionThreshold(
          transaction.toEnvelopeXdrBase64(),
          server.getAccountId(),
          network,
          domainName,
          webAuthDomain,
          threshold,
          signers);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Transaction has unrecognized signatures.", e.getMessage());
    }
  }

  @Test
  public void testVerifyChallengeTransactionThresholdInvalidNoSigners()
      throws InvalidSep10ChallengeException, IOException {
    Network network = Network.TESTNET;
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    KeyPair signerClient2 = KeyPair.random();

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, network, masterClient.getAccountId(), domainName, webAuthDomain, timeBounds);

    transaction.sign(masterClient);
    transaction.sign(signerClient1);
    transaction.sign(signerClient2);

    Set<Sep10Challenge.Signer> signers = Collections.emptySet();
    int threshold = 3;
    try {
      Sep10Challenge.verifyChallengeTransactionThreshold(
          transaction.toEnvelopeXdrBase64(),
          server.getAccountId(),
          network,
          domainName,
          webAuthDomain,
          threshold,
          signers);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals(
          "No verifiable signers provided, at least one G... address must be provided.",
          e.getMessage());
    }
  }

  @Test
  public void testVerifyChallengeTransactionThresholdInvalidNoPublicKeySigners()
      throws InvalidSep10ChallengeException, IOException {
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    KeyPair signerClient2 = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, network, masterClient.getAccountId(), domainName, webAuthDomain, timeBounds);

    transaction.sign(masterClient);
    transaction.sign(signerClient1);
    transaction.sign(signerClient2);

    String preauthTxHash = "TAQCSRX2RIDJNHFIFHWD63X7D7D6TRT5Y2S6E3TEMXTG5W3OECHZ2OG4";
    String xHash = "XDRPF6NZRR7EEVO7ESIWUDXHAOMM2QSKIQQBJK6I2FB7YKDZES5UCLWD";
    String unknownSignerType = "?ARPF6NZRR7EEVO7ESIWUDXHAOMM2QSKIQQBJK6I2FB7YKDZES5UCLWD";
    Set<Sep10Challenge.Signer> signers =
        new HashSet<Sep10Challenge.Signer>(
            Arrays.asList(
                new Sep10Challenge.Signer(preauthTxHash, 1),
                new Sep10Challenge.Signer(xHash, 2),
                new Sep10Challenge.Signer(unknownSignerType, 2)));
    int threshold = 3;

    try {
      Sep10Challenge.verifyChallengeTransactionThreshold(
          transaction.toEnvelopeXdrBase64(),
          server.getAccountId(),
          network,
          domainName,
          webAuthDomain,
          threshold,
          signers);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals(
          "No verifiable signers provided, at least one G... address must be provided.",
          e.getMessage());
    }
  }

  @Test
  public void testVerifyChallengeTransactionThresholdWeightsAddToMoreThan8Bits()
      throws IOException, InvalidSep10ChallengeException {
    Network network = Network.TESTNET;
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, network, masterClient.getAccountId(), domainName, webAuthDomain, timeBounds);

    transaction.sign(masterClient);
    transaction.sign(signerClient1);

    Set<Sep10Challenge.Signer> signers =
        new HashSet<Sep10Challenge.Signer>(
            Arrays.asList(
                new Sep10Challenge.Signer(masterClient.getAccountId(), 255),
                new Sep10Challenge.Signer(signerClient1.getAccountId(), 1)));

    int threshold = 1;
    Set<String> signersFound =
        Sep10Challenge.verifyChallengeTransactionThreshold(
            transaction.toEnvelopeXdrBase64(),
            server.getAccountId(),
            network,
            domainName,
            webAuthDomain,
            threshold,
            signers);
    assertEquals(
        new HashSet<String>(
            Arrays.asList(masterClient.getAccountId(), signerClient1.getAccountId())),
        signersFound);
  }

  @Test
  public void testVerifyChallengeTransactionSignersInvalidServer() throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    KeyPair signerClient2 = KeyPair.random();
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    byte[] nonce = new byte[48];
    SecureRandom random = new SecureRandom();
    random.nextBytes(nonce);
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] encodedNonce = base64Encoding.encode(nonce).getBytes();

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation manageDataOperation1 =
        new ManageDataOperation.Builder(domainName + " auth", encodedNonce)
            .setSourceAccount(masterClient.getAccountId())
            .build();

    Operation[] operations = new Operation[] {manageDataOperation1};
    Transaction transaction =
        new TransactionBuilder(AccountConverter.disableMuxed(), sourceAccount, network)
            .setBaseFee(100 * operations.length)
            .addOperations(Arrays.asList(operations))
            .addMemo(Memo.none())
            .addPreconditions(TransactionPreconditions.builder().timeBounds(timeBounds).build())
            .build();

    transaction.sign(masterClient);
    transaction.sign(signerClient1);
    transaction.sign(signerClient2);

    Set<String> signers =
        new HashSet<String>(
            Arrays.asList(
                masterClient.getAccountId(),
                signerClient1.getAccountId(),
                signerClient2.getAccountId(),
                KeyPair.random().getAccountId()));
    try {
      Sep10Challenge.verifyChallengeTransactionSigners(
          transaction.toEnvelopeXdrBase64(),
          server.getAccountId(),
          network,
          domainName,
          webAuthDomain,
          signers);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals(
          String.format("Transaction not signed by server: %s.", server.getAccountId()),
          e.getMessage());
    }
  }

  @Test
  public void testVerifyChallengeTransactionSignersValidServerAndClientMasterKey()
      throws InvalidSep10ChallengeException, IOException {
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, network, masterClient.getAccountId(), domainName, webAuthDomain, timeBounds);

    transaction.sign(masterClient);

    Set<String> signers =
        new HashSet<String>(Collections.singletonList(masterClient.getAccountId()));
    Set<String> signersFound =
        Sep10Challenge.verifyChallengeTransactionSigners(
            transaction.toEnvelopeXdrBase64(),
            server.getAccountId(),
            network,
            domainName,
            webAuthDomain,
            signers);
    assertEquals(signers, signersFound);
  }

  @Test
  public void testVerifyChallengeTransactionSignersValidMultipleDomainNames()
      throws InvalidSep10ChallengeException, IOException {
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, network, masterClient.getAccountId(), domainName, webAuthDomain, timeBounds);

    transaction.sign(masterClient);

    Set<String> signers =
        new HashSet<String>(Collections.singletonList(masterClient.getAccountId()));
    Set<String> signersFound =
        Sep10Challenge.verifyChallengeTransactionSigners(
            transaction.toEnvelopeXdrBase64(),
            server.getAccountId(),
            network,
            new String[] {"example3.com", "example2.com", "example.com"},
            webAuthDomain,
            signers);
    assertEquals(signers, signersFound);
  }

  @Test
  public void testVerifyChallengeTransactionSignersInvalidServerAndNoClient()
      throws InvalidSep10ChallengeException, IOException {
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    KeyPair signerClient2 = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, network, masterClient.getAccountId(), domainName, webAuthDomain, timeBounds);

    Set<String> signers =
        new HashSet<String>(
            Arrays.asList(
                masterClient.getAccountId(),
                signerClient1.getAccountId(),
                signerClient2.getAccountId(),
                KeyPair.random().getAccountId()));
    try {
      Sep10Challenge.verifyChallengeTransactionSigners(
          transaction.toEnvelopeXdrBase64(),
          server.getAccountId(),
          network,
          domainName,
          webAuthDomain,
          signers);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Transaction not signed by any client signer.", e.getMessage());
    }
  }

  @Test
  public void testVerifyChallengeTransactionSignersInvalidServerAndClientKeyUnrecognized()
      throws InvalidSep10ChallengeException, IOException {
    Network network = Network.TESTNET;
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    KeyPair signerClient2 = KeyPair.random();

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, network, masterClient.getAccountId(), domainName, webAuthDomain, timeBounds);

    transaction.sign(masterClient);
    transaction.sign(signerClient1);
    transaction.sign(signerClient2);
    transaction.sign(KeyPair.random());

    Set<String> signers =
        new HashSet<String>(
            Arrays.asList(
                masterClient.getAccountId(),
                signerClient1.getAccountId(),
                signerClient2.getAccountId(),
                KeyPair.random().getAccountId()));
    try {
      Sep10Challenge.verifyChallengeTransactionSigners(
          transaction.toEnvelopeXdrBase64(),
          server.getAccountId(),
          network,
          domainName,
          webAuthDomain,
          signers);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Transaction has unrecognized signatures.", e.getMessage());
    }
  }

  @Test
  public void testVerifyChallengeTransactionSignersValidServerAndMultipleClientSigners()
      throws InvalidSep10ChallengeException, IOException {
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    KeyPair signerClient2 = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, network, masterClient.getAccountId(), domainName, webAuthDomain, timeBounds);

    transaction.sign(signerClient1);
    transaction.sign(signerClient2);

    Set<String> signers =
        new HashSet<String>(
            Arrays.asList(
                masterClient.getAccountId(),
                signerClient1.getAccountId(),
                signerClient2.getAccountId(),
                KeyPair.random().getAccountId()));
    Set<String> signersFound =
        Sep10Challenge.verifyChallengeTransactionSigners(
            transaction.toEnvelopeXdrBase64(),
            server.getAccountId(),
            network,
            domainName,
            webAuthDomain,
            signers);
    assertEquals(
        new HashSet<String>(
            Arrays.asList(signerClient1.getAccountId(), signerClient2.getAccountId())),
        signersFound);
  }

  @Test
  public void testVerifyChallengeTransactionSignersValidServerAndMultipleClientSignersReverseOrder()
      throws InvalidSep10ChallengeException, IOException {
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    KeyPair signerClient2 = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, network, masterClient.getAccountId(), domainName, webAuthDomain, timeBounds);

    transaction.sign(signerClient2);
    transaction.sign(signerClient1);

    Set<String> signers =
        new HashSet<String>(
            Arrays.asList(
                masterClient.getAccountId(),
                signerClient1.getAccountId(),
                signerClient2.getAccountId(),
                KeyPair.random().getAccountId()));
    Set<String> signersFound =
        Sep10Challenge.verifyChallengeTransactionSigners(
            transaction.toEnvelopeXdrBase64(),
            server.getAccountId(),
            network,
            domainName,
            webAuthDomain,
            signers);
    assertEquals(
        new HashSet<String>(
            Arrays.asList(signerClient1.getAccountId(), signerClient2.getAccountId())),
        signersFound);
  }

  @Test
  public void testVerifyChallengeTransactionSignersValidServerAndClientSignersNotMasterKey()
      throws InvalidSep10ChallengeException, IOException {
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, network, masterClient.getAccountId(), domainName, webAuthDomain, timeBounds);

    transaction.sign(signerClient1);

    Set<String> signers =
        new HashSet<String>(
            Arrays.asList(masterClient.getAccountId(), signerClient1.getAccountId()));
    Set<String> signersFound =
        Sep10Challenge.verifyChallengeTransactionSigners(
            transaction.toEnvelopeXdrBase64(),
            server.getAccountId(),
            network,
            domainName,
            webAuthDomain,
            signers);
    assertEquals(
        new HashSet<String>(Collections.singletonList(signerClient1.getAccountId())), signersFound);
  }

  @Test
  public void testVerifyChallengeTransactionSignersValidServerAndClientSignersIgnoresServerSigner()
      throws InvalidSep10ChallengeException, IOException {
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, network, masterClient.getAccountId(), domainName, webAuthDomain, timeBounds);

    transaction.sign(signerClient1);

    Set<String> signers =
        new HashSet<String>(
            Arrays.asList(
                masterClient.getAccountId(), signerClient1.getAccountId(), server.getAccountId()));
    Set<String> signersFound =
        Sep10Challenge.verifyChallengeTransactionSigners(
            transaction.toEnvelopeXdrBase64(),
            server.getAccountId(),
            network,
            domainName,
            webAuthDomain,
            signers);
    assertEquals(
        new HashSet<String>(Collections.singletonList(signerClient1.getAccountId())), signersFound);
  }

  @Test
  public void testVerifyChallengeTransactionSignersInvalidServerNoClientSignersIgnoresServerSigner()
      throws InvalidSep10ChallengeException, IOException {
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, network, masterClient.getAccountId(), domainName, webAuthDomain, timeBounds);

    Set<String> signers =
        new HashSet<String>(
            Arrays.asList(
                masterClient.getAccountId(), signerClient1.getAccountId(), server.getAccountId()));
    try {
      Sep10Challenge.verifyChallengeTransactionSigners(
          transaction.toEnvelopeXdrBase64(),
          server.getAccountId(),
          network,
          domainName,
          webAuthDomain,
          signers);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Transaction not signed by any client signer.", e.getMessage());
    }
  }

  @Test
  public void testVerifyChallengeTransactionSignersValidIgnorePreauthTxHashAndXHash()
      throws InvalidSep10ChallengeException, IOException {
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, network, masterClient.getAccountId(), domainName, webAuthDomain, timeBounds);

    transaction.sign(signerClient1);

    String preauthTxHash = "TAQCSRX2RIDJNHFIFHWD63X7D7D6TRT5Y2S6E3TEMXTG5W3OECHZ2OG4";
    String xHash = "XDRPF6NZRR7EEVO7ESIWUDXHAOMM2QSKIQQBJK6I2FB7YKDZES5UCLWD";
    String unknownSignerType = "?ARPF6NZRR7EEVO7ESIWUDXHAOMM2QSKIQQBJK6I2FB7YKDZES5UCLWD";
    Set<String> signers =
        new HashSet<String>(
            Arrays.asList(
                masterClient.getAccountId(),
                signerClient1.getAccountId(),
                preauthTxHash,
                xHash,
                unknownSignerType));
    Set<String> signersFound =
        Sep10Challenge.verifyChallengeTransactionSigners(
            transaction.toEnvelopeXdrBase64(),
            server.getAccountId(),
            network,
            domainName,
            webAuthDomain,
            signers);
    assertEquals(
        new HashSet<String>(Collections.singletonList(signerClient1.getAccountId())), signersFound);
  }

  @Test
  public void
      testVerifyChallengeTransactionSignersInvalidServerAndClientSignersFailsDuplicateSignatures()
          throws InvalidSep10ChallengeException, IOException {
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, network, masterClient.getAccountId(), domainName, webAuthDomain, timeBounds);

    transaction.sign(signerClient1);
    transaction.sign(signerClient1);

    Set<String> signers =
        new HashSet<String>(
            Arrays.asList(masterClient.getAccountId(), signerClient1.getAccountId()));
    try {
      Sep10Challenge.verifyChallengeTransactionSigners(
          transaction.toEnvelopeXdrBase64(),
          server.getAccountId(),
          network,
          domainName,
          webAuthDomain,
          signers);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Transaction has unrecognized signatures.", e.getMessage());
    }
  }

  @Test
  public void testVerifyChallengeTransactionSignersInvalidServerAndClientSignersFailsSignerSeed()
      throws InvalidSep10ChallengeException, IOException {
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, network, masterClient.getAccountId(), domainName, webAuthDomain, timeBounds);

    transaction.sign(signerClient1);

    Set<String> signers =
        new HashSet<String>(Collections.singletonList(new String(signerClient1.getSecretSeed())));
    try {
      Sep10Challenge.verifyChallengeTransactionSigners(
          transaction.toEnvelopeXdrBase64(),
          server.getAccountId(),
          network,
          domainName,
          webAuthDomain,
          signers);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals(
          "No verifiable signers provided, at least one G... address must be provided.",
          e.getMessage());
    }
  }

  @Test
  public void testVerifyChallengeTransactionSignersInvalidNoSignersNull()
      throws InvalidSep10ChallengeException, IOException {
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    Network network = Network.TESTNET;
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, network, masterClient.getAccountId(), domainName, webAuthDomain, timeBounds);

    transaction.sign(signerClient1);

    try {
      Sep10Challenge.verifyChallengeTransactionSigners(
          transaction.toEnvelopeXdrBase64(),
          server.getAccountId(),
          network,
          domainName,
          webAuthDomain,
          null);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals(
          "No verifiable signers provided, at least one G... address must be provided.",
          e.getMessage());
    }
  }

  @Test
  public void testVerifyChallengeTransactionSignersInvalidNoSignersEmptySet()
      throws InvalidSep10ChallengeException, IOException {
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server, network, masterClient.getAccountId(), domainName, webAuthDomain, timeBounds);

    transaction.sign(signerClient1);

    try {
      Sep10Challenge.verifyChallengeTransactionSigners(
          transaction.toEnvelopeXdrBase64(),
          server.getAccountId(),
          network,
          domainName,
          webAuthDomain,
          Collections.<String>emptySet());
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals(
          "No verifiable signers provided, at least one G... address must be provided.",
          e.getMessage());
    }
  }

  @Test
  public void
      testVerifyChallengeTransactionValidAdditionalManageDataOpsWithSourceAccountSetToServerAccount()
          throws IOException, InvalidSep10ChallengeException {
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    byte[] nonce = new byte[48];
    SecureRandom random = new SecureRandom();
    random.nextBytes(nonce);
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] encodedNonce = base64Encoding.encode(nonce).getBytes();

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation operation1 =
        new ManageDataOperation.Builder(domainName + " auth", encodedNonce)
            .setSourceAccount(masterClient.getAccountId())
            .build();
    ManageDataOperation operation2 =
        new ManageDataOperation.Builder("key", "value".getBytes())
            .setSourceAccount(server.getAccountId())
            .build();
    Operation[] operations = new Operation[] {operation1, operation2};
    Transaction transaction =
        new TransactionBuilder(AccountConverter.disableMuxed(), sourceAccount, network)
            .setBaseFee(100 * operations.length)
            .addOperations(Arrays.asList(operations))
            .addMemo(Memo.none())
            .addPreconditions(TransactionPreconditions.builder().timeBounds(timeBounds).build())
            .build();
    transaction.sign(server);
    transaction.sign(masterClient);

    Set<String> signers =
        new HashSet<String>(Collections.singletonList(masterClient.getAccountId()));
    Set<String> signersFound =
        Sep10Challenge.verifyChallengeTransactionSigners(
            transaction.toEnvelopeXdrBase64(),
            server.getAccountId(),
            network,
            domainName,
            webAuthDomain,
            signers);
    assertEquals(signers, signersFound);
  }

  @Test
  public void
      testVerifyChallengeTransactionInvalidAdditionalManageDataOpsWithoutSourceAccountSetToServerAccount()
          throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    byte[] nonce = new byte[48];
    SecureRandom random = new SecureRandom();
    random.nextBytes(nonce);
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] encodedNonce = base64Encoding.encode(nonce).getBytes();

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation operation1 =
        new ManageDataOperation.Builder(domainName + " auth", encodedNonce)
            .setSourceAccount(masterClient.getAccountId())
            .build();
    ManageDataOperation operation2 =
        new ManageDataOperation.Builder("key", "value".getBytes())
            .setSourceAccount(masterClient.getAccountId())
            .build();
    Operation[] operations = new Operation[] {operation1, operation2};
    Transaction transaction =
        new TransactionBuilder(AccountConverter.disableMuxed(), sourceAccount, network)
            .setBaseFee(100 * operations.length)
            .addOperations(Arrays.asList(operations))
            .addMemo(Memo.none())
            .addPreconditions(TransactionPreconditions.builder().timeBounds(timeBounds).build())
            .build();
    transaction.sign(server);
    transaction.sign(masterClient);

    Set<String> signers =
        new HashSet<String>(Collections.singletonList(masterClient.getAccountId()));
    try {
      Sep10Challenge.verifyChallengeTransactionSigners(
          transaction.toEnvelopeXdrBase64(),
          server.getAccountId(),
          network,
          domainName,
          webAuthDomain,
          signers);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Subsequent operations are unrecognized.", e.getMessage());
    }
  }

  @Test
  public void
      testVerifyChallengeTransactionInvalidAdditionalManageDataOpsWithSourceAccountSetToNull()
          throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    byte[] nonce = new byte[48];
    SecureRandom random = new SecureRandom();
    random.nextBytes(nonce);
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] encodedNonce = base64Encoding.encode(nonce).getBytes();

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation operation1 =
        new ManageDataOperation.Builder(domainName + " auth", encodedNonce)
            .setSourceAccount(masterClient.getAccountId())
            .build();
    ManageDataOperation operation2 =
        new ManageDataOperation.Builder("key", "value".getBytes()).build();
    Operation[] operations = new Operation[] {operation1, operation2};
    Transaction transaction =
        new TransactionBuilder(AccountConverter.disableMuxed(), sourceAccount, network)
            .setBaseFee(100 * operations.length)
            .addOperations(Arrays.asList(operations))
            .addMemo(Memo.none())
            .addPreconditions(TransactionPreconditions.builder().timeBounds(timeBounds).build())
            .build();
    transaction.sign(server);
    transaction.sign(masterClient);

    Set<String> signers =
        new HashSet<String>(Collections.singletonList(masterClient.getAccountId()));
    try {
      Sep10Challenge.verifyChallengeTransactionSigners(
          transaction.toEnvelopeXdrBase64(),
          server.getAccountId(),
          network,
          domainName,
          webAuthDomain,
          signers);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Operation should have a source account.", e.getMessage());
    }
  }

  @Test
  public void testVerifyChallengeTransactionInvalidAdditionalOpsOfOtherTypes() throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    byte[] nonce = new byte[48];
    SecureRandom random = new SecureRandom();
    random.nextBytes(nonce);
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] encodedNonce = base64Encoding.encode(nonce).getBytes();

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation operation1 =
        new ManageDataOperation.Builder(domainName + " auth", encodedNonce)
            .setSourceAccount(masterClient.getAccountId())
            .build();
    BumpSequenceOperation operation2 =
        new BumpSequenceOperation.Builder(0L).setSourceAccount(server.getAccountId()).build();
    Operation[] operations = new Operation[] {operation1, operation2};
    Transaction transaction =
        new TransactionBuilder(AccountConverter.disableMuxed(), sourceAccount, network)
            .setBaseFee(100 * operations.length)
            .addOperations(Arrays.asList(operations))
            .addMemo(Memo.none())
            .addPreconditions(TransactionPreconditions.builder().timeBounds(timeBounds).build())
            .build();
    transaction.sign(server);
    transaction.sign(masterClient);

    Set<String> signers =
        new HashSet<String>(Collections.singletonList(masterClient.getAccountId()));
    try {
      Sep10Challenge.verifyChallengeTransactionSigners(
          transaction.toEnvelopeXdrBase64(),
          server.getAccountId(),
          network,
          domainName,
          webAuthDomain,
          signers);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Operation type should be ManageData.", e.getMessage());
    }
  }
}
