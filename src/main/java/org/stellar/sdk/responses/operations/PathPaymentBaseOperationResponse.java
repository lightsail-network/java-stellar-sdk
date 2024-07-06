package org.stellar.sdk.responses.operations;

import static org.stellar.sdk.Asset.create;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeNative;

@Getter
@EqualsAndHashCode(callSuper = false)
public abstract class PathPaymentBaseOperationResponse extends OperationResponse {
  @SerializedName("amount")
  private String amount;

  @SerializedName("source_amount")
  private String sourceAmount;

  @SerializedName("from")
  private String from;

  @SerializedName("from_muxed")
  private String fromMuxed;

  @SerializedName("from_muxed_id")
  private BigInteger fromMuxedId;

  @SerializedName("to")
  private String to;

  @SerializedName("to_muxed")
  private String toMuxed;

  @SerializedName("to_muxed_id")
  private BigInteger toMuxedId;

  @SerializedName("asset_type")
  private String assetType;

  @SerializedName("asset_code")
  private String assetCode;

  @SerializedName("asset_issuer")
  private String assetIssuer;

  @SerializedName("source_asset_type")
  private String sourceAssetType;

  @SerializedName("source_asset_code")
  private String sourceAssetCode;

  @SerializedName("source_asset_issuer")
  private String sourceAssetIssuer;

  @SerializedName("path")
  private List<Asset> path;

  public Asset getAsset() {
    if (assetType.equals("native")) {
      return new AssetTypeNative();
    } else {
      return create(assetType, assetCode, assetIssuer);
    }
  }

  public Asset getSourceAsset() {
    if (sourceAssetType.equals("native")) {
      return new AssetTypeNative();
    } else {
      return create(sourceAssetType, sourceAssetCode, sourceAssetIssuer);
    }
  }
}
