package org.stellar.sdk.responses.operations;

import static org.stellar.sdk.Asset.create;

import com.google.gson.annotations.SerializedName;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeNative;

/**
 * Represents CreatePassiveSellOffer operation response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/operations/" target="_blank">Operation
 *     documentation</a>
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
public class CreatePassiveSellOfferOperationResponse extends OperationResponse {
  @SerializedName("offer_id")
  protected final Long offerId;

  @SerializedName("amount")
  protected final String amount;

  // Price is not implemented yet in horizon
  @SerializedName("price")
  protected final String price;

  @SerializedName("buying_asset_type")
  protected final String buyingAssetType;

  @SerializedName("buying_asset_code")
  protected final String buyingAssetCode;

  @SerializedName("buying_asset_issuer")
  protected final String buyingAssetIssuer;

  @SerializedName("selling_asset_type")
  protected final String sellingAssetType;

  @SerializedName("selling_asset_code")
  protected final String sellingAssetCode;

  @SerializedName("selling_asset_issuer")
  protected final String sellingAssetIssuer;

  CreatePassiveSellOfferOperationResponse(
      Long offerId,
      String amount,
      String price,
      String buyingAssetType,
      String buyingAssetCode,
      String buyingAssetIssuer,
      String sellingAssetType,
      String sellingAssetCode,
      String sellingAssetIssuer) {
    this.offerId = offerId;
    this.amount = amount;
    this.price = price;
    this.buyingAssetType = buyingAssetType;
    this.buyingAssetCode = buyingAssetCode;
    this.buyingAssetIssuer = buyingAssetIssuer;
    this.sellingAssetType = sellingAssetType;
    this.sellingAssetCode = sellingAssetCode;
    this.sellingAssetIssuer = sellingAssetIssuer;
  }

  public Long getOfferId() {
    return offerId;
  }

  public String getAmount() {
    return amount;
  }

  public String getPrice() {
    return price;
  }

  public Asset getBuyingAsset() {
    if (buyingAssetType.equals("native")) {
      return new AssetTypeNative();
    } else {
      return create(buyingAssetType, buyingAssetCode, buyingAssetIssuer);
    }
  }

  public Asset getSellingAsset() {
    if (sellingAssetType.equals("native")) {
      return new AssetTypeNative();
    } else {
      return create(sellingAssetType, sellingAssetCode, sellingAssetIssuer);
    }
  }
}
