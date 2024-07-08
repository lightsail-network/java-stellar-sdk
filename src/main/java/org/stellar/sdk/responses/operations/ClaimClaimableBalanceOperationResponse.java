package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Represents ClaimClaimableBalance operation response.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/data/horizon/api-reference/resources/operations/object/claim-claimable-balance"
 *     target="_blank">Operation documentation</a>
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
