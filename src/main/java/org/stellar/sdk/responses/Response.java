package org.stellar.sdk.responses;

import org.apache.http.Header;

public abstract class Response {
  protected int rateLimitLimit;
  protected int rateLimitRemaining;
  protected int rateLimitReset;

  public void setHeaders(Header limit, Header remaining, Header reset) {
    this.rateLimitLimit = Integer.parseInt(limit.getValue());
    this.rateLimitRemaining = Integer.parseInt(remaining.getValue());
    this.rateLimitReset = Integer.parseInt(reset.getValue());
  }

  /**
   * Returns X-RateLimit-Limit header from the response.
   * This number represents the he maximum number of requests that the current client can
   * make in one hour.
   * @see <a href="https://www.stellar.org/developers/horizon/learn/rate-limiting.html" target="_blank">Rate Limiting</a>
   */
  public int getRateLimitLimit() {
    return rateLimitLimit;
  }

  /**
   * Returns X-RateLimit-Remaining header from the response.
   * The number of remaining requests for the current window.
   * @see <a href="https://www.stellar.org/developers/horizon/learn/rate-limiting.html" target="_blank">Rate Limiting</a>
   */
  public int getRateLimitRemaining() {
    return rateLimitRemaining;
  }

  /**
   * Returns X-RateLimit-Reset header from the response. Seconds until a new window starts.
   * @see <a href="https://www.stellar.org/developers/horizon/learn/rate-limiting.html" target="_blank">Rate Limiting</a>
   */
  public int getRateLimitReset() {
    return rateLimitReset;
  }
}
