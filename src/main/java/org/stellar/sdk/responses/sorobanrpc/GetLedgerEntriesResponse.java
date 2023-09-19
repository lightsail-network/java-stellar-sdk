package org.stellar.sdk.responses.sorobanrpc;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * Response for JSON-RPC method getLedgerEntries.
 *
 * @see <a href="https://soroban.stellar.org/api/methods/getLedgerEntries#returns"
 *     target="_blank">getLedgerEntries documentation</a>
 */
@AllArgsConstructor
@Value
public class GetLedgerEntriesResponse {
  List<LedgerEntryResult> entries;

  Long latestLedger;

  @AllArgsConstructor
  @Value
  public static class LedgerEntryResult {
    String key;

    String xdr;

    @SerializedName("lastModifiedLedgerSeq")
    Long lastModifiedLedger;
  }
}
