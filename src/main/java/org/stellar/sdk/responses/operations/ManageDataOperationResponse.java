package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.Base64Factory;

/**
 * Represents ManageDataoperation response.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/data/apis/horizon/api-reference/resources/operations/object/manage-data"
 *     target="_blank">Operation documentation</a>
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

  /**
   * Gets decoded value for the base64 encoded data value.
   *
   * @return decoded value
   */
  public byte[] getDecodedValue() {
    return Base64Factory.getInstance().decode(value);
  }
}
