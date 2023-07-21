package org.stellar.sdk.requests.sorobanrpc;

import lombok.Getter;

@Getter
public class SorobanRpcErrorResponse extends RuntimeException {
  private final Integer code;

  private final String message;

  private final String data;

  public SorobanRpcErrorResponse(Integer code, String message, String data) {
    super(message);
    this.code = code;
    this.message = message;
    this.data = data;
  }
}
