package org.stellar.sdk.requests.sorobanrpc;

import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class GetLedgerEntriesRequest {
  Collection<String> keys;
}
