package org.stellar.sdk.requests;

/** Exception thrown when request returned an non-success HTTP code. */
public class ErrorResponse extends RuntimeException {
  private int code;
  private String body;

  public ErrorResponse(int code, String body) {
    super("Error response from the server.");
    this.code = code;
    this.body = body;
  }

  public int getCode() {
    return code;
  }

  public String getBody() {
    return body;
  }
}
