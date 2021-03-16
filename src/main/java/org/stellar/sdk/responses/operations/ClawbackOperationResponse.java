package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;
import org.stellar.sdk.Asset;

/**
 * Represents a Clawback operation response.
 *
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
public class ClawbackOperationResponse extends OperationResponse {
  @SerializedName("asset_type")
  protected final String assetType;
  @SerializedName("asset_code")
  protected final String assetCode;
  @SerializedName("asset_issuer")
  protected final String assetIssuer;
  @SerializedName("amount")
  protected final String amount;
  @SerializedName("from")
  protected final String from;

  public ClawbackOperationResponse(String assetType, String assetCode, String assetIssuer, String amount, String from) {
    this.assetType = assetType;
    this.assetCode = assetCode;
    this.assetIssuer = assetIssuer;
    this.amount = amount;
    this.from = from;
  }

  public String getAssetType() {
    return assetType;
  }

  public String getAssetIssuer() {
    return assetIssuer;
  }

  public String getAssetCode() {
    return assetCode;
  }

  public Asset getAsset() {
    return Asset.createNonNativeAsset(assetCode, assetIssuer);
  }

  public String getAmount() {
    return amount;
  }

  public String getFrom() {
    return from;
  }

}
