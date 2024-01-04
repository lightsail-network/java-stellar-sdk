package org.stellar.sdk.responses.sorobanrpc;

import lombok.Value;

/**
 * Response for JSON-RPC method sendTransaction.
 *
 * @see <a href="https://soroban.stellar.org/api/methods/sendTransaction#returns"
 *     target="_blank">sendTransaction documentation</a>
 */
@Value
public class SendTransactionResponse {
  SendTransactionStatus status;

  String errorResultXdr;

  String hash;

  Long latestLedger;

  Long latestLedgerCloseTime;

  public enum SendTransactionStatus {
    PENDING,
    DUPLICATE,
    TRY_AGAIN_LATER,
    ERROR
  }
}
