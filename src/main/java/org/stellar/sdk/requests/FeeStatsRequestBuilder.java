package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.stellar.sdk.Util;
import org.stellar.sdk.exception.TooManyRequestsException;
import org.stellar.sdk.responses.FeeStatsResponse;

public class FeeStatsRequestBuilder extends RequestBuilder {
  public FeeStatsRequestBuilder(OkHttpClient httpClient, HttpUrl serverURI) {
    super(httpClient, serverURI, "fee_stats");
  }

  /**
   * Requests <code>GET /fee_stats</code>
   *
   * @throws org.stellar.sdk.exception.ConnectionErrorException if the request fails due to an
   *     IOException, including but not limited to a timeout, connection failure etc.
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   * @see <a href="https://developers.stellar.org/api/aggregations/fee-stats/">Fee Stats</a>
   */
  public FeeStatsResponse execute() {
    TypeToken<FeeStatsResponse> type = new TypeToken<FeeStatsResponse>() {};
    return Util.executeGetRequest(httpClient, this.buildUri(), type);
  }
}
