package org.stellar.sdk.responses.sorobanrpc;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.stellar.sdk.requests.sorobanrpc.EventFilterType;

/** Response for JSON-RPC method getEvents. */
@AllArgsConstructor
@Value
public class GetEventsResponse {
  ImmutableList<EventInfo> events;

  Long latestLedger;

  @AllArgsConstructor
  @Value
  public static class EventInfo {
    EventFilterType type;

    Integer ledger;

    String ledgerClosedAt;

    String contractId;

    String id;

    String pagingToken;

    ImmutableList<String> topic;

    EventInfoValue value;

    Boolean inSuccessfulContractCall;
  }

  @AllArgsConstructor
  @Value
  public static class EventInfoValue {
    String xdr;
  }
}
