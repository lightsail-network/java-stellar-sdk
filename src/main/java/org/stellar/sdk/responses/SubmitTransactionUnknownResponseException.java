package org.stellar.sdk.responses;

public class SubmitTransactionUnknownResponseException extends RuntimeException {
  private int code;
  private String body;

  public SubmitTransactionUnknownResponseException(int code, String body) {
    this.code = code;
    this.body = body;
  }

  @Override
  public String getMessage() {
    return String.format("Unknown response from Horizon: [%d] %s", code, body);
  }

  public int getCode() {
    return code;
  }

  public String getBody() {
    return body;
  }
}
