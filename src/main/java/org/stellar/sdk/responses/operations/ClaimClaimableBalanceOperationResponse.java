package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Represents ClaimClaimableBalance operation response.
 *
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class ClaimClaimableBalanceOperationResponse extends OperationResponse {
  @SerializedName("balance_id")
  String balanceId;

  @SerializedName("claimant")
  String claimant;

  @SerializedName("claimant_muxed")
  String claimantMuxed;

  @SerializedName("claimant_muxed_id")
  BigInteger claimantMuxedId;
}
