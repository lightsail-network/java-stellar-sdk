package org.stellar.sdk.responses;

import static org.stellar.sdk.Asset.create;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.Asset;

/**
 * Represents path response.
 *
 * @see <a href="https://developers.stellar.org/api/aggregations/paths/" target="_blank">Path
 *     documentation</a>
 * @see org.stellar.sdk.requests.StrictReceivePathsRequestBuilder
 * @see org.stellar.sdk.requests.StrictSendPathsRequestBuilder
 * @see org.stellar.sdk.Server#strictReceivePaths()
 * @see org.stellar.sdk.Server#strictSendPaths()
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class PathResponse extends Response {
  @SerializedName("destination_amount")
  String destinationAmount;

  @SerializedName("destination_asset_type")
  String destinationAssetType;

  @SerializedName("destination_asset_code")
  String destinationAssetCode;

  @SerializedName("destination_asset_issuer")
  String destinationAssetIssuer;

  @SerializedName("source_amount")
  String sourceAmount;

  @SerializedName("source_asset_type")
  String sourceAssetType;

  @SerializedName("source_asset_code")
  String sourceAssetCode;

  @SerializedName("source_asset_issuer")
  String sourceAssetIssuer;

  @SerializedName("path")
  ArrayList<Asset> path;

  @SerializedName("_links")
  Links links;

  public Asset getDestinationAsset() {
    return create(destinationAssetType, destinationAssetCode, destinationAssetIssuer);
  }

  public Asset getSourceAsset() {
    return create(sourceAssetType, sourceAssetCode, sourceAssetIssuer);
  }

  /** Links connected to path. */
  @Value
  public static class Links {
    @SerializedName("self")
    Link self;
  }
}
