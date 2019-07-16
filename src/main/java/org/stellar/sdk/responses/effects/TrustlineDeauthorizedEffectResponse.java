package org.stellar.sdk.responses.effects;

/**
 * Represents trustline_deauthorized effect response.
 * @see <a href="https://www.stellar.org/developers/horizon/reference/resources/effect.html" target="_blank">Effect documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
public class TrustlineDeauthorizedEffectResponse extends TrustlineAuthorizationResponse {
  TrustlineDeauthorizedEffectResponse(String trustor, String assetType, String assetCode) {
    super(trustor, assetType, assetCode);
  }
}
