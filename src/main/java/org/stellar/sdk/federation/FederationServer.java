package org.stellar.sdk.federation;

import com.google.gson.reflect.TypeToken;
import com.moandjiezana.toml.Toml;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import okhttp3.*;
import org.stellar.sdk.exception.ConnectionErrorException;
import org.stellar.sdk.federation.exception.FederationServerInvalidException;
import org.stellar.sdk.federation.exception.MalformedAddressException;
import org.stellar.sdk.federation.exception.NoFederationServerException;
import org.stellar.sdk.federation.exception.NotFoundException;
import org.stellar.sdk.federation.exception.StellarTomlNotFoundInvalidException;
import org.stellar.sdk.requests.ResponseHandler;

/**
 * FederationServer handles a network connection to a <a
 * href="https://developers.stellar.org/docs/glossary/federation/" target="_blank">federation
 * server</a> instance and exposes an interface for requests to that instance.
 *
 * <p>For resolving a stellar address without knowing which federation server to query use {@link
 * Federation#resolve(String)}.
 *
 * @see <a href="https://developers.stellar.org/docs/glossary/federation/"
 *     target="_blank">Federation docs</a>
 */
public class FederationServer {
  /** Returns a federation server URI. */
  @Getter private final HttpUrl serverUri;

  /** Returns a domain this server is responsible for. */
  @Getter private final String domain;

  private final OkHttpClient httpClient;

  /**
   * Unfortunately, okhttp mocking methods make it super hard to mock https request. This is only
   * used to switch to http in tests. To improve in a future.
   */
  static boolean httpsConnection = true;

  /**
   * Creates a new <code>FederationServer</code> instance.
   *
   * @param serverUri Federation Server URI
   * @param domain Domain name this federation server is responsible for
   * @throws FederationServerInvalidException Federation server is invalid (malformed URL, not
   *     HTTPS, etc.)
   */
  public FederationServer(URI serverUri, String domain) {
    this.serverUri = HttpUrl.get(serverUri);
    if (this.serverUri == null || (httpsConnection && !this.serverUri.isHttps())) {
      throw new FederationServerInvalidException();
    }
    this.domain = domain;
    this.httpClient = FederationServer.createHttpClient();
  }

  private static OkHttpClient createHttpClient() {
    return new OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .retryOnConnectionFailure(false)
        .build();
  }

  /**
   * Creates a new <code>FederationServer</code> instance.
   *
   * @param serverUri Federation Server URI
   * @param domain Domain name this federation server is responsible for
   * @throws FederationServerInvalidException Federation server is invalid (malformed URL, not
   *     HTTPS, etc.)
   */
  public FederationServer(String serverUri, String domain) {
    this(HttpUrl.parse(serverUri).uri(), domain);
  }

  /**
   * Creates a <code>FederationServer</code> instance for a given domain. It tries to find a
   * federation server URL in stellar.toml file.
   *
   * @see <a
   *     href="https://developers.stellar.org/docs/issuing-assets/publishing-asset-info/#what-is-a-stellartoml"
   *     target="_blank">Stellar.toml docs</a>
   * @param domain Domain to find a federation server for
   * @throws ConnectionErrorException Connection problems
   * @throws NoFederationServerException Stellar.toml does not contain federation server info
   * @throws FederationServerInvalidException Federation server is invalid (malformed URL, not
   *     HTTPS, etc.)
   * @throws StellarTomlNotFoundInvalidException Stellar.toml file was not found or was malformed.
   * @return FederationServer
   */
  public static FederationServer createForDomain(String domain) {
    StringBuilder uriBuilder = new StringBuilder();
    uriBuilder.append(httpsConnection ? "https://" : "http://");
    uriBuilder.append(domain);
    uriBuilder.append("/.well-known/stellar.toml");
    HttpUrl stellarTomlUri = HttpUrl.parse(uriBuilder.toString());
    OkHttpClient httpClient = FederationServer.createHttpClient();

    Request request = new Request.Builder().get().url(stellarTomlUri).build();
    Response response = null;
    try {
      response = httpClient.newCall(request).execute();

      if (response.code() >= 300) {
        throw new StellarTomlNotFoundInvalidException(response.code());
      }

      Toml stellarToml = new Toml().read(response.body().string());

      String federationServer = stellarToml.getString("FEDERATION_SERVER");
      if (federationServer == null) {
        throw new NoFederationServerException();
      }

      return new FederationServer(federationServer, domain);
    } catch (IOException e) {
      throw new ConnectionErrorException(e);
    } finally {
      if (response != null) {
        response.close();
      }
    }
  }

  /**
   * Resolves a stellar address using a given federation server.
   *
   * @param address Stellar addres, like <code>bob*stellar.org</code>
   * @throws MalformedAddressException Address is malformed
   * @throws ConnectionErrorException Connection problems
   * @throws NotFoundException Stellar address not found by federation server
   * @return FederationResponse
   */
  public FederationResponse resolveAddress(String address) {
    String[] tokens = address.split("\\*");
    if (tokens.length != 2) {
      throw new MalformedAddressException();
    }

    HttpUrl.Builder uriBuilder = this.serverUri.newBuilder();
    uriBuilder.setQueryParameter("type", "name");
    uriBuilder.setQueryParameter("q", address);
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
}
