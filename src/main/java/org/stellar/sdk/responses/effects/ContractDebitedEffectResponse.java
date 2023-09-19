package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents contract_debited effect response.
 *
 * @see <a
 *     href="https://github.com/stellar/go/blob/d41faf8cd619718b9801a62254a513591f6cbc0a/protocols/horizon/effects/main.go#L323-L328"
 *     target="_blank">Effect definition</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
@AllArgsConstructor
@Getter
public class ContractDebitedEffectResponse extends EffectResponse {
  @SerializedName("asset_type")
  private final String assetType;

  @SerializedName("asset_code")
  private final String assetCode;

  @SerializedName("asset_issuer")
  private final String assetIssuer;

  private final String contract;

  private final String amount;
}
