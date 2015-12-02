package org.stellar.sdk;

import com.google.gson.annotations.SerializedName;

import org.stellar.base.Asset;
import org.stellar.base.AssetTypeNative;
import org.stellar.base.Keypair;

import java.util.ArrayList;

/**
 * Represents path response.
 */
public class Path {
  @SerializedName("destination_amount")
  private final String destinationAmount;
  @SerializedName("destination_asset_type")
  private final String destinationAssetType;
  @SerializedName("destination_asset_code")
  private final String destinationAssetCode;
  @SerializedName("destination_asset_issuer")
  private final String destinationAssetIssuer;

  @SerializedName("source_amount")
  private final String sourceAmount;
  @SerializedName("source_asset_type")
  private final String sourceAssetType;
  @SerializedName("source_asset_code")
  private final String sourceAssetCode;
  @SerializedName("source_asset_issuer")
  private final String sourceAssetIssuer;

  @SerializedName("path")
  private final ArrayList<Asset> path;

  @SerializedName("_links")
  private final Links links;

  Path(String destinationAmount, String destinationAssetType, String destinationAssetCode, String destinationAssetIssuer, String sourceAmount, String sourceAssetType, String sourceAssetCode, String sourceAssetIssuer, ArrayList<Asset> path, Links links) {
    this.destinationAmount = destinationAmount;
    this.destinationAssetType = destinationAssetType;
    this.destinationAssetCode = destinationAssetCode;
    this.destinationAssetIssuer = destinationAssetIssuer;
    this.sourceAmount = sourceAmount;
    this.sourceAssetType = sourceAssetType;
    this.sourceAssetCode = sourceAssetCode;
    this.sourceAssetIssuer = sourceAssetIssuer;
    this.path = path;
    this.links = links;
  }

  public String getDestinationAmount() {
    return destinationAmount;
  }

  public String getSourceAmount() {
    return sourceAmount;
  }

  public ArrayList<Asset> getPath() {
    return path;
  }

  public Asset getDestinationAsset() {
    if (destinationAssetType.equals("native")) {
      return new AssetTypeNative();
    } else {
      Keypair issuer = Keypair.fromAddress(destinationAssetIssuer);
      return Asset.createNonNativeAsset(destinationAssetCode, issuer);
    }
  }

  public Asset getSourceAsset() {
    if (sourceAssetType.equals("native")) {
      return new AssetTypeNative();
    } else {
      Keypair issuer = Keypair.fromAddress(sourceAssetIssuer);
      return Asset.createNonNativeAsset(sourceAssetCode, issuer);
    }
  }

  public Links getLinks() {
    return links;
  }

  /**
   * Links connected to path.
   */
  public static class Links {
    @SerializedName("self")
    private final Link self;

    Links(Link self) {
      this.self = self;
    }

    public Link getSelf() {
      return self;
    }
  }
}
