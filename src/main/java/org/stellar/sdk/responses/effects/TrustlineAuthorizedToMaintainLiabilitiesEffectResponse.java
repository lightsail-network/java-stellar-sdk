package org.stellar.sdk.responses.effects;

import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * @deprecated As of release 0.24.0, replaced by {@link TrustlineFlagsUpdatedEffectResponse}
 *     <p>Represents trustline_authorized_to_maintain_liabilities effect response.
 * @see <a
 *     href="https://developers.stellar.org/docs/data/apis/horizon/api-reference/resources/effects"
 *     target="_blank">Effect documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
@Value
@EqualsAndHashCode(callSuper = true)
@Deprecated
public class TrustlineAuthorizedToMaintainLiabilitiesEffectResponse
    extends TrustlineAuthorizationResponse {
  TrustlineAuthorizedToMaintainLiabilitiesEffectResponse(
      String trustor, String assetType, String assetCode) {
    super(trustor, assetType, assetCode);
  }
}
