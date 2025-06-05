package org.stellar.sdk.http;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class PostRequest extends HttpRequest {
  private final String requestBody;
  private final String contentType;

  public PostRequest(URI uri, String requestBody, String contentType) {
    super(uri);
    this.requestBody = requestBody;
    this.contentType = contentType;
  }

  public static PostRequest formBody(URI uri, Map<String, String> formFields) {
    final var formString =
        formFields.entrySet().stream()
            .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
            .collect(Collectors.joining("&"));

    return new PostRequest(uri, formString, "application/x-www-form-urlencoded");
  }

  public static PostRequest jsonBody(URI uri, String json) {
    return new PostRequest(uri, json, "application/json");
  }

  public boolean isEmptyBody() {
    return requestBody == null;
  }
}
