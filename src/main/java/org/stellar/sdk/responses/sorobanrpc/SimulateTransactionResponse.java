package org.stellar.sdk.responses.sorobanrpc;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.List;
import lombok.Value;

/**
 * Response for JSON-RPC method simulateTransaction.
 *
 * <p>Note - The simulation response will have different model representations with different
 * members present or absent depending on type of response that it is conveying. For example, the
 * simulation response for invoke host function, could be one of three types: error, success, or
 * restore operation needed.
 *
 * <p>Please refer to the latest <a
 * href="https://soroban.stellar.org/api/methods/simulateTransaction#returns"
 * target="_blank">Soroban simulateTransaction documentation</a> for details on which members of the
 * simulation response model are keyed to each type of response.
 */
@Value
public class SimulateTransactionResponse {
  String error;

  /** The field can be parsed as {@link org.stellar.sdk.xdr.SorobanTransactionData} object. */
  String transactionData;

  /** The elements inside can be parsed as {@link org.stellar.sdk.xdr.DiagnosticEvent} objects. */
  List<String> events;

  Long minResourceFee;

  // An array of the individual host function call results.
  // This will only contain a single element if present, because only a single
  // invokeHostFunctionOperation
  // is supported per transaction.
  List<SimulateHostFunctionResult> results;

  SimulateTransactionCost cost;

  RestorePreamble restorePreamble;

  List<LedgerEntryChange> stateChanges;

  Long latestLedger;

  @Value
  public static class SimulateHostFunctionResult {

    /**
     * The elements inside can be parsed as {@link org.stellar.sdk.xdr.SorobanAuthorizationEntry}
     * objects.
     */
    List<String> auth;

    /** The field can be parsed as {@link org.stellar.sdk.xdr.SCVal} object. */
    String xdr;
  }

  @Value
  public static class SimulateTransactionCost {
    @SerializedName("cpuInsns")
    BigInteger cpuInstructions;

    @SerializedName("memBytes")
    BigInteger memoryBytes;
  }

  @Value
  public static class RestorePreamble {
    /** The field can be parsed as {@link org.stellar.sdk.xdr.SorobanTransactionData} object. */
    String transactionData;

    Long minResourceFee;
  }

  /**
   * LedgerEntryChange designates a change in a ledger entry. Before and After cannot be omitted at
   * the same time. If Before is omitted, it constitutes a creation, if After is omitted, it
   * constitutes a deletion.
   */
  @Value
  public static class LedgerEntryChange {
    String type;
    String key;
    String before;
    String after;
  }
}
