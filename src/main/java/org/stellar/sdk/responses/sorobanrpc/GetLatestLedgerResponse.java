package org.stellar.sdk.responses.sorobanrpc;

import lombok.Value;
import org.stellar.sdk.Util;
import org.stellar.sdk.xdr.LedgerCloseMeta;
import org.stellar.sdk.xdr.LedgerHeader;

/**
 * Response for JSON-RPC method getLatestLedger.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/data/apis/rpc/api-reference/methods/getLatestLedger"
 *     target="_blank">getLatestLedger documentation</a>
 */
@Value
public class GetLatestLedgerResponse {
  String id;

  Integer protocolVersion;

  Integer sequence;

  Long closeTime;

  /** The field can be parsed as {@link LedgerHeader} object. */
  String headerXdr;

  /** The field can be parsed as {@link LedgerCloseMeta} object. */
  String metadataXdr;

  /**
   * Parses the {@code envelopeXdr} field from a string to an {@link LedgerHeader} object.
   *
   * @return the parsed {@link LedgerHeader} object
   */
  public LedgerHeader parseHeaderXdr() {
    return Util.parseXdr(headerXdr, LedgerHeader::fromXdrBase64);
  }

  /**
   * Parses the {@code metadataXdr} field from a string to an {@link LedgerCloseMeta} object.
   *
   * @return the parsed {@link LedgerCloseMeta} object
   */
  public LedgerCloseMeta parseMetadataXdr() {
    return Util.parseXdr(metadataXdr, LedgerCloseMeta::fromXdrBase64);
  }
}
