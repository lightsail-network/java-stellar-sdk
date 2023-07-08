package org.stellar.sdk.responses.operations;

import static org.stellar.sdk.Asset.create;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.List;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeNative;
import org.stellar.sdk.responses.MuxedAccount;

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
  private ImmutableList<Asset> path;

  public String getAmount() {
    return amount;
  }

  public String getSourceAmount() {
    return sourceAmount;
  }

  public String getFrom() {
    return from;
  }

  public Optional<MuxedAccount> getFromMuxed() {
    if (this.fromMuxed == null || this.fromMuxed.isEmpty()) {
      return Optional.absent();
    }
    return Optional.of(new MuxedAccount(this.fromMuxed, this.from, this.fromMuxedId));
  }

  public Optional<MuxedAccount> getToMuxed() {
    if (this.toMuxed == null || this.toMuxed.isEmpty()) {
      return Optional.absent();
    }
    return Optional.of(new MuxedAccount(this.toMuxed, this.to, this.toMuxedId));
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
