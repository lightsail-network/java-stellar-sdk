package org.stellar.sdk.responses.sorobanrpc;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Value;
import org.stellar.sdk.Util;
import org.stellar.sdk.xdr.LedgerEntry;
import org.stellar.sdk.xdr.LedgerKey;

/**
 * Response for JSON-RPC method getLedgerEntries.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/data/apis/rpc/api-reference/methods/getLedgerEntries"
 *     target="_blank">getLedgerEntries documentation</a>
 */
@Value
public class GetLedgerEntriesResponse {
  List<LedgerEntryResult> entries;

  Long latestLedger;

  @Value
  public static class LedgerEntryResult {
    /** The field can be parsed as {@link org.stellar.sdk.xdr.LedgerKey} object. */
    String key;

    /**
     * The field can be parsed as {@link org.stellar.sdk.xdr.LedgerEntry.LedgerEntryData} object.
     */
    String xdr;

    @SerializedName("lastModifiedLedgerSeq")
    Long lastModifiedLedger;

    @SerializedName("liveUntilLedgerSeq")
    Long liveUntilLedger;

    /**
     * Parses the {@code key} field from a string to an {@link org.stellar.sdk.xdr.LedgerKey}
     * object.
     *
     * @return the parsed {@link org.stellar.sdk.xdr.LedgerKey} object
     */
    public LedgerKey parseKey() {
      return Util.parseXdr(key, LedgerKey::fromXdrBase64);
    }

    /**
     * Parses the {@code xdr} field from a string to an {@link
     * org.stellar.sdk.xdr.LedgerEntry.LedgerEntryData} object.
     *
     * @return the parsed {@link org.stellar.sdk.xdr.LedgerEntry.LedgerEntryData} object
     */
    public LedgerEntry.LedgerEntryData parseXdr() {
      return Util.parseXdr(xdr, LedgerEntry.LedgerEntryData::fromXdrBase64);
    }
  }
}
