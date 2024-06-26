package org.stellar.sdk.exception;

import lombok.Getter;
import org.stellar.sdk.responses.Problem;

@Getter
public class BadResponseException extends SdkException {
  private final int code;
  private final String body;
  private final Problem problem;

  public BadResponseException(int code, String body, Problem problem) {
    super("Bad response.");
    this.code = code;
    this.body = body;
    this.problem = problem;
  }
}