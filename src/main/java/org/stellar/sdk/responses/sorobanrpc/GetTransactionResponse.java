package org.stellar.sdk.responses.sorobanrpc;

import lombok.Value;

/**
 * Response for JSON-RPC method getTransaction.
 *
 * @see <a href="https://soroban.stellar.org/api/methods/getTransaction#returns"
 *     target="_blank">getTransaction documentation</a>
 */
@Value
public class GetTransactionResponse {
  GetTransactionStatus status;

  Long latestLedger;

  Long latestLedgerCloseTime;

  Long oldestLedger;

  Long oldestLedgerCloseTime;

  Integer applicationOrder;

  Boolean feeBump;

  /** The field can be parsed as {@link org.stellar.sdk.xdr.TransactionEnvelope} object. */
  String envelopeXdr;

  /** The field can be parsed as {@link org.stellar.sdk.xdr.TransactionResult} object. */
  String resultXdr;

  /** The field can be parsed as {@link org.stellar.sdk.xdr.TransactionMeta} object. */
  String resultMetaXdr;

  Long ledger;

  Long createdAt;

  public enum GetTransactionStatus {
    NOT_FOUND,
    SUCCESS,
    FAILED
  }
}
