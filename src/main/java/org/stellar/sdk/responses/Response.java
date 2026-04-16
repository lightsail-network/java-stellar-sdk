package org.stellar.sdk.responses;

import lombok.Getter;
import org.stellar.sdk.Asset;
import org.stellar.sdk.TrustLineAsset;

/**
 * Abstract base class for all Horizon API response objects.
 *
 * <p>Provides common utility methods shared by Horizon responses, such as constructing {@link
 * org.stellar.sdk.TrustLineAsset} instances from response fields.
 */
@Getter
public abstract class Response {
  protected static TrustLineAsset getTrustLineAsset(
      String type, String code, String issuer, String liquidityPoolId) {
    if ("liquidity_pool_shares".equals(type)) {
      return new TrustLineAsset(liquidityPoolId);
    }
    return new TrustLineAsset(Asset.create(type, code, issuer));
  }
}
