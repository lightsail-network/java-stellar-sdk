package org.stellar.sdk.http;

import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import org.stellar.sdk.StringUtil;

@Getter
@Builder
public class StringResponse {
  private final String responseBody;
  private final int statusCode;
  private final Map<String, LinkedList<String>> headers;

  public Optional<String> getHeader(String name) {
    if (headers == null || headers.isEmpty() || StringUtil.isBlank(name)) {
      return Optional.empty();
    }

    return headers.entrySet().stream()
        .filter(header -> header.getKey().equalsIgnoreCase(name))
        .map(header -> String.join(",", header.getValue()))
        .findFirst();
  }
}
