package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.stellar.sdk.Util;
import org.stellar.sdk.exception.ConnectionErrorException;
import org.stellar.sdk.exception.TooManyRequestsException;
import org.stellar.sdk.responses.AssetResponse;
import org.stellar.sdk.responses.Page;

public class AssetsRequestBuilder extends RequestBuilder {
  public AssetsRequestBuilder(OkHttpClient httpClient, HttpUrl serverURI) {
    super(httpClient, serverURI, "assets");
  }

  public AssetsRequestBuilder assetCode(String assetCode) {
    uriBuilder.setQueryParameter("asset_code", assetCode);
    return this;
  }

  public AssetsRequestBuilder assetIssuer(String assetIssuer) {
    uriBuilder.setQueryParameter("asset_issuer", assetIssuer);
    return this;
  }

  /**
   * Requests specific <code>uri</code> and returns {@link Page} of {@link AssetResponse}. This *
   * method is helpful for getting the next set of results.
   *
   * @param httpClient {@link OkHttpClient} to use to send the request.
   * @param uri {@link HttpUrl} URI to send the request to.
   * @return {@link Page} of {@link AssetResponse}
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws ConnectionErrorException if the request fails due to an IOException, including but not
   *     limited to a timeout, connection failure etc.
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   */
  public static Page<AssetResponse> execute(OkHttpClient httpClient, HttpUrl uri) {
    TypeToken<Page<AssetResponse>> type = new TypeToken<Page<AssetResponse>>() {};
    return Util.executeGetRequest(httpClient, uri, type);
  }

  /**
   * Build and execute request.
   *
   * @return {@link Page} of {@link AssetResponse}
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws ConnectionErrorException if the request fails due to an IOException, including but not
   *     limited to a timeout, connection failure etc.
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   */
  public Page<AssetResponse> execute() {
    return execute(this.httpClient, this.buildUri());
  }

  @Override
  public AssetsRequestBuilder cursor(String token) {
    super.cursor(token);
    return this;
  }

  @Override
  public AssetsRequestBuilder limit(int number) {
    super.limit(number);
    return this;
  }

  @Override
  public AssetsRequestBuilder order(Order direction) {
    super.order(direction);
    return this;
  }
}
