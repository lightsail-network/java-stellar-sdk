/**
 * Provides request builder classes for the Horizon API.
 *
 * <p>Each builder class corresponds to a Horizon REST endpoint and allows setting query parameters
 * (cursor, limit, order, filters) before executing the request. Common base functionality such as
 * pagination parameters is provided by {@link org.stellar.sdk.requests.RequestBuilder}.
 *
 * <p>Builders that support Server-Sent Events (SSE) streaming expose a {@code stream()} method for
 * real-time event subscription. Streaming maintains a persistent connection and automatically
 * reconnects on failure.
 *
 * <p>Request builders are obtained from the {@link org.stellar.sdk.Server} entry point. For
 * example:
 *
 * <pre>{@code
 * Server server = new Server("https://horizon.stellar.org");
 * Page<TransactionResponse> page = server.transactions()
 *     .forAccount("G...")
 *     .limit(10)
 *     .order(RequestBuilder.Order.DESC)
 *     .execute();
 * }</pre>
 *
 * @see org.stellar.sdk.Server
 * @see <a
 *     href="https://developers.stellar.org/docs/data/apis/horizon/api-reference/structure/pagination">Horizon
 *     pagination</a>
 * @see <a
 *     href="https://developers.stellar.org/docs/data/apis/horizon/api-reference/structure/streaming">Horizon
 *     streaming</a>
 */
package org.stellar.sdk.requests;
