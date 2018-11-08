package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;

public class OperationFeeStatsResponse extends Response {
    @SerializedName("min_accepted_fee")
    private final Long min;
    @SerializedName("mode_accepted_fee")
    private final Long mode;
    @SerializedName("last_ledger_base_fee")
    private final Long lastLedgerBaseFee;
    @SerializedName("last_ledger")
    private final Long lastLedger;

    public OperationFeeStatsResponse(Long min, Long mode, Long lastLedgerBaseFee, Long lastLedger) {
        this.min = min;
        this.mode = mode;
        this.lastLedgerBaseFee = lastLedgerBaseFee;
        this.lastLedger = lastLedger;
    }

    public Long getMin() {
        return min;
    }

    public Long getMode() {
        return mode;
    }

    public Long getLastLedgerBaseFee() {
        return lastLedgerBaseFee;
    }

    public Long getLastLedger() {
        return lastLedger;
    }
}
