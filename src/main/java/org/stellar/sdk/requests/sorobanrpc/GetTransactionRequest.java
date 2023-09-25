package org.stellar.sdk.requests.sorobanrpc;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * Request for JSON-RPC method getTransaction.
 *
 * @see <a href="https://soroban.stellar.org/api/methods/getTransaction#parameters"
 *     target="_blank">getTransaction documentation</a>
 */
@Value
@AllArgsConstructor
public class GetTransactionRequest {
  String hash;
}
