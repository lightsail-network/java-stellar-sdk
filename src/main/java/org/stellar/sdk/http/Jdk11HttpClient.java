package org.stellar.sdk.http;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ExecutorService;
import javax.net.ssl.SSLContext;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.stellar.sdk.StringUtil;

@AllArgsConstructor
public class Jdk11HttpClient implements IHttpClient {
  public static class Builder {
    private final Map<String, LinkedList<String>> defaultHeaders = new HashMap<>();
    private Duration connectTimeout = Duration.of(10, ChronoUnit.SECONDS);
    private Duration readTimeout = Duration.of(30, ChronoUnit.SECONDS);
    private boolean retryOnConnectionFailure = false;
    private SSLContext sslContext;

    public Builder withSslContext(SSLContext sslContext) {
      this.sslContext = sslContext;
      return this;
    }

    public Builder withConnectTimeout(long amount, @NotNull ChronoUnit chronoUnit) {
      if (amount < 0) {
        throw new IllegalArgumentException("Timeout cannot be negative.");
      }

      this.connectTimeout = Duration.of(amount, chronoUnit);
      return this;
    }

    public Builder withReadTimeout(long amount, @NotNull ChronoUnit chronoUnit) {
      if (amount < 0) {
        throw new IllegalArgumentException("Timeout cannot be negative.");
      }

      this.readTimeout = Duration.of(amount, chronoUnit);
      return this;
    }

    public Builder withRetryOnConnectionFailure(boolean retryOnConnectionFailure) {
      this.retryOnConnectionFailure = retryOnConnectionFailure;
      return this;
    }

    public Builder withDefaultHeader(@NotNull String name, String value) {
      if (name.isBlank()) {
        throw new IllegalArgumentException("Header name cannot be blank.");
      }

      defaultHeaders.put(name, new LinkedList<>(List.of(value)));
      return this;
    }

    public Jdk11HttpClient build() {
      final var builder = HttpClient.newBuilder().connectTimeout(connectTimeout);

      if (sslContext != null) {
        builder.sslContext(sslContext);
      }

      return new Jdk11HttpClient(
          builder.build(), readTimeout, retryOnConnectionFailure, defaultHeaders);
    }
  }

  private final HttpClient httpClient;
  private final Duration readTimeout;

  // TODO: Implement retry logic from OkHttp.
  private final boolean retryOnConnectionFailure;

  private final Map<String, LinkedList<String>> defaultHeaders;

  @Override
  public void close() throws Exception {
    if (httpClient != null) {
      httpClient
          .executor()
          .ifPresent(
              executor -> {
                if (executor instanceof ExecutorService) {
                  ((ExecutorService) executor).shutdownNow();
                }
              });
    }
  }

  @Override
  public StringResponse get(GetRequest request) throws IOException {
    final var builder = HttpRequest.newBuilder(request.getUri()).GET().timeout(readTimeout);

    applyDefaultHeaders(builder);

    return execute(builder.build());
  }

  @Override
  public StringResponse post(PostRequest request) throws IOException {
    final var builder =
        HttpRequest.newBuilder(request.getUri())
            .POST(
                request.isEmptyBody()
                    ? HttpRequest.BodyPublishers.noBody()
                    : HttpRequest.BodyPublishers.ofString(request.getRequestBody()))
            .header("Content-Type", request.getContentType())
            .timeout(readTimeout);

    applyDefaultHeaders(builder);

    return execute(builder.build());
  }

  private StringResponse execute(HttpRequest request) throws IOException {
    try {
      return toStringResponse(httpClient.send(request, HttpResponse.BodyHandlers.ofString()));
    } catch (InterruptedException e) {
      throw new RuntimeException("Interrupted during HTTP call.", e);
    }
  }

  private void applyDefaultHeaders(HttpRequest.Builder builder) {
    if (defaultHeaders != null && !defaultHeaders.isEmpty()) {
      defaultHeaders.forEach(
          (name, values) -> {
            final var valueString = StringUtil.join(values, ",");
            builder.header(name, valueString);
          });
    }
  }

  private static StringResponse toStringResponse(HttpResponse<String> responseBody) {
    return StringResponse.builder()
        .responseBody(responseBody.body())
        .statusCode(responseBody.statusCode())
        .headers(toHeadersMap(responseBody.headers()))
        .build();
  }

  private static Map<String, LinkedList<String>> toHeadersMap(HttpHeaders headers) {
    final var returnMap = new HashMap<String, LinkedList<String>>();

    headers
        .map()
        .forEach(
            (name, values) -> {
              returnMap.put(name, values == null ? new LinkedList<>() : new LinkedList<>(values));
            });

    return returnMap;
  }
}
