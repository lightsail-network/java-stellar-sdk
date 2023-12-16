package org.stellar.sdk.requests.sorobanrpc;

import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * Request for JSON-RPC method simulateTransaction.
 *
 * @see <a href="https://soroban.stellar.org/api/methods/simulateTransaction#parameters"
 *     target="_blank">simulateTransaction documentation</a>
 */
@AllArgsConstructor
@Value
public class SimulateTransactionRequest {
  String transaction;

  ResourceConfig resourceConfig;

  @Value
  @AllArgsConstructor
  public static class ResourceConfig {
    BigInteger instructionLeeway;
  }
}
