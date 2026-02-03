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
import org.stellar.sdk.responses.Problem;
import org.stellar.sdk.responses.SubmitTransactionAsyncResponse;
import org.stellar.sdk.responses.gson.GsonSingleton;
import org.stellar.sdk.responses.gson.TypedResponse;

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
    return handleResponse(response, false);
  }

  /**
   * Handles the HTTP response with pre-read body content and converts it to the appropriate object
   * or throws exceptions based on the response status.
   *
   * <p>This method is useful when the caller needs to limit the response body size before
   * processing, for example to prevent denial-of-service attacks.
   *
   * <p><b>Note:</b> This method does NOT close the response. The caller is responsible for closing
   * the response, typically using try-with-resources on the response object.
   *
   * @param response The HTTP response to handle (used for status code and headers)
   * @param content The pre-read response body content
   * @return The parsed object of type T
   * @throws TooManyRequestsException If the response code is 429 (Too Many Requests)
   * @throws BadRequestException If the response code is in the 4xx range
   * @throws BadResponseException If the response code is in the 5xx range
   */
  public T handleResponse(final Response response, String content) {
    return handleResponseContent(response, content, false);
  }

  /**
   * Handles the HTTP response and converts it to the appropriate object or throws exceptions based
   * on the response status.
   *
   * @param response The HTTP response to handle
   * @param submitTransactionAsync Only set it to true when calling {@link
   *     org.stellar.sdk.Server#submitTransactionXdrAsync(String)}.
   * @return The parsed object of type T
   * @throws TooManyRequestsException If the response code is 429 (Too Many Requests)
   * @throws UnexpectedException If the response body is empty or there's an unexpected error
   *     reading the response
   * @throws BadRequestException If the response code is in the 4xx range
   * @throws BadResponseException If the response code is in the 5xx range
   */
  public T handleResponse(final Response response, boolean submitTransactionAsync) {
    try {
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

      if (response.body() == null) {
        throw new UnexpectedException("Unexpected empty response body");
      }

      String content;
      try {
        content = response.body().string();
      } catch (IOException e) {
        throw new UnexpectedException("Unexpected error reading response", e);
      }

      return handleResponseContent(response, content, submitTransactionAsync);
    } finally {
      response.close();
    }
  }

  private T handleResponseContent(
      final Response response, String content, boolean submitTransactionAsync) {
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

    if (response.code() >= 200 && response.code() < 300) {
      T object = GsonSingleton.getInstance().fromJson(content, type.getType());
      if (object instanceof TypedResponse) {
        ((TypedResponse<T>) object).setType(type);
      }
      return object;
    }

    // Other errors
    if (response.code() >= 400 && response.code() < 600) {
      Problem problem = null;
      SubmitTransactionAsyncResponse submitTransactionAsyncProblem = null;
      try {
        problem = GsonSingleton.getInstance().fromJson(content, Problem.class);
      } catch (Exception e) {
        // if we can't parse the response, we just ignore it
      }

      if (submitTransactionAsync) {
        try {
          submitTransactionAsyncProblem =
              GsonSingleton.getInstance().fromJson(content, SubmitTransactionAsyncResponse.class);
        } catch (Exception e) {
          // if we can't parse the response, we just ignore it
        }
      }

      if (response.code() == 504) {
        throw new RequestTimeoutException(response.code(), content, problem);
      } else if (response.code() < 500) {
        // Codes in the 4xx range indicate an error that failed given the information provided
        throw new BadRequestException(
            response.code(), content, problem, submitTransactionAsyncProblem);
      } else {
        // Codes in the 5xx range indicate an error with the Horizon server.
        throw new BadResponseException(
            response.code(), content, problem, submitTransactionAsyncProblem);
      }
    }

    throw new UnknownResponseException(response.code(), content);
  }
}
