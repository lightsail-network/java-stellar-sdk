package org.stellar.sdk.http.sse;

import jakarta.ws.rs.sse.SseEventSource;
import java.net.SocketException;
import java.util.Optional;
import lombok.Getter;
import org.stellar.sdk.requests.EventListener;
import org.stellar.sdk.requests.RequestBuilder;
import org.stellar.sdk.responses.Pageable;
import org.stellar.sdk.responses.gson.GsonSingleton;

/**
 * SSE event handler backed by Apache CXF.
 *
 * @see https://cxf.apache.org/docs/sse.html
 * @param <T> the SDK response class.
 */
@Getter
public class CxfSseEventStream<T extends org.stellar.sdk.responses.Response>
    implements ISseEventStream {
  private final SseContext context;
  private final SseEventSource eventSource;
  private final Class<T> responseClass;
  private final long listenerId;
  private final EventListener<T> eventListener;
  private final RequestBuilder requestBuilder;

  public CxfSseEventStream(
      SseContext context,
      SseEventSource eventSource,
      Class<T> responseClass,
      long listenerId,
      EventListener<T> eventListener,
      RequestBuilder requestBuilder) {
    this.context = context;
    this.eventSource = eventSource;
    this.responseClass = responseClass;
    this.listenerId = listenerId;
    this.eventListener = eventListener;
    this.requestBuilder = requestBuilder;
    this.eventSource.register(
        inboundSseEvent -> {
          // onEvent
          if (context.getIsStopped().get() || listenerId != context.getCurrentListenerId().get()) {
            return;
          }

          context.getLastEventTime().set(System.currentTimeMillis());

          final var eventDataString = inboundSseEvent.readData();

          if (eventDataString.equals("\"hello\"") || eventDataString.equals("\"byebye\"")) {
            return;
          }

          T event = GsonSingleton.getInstance().fromJson(eventDataString, responseClass);

          if (event instanceof Pageable) {
            String pagingToken = ((Pageable) event).getPagingToken();
            requestBuilder.cursor(pagingToken);
          }

          context.getLastEventId().set(inboundSseEvent.getId());
          eventListener.onEvent(event);
        },
        throwable -> {
          // onError
          if (context.getIsStopped().get() || listenerId != context.getCurrentListenerId().get()) {
            return;
          }

          Optional<Integer> code = Optional.empty();

          // TODO: Figure out how to produce a code.
          // if (response != null) {
          //     code = Optional.of(response.code());
          // }

          if (throwable != null) {
            if (throwable instanceof SocketException) {
              context.getIsClosed().compareAndSet(false, true);
            } else {
              eventListener.onFailure(Optional.of(throwable), code);
            }
          } else {
            eventListener.onFailure(Optional.empty(), code);
          }
        },
        () -> {
          // onComplete
          if (context.getIsStopped().get() || listenerId != context.getCurrentListenerId().get()) {
            return;
          }

          context.getIsClosed().compareAndSet(false, true);
        });
  }

  @Override
  public void cancel() {
    if (eventSource != null) {
      eventSource.close();
    }
  }
}
