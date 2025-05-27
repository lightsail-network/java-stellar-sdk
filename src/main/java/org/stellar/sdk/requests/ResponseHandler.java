package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import org.stellar.sdk.exception.BadRequestException;
import org.stellar.sdk.exception.BadResponseException;
import org.stellar.sdk.exception.RequestTimeoutException;
import org.stellar.sdk.exception.TooManyRequestsException;
import org.stellar.sdk.exception.UnexpectedException;
import org.stellar.sdk.exception.UnknownResponseException;
import org.stellar.sdk.http.StringResponse;
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
  public T handleResponse(final StringResponse response) {
    return handleResponse(response, false);
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
  public T handleResponse(final StringResponse response, boolean submitTransactionAsync) {
    // Too Many Requests
    if (response.getStatusCode() == 429) {

      Integer retryAfter = null;
      final var headerMaybe = response.getHeader("Retry-After");
      if (headerMaybe.isPresent()) {
        try {
          retryAfter = Integer.parseInt(headerMaybe.get());
        } catch (NumberFormatException ignored) {
        }
      }
      throw new TooManyRequestsException(retryAfter);
    }

    String content = null;
    if (response.getResponseBody() == null) {
      throw new UnexpectedException("Unexpected empty response body");
    }

    content = response.getResponseBody();

    if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
      T object = GsonSingleton.getInstance().fromJson(content, type.getType());
      if (object instanceof TypedResponse) {
        ((TypedResponse<T>) object).setType(type);
      }
      return object;
    }

    // Other errors
    if (response.getStatusCode() >= 400 && response.getStatusCode() < 600) {
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

      if (response.getStatusCode() == 504) {
        throw new RequestTimeoutException(response.getStatusCode(), content, problem);
      } else if (response.getStatusCode() < 500) {
        // Codes in the 4xx range indicate an error that failed given the information provided
        throw new BadRequestException(
            response.getStatusCode(), content, problem, submitTransactionAsyncProblem);
      } else {
        // Codes in the 5xx range indicate an error with the Horizon server.
        throw new BadResponseException(
            response.getStatusCode(), content, problem, submitTransactionAsyncProblem);
      }
    }

    throw new UnknownResponseException(response.getStatusCode(), content);
  }
}
