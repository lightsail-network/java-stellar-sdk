package org.stellar.sdk.responses.sorobanrpc;

import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class GetLedgerEntriesResponse {
  @SerializedName("entries")
  ImmutableList<LedgerEntryResult> entries;

  @SerializedName("latestLedger")
  Long latestLedger;

  @AllArgsConstructor
  @Value
  public static class LedgerEntryResult {
    @SerializedName("key")
    String key;

    @SerializedName("xdr")
    String xdr;

    @SerializedName("lastModifiedLedgerSeq")
    Long lastModifiedLedger;
  }
}
