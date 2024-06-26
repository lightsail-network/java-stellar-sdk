package org.stellar.sdk.exception;

import lombok.Getter;
import org.stellar.sdk.responses.Problem;

/**
 * Exception thrown when a bad response is received from the server. This typically indicates a
 * server-side error (5xx HTTP status codes).
 */
@Getter
public class BadResponseException extends SdkException {
  /** The HTTP status code of the response. */
  private final int code;

  /** The raw body of the response. */
  private final String body;

  /** The parsed problem details, if available. */
  private final Problem problem;

  /**
   * Constructs a new BadRequestException.
   *
   * @param code The HTTP status code of the response
   * @param body The raw body of the response
   * @param problem The parsed problem details, may be null if parsing failed
   */
  public BadResponseException(int code, String body, Problem problem) {
    super("Bad response.");
    this.code = code;
    this.body = body;
    this.problem = problem;
  }
}
