package org.stellar.sdk.exception;

import lombok.Getter;

/**
 * Thrown when the server returns an HTTP status code that is not handled by other specific
 * exception types.
 *
 * <p>The response status code and body are available via {@link #getCode()} and {@link #getBody()}.
 */
@Getter
public class UnknownResponseException extends NetworkException {
  public UnknownResponseException(Integer code, String body) {
    super(String.format("Unknown response from server: [%d] %s", code, body), code, body);
  }
}
