package org.stellar.sdk.federation;

import com.google.common.net.InternetDomainName;

import junit.framework.TestCase;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.message.BasicStatusLine;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class FederationServerTest extends TestCase {
  @Mock
  private HttpClient mockClient;
  @Mock
  private HttpResponse mockResponse;
  @Mock
  private HttpEntity mockEntity;

  private FederationServer server;

  private final StatusLine httpOK = new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
  private final String successResponse =
          "{\"stellar_address\":\"bob*stellar.org\",\"account_id\":\"GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY\"}";
  private final String successResponseWithMemo =
          "{\"stellar_address\":\"bob*stellar.org\",\"account_id\":\"GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY\", \"memo_type\": \"text\", \"memo\": \"test\"}";

  private final StatusLine httpNotFound = new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_NOT_FOUND, "Not Found");
  private final String notFoundResponse =
          "{\"code\":\"not_found\",\"message\":\"Account not found\"}";

  private final String stellarToml =
          "FEDERATION_SERVER = \"https://api.stellar.org/federation\"";

  public void setUp() throws URISyntaxException, IOException {
    MockitoAnnotations.initMocks(this);
    server = new FederationServer(
            "https://api.stellar.org/federation",
            InternetDomainName.from("stellar.org")
    );
    FederationServer.setHttpClient(mockClient);

    when(mockResponse.getEntity()).thenReturn(mockEntity);
    when(mockClient.execute((HttpGet) any(), (HttpClientContext) any())).thenReturn(mockResponse);
  }

  @Test
  public void testCreateForDomain() throws IOException {
    InputStream response = new ByteArrayInputStream(stellarToml.getBytes(StandardCharsets.UTF_8));
    when(mockResponse.getStatusLine()).thenReturn(httpOK);
    when(mockEntity.getContent()).thenReturn(response);

    FederationServer server = FederationServer.createForDomain(InternetDomainName.from("stellar.org"));
    assertEquals(server.getServerUri().toString(), "https://api.stellar.org/federation");
    assertEquals(server.getDomain().toString(), "stellar.org");

    ArgumentCaptor<HttpUriRequest> argument = ArgumentCaptor.forClass(HttpUriRequest.class);
    Mockito.verify(mockClient).execute(argument.capture(), (HttpClientContext) any());
    assertEquals(URI.create("https://stellar.org/.well-known/stellar.toml"), argument.getValue().getURI());
  }

  @Test
  public void testNameFederationSuccess() throws IOException {
    InputStream jsonResponse = new ByteArrayInputStream(successResponse.getBytes(StandardCharsets.UTF_8));
    when(mockResponse.getStatusLine()).thenReturn(httpOK);
    when(mockEntity.getContent()).thenReturn(jsonResponse);

    FederationResponse response = server.resolveAddress("bob*stellar.org");
    assertEquals(response.getStellarAddress(), "bob*stellar.org");
    assertEquals(response.getAccountId(), "GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY");
    assertNull(response.getMemoType());
    assertNull(response.getMemo());
  }

  @Test
  public void testNameFederationSuccessWithMemo() throws IOException {
    InputStream jsonResponse = new ByteArrayInputStream(successResponseWithMemo.getBytes(StandardCharsets.UTF_8));
    when(mockResponse.getStatusLine()).thenReturn(httpOK);
    when(mockEntity.getContent()).thenReturn(jsonResponse);

    FederationResponse response = server.resolveAddress("bob*stellar.org");
    assertEquals(response.getStellarAddress(), "bob*stellar.org");
    assertEquals(response.getAccountId(), "GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY");
    assertEquals(response.getMemoType(), "text");
    assertEquals(response.getMemo(), "test");
  }

  @Test
  public void testNameFederationNotFound() throws IOException {
    InputStream jsonResponse = new ByteArrayInputStream(notFoundResponse.getBytes(StandardCharsets.UTF_8));
    when(mockResponse.getStatusLine()).thenReturn(httpNotFound);
    when(mockEntity.getContent()).thenReturn(jsonResponse);

    try {
      FederationResponse response = server.resolveAddress("bob*stellar.org");
      fail("Expected exception");
    } catch (NotFoundException e) {}
  }
}
