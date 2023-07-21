package org.stellar.sdk.requests.sorobanrpc;

import com.google.gson.annotations.SerializedName;
import java.util.Collection;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

@Value
@Builder
public class GetEventsRequest {
  @NonNull
  @SerializedName("startLedger")
  String startLedger;

  @SerializedName("filters")
  @Singular("filter")
  Collection<EventFilter> filters;

  @SerializedName("pagination")
  PaginationOptions pagination;

  @Value
  @Builder
  public static class PaginationOptions {
    @SerializedName("limit")
    Long limit;

    @SerializedName("cursor")
    String cursor;
  }

  @Builder
  @Value
  public static class EventFilter {
    @SerializedName("type")
    EventFilterType type;

    @SerializedName("contractIds")
    Collection<String> contractIds;

    @Singular("topic")
    @SerializedName("topics")
    Collection<Collection<String>> topics;
  }
}
