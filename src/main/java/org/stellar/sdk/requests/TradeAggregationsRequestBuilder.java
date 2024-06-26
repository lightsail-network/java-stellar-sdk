package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeCreditAlphaNum;
import org.stellar.sdk.Util;
import org.stellar.sdk.exception.TooManyRequestsException;
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

  /**
   * Requests specific <code>uri</code> and returns {@link Page} of {@link
   * TradeAggregationResponse}.
   *
   * @param httpClient {@link OkHttpClient} to use to send the request.
   * @param uri {@link HttpUrl} URI to send the request to.
   * @return {@link Page} of {@link TradeAggregationResponse}
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws org.stellar.sdk.exception.ConnectionErrorException if the request fails due to an
   *     IOException, including but not limited to a timeout, connection failure etc.
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   */
  public static Page<TradeAggregationResponse> execute(OkHttpClient httpClient, HttpUrl uri) {
    TypeToken<Page<TradeAggregationResponse>> type =
        new TypeToken<Page<TradeAggregationResponse>>() {};
    return Util.executeGetRequest(httpClient, uri, type);
  }

  public Page<TradeAggregationResponse> execute() {
    return execute(this.httpClient, this.buildUri());
  }
}
