/**
 * Provides support for the Stellar federation protocol.
 *
 * <p>Federation allows converting a human-readable Stellar address (e.g., {@code bob*stellar.org})
 * into an account ID and optional memo. The {@link org.stellar.sdk.federation.Federation} class
 * provides methods to resolve federation addresses by looking up the target domain's {@code
 * stellar.toml} file and querying its federation server.
 *
 * @see org.stellar.sdk.federation.Federation
 * @see org.stellar.sdk.federation.FederationResponse
 */
package org.stellar.sdk.federation;
