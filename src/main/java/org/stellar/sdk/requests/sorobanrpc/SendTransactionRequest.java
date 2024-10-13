package org.stellar.sdk.requests.sorobanrpc;

import lombok.Value;

/**
 * Request for JSON-RPC method sendTransaction.
 *
 * @see <a href="https://developers.stellar.org/docs/data/rpc/api-reference/methods/sendTransaction"
 *     target="_blank">sendTransaction documentation</a>
 */
@Value
public class SendTransactionRequest {
  String transaction;
}
