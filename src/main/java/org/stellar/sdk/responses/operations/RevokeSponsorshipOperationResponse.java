package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;
import java.util.Optional;

/**
 * Represents RevokeSponsorship operation response.
 *
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
public class RevokeSponsorshipOperationResponse extends OperationResponse {
  @SerializedName("account_id")
  private final String accountId;

  @SerializedName("claimable_balance_id")
  private final String claimableBalanceId;

  @SerializedName("data_account_id")
  private final String dataAccountId;

  @SerializedName("data_name")
  private final String dataName;

  @SerializedName("offer_id")
  private final Long offerId;

  @SerializedName("trustline_account_id")
  private final String trustlineAccountId;

  @SerializedName("trustline_asset")
  private final String trustlineAsset;

  @SerializedName("signer_account_id")
  private final String signerAccountId;

  @SerializedName("signer_key")
  private final String signerKey;

  public RevokeSponsorshipOperationResponse(
      String accountId,
      String claimableBalanceId,
      String dataAccountId,
      String dataName,
      Long offerId,
      String trustlineAccountId,
      String trustlineAsset,
      String signerAccountId,
      String signerKey) {
    this.accountId = accountId;
    this.claimableBalanceId = claimableBalanceId;
    this.dataAccountId = dataAccountId;
    this.dataName = dataName;
    this.offerId = offerId;
    this.trustlineAccountId = trustlineAccountId;
    this.trustlineAsset = trustlineAsset;
    this.signerAccountId = signerAccountId;
    this.signerKey = signerKey;
  }

  public Optional<String> getAccountId() {
    return Optional.ofNullable(accountId);
  }

  public Optional<String> getClaimableBalanceId() {
    return Optional.ofNullable(claimableBalanceId);
  }

  public Optional<String> getDataAccountId() {
    return Optional.ofNullable(dataAccountId);
  }

  public Optional<String> getDataName() {
    return Optional.ofNullable(dataName);
  }

  public Optional<Long> getOfferId() {
    return Optional.ofNullable(offerId);
  }

  public Optional<String> getTrustlineAccountId() {
    return Optional.ofNullable(trustlineAccountId);
  }

  public Optional<String> getTrustlineAsset() {
    return Optional.ofNullable(trustlineAsset);
  }

  public Optional<String> getSignerAccountId() {
    return Optional.ofNullable(signerAccountId);
  }

  public Optional<String> getSignerKey() {
    return Optional.ofNullable(signerKey);
  }
}
