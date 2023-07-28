package org.stellar.sdk.requests.sorobanrpc;

import java.util.Collection;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class GetEventsRequest {
  @NonNull String startLedger;

  @Singular("filter")
  Collection<EventFilter> filters;

  PaginationOptions pagination;

  @Value
  @Builder(toBuilder = true)
  public static class PaginationOptions {
    Long limit;

    String cursor;
  }

  @Builder(toBuilder = true)
  @Value
  public static class EventFilter {
    EventFilterType type;

    Collection<String> contractIds;

    @Singular("topic")
    Collection<Collection<String>> topics;
  }
}
