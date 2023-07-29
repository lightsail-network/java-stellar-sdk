package org.stellar.sdk.responses.sorobanrpc;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * Response for JSON-RPC method getTransaction.
 *
 * @see <a href="https://soroban.stellar.org/api/methods/getTransaction#returns"
 *     target="_blank">getTransaction documentation</a>
 */
@AllArgsConstructor
@Value
public class GetTransactionResponse {
  GetTransactionStatus status;

  Long latestLedger;

  Long latestLedgerCloseTime;

  Long oldestLedger;

  Long oldestLedgerCloseTime;

  Integer applicationOrder;

  Boolean feeBump;

  String envelopeXdr;

  String resultXdr;

  String resultMetaXdr;

  Long ledger;

  Long createdAt;

  public enum GetTransactionStatus {
    NOT_FOUND,
    SUCCESS,
    FAILED
  }
}
