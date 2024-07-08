package org.stellar.sdk.responses.effects;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.LiquidityPoolID;

/**
 * Represents trustline_updated effect response.
 *
 * @see <a href="https://developers.stellar.org/docs/data/horizon/api-reference/resources/effects"
 *     target="_blank">Effect documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class TrustlineUpdatedEffectResponse extends TrustlineCUDResponse {
  public TrustlineUpdatedEffectResponse(
      String limit,
      String assetType,
      String assetCode,
      String assetIssuer,
      LiquidityPoolID liquidityPoolId) {
    super(limit, assetType, assetCode, assetIssuer, liquidityPoolId);
  }
}
