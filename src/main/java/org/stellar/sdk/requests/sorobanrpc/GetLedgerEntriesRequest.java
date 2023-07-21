package org.stellar.sdk.requests.sorobanrpc;

import com.google.gson.annotations.SerializedName;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class GetLedgerEntriesRequest {
  @SerializedName("keys")
  Collection<String> keys;
}
