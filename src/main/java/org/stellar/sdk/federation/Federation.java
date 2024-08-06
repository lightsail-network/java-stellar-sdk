package org.stellar.sdk.federation;

import com.google.gson.reflect.TypeToken;
import com.moandjiezana.toml.Toml;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import lombok.NonNull;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.stellar.sdk.exception.ConnectionErrorException;
import org.stellar.sdk.exception.TooManyRequestsException;
import org.stellar.sdk.federation.exception.FederationServerInvalidException;
import org.stellar.sdk.federation.exception.MalformedAddressException;
import org.stellar.sdk.federation.exception.NoFederationServerException;
import org.stellar.sdk.federation.exception.NotFoundException;
import org.stellar.sdk.federation.exception.StellarTomlNotFoundInvalidException;
import org.stellar.sdk.requests.ResponseHandler;

/**
 * Helper class for resolving Stellar addresses
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/learn/encyclopedia/network-configuration/federation">Federation</a>
 */
public class Federation {
  private final OkHttpClient httpClient;

  /**
   * Creates a new <code>Federation</code> instance.
   *
   * @param httpClient OkHttpClient
   */
  public Federation(OkHttpClient httpClient) {
    this.httpClient = httpClient;
  }

  /** Creates a new <code>Federation</code> instance with a default <code>OkHttpClient</code>. */
  public Federation() {
    this.httpClient = createHttpClient();
  }

  /**
   * Resolves the given address to federation record if the user was found for a given Stellar
   * address.
   *
   * @param address Stellar address (e.g. `bob*stellar.org`).
   * @return FederationResponse
   * @throws MalformedAddressException Address is malformed
   * @throws org.stellar.sdk.exception.NetworkException All the exceptions below are subclasses of
   *     NetworkError
   * @throws NotFoundException Stellar address not found in the federation server
   * @throws StellarTomlNotFoundInvalidException Stellar.toml file not found or invalid
   * @throws NoFederationServerException No federation server defined in stellar.toml file
   * @throws FederationServerInvalidException Federation server is invalid
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws TooManyRequestsException if the request fails due to too many requests sent to the
   *     server
   * @throws org.stellar.sdk.exception.RequestTimeoutException When Horizon returns a <code>Timeout
   *                                                            </code> or connection timeout
   *     occurred
   * @throws org.stellar.sdk.exception.UnknownResponseException if the server returns an unknown
   *     status code
   * @throws org.stellar.sdk.exception.ConnectionErrorException When the request cannot be executed
   *     due to cancellation or connectivity problems, etc.
   */
  public FederationResponse resolveAddress(String address) {
    String[] tokens = address.split("\\*");
    if (tokens.length != 2) {
      throw new MalformedAddressException(
          "address is malformed, it should be in the format `bob*stellar.org`");
    }
    String q = tokens[0];
    String domain = tokens[1];
    QueryType queryType = QueryType.NAME;
    return resolve(q, domain, queryType);
  }

  /**
   * Resolves the given Stellar account ID to federation record if the user was found for a given
   * Stellar account ID.
   *
   * @param accountId Stellar account ID. (e.g. `GAVHK7L...`)
   * @param domain The domain to get the federation server URI from. (e.g. `example.com`)
   * @return FederationResponse
   * @throws org.stellar.sdk.exception.NetworkException All the exceptions below are subclasses of
   *     NetworkError
   * @throws NotFoundException Stellar address not found in the federation server
   * @throws StellarTomlNotFoundInvalidException Stellar.toml file not found or invalid
   * @throws FederationServerInvalidException Federation server is invalid
   * @throws NoFederationServerException No federation server defined in stellar.toml file
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws TooManyRequestsException if the request fails due to too many requests sent to the
   *     server
   * @throws org.stellar.sdk.exception.RequestTimeoutException When Horizon returns a <code>Timeout
   *                                                            </code> or connection timeout
   *     occurred
   * @throws org.stellar.sdk.exception.UnknownResponseException if the server returns an unknown
   *     status code
   * @throws org.stellar.sdk.exception.ConnectionErrorException When the request cannot be executed
   *     due to cancellation or connectivity problems, etc.
   */
  public FederationResponse resolveAccountId(String accountId, String domain) {
    return resolve(accountId, domain, QueryType.ID);
  }

  private FederationResponse resolve(String q, String domain, QueryType queryType) {
    HttpUrl federationServerUri = getFederationServerUri(domain);

    HttpUrl.Builder uriBuilder = federationServerUri.newBuilder();
    uriBuilder.setQueryParameter("type", queryType.getValue());
    uriBuilder.setQueryParameter("q", q);
    HttpUrl uri = uriBuilder.build();

    TypeToken<FederationResponse> type = new TypeToken<FederationResponse>() {};
    ResponseHandler<FederationResponse> responseHandler = new ResponseHandler<>(type);

    Request request = new Request.Builder().get().url(uri).build();
    try (Response response = this.httpClient.newCall(request).execute()) {
      if (response.code() == 404) {
        throw new NotFoundException();
      }

      return responseHandler.handleResponse(response);
    } catch (IOException e) {
      throw new ConnectionErrorException(e);
    }
  }

  /**
   * Gets the federation server URI from the given domain.
   *
   * @param domain The domain to get the federation server URI from.
   * @return The federation server URI.
   * @throws org.stellar.sdk.exception.NetworkException All the exceptions below are subclasses of
   *     NetworkError
   * @throws StellarTomlNotFoundInvalidException Stellar.toml file not found or invalid
   * @throws NoFederationServerException No federation server defined in stellar.toml file
   * @throws FederationServerInvalidException Federation server is invalid
   * @throws ConnectionErrorException When the request cannot be executed due to cancellation or
   *     connectivity problems, etc.
   */
  private HttpUrl getFederationServerUri(@NonNull String domain) {
    String uriBuilder = "https://" + domain + "/.well-known/stellar.toml";
    HttpUrl stellarTomlUri = HttpUrl.parse(uriBuilder);
    assert stellarTomlUri != null;
    Request request = new Request.Builder().get().url(stellarTomlUri).build();
    try (Response response = httpClient.newCall(request).execute()) {
      if (response.code() >= 300) {
        throw new StellarTomlNotFoundInvalidException(response.code());
      }
      assert response.body() != null;
      Toml stellarToml = new Toml().read(response.body().string());
      String federationServer = stellarToml.getString("FEDERATION_SERVER");
      if (federationServer == null || federationServer.isEmpty()) {
        throw new NoFederationServerException();
      }
      HttpUrl federationServerUrl = HttpUrl.parse(federationServer);
      if (federationServerUrl == null || (!federationServerUrl.isHttps())) {
        throw new FederationServerInvalidException();
      }
      return federationServerUrl;
    } catch (IOException e) {
      throw new ConnectionErrorException(e);
    }
  }

  private static OkHttpClient createHttpClient() {
    return new OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .retryOnConnectionFailure(false)
        .build();
  }

  private enum QueryType {
    NAME("name"),
    ID("id");

    private final String value;

    QueryType(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }
}
