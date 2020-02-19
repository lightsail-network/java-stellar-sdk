package org.stellar.sdk;

import com.google.common.io.BaseEncoding;
import org.junit.Test;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class Sep10ChallengeTest {

  @Test
  public void testChallenge() throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    String challenge = Sep10Challenge.newChallenge(
            server,
            Network.TESTNET,
            client.getAccountId(),
            "angkor wat",
            timeBounds
    );

    Transaction transaction = Transaction.fromEnvelopeXdr(challenge, Network.TESTNET);

    assertEquals(Network.TESTNET, transaction.getNetwork());
    assertEquals(100, transaction.getFee());
    assertEquals(timeBounds, transaction.getTimeBounds());
    assertEquals(server.getAccountId(), transaction.getSourceAccount());
    assertEquals(0, transaction.getSequenceNumber());

    assertEquals(1, transaction.getOperations().length);
    ManageDataOperation op = (ManageDataOperation) transaction.getOperations()[0];
    assertEquals(client.getAccountId(), op.getSourceAccount());
    assertEquals("angkor wat auth", op.getName());

    assertEquals(64, op.getValue().length);
    BaseEncoding base64Encoding = BaseEncoding.base64();
    assertTrue(base64Encoding.canDecode(new String(op.getValue())));

    assertEquals(1, transaction.getSignatures().size());
    assertTrue(
            server.verify(transaction.hash(), transaction.getSignatures().get(0).getSignature().getSignature())
    );
  }

  @Test
  public void testReadChallengeTransactionValidSignedByServer() throws InvalidSep10ChallengeException, IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    String challenge = Sep10Challenge.newChallenge(
            server,
            network,
            client.getAccountId(),
            "Stellar Test",
            timeBounds
    );

    Sep10Challenge.ChallengeTransaction challengeTransaction = Sep10Challenge.readChallengeTransaction(challenge, server.getAccountId(), Network.TESTNET);
    assertEquals(new Sep10Challenge.ChallengeTransaction(Transaction.fromEnvelopeXdr(challenge, Network.TESTNET), client.getAccountId()), challengeTransaction);
  }

  @Test
  public void testReadChallengeTransactionValidSignedByServerAndClient() throws InvalidSep10ChallengeException, IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    String challenge = Sep10Challenge.newChallenge(
            server,
            network,
            client.getAccountId(),
            "Stellar Test",
            timeBounds
    );

    Transaction transaction = Transaction.fromEnvelopeXdr(challenge, network);
    transaction.sign(client);

    Sep10Challenge.ChallengeTransaction challengeTransaction = Sep10Challenge.readChallengeTransaction(transaction.toEnvelopeXdrBase64(), server.getAccountId(), Network.TESTNET);
    assertEquals(new Sep10Challenge.ChallengeTransaction(Transaction.fromEnvelopeXdr(transaction.toEnvelopeXdrBase64(), Network.TESTNET), client.getAccountId()), challengeTransaction);
  }

  @Test
  public void testReadChallengeTransactionInvalidNotSignedByServer() throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    String anchorName = "Stellar Test";
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
    ManageDataOperation manageDataOperation1 = new ManageDataOperation.Builder(anchorName + " auth", encodedNonce)
            .setSourceAccount(client.getAccountId())
            .build();

    Operation[] operations = new Operation[]{manageDataOperation1};
    Transaction transaction = new Transaction(
            sourceAccount.getAccountId(),
            100 * operations.length,
            sourceAccount.getIncrementedSequenceNumber(),
            operations,
            Memo.none(),
            timeBounds,
            network
    );

    transaction.sign(client);
    try {
      Sep10Challenge.readChallengeTransaction(transaction.toEnvelopeXdrBase64(), server.getAccountId(), network);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals(String.format("Transaction not signed by server: %s.", server.getAccountId()), e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionInvalidServerAccountIDMismatch() throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    String challenge = Sep10Challenge.newChallenge(
            server,
            Network.TESTNET,
            client.getAccountId(),
            "Stellar Test",
            timeBounds
    );

    // assertThrows requires Java 8+
    try {
      Sep10Challenge.readChallengeTransaction(challenge, KeyPair.random().getAccountId(), Network.TESTNET);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Transaction source account is not equal to server's account.", e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionInvalidSeqNoNotZero() throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    String anchorName = "Stellar Test";
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
    ManageDataOperation manageDataOperation1 = new ManageDataOperation.Builder(anchorName + " auth", encodedNonce)
            .setSourceAccount(client.getAccountId())
            .build();

    Operation[] operations = new Operation[]{manageDataOperation1};
    Transaction transaction = new Transaction(
            sourceAccount.getAccountId(),
            100 * operations.length,
            sourceAccount.getIncrementedSequenceNumber(),
            operations,
            Memo.none(),
            timeBounds,
            network
    );
    transaction.sign(server);
    String challenge = transaction.toEnvelopeXdrBase64();

    try {
      Sep10Challenge.readChallengeTransaction(challenge, server.getAccountId(), Network.TESTNET);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("The transaction sequence number should be zero.", e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionInvalidTimeboundsInfinite() throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    String anchorName = "Stellar Test";
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
    ManageDataOperation operation = new ManageDataOperation.Builder(anchorName + " auth", encodedNonce)
            .setSourceAccount(client.getAccountId())
            .build();
    Operation[] operations = new Operation[]{operation};
    Transaction transaction = new Transaction(
            sourceAccount.getAccountId(),
            100 * operations.length,
            sourceAccount.getIncrementedSequenceNumber(),
            operations,
            Memo.none(),
            timeBounds,
            network
    );
    transaction.sign(server);
    String challenge = transaction.toEnvelopeXdrBase64();

    try {
      Sep10Challenge.readChallengeTransaction(challenge, server.getAccountId(), Network.TESTNET);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Transaction requires non-infinite timebounds.", e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionInvalidNoTimeBounds() throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    String anchorName = "Stellar Test";
    Network network = Network.TESTNET;

    byte[] nonce = new byte[48];
    SecureRandom random = new SecureRandom();
    random.nextBytes(nonce);
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] encodedNonce = base64Encoding.encode(nonce).getBytes();

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation operation = new ManageDataOperation.Builder(anchorName + " auth", encodedNonce)
            .setSourceAccount(client.getAccountId())
            .build();
    Operation[] operations = new Operation[]{operation};
    Transaction transaction = new Transaction(
            sourceAccount.getAccountId(),
            100 * operations.length,
            sourceAccount.getIncrementedSequenceNumber(),
            operations,
            Memo.none(),
            null,
            network
    );
    transaction.sign(server);
    String challenge = transaction.toEnvelopeXdrBase64();

    try {
      Sep10Challenge.readChallengeTransaction(challenge, server.getAccountId(), Network.TESTNET);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Transaction requires timebounds.", e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionInvalidTimeBoundsTooEarly() throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();

    long current = System.currentTimeMillis() / 1000L;
    long start = current + 300;
    long end = current + 600;
    TimeBounds timeBounds = new TimeBounds(start, end);

    String challenge = Sep10Challenge.newChallenge(
            server,
            Network.TESTNET,
            client.getAccountId(),
            "Stellar Test",
            timeBounds
    );

    try {
      Sep10Challenge.readChallengeTransaction(challenge, server.getAccountId(), Network.TESTNET);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Transaction is not within range of the specified timebounds.", e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionInvalidTimeBoundsTooLate() throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();

    long current = System.currentTimeMillis() / 1000L;
    long start = current - 600;
    long end = current - 300;
    TimeBounds timeBounds = new TimeBounds(start, end);

    String challenge = Sep10Challenge.newChallenge(
            server,
            Network.TESTNET,
            client.getAccountId(),
            "Stellar Test",
            timeBounds
    );

    try {
      Sep10Challenge.readChallengeTransaction(challenge, server.getAccountId(), Network.TESTNET);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Transaction is not within range of the specified timebounds.", e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionInvalidTooManyOperations() throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    String anchorName = "Stellar Test";
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
    ManageDataOperation manageDataOperation1 = new ManageDataOperation.Builder(anchorName + " auth", encodedNonce)
            .setSourceAccount(client.getAccountId())
            .build();

    ManageDataOperation manageDataOperation2 = new ManageDataOperation.Builder(anchorName + " auth", encodedNonce)
            .setSourceAccount(client.getAccountId())
            .build();

    Operation[] operations = new Operation[]{manageDataOperation1, manageDataOperation2};
    Transaction transaction = new Transaction(
            sourceAccount.getAccountId(),
            100 * operations.length,
            sourceAccount.getIncrementedSequenceNumber(),
            operations,
            Memo.none(),
            timeBounds,
            network
    );
    transaction.sign(server);
    String challenge = transaction.toEnvelopeXdrBase64();

    try {
      Sep10Challenge.readChallengeTransaction(challenge, server.getAccountId(), Network.TESTNET);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Transaction requires a single ManageData operation.", e.getMessage());
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

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    SetOptionsOperation setOptionsOperation = new SetOptionsOperation.Builder()
            .setSourceAccount(client.getAccountId())
            .build();

    Operation[] operations = new Operation[]{setOptionsOperation};
    Transaction transaction = new Transaction(
            sourceAccount.getAccountId(),
            100 * operations.length,
            sourceAccount.getIncrementedSequenceNumber(),
            operations,
            Memo.none(),
            timeBounds,
            network
    );
    transaction.sign(server);
    String challenge = transaction.toEnvelopeXdrBase64();

    try {
      Sep10Challenge.readChallengeTransaction(challenge, server.getAccountId(), Network.TESTNET);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Operation type should be ManageData.", e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionInvalidOperationNoSourceAccount() throws IOException {
    KeyPair server = KeyPair.random();
    String anchorName = "Stellar Test";
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
    ManageDataOperation manageDataOperation1 = new ManageDataOperation.Builder(anchorName + " auth", encodedNonce)
            .build();

    Operation[] operations = new Operation[]{manageDataOperation1};
    Transaction transaction = new Transaction(
            sourceAccount.getAccountId(),
            100 * operations.length,
            sourceAccount.getIncrementedSequenceNumber(),
            operations,
            Memo.none(),
            timeBounds,
            network
    );
    transaction.sign(server);
    String challenge = transaction.toEnvelopeXdrBase64();

    try {
      Sep10Challenge.readChallengeTransaction(challenge, server.getAccountId(), Network.TESTNET);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Operation should have a source account.", e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionInvalidDataValueWrongEncodedLength() throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    String anchorName = "Stellar Test";
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
    ManageDataOperation manageDataOperation1 = new ManageDataOperation.Builder(anchorName + " auth", encodedNonce)
            .setSourceAccount(client.getAccountId())
            .build();

    Operation[] operations = new Operation[]{manageDataOperation1};
    Transaction transaction = new Transaction(
            sourceAccount.getAccountId(),
            100 * operations.length,
            sourceAccount.getIncrementedSequenceNumber(),
            operations,
            Memo.none(),
            timeBounds,
            network
    );
    transaction.sign(server);
    String challenge = transaction.toEnvelopeXdrBase64();

    try {
      Sep10Challenge.readChallengeTransaction(challenge, server.getAccountId(), Network.TESTNET);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Random nonce encoded as base64 should be 64 bytes long.", e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionInvalidDataValueWrongByteLength() throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair client = KeyPair.random();
    String anchorName = "Stellar Test";
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
    ManageDataOperation manageDataOperation1 = new ManageDataOperation.Builder(anchorName + " auth", encodedNonce)
            .setSourceAccount(client.getAccountId())
            .build();

    Operation[] operations = new Operation[]{manageDataOperation1};
    Transaction transaction = new Transaction(
            sourceAccount.getAccountId(),
            100 * operations.length,
            sourceAccount.getIncrementedSequenceNumber(),
            operations,
            Memo.none(),
            timeBounds,
            network
    );
    transaction.sign(server);
    String challenge = transaction.toEnvelopeXdrBase64();

    try {
      Sep10Challenge.readChallengeTransaction(challenge, server.getAccountId(), Network.TESTNET);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Random nonce before encoding as base64 should be 48 bytes long.", e.getMessage());
    }
  }

  @Test
  public void testVerifyChallengeTransactionThresholdInvalidNotSignedByServer() throws IOException {
    Network network = Network.TESTNET;
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    KeyPair signerClient2 = KeyPair.random();
    String anchorName = "Stellar Test";

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    byte[] nonce = new byte[48];
    SecureRandom random = new SecureRandom();
    random.nextBytes(nonce);
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] encodedNonce = base64Encoding.encode(nonce).getBytes();

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation manageDataOperation1 = new ManageDataOperation.Builder(anchorName + " auth", encodedNonce)
            .setSourceAccount(masterClient.getAccountId())
            .build();

    Operation[] operations = new Operation[]{manageDataOperation1};
    Transaction transaction = new Transaction(
            sourceAccount.getAccountId(),
            100 * operations.length,
            sourceAccount.getIncrementedSequenceNumber(),
            operations,
            Memo.none(),
            timeBounds,
            network
    );

    transaction.sign(masterClient);

    Set<Sep10Challenge.Signer> signers = new HashSet<Sep10Challenge.Signer>(Arrays.asList(
            new Sep10Challenge.Signer(masterClient.getAccountId(), 1),
            new Sep10Challenge.Signer(signerClient1.getAccountId(), 2),
            new Sep10Challenge.Signer(signerClient2.getAccountId(), 4)
    ));

    int threshold = 6;
    try {
      Sep10Challenge.verifyChallengeTransactionThreshold(transaction.toEnvelopeXdrBase64(), server.getAccountId(), network, threshold, signers);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals(String.format("Transaction not signed by server: %s.", server.getAccountId()), e.getMessage());
    }

  }

  @Test
  public void testVerifyChallengeTransactionThresholdValidServerAndClientKeyMeetingThreshold() throws IOException, InvalidSep10ChallengeException {
    Network network = Network.TESTNET;
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    String challenge = Sep10Challenge.newChallenge(
            server,
            network,
            masterClient.getAccountId(),
            "Stellar Test",
            timeBounds
    );

    Transaction transaction = Transaction.fromEnvelopeXdr(challenge, Network.TESTNET);
    transaction.sign(masterClient);

    Set<Sep10Challenge.Signer> signers = new HashSet<Sep10Challenge.Signer>(Collections.singletonList(
            new Sep10Challenge.Signer(masterClient.getAccountId(), 255)
    ));

    int threshold = 255;
    Set<String> signersFound = Sep10Challenge.verifyChallengeTransactionThreshold(transaction.toEnvelopeXdrBase64(), server.getAccountId(), network, threshold, signers);
    assertEquals(new HashSet<String>(Collections.singletonList(masterClient.getAccountId())), signersFound);
  }

  @Test
  public void testVerifyChallengeTransactionThresholdValidServerAndMultipleClientKeyMeetingThreshold() throws IOException, InvalidSep10ChallengeException {
    Network network = Network.TESTNET;
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    KeyPair signerClient2 = KeyPair.random();
    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    String challenge = Sep10Challenge.newChallenge(
            server,
            network,
            masterClient.getAccountId(),
            "Stellar Test",
            timeBounds
    );

    Transaction transaction = Transaction.fromEnvelopeXdr(challenge, Network.TESTNET);
    transaction.sign(masterClient);
    transaction.sign(signerClient1);
    transaction.sign(signerClient2);

    Set<Sep10Challenge.Signer> signers = new HashSet<Sep10Challenge.Signer>(Arrays.asList(
            new Sep10Challenge.Signer(masterClient.getAccountId(), 1),
            new Sep10Challenge.Signer(signerClient1.getAccountId(), 2),
            new Sep10Challenge.Signer(signerClient2.getAccountId(), 4)
    ));

    int threshold = 7;
    Set<String> signersFound = Sep10Challenge.verifyChallengeTransactionThreshold(transaction.toEnvelopeXdrBase64(), server.getAccountId(), network, threshold, signers);
    assertEquals(new HashSet<String>(Arrays.asList(masterClient.getAccountId(), signerClient1.getAccountId(), signerClient2.getAccountId())), signersFound);
  }

  @Test
  public void testVerifyChallengeTransactionThresholdValidServerAndMultipleClientKeyMeetingThresholdSomeUnused() throws IOException, InvalidSep10ChallengeException {
    Network network = Network.TESTNET;
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    KeyPair signerClient2 = KeyPair.random();
    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    String challenge = Sep10Challenge.newChallenge(
            server,
            network,
            masterClient.getAccountId(),
            "Stellar Test",
            timeBounds
    );

    Transaction transaction = Transaction.fromEnvelopeXdr(challenge, Network.TESTNET);
    transaction.sign(masterClient);
    transaction.sign(signerClient1);

    Set<Sep10Challenge.Signer> signers = new HashSet<Sep10Challenge.Signer>(Arrays.asList(
            new Sep10Challenge.Signer(masterClient.getAccountId(), 1),
            new Sep10Challenge.Signer(signerClient1.getAccountId(), 2),
            new Sep10Challenge.Signer(signerClient2.getAccountId(), 4)
    ));

    int threshold = 3;
    Set<String> signersFound = Sep10Challenge.verifyChallengeTransactionThreshold(transaction.toEnvelopeXdrBase64(), server.getAccountId(), network, threshold, signers);
    assertEquals(new HashSet<String>(Arrays.asList(masterClient.getAccountId(), signerClient1.getAccountId())), signersFound);
  }

  @Test
  public void testVerifyChallengeTransactionThresholdValidServerAndMultipleClientKeyMeetingThresholdSomeUnusedIgnorePreauthTxHashAndXHash() throws IOException, InvalidSep10ChallengeException {
    Network network = Network.TESTNET;
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    KeyPair signerClient2 = KeyPair.random();

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    String challenge = Sep10Challenge.newChallenge(
            server,
            network,
            masterClient.getAccountId(),
            "Stellar Test",
            timeBounds
    );

    Transaction transaction = Transaction.fromEnvelopeXdr(challenge, Network.TESTNET);
    transaction.sign(masterClient);
    transaction.sign(signerClient1);

    String preauthTxHash = "TAQCSRX2RIDJNHFIFHWD63X7D7D6TRT5Y2S6E3TEMXTG5W3OECHZ2OG4";
    String xHash = "XDRPF6NZRR7EEVO7ESIWUDXHAOMM2QSKIQQBJK6I2FB7YKDZES5UCLWD";
    String unknownSignerType = "?ARPF6NZRR7EEVO7ESIWUDXHAOMM2QSKIQQBJK6I2FB7YKDZES5UCLWD";
    Set<Sep10Challenge.Signer> signers = new HashSet<Sep10Challenge.Signer>(Arrays.asList(
            new Sep10Challenge.Signer(masterClient.getAccountId(), 1),
            new Sep10Challenge.Signer(signerClient1.getAccountId(), 2),
            new Sep10Challenge.Signer(signerClient2.getAccountId(), 4),
            new Sep10Challenge.Signer(preauthTxHash, 10),
            new Sep10Challenge.Signer(xHash, 10),
            new Sep10Challenge.Signer(unknownSignerType, 10)
    ));

    int threshold = 3;
    Set<String> signersFound = Sep10Challenge.verifyChallengeTransactionThreshold(transaction.toEnvelopeXdrBase64(), server.getAccountId(), network, threshold, signers);
    assertEquals(new HashSet<String>(Arrays.asList(masterClient.getAccountId(), signerClient1.getAccountId())), signersFound);
  }

  @Test
  public void testVerifyChallengeTransactionThresholdInvalidServerAndMultipleClientKeyNotMeetingThreshold() throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    KeyPair signerClient2 = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    String challenge = Sep10Challenge.newChallenge(
            server,
            network,
            masterClient.getAccountId(),
            "Stellar Test",
            timeBounds
    );

    Transaction transaction = Transaction.fromEnvelopeXdr(challenge, Network.TESTNET);
    transaction.sign(masterClient);
    transaction.sign(signerClient1);

    Set<Sep10Challenge.Signer> signers = new HashSet<Sep10Challenge.Signer>(Arrays.asList(
            new Sep10Challenge.Signer(masterClient.getAccountId(), 1),
            new Sep10Challenge.Signer(signerClient1.getAccountId(), 2),
            new Sep10Challenge.Signer(signerClient2.getAccountId(), 4)
    ));

    int threshold = 7;
    try {
      Sep10Challenge.verifyChallengeTransactionThreshold(transaction.toEnvelopeXdrBase64(), server.getAccountId(), network, threshold, signers);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Signers with weight 3 do not meet threshold 7.", e.getMessage());
    }
  }

  @Test
  public void testVerifyChallengeTransactionThresholdInvalidClientKeyUnrecognized() throws IOException {
    Network network = Network.TESTNET;
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    KeyPair signerClient2 = KeyPair.random();

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    String challenge = Sep10Challenge.newChallenge(
            server,
            network,
            masterClient.getAccountId(),
            "Stellar Test",
            timeBounds
    );

    Transaction transaction = Transaction.fromEnvelopeXdr(challenge, Network.TESTNET);
    transaction.sign(masterClient);
    transaction.sign(signerClient1);
    transaction.sign(signerClient2);
    transaction.sign(KeyPair.random());

    Set<Sep10Challenge.Signer> signers = new HashSet<Sep10Challenge.Signer>(Arrays.asList(
            new Sep10Challenge.Signer(masterClient.getAccountId(), 1),
            new Sep10Challenge.Signer(signerClient1.getAccountId(), 2),
            new Sep10Challenge.Signer(signerClient2.getAccountId(), 4)
    ));

    int threshold = 7;
    try {
      Sep10Challenge.verifyChallengeTransactionThreshold(transaction.toEnvelopeXdrBase64(), server.getAccountId(), network, threshold, signers);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Transaction has unrecognized signatures.", e.getMessage());
    }
  }

  @Test
  public void testVerifyChallengeTransactionThresholdInvalidNoSigners() throws IOException {
    Network network = Network.TESTNET;
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    KeyPair signerClient2 = KeyPair.random();

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    String challenge = Sep10Challenge.newChallenge(
            server,
            network,
            masterClient.getAccountId(),
            "Stellar Test",
            timeBounds
    );

    Transaction transaction = Transaction.fromEnvelopeXdr(challenge, Network.TESTNET);
    transaction.sign(masterClient);
    transaction.sign(signerClient1);
    transaction.sign(signerClient2);

    Set<Sep10Challenge.Signer> signers = Collections.emptySet();
    int threshold = 3;
    try {
      Sep10Challenge.verifyChallengeTransactionThreshold(transaction.toEnvelopeXdrBase64(), server.getAccountId(), network, threshold, signers);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("No verifiable signers provided, at least one G... address must be provided.", e.getMessage());
    }
  }

  @Test
  public void testVerifyChallengeTransactionThresholdInvalidNoPublicKeySigners() throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    KeyPair signerClient2 = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    String challenge = Sep10Challenge.newChallenge(
            server,
            network,
            masterClient.getAccountId(),
            "Stellar Test",
            timeBounds
    );

    Transaction transaction = Transaction.fromEnvelopeXdr(challenge, Network.TESTNET);
    transaction.sign(masterClient);
    transaction.sign(signerClient1);
    transaction.sign(signerClient2);

    String preauthTxHash = "TAQCSRX2RIDJNHFIFHWD63X7D7D6TRT5Y2S6E3TEMXTG5W3OECHZ2OG4";
    String xHash = "XDRPF6NZRR7EEVO7ESIWUDXHAOMM2QSKIQQBJK6I2FB7YKDZES5UCLWD";
    String unknownSignerType = "?ARPF6NZRR7EEVO7ESIWUDXHAOMM2QSKIQQBJK6I2FB7YKDZES5UCLWD";
    Set<Sep10Challenge.Signer> signers = new HashSet<Sep10Challenge.Signer>(Arrays.asList(
            new Sep10Challenge.Signer(preauthTxHash, 1),
            new Sep10Challenge.Signer(xHash, 2),
            new Sep10Challenge.Signer(unknownSignerType, 2)
    ));
    int threshold = 3;

    try {
      Sep10Challenge.verifyChallengeTransactionThreshold(transaction.toEnvelopeXdrBase64(), server.getAccountId(), network, threshold, signers);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("No verifiable signers provided, at least one G... address must be provided.", e.getMessage());
    }
  }

  @Test
  public void testVerifyChallengeTransactionThresholdWeightsAddToMoreThan8Bits() throws IOException, InvalidSep10ChallengeException {
    Network network = Network.TESTNET;
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    String challenge = Sep10Challenge.newChallenge(
            server,
            network,
            masterClient.getAccountId(),
            "Stellar Test",
            timeBounds
    );

    Transaction transaction = Transaction.fromEnvelopeXdr(challenge, Network.TESTNET);
    transaction.sign(masterClient);
    transaction.sign(signerClient1);

    Set<Sep10Challenge.Signer> signers = new HashSet<Sep10Challenge.Signer>(Arrays.asList(
            new Sep10Challenge.Signer(masterClient.getAccountId(), 255),
            new Sep10Challenge.Signer(signerClient1.getAccountId(), 1)
    ));

    int threshold = 1;
    Set<String> signersFound = Sep10Challenge.verifyChallengeTransactionThreshold(transaction.toEnvelopeXdrBase64(), server.getAccountId(), network, threshold, signers);
    assertEquals(new HashSet<String>(Arrays.asList(masterClient.getAccountId(), signerClient1.getAccountId())), signersFound);
  }

  @Test
  public void testVerifyChallengeTransactionSignersInvalidServer() throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    KeyPair signerClient2 = KeyPair.random();
    String anchorName = "Stellar Test";
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
    ManageDataOperation manageDataOperation1 = new ManageDataOperation.Builder(anchorName + " auth", encodedNonce)
            .setSourceAccount(masterClient.getAccountId())
            .build();

    Operation[] operations = new Operation[]{manageDataOperation1};
    Transaction transaction = new Transaction(
            sourceAccount.getAccountId(),
            100 * operations.length,
            sourceAccount.getIncrementedSequenceNumber(),
            operations,
            Memo.none(),
            timeBounds,
            network
    );

    transaction.sign(masterClient);
    transaction.sign(signerClient1);
    transaction.sign(signerClient2);

    Set<String> signers = new HashSet<String>(Arrays.asList(masterClient.getAccountId(), signerClient1.getAccountId(), signerClient2.getAccountId(), KeyPair.random().getAccountId()));
    try {
      Sep10Challenge.verifyChallengeTransactionSigners(transaction.toEnvelopeXdrBase64(), server.getAccountId(), network, signers);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals(String.format("Transaction not signed by server: %s.", server.getAccountId()), e.getMessage());
    }
  }

  @Test
  public void testVerifyChallengeTransactionSignersValidServerAndClientMasterKey() throws InvalidSep10ChallengeException, IOException {
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    String challenge = Sep10Challenge.newChallenge(
            server,
            network,
            masterClient.getAccountId(),
            "Stellar Test",
            timeBounds
    );

    Transaction transaction = Transaction.fromEnvelopeXdr(challenge, network);
    transaction.sign(masterClient);

    Set<String> signers = new HashSet<String>(Collections.singletonList(masterClient.getAccountId()));
    Set<String> signersFound = Sep10Challenge.verifyChallengeTransactionSigners(transaction.toEnvelopeXdrBase64(), server.getAccountId(), network, signers);
    assertEquals(signers, signersFound);
  }

  @Test
  public void testVerifyChallengeTransactionSignersInvalidServerAndNoClient() throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    KeyPair signerClient2 = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    String challenge = Sep10Challenge.newChallenge(
            server,
            network,
            masterClient.getAccountId(),
            "Stellar Test",
            timeBounds
    );

    Transaction transaction = Transaction.fromEnvelopeXdr(challenge, network);
    Set<String> signers = new HashSet<String>(Arrays.asList(masterClient.getAccountId(), signerClient1.getAccountId(), signerClient2.getAccountId(), KeyPair.random().getAccountId()));
    try {
      Sep10Challenge.verifyChallengeTransactionSigners(transaction.toEnvelopeXdrBase64(), server.getAccountId(), network, signers);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Transaction not signed by any client signer.", e.getMessage());
    }
  }

  @Test
  public void testVerifyChallengeTransactionSignersInvalidServerAndClientKeyUnrecognized() throws IOException {
    Network network = Network.TESTNET;
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    KeyPair signerClient2 = KeyPair.random();

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    String challenge = Sep10Challenge.newChallenge(
            server,
            network,
            masterClient.getAccountId(),
            "Stellar Test",
            timeBounds
    );

    Transaction transaction = Transaction.fromEnvelopeXdr(challenge, Network.TESTNET);
    transaction.sign(masterClient);
    transaction.sign(signerClient1);
    transaction.sign(signerClient2);
    transaction.sign(KeyPair.random());

    Set<String> signers = new HashSet<String>(Arrays.asList(masterClient.getAccountId(), signerClient1.getAccountId(), signerClient2.getAccountId(), KeyPair.random().getAccountId()));
    try {
      Sep10Challenge.verifyChallengeTransactionSigners(transaction.toEnvelopeXdrBase64(), server.getAccountId(), network, signers);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Transaction has unrecognized signatures.", e.getMessage());
    }
  }

  @Test
  public void testVerifyChallengeTransactionSignersValidServerAndMultipleClientSigners() throws InvalidSep10ChallengeException, IOException {
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    KeyPair signerClient2 = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    String challenge = Sep10Challenge.newChallenge(
            server,
            network,
            masterClient.getAccountId(),
            "Stellar Test",
            timeBounds
    );

    Transaction transaction = Transaction.fromEnvelopeXdr(challenge, network);
    transaction.sign(signerClient1);
    transaction.sign(signerClient2);

    Set<String> signers = new HashSet<String>(Arrays.asList(masterClient.getAccountId(), signerClient1.getAccountId(), signerClient2.getAccountId(), KeyPair.random().getAccountId()));
    Set<String> signersFound = Sep10Challenge.verifyChallengeTransactionSigners(transaction.toEnvelopeXdrBase64(), server.getAccountId(), network, signers);
    assertEquals(new HashSet<String>(Arrays.asList(signerClient1.getAccountId(), signerClient2.getAccountId())), signersFound);
  }

  @Test
  public void testVerifyChallengeTransactionSignersValidServerAndMultipleClientSignersReverseOrder() throws InvalidSep10ChallengeException, IOException {
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    KeyPair signerClient2 = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    String challenge = Sep10Challenge.newChallenge(
            server,
            network,
            masterClient.getAccountId(),
            "Stellar Test",
            timeBounds
    );

    Transaction transaction = Transaction.fromEnvelopeXdr(challenge, network);
    transaction.sign(signerClient2);
    transaction.sign(signerClient1);

    Set<String> signers = new HashSet<String>(Arrays.asList(masterClient.getAccountId(), signerClient1.getAccountId(), signerClient2.getAccountId(), KeyPair.random().getAccountId()));
    Set<String> signersFound = Sep10Challenge.verifyChallengeTransactionSigners(transaction.toEnvelopeXdrBase64(), server.getAccountId(), network, signers);
    assertEquals(new HashSet<String>(Arrays.asList(signerClient1.getAccountId(), signerClient2.getAccountId())), signersFound);
  }


  @Test
  public void testVerifyChallengeTransactionSignersValidServerAndClientSignersNotMasterKey() throws InvalidSep10ChallengeException, IOException {
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    String challenge = Sep10Challenge.newChallenge(
            server,
            network,
            masterClient.getAccountId(),
            "Stellar Test",
            timeBounds
    );

    Transaction transaction = Transaction.fromEnvelopeXdr(challenge, network);
    transaction.sign(signerClient1);

    Set<String> signers = new HashSet<String>(Arrays.asList(masterClient.getAccountId(), signerClient1.getAccountId()));
    Set<String> signersFound = Sep10Challenge.verifyChallengeTransactionSigners(transaction.toEnvelopeXdrBase64(), server.getAccountId(), network, signers);
    assertEquals(new HashSet<String>(Collections.singletonList(signerClient1.getAccountId())), signersFound);
  }

  @Test
  public void testVerifyChallengeTransactionSignersValidServerAndClientSignersIgnoresServerSigner() throws InvalidSep10ChallengeException, IOException {
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    String challenge = Sep10Challenge.newChallenge(
            server,
            network,
            masterClient.getAccountId(),
            "Stellar Test",
            timeBounds
    );

    Transaction transaction = Transaction.fromEnvelopeXdr(challenge, network);
    transaction.sign(signerClient1);

    Set<String> signers = new HashSet<String>(Arrays.asList(masterClient.getAccountId(), signerClient1.getAccountId(), server.getAccountId()));
    Set<String> signersFound = Sep10Challenge.verifyChallengeTransactionSigners(transaction.toEnvelopeXdrBase64(), server.getAccountId(), network, signers);
    assertEquals(new HashSet<String>(Collections.singletonList(signerClient1.getAccountId())), signersFound);
  }


  @Test
  public void testVerifyChallengeTransactionSignersInvalidServerNoClientSignersIgnoresServerSigner() throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    String challenge = Sep10Challenge.newChallenge(
            server,
            network,
            masterClient.getAccountId(),
            "Stellar Test",
            timeBounds
    );

    Set<String> signers = new HashSet<String>(Arrays.asList(masterClient.getAccountId(), signerClient1.getAccountId(), server.getAccountId()));
    try {
      Sep10Challenge.verifyChallengeTransactionSigners(challenge, server.getAccountId(), network, signers);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Transaction not signed by any client signer.", e.getMessage());
    }
  }


  @Test
  public void testVerifyChallengeTransactionSignersValidIgnorePreauthTxHashAndXHash() throws InvalidSep10ChallengeException, IOException {
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    String challenge = Sep10Challenge.newChallenge(
            server,
            network,
            masterClient.getAccountId(),
            "Stellar Test",
            timeBounds
    );

    Transaction transaction = Transaction.fromEnvelopeXdr(challenge, network);
    transaction.sign(signerClient1);

    String preauthTxHash = "TAQCSRX2RIDJNHFIFHWD63X7D7D6TRT5Y2S6E3TEMXTG5W3OECHZ2OG4";
    String xHash = "XDRPF6NZRR7EEVO7ESIWUDXHAOMM2QSKIQQBJK6I2FB7YKDZES5UCLWD";
    String unknownSignerType = "?ARPF6NZRR7EEVO7ESIWUDXHAOMM2QSKIQQBJK6I2FB7YKDZES5UCLWD";
    Set<String> signers = new HashSet<String>(Arrays.asList(masterClient.getAccountId(),
            signerClient1.getAccountId(), preauthTxHash, xHash, unknownSignerType));
    Set<String> signersFound = Sep10Challenge.verifyChallengeTransactionSigners(transaction.toEnvelopeXdrBase64(), server.getAccountId(), network, signers);
    assertEquals(new HashSet<String>(Collections.singletonList(signerClient1.getAccountId())), signersFound);
  }

  @Test
  public void testVerifyChallengeTransactionSignersInvalidServerAndClientSignersFailsDuplicateSignatures() throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    String challenge = Sep10Challenge.newChallenge(
            server,
            network,
            masterClient.getAccountId(),
            "Stellar Test",
            timeBounds
    );

    Transaction transaction = Transaction.fromEnvelopeXdr(challenge, network);
    transaction.sign(signerClient1);
    transaction.sign(signerClient1);

    Set<String> signers = new HashSet<String>(Arrays.asList(masterClient.getAccountId(), signerClient1.getAccountId()));
    try {
      Sep10Challenge.verifyChallengeTransactionSigners(transaction.toEnvelopeXdrBase64(), server.getAccountId(), network, signers);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Transaction has unrecognized signatures.", e.getMessage());
    }
  }

  @Test
  public void testVerifyChallengeTransactionSignersInvalidServerAndClientSignersFailsSignerSeed() throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    String challenge = Sep10Challenge.newChallenge(
            server,
            network,
            masterClient.getAccountId(),
            "Stellar Test",
            timeBounds
    );

    Transaction transaction = Transaction.fromEnvelopeXdr(challenge, network);
    transaction.sign(signerClient1);

    Set<String> signers = new HashSet<String>(Collections.singletonList(new String(signerClient1.getSecretSeed())));
    try {
      Sep10Challenge.verifyChallengeTransactionSigners(transaction.toEnvelopeXdrBase64(), server.getAccountId(), network, signers);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("No verifiable signers provided, at least one G... address must be provided.", e.getMessage());
    }
  }

  @Test
  public void testVerifyChallengeTransactionSignersInvalidNoSignersNull() throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    String challenge = Sep10Challenge.newChallenge(
            server,
            network,
            masterClient.getAccountId(),
            "Stellar Test",
            timeBounds
    );

    Transaction transaction = Transaction.fromEnvelopeXdr(challenge, network);
    transaction.sign(signerClient1);

    try {
      Sep10Challenge.verifyChallengeTransactionSigners(transaction.toEnvelopeXdrBase64(), server.getAccountId(), network, null);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("No verifiable signers provided, at least one G... address must be provided.", e.getMessage());
    }
  }

  @Test
  public void testVerifyChallengeTransactionSignersInvalidNoSignersEmptySet() throws IOException {
    KeyPair server = KeyPair.random();
    KeyPair masterClient = KeyPair.random();
    KeyPair signerClient1 = KeyPair.random();
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    String challenge = Sep10Challenge.newChallenge(
            server,
            network,
            masterClient.getAccountId(),
            "Stellar Test",
            timeBounds
    );

    Transaction transaction = Transaction.fromEnvelopeXdr(challenge, network);
    transaction.sign(signerClient1);

    try {
      Sep10Challenge.verifyChallengeTransactionSigners(transaction.toEnvelopeXdrBase64(), server.getAccountId(), network, Collections.<String>emptySet());
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("No verifiable signers provided, at least one G... address must be provided.", e.getMessage());
    }
  }
}
