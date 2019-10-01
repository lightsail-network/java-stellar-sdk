package org.stellar.sdk;

import com.google.common.io.BaseEncoding;

import java.security.SecureRandom;

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
}
