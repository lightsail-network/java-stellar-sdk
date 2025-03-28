package org.stellar.sdk.requests;

import java.io.Closeable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.stellar.sdk.UriBuilder;
import org.stellar.sdk.UriUtil;
import org.stellar.sdk.Util;
import org.stellar.sdk.http.sse.ISseClient;
import org.stellar.sdk.http.sse.ISseEventStream;
import org.stellar.sdk.http.sse.SseContext;

public class SSEStream<T extends org.stellar.sdk.responses.Response> implements Closeable {
  static final long DEFAULT_RECONNECT_TIMEOUT = 15 * 1000L;

  private final ISseClient sseClient;
  private final RequestBuilder requestBuilder;
  private final Class<T> responseClass;
  private final EventListener<T> listener;
  private final AtomicBoolean isStopped = new AtomicBoolean(false);
  private final AtomicBoolean isClosed = new AtomicBoolean(true);
  private final AtomicLong latestEventTime = new AtomicLong(0);
  private final AtomicReference<String> lastEventId = new AtomicReference<>(null);
  private final ScheduledExecutorService executorService;
  private final AtomicReference<ISseEventStream> eventSource = new AtomicReference<>(null);
  private final long reconnectTimeout;
  private final AtomicLong currentListenerId = new AtomicLong(0);

  private SSEStream(
      final ISseClient sseClient,
      final RequestBuilder requestBuilder,
      final Class<T> responseClass,
      final EventListener<T> listener,
      final long reconnectTimeout) {
    this.sseClient = sseClient;
    this.requestBuilder = requestBuilder;
    this.responseClass = responseClass;
    this.listener = listener;
    this.reconnectTimeout = reconnectTimeout;

    executorService = Executors.newSingleThreadScheduledExecutor();
    requestBuilder.buildUri(); // call this once to add the segments
  }

  private void start() {
    if (isStopped.get()) {
      throw new IllegalStateException("Already stopped");
    }

    executorService.scheduleWithFixedDelay(
        () -> {
          if (System.currentTimeMillis() - latestEventTime.get() > reconnectTimeout) {
            latestEventTime.set(System.currentTimeMillis());
            isClosed.compareAndSet(false, true);
          }

          if (isClosed.get()) {
            isClosed.compareAndSet(true, false);
            if (!isStopped.get()) {
              restart();
            }
          }
        },
        0,
        200,
        TimeUnit.MILLISECONDS);
  }

  public String lastPagingToken() {
    return lastEventId.get();
  }

  private void restart() {
    final var currentEventSource = eventSource.get();

    if (currentEventSource != null) {
      currentEventSource.cancel();
    }

    // we cancelled the current event source, the `lastEventId` will not change anymore,
    // so we can safely restart with the same `lastEventId`
    long newListenerId = currentListenerId.incrementAndGet();
    eventSource.set(
        doStreamRequest(
            this,
            sseClient,
            requestBuilder,
            responseClass,
            listener,
            requestBuilder.uriBuilder.build().toString(),
            newListenerId));
  }

  @Override
  public void close() {
    if (isStopped.compareAndSet(false, true)) {
      ISseEventStream currentEventSource = eventSource.get();

      if (currentEventSource != null) {
        currentEventSource.cancel();
      }

      executorService.shutdownNow();
    }
  }

  static <T extends org.stellar.sdk.responses.Response> SSEStream<T> create(
      ISseClient sseClient,
      RequestBuilder requestBuilder,
      Class<T> responseClass,
      EventListener<T> listener,
      long reconnectTimeout) {
    SSEStream<T> stream =
        new SSEStream<>(sseClient, requestBuilder, responseClass, listener, reconnectTimeout);
    stream.start();
    return stream;
  }

  private static String addIdentificationQueryParameter(String url) {
    UriBuilder parsedUrl;

    try {
      parsedUrl = new UriBuilder(url);
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid URL: " + url);
    }

    parsedUrl.setQueryParameter("X-Client-Name", "java-stellar-sdk");
    parsedUrl.setQueryParameter("X-Client-Version", Util.getSdkVersion());
    return parsedUrl.build().toString();
  }

  private static <T extends org.stellar.sdk.responses.Response> ISseEventStream doStreamRequest(
      final SSEStream<T> stream,
      final ISseClient sseClient,
      final RequestBuilder requestBuilder,
      final Class<T> responseClass,
      final EventListener<T> listener,
      String url,
      long listenerId) {
    final var lastEventId = stream.lastEventId.get();

    final var context =
        SseContext.builder()
            .lastEventTime(stream.latestEventTime)
            .currentListenerId(stream.currentListenerId)
            .isClosed(stream.isClosed)
            .isStopped(stream.isStopped)
            .lastEventId(stream.lastEventId)
            .build();

    final var uri = UriUtil.toUri(addIdentificationQueryParameter(url));

    return sseClient.createEventStream(
        context, responseClass, listenerId, listener, requestBuilder, uri, lastEventId);
  }
}
