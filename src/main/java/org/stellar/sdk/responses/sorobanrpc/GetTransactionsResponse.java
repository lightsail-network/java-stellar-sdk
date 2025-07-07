package org.stellar.sdk.responses.sorobanrpc;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Value;
import org.stellar.sdk.Util;
import org.stellar.sdk.xdr.DiagnosticEvent;
import org.stellar.sdk.xdr.TransactionEnvelope;
import org.stellar.sdk.xdr.TransactionMeta;
import org.stellar.sdk.xdr.TransactionResult;

/**
 * Response for JSON-RPC method getTransactions.
 *
 * @see <a href="https://developers.stellar.org/docs/data/rpc/api-reference/methods/getTransactions"
 *     target="_blank">getTransactions documentation</a>
 */
@Value
public class GetTransactionsResponse {
  List<Transaction> transactions;
  Long latestLedger;
  Long latestLedgerCloseTimestamp;
  Long oldestLedger;
  Long oldestLedgerCloseTimestamp;
  String cursor;

  @Value
  public static class Transaction {
    TransactionStatus status;
    String txHash;
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

    /**
     * This field is deprecated, and it will be removed in the next version of the Stellar RPC and
     * the SDK. Use {@link Events#getDiagnosticEventsXdr()} instead.
     */
    @Deprecated List<String> diagnosticEventsXdr;

    Events events;

    /**
     * Parses the {@code envelopeXdr} field from a string to an {@link
     * org.stellar.sdk.xdr.TransactionEnvelope} object.
     *
     * @return the parsed {@link org.stellar.sdk.xdr.TransactionEnvelope} object
     */
    public TransactionEnvelope parseEnvelopeXdr() {
      return Util.parseXdr(envelopeXdr, TransactionEnvelope::fromXdrBase64);
    }

    /**
     * Parses the {@code resultXdr} field from a string to an {@link
     * org.stellar.sdk.xdr.TransactionResult} object.
     *
     * @return the parsed {@link org.stellar.sdk.xdr.TransactionResult} object
     */
    public TransactionResult parseResultXdr() {
      return Util.parseXdr(resultXdr, TransactionResult::fromXdrBase64);
    }

    /**
     * Parses the {@code resultMetaXdr} field from a string to an {@link
     * org.stellar.sdk.xdr.TransactionMeta} object.
     *
     * @return the parsed {@link org.stellar.sdk.xdr.TransactionMeta} object
     */
    public TransactionMeta parseResultMetaXdr() {
      return Util.parseXdr(resultMetaXdr, TransactionMeta::fromXdrBase64);
    }

    /**
     * Parses the {@code diagnosticEventsXdr} field from a list of strings to a list of {@link
     * org.stellar.sdk.xdr.DiagnosticEvent} objects.
     *
     * <p>This method is deprecated and will be removed in the next version of the Stellar RPC and
     * the SDK. Use {@link Events#getDiagnosticEventsXdr()} instead.
     *
     * @return a list of parsed {@link org.stellar.sdk.xdr.DiagnosticEvent} objects
     */
    @Deprecated
    public List<DiagnosticEvent> parseDiagnosticEventsXdr() {
      if (diagnosticEventsXdr == null) {
        return null;
      }
      return diagnosticEventsXdr.stream()
          .map(xdr -> Util.parseXdr(xdr, DiagnosticEvent::fromXdrBase64))
          .collect(Collectors.toList());
    }
  }

  public enum TransactionStatus {
    SUCCESS,
    FAILED
  }
}
