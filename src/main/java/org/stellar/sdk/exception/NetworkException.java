package org.stellar.sdk.exception;

import lombok.Getter;

/**
 * Represents an exception that occurs during network operations in the Stellar SDK.
 *
 * <p>This exception is thrown in the following cases:
 *
 * <ul>
 *   <li>When the server returns a non-2xx status code
 *   <li>When the error field in the information returned by the RPC server is not empty.
 *   <li>When a request times out
 *   <li>When a request cannot be executed due to cancellation or connectivity problems, etc.
 *   <li>When an {@link IllegalStateException} occurs because the call has already been executed
 * </ul>
 */
@Getter
public class NetworkException extends SdkException {
  /** The status code of the response. */
  private final Integer code;

  /** The raw body of the response. */
  private final String body;

  public NetworkException(Integer code, String body) {
    this.code = code;
    this.body = body;
  }

  public NetworkException(String message, Integer code, String body) {
    super(message);
    this.code = code;
    this.body = body;
  }

  public NetworkException(String message, Throwable cause, Integer code, String body) {
    super(message, cause);
    this.code = code;
    this.body = body;
  }

  public NetworkException(Throwable cause, Integer code, String body) {
    super(cause);
    this.code = code;
    this.body = body;
  }

  public NetworkException(
      String message,
      Throwable cause,
      boolean enableSuppression,
      boolean writableStackTrace,
      Integer code,
      String body) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.code = code;
    this.body = body;
  }
}
