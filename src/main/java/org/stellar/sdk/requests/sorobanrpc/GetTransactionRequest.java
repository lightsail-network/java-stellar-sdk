package org.stellar.sdk.requests.sorobanrpc;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class GetTransactionRequest {
  String hash;
}
