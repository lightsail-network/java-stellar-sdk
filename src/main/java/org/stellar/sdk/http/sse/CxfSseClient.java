package org.stellar.sdk.http.sse;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.sse.SseEventSource;
import java.net.URI;
import org.stellar.sdk.requests.EventListener;
import org.stellar.sdk.requests.RequestBuilder;

public class CxfSseClient implements ISseClient {
  private Client client;

  public CxfSseClient() {
    this.client = ClientBuilder.newClient();
  }

  @Override
  public <T extends org.stellar.sdk.responses.Response> ISseEventStream createEventStream(
      SseContext context,
      Class<T> responseClass,
      long listenerId,
      EventListener<T> eventListener,
      RequestBuilder requestBuilder,
      URI uri,
      String lastEventId) {
    final var sseEventSource = createEventSource(uri, lastEventId);

    return new CxfSseEventStream<>(
        context, sseEventSource, responseClass, listenerId, eventListener, requestBuilder);
  }

  private SseEventSource createEventSource(URI uri, String lastEventId) {
    final var webTarget = client.target(uri);

    if (lastEventId != null) {
      webTarget.request().header("Last-Event-ID", lastEventId);
    }

    return SseEventSource.target(webTarget).build();
  }
}
