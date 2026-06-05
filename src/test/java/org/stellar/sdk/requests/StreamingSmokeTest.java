package org.stellar.sdk.requests;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.stellar.sdk.Server;
import org.stellar.sdk.responses.AccountResponse;
import org.stellar.sdk.responses.LedgerResponse;

public class StreamingSmokeTest {

  @Test
  public void shouldStreamFromRequestBuilderEndpointPath() throws Exception {
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.enqueue(
        new MockResponse()
            .setHeader("Content-Type", "text/event-stream")
            .setBody("data: \"hello\"\n\n"));

    Server server = new Server(mockWebServer.url("").toString());
    SSEStream<AccountResponse> stream =
        server.accounts().limit(10).stream(
            new EventListener<AccountResponse>() {
              @Override
              public void onEvent(AccountResponse response) {}

              @Override
              public void onFailure(Optional<Throwable> error, Optional<Integer> responseCode) {}
            });

    try {
      RecordedRequest request = mockWebServer.takeRequest(5, TimeUnit.SECONDS);
      Assert.assertNotNull(request);
      // request.getPath() is the relative path + query; resolve it against the mock server's
      // base URL to inspect the segments and query parameters the SDK actually sent.
      HttpUrl url = mockWebServer.url("").resolve(request.getPath());
      Assert.assertNotNull(url);
      Assert.assertEquals("/accounts", url.encodedPath());
      Assert.assertEquals("10", url.queryParameter("limit"));
      Assert.assertEquals("java-stellar-sdk", url.queryParameter("X-Client-Name"));
    } finally {
      stream.close();
      server.close();
      mockWebServer.shutdown();
    }
  }

  @Test
  @Ignore // lets not run this by default for now
  public void shouldStreamLedgersFromTestNet() {
    final AtomicInteger events = new AtomicInteger();
    Server server = new Server("https://horizon-testnet.stellar.org/");
    SSEStream<LedgerResponse> manager = null;
    try {
      manager =
          server.ledgers().limit(100).stream(
              new EventListener<LedgerResponse>() {
                @Override
                public void onEvent(LedgerResponse r) {
                  events.incrementAndGet();
                }

                @Override
                public void onFailure(Optional<Throwable> error, Optional<Integer> responseCode) {}
              });

      try {
        Thread.sleep(30000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      manager.close();

      int eventCount = events.get();
      Assert.assertTrue(eventCount > 0);

      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      Assert.assertTrue(events.get() == eventCount);

    } finally {
      if (manager != null) {
        manager.close();
      }
    }
  }
}
