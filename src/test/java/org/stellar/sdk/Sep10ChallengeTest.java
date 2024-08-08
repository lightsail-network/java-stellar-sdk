package org.stellar.sdk;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import org.stellar.sdk.exception.InvalidSep10ChallengeException;
import org.stellar.sdk.operations.BumpSequenceOperation;
import org.stellar.sdk.operations.ManageDataOperation;
import org.stellar.sdk.operations.Operation;
import org.stellar.sdk.operations.SetOptionsOperation;
import org.stellar.sdk.xdr.DecoratedSignature;
import org.stellar.sdk.xdr.EnvelopeType;
import org.stellar.sdk.xdr.TransactionEnvelope;

public class Sep10ChallengeTest {

  @Test
  public void testChallenge() {
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
    Base64.getDecoder().decode(new String(homeDomainOp.getValue()));

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
  public void testNewChallengeRejectsMuxedClientSigningKey() {
    try {
      KeyPair server = KeyPair.random();
      KeyPair client = KeyPair.random();
      long now = System.currentTimeMillis() / 1000L;
      long end = now + 300;
      TimeBounds timeBounds = new TimeBounds(now, end);
      String domainName = "example.com";
      String webAuthDomain = "example.com";
      String clientDomain = "client.domain.com";
      String clientSigningKey =
          "MAQAA5L65LSYH7CQ3VTJ7F3HHLGCL3DSLAR2Y47263D56MNNGHSQSAAAAAAAAAAE2LP26";

      Sep10Challenge.newChallenge(
          server,
          Network.TESTNET,
          client.getAccountId(),
          domainName,
          webAuthDomain,
          timeBounds,
          clientDomain,
          clientSigningKey);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals(
          "MAQAA5L65LSYH7CQ3VTJ7F3HHLGCL3DSLAR2Y47263D56MNNGHSQSAAAAAAAAAAE2LP26 is not a valid account id",
          e.getMessage());
    }
  }

  @Test
  public void testNewChallengeRejectsInvalidMemo() {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    Network network = Network.TESTNET;
    String domainName = "example.com";
    String webAuthDomain = "example.com";
    Memo memo = Memo.text("invalid memo");

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    try {
      Sep10Challenge.newChallenge(
          server,
          network,
          client.getAccountId(),
          domainName,
          webAuthDomain,
          timeBounds,
          "",
          "",
          memo);
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("only memo type `id` is supported", e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionValidSignedByServer() {
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
  public void testReadChallengeTransactionAcceptsBothV0AndV1() {
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

    for (String envelopeBase64 : Arrays.asList(v0Base64, v1Base64)) {
      Sep10Challenge.ChallengeTransaction challengeTransaction =
          Sep10Challenge.readChallengeTransaction(
              envelopeBase64, server.getAccountId(), Network.TESTNET, domainName, webAuthDomain);
      assertEquals(
          new Sep10Challenge.ChallengeTransaction(transaction, client.getAccountId(), domainName),
          challengeTransaction);
    }
  }

  @Test
  public void testReadChallengeTransactionRejectsMuxedServer() {
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
  public void testReadChallengeTransactionRejectsMuxedClient() {
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
                new Account(transaction.getSourceAccount(), 100L), transaction.getNetwork())
            .setBaseFee((int) transaction.getFee())
            .addMemo(transaction.getMemo())
            .addOperations(Arrays.asList(operations))
            .addPreconditions(transaction.getPreconditions())
            .build();

    for (DecoratedSignature signature : transaction.signatures) {
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
  public void testReadChallengeTransactionValidSignedByServerAndClient() {
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
  public void testReadChallengeTransactionInvalidNotSignedByServer() {
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
    byte[] encodedNonce = java.util.Base64.getEncoder().encode(nonce);

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation manageDataOperation1 =
        ManageDataOperation.builder()
            .name(domainName + " auth")
            .value(encodedNonce)
            .sourceAccount(client.getAccountId())
            .build();

    Operation[] operations = new Operation[] {manageDataOperation1};
    Transaction transaction =
        new TransactionBuilder(sourceAccount, network)
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
  public void testReadChallengeTransactionInvalidCorrupted() {
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
  public void testReadChallengeTransactionInvalidServerAccountIDMismatch() {
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
  public void testReadChallengeTransactionInvalidSeqNoNotZero() {
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
    byte[] encodedNonce = java.util.Base64.getEncoder().encode(nonce);

    Account sourceAccount = new Account(server.getAccountId(), 100L);
    ManageDataOperation manageDataOperation1 =
        ManageDataOperation.builder()
            .name(domainName + " auth")
            .value(encodedNonce)
            .sourceAccount(client.getAccountId())
            .build();

    Operation[] operations = new Operation[] {manageDataOperation1};
    Transaction transaction =
        new TransactionBuilder(sourceAccount, network)
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
  public void testReadChallengeTransactionInvalidTimeboundsInfinite() {
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
    byte[] encodedNonce = java.util.Base64.getEncoder().encode(nonce);

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation operation =
        ManageDataOperation.builder()
            .name(domainName + " auth")
            .value(encodedNonce)
            .sourceAccount(client.getAccountId())
            .build();
    Operation[] operations = new Operation[] {operation};

    Transaction transaction =
        new TransactionBuilder(sourceAccount, network)
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
  public void testReadChallengeTransactionInvalidTimeBoundsTooEarly() {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();

    long current = System.currentTimeMillis() / 1000L;
    long start = current + Sep10Challenge.GRACE_PERIOD_SECONDS.longValue() + 30;
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
  public void testReadChallengeTransactionGracePeriod() {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();

    long current = System.currentTimeMillis() / 1000L;
    long start = current + Sep10Challenge.GRACE_PERIOD_SECONDS.longValue() - 30;
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
  public void testReadChallengeTransactionInvalidTimeBoundsTooLate() {
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
  public void testReadChallengeTransactionInvalidOperationWrongType() {
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
        SetOptionsOperation.builder().sourceAccount(client.getAccountId()).build();

    Operation[] operations = new Operation[] {setOptionsOperation};
    Transaction transaction =
        new TransactionBuilder(sourceAccount, network)
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
  public void testReadChallengeTransactionInvalidOperationNoSourceAccount() {
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
    byte[] encodedNonce = java.util.Base64.getEncoder().encode(nonce);

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation manageDataOperation1 =
        ManageDataOperation.builder().name(domainName + " auth").value(encodedNonce).build();

    Operation[] operations = new Operation[] {manageDataOperation1};
    Transaction transaction =
        new TransactionBuilder(sourceAccount, network)
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
  public void testReadChallengeTransactionInvalidDataValueWrongEncodedLength() {
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
    byte[] encodedNonce = java.util.Base64.getEncoder().encode(nonce);

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation manageDataOperation1 =
        ManageDataOperation.builder()
            .name(domainName + " auth")
            .value(encodedNonce)
            .sourceAccount(client.getAccountId())
            .build();

    Operation[] operations = new Operation[] {manageDataOperation1};
    Transaction transaction =
        new TransactionBuilder(sourceAccount, network)
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
  public void testReadChallengeTransactionInvalidDataValueCorruptBase64() {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    String domainName = "example.com";
    String webAuthDomain = "example.com";

    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    byte[] encodedNonce =
        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA?AAAAAAAAAAAAAAAAAAAAAAAAAA"
            .getBytes(StandardCharsets.UTF_8);
    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation manageDataOperation1 =
        ManageDataOperation.builder()
            .name(domainName + " auth")
            .value(encodedNonce)
            .sourceAccount(client.getAccountId())
            .build();

    Operation[] operations = new Operation[] {manageDataOperation1};
    Transaction transaction =
        new TransactionBuilder(sourceAccount, network)
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
      assertEquals("Illegal base64 character 3f", e.getCause().getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionInvalidDataValueWrongByteLength() {
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
    byte[] encodedNonce = java.util.Base64.getEncoder().encode(nonce);

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation manageDataOperation1 =
        ManageDataOperation.builder()
            .name(domainName + " auth")
            .value(encodedNonce)
            .sourceAccount(client.getAccountId())
            .build();

    Operation[] operations = new Operation[] {manageDataOperation1};
    Transaction transaction =
        new TransactionBuilder(sourceAccount, network)
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
  public void testReadChallengeTransactionInvalidDataValueIsNull() {
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
        ManageDataOperation.builder()
            .name(domainName + " auth")
            .value(null)
            .sourceAccount(client.getAccountId())
            .build();

    Operation[] operations = new Operation[] {manageDataOperation1};
    Transaction transaction =
        new TransactionBuilder(sourceAccount, network)
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
      testReadChallengeTransactionValidAdditionalManageDataOpsWithSourceAccountSetToServerAccount() {
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
    byte[] encodedNonce = java.util.Base64.getEncoder().encode(nonce);

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation operation1 =
        ManageDataOperation.builder()
            .name(domainName + " auth")
            .value(encodedNonce)
            .sourceAccount(client.getAccountId())
            .build();
    ManageDataOperation operation2 =
        ManageDataOperation.builder()
            .name("key")
            .value("value".getBytes())
            .sourceAccount(server.getAccountId())
            .build();
    Operation[] operations = new Operation[] {operation1, operation2};
    Transaction transaction =
        new TransactionBuilder(sourceAccount, network)
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
      testReadChallengeTransactionInvalidAdditionalManageDataOpsWithoutSourceAccountSetToServerAccount() {
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
    byte[] encodedNonce = java.util.Base64.getEncoder().encode(nonce);

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation operation1 =
        ManageDataOperation.builder()
            .name(domainName + " auth")
            .value(encodedNonce)
            .sourceAccount(client.getAccountId())
            .build();
    ManageDataOperation operation2 =
        ManageDataOperation.builder()
            .name("key")
            .value("value".getBytes())
            .sourceAccount(client.getAccountId())
            .build();
    Operation[] operations = new Operation[] {operation1, operation2};
    Transaction transaction =
        new TransactionBuilder(sourceAccount, network)
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
  public void
      testReadChallengeTransactionInvalidAdditionalManageDataOpsWithSourceAccountSetToNull() {
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
    byte[] encodedNonce = java.util.Base64.getEncoder().encode(nonce);

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation operation1 =
        ManageDataOperation.builder()
            .name(domainName + " auth")
            .value(encodedNonce)
            .sourceAccount(client.getAccountId())
            .build();
    ManageDataOperation operation2 =
        ManageDataOperation.builder().name("key").value("value".getBytes()).build();
    Operation[] operations = new Operation[] {operation1, operation2};
    Transaction transaction =
        new TransactionBuilder(sourceAccount, network)
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
  public void testReadChallengeTransactionInvalidAdditionalOpsOfOtherTypes() {
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
    byte[] encodedNonce = java.util.Base64.getEncoder().encode(nonce);

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation operation1 =
        ManageDataOperation.builder()
            .name(domainName + " auth")
            .value(encodedNonce)
            .sourceAccount(client.getAccountId())
            .build();
    BumpSequenceOperation operation2 =
        BumpSequenceOperation.builder().bumpTo(0L).sourceAccount(server.getAccountId()).build();
    Operation[] operations = new Operation[] {operation1, operation2};
    Transaction transaction =
        new TransactionBuilder(sourceAccount, network)
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
  public void testReadChallengeTransactionValidMultipleDomainNames() {
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
  public void testReadChallengeTransactionInvalidDomainNamesMismatch() {
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
  public void testReadChallengeTransactionInvalidDomainNamesEmpty() {
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
  public void testReadChallengeTransactionInvalidDomainNamesNull() {
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
  public void testReadChallengeTransactionValidWebAuthDomainNotEqualHomeDomain() {
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
  public void testReadChallengeTransactionInvalidWebAuthDomainMismatch() {
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
  public void testReadChallengeTransactionInvalidWebAuthDomainOperationValueIsNull() {
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
    byte[] encodedNonce = java.util.Base64.getEncoder().encode(nonce);

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation domainNameOperation =
        ManageDataOperation.builder()
            .name(domainName + " auth")
            .value(encodedNonce)
            .sourceAccount(client.getAccountId())
            .build();
    ManageDataOperation webAuthDomainOperation =
        ManageDataOperation.builder()
            .name("web_auth_domain")
            .value(null)
            .sourceAccount(server.getAccountId())
            .build();
    Operation[] operations = new Operation[] {domainNameOperation, webAuthDomainOperation};
    Transaction transaction =
        new TransactionBuilder(sourceAccount, network)
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
      testReadChallengeTransactionValidWebAuthDomainAllowSubsequentManageDataOperationsToHaveNullValue() {
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
    byte[] encodedNonce = java.util.Base64.getEncoder().encode(nonce);

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation domainNameOperation =
        ManageDataOperation.builder()
            .name(domainName + " auth")
            .value(encodedNonce)
            .sourceAccount(client.getAccountId())
            .build();
    ManageDataOperation webAuthDomainOperation =
        ManageDataOperation.builder()
            .name("web_auth_domain")
            .value(webAuthDomain.getBytes())
            .sourceAccount(server.getAccountId())
            .build();
    ManageDataOperation otherDomainOperation =
        ManageDataOperation.builder()
            .name("subsequent_op")
            .value(null)
            .sourceAccount(server.getAccountId())
            .build();
    Operation[] operations =
        new Operation[] {domainNameOperation, webAuthDomainOperation, otherDomainOperation};
    Transaction transaction =
        new TransactionBuilder(sourceAccount, network)
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
  public void testReadChallengeTransactionValidMemoId() {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    Network network = Network.TESTNET;
    String domainName = "example.com";
    String webAuthDomain = "example.com";
    Memo memo = Memo.id(123456L);

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    Transaction transaction =
        Sep10Challenge.newChallenge(
            server,
            network,
            client.getAccountId(),
            domainName,
            webAuthDomain,
            timeBounds,
            "",
            "",
            memo);

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
  public void testReadChallengeTransactionInvalidNotMemoId() {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    String domainName = "example.com";
    String webAuthDomain = "example.com";
    Memo memo = Memo.text("invalid memo");

    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    byte[] nonce = new byte[48];
    SecureRandom random = new SecureRandom();
    random.nextBytes(nonce);
    byte[] encodedNonce = java.util.Base64.getEncoder().encode(nonce);

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation manageDataOperation1 =
        ManageDataOperation.builder()
            .name(domainName + " auth")
            .value(encodedNonce)
            .sourceAccount(client.getAccountId())
            .build();

    Operation[] operations = new Operation[] {manageDataOperation1};
    Transaction transaction =
        new TransactionBuilder(sourceAccount, network)
            .setBaseFee(100 * operations.length)
            .addOperations(Arrays.asList(operations))
            .addMemo(memo)
            .addPreconditions(TransactionPreconditions.builder().timeBounds(timeBounds).build())
            .build();

    transaction.sign(server);
    String challenge = transaction.toEnvelopeXdrBase64();
    try {
      Sep10Challenge.readChallengeTransaction(
          challenge, server.getAccountId(), Network.TESTNET, domainName, webAuthDomain);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("only memo type `id` is supported", e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionRejectFeeBumpTransaction() {
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
    byte[] encodedNonce = java.util.Base64.getEncoder().encode(nonce);

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation manageDataOperation1 =
        ManageDataOperation.builder()
            .name(domainName + " auth")
            .value(encodedNonce)
            .sourceAccount(client.getAccountId())
            .build();

    Operation[] operations = new Operation[] {manageDataOperation1};
    Transaction transaction =
        new TransactionBuilder(sourceAccount, network)
            .setBaseFee(100 * operations.length)
            .addOperations(Arrays.asList(operations))
            .addPreconditions(TransactionPreconditions.builder().timeBounds(timeBounds).build())
            .build();

    transaction.sign(server);
    FeeBumpTransaction feeBumpTransaction =
        FeeBumpTransaction.createWithBaseFee(server.getAccountId(), 500, transaction);
    String challenge = feeBumpTransaction.toEnvelopeXdrBase64();
    try {
      Sep10Challenge.readChallengeTransaction(
          challenge, server.getAccountId(), Network.TESTNET, domainName, webAuthDomain);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Transaction cannot be a fee bump transaction", e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionRejectMuxedClientAccountId() {
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
    byte[] encodedNonce = java.util.Base64.getEncoder().encode(nonce);

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation manageDataOperation1 =
        ManageDataOperation.builder()
            .name(domainName + " auth")
            .value(encodedNonce)
            .sourceAccount("MCAAAAAAAAAAAAB7BQ2L7E5NBWMXDUCMZSIPOBKRDSBYVLMXGSSKF6YNPIB7Y77ITKNOG")
            .build();

    Operation[] operations = new Operation[] {manageDataOperation1};
    Transaction transaction =
        new TransactionBuilder(sourceAccount, network)
            .setBaseFee(100 * operations.length)
            .addOperations(Arrays.asList(operations))
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
          "clientAccountId: MCAAAAAAAAAAAAB7BQ2L7E5NBWMXDUCMZSIPOBKRDSBYVLMXGSSKF6YNPIB7Y77ITKNOG is not a valid account id",
          e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionInvalidNoSignature() {
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
    byte[] encodedNonce = java.util.Base64.getEncoder().encode(nonce);

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation manageDataOperation1 =
        ManageDataOperation.builder()
            .name(domainName + " auth")
            .value(encodedNonce)
            .sourceAccount(client.getAccountId())
            .build();

    Operation[] operations = new Operation[] {manageDataOperation1};
    Transaction transaction =
        new TransactionBuilder(sourceAccount, network)
            .setBaseFee(100 * operations.length)
            .addOperations(Arrays.asList(operations))
            .addPreconditions(TransactionPreconditions.builder().timeBounds(timeBounds).build())
            .build();

    String challenge = transaction.toEnvelopeXdrBase64();
    try {
      Sep10Challenge.readChallengeTransaction(
          challenge, server.getAccountId(), Network.TESTNET, domainName, webAuthDomain);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Transaction has no signatures.", e.getMessage());
    }
  }

  @Test
  public void testChallengeWithClientDomain() {
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
    Base64.getDecoder().decode(new String(homeDomainOp.getValue()));

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
  public void testVerifyChallengeWithClientDomain() {
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
    Set<String> signers = new HashSet<>(Collections.singletonList(client.getAccountId()));
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
  public void testVerifyChallengeWithClientDomainMissingSignature() {
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
  public void testVerifyChallengeTransactionThresholdInvalidNotSignedByServer() {
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
    byte[] encodedNonce = java.util.Base64.getEncoder().encode(nonce);

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation manageDataOperation1 =
        ManageDataOperation.builder()
            .name(domainName + " auth")
            .value(encodedNonce)
            .sourceAccount(masterClient.getAccountId())
            .build();

    Operation[] operations = new Operation[] {manageDataOperation1};
    Transaction transaction =
        new TransactionBuilder(sourceAccount, network)
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
  public void testVerifyChallengeTransactionThresholdValidServerAndClientKeyMeetingThreshold() {
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
  public void testVerifyChallengeTransactionThresholdValidMultipleDomainNames() {
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
      testVerifyChallengeTransactionThresholdValidServerAndMultipleClientKeyMeetingThreshold() {
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
      testVerifyChallengeTransactionThresholdValidServerAndMultipleClientKeyMeetingThresholdSomeUnused() {
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
  public void testVerifyChallengeTransactionWithAccountIdNonCompliantWithEd25519() {
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
      testVerifyChallengeTransactionThresholdValidServerAndMultipleClientKeyMeetingThresholdSomeUnusedIgnorePreauthTxHashAndXHash() {
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
      testVerifyChallengeTransactionThresholdInvalidServerAndMultipleClientKeyNotMeetingThreshold() {
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
  public void testVerifyChallengeTransactionThresholdInvalidClientKeyUnrecognized() {
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
  public void testVerifyChallengeTransactionThresholdInvalidNoSigners() {
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
  public void testVerifyChallengeTransactionThresholdInvalidNoPublicKeySigners() {
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
  public void testVerifyChallengeTransactionThresholdWeightsAddToMoreThan8Bits() {
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
  public void testVerifyChallengeTransactionSignersInvalidServer() {
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
    byte[] encodedNonce = java.util.Base64.getEncoder().encode(nonce);

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation manageDataOperation1 =
        ManageDataOperation.builder()
            .name(domainName + " auth")
            .value(encodedNonce)
            .sourceAccount(masterClient.getAccountId())
            .build();

    Operation[] operations = new Operation[] {manageDataOperation1};
    Transaction transaction =
        new TransactionBuilder(sourceAccount, network)
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
  public void testVerifyChallengeTransactionSignersValidServerAndClientMasterKey() {
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
  public void testVerifyChallengeTransactionSignersValidMultipleDomainNames() {
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
  public void testVerifyChallengeTransactionSignersInvalidServerAndNoClient() {
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
  public void testVerifyChallengeTransactionSignersInvalidServerAndClientKeyUnrecognized() {
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
  public void testVerifyChallengeTransactionSignersValidServerAndMultipleClientSigners() {
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
  public void
      testVerifyChallengeTransactionSignersValidServerAndMultipleClientSignersReverseOrder() {
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
  public void testVerifyChallengeTransactionSignersValidServerAndClientSignersNotMasterKey() {
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
  public void
      testVerifyChallengeTransactionSignersValidServerAndClientSignersIgnoresServerSigner() {
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
  public void
      testVerifyChallengeTransactionSignersInvalidServerNoClientSignersIgnoresServerSigner() {
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
  public void testVerifyChallengeTransactionSignersValidIgnorePreauthTxHashAndXHash() {
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
      testVerifyChallengeTransactionSignersInvalidServerAndClientSignersFailsDuplicateSignatures() {
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
  public void testVerifyChallengeTransactionSignersInvalidServerAndClientSignersFailsSignerSeed() {
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
  public void testVerifyChallengeTransactionSignersInvalidNoSignersNull() {
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
  public void testVerifyChallengeTransactionSignersInvalidNoSignersEmptySet() {
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
      testVerifyChallengeTransactionValidAdditionalManageDataOpsWithSourceAccountSetToServerAccount() {
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
    byte[] encodedNonce = java.util.Base64.getEncoder().encode(nonce);

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation operation1 =
        ManageDataOperation.builder()
            .name(domainName + " auth")
            .value(encodedNonce)
            .sourceAccount(masterClient.getAccountId())
            .build();
    ManageDataOperation operation2 =
        ManageDataOperation.builder()
            .name("key")
            .value("value".getBytes())
            .sourceAccount(server.getAccountId())
            .build();
    Operation[] operations = new Operation[] {operation1, operation2};
    Transaction transaction =
        new TransactionBuilder(sourceAccount, network)
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
      testVerifyChallengeTransactionInvalidAdditionalManageDataOpsWithoutSourceAccountSetToServerAccount() {
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
    byte[] encodedNonce = java.util.Base64.getEncoder().encode(nonce);

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation operation1 =
        ManageDataOperation.builder()
            .name(domainName + " auth")
            .value(encodedNonce)
            .sourceAccount(masterClient.getAccountId())
            .build();
    ManageDataOperation operation2 =
        ManageDataOperation.builder()
            .name("key")
            .value("value".getBytes())
            .sourceAccount(masterClient.getAccountId())
            .build();
    Operation[] operations = new Operation[] {operation1, operation2};
    Transaction transaction =
        new TransactionBuilder(sourceAccount, network)
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
      testVerifyChallengeTransactionInvalidAdditionalManageDataOpsWithSourceAccountSetToNull() {
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
    byte[] encodedNonce = java.util.Base64.getEncoder().encode(nonce);

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation operation1 =
        ManageDataOperation.builder()
            .name(domainName + " auth")
            .value(encodedNonce)
            .sourceAccount(masterClient.getAccountId())
            .build();
    ManageDataOperation operation2 =
        ManageDataOperation.builder().name("key").value("value".getBytes()).build();
    Operation[] operations = new Operation[] {operation1, operation2};
    Transaction transaction =
        new TransactionBuilder(sourceAccount, network)
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
  public void testVerifyChallengeTransactionInvalidAdditionalOpsOfOtherTypes() {
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
    byte[] encodedNonce = java.util.Base64.getEncoder().encode(nonce);

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation operation1 =
        ManageDataOperation.builder()
            .name(domainName + " auth")
            .value(encodedNonce)
            .sourceAccount(masterClient.getAccountId())
            .build();
    BumpSequenceOperation operation2 =
        BumpSequenceOperation.builder().bumpTo(0L).sourceAccount(server.getAccountId()).build();
    Operation[] operations = new Operation[] {operation1, operation2};
    Transaction transaction =
        new TransactionBuilder(sourceAccount, network)
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
