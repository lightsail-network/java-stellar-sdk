package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Represents BeginSponsoringFutureReserves operation response.
 *
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class BeginSponsoringFutureReservesOperationResponse extends OperationResponse {
  @SerializedName("sponsored_id")
  String sponsoredId;
}
