package org.stellar.sdk;

import com.google.gson.annotations.SerializedName;

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
    return href;
  }

  public boolean isTemplated() {
    return templated;
  }
}
