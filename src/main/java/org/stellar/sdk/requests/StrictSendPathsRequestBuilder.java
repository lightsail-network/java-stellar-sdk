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
