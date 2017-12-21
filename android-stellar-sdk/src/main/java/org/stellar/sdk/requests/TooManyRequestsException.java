package org.stellar.sdk.requests;

/**
 * Exception thrown when too many requests were sent to the Horizon server.
 * @see <a href="https://www.stellar.org/developers/horizon/learn/rate-limiting.html" target="_blank">Rate Limiting</a>
 */
public class TooManyRequestsException extends RuntimeException {
  private int retryAfter;

  public TooManyRequestsException(int retryAfter) {
    super("The rate limit for the requesting IP address is over its alloted limit.");
    this.retryAfter = retryAfter;
  }

  /**
   * Returns number of seconds a client should wait before sending requests again.
   */
  public int getRetryAfter() {
    return retryAfter;
  }
}
