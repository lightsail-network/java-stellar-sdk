package org.stellar.sdk.requests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Test;
import org.stellar.sdk.Server;
import org.stellar.sdk.exception.BadRequestException;

public class RootRequestBuilderTest {
  @Test
  public void testRoot() {
    Server server = new Server("https://horizon-testnet.stellar.org");

    assertEquals("https://horizon-testnet.stellar.org/", server.root().buildUri().toString());

    server.close();
  }

  @Test
  public void testRootRequestPath() throws IOException, InterruptedException {
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.enqueue(new MockResponse().setResponseCode(404));
    mockWebServer.start();
    Server server = new Server(mockWebServer.url("").toString());

    try {
      server.root().execute();
      fail("expected root request to return 404");
    } catch (BadRequestException e) {
      assertEquals(404, e.getCode().intValue());

      RecordedRequest request = mockWebServer.takeRequest();
      assertEquals("GET", request.getMethod());
      assertEquals("/", request.getPath());
    } finally {
      server.close();
      mockWebServer.shutdown();
    }
  }
}
