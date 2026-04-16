/**
 * Provides response model classes for Horizon operation resources.
 *
 * <p>Each class in this package represents the response data for a specific Stellar operation type
 * as returned by Horizon. All operation response classes extend {@link
 * org.stellar.sdk.responses.operations.OperationResponse}, which provides common fields such as the
 * operation ID, source account, type, and creation time.
 *
 * <p>Operation responses are retrieved via {@link
 * org.stellar.sdk.requests.OperationsRequestBuilder} and can be streamed in real time using
 * Server-Sent Events.
 *
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 */
package org.stellar.sdk.responses.operations;
