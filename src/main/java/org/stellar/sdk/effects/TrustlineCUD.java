package org.stellar.sdk.effects;

import com.google.gson.annotations.SerializedName;

import org.stellar.base.Asset;
import org.stellar.base.AssetTypeNative;
import org.stellar.base.Keypair;

public abstract class TrustlineCUD extends Effect {
  @SerializedName("limit")
  protected final String limit;
  @SerializedName("asset_type")
  protected final String assetType;
  @SerializedName("asset_code")
  protected final String assetCode;
  @SerializedName("asset_issuer")
  protected final String assetIssuer;

  public TrustlineCUD(String limit, String assetType, String assetCode, String assetIssuer) {
    this.limit = limit;
    this.assetType = assetType;
    this.assetCode = assetCode;
    this.assetIssuer = assetIssuer;
  }

  public String getLimit() {
    return limit;
  }

  public Asset getAsset() {
    if (assetType.equals("native")) {
      return new AssetTypeNative();
    } else {
      Keypair issuer = Keypair.fromAddress(assetIssuer);
      return Asset.createNonNativeAsset(assetCode, issuer);
    }
  }
}
