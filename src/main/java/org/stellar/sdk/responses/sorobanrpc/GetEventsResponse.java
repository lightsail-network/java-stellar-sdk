package org.stellar.sdk.responses.sorobanrpc;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.stellar.sdk.requests.sorobanrpc.EventFilterType;

/**
 * Response for JSON-RPC method getEvents.
 *
 * @see <a href="https://soroban.stellar.org/api/methods/getEvents#returns"
 *     target="_blank">getEvents documentation</a>
 */
@AllArgsConstructor
@Value
public class GetEventsResponse {
  List<EventInfo> events;

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

    List<String> topic;

    EventInfoValue value;

    Boolean inSuccessfulContractCall;
  }

  @AllArgsConstructor
  @Value
  public static class EventInfoValue {
    String xdr;
  }
}
