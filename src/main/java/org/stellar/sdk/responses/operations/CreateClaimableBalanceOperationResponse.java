package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import org.stellar.sdk.Asset;
import org.stellar.sdk.Claimant;

/**
 * Represents CreateClaimableBalance operation response.
 *
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
public class CreateClaimableBalanceOperationResponse extends OperationResponse {
  @SerializedName("asset")
  private final String assetString;

  @SerializedName("amount")
  protected final String amount;

  @SerializedName("claimants")
  protected final List<Claimant> claimants;

  public CreateClaimableBalanceOperationResponse(
      String assetString, String amount, List<Claimant> claimants) {
    this.assetString = assetString;
    this.amount = amount;
    this.claimants = claimants;
  }

  public String getAssetString() {
    return assetString;
  }

  public Asset getAsset() {
    return Asset.create(assetString);
  }

  public String getAmount() {
    return amount;
  }

  public List<Claimant> getClaimants() {
    return claimants;
  }
}
