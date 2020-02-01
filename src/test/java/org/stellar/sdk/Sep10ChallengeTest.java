package org.stellar.sdk;

import com.google.common.io.BaseEncoding;
import org.junit.Test;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class Sep10ChallengeTest {

  @Test
  public void testChallenge() throws IOException {
    KeyPair server = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    String client = "GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR";

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    String challenge = Sep10Challenge.newChallenge(
            server,
            Network.TESTNET,
            client,
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
    assertEquals(client, op.getSourceAccount());
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
  public void testReadChallengeTransaction() throws InvalidSep10ChallengeException, IOException {
    KeyPair server = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    String client = "GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR";

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    String challenge = Sep10Challenge.newChallenge(
            server,
            Network.TESTNET,
            client,
            "Stellar Test",
            timeBounds
    );

    Sep10Challenge.ChallengeTransaction challengeTransaction = Sep10Challenge.readChallengeTransaction(challenge, server.getAccountId(), Network.TESTNET);
    assertEquals(challengeTransaction, new Sep10Challenge.ChallengeTransaction(Transaction.fromEnvelopeXdr(challenge, Network.TESTNET), client));
  }

  @Test
  public void testReadChallengeTransactionTxSourceNotEqualServerAccountThrows() throws IOException {
    KeyPair server = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    String client = "GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR";

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    String challenge = Sep10Challenge.newChallenge(
            server,
            Network.TESTNET,
            client,
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
  public void testReadChallengeTransactionNoTimeBoundsThrows() throws IOException {
    KeyPair server = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    String client = "GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR";
    String anchorName = "Stellar Test";
    Network network = Network.TESTNET;

    byte[] nonce = new byte[48];
    SecureRandom random = new SecureRandom();
    random.nextBytes(nonce);
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] encodedNonce = base64Encoding.encode(nonce).getBytes();

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    ManageDataOperation operation = new ManageDataOperation.Builder(anchorName + " auth", encodedNonce)
            .setSourceAccount(client)
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
  public void testReadChallengeTransactionInfiniteTimeBoundsThrows() throws IOException {
    KeyPair server = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    String client = "GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR";
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
            .setSourceAccount(client)
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
  public void testReadChallengeTransactionTimeBoundsTooEarlyThrows() throws IOException {
    KeyPair server = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    String client = "GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR";

    long current = System.currentTimeMillis() / 1000L;
    long start = current + 300;
    long end = current + 600;
    TimeBounds timeBounds = new TimeBounds(start, end);

    String challenge = Sep10Challenge.newChallenge(
            server,
            Network.TESTNET,
            client,
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
  public void testReadChallengeTransactionTimeBoundsTooLateThrows() throws IOException {
    KeyPair server = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    String client = "GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR";

    long current = System.currentTimeMillis() / 1000L;
    long start = current - 600;
    long end = current - 300;
    TimeBounds timeBounds = new TimeBounds(start, end);

    String challenge = Sep10Challenge.newChallenge(
            server,
            Network.TESTNET,
            client,
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
  public void testReadChallengeTransactionNotSingleOperationThrows() throws IOException {
    KeyPair server = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    String client = "GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR";
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
            .setSourceAccount(client)
            .build();

    ManageDataOperation manageDataOperation2 = new ManageDataOperation.Builder(anchorName + " auth", encodedNonce)
            .setSourceAccount(client)
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
  public void testReadChallengeTransactionNotManageDataOperationThrows() throws IOException {
    KeyPair server = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    String client = "GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR";
    String anchorName = "Stellar Test";
    Network network = Network.TESTNET;

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    Account sourceAccount = new Account(server.getAccountId(), -1L);
    SetOptionsOperation setOptionsOperation = new SetOptionsOperation.Builder()
            .setSourceAccount(client)
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
  public void testReadChallengeTransactionOperationNoSourceThrows() throws IOException {
    KeyPair server = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    String client = "GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR";
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
  public void testReadChallengeTransactionOperationDataThrows() throws IOException {
    KeyPair server = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    String client = "GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR";
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
            .setSourceAccount(client)
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
  public void testReadChallengeTransactionNoSignatureThrows() throws IOException {
    KeyPair server = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    String client = "GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR";
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
            .setSourceAccount(client)
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
    String challenge = transaction.toEnvelopeXdrBase64();

    try {
      Sep10Challenge.readChallengeTransaction(challenge, server.getAccountId(), Network.TESTNET);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Transaction has no signatures.", e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionNoServerSignatureThrows() throws IOException {
    KeyPair server = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    String client = "GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR";
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
            .setSourceAccount(client)
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
    transaction.sign(KeyPair.random());
    String challenge = transaction.toEnvelopeXdrBase64();

    try {
      Sep10Challenge.readChallengeTransaction(challenge, server.getAccountId(), Network.TESTNET);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals(String.format("Transaction not signed by server: %s.", server.getAccountId()), e.getMessage());
    }
  }

  @Test
  public void testReadChallengeTransactionBadSequenceThrows() throws IOException {
    KeyPair server = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    String client = "GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR";
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
            .setSourceAccount(client)
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
  public void testVerifyChallengeTransactionSigners() throws IOException, InvalidSep10ChallengeException {
    Network network = Network.TESTNET;
    KeyPair server = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
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

    List<String> signers = Arrays.asList(masterClient.getAccountId(), signerClient1.getAccountId(), signerClient2.getAccountId(), KeyPair.random().getAccountId());
    List<String> signersFound = Sep10Challenge.verifyChallengeTransactionSigners(transaction.toEnvelopeXdrBase64(), server.getAccountId(), network, signers);
    assertEquals(signersFound, Arrays.asList(masterClient.getAccountId(), signerClient1.getAccountId(), signerClient2.getAccountId()));
  }

  @Test
  public void testVerifyChallengeTransactionSignersNoSignerThrows() throws IOException {
    Network network = Network.TESTNET;
    KeyPair server = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
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

    List<String> signers = Collections.emptyList();
    try {
      Sep10Challenge.verifyChallengeTransactionSigners(transaction.toEnvelopeXdrBase64(), server.getAccountId(), network, signers);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("No signers provided.", e.getMessage());
    }
  }

  @Test
  public void testVerifyChallengeTransactionSignersNoServerSignatureThrows() throws IOException {
    KeyPair server = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
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

    List<String> signers = Arrays.asList(masterClient.getAccountId(), signerClient1.getAccountId(), signerClient2.getAccountId(), KeyPair.random().getAccountId());
    try {
      Sep10Challenge.verifyChallengeTransactionSigners(transaction.toEnvelopeXdrBase64(), server.getAccountId(), network, signers);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals(String.format("Transaction not signed by server: %s.", server.getAccountId()), e.getMessage());
    }
  }

  @Test
  public void testVerifyChallengeTransactionSignersUnrecognizedSignaturesThrows() throws IOException {
    Network network = Network.TESTNET;
    KeyPair server = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
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

    List<String> signers = Arrays.asList(masterClient.getAccountId(), signerClient1.getAccountId(), signerClient2.getAccountId(), KeyPair.random().getAccountId());
    try {
      Sep10Challenge.verifyChallengeTransactionSigners(transaction.toEnvelopeXdrBase64(), server.getAccountId(), network, signers);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("Transaction has unrecognized signatures.", e.getMessage());
    }
  }

  @Test
  public void testVerifyChallengeTransactionSignerOtherSignerType() throws IOException, InvalidSep10ChallengeException {
    Network network = Network.TESTNET;
    KeyPair server = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
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

    List<String> signers = Arrays.asList(
            "TAQCSRX2RIDJNHFIFHWD63X7D7D6TRT5Y2S6E3TEMXTG5W3OECHZ2OG4",
            "XDRPF6NZRR7EEVO7ESIWUDXHAOMM2QSKIQQBJK6I2FB7YKDZES5UCLWD",
            "?ARPF6NZRR7EEVO7ESIWUDXHAOMM2QSKIQQBJK6I2FB7YKDZES5UCLWD",
            masterClient.getAccountId(),
            signerClient1.getAccountId(),
            signerClient2.getAccountId(),
            KeyPair.random().getAccountId());
    List<String> signersFound = Sep10Challenge.verifyChallengeTransactionSigners(transaction.toEnvelopeXdrBase64(), server.getAccountId(), network, signers);
    assertEquals(signersFound, Arrays.asList(masterClient.getAccountId(), signerClient1.getAccountId(), signerClient2.getAccountId()));
  }

  @Test
  public void testVerifyChallengeTransactionSignerNoPublicKeySignerThrows() throws IOException {
    Network network = Network.TESTNET;
    KeyPair server = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
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

    List<String> signers = Arrays.asList(
            "TAQCSRX2RIDJNHFIFHWD63X7D7D6TRT5Y2S6E3TEMXTG5W3OECHZ2OG4",
            "XDRPF6NZRR7EEVO7ESIWUDXHAOMM2QSKIQQBJK6I2FB7YKDZES5UCLWD",
            "?ARPF6NZRR7EEVO7ESIWUDXHAOMM2QSKIQQBJK6I2FB7YKDZES5UCLWD");
    try {
      Sep10Challenge.verifyChallengeTransactionSigners(transaction.toEnvelopeXdrBase64(), server.getAccountId(), network, signers);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("No verifiable signers provided, at least one G... address must be provided.", e.getMessage());
    }
  }

  @Test
  public void testVerifyChallengeTransactionThreshold() throws IOException, InvalidSep10ChallengeException {
    Network network = Network.TESTNET;
    KeyPair server = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
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
    transaction.sign(signerClient1);
    transaction.sign(signerClient2);

    List<Sep10Challenge.Signer> signers = Arrays.asList(
            new Sep10Challenge.Signer(masterClient.getAccountId(), 1),
            new Sep10Challenge.Signer(signerClient1.getAccountId(), 2),
            new Sep10Challenge.Signer(signerClient2.getAccountId(), 4),
            new Sep10Challenge.Signer(KeyPair.random().getAccountId(), 255)
    );

    int threshold = 6;
    List<String> signersFound = Sep10Challenge.verifyChallengeTransactionThreshold(transaction.toEnvelopeXdrBase64(), server.getAccountId(), network, threshold, signers);
    assertEquals(signersFound, Arrays.asList(signerClient1.getAccountId(), signerClient2.getAccountId()));
  }


  @Test
  public void testVerifyChallengeTransactionThresholdNotMeetThreshold() throws IOException {
    Network network = Network.TESTNET;
    KeyPair server = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
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
    transaction.sign(signerClient1);
    transaction.sign(signerClient2);

    List<Sep10Challenge.Signer> signers = Arrays.asList(
            new Sep10Challenge.Signer(masterClient.getAccountId(), 1),
            new Sep10Challenge.Signer(signerClient1.getAccountId(), 2),
            new Sep10Challenge.Signer(signerClient2.getAccountId(), 4),
            new Sep10Challenge.Signer(KeyPair.random().getAccountId(), 255)
    );

    int threshold = 7;
    try {
      Sep10Challenge.verifyChallengeTransactionThreshold(transaction.toEnvelopeXdrBase64(), server.getAccountId(), network, threshold, signers);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals(String.format("Signers with weight %d do not meet threshold %d.", 6, threshold), e.getMessage());
    }
  }

  @Test
  public void testVerifyChallengeTransactionThresholdNoSignerThrows() throws IOException {
    Network network = Network.TESTNET;
    KeyPair server = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
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
    transaction.sign(signerClient1);
    transaction.sign(signerClient2);

    List<Sep10Challenge.Signer> signers = Collections.emptyList();

    int threshold = 6;
    try {
      Sep10Challenge.verifyChallengeTransactionThreshold(transaction.toEnvelopeXdrBase64(), server.getAccountId(), network, threshold, signers);
      fail();
    } catch (InvalidSep10ChallengeException e) {
      assertEquals("No signers provided.", e.getMessage());
    }
  }
}
