package org.stellar.sdk.federation;

import com.google.gson.annotations.SerializedName;
import lombok.Value;

/**
 * Object to hold a response from a federation server.
 *
 * @see <a href="https://developers.stellar.org/docs/learn/glossary#federation"
 *     target="_blank">Federation docs</a>
 */
@Value
public class FederationResponse {
  /**
   * Stellar address (e.g. {@code bob*stellar.org}).
   *
   * @return the Stellar address
   */
  @SerializedName("stellar_address")
  String stellarAddress;

  /**
   * Account ID (e.g. {@code GAVHK7L...}).
   *
   * @return the account ID
   */
  @SerializedName("account_id")
  String accountId;

  /**
   * Memo type or null when no memo attached.
   *
   * @return the memo type, or null
   */
  @SerializedName("memo_type")
  String memoType;

  /**
   * Memo value or null when no memo attached.
   *
   * @return the memo value, or null
   */
  @SerializedName("memo")
  String memo;
}
