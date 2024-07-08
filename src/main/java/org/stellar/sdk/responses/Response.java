package org.stellar.sdk.responses;

import lombok.Getter;
import org.stellar.sdk.Asset;
import org.stellar.sdk.LiquidityPoolID;
import org.stellar.sdk.TrustLineAsset;

@Getter
public abstract class Response {
  protected static TrustLineAsset getTrustLineAsset(
      String type, String code, String issuer, String liquidityPoolId) {
    if ("liquidity_pool_shares".equals(type)) {
      return new TrustLineAsset(new LiquidityPoolID(liquidityPoolId));
    }
    return new TrustLineAsset(Asset.create(type, code, issuer));
  }
}
