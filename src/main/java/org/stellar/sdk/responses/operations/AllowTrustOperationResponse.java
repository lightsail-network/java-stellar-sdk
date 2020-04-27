package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;

import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeNative;

/**
 * Represents AllowTrust operation response.
 * @see <a href="https://www.stellar.org/developers/horizon/reference/resources/operation.html" target="_blank">Operation documentation</a>
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
public class AllowTrustOperationResponse extends OperationResponse {
  @SerializedName("trustor")
  protected final String trustor;
  @SerializedName("trustee")
  protected final String trustee;
  @SerializedName("asset_type")
  protected final String assetType;
  @SerializedName("asset_code")
  protected final String assetCode;
  @SerializedName("asset_issuer")
  protected final String assetIssuer;
  @SerializedName("authorize")
  protected final boolean authorize;
  @SerializedName("authorize_to_maintain_liabilities")
  protected final boolean authorizeToMaintainLiabilities;

  AllowTrustOperationResponse(boolean authorize, boolean authorizeToMaintainLiabilities, String assetIssuer, String assetCode, String assetType, String trustee, String trustor) {
    this.authorize = authorize;
    this.authorizeToMaintainLiabilities = authorizeToMaintainLiabilities;
    this.assetIssuer = assetIssuer;
    this.assetCode = assetCode;
    this.assetType = assetType;
    this.trustee = trustee;
    this.trustor = trustor;
  }

  public String getTrustor() {
    return trustor;
  }

  public String getTrustee() {
    return trustee;
  }

  public boolean isAuthorize() {
    return authorize;
  }

  public boolean isAuthorizedToMaintainLiabilities() {
    return authorizeToMaintainLiabilities;
  }


  public Asset getAsset() {
    if (assetType.equals("native")) {
      return new AssetTypeNative();
    } else {
      return Asset.createNonNativeAsset(assetCode, assetIssuer);
    }
  }
}
