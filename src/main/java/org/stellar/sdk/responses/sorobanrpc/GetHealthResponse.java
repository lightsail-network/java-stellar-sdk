package org.stellar.sdk.responses.sorobanrpc;

import lombok.Value;

/**
 * Response for JSON-RPC method getHealth.
 *
 * @see <a href="https://soroban.stellar.org/api/methods/getHealth#returns"
 *     target="_blank">getHealth documentation</a>
 */
@Value
public class GetHealthResponse {
  String status;
}
