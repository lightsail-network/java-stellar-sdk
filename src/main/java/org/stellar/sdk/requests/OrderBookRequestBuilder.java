package org.stellar.sdk.requests;

import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeCreditAlphaNum;

import java.net.URI;

/**
 * Builds requests connected to order book.
 */
public class OrderBookRequestBuilder extends RequestBuilder {
  public OrderBookRequestBuilder(URI serverURI) {
    super(serverURI, "order_book");
  }

  public OrderBookRequestBuilder buyingAsset(Asset asset) {
    uriBuilder.addParameter("buying_asset_type", asset.getType());
    if (asset instanceof AssetTypeCreditAlphaNum) {
      AssetTypeCreditAlphaNum creditAlphaNumAsset = (AssetTypeCreditAlphaNum) asset;
      uriBuilder.addParameter("buying_asset_code", creditAlphaNumAsset.getCode());
      uriBuilder.addParameter("buying_asset_issuer", creditAlphaNumAsset.getIssuer().getAccountId());
    }
    return this;
  }
  
  public OrderBookRequestBuilder sellingAsset(Asset asset) {
    uriBuilder.addParameter("selling_asset_type", asset.getType());
    if (asset instanceof AssetTypeCreditAlphaNum) {
      AssetTypeCreditAlphaNum creditAlphaNumAsset = (AssetTypeCreditAlphaNum) asset;
      uriBuilder.addParameter("selling_asset_code", creditAlphaNumAsset.getCode());
      uriBuilder.addParameter("selling_asset_issuer", creditAlphaNumAsset.getIssuer().getAccountId());
    }
    return this;
  }

  public OrderBookRequestBuilder trades() {
    this.setSegments("order_book", "trades");
    return this;
  }

  public void execute() {
    throw new RuntimeException("Not implemented yet.");
  }
}
