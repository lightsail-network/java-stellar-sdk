package org.stellar.sdk.requests.sorobanrpc;

import lombok.Builder;
import lombok.Value;

/**
 * Request for JSON-RPC method getTransactions.
 *
 * @see <a href="https://developers.stellar.org/docs/data/rpc/api-reference/methods/getTransactions"
 *     target="_blank">getTransactions documentation</a>
 */
@Value
@Builder(toBuilder = true)
public class GetTransactionsRequest {
  Long startLedger;

  PaginationOptions pagination;

  @Value
  @Builder(toBuilder = true)
  public static class PaginationOptions {
    Long limit;

    String cursor;
  }
}
