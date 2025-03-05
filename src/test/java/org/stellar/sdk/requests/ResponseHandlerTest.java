package org.stellar.sdk.requests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Assert;
import org.junit.Test;
import org.stellar.sdk.exception.TooManyRequestsException;
import org.stellar.sdk.http.Jdk11HttpClient;

public class ResponseHandlerTest {

  @Test
  public void testTooManyRequests() throws IOException {

    MockResponse response = new MockResponse();
    response.setResponseCode(429);
    response.setHeader("Retry-After", "10");

    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.start();
    mockWebServer.enqueue(response);

    final var httpClient = new Jdk11HttpClient.Builder().build();
    try {
      AccountsRequestBuilder.execute(httpClient, mockWebServer.url("/").uri());
      Assert.fail();
    } catch (TooManyRequestsException tmre) {
      assertEquals(10, tmre.getRetryAfter().intValue());
    } finally {

      mockWebServer.shutdown();
      mockWebServer.close();
    }
  }

  @Test
  public void testTooManyRequestsNoHeader() throws IOException, InterruptedException {

    MockResponse response = new MockResponse();
    response.setResponseCode(429);

    MockWebServer mockWebServer = new MockWebServer();

    mockWebServer.start();
    mockWebServer.enqueue(response);

    final var httpClient = new Jdk11HttpClient.Builder().build();

    try {
      AccountsRequestBuilder.execute(httpClient, mockWebServer.url("/").uri());
      Assert.fail();
    } catch (TooManyRequestsException tmre) {
      assertEquals(null, tmre.getRetryAfter());
    } finally {

      mockWebServer.shutdown();
      mockWebServer.close();
    }
  }
}
