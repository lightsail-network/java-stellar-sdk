package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import java.net.URI;
import java.net.URISyntaxException;

/** Represents links in responses. */
public class Link {
  @SerializedName("href")
  private final String href;

  @SerializedName("templated")
  private final boolean templated;

  Link(String href, boolean templated) {
    this.href = href;
    this.templated = templated;
  }

  public String getHref() {
    // TODO templated
    return href;
  }

  public URI getUri() {
    // TODO templated
    try {
      return new URI(href);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean isTemplated() {
    return templated;
  }
}
