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
  Float ledgerCapacityUsage;

  @SerializedName("fee_charged")
  FeeDistribution feeCharged;

  @SerializedName("max_fee")
  FeeDistribution maxFee;
}
