package org.stellar.sdk.responses.sorobanrpc;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.List;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Value
public class SimulateTransactionResponse {
  String error;

  String transactionData;

  List<String> events;

  Long minResourceFee;

  // An array of the individual host function call results.
  // This will only contain a single element if present, because only a single
  // invokeHostFunctionOperation
  // is supported per transaction.
  List<SimulateHostFunctionResult> results;

  SimulateTransactionCost cost;

  RestorePreamble restorePreamble;

  Long latestLedger;

  @AllArgsConstructor
  @Value
  public static class SimulateHostFunctionResult {
    List<String> auth;

    String xdr;
  }

  @AllArgsConstructor
  @Value
  public static class SimulateTransactionCost {
    @SerializedName("cpuInsns")
    BigInteger cpuInstructions;

    @SerializedName("memBytes")
    BigInteger memoryBytes;
  }

  @AllArgsConstructor
  @Value
  public static class RestorePreamble {
    String transactionData;

    Long minResourceFee;
  }
}
