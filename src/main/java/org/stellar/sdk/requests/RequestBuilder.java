package org.stellar.sdk.requests;

import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public abstract class RequestBuilder {
  private URIBuilder uriBuilder;
  private ArrayList<String> segments;
  private boolean segmentsAdded;

  public RequestBuilder(URI serverURI, String defaultSegment) {
    uriBuilder = new URIBuilder(serverURI);
    segments = new ArrayList<String>();
    if (defaultSegment != null) {
      this.addSegments(defaultSegment);
    }
    segmentsAdded = false; // Allow overwriting segments
  }

  protected RequestBuilder addSegments(String ...segments) {
    if (segmentsAdded) {
      throw new RuntimeException("URL segments have been already added.");
    }

    segmentsAdded = true;
    // Remove default segments
    this.segments.clear();
    for (String segment : segments) {
      this.segments.add(segment);
    }

    return this;
  }

  public RequestBuilder cursor(String token) {
    uriBuilder.addParameter("cursor", token);
    return this;
  }

  public RequestBuilder limit(int number) {
    uriBuilder.addParameter("limit", String.valueOf(number));
    return this;
  }

  public RequestBuilder order(Order direction) {
    uriBuilder.addParameter("order", direction.getValue());
    return this;
  }

  URI buildUri() {
    if (segments.size() > 0) {
      String path = "";
      for (String segment : segments) {
        path += "/"+segment;
      }
      uriBuilder.setPath(path);
    }
    try {
      return uriBuilder.build();
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  public enum Order {
    ASC("asc"),
    DESC("desc");
    private final String value;
    Order(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }
  }
}
