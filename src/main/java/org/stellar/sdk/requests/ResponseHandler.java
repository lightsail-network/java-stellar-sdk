package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;

import okhttp3.Response;
import okhttp3.ResponseBody;
import org.stellar.sdk.responses.GsonSingleton;

import java.io.IOException;

public class ResponseHandler<T> {

  private TypeToken<T> type;

  /**
   * "Generics on a type are typically erased at runtime, except when the type is compiled with the
   * generic parameter bound. In that case, the compiler inserts the generic type information into
   * the compiled class. In other cases, that is not possible."
   * More info: http://stackoverflow.com/a/14506181
   * @param type
   */
  public ResponseHandler(TypeToken<T> type) {
    this.type = type;
  }

  public T handleResponse(final Response response) throws IOException, TooManyRequestsException {
    // Too Many Requests
    if (response.code() == 429) {
      int retryAfter = Integer.parseInt(response.header("Retry-After"));
      throw new TooManyRequestsException(retryAfter);
    }

    // No content
    ResponseBody body = response.body();
    if (body == null) {
      throw new RuntimeException("Response contains no content");
    }

    // Other errors
    if (response.code() >= 300) {
      throw new ErrorResponse(response.code(), body.string());
    }

    T object = GsonSingleton.getInstance().fromJson(body.string(), type.getType());
    if (object instanceof org.stellar.sdk.responses.Response) {
      ((org.stellar.sdk.responses.Response) object).setHeaders(response.headers());
    }
    return object;
  }
}
