package org.stellar.sdk.requests.sorobanrpc;

import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class SimulateTransactionRequest {
  String transaction;
}
