package org.stellar.sdk.effects;

import com.google.gson.annotations.SerializedName;

import org.stellar.base.Keypair;

abstract class TrustlineAuthorization extends Effect {
  @SerializedName("trustor")
  protected final Keypair trustor;
  @SerializedName("asset_type")
  protected final String assetType;
  @SerializedName("asset_code")
  protected final String assetCode;

  TrustlineAuthorization(Keypair trustor, String assetType, String assetCode) {
    this.trustor = trustor;
    this.assetType = assetType;
    this.assetCode = assetCode;
  }

  public Keypair getTrustor() {
    return trustor;
  }

  public String getAssetType() {
    return assetType;
  }

  public String getAssetCode() {
    return assetCode;
  }
}
