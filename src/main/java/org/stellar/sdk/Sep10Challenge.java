package org.stellar.sdk;

import com.google.common.base.Objects;
import com.google.common.io.BaseEncoding;
import org.stellar.sdk.xdr.DecoratedSignature;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sep10Challenge {
  /**
   * Returns a valid <a href="https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0010.md#response" target="_blank">SEP 10</a> challenge, for use in web authentication.
   * @param signer           The server's signing account.
   * @param network          The Stellar network used by the server.
   * @param clientAccountId  The stellar account belonging to the client.
   * @param anchorName       The name of the anchor which will be included in the ManageData operation.
   * @param timebounds       The lifetime of the challenge token.
   */
  public static String newChallenge(
      KeyPair signer,
      Network network,
      String clientAccountId,
      String anchorName,
      TimeBounds timebounds
  ) {
    byte[] nonce = new byte[48];
    SecureRandom random = new SecureRandom();
    random.nextBytes(nonce);
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] encodedNonce = base64Encoding.encode(nonce).getBytes();

    Account sourceAccount = new Account(signer.getAccountId(), -1L);
    ManageDataOperation operation = new ManageDataOperation.Builder(anchorName + " auth", encodedNonce)
        .setSourceAccount(clientAccountId)
        .build();
    Transaction transaction = new Transaction.Builder(sourceAccount, network)
        .addTimeBounds(timebounds)
        .setOperationFee(100)
        .addOperation(operation)
        .build();

    transaction.sign(signer);

    return transaction.toEnvelopeXdrBase64();
  }

  /**
   * Returns a valid <a href="https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0010.md#response" target="_blank">SEP 10</a> challenge, for use in web authentication.
   *
   * @param signer          The server's signing account.
   * @param network         The Stellar network used by the server.
   * @param clientAccountId The stellar account belonging to the client.
   * @param anchorName      The name of the anchor which will be included in the ManageData operation.
   * @param timeout         Challenge duration in seconds.
   * @return a valid <a href="https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0010.md#response" target="_blank">SEP 10</a> challenge.
   */
  public static String newChallenge(
          KeyPair signer,
          Network network,
          String clientAccountId,
          String anchorName,
          long timeout
  ) {
    long now = System.currentTimeMillis() / 1000L;
    long end = now + timeout;
    TimeBounds timeBounds = new TimeBounds(now, end);
    return newChallenge(signer, network, clientAccountId, anchorName, timeBounds);
  }

  /**
   * Reads a SEP 10 challenge transaction and returns the decoded transaction envelope and client account ID contained within.
   * <p>
   * It also verifies that transaction is signed by the server.
   * <p>
   * It does not verify that the transaction has been signed by the client or
   * that any signatures other than the servers on the transaction are valid. Use
   * one of the following functions to completely verify the transaction:
   * {@link Sep10Challenge#verifyChallengeTransactionSigners(String, String, Network, List)} or
   * {@link Sep10Challenge#verifyChallengeTransactionThreshold(String, String, Network, int, List)}
   *
   * @param challengeTransaction SEP-0010 transaction challenge transaction in base64.
   * @param serverAccountId      Account ID for server's account.
   * @param network              The network to connect to for verifying and retrieving.
   * @return {@link ChallengeTransaction}, the decoded transaction envelope and client account ID contained within.
   * @throws InvalidSep10ChallengeException If the SEP-0010 validation fails, the exception will be thrown.
   * @throws IOException                    If read XDR string fails, the exception will be thrown.
   */
  public static ChallengeTransaction readChallengeTransaction(String challengeTransaction, String serverAccountId, Network network) throws InvalidSep10ChallengeException, IOException {
    // decode the received input as a base64-urlencoded XDR representation of Stellar transaction envelope
    Transaction transaction = Transaction.fromEnvelopeXdr(challengeTransaction, network);

    // verify that transaction source account is equal to the server's signing key
    if (!serverAccountId.equals(transaction.getSourceAccount())) {
      throw new InvalidSep10ChallengeException("Transaction source account is not equal to server's account.");
    }

    // verify that transaction has time bounds set, and that current time is between the minimum and maximum bounds.
    if (transaction.getTimeBounds() == null) {
      throw new InvalidSep10ChallengeException("Transaction requires timebounds.");
    }

    long maxTime = transaction.getTimeBounds().getMaxTime();
    long minTime = transaction.getTimeBounds().getMinTime();
    if (maxTime == 0L) {
      throw new InvalidSep10ChallengeException("Transaction requires non-infinite timebounds.");
    }

    long currentTime = System.currentTimeMillis() / 1000L;
    if (currentTime < minTime || currentTime > maxTime) {
      throw new InvalidSep10ChallengeException("Transaction is not within range of the specified timebounds.");
    }

    // verify that transaction contains a single Manage Data operation and its source account is not null
    if (transaction.getOperations().length != 1) {
      throw new InvalidSep10ChallengeException("Transaction requires a single ManageData operation.");
    }
    Operation operation = transaction.getOperations()[0];
    if (!(operation instanceof ManageDataOperation)) {
      throw new InvalidSep10ChallengeException("Operation type should be ManageData.");
    }
    ManageDataOperation manageDataOperation = (ManageDataOperation) operation;

    // verify that transaction envelope has a correct signature by server's signing key
    String clientAccountId = manageDataOperation.getSourceAccount();
    if (clientAccountId == null) {
      throw new InvalidSep10ChallengeException("Operation should have a source account.");
    }

    if (manageDataOperation.getValue().length != 64) {
      throw new InvalidSep10ChallengeException("Operation value should be a 64 bytes base64 random string.");
    }

    if (transaction.getSignatures().isEmpty()) {
      throw new InvalidSep10ChallengeException("Transaction has no signatures.");
    }

    if (!verifyTransactionSignedByAccountId(transaction, serverAccountId)) {
      throw new InvalidSep10ChallengeException(String.format("Transaction not signed by server: %s.", serverAccountId));
    }

    // verify that transaction sequenceNumber is equal to zero
    if (transaction.getSequenceNumber() != 0L) {
      throw new InvalidSep10ChallengeException("The transaction sequence number should be zero.");
    }

    return new ChallengeTransaction(transaction, clientAccountId);
  }

  /**
   * Verifies that for a SEP 10 challenge transaction
   * all signatures on the transaction are accounted for. A transaction is
   * verified if it is signed by the server account, and all other signatures
   * match a signer that has been provided as an argument. Additional signers can
   * be provided that do not have a signature, but all signatures must be matched
   * to a signer for verification to succeed. If verification succeeds a list of
   * signers that were found is returned, excluding the server account ID.
   *
   * @param challengeTransaction SEP-0010 transaction challenge transaction in base64.
   * @param serverAccountId      Account ID for server's account.
   * @param network              The network to connect to for verifying and retrieving.
   * @param signers              The signers of client account.
   * @return a list of signers that were found is returned, excluding the server account ID.
   * @throws InvalidSep10ChallengeException If the SEP-0010 validation fails, the exception will be thrown.
   * @throws IOException                    If read XDR string fails, the exception will be thrown.
   */
  public static List<String> verifyChallengeTransactionSigners(String challengeTransaction, String serverAccountId, Network network, List<String> signers) throws InvalidSep10ChallengeException, IOException {
    if (signers == null || signers.isEmpty()) {
      throw new InvalidSep10ChallengeException("No signers provided.");
    }

    // Read the transaction which validates its structure.
    ChallengeTransaction readableChallengeTransaction = readChallengeTransaction(challengeTransaction, serverAccountId, network);
    Transaction transaction = readableChallengeTransaction.getTransaction();

    // Ensure the server account ID is an address and not a seed.
    KeyPair serverKeyPair = KeyPair.fromAccountId(serverAccountId);

    // Deduplicate the client signers and ensure the server is not included
    // anywhere we check or output the list of signers.
    List<String> clientSigners = new ArrayList<String>();
    for (String signer : signers) {
      // Ignore the server signer if it is in the signers list. It's
      // important when verifying signers of a challenge transaction that we
      // only verify and return client signers. If an account has the server
      // as a signer the server should not play a part in the authentication
      // of the client.
      if (serverKeyPair.getAccountId().equals(signer) || clientSigners.contains(signer)) {
        continue;
      }
      clientSigners.add(signer);
    }

    // Verify all the transaction's signers (server and client) in one
    // hit. We do this in one hit here even though the server signature was
    // checked in the readChallengeTx to ensure that every signature and signer
    // are consumed only once on the transaction.
    List<String> allSigners = new ArrayList<String>(clientSigners);
    allSigners.add(serverKeyPair.getAccountId());
    List<String> allSignersFound = verifyTransactionSignatures(transaction, allSigners);

    // Confirm the server is in the list of signers found and remove it.
    boolean serverSignerFound = false;
    List<String> signersFound = new ArrayList<String>();
    for (String signer : allSignersFound) {
      if (serverKeyPair.getAccountId().equals(signer)) {
        serverSignerFound = true;
        continue;
      }
      signersFound.add(signer);
    }

    // Confirm we matched a signature to the server signer.
    if (!serverSignerFound) {
      throw new InvalidSep10ChallengeException(String.format("Transaction not signed by server: %s.", serverAccountId));
    }

    // Confirm all signatures were consumed by a signer.
    if (allSignersFound.size() != transaction.getSignatures().size()) {
      throw new InvalidSep10ChallengeException("Transaction has unrecognized signatures.");
    }

    return signersFound;
  }

  /**
   * Verifies that for a SEP-0010 challenge transaction
   * all signatures on the transaction are accounted for and that the signatures
   * meet a threshold on an account. A transaction is verified if it is signed by
   * the server account, and all other signatures match a signer that has been
   * provided as an argument, and those signatures meet a threshold on the
   * account.
   *
   * @param challengeTransaction SEP-0010 transaction challenge transaction in base64.
   * @param serverAccountId      Account ID for server's account.
   * @param network              The network to connect to for verifying and retrieving.
   * @param threshold            The threshold on the client account.
   * @param signers              The signers of client account.
   * @return a list of signers that were found is returned, excluding the server account ID.
   * @throws InvalidSep10ChallengeException If the SEP-0010 validation fails, the exception will be thrown.
   * @throws IOException                    If read XDR string fails, the exception will be thrown.
   */
  public static List<String> verifyChallengeTransactionThreshold(String challengeTransaction, String serverAccountId, Network network, int threshold, List<Signer> signers) throws InvalidSep10ChallengeException, IOException {
    String ed25519PublicKeySignerType = "ed25519_public_key";

    if (signers == null || signers.isEmpty()) {
      throw new InvalidSep10ChallengeException("No signers provided.");
    }

    List<String> ed25519PublicKeySigners = new ArrayList<String>();
    for (Signer signer : signers) {
      if (ed25519PublicKeySignerType.equals(signer.getType())) {
        ed25519PublicKeySigners.add(signer.getKey());
      }
    }

    List<String> signersFound = verifyChallengeTransactionSigners(challengeTransaction, serverAccountId, network, ed25519PublicKeySigners);

    int weight = 0;
    for (String signer : signersFound) {
      for (Signer signerWithWeight : signers) {
        if (signer.equals(signerWithWeight.getKey()) && ed25519PublicKeySignerType.equals(signerWithWeight.getType())) {
          weight += signerWithWeight.getWeight();
          break;
        }
      }
    }

    if (weight < threshold) {
      throw new InvalidSep10ChallengeException(String.format("Signers with weight %d do not meet threshold %d.", weight, threshold));
    }

    return signersFound;
  }

  private static List<String> verifyTransactionSignatures(Transaction transaction, List<String> signers) throws InvalidSep10ChallengeException {
    List<DecoratedSignature> signatures = transaction.getSignatures();
    if (signatures.isEmpty()) {
      throw new InvalidSep10ChallengeException("Transaction has no signatures.");
    }
    List<String> signersFound = new ArrayList<String>();
    for (String signer : signers) {
      if (verifyTransactionSignedByAccountId(transaction, signer)) {
        signersFound.add(signer);
      }
    }
    return signersFound;
  }

  private static boolean verifyTransactionSignedByAccountId(Transaction transaction, String accountId) {
    KeyPair keyPair = KeyPair.fromAccountId(accountId);
    byte[] txHash = transaction.hash();
    for (DecoratedSignature decoratedSignature : transaction.getSignatures()) {
      if (Arrays.equals(decoratedSignature.getHint().getSignatureHint(), keyPair.getSignatureHint().getSignatureHint()) && keyPair.verify(txHash, decoratedSignature.getSignature().getSignature())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Used to store the results produced by {@link Sep10Challenge#readChallengeTransaction(String, String, Network)}.
   */
  public static class ChallengeTransaction {
    private Transaction transaction;
    private String clientAccountId;

    public ChallengeTransaction(Transaction transaction, String clientAccountId) {
      this.transaction = transaction;
      this.clientAccountId = clientAccountId;
    }

    public Transaction getTransaction() {
      return transaction;
    }

    public void setTransaction(Transaction transaction) {
      this.transaction = transaction;
    }

    public String getClientAccountId() {
      return clientAccountId;
    }

    public void setClientAccountId(String clientAccountId) {
      this.clientAccountId = clientAccountId;
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(this.transaction, this.clientAccountId);
    }

    @Override
    public boolean equals(Object object) {
      if (object == this) {
        return true;
      }

      if (!(object instanceof ChallengeTransaction)) {
        return false;
      }

      ChallengeTransaction other = (ChallengeTransaction) object;
      return Objects.equal(this.transaction, other.transaction) &&
              Objects.equal(this.clientAccountId, other.clientAccountId);
    }
  }

  /**
   * Represents signers.
   */
  public static class Signer {
    private String key;
    private String type;
    private int weight;

    public Signer(String key, String type, int weight) {
      this.key = key;
      this.type = type;
      this.weight = weight;
    }

    public String getKey() {
      return key;
    }

    public void setKey(String key) {
      this.key = key;
    }

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public int getWeight() {
      return weight;
    }

    public void setWeight(int weight) {
      this.weight = weight;
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(this.key, this.type, this.weight);
    }

    @Override
    public boolean equals(Object object) {
      if (object == this) {
        return true;
      }

      if (!(object instanceof Signer)) {
        return false;
      }

      Signer other = (Signer) object;
      return Objects.equal(this.key, other.key) &&
              Objects.equal(this.type, other.type) &&
              Objects.equal(this.weight, other.weight);
    }
  }
}
