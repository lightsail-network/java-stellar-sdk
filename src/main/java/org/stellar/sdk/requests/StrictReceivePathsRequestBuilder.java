package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.List;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeCreditAlphaNum;
import org.stellar.sdk.responses.Page;
import org.stellar.sdk.responses.PathResponse;

/** Builds requests connected to paths. */
public class StrictReceivePathsRequestBuilder extends RequestBuilder {
  public StrictReceivePathsRequestBuilder(OkHttpClient httpClient, HttpUrl serverURI) {
    super(httpClient, serverURI, "");
    this.setSegments("paths", "strict-receive");
  }

  public StrictReceivePathsRequestBuilder destinationAccount(String account) {
    uriBuilder.setQueryParameter("destination_account", account);
    return this;
  }

  public StrictReceivePathsRequestBuilder sourceAccount(String account) {
    if (uriBuilder.build().queryParameter("source_assets") != null) {
      throw new RuntimeException("cannot set both source_assets and source_account");
    }
    uriBuilder.setQueryParameter("source_account", account);
    return this;
  }

  public StrictReceivePathsRequestBuilder sourceAssets(List<Asset> assets) {
    if (uriBuilder.build().queryParameter("source_account") != null) {
      throw new RuntimeException("cannot set both source_assets and source_account");
    }
    setAssetsParameter("source_assets", assets);
    return this;
  }

  public StrictReceivePathsRequestBuilder destinationAmount(String amount) {
    uriBuilder.setQueryParameter("destination_amount", amount);
    return this;
  }

  public StrictReceivePathsRequestBuilder destinationAsset(Asset asset) {
    uriBuilder.setQueryParameter("destination_asset_type", asset.getType());
    if (asset instanceof AssetTypeCreditAlphaNum) {
      AssetTypeCreditAlphaNum creditAlphaNumAsset = (AssetTypeCreditAlphaNum) asset;
      uriBuilder.setQueryParameter("destination_asset_code", creditAlphaNumAsset.getCode());
      uriBuilder.setQueryParameter("destination_asset_issuer", creditAlphaNumAsset.getIssuer());
    }
    return this;
  }

  /**
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   * @throws IOException
   */
  public static Page<PathResponse> execute(OkHttpClient httpClient, HttpUrl uri)
      throws IOException, TooManyRequestsException {
    TypeToken type = new TypeToken<Page<PathResponse>>() {};
    ResponseHandler<Page<PathResponse>> responseHandler =
        new ResponseHandler<Page<PathResponse>>(type);

    Request request = new Request.Builder().get().url(uri).build();
    Response response = httpClient.newCall(request).execute();

    return responseHandler.handleResponse(response);
  }

  /**
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   * @throws IOException
   */
  public Page<PathResponse> execute() throws IOException, TooManyRequestsException {
    return this.execute(this.httpClient, this.buildUri());
  }
}
