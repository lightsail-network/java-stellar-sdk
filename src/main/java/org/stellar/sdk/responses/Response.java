package org.stellar.sdk.responses;

import lombok.Getter;
import org.stellar.sdk.Asset;
import org.stellar.sdk.TrustLineAsset;

/**
 * Abstract base class for SDK response objects, shared by both Horizon and Stellar RPC responses.
 *
 * <p>Provides common utility methods used across response types, such as constructing {@link
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
