package org.stellar.sdk.responses.sorobanrpc;

import lombok.AllArgsConstructor;
import lombok.Value;

/** Response for JSON-RPC method getLatestLedger. */
@AllArgsConstructor
@Value
public class GetLatestLedgerResponse {
  String id;

  Integer protocolVersion;

  Integer sequence;
}
