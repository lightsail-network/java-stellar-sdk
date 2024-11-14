package org.stellar.sdk.responses.sorobanrpc;

import java.util.List;
import lombok.Value;
import org.stellar.sdk.Util;
import org.stellar.sdk.xdr.LedgerCloseMeta;
import org.stellar.sdk.xdr.LedgerHeaderHistoryEntry;

/**
 * Response for JSON-RPC method getLedgers.
 *
 * @see <a href="https://developers.stellar.org/docs/data/rpc/api-reference/methods/getLedgers"
 *     target="_blank">getLedgers documentation</a>
 */
@Value
public class GetLedgersResponse {
  List<LedgerInfo> ledgers;
  Long latestLedger;
  Long latestLedgerCloseTime;
  Long oldestLedger;
  Long oldestLedgerCloseTime;
  String cursor;

  @Value
  public static class LedgerInfo {
    String hash;
    Long sequence;
    Long ledgerCloseTime;

    /** The field can be parsed as {@link LedgerHeaderHistoryEntry} object. */
    String headerXdr;

    /** The field can be parsed as {@link LedgerCloseMeta} object. */
    String metadataXdr;

    /**
     * Parses the {@code envelopeXdr} field from a string to an {@link LedgerHeaderHistoryEntry}
     * object.
     *
     * @return the parsed {@link LedgerHeaderHistoryEntry} object
     */
    public LedgerHeaderHistoryEntry parseHeaderXdr() {
      return Util.parseXdr(headerXdr, LedgerHeaderHistoryEntry::fromXdrBase64);
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
}
