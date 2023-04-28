package org.stellar.sdk.requests;

/**
 * Exception thrown when too many requests were sent to the Horizon server.
 * @see <a href="https://developers.stellar.org/api/introduction/rate-limiting/" target="_blank">Rate Limiting</a>
 */
public class TooManyRequestsException extends RuntimeException {
  public TooManyRequestsException() {
    super("The rate limit for the requesting IP address is over its alloted limit.");
  }
}
