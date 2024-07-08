package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class FeeStatsResponse extends Response {
  @SerializedName("last_ledger")
  Long lastLedger;

  @SerializedName("last_ledger_base_fee")
  Long lastLedgerBaseFee;

  @SerializedName("ledger_capacity_usage")
  String ledgerCapacityUsage;

  @SerializedName("fee_charged")
  FeeDistribution feeCharged;

  @SerializedName("max_fee")
  FeeDistribution maxFee;

  @Value
  public static class FeeDistribution {
    @SerializedName("min")
    Long min;

    @SerializedName("max")
    Long max;

    @SerializedName("mode")
    Long mode;

    @SerializedName("p10")
    Long p10;

    @SerializedName("p20")
    Long p20;

    @SerializedName("p30")
    Long p30;

    @SerializedName("p40")
    Long p40;

    @SerializedName("p50")
    Long p50;

    @SerializedName("p60")
    Long p60;

    @SerializedName("p70")
    Long p70;

    @SerializedName("p80")
    Long p80;

    @SerializedName("p90")
    Long p90;

    @SerializedName("p95")
    Long p95;

    @SerializedName("p99")
    Long p99;
  }
}
