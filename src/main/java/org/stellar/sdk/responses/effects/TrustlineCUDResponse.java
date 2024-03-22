package org.stellar.sdk.responses.effects;

import static org.stellar.sdk.Asset.create;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeNative;

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

  public Asset getAsset() {
    if (assetType.equals("native")) {
      return new AssetTypeNative();
    } else {
      return create(assetType, assetCode, assetIssuer, liquidityPoolId);
    }
  }
}
