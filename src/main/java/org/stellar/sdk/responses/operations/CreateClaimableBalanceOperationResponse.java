package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.Asset;
import org.stellar.sdk.Claimant;

/**
 * Represents CreateClaimableBalance operation response.
 *
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class CreateClaimableBalanceOperationResponse extends OperationResponse {
  @SerializedName("sponsor")
  String sponsor;

  @SerializedName("asset")
  Asset asset;

  @SerializedName("amount")
  String amount;

  @SerializedName("claimants")
  List<Claimant> claimants;
}
