package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeNative;

import static org.stellar.sdk.Asset.create;

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

  public TrustlineCUDResponse(String limit, String assetType, String assetCode, String assetIssuer, String liquidityPoolId) {
    this.limit = limit;
    this.assetType = assetType;
    this.assetCode = assetCode;
    this.assetIssuer = assetIssuer;
    this.liquidityPoolId = liquidityPoolId;
  }

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
