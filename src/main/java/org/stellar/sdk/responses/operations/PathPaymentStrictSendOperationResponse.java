package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Represents PathPaymentStrictSend operation response.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/data/apis/horizon/api-reference/resources/operations/object/path-payment-strict-send"
 *     target="_blank">Operation documentation</a>
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class PathPaymentStrictSendOperationResponse extends PathPaymentBaseOperationResponse {
  @SerializedName("destination_min")
  String destinationMin;
}
