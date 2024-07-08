package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Represents ExtendFootprintTTL operation response.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/data/horizon/api-reference/resources/operations/object/extend-footprint-ttl"
 *     target="_blank">Operation documentation</a>
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class ExtendFootprintTTLOperationResponse extends OperationResponse {
  @SerializedName("extend_to")
  Long extendTo;
}
