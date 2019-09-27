package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeNative;

import java.util.List;

/**
 * Represents PATH_PAYMENT_STRICT_SEND operation response.
 *
 * @see <a href="https://www.stellar.org/developers/horizon/reference/resources/operation.html" target="_blank">Operation documentation</a>
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
public class PathPaymentStrictSendOperationResponse extends PathPaymentBaseOperationResponse {
  @SerializedName("destination_min")
  protected final String destinationMin;

  public PathPaymentStrictSendOperationResponse(String amount, String sourceAmount, String destinationMin, String from, String to, String assetType, String assetCode, String assetIssuer, String sourceAssetType, String sourceAssetCode, String sourceAssetIssuer, List<Asset> path) {
    super(amount, sourceAmount, from, to, assetType, assetCode, assetIssuer, sourceAssetType, sourceAssetCode, sourceAssetIssuer, path);
    this.destinationMin = destinationMin;
  }

  public String getDestinationMin() {
    return destinationMin;
  }
}
