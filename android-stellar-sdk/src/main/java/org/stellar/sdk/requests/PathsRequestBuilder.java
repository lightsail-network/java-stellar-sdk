package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;

import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeCreditAlphaNum;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.responses.Page;
import org.stellar.sdk.responses.PathResponse;

import java.io.IOException;
import java.net.URI;

/**
 * Builds requests connected to paths.
 */
public class PathsRequestBuilder extends RequestBuilder {
  public PathsRequestBuilder(URI serverURI) {
    super(serverURI, "paths");
  }

  public PathsRequestBuilder destinationAccount(KeyPair account) {
    uriBuilder.appendQueryParameter("destination_account", account.getAccountId());
    return this;
  }

  public PathsRequestBuilder sourceAccount(KeyPair account) {
    uriBuilder.appendQueryParameter("source_account", account.getAccountId());
    return this;
  }

  public PathsRequestBuilder destinationAmount(String amount) {
    uriBuilder.appendQueryParameter("destination_amount", amount);
    return this;
  }

  public PathsRequestBuilder destinationAsset(Asset asset) {
    uriBuilder.appendQueryParameter("destination_asset_type", asset.getType());
    if (asset instanceof AssetTypeCreditAlphaNum) {
      AssetTypeCreditAlphaNum creditAlphaNumAsset = (AssetTypeCreditAlphaNum) asset;
      uriBuilder.appendQueryParameter("destination_asset_code", creditAlphaNumAsset.getCode());
      uriBuilder.appendQueryParameter("destination_asset_issuer", creditAlphaNumAsset.getIssuer().getAccountId());
    }
    return this;
  }

  /**
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   * @throws IOException
   */
  public static Page<PathResponse> execute(URI uri) throws IOException, TooManyRequestsException {
    TypeToken type = new TypeToken<Page<PathResponse>>() {};
    ResponseHandler<Page<PathResponse>> responseHandler = new ResponseHandler<Page<PathResponse>>(type);
    return responseHandler.handleGetRequest(uri);
  }

  /**
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   * @throws IOException
   */
  public Page<PathResponse> execute() throws IOException, TooManyRequestsException {
    return this.execute(this.buildUri());
  }
}
