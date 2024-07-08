package org.stellar.sdk.responses.effects;

import static org.stellar.sdk.Asset.create;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.Asset;

/**
 * Represents contract_credited effect response.
 *
 * @see <a
 *     href="https://github.com/stellar/go/blob/d41faf8cd619718b9801a62254a513591f6cbc0a/protocols/horizon/effects/main.go#L316-L321"
 *     target="_blank">Effect definition</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class ContractCreditedEffectResponse extends EffectResponse {
  @SerializedName("asset_type")
  String assetType;

  @SerializedName("asset_code")
  String assetCode;

  @SerializedName("asset_issuer")
  String assetIssuer;

  String contract;

  String amount;

  public Asset getAsset() {
    return create(assetType, assetCode, assetIssuer);
  }
}
