package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.stellar.sdk.TrustLineAsset;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter
@ToString(callSuper = true)
abstract class TrustlineCUDResponse extends EffectResponse {
  @SerializedName("limit")
  private final String limit;

  @SerializedName("asset_type")
  private final String assetType;

  @SerializedName("asset_code")
  private final String assetCode;

  @SerializedName("asset_issuer")
  private final String assetIssuer;

  @SerializedName("liquidity_pool_id")
  private final String liquidityPoolId;

  // TODO: get asset
  public TrustLineAsset getTrustLineAsset() {
    return getTrustLineAsset(assetType, assetCode, assetIssuer, liquidityPoolId);
  }
}
