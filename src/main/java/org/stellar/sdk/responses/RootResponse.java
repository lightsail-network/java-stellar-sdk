package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Represents root endpoint response.
 *
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

  @SerializedName("current_protocol_version")
  private final int currentProtocolVersion;

  @SerializedName("core_supported_protocol_version")
  private final int coreSupportedProtocolVersion;

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

  /**
   * @deprecated Will be removed in Horizon 0.17.0
   */
  public int getProtocolVersion() {
    return protocolVersion;
  }

  public int getCurrentProtocolVersion() {
    return currentProtocolVersion;
  }

  public int getCoreSupportedProtocolVersion() {
    return coreSupportedProtocolVersion;
  }

  public RootResponse(
      String horizonVersion,
      String stellarCoreVersion,
      int historyLatestLedger,
      int historyElderLedger,
      int coreLatestLedger,
      String networkPassphrase,
      int protocolVersion,
      int currentProtocolVersion,
      int coreSupportedProtocolVersion) {
    this.horizonVersion = horizonVersion;
    this.stellarCoreVersion = stellarCoreVersion;
    this.historyLatestLedger = historyLatestLedger;
    this.historyElderLedger = historyElderLedger;
    this.coreLatestLedger = coreLatestLedger;
    this.networkPassphrase = networkPassphrase;
    this.protocolVersion = protocolVersion;
    this.currentProtocolVersion = currentProtocolVersion;
    this.coreSupportedProtocolVersion = coreSupportedProtocolVersion;
  }
}
