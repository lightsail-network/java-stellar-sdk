package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;

import org.apache.http.client.fluent.Request;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeCreditAlphaNum;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.responses.Page;
import org.stellar.sdk.responses.Path;

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
    uriBuilder.addParameter("destination_account", account.getAddress());
    return this;
  }

  public PathsRequestBuilder sourceAccount(KeyPair account) {
    uriBuilder.addParameter("source_account", account.getAddress());
    return this;
  }

  public PathsRequestBuilder destinationAmount(String amount) {
    uriBuilder.addParameter("destination_amount", amount);
    return this;
  }

  public PathsRequestBuilder destinationAsset(Asset asset) {
    uriBuilder.addParameter("destination_asset_type", asset.getType());
    if (asset instanceof AssetTypeCreditAlphaNum) {
      AssetTypeCreditAlphaNum creditAlphaNumAsset = (AssetTypeCreditAlphaNum) asset;
      uriBuilder.addParameter("destination_asset_code", creditAlphaNumAsset.getCode());
      uriBuilder.addParameter("destination_asset_issuer", creditAlphaNumAsset.getIssuer().getAddress());
    }
    return this;
  }

  public static Page<Path> execute(URI uri) throws IOException {
    TypeToken type = new TypeToken<Page<Path>>() {};
    ResponseHandler<Page<Path>> responseHandler = new ResponseHandler<Page<Path>>(type);
    return (Page<Path>) Request.Get(uri).execute().handleResponse(responseHandler);
  }

  public Page<Path> execute() throws IOException {
    return this.execute(this.buildUri());
  }
}
