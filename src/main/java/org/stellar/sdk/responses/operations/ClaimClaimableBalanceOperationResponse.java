package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;

/**
 * Represents ClaimClaimableBalance operation response.
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
public class ClaimClaimableBalanceOperationResponse extends OperationResponse {
  @SerializedName("balance_id")
  private final String balanceId;
  @SerializedName("claimant")
  protected final String claimant;

  public ClaimClaimableBalanceOperationResponse(String balanceId, String claimant) {
    this.balanceId = balanceId;
    this.claimant = claimant;
  }

  public String getBalanceId() {
    return balanceId;
  }

  public String getClaimant() {
    return claimant;
  }
}
