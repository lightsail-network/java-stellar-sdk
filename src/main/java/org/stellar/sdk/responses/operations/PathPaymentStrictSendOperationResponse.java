package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;

/**
 * Represents PATH_PAYMENT_STRICT_SEND operation response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/operations/" target="_blank">Operation
 *     documentation</a>
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
public class PathPaymentStrictSendOperationResponse extends PathPaymentBaseOperationResponse {
  @SerializedName("destination_min")
  private String destinationMin;

  public String getDestinationMin() {
    return destinationMin;
  }
}
