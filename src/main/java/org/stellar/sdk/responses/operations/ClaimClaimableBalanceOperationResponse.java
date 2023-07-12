package org.stellar.sdk.responses.operations;

import com.google.common.base.Optional;
import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import org.stellar.sdk.responses.MuxedAccount;

/**
 * Represents ClaimClaimableBalance operation response.
 *
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
public class ClaimClaimableBalanceOperationResponse extends OperationResponse {
  @SerializedName("balance_id")
  private String balanceId;

  @SerializedName("claimant")
  private String claimant;

  @SerializedName("claimant_muxed")
  private String claimantMuxed;

  @SerializedName("claimant_muxed_id")
  private BigInteger claimantMuxedId;

  public Optional<MuxedAccount> getClaimantMuxed() {
    if (this.claimantMuxed == null || this.claimantMuxed.isEmpty()) {
      return Optional.absent();
    }
    return Optional.of(new MuxedAccount(this.claimantMuxed, this.claimant, this.claimantMuxedId));
  }

  public String getBalanceId() {
    return balanceId;
  }

  public String getClaimant() {
    return claimant;
  }
}
