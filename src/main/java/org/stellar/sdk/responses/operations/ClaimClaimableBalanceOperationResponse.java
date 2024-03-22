package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.responses.MuxedAccount;

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

  public Optional<MuxedAccount> getClaimantMuxed() {
    if (this.claimantMuxed == null || this.claimantMuxed.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(new MuxedAccount(this.claimantMuxed, this.claimant, this.claimantMuxedId));
  }
}
