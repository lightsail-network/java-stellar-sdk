package org.stellar.sdk.exception;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

/**
 * Exception thrown when too many requests were sent to the Horizon server.
 *
 * @see <a href="https://developers.stellar.org/api/introduction/rate-limiting/"
 *     target="_blank">Rate Limiting</a>
 */
public class TooManyRequestsException extends NetworkException {

  /** Number of seconds a client should wait before sending requests again */
  @Getter @Nullable private final Integer retryAfter;

  public TooManyRequestsException(@Nullable Integer retryAfter) {
    super("The rate limit for the requesting IP address is over its allowed limit.", 429, null);
    this.retryAfter = retryAfter;
  }
}
