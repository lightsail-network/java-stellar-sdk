package org.stellar.sdk.responses.sorobanrpc;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Value;
import org.stellar.sdk.Util;
import org.stellar.sdk.xdr.DiagnosticEvent;
import org.stellar.sdk.xdr.TransactionResult;

/**
 * Response for JSON-RPC method sendTransaction.
 *
 * @see <a href="https://developers.stellar.org/docs/data/rpc/api-reference/methods/sendTransaction"
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

  /**
   * Parses the {@code errorResultXdr} field from a string to an {@link
   * org.stellar.sdk.xdr.TransactionResult} object.
   *
   * @return the parsed {@link org.stellar.sdk.xdr.TransactionResult} object
   */
  public TransactionResult parseErrorResultXdr() {
    return Util.parseXdr(errorResultXdr, TransactionResult::fromXdrBase64);
  }

  /**
   * Parses the {@code diagnosticEventsXdr} field from a list of strings to a list of {@link
   * org.stellar.sdk.xdr.DiagnosticEvent} objects.
   *
   * @return a list of parsed {@link org.stellar.sdk.xdr.DiagnosticEvent} objects
   */
  public List<DiagnosticEvent> parseDiagnosticEventsXdr() {
    if (diagnosticEventsXdr == null) {
      return null;
    }
    return diagnosticEventsXdr.stream()
        .map(xdr -> Util.parseXdr(xdr, DiagnosticEvent::fromXdrBase64))
        .collect(Collectors.toList());
  }

  public enum SendTransactionStatus {
    PENDING,
    DUPLICATE,
    TRY_AGAIN_LATER,
    ERROR
  }
}
