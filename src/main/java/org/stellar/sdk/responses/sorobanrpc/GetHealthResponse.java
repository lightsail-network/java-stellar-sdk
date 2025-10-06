package org.stellar.sdk.responses.sorobanrpc;

import lombok.Value;

/**
 * Response for JSON-RPC method getHealth.
 *
 * @see <a href="https://developers.stellar.org/docs/data/apis/rpc/api-reference/methods/getHealth"
 *     target="_blank">getHealth documentation</a>
 */
@Value
public class GetHealthResponse {
  String status;
  Long latestLedger;
  Long oldestLedger;
  Long ledgerRetentionWindow;
}
