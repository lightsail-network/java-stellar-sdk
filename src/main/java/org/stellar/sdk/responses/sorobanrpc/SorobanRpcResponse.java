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

  private final String id;

  private final T result;

  private final Error error;

  @AllArgsConstructor
  @Value
  public static class Error {
    Integer code;

    String message;

    String data;
  }
}
