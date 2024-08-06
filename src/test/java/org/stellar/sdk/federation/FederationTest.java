package org.stellar.sdk.federation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Test;
import org.stellar.sdk.SslCertificateUtils;
import org.stellar.sdk.federation.exception.MalformedAddressException;

public class FederationTest {
  @Test
  public void testResolveAddressSuccess() throws IOException {
    SSLSocketFactory sslSocketFactory = SslCertificateUtils.createSslSocketFactory();
    X509TrustManager trustAllCerts = SslCertificateUtils.createTrustAllCertsManager();
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.useHttps(sslSocketFactory, false);
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    String domain = String.format("%s:%d", baseUrl.host(), baseUrl.port());
    OkHttpClient client =
        new OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustAllCerts)
            .hostnameVerifier((hostname, session) -> true)
            .build();

    String stellarToml = "FEDERATION_SERVER = \"https://" + domain + "/federation\"";
    String successResponse =
        "{\"stellar_address\":\"bob*"
            + domain
            + "\",\"account_id\":\"GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY\"}";

    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(stellarToml));
    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(successResponse));

    FederationResponse response = new Federation(client).resolveAddress("bob*" + domain);
    assertEquals(response.getStellarAddress(), "bob*" + domain);
    assertEquals(
        response.getAccountId(), "GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY");
    assertNull(response.getMemoType());
    assertNull(response.getMemo());
    mockWebServer.close();
  }

  @Test
  public void testResolveAddressSuccessWithMemo() throws IOException {
    SSLSocketFactory sslSocketFactory = SslCertificateUtils.createSslSocketFactory();
    X509TrustManager trustAllCerts = SslCertificateUtils.createTrustAllCertsManager();
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.useHttps(sslSocketFactory, false);
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    String domain = String.format("%s:%d", baseUrl.host(), baseUrl.port());
    OkHttpClient client =
        new OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustAllCerts)
            .hostnameVerifier((hostname, session) -> true)
            .build();

    String stellarToml = "FEDERATION_SERVER = \"https://" + domain + "/federation\"";
    String successResponse =
        "{\"stellar_address\":\"bob*"
            + domain
            + "\",\"account_id\":\"GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY\""
            + ", \"memo_type\": \"text\", \"memo\": \"test federation\"}";

    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(stellarToml));
    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(successResponse));

    FederationResponse response = new Federation(client).resolveAddress("bob*" + domain);
    assertEquals(response.getStellarAddress(), "bob*" + domain);
    assertEquals(
        response.getAccountId(), "GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY");
    assertEquals(response.getMemoType(), "text");
    assertEquals(response.getMemo(), "test federation");
    mockWebServer.close();
  }

  @Test
  public void testResolveAddressNotFound() throws IOException {
    SSLSocketFactory sslSocketFactory = SslCertificateUtils.createSslSocketFactory();
    X509TrustManager trustAllCerts = SslCertificateUtils.createTrustAllCertsManager();
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.useHttps(sslSocketFactory, false);
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    String domain = String.format("%s:%d", baseUrl.host(), baseUrl.port());
    OkHttpClient client =
        new OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustAllCerts)
            .hostnameVerifier((hostname, session) -> true)
            .build();

    String stellarToml = "FEDERATION_SERVER = \"https://" + domain + "/federation\"";
    String notFoundResponse = "{\"code\":\"not_found\",\"message\":\"Account not found\"}";

    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(stellarToml));
    mockWebServer.enqueue(new MockResponse().setResponseCode(404).setBody(notFoundResponse));

    assertThrows(
        org.stellar.sdk.federation.exception.NotFoundException.class,
        () -> new Federation(client).resolveAddress("bob*" + domain));
    mockWebServer.close();
  }

  @Test
  public void testResolveAccountIdSuccess() throws IOException {
    SSLSocketFactory sslSocketFactory = SslCertificateUtils.createSslSocketFactory();
    X509TrustManager trustAllCerts = SslCertificateUtils.createTrustAllCertsManager();
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.useHttps(sslSocketFactory, false);
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    String domain = String.format("%s:%d", baseUrl.host(), baseUrl.port());
    OkHttpClient client =
        new OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustAllCerts)
            .hostnameVerifier((hostname, session) -> true)
            .build();

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
    assertEquals(response.getStellarAddress(), "bob*" + domain);
    assertEquals(
        response.getAccountId(), "GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY");
    assertNull(response.getMemoType());
    assertNull(response.getMemo());
    mockWebServer.close();
  }

  @Test
  public void testResolveAccountIdSuccessWithMemo() throws IOException {
    SSLSocketFactory sslSocketFactory = SslCertificateUtils.createSslSocketFactory();
    X509TrustManager trustAllCerts = SslCertificateUtils.createTrustAllCertsManager();
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.useHttps(sslSocketFactory, false);
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    String domain = String.format("%s:%d", baseUrl.host(), baseUrl.port());
    OkHttpClient client =
        new OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustAllCerts)
            .hostnameVerifier((hostname, session) -> true)
            .build();

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
    assertEquals(response.getStellarAddress(), "bob*" + domain);
    assertEquals(
        response.getAccountId(), "GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY");
    assertEquals(response.getMemoType(), "text");
    assertEquals(response.getMemo(), "test federation");
    mockWebServer.close();
  }

  @Test
  public void testResolveAccountIdNotFound() throws IOException {
    SSLSocketFactory sslSocketFactory = SslCertificateUtils.createSslSocketFactory();
    X509TrustManager trustAllCerts = SslCertificateUtils.createTrustAllCertsManager();
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.useHttps(sslSocketFactory, false);
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    String domain = String.format("%s:%d", baseUrl.host(), baseUrl.port());
    OkHttpClient client =
        new OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustAllCerts)
            .hostnameVerifier((hostname, session) -> true)
            .build();

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
    mockWebServer.shutdown();
    mockWebServer.close();
  }

  @Test
  public void testStellarTomlNotFoundInvalidExceptionThrows() throws IOException {
    SSLSocketFactory sslSocketFactory = SslCertificateUtils.createSslSocketFactory();
    X509TrustManager trustAllCerts = SslCertificateUtils.createTrustAllCertsManager();
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.useHttps(sslSocketFactory, false);
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    String domain = String.format("%s:%d", baseUrl.host(), baseUrl.port());
    OkHttpClient client =
        new OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustAllCerts)
            .hostnameVerifier((hostname, session) -> true)
            .build();

    mockWebServer.enqueue(new MockResponse().setResponseCode(404).setBody(""));
    assertThrows(
        org.stellar.sdk.federation.exception.StellarTomlNotFoundInvalidException.class,
        () -> new Federation(client).resolveAddress("bob*" + domain));
    mockWebServer.close();
  }

  @Test
  public void testNoFederationServerExceptionThrows() throws IOException {
    SSLSocketFactory sslSocketFactory = SslCertificateUtils.createSslSocketFactory();
    X509TrustManager trustAllCerts = SslCertificateUtils.createTrustAllCertsManager();
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.useHttps(sslSocketFactory, false);
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    String domain = String.format("%s:%d", baseUrl.host(), baseUrl.port());
    OkHttpClient client =
        new OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustAllCerts)
            .hostnameVerifier((hostname, session) -> true)
            .build();

    String stellarToml = "";
    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(stellarToml));
    assertThrows(
        org.stellar.sdk.federation.exception.NoFederationServerException.class,
        () -> new Federation(client).resolveAddress("bob*" + domain));
    mockWebServer.close();
  }

  @Test
  public void testFederationServerInvalidExceptionThrows() throws IOException {
    SSLSocketFactory sslSocketFactory = SslCertificateUtils.createSslSocketFactory();
    X509TrustManager trustAllCerts = SslCertificateUtils.createTrustAllCertsManager();
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.useHttps(sslSocketFactory, false);
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");
    String domain = String.format("%s:%d", baseUrl.host(), baseUrl.port());
    OkHttpClient client =
        new OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustAllCerts)
            .hostnameVerifier((hostname, session) -> true)
            .build();

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
        MalformedAddressException.class,
        () -> new Federation().resolveAddress("bob*stellar.org*test"));
    assertThrows(MalformedAddressException.class, () -> new Federation().resolveAddress("bob"));
  }
}
