package org.stellar.sdk.requests.sorobanrpc;

import com.google.gson.annotations.SerializedName;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * Represent the request sent to Soroban-RPC.
 *
 * @see <a href="https://www.jsonrpc.org/specification#request_object" target="_blank">JSON-RPC 2.0
 *     Specification - Request object</a>
 */
@RequiredArgsConstructor
@Value
public class SorobanRpcRequest<T> {
  @SerializedName("jsonrpc")
  String jsonRpc = "2.0";

  String id;

  String method;

  T params;
}
