package org.stellar.sdk.responses.effects;

import static org.stellar.sdk.Asset.create;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.Asset;

/**
 * Represents trade effect response.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/data/apis/horizon/api-reference/resources/effects"
 *     target="_blank">Effect documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class TradeEffectResponse extends EffectResponse {
  @SerializedName("seller")
  String seller;

  @SerializedName("seller_muxed")
  String sellerMuxed;

  @SerializedName("seller_muxed_id")
  BigInteger sellerMuxedId;

  @SerializedName("offer_id")
  Long offerId;

  @SerializedName("sold_amount")
  String soldAmount;

  @SerializedName("sold_asset_type")
  String soldAssetType;

  @SerializedName("sold_asset_code")
  String soldAssetCode;

  @SerializedName("sold_asset_issuer")
  String soldAssetIssuer;

  @SerializedName("bought_amount")
  String boughtAmount;

  @SerializedName("bought_asset_type")
  String boughtAssetType;

  @SerializedName("bought_asset_code")
  String boughtAssetCode;

  @SerializedName("bought_asset_issuer")
  String boughtAssetIssuer;

  public Asset getSoldAsset() {
    return create(soldAssetType, soldAssetCode, soldAssetIssuer);
  }

  public Asset getBoughtAsset() {
    return create(boughtAssetType, boughtAssetCode, boughtAssetIssuer);
  }
}
