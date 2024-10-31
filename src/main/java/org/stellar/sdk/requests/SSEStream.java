package org.stellar.sdk.requests;

import java.io.Closeable;
import java.net.SocketException;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.sse.RealEventSource;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.stellar.sdk.Util;
import org.stellar.sdk.responses.Pageable;
import org.stellar.sdk.responses.gson.GsonSingleton;

public class SSEStream<T extends org.stellar.sdk.responses.Response> implements Closeable {
  static final long DEFAULT_RECONNECT_TIMEOUT = 15 * 1000L;

  private final OkHttpClient okHttpClient;
  private final RequestBuilder requestBuilder;
  private final Class<T> responseClass;
  private final EventListener<T> listener;
  private final AtomicBoolean isStopped = new AtomicBoolean(false);
  private final AtomicBoolean isClosed = new AtomicBoolean(true);
  private final AtomicLong latestEventTime = new AtomicLong(0);
  private final AtomicReference<String> lastEventId = new AtomicReference<>(null);
  private final ScheduledExecutorService executorService;
  private final AtomicReference<EventSource> eventSource = new AtomicReference<>(null);
  private final long reconnectTimeout;
  private final AtomicLong currentListenerId = new AtomicLong(0);

  private SSEStream(
      final OkHttpClient okHttpClient,
      final RequestBuilder requestBuilder,
      final Class<T> responseClass,
      final EventListener<T> listener,
      final long reconnectTimeout) {
    // Create a new client with no read timeout
    this.okHttpClient = okHttpClient.newBuilder().readTimeout(0, TimeUnit.MILLISECONDS).build();
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
    EventSource currentEventSource = eventSource.get();
    if (currentEventSource != null) {
      currentEventSource.cancel();
    }
    // we cancelled the current event source, the `lastEventId` will not change anymore,
    // so we can safely restart with the same `lastEventId`
    long newListenerId = currentListenerId.incrementAndGet();
    eventSource.set(
        doStreamRequest(
            this,
            okHttpClient,
            requestBuilder,
            responseClass,
            listener,
            requestBuilder.uriBuilder.build().toString(),
            source -> isClosed.compareAndSet(false, true),
            newListenerId));
  }

  @Override
  public void close() {
    if (isStopped.compareAndSet(false, true)) {
      EventSource currentEventSource = eventSource.get();
      if (currentEventSource != null) {
        currentEventSource.cancel();
      }
      executorService.shutdownNow();
    }
  }

  static <T extends org.stellar.sdk.responses.Response> SSEStream<T> create(
      OkHttpClient okHttpClient,
      RequestBuilder requestBuilder,
      Class<T> responseClass,
      EventListener<T> listener,
      long reconnectTimeout) {
    SSEStream<T> stream =
        new SSEStream<>(okHttpClient, requestBuilder, responseClass, listener, reconnectTimeout);
    stream.start();
    return stream;
  }

  private static String addIdentificationQueryParameter(String url) {
    HttpUrl parsedUrl = HttpUrl.parse(url);
    if (parsedUrl == null) {
      throw new IllegalArgumentException("Invalid URL: " + url);
    }

    return parsedUrl
        .newBuilder()
        .addQueryParameter("X-Client-Name", "java-stellar-sdk")
        .addQueryParameter("X-Client-Version", Util.getSdkVersion())
        .build()
        .toString();
  }

  private static <T extends org.stellar.sdk.responses.Response> EventSource doStreamRequest(
      final SSEStream<T> stream,
      final OkHttpClient okHttpClient,
      final RequestBuilder requestBuilder,
      final Class<T> responseClass,
      final EventListener<T> listener,
      String url,
      final CloseListener closeListener,
      long listenerId) {

    Request.Builder builder =
        new Request.Builder()
            .url(addIdentificationQueryParameter(url))
            .header("Accept", "text/event-stream");
    String lastEventId = stream.lastEventId.get();
    if (lastEventId != null) {
      builder.header("Last-Event-ID", lastEventId);
    }
    Request request = builder.build();
    RealEventSource eventSource =
        new RealEventSource(
            request,
            new StellarEventSourceListener<>(
                stream, closeListener, responseClass, requestBuilder, listener, listenerId));
    eventSource.connect(okHttpClient);
    return eventSource;
  }

  private interface CloseListener {
    void closed(EventSource source);
  }

  private static class StellarEventSourceListener<T extends org.stellar.sdk.responses.Response>
      extends EventSourceListener {
    private final SSEStream<T> stream;
    private final CloseListener closeListener;
    private final Class<T> responseClass;
    private final RequestBuilder requestBuilder;
    private final EventListener<T> listener;
    private final long listenerId;

    StellarEventSourceListener(
        SSEStream<T> stream,
        CloseListener closeListener,
        Class<T> responseClass,
        RequestBuilder requestBuilder,
        EventListener<T> listener,
        long listenerId) {
      this.stream = stream;
      this.closeListener = closeListener;
      this.responseClass = responseClass;
      this.requestBuilder = requestBuilder;
      this.listener = listener;
      this.listenerId = listenerId;
    }

    @Override
    public void onClosed(@NotNull EventSource eventSource) {
      if (stream.isStopped.get() || listenerId != stream.currentListenerId.get()) {
        return;
      }
      if (closeListener != null) {
        closeListener.closed(eventSource);
      }
    }

    @Override
    public void onOpen(@NotNull EventSource eventSource, @NotNull Response response) {}

    @Override
    public void onFailure(
        @NotNull EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
      if (stream.isStopped.get() || listenerId != stream.currentListenerId.get()) {
        return;
      }
      Optional<Integer> code = Optional.empty();
      if (response != null) {
        code = Optional.of(response.code());
      }
      if (t != null) {
        if (t instanceof SocketException) {
          if (closeListener != null) {
            closeListener.closed(eventSource);
          }
        } else {
          listener.onFailure(Optional.of(t), code);
        }
      } else {
        Optional<Throwable> absent = Optional.empty();
        listener.onFailure(absent, code);
      }
    }

    @Override
    public void onEvent(
        @NotNull EventSource eventSource,
        @Nullable String id,
        @Nullable String type,
        @NotNull String data) {
      if (stream.isStopped.get() || listenerId != stream.currentListenerId.get()) {
        return;
      }
      // Update the timestamp of the last received event.
      stream.latestEventTime.set(System.currentTimeMillis());

      if (data.equals("\"hello\"") || data.equals("\"byebye\"")) {
        return;
      }
      T event = GsonSingleton.getInstance().fromJson(data, responseClass);
      if (event instanceof Pageable) {
        String pagingToken = ((Pageable) event).getPagingToken();
        requestBuilder.cursor(pagingToken);
      }
      stream.lastEventId.set(id);
      listener.onEvent(event);
    }
  }
}
