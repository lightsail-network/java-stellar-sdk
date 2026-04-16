package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import java.util.List;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeCreditAlphaNum;
import org.stellar.sdk.exception.TooManyRequestsException;
import org.stellar.sdk.responses.Page;
import org.stellar.sdk.responses.PathResponse;

/**
 * Builds requests to the Horizon {@code /paths/strict-send} endpoint.
 *
 * <p>Finds payment paths where the source amount is fixed. Specify either a destination account or
 * a set of destination assets, along with the source asset and amount, to discover available
 * payment paths.
 *
 * @see <a href="https://developers.stellar.org/docs/data/apis/horizon/api-reference">Horizon API
 *     reference</a>
 */
public class StrictSendPathsRequestBuilder extends RequestBuilder {
  public StrictSendPathsRequestBuilder(OkHttpClient httpClient, HttpUrl serverURI) {
    super(httpClient, serverURI, "");
    this.setSegments("paths", "strict-send");
  }

  /**
   * Sets the destination account whose assets will be accepted as the end point for path finding.
   * Mutually exclusive with {@link #destinationAssets(List)}.
   *
   * @param account the destination account ID
   * @return this builder instance for chaining
   * @throws IllegalArgumentException if {@code destination_assets} has already been set
   */
  public StrictSendPathsRequestBuilder destinationAccount(String account) {
    if (uriBuilder.build().queryParameter("destination_assets") != null) {
      throw new IllegalArgumentException(
          "cannot set both destination_assets and destination_account");
    }
    uriBuilder.setQueryParameter("destination_account", account);
    return this;
  }

  /**
   * Sets the destination assets to consider as the end point for path finding. Mutually exclusive
   * with {@link #destinationAccount(String)}.
   *
   * @param assets the list of destination assets
   * @return this builder instance for chaining
   * @throws IllegalArgumentException if {@code destination_account} has already been set
   */
  public StrictSendPathsRequestBuilder destinationAssets(List<Asset> assets) {
    if (uriBuilder.build().queryParameter("destination_account") != null) {
      throw new IllegalArgumentException(
          "cannot set both destination_assets and destination_account");
    }
    setAssetsParameter("destination_assets", assets);
    return this;
  }

  /**
   * Sets the fixed source amount the sender wants to spend.
   *
   * @param amount the source amount
   * @return this builder instance for chaining
   */
  public StrictSendPathsRequestBuilder sourceAmount(String amount) {
    uriBuilder.setQueryParameter("source_amount", amount);
    return this;
  }

  /**
   * Sets the asset the sender wants to send.
   *
   * @param asset the source asset
   * @return this builder instance for chaining
   */
  public StrictSendPathsRequestBuilder sourceAsset(Asset asset) {
    uriBuilder.setQueryParameter("source_asset_type", getAssetType(asset));
    if (asset instanceof AssetTypeCreditAlphaNum) {
      AssetTypeCreditAlphaNum creditAlphaNumAsset = (AssetTypeCreditAlphaNum) asset;
      uriBuilder.setQueryParameter("source_asset_code", creditAlphaNumAsset.getCode());
      uriBuilder.setQueryParameter("source_asset_issuer", creditAlphaNumAsset.getIssuer());
    }
    return this;
  }

  /**
   * Requests specific <code>uri</code> and returns {@link Page} of {@link PathResponse}.
   *
   * @param httpClient {@link OkHttpClient} to use to send the request.
   * @param uri {@link HttpUrl} URI to send the request to.
   * @return {@link Page} of {@link PathResponse}
   * @throws org.stellar.sdk.exception.NetworkException All the exceptions below are subclasses of
   *     NetworkException
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
   * @throws org.stellar.sdk.exception.ConnectionErrorException When the request cannot be executed
   *     due to cancellation or connectivity problems, etc.
   */
  public static Page<PathResponse> execute(OkHttpClient httpClient, HttpUrl uri) {
    TypeToken<Page<PathResponse>> type = new TypeToken<Page<PathResponse>>() {};
    return executeGetRequest(httpClient, uri, type);
  }

  /**
   * Build and execute request.
   *
   * @return {@link Page} of {@link PathResponse}
   * @throws org.stellar.sdk.exception.NetworkException All the exceptions below are subclasses of
   *     NetworkException
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
   * @throws org.stellar.sdk.exception.ConnectionErrorException When the request cannot be executed
   *     due to cancellation or connectivity problems, etc.
   */
  public Page<PathResponse> execute() {
    return execute(this.httpClient, this.buildUri());
  }
}
