package org.stellar.sdk.responses.sorobanrpc;

import java.util.List;
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

  /** The field can be parsed as {@link org.stellar.sdk.xdr.TransactionResult} object. */
  String errorResultXdr;

  /** The elements inside can be parsed as {@link org.stellar.sdk.xdr.DiagnosticEvent} objects. */
  List<String> diagnosticEventsXdr;

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
