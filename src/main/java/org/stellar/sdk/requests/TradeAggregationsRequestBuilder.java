package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeCreditAlphaNum;
import org.stellar.sdk.responses.Page;
import org.stellar.sdk.responses.TradeAggregationResponse;

/** Builds requests connected to trades. */
public class TradeAggregationsRequestBuilder extends RequestBuilder {
  public TradeAggregationsRequestBuilder(
      OkHttpClient httpClient,
      HttpUrl serverURI,
      Asset baseAsset,
      Asset counterAsset,
      long startTime,
      long endTime,
      long resolution,
      long offset) {
    super(httpClient, serverURI, "trade_aggregations");

    this.baseAsset(baseAsset);
    this.counterAsset(counterAsset);
    uriBuilder.setQueryParameter("start_time", String.valueOf(startTime));
    uriBuilder.setQueryParameter("end_time", String.valueOf(endTime));
    uriBuilder.setQueryParameter("resolution", String.valueOf(resolution));
    uriBuilder.setQueryParameter("offset", String.valueOf(offset));
  }

  private void baseAsset(Asset asset) {
    uriBuilder.setQueryParameter("base_asset_type", asset.getType());
    if (asset instanceof AssetTypeCreditAlphaNum) {
      AssetTypeCreditAlphaNum creditAlphaNumAsset = (AssetTypeCreditAlphaNum) asset;
      uriBuilder.setQueryParameter("base_asset_code", creditAlphaNumAsset.getCode());
      uriBuilder.setQueryParameter("base_asset_issuer", creditAlphaNumAsset.getIssuer());
    }
  }

  private void counterAsset(Asset asset) {
    uriBuilder.setQueryParameter("counter_asset_type", asset.getType());
    if (asset instanceof AssetTypeCreditAlphaNum) {
      AssetTypeCreditAlphaNum creditAlphaNumAsset = (AssetTypeCreditAlphaNum) asset;
      uriBuilder.setQueryParameter("counter_asset_code", creditAlphaNumAsset.getCode());
      uriBuilder.setQueryParameter("counter_asset_issuer", creditAlphaNumAsset.getIssuer());
    }
  }

  public static Page<TradeAggregationResponse> execute(OkHttpClient httpClient, HttpUrl uri)
      throws IOException, TooManyRequestsException {
    TypeToken type = new TypeToken<Page<TradeAggregationResponse>>() {};
    ResponseHandler<Page<TradeAggregationResponse>> responseHandler =
        new ResponseHandler<Page<TradeAggregationResponse>>(type);

    Request request = new Request.Builder().get().url(uri).build();
    Response response = httpClient.newCall(request).execute();

    return responseHandler.handleResponse(response);
  }

  public Page<TradeAggregationResponse> execute() throws IOException, TooManyRequestsException {
    return this.execute(this.httpClient, this.buildUri());
  }
}
