package org.stellar.sdk.federation;

import junit.framework.TestCase;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.message.BasicStatusLine;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class FederationTest extends TestCase {
  @Mock
  private HttpClient mockClient;
  @Mock
  private HttpResponse mockResponse;
  @Mock
  private HttpEntity mockEntity;

  private final StatusLine httpOK = new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
  private final String stellarToml =
          "FEDERATION_SERVER = \"https://api.stellar.org/federation\"";
  private final String successResponse =
          "{\"stellar_address\":\"bob*stellar.org\",\"account_id\":\"GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY\"}";

  public void setUp() throws URISyntaxException, IOException {
    MockitoAnnotations.initMocks(this);
    FederationServer.setHttpClient(mockClient);
    when(mockResponse.getEntity()).thenReturn(mockEntity);
    when(mockClient.execute((HttpGet) any(), (HttpClientContext) any())).thenReturn(mockResponse);
  }

  @Test
  public void testResolveSuccess() throws IOException {
    InputStream stellarTomlresponse = new ByteArrayInputStream(stellarToml.getBytes(StandardCharsets.UTF_8));
    InputStream federationResponse = new ByteArrayInputStream(successResponse.getBytes(StandardCharsets.UTF_8));

    when(mockResponse.getStatusLine()).thenReturn(httpOK, httpOK);
    when(mockEntity.getContent()).thenReturn(stellarTomlresponse, federationResponse);

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
