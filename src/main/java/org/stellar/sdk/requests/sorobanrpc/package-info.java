/**
 * Provides request types for Stellar RPC (formerly Soroban-RPC).
 *
 * <p>This package contains the request objects sent to a Stellar RPC instance via {@link
 * org.stellar.sdk.SorobanServer}. Each request class corresponds to an RPC method (e.g., {@link
 * org.stellar.sdk.requests.sorobanrpc.GetTransactionRequest} for {@code getTransaction}, {@link
 * org.stellar.sdk.requests.sorobanrpc.SimulateTransactionRequest} for {@code simulateTransaction}).
 *
 * @see org.stellar.sdk.SorobanServer
 * @see <a href="https://developers.stellar.org/docs/data/apis/rpc/api-reference/methods">Stellar
 *     RPC methods</a>
 */
package org.stellar.sdk.requests.sorobanrpc;
