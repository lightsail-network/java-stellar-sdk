package org.stellar.sdk;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

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
