package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;

/**
 * Represents BeginSponsoringFutureReserves operation response.
 *
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
public class BeginSponsoringFutureReservesOperationResponse extends OperationResponse {
  @SerializedName("sponsored_id")
  private final String sponsoredId;

  public BeginSponsoringFutureReservesOperationResponse(String sponsoredId) {
    this.sponsoredId = sponsoredId;
  }

  public String getSponsoredId() {
    return sponsoredId;
  }
}
