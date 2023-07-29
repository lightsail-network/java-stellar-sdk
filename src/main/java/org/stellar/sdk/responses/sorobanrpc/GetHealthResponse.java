package org.stellar.sdk.responses.sorobanrpc;

import lombok.AllArgsConstructor;
import lombok.Value;

/** Response for JSON-RPC method getHealth. */
@AllArgsConstructor
@Value
public class GetHealthResponse {
  String status;
}
