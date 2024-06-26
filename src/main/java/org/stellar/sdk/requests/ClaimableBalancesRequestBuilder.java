package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.stellar.sdk.Asset;
import org.stellar.sdk.Util;
import org.stellar.sdk.exception.ConnectionErrorException;
import org.stellar.sdk.exception.TooManyRequestsException;
import org.stellar.sdk.responses.AssetResponse;
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
   * @return {@link ClaimableBalanceResponse}
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws org.stellar.sdk.exception.ConnectionErrorException if the request fails due to an
   *     IOException, including but not limited to a timeout, connection failure etc.
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   */
  public ClaimableBalanceResponse claimableBalance(HttpUrl uri) {
    TypeToken<ClaimableBalanceResponse> type = new TypeToken<ClaimableBalanceResponse>() {};
    return Util.executeGetRequest(httpClient, uri, type);
  }

  /**
   * The claimable balance details endpoint provides information on a claimable balance.
   *
   * @param id specifies which claimable balance to load.
   * @return The claimable balance details.
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws org.stellar.sdk.exception.ConnectionErrorException if the request fails due to an
   *     IOException, including but not limited to a timeout, connection failure etc.
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   */
  public ClaimableBalanceResponse claimableBalance(String id) {
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
   * @param httpClient {@link OkHttpClient} to use to send the request.
   * @param uri {@link HttpUrl} URI to send the request to.
   * @return {@link Page} of {@link ClaimableBalanceResponse}
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws ConnectionErrorException if the request fails due to an IOException, including but not
   *     limited to a timeout, connection failure etc.
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   */
  public static Page<ClaimableBalanceResponse> execute(OkHttpClient httpClient, HttpUrl uri) {
    TypeToken<Page<ClaimableBalanceResponse>> type =
        new TypeToken<Page<ClaimableBalanceResponse>>() {};
    return Util.executeGetRequest(httpClient, uri, type);
  }

  /**
   * Build and execute request.
   *
   * @return {@link Page} of {@link AssetResponse}
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws ConnectionErrorException if the request fails due to an IOException, including but not
   *     limited to a timeout, connection failure etc.
   * @throws TooManyRequestsException when too many requests were sent to the Horizon server.
   */
  public Page<ClaimableBalanceResponse> execute() {
    return execute(this.httpClient, this.buildUri());
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
