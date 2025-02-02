package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.stellar.sdk.*;
import org.stellar.sdk.exception.ConnectionErrorException;
import org.stellar.sdk.exception.RequestTimeoutException;
import org.stellar.sdk.exception.TooManyRequestsException;
import org.stellar.sdk.http.GetRequest;
import org.stellar.sdk.http.IHttpClient;
import org.stellar.sdk.http.StringResponse;
import org.stellar.sdk.http.sse.ISseClient;

/** Abstract class for request builders. */
public abstract class RequestBuilder {
  protected UriBuilder uriBuilder;
  protected IHttpClient httpClient;
  protected ISseClient sseClient;
  private final ArrayList<String> segments;
  private boolean segmentsAdded;

  RequestBuilder(
      IHttpClient httpClient, ISseClient sseClient, URI serverURI, String defaultSegment) {
    this.httpClient = httpClient;
    this.sseClient = sseClient;
    this.uriBuilder = new UriBuilder(serverURI);
    this.segments = new ArrayList<>();
    if (defaultSegment != null) {
      this.setSegments(defaultSegment);
    }
    this.segmentsAdded = false; // Allow overwriting segments
  }

  protected RequestBuilder setSegments(String... segments) {
    if (segmentsAdded) {
      throw new IllegalArgumentException("URL segments have been already added.");
    }

    segmentsAdded = true;
    // Remove default segments
    this.segments.clear();
    this.segments.addAll(Arrays.asList(segments));

    return this;
  }

  /**
   * Sets <code>cursor</code> parameter on the request. A cursor is a value that points to a
   * specific location in a collection of resources. The cursor attribute itself is an opaque value
   * meaning that users should not try to parse it.
   *
   * @see <a href="https://developers.stellar.org/api/introduction/pagination/">Page
   *     documentation</a>
   * @param cursor A cursor is a value that points to a specific location in a collection of
   *     resources.
   */
  public RequestBuilder cursor(String cursor) {
    uriBuilder.setQueryParameter("cursor", cursor);
    return this;
  }

  /**
   * Sets <code>limit</code> parameter on the request. It defines maximum number of records to
   * return. For range and default values check documentation of the endpoint requested.
   *
   * @param number maxium number of records to return
   */
  public RequestBuilder limit(int number) {
    uriBuilder.setQueryParameter("limit", String.valueOf(number));
    return this;
  }

  /**
   * Sets <code>order</code> parameter on the request.
   *
   * @param direction {@link org.stellar.sdk.requests.RequestBuilder.Order}
   */
  public RequestBuilder order(Order direction) {
    uriBuilder.setQueryParameter("order", direction.getValue());
    return this;
  }

  /**
   * Sets a parameter consisting of a comma separated list of assets on the request.
   *
   * @param name the name of the query parameter
   * @param assets the list of assets to be serialized into the query parameter value
   */
  public void setAssetsParameter(String name, List<Asset> assets) {
    List<String> assetStrings = new ArrayList<>();
    for (Asset asset : assets) {
      assetStrings.add(encodeAsset(asset));
    }
    uriBuilder.setQueryParameter(name, String.join(",", assetStrings));
  }

  /**
   * Sets a parameter consisting of an asset represented as "assetCode:assetIssue" on the request.
   *
   * @param name the name of the query parameter
   * @param asset the asset to be serialized into the query parameter value
   */
  public void setAssetParameter(String name, Asset asset) {
    uriBuilder.setQueryParameter(name, encodeAsset(asset));
  }

  private String encodeAsset(Asset asset) {
    if (asset instanceof AssetTypeNative) {
      return "native";
    } else if (asset instanceof AssetTypeCreditAlphaNum) {
      AssetTypeCreditAlphaNum creditAsset = (AssetTypeCreditAlphaNum) asset;
      return creditAsset.getCode() + ":" + creditAsset.getIssuer();
    } else {
      throw new IllegalArgumentException("unsupported asset " + asset.getType());
    }
  }

  URI buildUri() {
    if (!segments.isEmpty()) {
      segments.forEach(
          segment -> {
            uriBuilder.addPathSegment(segment);
          });
    }

    return uriBuilder.build();
  }

  /** Represents possible <code>order</code> parameter values. */
  public enum Order {
    ASC("asc"),
    DESC("desc");
    private final String value;

    Order(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }

  /**
   * Executes a GET request and handles the response.
   *
   * @param <T> The type of the response object
   * @param httpClient The {@link IHttpClient} to use for the request
   * @param uri The {@link URI} to send the GET request to
   * @param typeToken The TypeToken representing the type of the response
   * @return The response object of type T
   * @throws org.stellar.sdk.exception.NetworkException All the exceptions below are subclasses of
   *     NetworkError
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws TooManyRequestsException if the request fails due to too many requests sent to the
   *     server
   * @throws RequestTimeoutException When Horizon returns a <code>Timeout</code> or connection
   *     timeout occurred
   * @throws org.stellar.sdk.exception.UnknownResponseException if the server returns an unknown
   *     status code
   * @throws ConnectionErrorException When the request cannot be executed due to cancellation or
   *     connectivity problems, etc.
   */
  static <T> T executeGetRequest(IHttpClient httpClient, URI uri, TypeToken<T> typeToken) {
    ResponseHandler<T> responseHandler = new ResponseHandler<>(typeToken);

    final var getRequest = new GetRequest(uri);

    StringResponse response;
    try {
      response = httpClient.get(getRequest);
    } catch (SocketTimeoutException e) {
      throw new RequestTimeoutException(e);
    } catch (IOException e) {
      throw new ConnectionErrorException(e);
    }
    return responseHandler.handleResponse(response);
  }

  static String getAssetType(Asset asset) {
    String assetType;
    switch (asset.getType()) {
      case ASSET_TYPE_NATIVE:
        assetType = "native";
        break;
      case ASSET_TYPE_CREDIT_ALPHANUM4:
        assetType = "credit_alphanum4";
        break;
      case ASSET_TYPE_CREDIT_ALPHANUM12:
        assetType = "credit_alphanum12";
        break;
      default:
        throw new IllegalArgumentException("Invalid asset type");
    }
    return assetType;
  }
}
