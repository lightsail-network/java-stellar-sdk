package org.stellar.sdk.responses.sorobanrpc;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import org.stellar.sdk.responses.Response;

@AllArgsConstructor
@Getter
public class SorobanRpcResponse<T> extends Response {
  @SerializedName("jsonrpc")
  private final String jsonRpc;

  @SerializedName("id")
  private final String id;

  @SerializedName("result")
  private final T result;

  @SerializedName("error")
  private final Error error;

  @AllArgsConstructor
  @Value
  public static class Error {
    @SerializedName("code")
    Integer code;

    @SerializedName("message")
    String message;

    @SerializedName("data")
    String data;
  }
}
