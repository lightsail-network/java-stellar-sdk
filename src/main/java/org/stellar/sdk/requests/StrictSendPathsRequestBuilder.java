package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import java.util.List;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeCreditAlphaNum;
import org.stellar.sdk.Util;
import org.stellar.sdk.exception.TooManyRequestsException;
import org.stellar.sdk.responses.Page;
import org.stellar.sdk.responses.PathResponse;

public class StrictSendPathsRequestBuilder extends RequestBuilder {
  public StrictSendPathsRequestBuilder(OkHttpClient httpClient, HttpUrl serverURI) {
    super(httpClient, serverURI, "");
    this.setSegments("paths", "strict-send");
  }

  public StrictSendPathsRequestBuilder destinationAccount(String account) {
    if (uriBuilder.build().queryParameter("destination_assets") != null) {
      throw new RuntimeException("cannot set both destination_assets and destination_account");
    }
    uriBuilder.setQueryParameter("destination_account", account);
    return this;
  }

  public StrictSendPathsRequestBuilder destinationAssets(List<Asset> assets) {
    if (uriBuilder.build().queryParameter("destination_account") != null) {
      throw new RuntimeException("cannot set both destination_assets and destination_account");
    }
    setAssetsParameter("destination_assets", assets);
    return this;
  }

  public StrictSendPathsRequestBuilder sourceAmount(String amount) {
    uriBuilder.setQueryParameter("source_amount", amount);
    return this;
  }

  public StrictSendPathsRequestBuilder sourceAsset(Asset asset) {
    uriBuilder.setQueryParameter("source_asset_type", asset.getType());
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
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws org.stellar.sdk.exception.ConnectionErrorException if the request fails due to an
   *     IOException, including but not limited to a timeout, connection failure etc.
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   */
  public static Page<PathResponse> execute(OkHttpClient httpClient, HttpUrl uri) {
    TypeToken<Page<PathResponse>> type = new TypeToken<Page<PathResponse>>() {};
    return Util.executeGetRequest(httpClient, uri, type);
  }

  /**
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   * @throws org.stellar.sdk.exception.ConnectionErrorException if the request fails due to an
   *     IOException, including but not limited to a timeout, connection failure etc.
   */
  public Page<PathResponse> execute() {
    return execute(this.httpClient, this.buildUri());
  }
}
