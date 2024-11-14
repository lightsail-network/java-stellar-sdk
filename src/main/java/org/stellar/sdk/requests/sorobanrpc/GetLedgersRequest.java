package org.stellar.sdk.requests.sorobanrpc;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/**
 * Request for JSON-RPC method getLedgers.
 *
 * @see <a href="https://developers.stellar.org/docs/data/rpc/api-reference/methods/getLedgers"
 *     target="_blank">getLedgers documentation</a>
 */
@Value
@Builder(toBuilder = true)
public class GetLedgersRequest {
  @NonNull Long startLedger;

  PaginationOptions pagination;

  @Value
  @Builder(toBuilder = true)
  public static class PaginationOptions {
    Long limit;

    String cursor;
  }
}
