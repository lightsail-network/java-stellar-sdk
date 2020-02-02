package org.stellar.sdk;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.io.BaseEncoding;
import org.stellar.sdk.xdr.DecoratedSignature;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.*;

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
   * Reads a SEP 10 challenge transaction and returns the decoded transaction envelope and client account ID contained within.
   * <p>
   * It also verifies that transaction is signed by the server.
   * <p>
   * It does not verify that the transaction has been signed by the client or
   * that any signatures other than the servers on the transaction are valid. Use
   * one of the following functions to completely verify the transaction:
   * {@link Sep10Challenge#verifyChallengeTransactionSigners(String, String, Network, Set)} or
   * {@link Sep10Challenge#verifyChallengeTransactionThreshold(String, String, Network, int, Set)}
   *
   * @param challengeXdr    SEP-0010 transaction challenge transaction in base64.
   * @param serverAccountId Account ID for server's account.
   * @param network         The network to connect to for verifying and retrieving.
   * @return {@link ChallengeTransaction}, the decoded transaction envelope and client account ID contained within.
   * @throws InvalidSep10ChallengeException If the SEP-0010 validation fails, the exception will be thrown.
   * @throws IOException                    If read XDR string fails, the exception will be thrown.
   */
  public static ChallengeTransaction readChallengeTransaction(String challengeXdr, String serverAccountId, Network network) throws InvalidSep10ChallengeException, IOException {
    // decode the received input as a base64-urlencoded XDR representation of Stellar transaction envelope
    Transaction transaction = Transaction.fromEnvelopeXdr(challengeXdr, network);

    // verify that transaction source account is equal to the server's signing key
    if (!serverAccountId.equals(transaction.getSourceAccount())) {
      throw new InvalidSep10ChallengeException("Transaction source account is not equal to server's account.");
    }

    // verify that transaction sequenceNumber is equal to zero
    if (transaction.getSequenceNumber() != 0L) {
      throw new InvalidSep10ChallengeException("The transaction sequence number should be zero.");
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

    // verify manage data value
    if (manageDataOperation.getValue().length != 64) {
      throw new InvalidSep10ChallengeException("Random nonce encoded as base64 should be 64 bytes long.");
    }

    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] nonce = base64Encoding.decode(new String(manageDataOperation.getValue()));
    if (nonce.length != 48) {
      throw new InvalidSep10ChallengeException("Random nonce before encoding as base64 should be 48 bytes long.");
    }

    if (!verifyTransactionSignature(transaction, serverAccountId)) {
      throw new InvalidSep10ChallengeException(String.format("Transaction not signed by server: %s.", serverAccountId));
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
   * @param challengeXdr    SEP-0010 transaction challenge transaction in base64.
   * @param serverAccountId Account ID for server's account.
   * @param network         The network to connect to for verifying and retrieving.
   * @param signers         The signers of client account.
   * @return a list of signers that were found is returned, excluding the server account ID.
   * @throws InvalidSep10ChallengeException If the SEP-0010 validation fails, the exception will be thrown.
   * @throws IOException                    If read XDR string fails, the exception will be thrown.
   */
  public static LinkedHashSet<String> verifyChallengeTransactionSigners(String challengeXdr, String serverAccountId, Network network, Set<String> signers) throws InvalidSep10ChallengeException, IOException {
    if (signers == null || signers.isEmpty()) {
      throw new InvalidSep10ChallengeException("No signers provided.");
    }

    // Read the transaction which validates its structure.
    ChallengeTransaction parsedChallengeTransaction = readChallengeTransaction(challengeXdr, serverAccountId, network);
    Transaction transaction = parsedChallengeTransaction.getTransaction();

    // Ensure the server account ID is an address and not a seed.
    KeyPair serverKeyPair = KeyPair.fromAccountId(serverAccountId);

    // Deduplicate the client signers and ensure the server is not included
    // anywhere we check or output the list of signers.
    Set<String> clientSigners = new HashSet<String>();
    for (String signer : signers) {
      // Ignore non-G... account/address signers.
      Optional<StrKey.VersionByte> versionByteOptional;
      versionByteOptional = StrKey.decodedVersionByte(signer);
      if (!versionByteOptional.isPresent()) {
        continue;
      }
      if (!StrKey.VersionByte.ACCOUNT_ID.equals(versionByteOptional.get())) {
        continue;
      }

      // Ignore the server signer if it is in the signers list. It's
      // important when verifying signers of a challenge transaction that we
      // only verify and return client signers. If an account has the server
      // as a signer the server should not play a part in the authentication
      // of the client.
      if (serverKeyPair.getAccountId().equals(signer)) {
        continue;
      }
      clientSigners.add(signer);
    }

    // Don't continue if none of the signers provided are in the final list.
    if (clientSigners.isEmpty()) {
      throw new InvalidSep10ChallengeException("No verifiable signers provided, at least one G... address must be provided.");
    }

    // Verify all the transaction's signers (server and client) in one
    // hit. We do this in one hit here even though the server signature was
    // checked in the readChallengeTx to ensure that every signature and signer
    // are consumed only once on the transaction.
    Set<String> allSigners = new HashSet<String>(clientSigners);
    allSigners.add(serverKeyPair.getAccountId());
    LinkedHashSet<String> signersFound = verifyTransactionSignatures(transaction, allSigners);

    // Confirm the server is in the list of signers found and remove it.
    boolean serverSignerFound = signersFound.remove(serverKeyPair.getAccountId());

    // Confirm we matched a signature to the server signer.
    if (!serverSignerFound) {
      throw new InvalidSep10ChallengeException(String.format("Transaction not signed by server: %s.", serverAccountId));
    }

    // Confirm we matched signatures to the client signers.
    if (signersFound.isEmpty()) {
      throw new InvalidSep10ChallengeException("Transaction not signed by any client signer.");
    }

    // Confirm all signatures were consumed by a signer.
    if (signersFound.size() != transaction.getSignatures().size() - 1) {
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
   * @param challengeXdr    SEP-0010 transaction challenge transaction in base64.
   * @param serverAccountId Account ID for server's account.
   * @param network         The network to connect to for verifying and retrieving.
   * @param threshold       The threshold on the client account.
   * @param signers         The signers of client account.
   * @return a list of signers that were found is returned, excluding the server account ID.
   * @throws InvalidSep10ChallengeException If the SEP-0010 validation fails, the exception will be thrown.
   * @throws IOException                    If read XDR string fails, the exception will be thrown.
   */
  public static LinkedHashSet<String> verifyChallengeTransactionThreshold(String challengeXdr, String serverAccountId, Network network, int threshold, Set<Signer> signers) throws InvalidSep10ChallengeException, IOException {
    if (signers == null || signers.isEmpty()) {
      throw new InvalidSep10ChallengeException("No signers provided.");
    }

    Map<String, Signer> accountIdSignerMap = new HashMap<String, Signer>();
    for (Signer signer : signers) {
      accountIdSignerMap.put(signer.getKey(), signer);
    }

    LinkedHashSet<String> signersFound = verifyChallengeTransactionSigners(challengeXdr, serverAccountId, network, accountIdSignerMap.keySet());

    int weight = 0;
    for (String signer : signersFound) {
      if (!accountIdSignerMap.containsKey(signer)) {
        continue;
      }
      weight += accountIdSignerMap.get(signer).getWeight();
    }

    if (weight < threshold) {
      throw new InvalidSep10ChallengeException(String.format("Signers with weight %d do not meet threshold %d.", weight, threshold));
    }

    return signersFound;
  }

  private static LinkedHashSet<String> verifyTransactionSignatures(Transaction transaction, Set<String> signers) throws InvalidSep10ChallengeException {
    List<DecoratedSignature> signatures = transaction.getSignatures();
    if (signatures.isEmpty()) {
      throw new InvalidSep10ChallengeException("Transaction has no signatures.");
    }

    byte[] txHash = transaction.hash();

    // find and verify signatures
    Set<Integer> signatureUsed = new HashSet<Integer>();
    LinkedHashSet<String> signersFound = new LinkedHashSet<String>();
    for (String signer : signers) {
      KeyPair keyPair = KeyPair.fromAccountId(signer);
      int index = -1;
      for (DecoratedSignature decoratedSignature : transaction.getSignatures()) {
        index += 1;
        // prevent a signature from being reused
        if (signatureUsed.contains(index)) {
          continue;
        }

        if (Arrays.equals(decoratedSignature.getHint().getSignatureHint(), keyPair.getSignatureHint().getSignatureHint()) && keyPair.verify(txHash, decoratedSignature.getSignature().getSignature())) {
          signersFound.add(signer);
          signatureUsed.add(index);
          break;
        }
      }
    }
    return signersFound;
  }

  private static boolean verifyTransactionSignature(Transaction transaction, String accountId) throws InvalidSep10ChallengeException {
    return !verifyTransactionSignatures(transaction, Collections.singleton(accountId)).isEmpty();
  }

  /**
   * Used to store the results produced by {@link Sep10Challenge#readChallengeTransaction(String, String, Network)}.
   */
  public static class ChallengeTransaction {
    private final Transaction transaction;
    private final String clientAccountId;

    public ChallengeTransaction(Transaction transaction, String clientAccountId) {
      this.transaction = transaction;
      this.clientAccountId = clientAccountId;
    }

    public Transaction getTransaction() {
      return transaction;
    }

    public String getClientAccountId() {
      return clientAccountId;
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
   * Represents a transaction signer.
   */
  public static class Signer {
    private final String key;
    private final int weight;

    public Signer(String key, int weight) {
      this.key = key;
      this.weight = weight;
    }

    public String getKey() {
      return key;
    }

    public int getWeight() {
      return weight;
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(this.key, this.weight);
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
              Objects.equal(this.weight, other.weight);
    }
  }
}
