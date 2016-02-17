package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;

import org.stellar.sdk.KeyPair;

abstract class TrustlineAuthorizationResponse extends EffectResponse {
  @SerializedName("trustor")
  protected final KeyPair trustor;
  @SerializedName("asset_type")
  protected final String assetType;
  @SerializedName("asset_code")
  protected final String assetCode;

  TrustlineAuthorizationResponse(KeyPair trustor, String assetType, String assetCode) {
    this.trustor = trustor;
    this.assetType = assetType;
    this.assetCode = assetCode;
  }

  public KeyPair getTrustor() {
    return trustor;
  }

  public String getAssetType() {
    return assetType;
  }

  public String getAssetCode() {
    return assetCode;
  }
}
