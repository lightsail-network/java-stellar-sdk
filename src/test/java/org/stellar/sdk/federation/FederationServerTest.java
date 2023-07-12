package org.stellar.sdk.federation;

import java.io.IOException;
import junit.framework.TestCase;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FederationServerTest extends TestCase {
  private final String successResponse =
      "{\"stellar_address\":\"bob*stellar.org\",\"account_id\":\"GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY\"}";
  private final String successResponseWithMemo =
      "{\"stellar_address\":\"bob*stellar.org\",\"account_id\":\"GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY\", \"memo_type\": \"text\", \"memo\": \"test\"}";

  private final String notFoundResponse =
      "{\"code\":\"not_found\",\"message\":\"Account not found\"}";

  private final String stellarToml = "FEDERATION_SERVER = \"https://api.stellar.org/federation\"";

  @Before
  public void setUp() throws IOException {
    FederationServer.httpsConnection = false;
  }

  @After
  public void tearDown() throws IOException {
    FederationServer.httpsConnection = true;
  }

  @Test
  public void testCreateForDomain() throws IOException, InterruptedException {
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(stellarToml));
    mockWebServer.start();

    HttpUrl baseUrl = mockWebServer.url("");
    String domain = String.format("%s:%d", baseUrl.host(), baseUrl.port());
    FederationServer server = FederationServer.createForDomain(domain);
    assertEquals(server.getServerUri().toString(), "https://api.stellar.org/federation");
    assertEquals(server.getDomain(), domain);

    RecordedRequest stellarTomlRequest = mockWebServer.takeRequest();
    assertEquals(
        "http://" + domain + "/.well-known/stellar.toml",
        stellarTomlRequest.getRequestUrl().toString());
  }

  @Test
  public void testNameFederationSuccess() throws IOException {
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(successResponse));
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");

    FederationServer server = new FederationServer(baseUrl.toString(), "stellar.org");

    FederationResponse response = server.resolveAddress("bob*stellar.org");
    assertEquals(response.getStellarAddress(), "bob*stellar.org");
    assertEquals(
        response.getAccountId(), "GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY");
    assertNull(response.getMemoType());
    assertNull(response.getMemo());
  }

  @Test
  public void testNameFederationSuccessWithMemo() throws IOException {
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(successResponseWithMemo));
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");

    FederationServer server = new FederationServer(baseUrl.toString(), "stellar.org");

    FederationResponse response = server.resolveAddress("bob*stellar.org");
    assertEquals(response.getStellarAddress(), "bob*stellar.org");
    assertEquals(
        response.getAccountId(), "GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY");
    assertEquals(response.getMemoType(), "text");
    assertEquals(response.getMemo(), "test");
  }

  @Test
  public void testNameFederationNotFound() throws IOException {
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.enqueue(new MockResponse().setResponseCode(404).setBody(notFoundResponse));
    mockWebServer.start();
    HttpUrl baseUrl = mockWebServer.url("");

    FederationServer server = new FederationServer(baseUrl.toString(), "stellar.org");

    try {
      FederationResponse response = server.resolveAddress("bob*stellar.org");
      fail("Expected exception");
    } catch (NotFoundException e) {
    }
  }
}
