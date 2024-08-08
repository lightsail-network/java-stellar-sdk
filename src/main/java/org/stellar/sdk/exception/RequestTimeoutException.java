package org.stellar.sdk.exception;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import org.stellar.sdk.responses.Problem;

/**
 * Exception thrown when a request times out. This exception can be thrown in two cases:
 *
 * <ol>
 *   <li>When the server returns an HTTP 504 (Gateway Timeout) status code, indicating that the
 *       server did not receive a timely response from an upstream server.
 *   <li>When the client experiences a timeout while waiting for the server's response, even if the
 *       server has not explicitly returned a 504 status code.
 * </ol>
 */
@Getter
public class RequestTimeoutException extends NetworkException {
  /** The parsed problem details, if available. */
  @Nullable private final Problem problem;

  /**
   * Constructs a new BadRequestException.
   *
   * @param code The HTTP status code of the response
   * @param body The raw body of the response
   * @param problem The parsed problem details, may be null if parsing failed
   */
  public RequestTimeoutException(int code, String body, Problem problem) {
    super("Request timed out.", code, body);
    this.problem = problem;
  }

  /**
   * Constructs a new BadRequestException.
   *
   * @param code The HTTP status code of the response
   * @param body The raw body of the response
   */
  public RequestTimeoutException(int code, String body) {
    this(code, body, null);
  }

  /**
   * Constructs a new BadRequestException.
   *
   * @param cause The exception that caused this error
   */
  public RequestTimeoutException(Throwable cause) {
    super("Request timed out", cause, null, null);
    this.problem = null;
  }
}
