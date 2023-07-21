package org.stellar.sdk.requests.sorobanrpc;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class GetTransactionRequest {
  @SerializedName("hash")
  String hash;
}
