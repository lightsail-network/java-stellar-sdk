package org.stellar.sdk.requests.sorobanrpc;

import java.util.Collection;
import lombok.Value;

/**
 * Request for JSON-RPC method getLedgerEntries.
 *
 * @see <a href="https://soroban.stellar.org/api/methods/getLedgerEntries#parameters"
 *     target="_blank">getLedgerEntries documentation</a>
 */
@Value
public class GetLedgerEntriesRequest {
  Collection<String> keys;
}
