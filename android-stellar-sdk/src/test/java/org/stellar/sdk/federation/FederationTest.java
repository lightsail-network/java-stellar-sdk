package org.stellar.sdk.federation;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.net.URISyntaxException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class FederationTest extends TestCase {

  @Mock
  private OkHttpClient fakeClient;
  @Mock
  private Call mockCall;

  private final String stellarToml =
          "FEDERATION_SERVER = \"https://api.stellar.org/federation\"";
  private final String successResponse =
          "{\"stellar_address\":\"bob*stellar.org\",\"account_id\":\"GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY\"}";

  @Before
  public void setUp() throws URISyntaxException, IOException {
    MockitoAnnotations.initMocks(this);
    FederationServer.setHttpClient(fakeClient);

    when(fakeClient.newCall((Request) any())).thenReturn(mockCall);
  }

  @Test
  public void testResolveSuccess() throws IOException {
    Response mockResponseToml = new Response.Builder()
        .code(200)
        .message("OK")
        .protocol(Protocol.HTTP_1_1)
        .body(ResponseBody.create(MediaType.parse("application/json"), stellarToml))
        .request(new Request.Builder()
            .url("http://www.test.com")
            .get()
            .build())
        .build();
    Response mockResponseSuccess = new Response.Builder()
        .code(200)
        .message("OK")
        .protocol(Protocol.HTTP_1_1)
        .body(ResponseBody.create(MediaType.parse("application/json"), successResponse))
        .request(new Request.Builder()
            .url("http://www.test.com")
            .get()
            .build())
        .build();

    when(mockCall.execute()).thenReturn(mockResponseToml, mockResponseSuccess);

    FederationResponse response = Federation.resolve("bob*stellar.org");
    assertEquals(response.getStellarAddress(), "bob*stellar.org");
    assertEquals(response.getAccountId(), "GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY");
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
