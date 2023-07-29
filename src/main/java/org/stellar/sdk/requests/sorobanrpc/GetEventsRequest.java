package org.stellar.sdk.requests.sorobanrpc;

import java.util.Collection;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

/**
 * Request for JSON-RPC method getEvents.
 *
 * @see <a href="https://soroban.stellar.org/api/methods/getEvents#parameters"
 *     target="_blank">getEvents documentation</a>
 */
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
