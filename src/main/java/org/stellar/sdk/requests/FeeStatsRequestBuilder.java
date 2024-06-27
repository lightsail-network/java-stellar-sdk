package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.stellar.sdk.exception.ConnectionErrorException;
import org.stellar.sdk.exception.TooManyRequestsException;
import org.stellar.sdk.responses.FeeStatsResponse;

public class FeeStatsRequestBuilder extends RequestBuilder {
  public FeeStatsRequestBuilder(OkHttpClient httpClient, HttpUrl serverURI) {
    super(httpClient, serverURI, "fee_stats");
  }

  /**
   * Requests <code>GET /fee_stats</code>
   *
   * @return {@link FeeStatsResponse}
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
   * @throws ConnectionErrorException When the request cannot be executed due to cancellation or
   *     connectivity problems, etc.
   */
  public FeeStatsResponse execute() {
    TypeToken<FeeStatsResponse> type = new TypeToken<FeeStatsResponse>() {};
    return executeGetRequest(httpClient, this.buildUri(), type);
  }
}
