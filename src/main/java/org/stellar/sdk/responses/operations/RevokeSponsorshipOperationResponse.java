package org.stellar.sdk.responses.operations;

import com.google.common.base.Optional;
import com.google.gson.annotations.SerializedName;

/**
 * Represents RevokeSponsorship operation response.
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
  private final String offerId;

  @SerializedName("trustline_account_id")
  private final String trustlineAccountId;

  @SerializedName("trustline_asset")
  private final String trustlineAsset;

  @SerializedName("signer_account_id")
  private final String signerAccountId;

  @SerializedName("signer_key")
  private final String signerKey;


  public RevokeSponsorshipOperationResponse(String accountId, String claimableBalanceId, String dataAccountId, String dataName, String offerId, String trustlineAccountId, String trustlineAsset, String signerAccountId, String signerKey) {
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
    return Optional.of(accountId);
  }

  public Optional<String> getClaimableBalanceId() {
    return Optional.of(claimableBalanceId);
  }

  public Optional<String> getDataAccountId() {
    return Optional.of(dataAccountId);
  }

  public Optional<String> getDataName() {
    return Optional.of(dataName);
  }

  public Optional<String> getOfferId() {
    return Optional.of(offerId);
  }

  public Optional<String> getTrustlineAccountId() {
    return Optional.of(trustlineAccountId);
  }

  public Optional<String> getTrustlineAsset() {
    return Optional.of(trustlineAsset);
  }

  public Optional<String> getSignerAccountId() {
    return Optional.of(signerAccountId);
  }

  public Optional<String> getSignerKey() {
    return Optional.of(signerKey);
  }

}
