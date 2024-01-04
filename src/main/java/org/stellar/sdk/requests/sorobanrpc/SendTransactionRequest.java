package org.stellar.sdk.requests.sorobanrpc;

import lombok.Value;

/**
 * Request for JSON-RPC method sendTransaction.
 *
 * @see <a href="https://soroban.stellar.org/api/methods/sendTransaction#parameters"
 *     target="_blank">sendTransaction documentation</a>
 */
@Value
public class SendTransactionRequest {
  String transaction;
}
