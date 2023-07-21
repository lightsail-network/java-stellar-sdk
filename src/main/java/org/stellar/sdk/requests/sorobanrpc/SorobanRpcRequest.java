package org.stellar.sdk.requests.sorobanrpc;

import com.google.gson.annotations.SerializedName;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
@Value
public class SorobanRpcRequest<T> {
  @SerializedName("jsonrpc")
  String jsonRpc = "2.0";

  @SerializedName("id")
  String id;

  @SerializedName("method")
  String method;

  @SerializedName("params")
  T params;
}
