package org.stellar.sdk.responses.sorobanrpc;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Value;
import org.stellar.sdk.requests.sorobanrpc.EventFilterType;

/**
 * Response for JSON-RPC method getEvents.
 *
 * @see <a href="https://soroban.stellar.org/api/methods/getEvents#returns"
 *     target="_blank">getEvents documentation</a>
 */
@Value
public class GetEventsResponse {
  List<EventInfo> events;

  Long latestLedger;

  @Value
  public static class EventInfo {
    EventFilterType type;

    Long ledger;

    String ledgerClosedAt;

    String contractId;

    String id;

    String pagingToken;

    List<String> topic;

    String value;

    Boolean inSuccessfulContractCall;

    @SerializedName("txHash")
    String transactionHash;
  }
}
