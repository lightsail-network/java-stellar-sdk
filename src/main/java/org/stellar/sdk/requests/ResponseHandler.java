package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.stellar.sdk.responses.GsonSingleton;
import org.stellar.sdk.responses.Response;

import java.io.IOException;
import java.io.StringWriter;

public class ResponseHandler<T> implements org.apache.http.client.ResponseHandler {

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

  public T handleResponse(final HttpResponse response) throws IOException, TooManyRequestsException {
    StatusLine statusLine = response.getStatusLine();
    HttpEntity entity = response.getEntity();

    // Too Many Requests
    if (statusLine.getStatusCode() == 429) {
      int retryAfter = Integer.parseInt(response.getFirstHeader("Retry-After").getValue());
      throw new TooManyRequestsException(retryAfter);
    }
    // Other errors
    if (statusLine.getStatusCode() >= 300) {
      throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
    }
    // No content
    if (entity == null) {
      throw new ClientProtocolException("Response contains no content");
    }

    StringWriter writer = new StringWriter();
    IOUtils.copy(entity.getContent(), writer);
    String content = writer.toString();

    T object = GsonSingleton.getInstance().fromJson(content, type.getType());
    if (object instanceof Response) {
      ((Response) object).setHeaders(
              response.getFirstHeader("X-Ratelimit-Limit"),
              response.getFirstHeader("X-Ratelimit-Remaining"),
              response.getFirstHeader("X-Ratelimit-Reset")
      );
    }
    return object;
  }
}
