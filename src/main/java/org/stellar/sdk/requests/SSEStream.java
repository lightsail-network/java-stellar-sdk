package org.stellar.sdk.requests;

import java.io.Closeable;
import java.net.SocketException;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.sse.RealEventSource;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.Nullable;
import org.stellar.sdk.Util;
import org.stellar.sdk.responses.GsonSingleton;
import org.stellar.sdk.responses.Pageable;

public class SSEStream<T extends org.stellar.sdk.responses.Response> implements Closeable {
  static final long DEFAULT_RECONNECT_TIMEOUT = 15 * 1000L;
  private final OkHttpClient okHttpClient;
  private final RequestBuilder requestBuilder;
  private final Class<T> responseClass;
  private final EventListener<T> listener;
  private final AtomicBoolean isStopped = new AtomicBoolean(false);
  private final AtomicBoolean serverSideClosed =
      new AtomicBoolean(true); // make sure we start correctly

  // When the client closes the connection itself, it will be set to true.
  // This is for handling cases where the SSE (Server-Sent Events) does not
  // receive a response for a long time.
  // If the server requests us to close the connection, we will set serverSideClosed to true.
  private final AtomicBoolean clientSideClosed = new AtomicBoolean(true);
  private final AtomicLong latestEventTime =
      new AtomicLong(0); // The timestamp of the last received event.
  private final AtomicReference<String> lastEventId = new AtomicReference<String>(null);
  private final ExecutorService executorService;
  private EventSource eventSource = null;
  private final Lock lock = new ReentrantLock();
  private final long reconnectTimeout;

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

    executorService = Executors.newSingleThreadExecutor();
    requestBuilder.buildUri(); // call this once to add the segments
  }

  private void start() {
    if (isStopped.get()) {
      throw new IllegalStateException("Already stopped");
    }

    executorService.submit(
        new Runnable() {
          @Override
          public void run() {
            latestEventTime.set(System.currentTimeMillis());

            while (!isStopped.get()) {
              if (System.currentTimeMillis() - latestEventTime.get() > reconnectTimeout) {
                // Check if the client has not received any event for a long time.
                // If so, we will close the connection and restart it.
                latestEventTime.set(System.currentTimeMillis());
                clientSideClosed.set(true);
              }

              try {
                Thread.sleep(200);
                if (serverSideClosed.get() || clientSideClosed.get()) {
                  // don't restart until true again
                  serverSideClosed.set(false);
                  clientSideClosed.set(false);
                  if (!isStopped.get()) {
                    lock.lock();
                    try {
                      // check again if somebody called close in between
                      if (!isStopped.get()) {
                        restart();
                      }
                    } finally {
                      lock.unlock();
                    }
                  }
                }
              } catch (InterruptedException e) {
                throw new IllegalStateException("interrupted", e);
              }
            }
          }
        });
  }

  public String lastPagingToken() {
    return lastEventId.get();
  }

  private void restart() {
    if (eventSource != null) {
      eventSource.cancel();
    }
    eventSource =
        doStreamRequest(
            this,
            okHttpClient,
            requestBuilder,
            responseClass,
            listener,
            requestBuilder.uriBuilder.build().toString(),
            new CloseListener() {
              @Override
              public void closed(EventSource source) {
                serverSideClosed.set(true);
              }
            });
  }

  public void close() {
    isStopped.set(true);
    if (eventSource != null) {
      eventSource.cancel();
    }
    executorService.shutdownNow();
  }

  static <T extends org.stellar.sdk.responses.Response> SSEStream<T> create(
      final OkHttpClient okHttpClient,
      final RequestBuilder requestBuilder,
      final Class<T> responseClass,
      final EventListener<T> listener,
      final long reconnectTimeout) {
    SSEStream<T> stream =
        new SSEStream<T>(okHttpClient, requestBuilder, responseClass, listener, reconnectTimeout);
    stream.start();
    return stream;
  }

  private static String addIdentificationQueryParameter(String url) {
    HttpUrl.Builder urlBuilder =
        HttpUrl.parse(url)
            .newBuilder()
            .addQueryParameter("X-Client-Name", "java-stellar-sdk")
            .addQueryParameter("X-Client-Version", Util.getSdkVersion());
    return urlBuilder.build().toString();
  }

  private static <T extends org.stellar.sdk.responses.Response> EventSource doStreamRequest(
      final SSEStream<T> stream,
      final OkHttpClient okHttpClient,
      final RequestBuilder requestBuilder,
      final Class<T> responseClass,
      final EventListener<T> listener,
      String url,
      final CloseListener closeListener) {

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
            new StellarEventSourceListener<T>(
                stream, closeListener, responseClass, requestBuilder, listener));
    eventSource.connect(okHttpClient);
    return eventSource;
  }

  private interface CloseListener {
    void closed(EventSource source);
  }

  private static class StellarEventSourceListener<T extends org.stellar.sdk.responses.Response>
      extends EventSourceListener {

    private SSEStream<T> stream;
    private final CloseListener closeListener;
    private final Class<T> responseClass;
    private final RequestBuilder requestBuilder;
    private final EventListener<T> listener;

    StellarEventSourceListener(
        SSEStream<T> stream,
        CloseListener closeListener,
        Class<T> responseClass,
        RequestBuilder requestBuilder,
        EventListener<T> listener) {
      this.stream = stream;
      this.closeListener = closeListener;
      this.responseClass = responseClass;
      this.requestBuilder = requestBuilder;
      this.listener = listener;
    }

    @Override
    public void onClosed(EventSource eventSource) {
      if (closeListener != null) {
        closeListener.closed(eventSource);
      }
    }

    @Override
    public void onOpen(EventSource eventSource, Response response) {}

    @Override
    public void onFailure(
        EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
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
        EventSource eventSource, @Nullable String id, @Nullable String type, String data) {
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
