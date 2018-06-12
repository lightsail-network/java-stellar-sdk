package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Represents root endpoint response.
 * @see org.stellar.sdk.Server#root()
 */
public class RootResponse extends Response {
    @SerializedName("horizon_version")
    private final String horizonVersion;
    @SerializedName("core_version")
    private final String stellarCoreVersion;
    @SerializedName("history_latest_ledger")
    private final int historyLatestLedger;
    @SerializedName("history_elder_ledger")
    private final int historyElderLedger;
    @SerializedName("core_latest_ledger")
    private final int coreLatestLedger;
    @SerializedName("network_passphrase")
    private final String networkPassphrase;
    @SerializedName("protocol_version")
    private final int protocolVersion;

    public String getHorizonVersion() {
        return horizonVersion;
    }

    public String getStellarCoreVersion() {
        return stellarCoreVersion;
    }

    public int getHistoryLatestLedger() {
        return historyLatestLedger;
    }

    public int getHistoryElderLedger() {
        return historyElderLedger;
    }

    public int getCoreLatestLedger() {
        return coreLatestLedger;
    }

    public String getNetworkPassphrase() {
        return networkPassphrase;
    }

    public int getProtocolVersion() {
        return protocolVersion;
    }

    public RootResponse(String horizonVersion, String stellarCoreVersion, int historyLatestLedger, int historyElderLedger, int coreLatestLedger, String networkPassphrase, int protocolVersion) {
        this.horizonVersion = horizonVersion;
        this.stellarCoreVersion = stellarCoreVersion;
        this.historyLatestLedger = historyLatestLedger;
        this.historyElderLedger = historyElderLedger;
        this.coreLatestLedger = coreLatestLedger;
        this.networkPassphrase = networkPassphrase;
        this.protocolVersion = protocolVersion;
    }
}
