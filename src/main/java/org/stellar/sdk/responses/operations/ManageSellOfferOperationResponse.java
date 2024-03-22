package org.stellar.sdk.responses.operations;

import static org.stellar.sdk.Asset.create;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeNative;

/**
 * Represents ManageSellOffer operation response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/operations/" target="_blank">Operation
 *     documentation</a>
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class ManageSellOfferOperationResponse extends OperationResponse {
  @SerializedName("offer_id")
  Long offerId;

  @SerializedName("amount")
  String amount;

  // Price is not implemented yet in horizon
  @SerializedName("price")
  String price;

  @SerializedName("buying_asset_type")
  String buyingAssetType;

  @SerializedName("buying_asset_code")
  String buyingAssetCode;

  @SerializedName("buying_asset_issuer")
  String buyingAssetIssuer;

  @SerializedName("selling_asset_type")
  String sellingAssetType;

  @SerializedName("selling_asset_code")
  String sellingAssetCode;

  @SerializedName("selling_asset_issuer")
  String sellingAssetIssuer;

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
