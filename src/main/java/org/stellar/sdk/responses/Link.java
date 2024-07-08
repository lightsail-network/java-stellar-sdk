package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.Value;
import org.stellar.sdk.exception.UnexpectedException;

/** Represents links in responses. */
@Value
public class Link {
  @SerializedName("href")
  String href;

  @SerializedName("templated")
  boolean templated;
}
