package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;

/**
 * Represents EndSponsoringFutureReserves operation response.
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
public class EndSponsoringFutureReservesOperationResponse extends OperationResponse {
  @SerializedName("begin_sponsor")
  private final String beginSponsor;

  public EndSponsoringFutureReservesOperationResponse(String beginSponsor) {
    this.beginSponsor = beginSponsor;
  }

  public String getBeginSponsor() {
    return beginSponsor;
  }
}
