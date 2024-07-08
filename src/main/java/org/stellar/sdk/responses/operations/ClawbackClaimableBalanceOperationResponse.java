package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Value;

// TODO: add docs link
/**
 * Represents a Clawback Claimable Balance operation response.
 *
 * @see <a href="" target="_blank">Operation documentation</a>
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class ClawbackClaimableBalanceOperationResponse extends OperationResponse {
  @SerializedName("balance_id")
  String balanceId;
}
