package org.stellar.sdk;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.apache.http.client.fluent.Request;
import org.stellar.sdk.requests.ResponseHandler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Represents page of objects.
 */
public class Page<T> {
  @SerializedName("records")
  private ArrayList<T> records;
  @SerializedName("links")
  private Links links;

  Page() {}

  public ArrayList<T> getRecords() {
    return records;
  }

  public Links getLinks() {
    return links;
  }

  public Page<T> getNextPage() throws URISyntaxException, IOException {
    TypeToken type = new TypeToken<Page<T>>() {};
    ResponseHandler<Page<T>> responseHandler = new ResponseHandler<Page<T>>(type);
    URI uri = new URI(this.getLinks().getNext().getHref());
    return (Page<T>) Request.Get(uri).execute().handleResponse(responseHandler);
  }

  /**
   * Links connected to page response.
   */
  public static class Links {
    @SerializedName("next")
    private final Link next;
    @SerializedName("prev")
    private final Link prev;
    @SerializedName("self")
    private final Link self;

    Links(Link next, Link prev, Link self) {
      this.next = next;
      this.prev = prev;
      this.self = self;
    }

    public Link getNext() {
      return next;
    }

    public Link getPrev() {
      return prev;
    }

    public Link getSelf() {
      return self;
    }
  }
}
