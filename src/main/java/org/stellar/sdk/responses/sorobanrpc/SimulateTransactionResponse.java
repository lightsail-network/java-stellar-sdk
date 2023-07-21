package org.stellar.sdk.responses.sorobanrpc;

import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class SimulateTransactionResponse {
  @SerializedName("error")
  String error;

  @SerializedName("transactionData")
  String transactionData;

  @SerializedName("events")
  ImmutableList<String> events;

  @SerializedName("minResourceFee")
  Long minResourceFee;

  @SerializedName("results")
  ImmutableList<SimulateHostFunctionResult> results;

  @SerializedName("cost")
  SimulateTransactionCost cost;

  @SerializedName("latestLedger")
  Long latestLedger;

  @AllArgsConstructor
  @Value
  public static class SimulateHostFunctionResult {
    @SerializedName("auth")
    ImmutableList<String> auth;

    @SerializedName("xdr")
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
