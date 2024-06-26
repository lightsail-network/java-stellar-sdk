package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.stellar.sdk.Util;
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

  public static Page<AssetResponse> execute(OkHttpClient httpClient, HttpUrl uri) {
    TypeToken<Page<AssetResponse>> type = new TypeToken<Page<AssetResponse>>() {};
    return Util.executeGetRequest(httpClient, uri, type);
  }

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
