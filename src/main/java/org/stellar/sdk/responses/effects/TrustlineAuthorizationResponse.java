package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;

abstract class TrustlineAuthorizationResponse extends EffectResponse {
  @SerializedName("trustor")
  protected final String trustor;

  @SerializedName("asset_type")
  protected final String assetType;

  @SerializedName("asset_code")
  protected final String assetCode;

  TrustlineAuthorizationResponse(String trustor, String assetType, String assetCode) {
    this.trustor = trustor;
    this.assetType = assetType;
    this.assetCode = assetCode;
  }

  public String getTrustor() {
    return trustor;
  }

  public String getAssetType() {
    return assetType;
  }

  public String getAssetCode() {
    return assetCode;
  }
}
