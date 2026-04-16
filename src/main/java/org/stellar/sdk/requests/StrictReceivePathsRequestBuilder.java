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
 * Builds requests to the Horizon {@code /paths/strict-receive} endpoint.
 *
 * <p>Finds payment paths where the destination amount is fixed. Specify either a source account or
 * a set of source assets, along with the destination asset and amount, to discover available
 * payment paths.
 *
 * @see <a href="https://developers.stellar.org/docs/data/apis/horizon/api-reference">Horizon API
 *     reference</a>
 */
public class StrictReceivePathsRequestBuilder extends RequestBuilder {
  public StrictReceivePathsRequestBuilder(OkHttpClient httpClient, HttpUrl serverURI) {
    super(httpClient, serverURI, "");
    this.setSegments("paths", "strict-receive");
  }

  /**
   * Sets the destination account for the path payment.
   *
   * @param account the destination account ID
   * @return this builder instance for chaining
   */
  public StrictReceivePathsRequestBuilder destinationAccount(String account) {
    uriBuilder.setQueryParameter("destination_account", account);
    return this;
  }

  /**
   * Sets the source account whose assets will be used as the starting point for path finding.
   * Mutually exclusive with {@link #sourceAssets(List)}.
   *
   * @param account the source account ID
   * @return this builder instance for chaining
   * @throws IllegalArgumentException if {@code source_assets} has already been set
   */
  public StrictReceivePathsRequestBuilder sourceAccount(String account) {
    if (uriBuilder.build().queryParameter("source_assets") != null) {
      throw new IllegalArgumentException("cannot set both source_assets and source_account");
    }
    uriBuilder.setQueryParameter("source_account", account);
    return this;
  }

  /**
   * Sets the source assets to consider as the starting point for path finding. Mutually exclusive
   * with {@link #sourceAccount(String)}.
   *
   * @param assets the list of source assets
   * @return this builder instance for chaining
   * @throws IllegalArgumentException if {@code source_account} has already been set
   */
  public StrictReceivePathsRequestBuilder sourceAssets(List<Asset> assets) {
    if (uriBuilder.build().queryParameter("source_account") != null) {
      throw new IllegalArgumentException("cannot set both source_assets and source_account");
    }
    setAssetsParameter("source_assets", assets);
    return this;
  }

  /**
   * Sets the fixed destination amount the recipient should receive.
   *
   * @param amount the destination amount
   * @return this builder instance for chaining
   */
  public StrictReceivePathsRequestBuilder destinationAmount(String amount) {
    uriBuilder.setQueryParameter("destination_amount", amount);
    return this;
  }

  /**
   * Sets the asset the recipient wants to receive.
   *
   * @param asset the destination asset
   * @return this builder instance for chaining
   */
  public StrictReceivePathsRequestBuilder destinationAsset(Asset asset) {
    uriBuilder.setQueryParameter("destination_asset_type", getAssetType(asset));
    if (asset instanceof AssetTypeCreditAlphaNum) {
      AssetTypeCreditAlphaNum creditAlphaNumAsset = (AssetTypeCreditAlphaNum) asset;
      uriBuilder.setQueryParameter("destination_asset_code", creditAlphaNumAsset.getCode());
      uriBuilder.setQueryParameter("destination_asset_issuer", creditAlphaNumAsset.getIssuer());
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
