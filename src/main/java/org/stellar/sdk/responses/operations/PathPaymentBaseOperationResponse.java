package org.stellar.sdk.responses.operations;

import static org.stellar.sdk.Asset.create;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.stellar.sdk.Asset;

/**
 * Base class for operations that represent a path payment.
 *
 * @see PathPaymentStrictReceiveOperationResponse
 * @see PathPaymentStrictSendOperationResponse
 */
@Getter
@ToString
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
    return create(assetType, assetCode, assetIssuer);
  }

  public Asset getSourceAsset() {
    return create(sourceAssetType, sourceAssetCode, sourceAssetIssuer);
  }
}
