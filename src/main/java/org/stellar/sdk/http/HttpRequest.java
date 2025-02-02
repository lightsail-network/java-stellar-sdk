package org.stellar.sdk.http;

import java.net.URI;
import java.util.LinkedHashMap;
import lombok.Getter;

@Getter
public abstract class HttpRequest {
  protected final URI uri;
  protected final LinkedHashMap<String, String> requestHeaders = new LinkedHashMap<>();

  protected HttpRequest(URI uri) {
    this.uri = uri;
  }
}
