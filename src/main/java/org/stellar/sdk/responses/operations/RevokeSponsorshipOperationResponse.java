package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Represents RevokeSponsorship operation response.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/data/horizon/api-reference/resources/operations/object/revoke-sponsorship"
 *     target="_blank">Operation documentation</a>
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
}
