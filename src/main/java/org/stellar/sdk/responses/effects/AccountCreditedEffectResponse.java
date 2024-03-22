package org.stellar.sdk.responses.effects;

import static org.stellar.sdk.Asset.create;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.Asset;

/**
 * Represents account_credited effect response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/effects/" target="_blank">Effect
 *     documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class AccountCreditedEffectResponse extends EffectResponse {
  @SerializedName("amount")
  String amount;

  @SerializedName("asset_type")
  String assetType;

  @SerializedName("asset_code")
  String assetCode;

  @SerializedName("asset_issuer")
  String assetIssuer;

  public Asset getAsset() {
    return create(assetType, assetCode, assetIssuer);
  }
}
