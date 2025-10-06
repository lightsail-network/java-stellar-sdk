package org.stellar.sdk.responses.sorobanrpc;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * Response for JSON-RPC method getNetwork.
 *
 * @see <a href="https://developers.stellar.org/docs/data/apis/rpc/api-reference/methods/getNetwork"
 *     target="_blank">getNetwork documentation</a>
 */
@AllArgsConstructor
@Value
public class GetNetworkResponse {
  String friendbotUrl;

  String passphrase;

  Integer protocolVersion;
}
