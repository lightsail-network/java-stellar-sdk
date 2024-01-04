package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Represents contract_debited effect response.
 *
 * @see <a
 *     href="https://github.com/stellar/go/blob/d41faf8cd619718b9801a62254a513591f6cbc0a/protocols/horizon/effects/main.go#L323-L328"
 *     target="_blank">Effect definition</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class ContractDebitedEffectResponse extends EffectResponse {
  @SerializedName("asset_type")
  String assetType;

  @SerializedName("asset_code")
  String assetCode;

  @SerializedName("asset_issuer")
  String assetIssuer;

  String contract;

  String amount;
}
