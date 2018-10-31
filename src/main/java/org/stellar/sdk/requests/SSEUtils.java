package org.stellar.sdk.requests;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.sse.RealEventSource;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.stellar.sdk.responses.GsonSingleton;

import javax.annotation.Nullable;
import java.net.SocketException;


public class SSEUtils {
  public static <T extends org.stellar.sdk.responses.Response> SSEManager<T> stream(final OkHttpClient okHttpClient, final RequestBuilder requestBuilder, final Class<T> clazz, final EventListener<T> listener) {
    SSEManager<T> sseManager = new SSEManager<T>(okHttpClient, requestBuilder, clazz, listener);
    sseManager.start();
    return sseManager;
  }

  static <T extends org.stellar.sdk.responses.Response> EventSource streamInternal(final OkHttpClient okHttpClient, final RequestBuilder requestBuilder, final Class<T> clazz, final EventListener<T> listener, String url, final CloseListener closeListener ) {

    Request request = new Request.Builder()
            .url(url)
            .header("Accept","text/event-stream")
            .build();
    RealEventSource eventSource = new RealEventSource(request, new EventSourceListener() {

      @Override
      public void onClosed(EventSource eventSource) {
        if(closeListener!=null) {
          closeListener.closed(eventSource);
        }
      }

      @Override
      public void onOpen(EventSource eventSource, Response response) {
      }

      @Override
      public void onFailure(EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
        int code = -1;
        if(response != null) {
          code = response.code();
        }
        if(t!=null) {
          if(t instanceof SocketException) {
            // not a failure, server disconnected
          } else {
            throw new IllegalStateException("Failed " + code, t);
          }
        } else {
          throw new IllegalStateException("Failed " + code);
        }
      }

      @Override
      public void onEvent(EventSource eventSource, @Nullable String id, @Nullable String type, String data) {
        if (data.equals("\"hello\"")) {
          return;
        }
        T event = GsonSingleton.getInstance().fromJson(data, clazz);
        requestBuilder.cursor(event.getPagingToken());
        listener.onEvent(event);
      }
    });

    eventSource.connect(okHttpClient);
    return eventSource;
  }
}
