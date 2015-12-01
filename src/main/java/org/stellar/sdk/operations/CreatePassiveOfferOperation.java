package org.stellar.sdk.operations;

/**
 * Represents CreatePassiveOffer operation response.
 */
public class CreatePassiveOfferOperation extends ManageOfferOperation {
  CreatePassiveOfferOperation(Integer offerId, String amount, String price, String buyingAssetType, String buyingAssetCode, String buyingAssetIssuer, String sellingAssetType, String sellingAssetCode, String sellingAssetIssuer) {
    super(offerId, amount, price, buyingAssetType, buyingAssetCode, buyingAssetIssuer, sellingAssetType, sellingAssetCode, sellingAssetIssuer);
  }
}
