package org.stellar.sdk.requests;


import com.google.gson.reflect.TypeToken;
import com.here.oksse.OkSse;
import com.here.oksse.ServerSentEvent;

import org.stellar.sdk.responses.AccountResponse;
import org.stellar.sdk.responses.ClientProtocolException;
import org.stellar.sdk.responses.GsonSingleton;
import org.stellar.sdk.responses.HttpResponseException;
import org.stellar.sdk.responses.Response;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class StreamHandler<T> {

  private TypeToken<T> type;
  OkSse okSse = new OkSse();

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
          T object = GsonSingleton.getInstance().fromJson(data, type.getType());
          listener.onEvent(object);
        }

        @Override
        public void onComment(ServerSentEvent sse, String comment) {
        }

        @Override
        public boolean onRetryTime(ServerSentEvent sse, long milliseconds) {
          return false;
        }

        @Override
        public boolean onRetryError(ServerSentEvent sse, Throwable throwable, okhttp3.Response response) {
          return false;
        }

        @Override
        public void onClosed(ServerSentEvent sse) {
        }

        @Override
        public Request onPreRetry(ServerSentEvent sse, Request originalRequest) {
          return null;
        }
      });
    } catch (MalformedURLException e) {
      throw new ClientProtocolException("Can't perform request", e);
    }
  }


}
