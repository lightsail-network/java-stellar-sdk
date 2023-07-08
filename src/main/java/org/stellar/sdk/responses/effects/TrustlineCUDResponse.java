package org.stellar.sdk.responses.effects;

import static org.stellar.sdk.Asset.create;

import com.google.gson.annotations.SerializedName;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeNative;

abstract class TrustlineCUDResponse extends EffectResponse {
  @SerializedName("limit")
  private String limit;

  @SerializedName("asset_type")
  private String assetType;

  @SerializedName("asset_code")
  private String assetCode;

  @SerializedName("asset_issuer")
  private String assetIssuer;

  @SerializedName("liquidity_pool_id")
  private String liquidityPoolId;

  public String getLimit() {
    return limit;
  }

  public Asset getAsset() {
    if (assetType.equals("native")) {
      return new AssetTypeNative();
    } else {
      return create(assetType, assetCode, assetIssuer, liquidityPoolId);
    }
  }
}
