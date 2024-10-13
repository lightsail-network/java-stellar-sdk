package org.stellar.sdk.responses.sorobanrpc;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Value;
import org.stellar.sdk.Util;
import org.stellar.sdk.requests.sorobanrpc.EventFilterType;
import org.stellar.sdk.xdr.SCVal;

/**
 * Response for JSON-RPC method getEvents.
 *
 * @see <a href="https://developers.stellar.org/docs/data/rpc/api-reference/methods/getEvents"
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

    /** The elements inside can be parsed as {@link org.stellar.sdk.xdr.SCVal} objects. */
    List<String> topic;

    /** The field can be parsed as {@link org.stellar.sdk.xdr.SCVal} object. */
    String value;

    Boolean inSuccessfulContractCall;

    @SerializedName("txHash")
    String transactionHash;

    /**
     * Parses the {@code topic} field from a list of strings to a list of {@link
     * org.stellar.sdk.xdr.SCVal} objects.
     *
     * @return a list of parsed {@link org.stellar.sdk.xdr.SCVal} objects
     */
    public List<SCVal> parseTopic() {
      if (topic == null) {
        return null;
      }
      return topic.stream()
          .map(t -> Util.parseXdr(t, SCVal::fromXdrBase64))
          .collect(Collectors.toList());
    }

    /**
     * Parses the {@code value} field from a string to an {@link org.stellar.sdk.xdr.SCVal} object.
     *
     * @return the parsed {@link org.stellar.sdk.xdr.SCVal} object
     */
    public SCVal parseValue() {
      return Util.parseXdr(value, SCVal::fromXdrBase64);
    }
  }
}
