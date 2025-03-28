package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import java.net.URI;
import org.stellar.sdk.exception.ConnectionErrorException;
import org.stellar.sdk.exception.TooManyRequestsException;
import org.stellar.sdk.http.IHttpClient;
import org.stellar.sdk.http.sse.ISseClient;
import org.stellar.sdk.responses.AssetResponse;
import org.stellar.sdk.responses.Page;

public class AssetsRequestBuilder extends RequestBuilder {
  public AssetsRequestBuilder(IHttpClient httpClient, ISseClient sseClient, URI serverURI) {
    super(httpClient, sseClient, serverURI, "assets");
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
   * @param httpClient {@link IHttpClient} to use to send the request.
   * @param uri {@link URI} URI to send the request to.
   * @return {@link Page} of {@link AssetResponse}
   * @throws org.stellar.sdk.exception.NetworkException All the exceptions below are subclasses of
   *     NetworkError
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws TooManyRequestsException if the request fails due to too many requests sent to the
   *     server
   * @throws org.stellar.sdk.exception.RequestTimeoutException When Horizon returns a <code>Timeout
   *     </code> or connection timeout occurred
   * @throws org.stellar.sdk.exception.UnknownResponseException if the server returns an unknown
   *     status code
   * @throws ConnectionErrorException When the request cannot be executed due to cancellation or
   *     connectivity problems, etc.
   */
  public static Page<AssetResponse> execute(IHttpClient httpClient, URI uri) {
    TypeToken<Page<AssetResponse>> type = new TypeToken<Page<AssetResponse>>() {};
    return executeGetRequest(httpClient, uri, type);
  }

  /**
   * Build and execute request.
   *
   * @return {@link Page} of {@link AssetResponse}
   * @throws org.stellar.sdk.exception.NetworkException All the exceptions below are subclasses of
   *     NetworkError
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws TooManyRequestsException if the request fails due to too many requests sent to the
   *     server
   * @throws org.stellar.sdk.exception.RequestTimeoutException When Horizon returns a <code>Timeout
   *     </code> or connection timeout occurred
   * @throws org.stellar.sdk.exception.UnknownResponseException if the server returns an unknown
   *     status code
   * @throws ConnectionErrorException When the request cannot be executed due to cancellation or
   *     connectivity problems, etc.
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
