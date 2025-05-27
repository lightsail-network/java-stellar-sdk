package org.stellar.sdk.federation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.net.URLEncoder;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Test;
import org.stellar.sdk.SslCertificateUtils;
import org.stellar.sdk.http.IHttpClient;
import org.stellar.sdk.http.Jdk11HttpClient;

public class FederationTest {
  @Test
  public void testResolveAddressSuccess() throws IOException, InterruptedException {
    SSLContext sslContext = SslCertificateUtils.createSslContext();
    X509TrustManager trustAllCerts = SslCertificateUtils.createTrustAllCertsManager();
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.useHttps(sslContext.getSocketFactory(), false);
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    String domain = String.format("%s:%d", baseUrl.host(), baseUrl.port());
    final var client = laxTlsClient(sslContext);

    String stellarToml = "FEDERATION_SERVER = \"https://" + domain + "/federation\"";
    String successResponse =
        "{\"stellar_address\":\"bob*"
            + domain
            + "\",\"account_id\":\"GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY\"}";

    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(stellarToml));
    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(successResponse));

    FederationResponse response = new Federation(client).resolveAddress("bob*" + domain);

    RecordedRequest stellarTomlRequest = mockWebServer.takeRequest();
    assertEquals("GET", stellarTomlRequest.getMethod());
    assertEquals("/.well-known/stellar.toml", stellarTomlRequest.getPath());

    RecordedRequest federationRequest = mockWebServer.takeRequest();
    assertEquals("GET", federationRequest.getMethod());
    String expectedPath = "/federation?type=name&q=bob*" + URLEncoder.encode(domain, "UTF-8");
    assertEquals(expectedPath, federationRequest.getPath());

    assertEquals(response.getStellarAddress(), "bob*" + domain);
    assertEquals(
        response.getAccountId(), "GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY");
    assertNull(response.getMemoType());
    assertNull(response.getMemo());

    mockWebServer.close();
  }

  @Test
  public void testResolveAddressSuccessWithMemo() throws IOException, InterruptedException {
    SSLContext sslContext = SslCertificateUtils.createSslContext();
    X509TrustManager trustAllCerts = SslCertificateUtils.createTrustAllCertsManager();
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.useHttps(sslContext.getSocketFactory(), false);
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    String domain = String.format("%s:%d", baseUrl.host(), baseUrl.port());
    final var client = laxTlsClient(sslContext);

    String stellarToml = "FEDERATION_SERVER = \"https://" + domain + "/federation\"";
    String successResponse =
        "{\"stellar_address\":\"bob*"
            + domain
            + "\",\"account_id\":\"GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY\""
            + ", \"memo_type\": \"text\", \"memo\": \"test federation\"}";

    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(stellarToml));
    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(successResponse));

    FederationResponse response = new Federation(client).resolveAddress("bob*" + domain);

    RecordedRequest stellarTomlRequest = mockWebServer.takeRequest();
    assertEquals("GET", stellarTomlRequest.getMethod());
    assertEquals("/.well-known/stellar.toml", stellarTomlRequest.getPath());

    RecordedRequest federationRequest = mockWebServer.takeRequest();
    assertEquals("GET", federationRequest.getMethod());
    String expectedPath = "/federation?type=name&q=bob*" + URLEncoder.encode(domain, "UTF-8");
    assertEquals(expectedPath, federationRequest.getPath());

    assertEquals(response.getStellarAddress(), "bob*" + domain);
    assertEquals(
        response.getAccountId(), "GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY");
    assertEquals(response.getMemoType(), "text");
    assertEquals(response.getMemo(), "test federation");
    mockWebServer.close();
  }

  @Test
  public void testResolveAddressNotFound() throws IOException, InterruptedException {
    SSLContext sslContext = SslCertificateUtils.createSslContext();
    X509TrustManager trustAllCerts = SslCertificateUtils.createTrustAllCertsManager();
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.useHttps(sslContext.getSocketFactory(), false);
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    String domain = String.format("%s:%d", baseUrl.host(), baseUrl.port());
    final var client = laxTlsClient(sslContext);

    String stellarToml = "FEDERATION_SERVER = \"https://" + domain + "/federation\"";
    String notFoundResponse = "{\"code\":\"not_found\",\"message\":\"Account not found\"}";

    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(stellarToml));
    mockWebServer.enqueue(new MockResponse().setResponseCode(404).setBody(notFoundResponse));

    assertThrows(
        org.stellar.sdk.federation.exception.NotFoundException.class,
        () -> new Federation(client).resolveAddress("bob*" + domain));

    RecordedRequest stellarTomlRequest = mockWebServer.takeRequest();
    assertEquals("GET", stellarTomlRequest.getMethod());
    assertEquals("/.well-known/stellar.toml", stellarTomlRequest.getPath());

    RecordedRequest federationRequest = mockWebServer.takeRequest();
    assertEquals("GET", federationRequest.getMethod());
    String expectedPath = "/federation?type=name&q=bob*" + URLEncoder.encode(domain, "UTF-8");
    assertEquals(expectedPath, federationRequest.getPath());

    mockWebServer.close();
  }

  @Test
  public void testResolveAccountIdSuccess() throws IOException, InterruptedException {
    SSLContext sslContext = SslCertificateUtils.createSslContext();
    X509TrustManager trustAllCerts = SslCertificateUtils.createTrustAllCertsManager();
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.useHttps(sslContext.getSocketFactory(), false);
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    String domain = String.format("%s:%d", baseUrl.host(), baseUrl.port());
    final var client = laxTlsClient(sslContext);

    String stellarToml = "FEDERATION_SERVER = \"https://" + domain + "/federation\"";
    String successResponse =
        "{\"stellar_address\":\"bob*"
            + domain
            + "\",\"account_id\":\"GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY\"}";

    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(stellarToml));
    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(successResponse));

    FederationResponse response =
        new Federation(client)
            .resolveAccountId("GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY", domain);

    RecordedRequest stellarTomlRequest = mockWebServer.takeRequest();
    assertEquals("GET", stellarTomlRequest.getMethod());
    assertEquals("/.well-known/stellar.toml", stellarTomlRequest.getPath());

    RecordedRequest federationRequest = mockWebServer.takeRequest();
    assertEquals("GET", federationRequest.getMethod());
    String expectedPath =
        "/federation?type=id&q=GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY";
    assertEquals(expectedPath, federationRequest.getPath());

    assertEquals(
        response.getAccountId(), "GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY");
    assertNull(response.getMemoType());
    assertNull(response.getMemo());
    mockWebServer.close();
  }

  @Test
  public void testResolveAccountIdSuccessWithMemo() throws IOException, InterruptedException {
    SSLContext sslContext = SslCertificateUtils.createSslContext();
    X509TrustManager trustAllCerts = SslCertificateUtils.createTrustAllCertsManager();
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.useHttps(sslContext.getSocketFactory(), false);
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    String domain = String.format("%s:%d", baseUrl.host(), baseUrl.port());
    final var client = laxTlsClient(sslContext);

    String stellarToml = "FEDERATION_SERVER = \"https://" + domain + "/federation\"";
    String successResponse =
        "{\"stellar_address\":\"bob*"
            + domain
            + "\",\"account_id\":\"GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY\""
            + ", \"memo_type\": \"text\", \"memo\": \"test federation\"}";

    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(stellarToml));
    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(successResponse));
    FederationResponse response =
        new Federation(client)
            .resolveAccountId("GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY", domain);

    RecordedRequest stellarTomlRequest = mockWebServer.takeRequest();
    assertEquals("GET", stellarTomlRequest.getMethod());
    assertEquals("/.well-known/stellar.toml", stellarTomlRequest.getPath());

    RecordedRequest federationRequest = mockWebServer.takeRequest();
    assertEquals("GET", federationRequest.getMethod());
    String expectedPath =
        "/federation?type=id&q=GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY";
    assertEquals(expectedPath, federationRequest.getPath());

    assertEquals(response.getStellarAddress(), "bob*" + domain);
    assertEquals(
        response.getAccountId(), "GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY");
    assertEquals(response.getMemoType(), "text");
    assertEquals(response.getMemo(), "test federation");
    mockWebServer.close();
  }

  @Test
  public void testResolveAccountIdNotFound() throws IOException, InterruptedException {
    SSLContext sslContext = SslCertificateUtils.createSslContext();
    X509TrustManager trustAllCerts = SslCertificateUtils.createTrustAllCertsManager();
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.useHttps(sslContext.getSocketFactory(), false);
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    String domain = String.format("%s:%d", baseUrl.host(), baseUrl.port());
    final var client = laxTlsClient(sslContext);

    String stellarToml = "FEDERATION_SERVER = \"https://" + domain + "/federation\"";
    String notFoundResponse = "{\"code\":\"not_found\",\"message\":\"Account not found\"}";

    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(stellarToml));
    mockWebServer.enqueue(new MockResponse().setResponseCode(404).setBody(notFoundResponse));

    assertThrows(
        org.stellar.sdk.federation.exception.NotFoundException.class,
        () ->
            new Federation(client)
                .resolveAccountId(
                    "GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY", domain));

    RecordedRequest stellarTomlRequest = mockWebServer.takeRequest();
    assertEquals("GET", stellarTomlRequest.getMethod());
    assertEquals("/.well-known/stellar.toml", stellarTomlRequest.getPath());

    RecordedRequest federationRequest = mockWebServer.takeRequest();
    assertEquals("GET", federationRequest.getMethod());
    String expectedPath =
        "/federation?type=id&q=GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY";
    assertEquals(expectedPath, federationRequest.getPath());

    mockWebServer.shutdown();
    mockWebServer.close();
  }

  @Test
  public void testStellarTomlNotFoundInvalidExceptionThrows() throws IOException {
    SSLContext sslContext = SslCertificateUtils.createSslContext();
    X509TrustManager trustAllCerts = SslCertificateUtils.createTrustAllCertsManager();
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.useHttps(sslContext.getSocketFactory(), false);
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    String domain = String.format("%s:%d", baseUrl.host(), baseUrl.port());
    final var client = laxTlsClient(sslContext);

    mockWebServer.enqueue(new MockResponse().setResponseCode(404).setBody(""));
    assertThrows(
        org.stellar.sdk.federation.exception.StellarTomlNotFoundInvalidException.class,
        () -> new Federation(client).resolveAddress("bob*" + domain));
    mockWebServer.close();
  }

  @Test
  public void testNoFederationServerExceptionThrows() throws IOException {
    SSLContext sslContext = SslCertificateUtils.createSslContext();
    X509TrustManager trustAllCerts = SslCertificateUtils.createTrustAllCertsManager();
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.useHttps(sslContext.getSocketFactory(), false);
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    String domain = String.format("%s:%d", baseUrl.host(), baseUrl.port());
    final var client = laxTlsClient(sslContext);

    String stellarToml = "";
    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(stellarToml));
    assertThrows(
        org.stellar.sdk.federation.exception.NoFederationServerException.class,
        () -> new Federation(client).resolveAddress("bob*" + domain));
    mockWebServer.close();
  }

  @Test
  public void testFederationServerInvalidExceptionThrows() throws IOException {
    SSLContext sslContext = SslCertificateUtils.createSslContext();
    X509TrustManager trustAllCerts = SslCertificateUtils.createTrustAllCertsManager();
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.useHttps(sslContext.getSocketFactory(), false);
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    String domain = String.format("%s:%d", baseUrl.host(), baseUrl.port());
    final var client = laxTlsClient(sslContext);

    String stellarToml = "FEDERATION_SERVER = \"http://" + domain + "/federation\"";

    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(stellarToml));

    assertThrows(
        org.stellar.sdk.federation.exception.FederationServerInvalidException.class,
        () ->
            new Federation(client)
                .resolveAccountId(
                    "GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY", domain));
    mockWebServer.shutdown();
    mockWebServer.close();
  }

  @Test
  public void testMalformedAddressExceptionThrows() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new Federation(null).resolveAddress("bob*stellar.org*test"));
    assertThrows(IllegalArgumentException.class, () -> new Federation(null).resolveAddress("bob"));
  }

  private static IHttpClient laxTlsClient(SSLContext sslContext) {
    return new Jdk11HttpClient.Builder().withSslContext(sslContext).build();
  }
}
