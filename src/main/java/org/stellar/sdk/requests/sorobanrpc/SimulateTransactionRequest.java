package org.stellar.sdk.requests.sorobanrpc;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class SimulateTransactionRequest {
  @SerializedName("transaction")
  String transaction;
}
