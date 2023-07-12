package org.stellar.sdk.federation;

import com.google.gson.annotations.SerializedName;

/**
 * Object to hold a response from a federation server.
 *
 * @see <a href="https://developers.stellar.org/docs/glossary/federation/"
 *     target="_blank">Federation docs</a>
 */
public class FederationResponse {
  @SerializedName("stellar_address")
  private final String stellarAddress;

  @SerializedName("account_id")
  private final String accountId;

  @SerializedName("memo_type")
  private final String memoType;

  @SerializedName("memo")
  private final String memo;

  public FederationResponse(String stellarAddress, String accountId, String memoType, String memo) {
    this.stellarAddress = stellarAddress;
    this.accountId = accountId;
    this.memoType = memoType;
    this.memo = memo;
  }

  public String getStellarAddress() {
    return stellarAddress;
  }

  public String getAccountId() {
    return accountId;
  }

  /**
   * @return Memo type or null when no memo attached
   */
  public String getMemoType() {
    return memoType;
  }

  /**
   * @return Memo value or null when no memo attached
   */
  public String getMemo() {
    return memo;
  }
}
