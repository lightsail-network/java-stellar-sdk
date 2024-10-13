package org.stellar.sdk.requests.sorobanrpc;

import lombok.Value;

/**
 * Request for JSON-RPC method getTransaction.
 *
 * @see <a href="https://developers.stellar.org/docs/data/rpc/api-reference/methods/getTransaction"
 *     target="_blank">getTransaction documentation</a>
 */
@Value
public class GetTransactionRequest {
  String hash;
}
