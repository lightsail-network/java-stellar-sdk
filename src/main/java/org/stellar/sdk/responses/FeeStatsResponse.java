package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;

public class FeeStatsResponse extends Response {
    @SerializedName("min_accepted_fee")
    private final Long min;
    @SerializedName("mode_accepted_fee")
    private final Long mode;
    @SerializedName("p10_accepted_fee")
    private final Long p10;
    @SerializedName("p20_accepted_fee")
    private final Long p20;
    @SerializedName("p30_accepted_fee")
    private final Long p30;
    @SerializedName("p40_accepted_fee")
    private final Long p40;
    @SerializedName("p50_accepted_fee")
    private final Long p50;
    @SerializedName("p60_accepted_fee")
    private final Long p60;
    @SerializedName("p70_accepted_fee")
    private final Long p70;
    @SerializedName("p80_accepted_fee")
    private final Long p80;
    @SerializedName("p90_accepted_fee")
    private final Long p90;
    @SerializedName("p95_accepted_fee")
    private final Long p95;
    @SerializedName("p99_accepted_fee")
    private final Long p99;
    @SerializedName("ledger_capacity_usage")
    private final Float ledgerCapacityUsage;
    @SerializedName("last_ledger_base_fee")
    private final Long lastLedgerBaseFee;
    @SerializedName("last_ledger")
    private final Long lastLedger;

    public FeeStatsResponse(Long min, Long mode, Long p10, Long p20, Long p30, Long p40, Long p50, Long p60, Long p70, Long p80, Long p90, Long p95, Long p99, Float ledgerCapacityUsage, Long lastLedgerBaseFee, Long lastLedger) {
        this.min = min;
        this.mode = mode;
        this.p10 = p10;
        this.p20 = p20;
        this.p30 = p30;
        this.p40 = p40;
        this.p50 = p50;
        this.p60 = p60;
        this.p70 = p70;
        this.p80 = p80;
        this.p90 = p90;
        this.p95 = p95;
        this.p99 = p99;
        this.ledgerCapacityUsage = ledgerCapacityUsage;
        this.lastLedgerBaseFee = lastLedgerBaseFee;
        this.lastLedger = lastLedger;
    }

    public Long getMin() {
        return min;
    }

    public Long getMode() {
        return mode;
    }

    public Long getP10() {
        return p10;
    }

    public Long getP20() {
        return p20;
    }

    public Long getP30() {
        return p30;
    }

    public Long getP40() {
        return p40;
    }

    public Long getP50() {
        return p50;
    }

    public Long getP60() {
        return p60;
    }

    public Long getP70() {
        return p70;
    }

    public Long getP80() {
        return p80;
    }

    public Long getP90() {
        return p90;
    }

    public Long getP95() {
        return p95;
    }

    public Long getP99() {
        return p99;
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

}
