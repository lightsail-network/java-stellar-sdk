package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;

public class FeeStatsResponse extends Response {
  @SerializedName("ledger_capacity_usage")
  private final Float ledgerCapacityUsage;

  @SerializedName("last_ledger_base_fee")
  private final Long lastLedgerBaseFee;

  @SerializedName("last_ledger")
  private final Long lastLedger;

  @SerializedName("fee_charged")
  private final FeeDistribution feeCharged;

  @SerializedName("max_fee")
  private final FeeDistribution maxFee;

  public FeeStatsResponse(
      Float ledgerCapacityUsage,
      Long lastLedgerBaseFee,
      Long lastLedger,
      FeeDistribution feeCharged,
      FeeDistribution maxFee) {
    this.ledgerCapacityUsage = ledgerCapacityUsage;
    this.lastLedgerBaseFee = lastLedgerBaseFee;
    this.lastLedger = lastLedger;
    this.feeCharged = feeCharged;
    this.maxFee = maxFee;
  }

  public Float getLedgerCapacityUsage() {
    return ledgerCapacityUsage;
  }

  public Long getLastLedgerBaseFee() {
    return lastLedgerBaseFee;
  }

  public Long getLastLedger() {
    return lastLedger;
  }

  public FeeDistribution getFeeCharged() {
    return feeCharged;
  }

  public FeeDistribution getMaxFee() {
    return maxFee;
  }
}
