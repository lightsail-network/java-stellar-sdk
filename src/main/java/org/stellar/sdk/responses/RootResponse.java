package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Represents root endpoint response.
 *
 * @see org.stellar.sdk.Server#root()
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class RootResponse extends Response {
  @SerializedName("horizon_version")
  String horizonVersion;

  @SerializedName("core_version")
  String stellarCoreVersion;

  @SerializedName("history_latest_ledger")
  int historyLatestLedger;

  @SerializedName("history_elder_ledger")
  int historyElderLedger;

  @SerializedName("core_latest_ledger")
  int coreLatestLedger;

  @SerializedName("network_passphrase")
  String networkPassphrase;

  @SerializedName("current_protocol_version")
  int currentProtocolVersion;

  @SerializedName("core_supported_protocol_version")
  int coreSupportedProtocolVersion;
}
