/**
 * Provides response model classes for the Horizon API.
 *
 * <p>Each class in this package represents a JSON response returned by Horizon endpoints. Response
 * objects are deserialized from JSON using Gson. Paginated responses are wrapped in {@link
 * org.stellar.sdk.responses.Page}, which provides navigation to the next page of results.
 *
 * <p>Response types that support cursor-based pagination implement the {@link
 * org.stellar.sdk.responses.Pageable} interface.
 *
 * @see org.stellar.sdk.Server
 * @see org.stellar.sdk.responses.Page
 * @see org.stellar.sdk.responses.Pageable
 */
package org.stellar.sdk.responses;
