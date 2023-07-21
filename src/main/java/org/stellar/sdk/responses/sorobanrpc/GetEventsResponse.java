package org.stellar.sdk.responses.sorobanrpc;

import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.stellar.sdk.requests.sorobanrpc.EventFilterType;

@AllArgsConstructor
@Value
public class GetEventsResponse {
  @SerializedName("events")
  ImmutableList<EventInfo> events;

  @SerializedName("latestLedger")
  Long latestLedger;

  @AllArgsConstructor
  @Value
  public static class EventInfo {
    @SerializedName("type")
    EventFilterType type;

    @SerializedName("ledger")
    Integer ledger;

    @SerializedName("ledgerClosedAt")
    String ledgerClosedAt;

    @SerializedName("contractId")
    String contractId;

    @SerializedName("id")
    String id;

    @SerializedName("pagingToken")
    String pagingToken;

    @SerializedName("topic")
    ImmutableList<String> topic;

    @SerializedName("value")
    EventInfoValue value;

    @SerializedName("inSuccessfulContractCall")
    Boolean inSuccessfulContractCall;
  }

  @AllArgsConstructor
  @Value
  public static class EventInfoValue {
    @SerializedName("xdr")
    String xdr;
  }
}
