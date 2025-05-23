package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import org.stellar.sdk.exception.ConnectionErrorException;
import org.stellar.sdk.exception.RequestTimeoutException;
import org.stellar.sdk.exception.UnexpectedException;
import org.stellar.sdk.http.GetRequest;
import org.stellar.sdk.http.IHttpClient;
import org.stellar.sdk.http.StringResponse;
import org.stellar.sdk.requests.ResponseHandler;
import org.stellar.sdk.responses.gson.TypedResponse;

/**
 * Represents page of objects.
 *
 * @see <a href="https://developers.stellar.org/api/introduction/pagination/" target="_blank">Page
 *     documentation</a>
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class Page<T> extends Response implements TypedResponse<Page<T>> {

  @SerializedName("records")
  private List<T> records;

  @SerializedName("links")
  private Links links;

  private TypeToken<Page<T>> type;

  Page() {}

  /**
   * @return The next page of results or null when there is no link for the next page of results
   * @throws org.stellar.sdk.exception.NetworkException All the exceptions below are subclasses of
   *     NetworkError
   * @throws org.stellar.sdk.exception.BadRequestException if the request fails due to a bad request
   *     (4xx)
   * @throws org.stellar.sdk.exception.BadResponseException if the request fails due to a bad
   *     response from the server (5xx)
   * @throws org.stellar.sdk.exception.TooManyRequestsException if the request fails due to too many
   *     requests sent to the server
   * @throws org.stellar.sdk.exception.RequestTimeoutException When Horizon returns a <code>Timeout
   *     </code> or connection timeout occurred
   * @throws org.stellar.sdk.exception.UnknownResponseException if the server returns an unknown
   *     status code
   * @throws org.stellar.sdk.exception.ConnectionErrorException When the request cannot be executed
   *     due to cancellation or connectivity problems, etc.
   */
  public Page<T> getNextPage(IHttpClient httpClient) {
    if (this.getLinks().getNext() == null) {
      return null;
    }
    if (this.type == null) {
      throw new NullPointerException(
          "type cannot be null, is it being correctly set after the creation of this "
              + getClass().getSimpleName()
              + "?");
    }
    ResponseHandler<Page<T>> responseHandler = new ResponseHandler<Page<T>>(this.type);
    String url = this.getLinks().getNext().getHref();

    StringResponse response;
    try {
      final var request = new GetRequest(new URI(url));
      response = httpClient.get(request);
    } catch (SocketTimeoutException e) {
      throw new RequestTimeoutException(e);
    } catch (IOException e) {
      throw new ConnectionErrorException(e);
    } catch (URISyntaxException e) {
      throw new UnexpectedException(e);
    }
    return responseHandler.handleResponse(response);
  }

  @Override
  public void setType(TypeToken<Page<T>> type) {
    this.type = type;
  }

  /** Links connected to page response. */
  @Value
  public static class Links {
    @SerializedName("next")
    Link next;

    @SerializedName("prev")
    Link prev;

    @SerializedName("self")
    Link self;
  }
}
