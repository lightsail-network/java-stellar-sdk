package org.stellar.sdk.exception;

import java.util.Optional;

/**
 * Exception thrown when too many requests were sent to the Horizon server.
 *
 * @see <a href="https://developers.stellar.org/api/introduction/rate-limiting/"
 *     target="_blank">Rate Limiting</a>
 */
public class TooManyRequestsException extends NetworkException {
  private final Integer retryAfter;

  public TooManyRequestsException(Integer retryAfter) {
    super(429, null);
    this.retryAfter = retryAfter;
  }

  /**
   * Returns number of seconds a client should wait before sending requests again, or -1 this time
   * is unknown.
   */
  public Optional<Integer> getRetryAfter() {
    return Optional.ofNullable(retryAfter);
  }
}
