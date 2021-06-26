package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;
import org.stellar.sdk.Asset;

import java.util.List;

/**
 * Represents PATH_PAYMENT_STRICT_RECEIVE operation response.
 *
 * @see <a href="https://www.stellar.org/developers/horizon/reference/resources/operation.html" target="_blank">Operation documentation</a>
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
public class PathPaymentStrictReceiveOperationResponse extends PathPaymentBaseOperationResponse {
  @SerializedName("source_max")
  private String sourceMax;


  public String getSourceMax() {
    return sourceMax;
  }
}