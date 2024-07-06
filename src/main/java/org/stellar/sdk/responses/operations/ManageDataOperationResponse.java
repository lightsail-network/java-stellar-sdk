package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Represents ManageDataoperation response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/operations/" target="_blank">Operation
 *     documentation</a>
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class ManageDataOperationResponse extends OperationResponse {
  @SerializedName("name")
  String name;

  @SerializedName("value")
  String value;

  // TODO: decode value
}
