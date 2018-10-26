package org.stellar.sdk.requests;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.internal.sse.RealEventSource;
import okhttp3.sse.EventSourceListener;
import org.stellar.sdk.responses.GsonSingleton;

import javax.annotation.Nullable;

public class SSEUtils {
  public static <T> okhttp3.sse.EventSource stream(OkHttpClient okHttpClient, HttpUrl httpUrl, final Class<T> clazz, final EventListener<T> listener) {
    Request request = new Request.Builder().url(httpUrl).build();

    return new RealEventSource(request, new EventSourceListener() {
      @Override
      public void onEvent(okhttp3.sse.EventSource eventSource, @Nullable String id, @Nullable String type, String data) {
        if (data.equals("\"hello\"")) {
          return;
        }
        listener.onEvent(GsonSingleton.getInstance().fromJson(data, clazz));
      }
    });
  }
}
