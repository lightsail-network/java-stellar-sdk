package org.stellar.sdk.exception;

import lombok.Getter;

/**
 * Represents an exception that occurs during network operations in the Stellar SDK. This exception
 * is thrown in the following cases: - When the server returns a non-2xx status code - When a
 * request times out - When a request cannot be executed due to cancellation, connectivity problems,
 * or timeouts - When an {@link IllegalStateException} occurs because the call has already been
 * executed
 */
@Getter
public class NetworkException extends SdkException {
  /** The HTTP status code of the response. */
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
