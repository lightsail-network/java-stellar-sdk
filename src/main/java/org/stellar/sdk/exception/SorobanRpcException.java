package org.stellar.sdk.exception;

import lombok.Getter;

/**
 * Thrown when a Stellar RPC (formerly Soroban-RPC) instance responds with an error.
 *
 * @see <a href="https://www.jsonrpc.org/specification#error_object" target="_blank">JSON-RPC 2.0
 *     Specification - Error object</a>
 */
@Getter
public class SorobanRpcException extends NetworkException {
  private final Integer code;

  private final String message;

  private final String data;

  public SorobanRpcException(Integer code, String message, String data) {
    super(message, code, data);
    this.code = code;
    this.message = message;
    this.data = data;
  }
}
