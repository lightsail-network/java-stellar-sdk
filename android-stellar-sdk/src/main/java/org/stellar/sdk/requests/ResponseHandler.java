package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;

import org.stellar.sdk.responses.ClientProtocolException;
import org.stellar.sdk.responses.GsonSingleton;
import org.stellar.sdk.responses.HttpResponseException;
import org.stellar.sdk.responses.Response;

import java.io.IOException;
import java.net.URI;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

public class ResponseHandler<T> {

  private TypeToken<T> type;
  private OkHttpClient client = new OkHttpClient();

  /**
   * "Generics on a type are typically erased at runtime, except when the type is compiled with the
   * generic parameter bound. In that case, the compiler inserts the generic type information into
   * the compiled class. In other cases, that is not possible."
   * More info: http://stackoverflow.com/a/14506181
   *
   * @param type
   */
  public ResponseHandler(TypeToken<T> type) {
    this.type = type;
  }

  public T handleGetRequest(final URI uri) throws IOException {
    return handleResponse(client.newCall(
        new Request.Builder()
            .url(uri.toString())
            .build()
    )
        .execute());
  }

  public T handleResponse(final okhttp3.Response response) throws IOException, TooManyRequestsException {
    // Too Many Requests
    if (response.code() == 429) {
      String retryAfterString = response.header("Retry-After");
      if (retryAfterString != null) {
        try {
          int retryAfter = Integer.parseInt(retryAfterString);
          throw new TooManyRequestsException(retryAfter);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      throw new TooManyRequestsException(0);
    }

    // Other errors
    if (response.code() >= 300) {
      throw new HttpResponseException(response.code(), response.message());
    }
    // No content
    ResponseBody responseBody = response.body();
    if (responseBody == null) {
      throw new ClientProtocolException("Response contains no content");
    }

    T object = GsonSingleton.getInstance().fromJson(responseBody.string(), type.getType());
    if (object instanceof Response) {
      ((Response) object).setHeaders(
          response.header("X-Ratelimit-Limit"),
          response.header("X-Ratelimit-Remaining"),
          response.header("X-Ratelimit-Reset")
      );
    }
    return object;
  }

}
