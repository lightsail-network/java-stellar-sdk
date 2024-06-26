package org.stellar.sdk.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UnknownResponseException extends SdkException {
  private int code;
  private String body;

  @Override
  public String getMessage() {
    return String.format("Unknown response from Horizon: [%d] %s", code, body);
  }
}
