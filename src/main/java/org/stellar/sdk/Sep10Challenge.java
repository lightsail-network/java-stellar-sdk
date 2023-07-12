package org.stellar.sdk;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.io.BaseEncoding;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.stellar.sdk.xdr.DecoratedSignature;
import org.stellar.sdk.xdr.Signature;
import org.stellar.sdk.xdr.SignatureHint;

public class Sep10Challenge {
  static final int GRACE_PERIOD_SECONDS = 5 * 60;
  static final String CLIENT_DOMAIN_DATA_NAME = "client_domain";
  private static final String HOME_DOMAIN_MANAGER_DATA_NAME_FLAG = "auth";
  private static final String WEB_AUTH_DOMAIN_MANAGER_DATA_NAME = "web_auth_domain";

  private Sep10Challenge() {
    // no instance
  }

  /**
   * Returns a valid <a
   * href="https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0010.md#response"
   * target="_blank">SEP 10</a> challenge, for use in web authentication.
   *
   * @param signer The server's signing account.
   * @param network The Stellar network used by the server.
   * @param clientAccountId The stellar account belonging to the client.
   * @param domainName The <a href="https://en.wikipedia.org/wiki/Fully_qualified_domain_name"
   *     target="_blank">fully qualified domain name</a> of the service requiring authentication.
   * @param webAuthDomain The fully qualified domain name of the service issuing the challenge.
   * @param timebounds The lifetime of the challenge token.
   */
  public static Transaction newChallenge(
      KeyPair signer,
      Network network,
      String clientAccountId,
      String domainName,
      String webAuthDomain,
      TimeBounds timebounds)
      throws InvalidSep10ChallengeException {
    return newChallenge(
        signer, network, clientAccountId, domainName, webAuthDomain, timebounds, "", "");
  }

  /**
   * Returns a valid <a
   * href="https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0010.md#response"
   * target="_blank">SEP 10</a> challenge, for use in web authentication.
   *
   * @param signer The server's signing account.
   * @param network The Stellar network used by the server.
   * @param clientAccountId The stellar account belonging to the client.
   * @param domainName The <a href="https://en.wikipedia.org/wiki/Fully_qualified_domain_name"
   *     target="_blank">fully qualified domain name</a> of the service requiring authentication.
   * @param webAuthDomain The fully qualified domain name of the service issuing the challenge.
   * @param timebounds The lifetime of the challenge token.
   * @param clientDomain The domain of the client application requesting authentication.
   * @param clientSigningKey The stellar account listed as the SIGNING_KEY on the client domain's
   *     TOML file.
   * @param memo The memo of the challenge transaction.
   */
  public static Transaction newChallenge(
      KeyPair signer,
      Network network,
      String clientAccountId,
      String domainName,
      String webAuthDomain,
      TimeBounds timebounds,
      String clientDomain,
      String clientSigningKey,
      Memo memo)
      throws InvalidSep10ChallengeException {
    byte[] nonce = new byte[48];
    SecureRandom random = new SecureRandom();
    random.nextBytes(nonce);
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] encodedNonce = base64Encoding.encode(nonce).getBytes();

    if (clientDomain.isEmpty() != clientSigningKey.isEmpty()) {
      throw new InvalidSep10ChallengeException(
          "clientDomain is required if clientSigningKey is provided");
    }

    if (StrKey.decodeVersionByte(clientAccountId) != StrKey.VersionByte.ACCOUNT_ID) {
      throw new InvalidSep10ChallengeException(clientAccountId + " is not a valid account id");
    }

    Account sourceAccount = new Account(signer.getAccountId(), -1L);
    ManageDataOperation domainNameOperation =
        new ManageDataOperation.Builder(
                String.format("%s %s", domainName, HOME_DOMAIN_MANAGER_DATA_NAME_FLAG),
                encodedNonce)
            .setSourceAccount(clientAccountId)
            .build();
    ManageDataOperation webAuthDomainOperation =
        new ManageDataOperation.Builder(WEB_AUTH_DOMAIN_MANAGER_DATA_NAME, webAuthDomain.getBytes())
            .setSourceAccount(sourceAccount.getAccountId())
            .build();

    TransactionBuilder builder =
        new TransactionBuilder(AccountConverter.enableMuxed(), sourceAccount, network)
            .addTimeBounds(timebounds)
            .setBaseFee(100)
            .addOperation(domainNameOperation)
            .addOperation(webAuthDomainOperation);

    if (memo != null) {
      if (!(memo instanceof MemoId)) {
        throw new InvalidSep10ChallengeException("only memo type `id` is supported");
      }
      builder.addMemo(memo);
    }

    if (!clientSigningKey.isEmpty()) {
      if (StrKey.decodeVersionByte(clientSigningKey) != StrKey.VersionByte.ACCOUNT_ID) {
        throw new InvalidSep10ChallengeException(clientSigningKey + " is not a valid account id");
      }
      builder.addOperation(
          new ManageDataOperation.Builder(CLIENT_DOMAIN_DATA_NAME, clientDomain.getBytes())
              .setSourceAccount(clientSigningKey)
              .build());
    }

    Transaction transaction = builder.build();
    transaction.sign(signer);

    return transaction;
  }

  /**
   * Returns a valid <a
   * href="https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0010.md#response"
   * target="_blank">SEP 10</a> challenge, for use in web authentication.
   *
   * @param signer The server's signing account.
   * @param network The Stellar network used by the server.
   * @param clientAccountId The stellar account belonging to the client.
   * @param domainName The <a href="https://en.wikipedia.org/wiki/Fully_qualified_domain_name"
   *     target="_blank">fully qualified domain name</a> of the service requiring authentication.
   * @param webAuthDomain The fully qualified domain name of the service issuing the challenge.
   * @param timebounds The lifetime of the challenge token.
   * @param clientDomain The domain of the client application requesting authentication.
   * @param clientSigningKey The stellar account listed as the SIGNING_KEY on the client domain's
   *     TOML file.
   */
  public static Transaction newChallenge(
      KeyPair signer,
      Network network,
      String clientAccountId,
      String domainName,
      String webAuthDomain,
      TimeBounds timebounds,
      String clientDomain,
      String clientSigningKey)
      throws InvalidSep10ChallengeException {
    return newChallenge(
        signer,
        network,
        clientAccountId,
        domainName,
        webAuthDomain,
        timebounds,
        clientDomain,
        clientSigningKey,
        null);
  }

  /**
   * Reads a SEP 10 challenge transaction and returns the decoded transaction envelope and client
   * account ID contained within.
   *
   * <p>It also verifies that transaction is signed by the server.
   *
   * <p>It does not verify that the transaction has been signed by the client or that any signatures
   * other than the servers on the transaction are valid. Use one of the following functions to
   * completely verify the transaction: {@link
   * Sep10Challenge#verifyChallengeTransactionSigners(String, String, Network, String, String, Set)}
   * or {@link Sep10Challenge#verifyChallengeTransactionThreshold(String, String, Network, String,
   * String, int, Set)} or {@link Sep10Challenge#verifyChallengeTransactionSigners(String, String,
   * Network, String[], String, Set)} or {@link
   * Sep10Challenge#verifyChallengeTransactionThreshold(String, String, Network, String[], String,
   * int, Set)} or
   *
   * @param challengeXdr SEP-0010 transaction challenge transaction in base64.
   * @param serverAccountId Account ID for server's account.
   * @param network The network to connect to for verifying and retrieving.
   * @param domainNames An array of home domains, one of which is expected to be included in the
   *     first Manage Data operation's string key.
   * @param webAuthDomain The home domain that is expected to be included as the value of the Manage
   *     Data operation with the 'web_auth_domain' key. If no such operation is included, this
   *     parameter is not used.
   * @return {@link ChallengeTransaction}, the decoded transaction envelope and client account ID
   *     contained within.
   * @throws InvalidSep10ChallengeException If the SEP-0010 validation fails, the exception will be
   *     thrown.
   * @throws IOException If read XDR string fails, the exception will be thrown.
   */
  public static ChallengeTransaction readChallengeTransaction(
      String challengeXdr,
      String serverAccountId,
      Network network,
      String[] domainNames,
      String webAuthDomain)
      throws InvalidSep10ChallengeException, IOException {
    if (domainNames == null || domainNames.length == 0) {
      throw new IllegalArgumentException(
          "At least one domain name must be included in domainNames.");
    }

    // decode the received input as a base64-urlencoded XDR representation of Stellar transaction
    // envelope
    AbstractTransaction parsed =
        Transaction.fromEnvelopeXdr(AccountConverter.enableMuxed(), challengeXdr, network);
    if (!(parsed instanceof Transaction)) {
      throw new InvalidSep10ChallengeException("Transaction cannot be a fee bump transaction");
    }
    Transaction transaction = (Transaction) parsed;

    if (StrKey.decodeVersionByte(serverAccountId) != StrKey.VersionByte.ACCOUNT_ID) {
      throw new InvalidSep10ChallengeException(
          "serverAccountId: " + serverAccountId + " is not a valid account id");
    }

    // verify that transaction source account is equal to the server's signing key
    if (!serverAccountId.equals(transaction.getSourceAccount())) {
      throw new InvalidSep10ChallengeException(
          "Transaction source account is not equal to server's account.");
    }

    // verify that transaction sequenceNumber is equal to zero
    if (transaction.getSequenceNumber() != 0L) {
      throw new InvalidSep10ChallengeException("The transaction sequence number should be zero.");
    }

    Memo memo = transaction.getMemo();
    if (memo != null && !(memo instanceof MemoNone || memo instanceof MemoId)) {
      throw new InvalidSep10ChallengeException("only memo type `id` is supported");
    }

    long maxTime = transaction.getTimeBounds().getMaxTime();
    long minTime = transaction.getTimeBounds().getMinTime();
    if (maxTime == 0L) {
      throw new InvalidSep10ChallengeException("Transaction requires non-infinite timebounds.");
    }

    long currentTime = System.currentTimeMillis() / 1000L;
    if ((currentTime + GRACE_PERIOD_SECONDS) < minTime || currentTime > maxTime) {
      throw new InvalidSep10ChallengeException(
          "Transaction is not within range of the specified timebounds.");
    }

    if (transaction.getOperations().length < 1) {
      throw new InvalidSep10ChallengeException(
          "Transaction requires at least one ManageData operation.");
    }

    // verify that the first operation in the transaction is a Manage Data operation
    // and its source account is not null
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

    String matchedDomainName = null;
    for (String homeDomain : domainNames) {
      if ((homeDomain + " " + HOME_DOMAIN_MANAGER_DATA_NAME_FLAG)
          .equals(manageDataOperation.getName())) {
        matchedDomainName = homeDomain;
        break;
      }
    }

    if (matchedDomainName == null) {
      throw new InvalidSep10ChallengeException(
          "The transaction's operation key name does not include one of the expected home domains.");
    }

    if (StrKey.decodeVersionByte(clientAccountId) != StrKey.VersionByte.ACCOUNT_ID) {
      throw new InvalidSep10ChallengeException(
          "clientAccountId: " + clientAccountId + " is not a valid account id");
    }

    if (manageDataOperation.getValue() == null) {
      throw new InvalidSep10ChallengeException(
          "The transaction's operation value should not be null.");
    }
    // verify manage data value
    if (manageDataOperation.getValue().length != 64) {
      throw new InvalidSep10ChallengeException(
          "Random nonce encoded as base64 should be 64 bytes long.");
    }

    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] nonce;
    try {
      nonce = base64Encoding.decode(new String(manageDataOperation.getValue()));
    } catch (IllegalArgumentException e) {
      throw new InvalidSep10ChallengeException(
          "Failed to decode random nonce provided in ManageData operation.", e);
    }

    if (nonce.length != 48) {
      throw new InvalidSep10ChallengeException(
          "Random nonce before encoding as base64 should be 48 bytes long.");
    }

    // verify subsequent operations are manage data ops with source account set to server account
    for (int i = 1; i < transaction.getOperations().length; i++) {
      Operation op = transaction.getOperations()[i];
      if (!(op instanceof ManageDataOperation)) {
        throw new InvalidSep10ChallengeException("Operation type should be ManageData.");
      }
      ManageDataOperation manageDataOp = (ManageDataOperation) op;
      if (manageDataOp.getSourceAccount() == null) {
        throw new InvalidSep10ChallengeException("Operation should have a source account.");
      }
      if (!manageDataOp.getName().equals(CLIENT_DOMAIN_DATA_NAME)
          && !manageDataOp.getSourceAccount().equals(serverAccountId)) {
        throw new InvalidSep10ChallengeException("Subsequent operations are unrecognized.");
      }
      if (WEB_AUTH_DOMAIN_MANAGER_DATA_NAME.equals(manageDataOp.getName())) {
        if (manageDataOp.getValue() == null) {
          throw new InvalidSep10ChallengeException(
              "'web_auth_domain' operation value should not be null.");
        }
        if (!Arrays.equals(webAuthDomain.getBytes(), manageDataOp.getValue())) {
          throw new InvalidSep10ChallengeException(
              String.format("'web_auth_domain' operation value does not match %s.", webAuthDomain));
        }
      }
    }

    if (!verifyTransactionSignature(transaction, serverAccountId)) {
      throw new InvalidSep10ChallengeException(
          String.format("Transaction not signed by server: %s.", serverAccountId));
    }

    return new ChallengeTransaction(transaction, clientAccountId, matchedDomainName);
  }

  /**
   * Reads a SEP 10 challenge transaction and returns the decoded transaction envelope and client
   * account ID contained within.
   *
   * <p>It also verifies that transaction is signed by the server.
   *
   * <p>It does not verify that the transaction has been signed by the client or that any signatures
   * other than the servers on the transaction are valid. Use one of the following functions to
   * completely verify the transaction: {@link
   * Sep10Challenge#verifyChallengeTransactionSigners(String, String, Network, String, String, Set)}
   * or {@link Sep10Challenge#verifyChallengeTransactionThreshold(String, String, Network, String,
   * String, int, Set)} or {@link Sep10Challenge#verifyChallengeTransactionSigners(String, String,
   * Network, String[], String, Set)} or {@link
   * Sep10Challenge#verifyChallengeTransactionThreshold(String, String, Network, String[], String,
   * int, Set)} or
   *
   * @param challengeXdr SEP-0010 transaction challenge transaction in base64.
   * @param serverAccountId Account ID for server's account.
   * @param network The network to connect to for verifying and retrieving.
   * @param domainName The home domain that is expected to be included in the first Manage Data
   *     operation's string key.
   * @param webAuthDomain The home domain that is expected to be included as the value of the Manage
   *     Data operation with the 'web_auth_domain' key, if present.
   * @return {@link ChallengeTransaction}, the decoded transaction envelope and client account ID
   *     contained within.
   * @throws InvalidSep10ChallengeException If the SEP-0010 validation fails, the exception will be
   *     thrown.
   * @throws IOException If read XDR string fails, the exception will be thrown.
   */
  public static ChallengeTransaction readChallengeTransaction(
      String challengeXdr,
      String serverAccountId,
      Network network,
      String domainName,
      String webAuthDomain)
      throws InvalidSep10ChallengeException, IOException {
    return readChallengeTransaction(
        challengeXdr, serverAccountId, network, new String[] {domainName}, webAuthDomain);
  }

  /**
   * Verifies that for a SEP 10 challenge transaction all signatures on the transaction are
   * accounted for. A transaction is verified if it is signed by the server account, and all other
   * signatures match a signer that has been provided as an argument. Additional signers can be
   * provided that do not have a signature, but all signatures must be matched to a signer for
   * verification to succeed. If verification succeeds a list of signers that were found is
   * returned, excluding the server account ID.
   *
   * @param challengeXdr SEP-0010 transaction challenge transaction in base64.
   * @param serverAccountId Account ID for server's account.
   * @param network The network to connect to for verifying and retrieving.
   * @param domainName The home domain that is expected to be included in the first Manage Data
   *     operation's string key.
   * @param webAuthDomain The home domain that is expected to be included as the value of the Manage
   *     Data operation with the 'web_auth_domain' key, if present.
   * @param signers The signers of client account.
   * @return a list of signers that were found is returned, excluding the server account ID.
   * @throws InvalidSep10ChallengeException If the SEP-0010 validation fails, the exception will be
   *     thrown.
   * @throws IOException If read XDR string fails, the exception will be thrown.
   */
  public static Set<String> verifyChallengeTransactionSigners(
      String challengeXdr,
      String serverAccountId,
      Network network,
      String domainName,
      String webAuthDomain,
      Set<String> signers)
      throws InvalidSep10ChallengeException, IOException {
    return verifyChallengeTransactionSigners(
        challengeXdr, serverAccountId, network, new String[] {domainName}, webAuthDomain, signers);
  }

  /**
   * Verifies that for a SEP 10 challenge transaction all signatures on the transaction are
   * accounted for. A transaction is verified if it is signed by the server account, and all other
   * signatures match a signer that has been provided as an argument. Additional signers can be
   * provided that do not have a signature, but all signatures must be matched to a signer for
   * verification to succeed. If verification succeeds a list of signers that were found is
   * returned, excluding the server account ID.
   *
   * @param challengeXdr SEP-0010 transaction challenge transaction in base64.
   * @param serverAccountId Account ID for server's account.
   * @param network The network to connect to for verifying and retrieving.
   * @param domainNames An array of home domains, one of which is expected to be included in the
   *     first Manage Data operation's string key.
   * @param webAuthDomain The home domain that is expected to be included as the value of the Manage
   *     Data operation with the 'web_auth_domain' key, if present.
   * @param signers The signers of client account.
   * @return a list of signers that were found is returned, excluding the server account ID.
   * @throws InvalidSep10ChallengeException If the SEP-0010 validation fails, the exception will be
   *     thrown.
   * @throws IOException If read XDR string fails, the exception will be thrown.
   */
  public static Set<String> verifyChallengeTransactionSigners(
      String challengeXdr,
      String serverAccountId,
      Network network,
      String[] domainNames,
      String webAuthDomain,
      Set<String> signers)
      throws InvalidSep10ChallengeException, IOException {
    if (signers == null || signers.isEmpty()) {
      throw new InvalidSep10ChallengeException(
          "No verifiable signers provided, at least one G... address must be provided.");
    }

    // Read the transaction which validates its structure.
    ChallengeTransaction parsedChallengeTransaction =
        readChallengeTransaction(
            challengeXdr, serverAccountId, network, domainNames, webAuthDomain);
    Transaction transaction = parsedChallengeTransaction.getTransaction();

    // Ensure the server account ID is an address and not a seed.
    KeyPair serverKeyPair = KeyPair.fromAccountId(serverAccountId);

    // Deduplicate the client signers and ensure the server is not included
    // anywhere we check or output the list of signers.
    Set<String> clientSigners = new HashSet<String>();
    for (String signer : signers) {
      // Ignore non-G... account/address signers.
      StrKey.VersionByte versionByte;
      try {
        versionByte = StrKey.decodeVersionByte(signer);
      } catch (Exception e) {
        continue;
      }

      if (!StrKey.VersionByte.ACCOUNT_ID.equals(versionByte)) {
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
      throw new InvalidSep10ChallengeException(
          "No verifiable signers provided, at least one G... address must be provided.");
    }

    // Verify all the transaction's signers (server and client) in one
    // hit. We do this in one hit here even though the server signature was
    // checked in the readChallengeTx to ensure that every signature and signer
    // are consumed only once on the transaction.
    Set<String> allSigners = new HashSet<String>(clientSigners);
    allSigners.add(serverKeyPair.getAccountId());
    Optional<String> clientDomainSigner = Optional.absent();

    for (Operation op : transaction.getOperations()) {
      if (!(op instanceof ManageDataOperation)) {
        throw new InvalidSep10ChallengeException("Operation type should be ManageData.");
      }
      ManageDataOperation manageDataOp = (ManageDataOperation) op;
      if (manageDataOp.getSourceAccount() == null) {
        throw new InvalidSep10ChallengeException("Operation should have a source account.");
      }
      if (manageDataOp.getName().equals(CLIENT_DOMAIN_DATA_NAME)) {
        allSigners.add(manageDataOp.getSourceAccount());
        clientDomainSigner = Optional.of(manageDataOp.getSourceAccount());
        break;
      }
    }

    Set<String> signersFound = verifyTransactionSignatures(transaction, allSigners);

    // Confirm the server is in the list of signers found and remove it.
    boolean serverSignerFound = signersFound.remove(serverKeyPair.getAccountId());

    // Confirm we matched a signature to the server signer.
    if (!serverSignerFound) {
      throw new InvalidSep10ChallengeException(
          String.format("Transaction not signed by server: %s.", serverAccountId));
    }

    // Confirm we matched signatures to the client signers.
    if (signersFound.isEmpty()) {
      throw new InvalidSep10ChallengeException("Transaction not signed by any client signer.");
    }

    int expectedSignaturesLength = transaction.getSignatures().size() - 1;

    if (clientDomainSigner.isPresent()) {
      // Confirm the client_domain signer is in the list of signers found and remove it.
      boolean clientSignerFound = signersFound.remove(clientDomainSigner.get());

      // Confirm we matched a signature to the client_domain signer.
      if (!clientSignerFound) {
        throw new InvalidSep10ChallengeException(
            String.format(
                "Transaction not signed by by the source account of the 'client_domain' ManageDataOperation: %s.",
                clientDomainSigner.get()));
      }
      expectedSignaturesLength--;
    }

    // Confirm all signatures were consumed by a signer.
    if (signersFound.size() != expectedSignaturesLength) {
      throw new InvalidSep10ChallengeException("Transaction has unrecognized signatures.");
    }

    return signersFound;
  }

  /**
   * Verifies that for a SEP-0010 challenge transaction all signatures on the transaction are
   * accounted for and that the signatures meet a threshold on an account. A transaction is verified
   * if it is signed by the server account, and all other signatures match a signer that has been
   * provided as an argument, and those signatures meet a threshold on the account.
   *
   * @param challengeXdr SEP-0010 transaction challenge transaction in base64.
   * @param serverAccountId Account ID for server's account.
   * @param network The network to connect to for verifying and retrieving.
   * @param domainNames An array of home domains, one of which is expected to be included in the
   *     first Manage Data operation's string key.
   * @param webAuthDomain The home domain that is expected to be included as the value of the Manage
   *     Data operation with the 'web_auth_domain' key, if present.
   * @param threshold The threshold on the client account.
   * @param signers The signers of client account.
   * @return a list of signers that were found is returned, excluding the server account ID.
   * @throws InvalidSep10ChallengeException If the SEP-0010 validation fails, the exception will be
   *     thrown.
   * @throws IOException If read XDR string fails, the exception will be thrown.
   */
  public static Set<String> verifyChallengeTransactionThreshold(
      String challengeXdr,
      String serverAccountId,
      Network network,
      String[] domainNames,
      String webAuthDomain,
      int threshold,
      Set<Signer> signers)
      throws InvalidSep10ChallengeException, IOException {
    if (signers == null || signers.isEmpty()) {
      throw new InvalidSep10ChallengeException(
          "No verifiable signers provided, at least one G... address must be provided.");
    }

    Map<String, Integer> weightsForSigner = new HashMap<String, Integer>();
    for (Signer signer : signers) {
      weightsForSigner.put(signer.getKey(), signer.getWeight());
    }

    Set<String> signersFound =
        verifyChallengeTransactionSigners(
            challengeXdr,
            serverAccountId,
            network,
            domainNames,
            webAuthDomain,
            weightsForSigner.keySet());

    int sum = 0;
    for (String signer : signersFound) {
      Integer weight = weightsForSigner.get(signer);
      if (weight != null) {
        sum += weight;
      }
    }

    if (sum < threshold) {
      throw new InvalidSep10ChallengeException(
          String.format("Signers with weight %d do not meet threshold %d.", sum, threshold));
    }

    return signersFound;
  }

  /**
   * Verifies that for a SEP-0010 challenge transaction all signatures on the transaction are
   * accounted for and that the signatures meet a threshold on an account. A transaction is verified
   * if it is signed by the server account, and all other signatures match a signer that has been
   * provided as an argument, and those signatures meet a threshold on the account.
   *
   * @param challengeXdr SEP-0010 transaction challenge transaction in base64.
   * @param serverAccountId Account ID for server's account.
   * @param network The network to connect to for verifying and retrieving.
   * @param domainName The home domain that is expected to be included in the first Manage Data
   *     operation's string key.
   * @param webAuthDomain The home domain that is expected to be included as the value of the Manage
   *     Data operation with the 'web_auth_domain' key, if present.
   * @param threshold The threshold on the client account.
   * @param signers The signers of client account.
   * @return a list of signers that were found is returned, excluding the server account ID.
   * @throws InvalidSep10ChallengeException If the SEP-0010 validation fails, the exception will be
   *     thrown.
   * @throws IOException If read XDR string fails, the exception will be thrown.
   */
  public static Set<String> verifyChallengeTransactionThreshold(
      String challengeXdr,
      String serverAccountId,
      Network network,
      String domainName,
      String webAuthDomain,
      int threshold,
      Set<Signer> signers)
      throws InvalidSep10ChallengeException, IOException {
    return verifyChallengeTransactionThreshold(
        challengeXdr,
        serverAccountId,
        network,
        new String[] {domainName},
        webAuthDomain,
        threshold,
        signers);
  }

  private static Set<String> verifyTransactionSignatures(
      Transaction transaction, Set<String> signers) throws InvalidSep10ChallengeException {
    if (transaction.getSignatures().isEmpty()) {
      throw new InvalidSep10ChallengeException("Transaction has no signatures.");
    }

    byte[] txHash = transaction.hash();

    // find and verify signatures
    Set<String> signersFound = new HashSet<String>();
    Multimap<SignatureHint, Signature> signatures = HashMultimap.create();
    for (DecoratedSignature decoratedSignature : transaction.getSignatures()) {
      signatures.put(decoratedSignature.getHint(), decoratedSignature.getSignature());
    }

    for (String signer : signers) {
      KeyPair keyPair;
      try {
        keyPair = KeyPair.fromAccountId(signer);
      } catch (RuntimeException e) {
        continue;
      }
      SignatureHint hint = keyPair.getSignatureHint();

      for (Signature signature : signatures.get(hint)) {
        if (keyPair.verify(txHash, signature.getSignature())) {
          signersFound.add(signer);
          // explicitly ensure that a transaction signature cannot be
          // mapped to more than one signer
          signatures.remove(hint, signature);
          break;
        }
      }
    }

    return signersFound;
  }

  private static boolean verifyTransactionSignature(Transaction transaction, String accountId)
      throws InvalidSep10ChallengeException {
    return !verifyTransactionSignatures(transaction, Collections.singleton(accountId)).isEmpty();
  }

  /**
   * Used to store the results produced by {@link Sep10Challenge#readChallengeTransaction(String,
   * String, Network, String[], String)}.
   */
  public static class ChallengeTransaction {
    private final Transaction transaction;
    private final String clientAccountId;
    private final String matchedHomeDomain;

    public ChallengeTransaction(
        Transaction transaction, String clientAccountId, String matchedHomeDomain) {
      this.transaction = transaction;
      this.clientAccountId = clientAccountId;
      this.matchedHomeDomain = matchedHomeDomain;
    }

    public Transaction getTransaction() {
      return transaction;
    }

    public String getClientAccountId() {
      return clientAccountId;
    }

    public String getMatchedHomeDomain() {
      return matchedHomeDomain;
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(this.transaction.hashHex(), this.clientAccountId);
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
      return Objects.equal(this.transaction.hashHex(), other.transaction.hashHex())
          && Objects.equal(this.clientAccountId, other.clientAccountId)
          && Objects.equal(this.matchedHomeDomain, other.matchedHomeDomain);
    }
  }

  /** Represents a transaction signer. */
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
      return Objects.equal(this.key, other.key) && Objects.equal(this.weight, other.weight);
    }
  }
}
