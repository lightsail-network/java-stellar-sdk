package org.stellar.sdk.requests;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.sse.RealEventSource;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.stellar.sdk.responses.GsonSingleton;

import javax.annotation.Nullable;
import java.net.URI;
import java.util.concurrent.atomic.AtomicBoolean;

public class SSEUtils {
  public static <T extends org.stellar.sdk.responses.Response> EventSource stream(final OkHttpClient okHttpClient, final RequestBuilder requestBuilder, final Class<T> clazz, final EventListener<T> listener) {
    String url = requestBuilder.buildUri().toString();
    Request request = new Request.Builder()
            .url(url)
            .header("Accept","text/event-stream")
            .build();


    return streamInternal(okHttpClient, requestBuilder, clazz, listener, url);
  }

  private static <T extends org.stellar.sdk.responses.Response> EventSource streamInternal(final OkHttpClient okHttpClient, final RequestBuilder requestBuilder, final Class<T> clazz, final EventListener<T> listener, String url) {

    Request request = new Request.Builder()
            .url(url)
            .header("Accept","text/event-stream")
            .build();
    RealEventSource eventSource = new RealEventSource(request, new EventSourceListener() {
      private String cursor = null;

      @Override
      public void onClosed(EventSource eventSource) {
        if(cursor!= null) {
          requestBuilder.cursor(cursor);
          streamInternal(okHttpClient,requestBuilder,clazz,listener,requestBuilder.uriBuilder.build().toString());
        }
      }

      @Override
      public void onOpen(EventSource eventSource, Response response) {
      }

      @Override
      public void onFailure(EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
        this.cursor = null;
        if(t!=null)
          throw new IllegalStateException("Failed " + response.code(),t);
        else
          throw new IllegalStateException("Failed " + response.code());
      }

      @Override
      public void onEvent(EventSource eventSource, @Nullable String id, @Nullable String type, String data) {
        if (data.equals("\"hello\"")) {
          return;
        }
        T event = GsonSingleton.getInstance().fromJson(data, clazz);
        this.cursor = event.getPagingToken();
        listener.onEvent(event);
      }
    });

    eventSource.connect(okHttpClient);
    return eventSource;
  }
}
