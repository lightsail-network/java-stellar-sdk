package org.stellar.sdk.requests.sorobanrpc;

import lombok.Value;

/**
 * Request for JSON-RPC method getTransaction.
 *
 * @see <a href="https://soroban.stellar.org/api/methods/getTransaction#parameters"
 *     target="_blank">getTransaction documentation</a>
 */
@Value
public class GetTransactionRequest {
  String hash;
}
