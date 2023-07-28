package org.stellar.sdk.requests.sorobanrpc;

import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class SendTransactionRequest {
  String transaction;
}
