package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.responses.MuxedAccount;

/**
 * Represents EndSponsoringFutureReserves operation response.
 *
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class EndSponsoringFutureReservesOperationResponse extends OperationResponse {
  @SerializedName("begin_sponsor")
  String beginSponsor;

  @SerializedName("begin_sponsor_muxed")
  String beginSponsorMuxed;

  @SerializedName("begin_sponsor_muxed_id")
  BigInteger beginSponsorMuxedId;

  public Optional<MuxedAccount> getBeginSponsorMuxed() {
    if (this.beginSponsorMuxed == null || this.beginSponsorMuxed.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(
        new MuxedAccount(this.beginSponsorMuxed, this.beginSponsor, this.beginSponsorMuxedId));
  }
}
