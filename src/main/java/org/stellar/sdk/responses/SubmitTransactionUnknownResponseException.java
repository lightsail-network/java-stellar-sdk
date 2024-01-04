package org.stellar.sdk.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubmitTransactionUnknownResponseException extends RuntimeException {
  private int code;
  private String body;

  @Override
  public String getMessage() {
    return String.format("Unknown response from Horizon: [%d] %s", code, body);
  }
}
