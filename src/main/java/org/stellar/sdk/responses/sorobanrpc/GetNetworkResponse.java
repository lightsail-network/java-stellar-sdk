package org.stellar.sdk.responses.sorobanrpc;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * Response for JSON-RPC method getNetwork.
 *
 * @see <a href="https://soroban.stellar.org/api/methods/getNetwork#returns"
 *     target="_blank">getNetwork documentation</a>
 */
@AllArgsConstructor
@Value
public class GetNetworkResponse {
  String friendbotUrl;

  String passphrase;

  Integer protocolVersion;
}
