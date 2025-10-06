package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Represents EndSponsoringFutureReserves operation response.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/data/apis/horizon/api-reference/resources/operations/object/end-sponsoring-future-reserves"
 *     target="_blank">Operation documentation</a>
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
}
