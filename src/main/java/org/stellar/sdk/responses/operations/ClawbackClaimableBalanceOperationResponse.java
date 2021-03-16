package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a Clawback Claimable Balance operation response.
 *
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
public class ClawbackClaimableBalanceOperationResponse extends OperationResponse {
  @SerializedName("balance_id")
  private final String balanceId;

  public ClawbackClaimableBalanceOperationResponse(String balanceId) {
    this.balanceId = balanceId;
  }

  public String getBalanceId() {
    return balanceId;
  }
}
