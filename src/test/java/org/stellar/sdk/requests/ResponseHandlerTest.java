package org.stellar.sdk.requests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Optional;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Assert;
import org.junit.Test;
import org.stellar.sdk.exception.TooManyRequestsException;

public class ResponseHandlerTest {

  @Test
  public void testTooManyRequests() throws IOException, InterruptedException {

    MockResponse response = new MockResponse();
    response.setResponseCode(429);
    response.setHeader("Retry-After", "10");

    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.start();
    mockWebServer.enqueue(response);

    OkHttpClient okHttpClient = new OkHttpClient().newBuilder().build();
    try {

      AccountsRequestBuilder.execute(okHttpClient, mockWebServer.url("/"));
      Assert.fail();
    } catch (TooManyRequestsException tmre) {
      assertEquals(Optional.of(10), tmre.getRetryAfter());
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

    OkHttpClient okHttpClient = new OkHttpClient().newBuilder().build();

    try {
      AccountsRequestBuilder.execute(okHttpClient, mockWebServer.url("/"));
      Assert.fail();
    } catch (TooManyRequestsException tmre) {
      assertEquals(Optional.empty(), tmre.getRetryAfter());
    } finally {

      mockWebServer.shutdown();
      mockWebServer.close();
    }
  }
}
