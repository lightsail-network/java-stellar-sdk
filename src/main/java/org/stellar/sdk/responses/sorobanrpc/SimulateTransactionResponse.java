package org.stellar.sdk.responses.sorobanrpc;

import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * Response for JSON-RPC method simulateTransaction.
 *
 * @see <a href="https://soroban.stellar.org/api/methods/simulateTransaction#returns"
 *     target="_blank">simulateTransaction documentation</a>
 */
@AllArgsConstructor
@Value
public class SimulateTransactionResponse {
  String error;

  String transactionData;

  ImmutableList<String> events;

  Long minResourceFee;

  ImmutableList<SimulateHostFunctionResult> results;

  SimulateTransactionCost cost;

  Long latestLedger;

  @AllArgsConstructor
  @Value
  public static class SimulateHostFunctionResult {
    ImmutableList<String> auth;

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
}
