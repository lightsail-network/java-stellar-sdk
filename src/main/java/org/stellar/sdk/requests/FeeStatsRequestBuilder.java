package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.stellar.sdk.responses.FeeStatsResponse;

public class FeeStatsRequestBuilder extends RequestBuilder {
  public FeeStatsRequestBuilder(OkHttpClient httpClient, HttpUrl serverURI) {
    super(httpClient, serverURI, "fee_stats");
  }

  /**
   * Requests <code>GET /fee_stats</code>
   *
   * @throws IOException if the request fails due to an IOException, including but not limited to a
   *     timeout, connection failure etc.
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   * @see <a href="https://developers.stellar.org/api/aggregations/fee-stats/">Fee Stats</a>
   */
  public FeeStatsResponse execute() throws IOException, TooManyRequestsException {
    TypeToken<FeeStatsResponse> type = new TypeToken<FeeStatsResponse>() {};
    ResponseHandler<FeeStatsResponse> responseHandler = new ResponseHandler<>(type);

    Request request = new Request.Builder().get().url(this.buildUri()).build();
    Response response = httpClient.newCall(request).execute();

    return responseHandler.handleResponse(response);
  }
}
