package org.stellar.sdk.http.sse;

import java.net.URI;
import org.stellar.sdk.requests.EventListener;
import org.stellar.sdk.requests.RequestBuilder;

public interface ISseClient {
  <T extends org.stellar.sdk.responses.Response> ISseEventStream createEventStream(
      SseContext context,
      Class<T> responseClass,
      long listenerId,
      EventListener<T> eventListener,
      RequestBuilder requestBuilder,
      URI uri,
      String lastEventId);
}
