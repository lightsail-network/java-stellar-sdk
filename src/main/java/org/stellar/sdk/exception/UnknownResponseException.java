package org.stellar.sdk.exception;

import lombok.Getter;

@Getter
public class UnknownResponseException extends NetworkException {
  public UnknownResponseException(Integer code, String body) {
    super(String.format("Unknown response from Horizon: [%d] %s", code, body), code, body);
  }
}
