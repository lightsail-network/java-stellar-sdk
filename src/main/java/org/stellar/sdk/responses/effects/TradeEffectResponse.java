package org.stellar.sdk.responses.effects;

import static org.stellar.sdk.Asset.create;

import com.google.common.base.Optional;
import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
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
public class TradeEffectResponse extends EffectResponse {
  @SerializedName("seller")
  private String seller;

  @SerializedName("seller_muxed")
  private String sellerMuxed;

  @SerializedName("seller_muxed_id")
  private BigInteger sellerMuxedId;

  @SerializedName("offer_id")
  private Long offerId;

  @SerializedName("sold_amount")
  private String soldAmount;

  @SerializedName("sold_asset_type")
  private String soldAssetType;

  @SerializedName("sold_asset_code")
  private String soldAssetCode;

  @SerializedName("sold_asset_issuer")
  private String soldAssetIssuer;

  @SerializedName("bought_amount")
  private String boughtAmount;

  @SerializedName("bought_asset_type")
  private String boughtAssetType;

  @SerializedName("bought_asset_code")
  private String boughtAssetCode;

  @SerializedName("bought_asset_issuer")
  private String boughtAssetIssuer;

  public Optional<MuxedAccount> getSellerMuxed() {
    if (this.sellerMuxed == null || this.sellerMuxed.isEmpty()) {
      return Optional.absent();
    }
    return Optional.of(new MuxedAccount(this.sellerMuxed, this.seller, this.sellerMuxedId));
  }

  public String getSeller() {
    return seller;
  }

  public Long getOfferId() {
    return offerId;
  }

  public String getSoldAmount() {
    return soldAmount;
  }

  public String getBoughtAmount() {
    return boughtAmount;
  }

  public Asset getSoldAsset() {
    return create(soldAssetType, soldAssetCode, soldAssetIssuer);
  }

  public Asset getBoughtAsset() {
    return create(boughtAssetType, boughtAssetCode, boughtAssetIssuer);
  }
}
