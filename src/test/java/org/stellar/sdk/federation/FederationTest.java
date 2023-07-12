package org.stellar.sdk.federation;

import java.io.IOException;
import junit.framework.TestCase;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FederationTest extends TestCase {
  @Before
  public void setUp() throws IOException {
    FederationServer.httpsConnection = false;
  }

  @After
  public void tearDown() throws IOException {
    FederationServer.httpsConnection = true;
  }

  @Test
  public void testResolveSuccess() throws IOException {
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.start();

    HttpUrl baseUrl = mockWebServer.url("");
    String domain = String.format("%s:%d", baseUrl.host(), baseUrl.port());

    String stellarToml = "FEDERATION_SERVER = \"http://" + domain + "/federation\"";
    String successResponse =
        "{\"stellar_address\":\"bob*"
            + domain
            + "\",\"account_id\":\"GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY\"}";

    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(stellarToml));
    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(successResponse));

    FederationResponse response = Federation.resolve("bob*" + domain);
    assertEquals(response.getStellarAddress(), "bob*" + domain);
    assertEquals(
        response.getAccountId(), "GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY");
    assertNull(response.getMemoType());
    assertNull(response.getMemo());
  }

  @Test
  public void testMalformedAddress() {
    try {
      FederationResponse response = Federation.resolve("bob*stellar.org*test");
      fail("Expected exception");
    } catch (MalformedAddressException e) {
      //
    }
  }
}
