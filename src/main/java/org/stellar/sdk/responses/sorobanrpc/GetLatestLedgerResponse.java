package org.stellar.sdk.responses.sorobanrpc;

import lombok.Value;

/**
 * Response for JSON-RPC method getLatestLedger.
 *
 * @see <a href="https://soroban.stellar.org/api/methods/getLatestLedger#returns"
 *     target="_blank">getLatestLedger documentation</a>
 */
@Value
public class GetLatestLedgerResponse {
  String id;

  Long protocolVersion;

  Integer sequence;
}
