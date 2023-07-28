package org.stellar.sdk.responses.sorobanrpc;

import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class GetNetworkResponse {
  String friendbotUrl;

  String passphrase;

  Integer protocolVersion;
}
