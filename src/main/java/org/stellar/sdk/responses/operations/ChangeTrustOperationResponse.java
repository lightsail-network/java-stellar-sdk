package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.TrustLineAsset;

/**
 * Represents ChangeTrust operation response.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/data/horizon/api-reference/resources/operations/object/change-trust"
 *     target="_blank">Operation documentation</a>
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class ChangeTrustOperationResponse extends OperationResponse {
  @SerializedName("trustor")
  String trustor;

  @SerializedName("trustor_muxed")
  String trustorMuxed;

  @SerializedName("trustor_muxed_id")
  BigInteger trustorMuxedId;

  @SerializedName("trustee")
  String trustee;

  @SerializedName("asset_type")
  String assetType;

  @SerializedName("asset_code")
  String assetCode;

  @SerializedName("asset_issuer")
  String assetIssuer;

  @SerializedName("limit")
  String limit;

  @SerializedName("liquidity_pool_id")
  String liquidityPoolId;

  public TrustLineAsset getTrustLineAsset() {
    return getTrustLineAsset(assetType, assetCode, assetIssuer, liquidityPoolId);
  }
}
