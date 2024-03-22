package org.stellar.sdk.requests;

import lombok.Getter;

/** Exception thrown when request returned an non-success HTTP code. */
@Getter
public class ErrorResponse extends RuntimeException {
  private final int code;
  private final String body;

  public ErrorResponse(int code, String body) {
    super("Error response from the server.");
    this.code = code;
    this.body = body;
  }
}
