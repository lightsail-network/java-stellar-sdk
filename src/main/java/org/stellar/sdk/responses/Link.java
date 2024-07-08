package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import lombok.Value;

/** Represents links in responses. */
@Value
public class Link {
  @SerializedName("href")
  String href;

  @SerializedName("templated")
  Boolean templated;
}
