package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents contract_credited effect response.
 *
 * @see <a
 *     href="https://github.com/stellar/go/blob/d41faf8cd619718b9801a62254a513591f6cbc0a/protocols/horizon/effects/main.go#L316-L321"
 *     target="_blank">Effect definition</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
@AllArgsConstructor
@Getter
public class ContractCreditedEffectResponse extends EffectResponse {
  @SerializedName("asset_type")
  private final String assetType;

  @SerializedName("asset_code")
  private final String assetCode;

  @SerializedName("asset_issuer")
  private final String assetIssuer;

  @SerializedName("contract")
  private final String contract;

  @SerializedName("amount")
  private final String amount;
}
