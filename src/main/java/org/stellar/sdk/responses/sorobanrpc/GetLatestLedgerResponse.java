package org.stellar.sdk.responses.sorobanrpc;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class GetLatestLedgerResponse {
  @SerializedName("id")
  String id;

  @SerializedName("protocolVersion")
  Integer protocolVersion;

  @SerializedName("sequence")
  Integer sequence;
}
