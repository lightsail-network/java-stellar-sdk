package org.stellar.sdk.exception;

import lombok.Getter;

@Getter
public class UnknownResponseException extends NetworkException {
  public UnknownResponseException(Integer code, String body) {
    super(code, body);
  }

  @Override
  public String getMessage() {
    return String.format("Unknown response from Horizon: [%d] %s", this.getCode(), this.getBody());
  }
}
