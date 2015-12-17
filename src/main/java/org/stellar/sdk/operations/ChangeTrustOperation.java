package org.stellar.sdk.operations;

import com.google.gson.annotations.SerializedName;

import org.stellar.base.Asset;
import org.stellar.base.AssetTypeNative;
import org.stellar.base.KeyPair;

/**
 * Represents ChangeTrust operation response.
 * @see <a href="https://www.stellar.org/developers/horizon/reference/resources/operation.html" target="_blank">Operation documentation</a>
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
public class ChangeTrustOperation extends Operation {
  @SerializedName("trustor")
  protected final KeyPair trustor;
  @SerializedName("trustee")
  protected final KeyPair trustee;
  @SerializedName("asset_type")
  protected final String assetType;
  @SerializedName("asset_code")
  protected final String assetCode;
  @SerializedName("asset_issuer")
  protected final String assetIssuer;
  @SerializedName("limit")
  protected final String limit;

  ChangeTrustOperation(KeyPair trustor, KeyPair trustee, String assetType, String assetCode, String assetIssuer, String limit) {
    this.trustor = trustor;
    this.trustee = trustee;
    this.assetType = assetType;
    this.assetCode = assetCode;
    this.assetIssuer = assetIssuer;
    this.limit = limit;
  }

  public KeyPair getTrustor() {
    return trustor;
  }

  public KeyPair getTrustee() {
    return trustee;
  }

  public String getLimit() {
    return limit;
  }

  public Asset getAsset() {
    if (assetType.equals("native")) {
      return new AssetTypeNative();
    } else {
      KeyPair issuer = KeyPair.fromAddress(assetIssuer);
      return Asset.createNonNativeAsset(assetCode, issuer);
    }
  }
}
