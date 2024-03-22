package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.stellar.sdk.requests.ResponseHandler;

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
  private ArrayList<T> records;

  @SerializedName("links")
  private Links links;

  private TypeToken<Page<T>> type;

  Page() {}

  /**
   * @return The next page of results or null when there is no link for the next page of results
   * @throws URISyntaxException
   * @throws IOException
   */
  public Page<T> getNextPage(OkHttpClient httpClient) throws URISyntaxException, IOException {
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

    Request request = new Request.Builder().get().url(url).build();
    okhttp3.Response response = httpClient.newCall(request).execute();

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
