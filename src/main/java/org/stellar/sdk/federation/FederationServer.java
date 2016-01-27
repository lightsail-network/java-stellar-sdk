package org.stellar.sdk.federation;

import com.google.common.net.InternetDomainName;
import com.google.gson.reflect.TypeToken;
import com.moandjiezana.toml.Toml;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.stellar.sdk.requests.ResponseHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * FederationServer handles a network connection to a
 * <a href="https://www.stellar.org/developers/learn/concepts/federation.html" target="_blank">federation server</a>
 * instance and exposes an interface for requests to that instance.
 *
 * For resolving a stellar address without knowing which federation server
 * to query use {@link Federation#resolve(String)}.
 *
 * @see <a href="https://www.stellar.org/developers/learn/concepts/federation.html" target="_blank">Federation docs</a>
 */
public class FederationServer {
  private final URI serverUri;
  private final InternetDomainName domain;
  private static HttpClient httpClient = HttpClients.createDefault();

  /**
   * Creates a new <code>FederationServer</code> instance.
   * @param serverUri Federation Server URI
   * @param domain Domain name this federation server is responsible for
   */
  public FederationServer(URI serverUri, InternetDomainName domain) {
    this.serverUri = serverUri;
    if (this.serverUri.getScheme() != "https") {
      throw new RuntimeException("Only HTTPS servers allowed");
    }
    this.domain = domain;
  }

  /**
   * Creates a new <code>FederationServer</code> instance.
   * @param serverUri Federation Server URI
   * @param domain Domain name this federation server is responsible for
   */
  public FederationServer(String serverUri, InternetDomainName domain) {
    try {
      this.serverUri = new URI(serverUri);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
    this.domain = domain;
  }

  /**
   * Creates a <code>FederationServer</code> instance for a given domain.
   * It tries to find a federation server URL in stellar.toml file.
   * @see <a href="https://www.stellar.org/developers/learn/concepts/stellar-toml.html" target="_blank">Stellar.toml docs</a>
   * @param domain Domain to find a federation server for
   * @throws IOException
   */
  public static FederationServer createForDomain(InternetDomainName domain) throws IOException {
    Executor executor = Executor.newInstance(FederationServer.httpClient);
    URI stellarTomlUri;
    try {
      stellarTomlUri = new URI("https://www."+domain.toString()+"/.well-known/stellar.toml");
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
    Toml stellarToml = executor.execute(Request.Get(stellarTomlUri))
            .handleResponse(new org.apache.http.client.ResponseHandler<Toml>() {
              @Override
              public Toml handleResponse(HttpResponse response) throws IOException {
                if (response.getStatusLine().getStatusCode() >= 300) {
                  throw new RuntimeException("stellar.toml file not found");
                }

                HttpEntity entity = response.getEntity();
                if (entity == null) {
                  throw new RuntimeException("stellar.toml response contains no content");
                }

                StringWriter writer = new StringWriter();
                InputStream content = entity.getContent();
                IOUtils.copy(content, writer);
                return new Toml().read(writer.toString());
              }
            });

    String federationServer = stellarToml.getString("FEDERATION_SERVER");
    if (federationServer == null) {
      throw new RuntimeException("stellar.toml file does not contain FEDERATION_SERVER field");
    }

    return new FederationServer(federationServer, domain);
  }

  /**
   * Resolves a stellar address using a given federation server.
   * @param address Stellar addres, like <code>bob*stellar.org</code>
   * @throws IOException
   */
  public FederationResponse resolveAddress(String address) throws IOException {
    String[] tokens = address.split("\\*");
    if (tokens.length != 2) {
      throw new RuntimeException("Invalid Stellar address");
    }

    URIBuilder uriBuilder = new URIBuilder(this.serverUri);
    uriBuilder.setParameter("type", "name");
    uriBuilder.setParameter("q", address);
    URI uri;
    try {
      uri = uriBuilder.build();
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }

    TypeToken type = new TypeToken<FederationResponse>() {};
    ResponseHandler<FederationResponse> responseHandler = new ResponseHandler<FederationResponse>(type);
    Executor executor = Executor.newInstance(FederationServer.httpClient);
    return (FederationResponse) executor.execute(Request.Get(uri)).handleResponse(responseHandler);
  }

  /**
   * Returns a federation server URI.
   */
  public URI getServerUri() {
    return serverUri;
  }

  /**
   * Returns a domain this server is responsible for.
   */
  public InternetDomainName getDomain() {
    return domain;
  }

  /**
   * To support mocking a client
   * @param httpClient
   */
  static void setHttpClient(HttpClient httpClient) {
    FederationServer.httpClient = httpClient;
  }
}
