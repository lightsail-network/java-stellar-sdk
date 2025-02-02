package org.stellar.sdk;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class UriBuilder {
  private String scheme;
  private String host;
  private Integer port;
  private final LinkedList<String> pathSegments;
  private final LinkedHashMap<String, String> queryParams = new LinkedHashMap<>();

  public UriBuilder(String uriString) {
    this(UriUtil.toUri(uriString));
  }

  public UriBuilder(URI uri) {
    this.scheme = uri.getScheme();
    this.host = uri.getHost();
    this.port = uri.getPort() < 0 ? null : uri.getPort();
    this.pathSegments = splitPathParts(uri.getPath());
    this.queryParams.putAll(parseQueryParams(uri.getQuery()));
  }

  public URI build() {
    final var builder = new StringBuilder();
    builder.append(scheme).append("://").append(host);

    if (port != null) {
      builder.append(":").append(port);
    }

    if (!pathSegments.isEmpty()) {
      pathSegments.forEach(
          s -> {
            if (builder.charAt(builder.length() - 1) != '/') {
              builder.append("/");
            }

            builder.append(s);
          });
    }

    if (!queryParams.isEmpty()) {
      builder.append("?");
      queryParams.forEach(
          (name, value) -> {
            if (builder.charAt(builder.length() - 1) != '?') {
              builder.append("&");
            }

            builder.append(name);

            if (value != null) {
              builder.append("=").append(value);
            }
          });
    }

    try {
      return new URI(builder.toString());
    } catch (URISyntaxException e) {
      throw new RuntimeException("Invalid URI: " + builder, e);
    }
  }

  public void removeAllQueryParameters(String name) {
    queryParams.remove(name);
  }

  public UriBuilder setScheme(String scheme) {
    if (StringUtil.isBlank(scheme)) {
      throw new IllegalArgumentException("Scheme cannot be blank.");
    }

    this.scheme = scheme;
    return this;
  }

  public UriBuilder setHost(String host) {
    if (StringUtil.isBlank(host)) {
      throw new IllegalArgumentException("Host cannot be blank.");
    }

    this.host = host;
    return this;
  }

  public UriBuilder setPort(Integer port) {
    if (port != null && (port < 1 || port > 65535)) {
      throw new IllegalArgumentException("Port must be a positive number between 1 and 65535.");
    }

    this.port = port;
    return this;
  }

  public boolean hasQueryParameter(String name) {
    return getQueryParameter(name).isPresent();
  }

  public Optional<String> getQueryParameter(String name) {
    return Optional.ofNullable(queryParams.get(name));
  }

  public UriBuilder setQueryParameter(String name, String value) {
    if (StringUtil.isBlank(name)) {
      throw new IllegalArgumentException("Query parameter name cannot be blank.");
    }

    name = urlEncode(name.trim());
    value = value != null ? urlEncode(value) : null;

    this.queryParams.put(name, value);
    return this;
  }

  public UriBuilder addPathSegment(String pathSegment) {
    if (StringUtil.isBlank(pathSegment)) {
      throw new IllegalArgumentException("Path segment cannot be blank.");
    }

    pathSegments.add(pathSegment);
    return this;
  }

  private static LinkedHashMap<String, String> parseQueryParams(String queryParamsString) {
    if (StringUtil.isBlank(queryParamsString)) {
      return new LinkedHashMap<>();
    } else {
      final var paramsMap = new LinkedHashMap<String, String>();
      final var pattern = Pattern.compile("\\??(?:&?[^=&]*=[^=&]*)*");
      final var matcher = pattern.matcher(queryParamsString);
      while (matcher.find()) {
        final var parts = matcher.group(1).split("=", 2);
        if (parts.length == 1 || StringUtil.isBlank(parts[1])) {
          paramsMap.put(parts[0], "");
        } else {
          paramsMap.put(parts[0], parts[1]);
        }
      }

      return paramsMap;
    }
  }

  private static LinkedList<String> splitPathParts(String path) {
    if (StringUtil.isBlank(path)) {
      return new LinkedList<>();
    } else {
      if (path.startsWith("/")) {
        path = path.substring(1);
      }

      return Arrays.stream(path.split("/")).collect(Collectors.toCollection(LinkedList::new));
    }
  }

  private static String urlEncode(String input) {
    return input == null ? null : URLEncoder.encode(input, StandardCharsets.UTF_8);
  }
}
