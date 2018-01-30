package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;

import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeNative;
import org.stellar.sdk.KeyPair;

/**
 * Represents trade effect response.
 * @see <a href="https://www.stellar.org/developers/horizon/reference/resources/effect.html" target="_blank">Effect documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
public class TradeEffectResponse extends EffectResponse {
  @SerializedName("seller")
  protected final KeyPair seller;
  @SerializedName("offer_id")
  protected final Long offerId;
  
  @SerializedName("sold_amount")
  protected final String soldAmount;
  @SerializedName("sold_asset_type")
  protected final String soldAssetType;
  @SerializedName("sold_asset_code")
  protected final String soldAssetCode;
  @SerializedName("sold_asset_issuer")
  protected final String soldAssetIssuer;

  @SerializedName("bought_amount")
  protected final String boughtAmount;
  @SerializedName("bought_asset_type")
  protected final String boughtAssetType;
  @SerializedName("bought_asset_code")
  protected final String boughtAssetCode;
  @SerializedName("bought_asset_issuer")
  protected final String boughtAssetIssuer;

  TradeEffectResponse(KeyPair seller, Long offerId, String soldAmount, String soldAssetType, String soldAssetCode, String soldAssetIssuer, String boughtAmount, String boughtAssetType, String boughtAssetCode, String boughtAssetIssuer) {
    this.seller = seller;
    this.offerId = offerId;
    this.soldAmount = soldAmount;
    this.soldAssetType = soldAssetType;
    this.soldAssetCode = soldAssetCode;
    this.soldAssetIssuer = soldAssetIssuer;
    this.boughtAmount = boughtAmount;
    this.boughtAssetType = boughtAssetType;
    this.boughtAssetCode = boughtAssetCode;
    this.boughtAssetIssuer = boughtAssetIssuer;
  }

  public KeyPair getSeller() {
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
    if (soldAssetType.equals("native")) {
      return new AssetTypeNative();
    } else {
      KeyPair issuer = KeyPair.fromAccountId(soldAssetIssuer);
      return Asset.createNonNativeAsset(soldAssetCode, issuer);
    }
  }

  public Asset getBoughtAsset() {
    if (boughtAssetType.equals("native")) {
      return new AssetTypeNative();
    } else {
      KeyPair issuer = KeyPair.fromAccountId(boughtAssetIssuer);
      return Asset.createNonNativeAsset(boughtAssetCode, issuer);
    }
  }
}
