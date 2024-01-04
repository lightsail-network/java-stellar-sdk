package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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

  public static Page<AssetResponse> execute(OkHttpClient httpClient, HttpUrl uri)
      throws IOException, TooManyRequestsException {
    TypeToken<Page<AssetResponse>> type = new TypeToken<Page<AssetResponse>>() {};
    ResponseHandler<Page<AssetResponse>> responseHandler = new ResponseHandler<>(type);

    Request request = new Request.Builder().get().url(uri).build();
    Response response = httpClient.newCall(request).execute();

    return responseHandler.handleResponse(response);
  }

  public Page<AssetResponse> execute() throws IOException, TooManyRequestsException {
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
