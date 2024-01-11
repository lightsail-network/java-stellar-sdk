package org.stellar.sdk.responses.effects;

import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Represents offer_created effect response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/effects/" target="_blank">Effect
 *     documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class OfferCreatedEffectResponse extends EffectResponse {}
