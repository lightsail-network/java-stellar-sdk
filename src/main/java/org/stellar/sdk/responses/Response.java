package org.stellar.sdk.responses;

import lombok.Getter;
import okhttp3.Headers;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeNative;
import org.stellar.sdk.LiquidityPoolID;
import org.stellar.sdk.TrustLineAsset;

@Getter
public abstract class Response {
  /**
   * Returns X-RateLimit-Limit header from the response. This number represents the he maximum
   * number of requests that the current client can make in one hour.
   *
   * @see <a href="https://developers.stellar.org/api/introduction/rate-limiting/"
   *     target="_blank">Rate Limiting</a>
   */
  protected int rateLimitLimit;

  /**
   * Returns X-RateLimit-Remaining header from the response. The number of remaining requests for
   * the current window.
   *
   * @see <a href="https://developers.stellar.org/api/introduction/rate-limiting/"
   *     target="_blank">Rate Limiting</a>
   */
  protected int rateLimitRemaining;

  /**
   * Returns X-RateLimit-Reset header from the response. Seconds until a new window starts.
   *
   * @see <a href="https://developers.stellar.org/api/introduction/rate-limiting/"
   *     target="_blank">Rate Limiting</a>
   */
  protected int rateLimitReset;

  public void setHeaders(Headers headers) {
    String limitHeader = headers.get("X-Ratelimit-Limit");
    if (limitHeader != null) {
      this.rateLimitLimit = Integer.parseInt(limitHeader);
    }
    String remainingHeader = headers.get("X-Ratelimit-Remaining");
    if (remainingHeader != null) {
      this.rateLimitRemaining = Integer.parseInt(remainingHeader);
    }
    String resetHeader = headers.get("X-Ratelimit-Reset");
    if (resetHeader != null) {
      this.rateLimitReset = Integer.parseInt(resetHeader);
    }
  }

  protected static TrustLineAsset getTrustLineAsset(
      String type, String code, String issuer, String liquidityPoolId) {
    if (type == null) {
      return new TrustLineAsset(new AssetTypeNative());
    }
    if (type.equals("liquidity_pool_shares")) {
      return new TrustLineAsset(new LiquidityPoolID(liquidityPoolId));
    }
    return new TrustLineAsset(Asset.create(type, code, issuer));
  }
}
