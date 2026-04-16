/**
 * Provides response model classes for Horizon effect resources.
 *
 * <p>Effects represent specific changes to the ledger that result from operations (e.g., account
 * creation, payment receipt, trustline changes). Each effect class in this package extends {@link
 * org.stellar.sdk.responses.effects.EffectResponse} and contains the data specific to that effect
 * type.
 *
 * <p>Effects are retrieved via {@link org.stellar.sdk.requests.EffectsRequestBuilder} and can be
 * streamed in real time using Server-Sent Events.
 *
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 */
package org.stellar.sdk.responses.effects;
