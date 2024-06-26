package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import okhttp3.Response;
import org.stellar.sdk.exception.BadRequestException;
import org.stellar.sdk.exception.BadResponseException;
import org.stellar.sdk.exception.RequestTimeoutException;
import org.stellar.sdk.exception.TooManyRequestsException;
import org.stellar.sdk.exception.UnexpectedException;
import org.stellar.sdk.exception.UnknownResponseException;
import org.stellar.sdk.responses.GsonSingleton;
import org.stellar.sdk.responses.Problem;
import org.stellar.sdk.responses.TypedResponse;

@SuppressWarnings("unchecked")
public class ResponseHandler<T> {

  private final TypeToken<T> type;

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

  /**
   * Handles the HTTP response and converts it to the appropriate object or throws exceptions based
   * on the response status.
   *
   * @param response The HTTP response to handle
   * @return The parsed object of type T
   * @throws TooManyRequestsException If the response code is 429 (Too Many Requests)
   * @throws UnexpectedException If the response body is empty or there's an unexpected error
   *     reading the response
   * @throws BadRequestException If the response code is in the 4xx range
   * @throws BadResponseException If the response code is in the 5xx range
   */
  public T handleResponse(final Response response) {
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

      if (response.code() == 504) {
        throw new RequestTimeoutException();
      }

      String content = null;
      if (response.body() == null) {
        throw new UnexpectedException("Unexpected empty response body");
      }

      try {
        content = response.body().string();
      } catch (IOException e) {
        throw new UnexpectedException("Unexpected error reading response", e);
      }

      if (response.code() >= 200 && response.code() < 300) {
        T object = GsonSingleton.getInstance().fromJson(content, type.getType());
        if (object instanceof org.stellar.sdk.responses.Response) {
          ((org.stellar.sdk.responses.Response) object).setHeaders(response.headers());
        }
        if (object instanceof TypedResponse) {
          ((TypedResponse<T>) object).setType(type);
        }
        return object;
      }

      // Other errors
      if (response.code() >= 400 && response.code() < 600) {
        Problem problem = null;
        try {
          problem = GsonSingleton.getInstance().fromJson(content, Problem.class);
        } catch (Exception e) {
          // if we can't parse the response, we just ignore it
        }

        if (response.code() < 500) {
          // Codes in the 4xx range indicate an error that failed given the information provided
          throw new BadRequestException(response.code(), content, problem);
        } else {
          // Codes in the 5xx range indicate an error with the Horizon server.
          throw new BadResponseException(response.code(), content, problem);
        }
      }

      throw new UnknownResponseException(response.code(), content);
    } finally {
      response.close();
    }
  }
}
