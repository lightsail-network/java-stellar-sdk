package org.stellar.sdk.federation;

import android.net.Uri;
import com.google.gson.reflect.TypeToken;
import com.moandjiezana.toml.Toml;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.stellar.sdk.requests.ResponseHandler;
import org.stellar.sdk.responses.HttpResponseException;

/**
 * FederationServer handles a network connection to a
 * <a href="https://www.stellar.org/developers/learn/concepts/federation.html" target="_blank">federation server</a>
 * instance and exposes an interface for requests to that instance.
 * <p>
 * For resolving a stellar address without knowing which federation server
 * to query use {@link Federation#resolve(String)}.
 *
 * @see <a href="https://www.stellar.org/developers/learn/concepts/federation.html" target="_blank">Federation docs</a>
 */
public class FederationServer {
  private final URI serverUri;
  private final String domain;
  private static OkHttpClient httpClient = new OkHttpClient();

  /**
   * Creates a new <code>FederationServer</code> instance.
   *
   * @param serverUri Federation Server URI
   * @param domain    Domain name this federation server is responsible for
   * @throws FederationServerInvalidException Federation server is invalid (malformed URL, not HTTPS, etc.)
   */
  public FederationServer(URI serverUri, String domain) {
    this.serverUri = serverUri;
    if (this.serverUri.getScheme() != "https") {
      throw new FederationServerInvalidException();
    }
    this.domain = domain;
  }

  /**
   * Creates a new <code>FederationServer</code> instance.
   *
   * @param serverUri Federation Server URI
   * @param domain    Domain name this federation server is responsible for
   * @throws FederationServerInvalidException Federation server is invalid (malformed URL, not HTTPS, etc.)
   */
  public FederationServer(String serverUri, String domain) {
    try {
      this.serverUri = new URI(serverUri);
    } catch (URISyntaxException e) {
      throw new FederationServerInvalidException();
    }
    this.domain = domain;
  }

  /**
   * Creates a <code>FederationServer</code> instance for a given domain.
   * It tries to find a federation server URL in stellar.toml file.
   *
   * @param domain Domain to find a federation server for
   * @return FederationServer
   * @throws ConnectionErrorException            Connection problems
   * @throws NoFederationServerException         Stellar.toml does not contain federation server info
   * @throws FederationServerInvalidException    Federation server is invalid (malformed URL, not HTTPS, etc.)
   * @throws StellarTomlNotFoundInvalidException Stellar.toml file was not found or was malformed.
   * @see <a href="https://www.stellar.org/developers/learn/concepts/stellar-toml.html" target="_blank">Stellar.toml docs</a>
   */
  public static FederationServer createForDomain(String domain) {
    URI stellarTomlUri;
    try {
      StringBuilder uriBuilder = new StringBuilder();
      uriBuilder.append("https://");
      uriBuilder.append(domain);
      uriBuilder.append("/.well-known/stellar.toml");
      stellarTomlUri = new URI(uriBuilder.toString());
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
    Toml stellarToml = null;
    try {
      Request request = new Request.Builder().get()
          .url(stellarTomlUri.toString())
          .build();
      Response response = httpClient.newCall(request)
          .execute();
      if (response.code() >= 300) {
        throw new StellarTomlNotFoundInvalidException();
      }
      ResponseBody body = response.body();
      if (body == null) {
        throw new StellarTomlNotFoundInvalidException();
      }
      stellarToml = new Toml().read(body.string());
    } catch (IOException e) {
      throw new ConnectionErrorException();
    }

    String federationServer = stellarToml.getString("FEDERATION_SERVER");
    if (federationServer == null) {
      throw new NoFederationServerException();
    }

    return new FederationServer(federationServer, domain);
  }

  /**
   * Resolves a stellar address using a given federation server.
   *
   * @param address Stellar addres, like <code>bob*stellar.org</code>
   * @return FederationResponse
   * @throws MalformedAddressException Address is malformed
   * @throws ConnectionErrorException  Connection problems
   * @throws NotFoundException         Stellar address not found by federation server
   * @throws ServerErrorException      Federation server responded with error
   */
  public FederationResponse resolveAddress(String address) {
    String[] tokens = address.split("\\*");
    if (tokens.length != 2) {
      throw new MalformedAddressException();
    }

    Uri uri = Uri.parse(serverUri.toString())
        .buildUpon()
        .appendQueryParameter("type", "name")
        .appendQueryParameter("q", address)
        .build();

    TypeToken type = new TypeToken<FederationResponse>() {
    };
      ResponseHandler<FederationResponse> responseHandler = new ResponseHandler<FederationResponse>(httpClient, type);

    Request request = new Request.Builder()
        .url(uri.toString())
        .get()
        .build();

    try {
      Response response = httpClient.newCall(request)
          .execute();
      return responseHandler.handleResponse(response);
    } catch (HttpResponseException e) {
      if (e.getStatusCode() == 404) {
        throw new NotFoundException();
      } else {
        throw new ServerErrorException();
      }
    } catch (IOException e) {
      throw new ConnectionErrorException();
    }
  }

  /**
   * Returns a federation server URI.
   *
   * @return URI
   */
  public URI getServerUri() {
    return serverUri;
  }

  /**
   * Returns a domain this server is responsible for.
   *
   * @return InternetDomainName
   */
  public String getDomain() {
    return domain;
  }

  /**
   * To support mocking a client
   *
   * @param httpClient
   */
  static void setHttpClient(OkHttpClient httpClient) {
    FederationServer.httpClient = httpClient;
  }
}
