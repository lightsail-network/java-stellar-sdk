package org.stellar.sdk.requests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Test;
import org.stellar.sdk.http.GetRequest;
import org.stellar.sdk.http.Jdk11HttpClient;

public class ClientIdHeadersTest {
  @Test
  public void testClientIdentificationInterceptor() throws IOException, InterruptedException {
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.start();
    mockWebServer.enqueue(new MockResponse());

    final var httpClient =
        new Jdk11HttpClient.Builder()
            .withDefaultHeader("X-Client-Name", "java-stellar-sdk")
            .withDefaultHeader("X-Client-Version", "dev")
            .build();

    httpClient.get(new GetRequest(mockWebServer.url("/").uri()));

    RecordedRequest request = mockWebServer.takeRequest();
    assertEquals("java-stellar-sdk", request.getHeader("X-Client-Name"));
    assertEquals("dev", request.getHeader("X-Client-Version"));

    mockWebServer.shutdown();
    mockWebServer.close();
  }
}
