package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import okhttp3.Response;
import org.stellar.sdk.responses.GsonSingleton;
import org.stellar.sdk.responses.TypedResponse;

public class ResponseHandler<T> {

  private TypeToken<T> type;

  /**
   * "Generics on a type are typically erased at runtime, except when the type is compiled with the
   * generic parameter bound. In that case, the compiler inserts the generic type information into
   * the compiled class. In other cases, that is not possible." More info:
   * http://stackoverflow.com/a/14506181
   *
   * @param type
   */
  public ResponseHandler(TypeToken<T> type) {
    this.type = type;
  }

  public T handleResponse(final Response response) throws IOException, TooManyRequestsException {
    try {
      // Too Many Requests
      if (response.code() == 429) {

        Integer retryAfter = null;
        String header = response.header("Retry-After");
        if (header != null) {
          try {
            retryAfter = Integer.parseInt(header);
          } catch (NumberFormatException ignored) {
          }
        }
        throw new TooManyRequestsException(retryAfter);
      }

      String content = response.body().string();

      // Other errors
      if (response.code() >= 300) {
        throw new ErrorResponse(response.code(), content);
      }

      T object = GsonSingleton.getInstance().fromJson(content, type.getType());
      if (object instanceof org.stellar.sdk.responses.Response) {
        ((org.stellar.sdk.responses.Response) object).setHeaders(response.headers());
      }
      if (object instanceof TypedResponse) {
        ((TypedResponse) object).setType(type);
      }
      return object;
    } finally {
      response.close();
    }
  }
}
