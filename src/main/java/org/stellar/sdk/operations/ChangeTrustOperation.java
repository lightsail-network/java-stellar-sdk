package org.stellar.sdk.operations;

import com.google.gson.annotations.SerializedName;

import org.stellar.base.Asset;
import org.stellar.base.AssetTypeNative;
import org.stellar.base.Keypair;

/**
 * Represents ChangeTrust operation response.
 */
public class ChangeTrustOperation extends Operation {
  @SerializedName("trustor")
  protected final Keypair trustor;
  @SerializedName("trustee")
  protected final Keypair trustee;
  @SerializedName("asset_type")
  protected final String assetType;
  @SerializedName("asset_code")
  protected final String assetCode;
  @SerializedName("asset_issuer")
  protected final String assetIssuer;
  @SerializedName("limit")
  protected final String limit;

  ChangeTrustOperation(Keypair trustor, Keypair trustee, String assetType, String assetCode, String assetIssuer, String limit) {
    this.trustor = trustor;
    this.trustee = trustee;
    this.assetType = assetType;
    this.assetCode = assetCode;
    this.assetIssuer = assetIssuer;
    this.limit = limit;
  }

  public Keypair getTrustor() {
    return trustor;
  }

  public Keypair getTrustee() {
    return trustee;
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
