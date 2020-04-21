package org.stellar.sdk.responses.effects;

/**
 * Represents trustline_authorized_to_maintain_liabilities effect response.
 * @see <a href="https://www.stellar.org/developers/horizon/reference/resources/effect.html" target="_blank">Effect documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
public class TrustlineAuthorizedToMaintainLiabilitiesEffectResponse extends TrustlineAuthorizationResponse {
  TrustlineAuthorizedToMaintainLiabilitiesEffectResponse(String trustor, String assetType, String assetCode) {
    super(trustor, assetType, assetCode);
  }
}
