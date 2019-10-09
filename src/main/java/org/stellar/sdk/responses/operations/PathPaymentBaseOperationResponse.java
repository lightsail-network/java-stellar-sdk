package org.stellar.sdk.responses.operations;

import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.SerializedName;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeNative;

import java.util.List;

public abstract class PathPaymentBaseOperationResponse extends OperationResponse {
  @SerializedName("amount")
  protected final String amount;
  @SerializedName("source_amount")
  protected final String sourceAmount;
  @SerializedName("from")
  protected final String from;
  @SerializedName("to")
  protected final String to;

  @SerializedName("asset_type")
  protected final String assetType;
  @SerializedName("asset_code")
  protected final String assetCode;
  @SerializedName("asset_issuer")
  protected final String assetIssuer;

  @SerializedName("source_asset_type")
  protected final String sourceAssetType;
  @SerializedName("source_asset_code")
  protected final String sourceAssetCode;
  @SerializedName("source_asset_issuer")
  protected final String sourceAssetIssuer;

  @SerializedName("path")
  protected final List<Asset> path;


  public PathPaymentBaseOperationResponse(String amount, String sourceAmount, String from, String to, String assetType, String assetCode, String assetIssuer, String sourceAssetType, String sourceAssetCode, String sourceAssetIssuer, List<Asset> path) {
    this.amount = amount;
    this.sourceAmount = sourceAmount;
    this.from = from;
    this.to = to;
    this.assetType = assetType;
    this.assetCode = assetCode;
    this.assetIssuer = assetIssuer;
    this.sourceAssetType = sourceAssetType;
    this.sourceAssetCode = sourceAssetCode;
    this.sourceAssetIssuer = sourceAssetIssuer;
    this.path = ImmutableList.copyOf(path);
  }

  public String getAmount() {
    return amount;
  }

  public String getSourceAmount() {
    return sourceAmount;
  }

  public String getFrom() {
    return from;
  }

  public String getTo() {
    return to;
  }

  public List<Asset> getPath() {
    return this.path;
  }

  public Asset getAsset() {
    if (assetType.equals("native")) {
      return new AssetTypeNative();
    } else {
      return Asset.createNonNativeAsset(assetCode, assetIssuer);
    }
  }

  public Asset getSourceAsset() {
    if (sourceAssetType.equals("native")) {
      return new AssetTypeNative();
    } else {
      return Asset.createNonNativeAsset(sourceAssetCode, sourceAssetIssuer);
    }
  }
}
