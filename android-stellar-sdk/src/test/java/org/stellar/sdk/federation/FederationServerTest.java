package org.stellar.sdk.federation;

import com.google.common.net.InternetDomainName;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 23)
public class FederationServerTest extends TestCase {

  private final String successResponse =
      "{\"stellar_address\":\"bob*stellar.org\",\"account_id\":\"GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY\"}";
  private final String successResponseWithMemo =
      "{\"stellar_address\":\"bob*stellar.org\",\"account_id\":\"GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY\", \"memo_type\": \"text\", \"memo\": \"test\"}";

  private final String notFoundResponse =
      "{\"code\":\"not_found\",\"message\":\"Account not found\"}";
  private final String stellarToml =
      "FEDERATION_SERVER = \"https://api.stellar.org/federation\"";

  private OkHttpClient fakeClient;
  private MockWebServer mockWebServer;
  private FederationServer server;

  @Before
  public void setUp() throws URISyntaxException, IOException {
    MockitoAnnotations.initMocks(this);
    fakeClient = new OkHttpClient();
    mockWebServer = new MockWebServer();
    mockWebServer.start();

    FederationServer.setHttpClient(fakeClient);
    server = new FederationServer(
        mockWebServer.url("/federation").toString(),
        InternetDomainName.from("stellar.org")
    );
  }

  @Test
  public void testCreateForDomain() throws IOException {
    OkHttpClient mockClient = mock(OkHttpClient.class);
    FederationServer.setHttpClient(mockClient);
    Call mockCall = mock(Call.class);
    when(mockClient.newCall((Request) any())).thenReturn(mockCall);
    Response mockResponseSuccess = new Response.Builder()
        .code(200)
        .message("OK")
        .protocol(Protocol.HTTP_1_1)
        .body(ResponseBody.create(MediaType.parse("application/json"), stellarToml))
        .request(new Request.Builder()
            .url("http://www.test.com")
            .get()
            .build())
        .build();

    when(mockCall.execute()).thenReturn(mockResponseSuccess);

    FederationServer server = FederationServer.createForDomain(InternetDomainName.from("stellar.org"));
    assertEquals(server.getServerUri().toString(), "https://api.stellar.org/federation");
    assertEquals(server.getDomain().toString(), "stellar.org");

    ArgumentCaptor<Request> argument = ArgumentCaptor.forClass(Request.class);
    verify(mockClient).newCall(argument.capture());

    assertEquals(URI.create("https://stellar.org/.well-known/stellar.toml"), argument.getValue().url().uri());
  }

  @Test
  public void testNameFederationSuccess() throws IOException {
    mockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setBody(successResponse)
    );

    FederationResponse response = server.resolveAddress("bob*stellar.org");
    assertEquals(response.getStellarAddress(), "bob*stellar.org");
    assertEquals(response.getAccountId(), "GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY");
    assertNull(response.getMemoType());
    assertNull(response.getMemo());
  }

  @Test
  public void testNameFederationSuccessWithMemo() throws IOException {
    mockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setBody(successResponseWithMemo)
    );

    FederationResponse response = server.resolveAddress("bob*stellar.org");
    assertEquals(response.getStellarAddress(), "bob*stellar.org");
    assertEquals(response.getAccountId(), "GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY");
    assertEquals(response.getMemoType(), "text");
    assertEquals(response.getMemo(), "test");
  }

  @Test
  public void testNameFederationNotFound() throws IOException {
    mockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(404)
            .setBody(notFoundResponse)
    );

    try {
      FederationResponse response = server.resolveAddress("bob*stellar.org");
      fail("Expected exception");
    } catch (NotFoundException e) {
    }
  }
}
