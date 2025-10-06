package org.stellar.sdk.responses.effects;

import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Represents trustline_removed effect response.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/data/apis/horizon/api-reference/resources/effects"
 *     target="_blank">Effect documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class TrustlineRemovedEffectResponse extends TrustlineCUDResponse {
  public TrustlineRemovedEffectResponse(
      String limit,
      String assetType,
      String assetCode,
      String assetIssuer,
      String liquidityPoolId) {
    super(limit, assetType, assetCode, assetIssuer, liquidityPoolId);
  }
}
