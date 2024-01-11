package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Represents RevokeSponsorship operation response.
 *
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class RevokeSponsorshipOperationResponse extends OperationResponse {
  @SerializedName("account_id")
  String accountId;

  @SerializedName("claimable_balance_id")
  String claimableBalanceId;

  @SerializedName("data_account_id")
  String dataAccountId;

  @SerializedName("data_name")
  String dataName;

  @SerializedName("offer_id")
  Long offerId;

  @SerializedName("trustline_account_id")
  String trustlineAccountId;

  @SerializedName("trustline_asset")
  String trustlineAsset;

  @SerializedName("signer_account_id")
  String signerAccountId;

  @SerializedName("signer_key")
  String signerKey;

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
