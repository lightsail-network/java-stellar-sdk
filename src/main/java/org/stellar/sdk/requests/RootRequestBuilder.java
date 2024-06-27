package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.stellar.sdk.exception.TooManyRequestsException;
import org.stellar.sdk.responses.RootResponse;

/** Builds requests connected to root. */
public class RootRequestBuilder extends RequestBuilder {
  public RootRequestBuilder(OkHttpClient httpClient, HttpUrl serverURI) {
    super(httpClient, serverURI, "/");
  }

  /**
   * Requests <code>GET /</code>
   *
   * @return {@link RootResponse}
   * @throws org.stellar.sdk.exception.NetworkException All the exceptions below are subclasses of
   *     NetworkError
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
  public RootResponse execute() {
    TypeToken<RootResponse> type = new TypeToken<RootResponse>() {};
    return executeGetRequest(httpClient, this.buildUri(), type);
  }
}
