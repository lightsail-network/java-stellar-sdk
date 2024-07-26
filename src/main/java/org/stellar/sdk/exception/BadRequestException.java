package org.stellar.sdk.exception;

import lombok.Getter;
import org.stellar.sdk.responses.Problem;
import org.stellar.sdk.responses.SubmitTransactionAsyncResponse;

/**
 * Exception thrown when a bad request is made to the server. This typically indicates a client-side
 * error (4xx HTTP status codes).
 */
@Getter
public class BadRequestException extends NetworkException {
  /** The parsed problem details, if available. */
  private final Problem problem;

  /**
   * The parsed async transaction submission problem details.
   *
   * <p>This field is only present when the exception is thrown as a result of calling the "Submit
   * Transaction Asynchronously" API endpoint and the server returned an error response. In other
   * cases, it will be null.
   */
  private final SubmitTransactionAsyncResponse submitTransactionAsyncProblem;

  /**
   * Constructs a new BadRequestException.
   *
   * @param code The HTTP status code of the response
   * @param body The raw body of the response
   * @param problem The parsed problem details, may be null if parsing failed
   */
  public BadRequestException(
      int code,
      String body,
      Problem problem,
      SubmitTransactionAsyncResponse submitTransactionAsyncProblem) {
    super("Bad Request.", code, body);
    this.problem = problem;
    this.submitTransactionAsyncProblem = submitTransactionAsyncProblem;
  }
}
