package org.stellar.sdk.requests;


import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import org.stellar.sdk.responses.ClientProtocolException;
import org.stellar.sdk.responses.GsonSingleton;
import org.stellar.sdk.sse.OkSse;
import org.stellar.sdk.sse.ServerSentEvent;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;

import okhttp3.Request;

public class StreamHandler<T> {

  private TypeToken<T> type;
  private OkSse okSse = new OkSse();

  /**
   * "Generics on a type are typically erased at runtime, except when the type is compiled with the
   * generic parameter bound. In that case, the compiler inserts the generic type information into
   * the compiled class. In other cases, that is not possible."
   * More info: http://stackoverflow.com/a/14506181
   *
   * @param type
   */
  public StreamHandler(TypeToken<T> type) {
    this.type = type;
  }

  public ServerSentEvent handleStream(final URI uri, final EventListener<T> listener) throws IOException {
    try {
      Request request = new Request.Builder()
          .url(uri.toURL())
          .build();

      return okSse.newServerSentEvent(request, new ServerSentEvent.Listener() {
        @Override
        public void onOpen(ServerSentEvent sse, okhttp3.Response response) {
        }

        @Override
        public void onMessage(ServerSentEvent sse, String id, String event, String data) {
          if (data.equals("\"hello\"")) {
            return;
          }
          try {
            T object = GsonSingleton.getInstance().fromJson(data, type.getType());
            if (object != null) {
              listener.onEvent(object);
            }
          } catch (JsonParseException e) {
            e.printStackTrace();
          }
        }

        @Override
        public void onComment(ServerSentEvent sse, String comment) {
        }

        @Override
        public boolean onRetryTime(ServerSentEvent sse, long milliseconds) {
          return true;
        }

        @Override
        public boolean onRetryError(ServerSentEvent sse, Throwable throwable, okhttp3.Response response) {
          return true;
        }

        @Override
        public void onClosed(ServerSentEvent sse) {
        }

        @Override
        public Request onPreRetry(ServerSentEvent sse, Request originalRequest) {
          return originalRequest;
        }
      });
    } catch (MalformedURLException e) {
      throw new ClientProtocolException("Can't perform request", e);
    }
  }


}
