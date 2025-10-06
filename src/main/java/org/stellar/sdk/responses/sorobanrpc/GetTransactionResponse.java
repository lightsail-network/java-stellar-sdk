package org.stellar.sdk.responses.sorobanrpc;

import lombok.Value;
import org.stellar.sdk.Util;
import org.stellar.sdk.xdr.TransactionEnvelope;
import org.stellar.sdk.xdr.TransactionMeta;
import org.stellar.sdk.xdr.TransactionResult;

/**
 * Response for JSON-RPC method getTransaction.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/data/apis/rpc/api-reference/methods/getTransaction"
 *     target="_blank">getTransaction documentation</a>
 */
@Value
public class GetTransactionResponse {
  GetTransactionStatus status;

  String txHash;

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

  public enum GetTransactionStatus {
    NOT_FOUND,
    SUCCESS,
    FAILED
  }
}
