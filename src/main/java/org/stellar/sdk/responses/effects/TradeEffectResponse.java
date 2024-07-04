package org.stellar.sdk.responses.effects;

import static org.stellar.sdk.Asset.create;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.Asset;
import org.stellar.sdk.responses.MuxedAccount;

/**
 * Represents trade effect response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/effects/" target="_blank">Effect
 *     documentation</a>
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

  // TODO
  public Optional<MuxedAccount> getSellerMuxed() {
    if (this.sellerMuxed == null || this.sellerMuxed.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(new MuxedAccount(this.sellerMuxed, this.seller, this.sellerMuxedId));
  }

  public Asset getSoldAsset() {
    return create(soldAssetType, soldAssetCode, soldAssetIssuer);
  }

  public Asset getBoughtAsset() {
    return create(boughtAssetType, boughtAssetCode, boughtAssetIssuer);
  }
}
