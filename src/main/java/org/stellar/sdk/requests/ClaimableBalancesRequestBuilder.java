package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.stellar.sdk.Asset;
import org.stellar.sdk.responses.ClaimableBalanceResponse;
import org.stellar.sdk.responses.Page;

/** Builds requests connected to claimable balances. */
public class ClaimableBalancesRequestBuilder extends RequestBuilder {
  public ClaimableBalancesRequestBuilder(OkHttpClient httpClient, HttpUrl serverURI) {
    super(httpClient, serverURI, "claimable_balances");
  }

  /**
   * Requests specific <code>uri</code> and returns {@link ClaimableBalanceResponse}. This method is
   * helpful for getting the links.
   *
   * @throws IOException
   */
  public ClaimableBalanceResponse claimableBalance(HttpUrl uri) throws IOException {
    TypeToken type = new TypeToken<ClaimableBalanceResponse>() {};
    ResponseHandler<ClaimableBalanceResponse> responseHandler =
        new ResponseHandler<ClaimableBalanceResponse>(type);

    Request request = new Request.Builder().get().url(uri).build();
    Response response = httpClient.newCall(request).execute();

    return responseHandler.handleResponse(response);
  }

  /**
   * The claimable balance details endpoint provides information on a claimable balance.
   *
   * @param id specifies which claimable balance to load.
   * @return The claimable balance details.
   * @throws IOException
   */
  public ClaimableBalanceResponse claimableBalance(String id) throws IOException {
    this.setSegments("claimable_balances", id);
    return this.claimableBalance(this.buildUri());
  }

  /**
   * Returns all claimable balances sponsored by a given account.
   *
   * @param sponsor Account ID of the sponsor.
   * @return current {@link ClaimableBalancesRequestBuilder} instance
   */
  public ClaimableBalancesRequestBuilder forSponsor(String sponsor) {
    uriBuilder.setQueryParameter("sponsor", sponsor);
    return this;
  }

  /**
   * Returns all claimable balances which hold a given asset.
   *
   * @param asset The Asset held by the claimable balance.
   * @return current {@link ClaimableBalancesRequestBuilder} instance
   */
  public ClaimableBalancesRequestBuilder forAsset(Asset asset) {
    setAssetParameter("asset", asset);
    return this;
  }

  /**
   * Returns all claimable balances which can be claimed by a given account id.
   *
   * @param claimant Account ID of the address which can claim the claimable balance.
   * @return current {@link ClaimableBalancesRequestBuilder} instance
   */
  public ClaimableBalancesRequestBuilder forClaimant(String claimant) {
    uriBuilder.setQueryParameter("claimant", claimant);
    return this;
  }

  /**
   * Requests specific <code>uri</code> and returns {@link Page} of {@link
   * ClaimableBalanceResponse}. This method is helpful for getting the next set of results.
   *
   * @return {@link Page} of {@link ClaimableBalanceResponse}
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   * @throws IOException
   */
  public static Page<ClaimableBalanceResponse> execute(OkHttpClient httpClient, HttpUrl uri)
      throws IOException, TooManyRequestsException {
    TypeToken type = new TypeToken<Page<ClaimableBalanceResponse>>() {};
    ResponseHandler<Page<ClaimableBalanceResponse>> responseHandler =
        new ResponseHandler<Page<ClaimableBalanceResponse>>(type);

    Request request = new Request.Builder().get().url(uri).build();
    Response response = httpClient.newCall(request).execute();

    return responseHandler.handleResponse(response);
  }

  /**
   * Build and execute request.
   *
   * @return {@link Page} of {@link ClaimableBalanceResponse}
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   * @throws IOException
   */
  public Page<ClaimableBalanceResponse> execute() throws IOException, TooManyRequestsException {
    return this.execute(this.httpClient, this.buildUri());
  }

  @Override
  public ClaimableBalancesRequestBuilder cursor(String token) {
    super.cursor(token);
    return this;
  }

  @Override
  public ClaimableBalancesRequestBuilder limit(int number) {
    super.limit(number);
    return this;
  }

  @Override
  public ClaimableBalancesRequestBuilder order(Order direction) {
    super.order(direction);
    return this;
  }
}
