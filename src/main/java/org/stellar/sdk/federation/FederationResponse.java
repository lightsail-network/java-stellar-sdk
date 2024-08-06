package org.stellar.sdk.federation;

import com.google.gson.annotations.SerializedName;
import lombok.Value;

/**
 * Object to hold a response from a federation server.
 *
 * @see <a href="https://developers.stellar.org/docs/glossary/federation/"
 *     target="_blank">Federation docs</a>
 */
@Value
public class FederationResponse {
  /** Stellar address (e.g. `bob*stellar.org`). */
  @SerializedName("stellar_address")
  String stellarAddress;

  /** Account ID (e.g. `GAVHK7L...`). */
  @SerializedName("account_id")
  String accountId;

  /** Memo type or null when no memo attached */
  @SerializedName("memo_type")
  String memoType;

  /** Memo value or null when no memo attached */
  @SerializedName("memo")
  String memo;
}
