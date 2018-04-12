package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeCreditAlphaNum;
import org.stellar.sdk.responses.OrderBookResponse;

import java.io.IOException;

/**
 * Builds requests connected to order book.
 */
public class OrderBookRequestBuilder extends RequestBuilder {
  public OrderBookRequestBuilder(OkHttpClient httpClient, HttpUrl serverURI) {
    super(httpClient, serverURI, "order_book");
  }

  public OrderBookRequestBuilder buyingAsset(Asset asset) {
    uriBuilder.setQueryParameter("buying_asset_type", asset.getType());
    if (asset instanceof AssetTypeCreditAlphaNum) {
      AssetTypeCreditAlphaNum creditAlphaNumAsset = (AssetTypeCreditAlphaNum) asset;
      uriBuilder.setQueryParameter("buying_asset_code", creditAlphaNumAsset.getCode());
      uriBuilder.setQueryParameter("buying_asset_issuer", creditAlphaNumAsset.getIssuer().getAccountId());
    }
    return this;
  }
  
  public OrderBookRequestBuilder sellingAsset(Asset asset) {
    uriBuilder.setQueryParameter("selling_asset_type", asset.getType());
    if (asset instanceof AssetTypeCreditAlphaNum) {
      AssetTypeCreditAlphaNum creditAlphaNumAsset = (AssetTypeCreditAlphaNum) asset;
      uriBuilder.setQueryParameter("selling_asset_code", creditAlphaNumAsset.getCode());
      uriBuilder.setQueryParameter("selling_asset_issuer", creditAlphaNumAsset.getIssuer().getAccountId());
    }
    return this;
  }

  public static OrderBookResponse execute(OkHttpClient httpClient, HttpUrl uri) throws IOException, TooManyRequestsException {
    TypeToken type = new TypeToken<OrderBookResponse>() {};
    ResponseHandler<OrderBookResponse> responseHandler = new ResponseHandler<OrderBookResponse>(type);

    Request request = new Request.Builder().get().url(uri).build();
    Response response = httpClient.newCall(request).execute();

    return responseHandler.handleResponse(response);
  }

  public OrderBookResponse execute() throws IOException, TooManyRequestsException {
    return this.execute(this.httpClient, this.buildUri());
  }

  @Override
  public RequestBuilder cursor(String cursor) {
    throw new RuntimeException("Not implemented yet.");
  }

  @Override
  public RequestBuilder limit(int number) {
    throw new RuntimeException("Not implemented yet.");
  }

  @Override
  public RequestBuilder order(Order direction) {
    throw new RuntimeException("Not implemented yet.");
  }
}
