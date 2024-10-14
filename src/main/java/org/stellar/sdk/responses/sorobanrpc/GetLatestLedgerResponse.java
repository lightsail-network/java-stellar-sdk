package org.stellar.sdk.responses.sorobanrpc;

import lombok.Value;

/**
 * Response for JSON-RPC method getLatestLedger.
 *
 * @see <a href="https://developers.stellar.org/docs/data/rpc/api-reference/methods/getLatestLedger"
 *     target="_blank">getLatestLedger documentation</a>
 */
@Value
public class GetLatestLedgerResponse {
  String id;

  Integer protocolVersion;

  Integer sequence;
}
