package org.stellar.sdk.requests.sorobanrpc;

import java.util.Collection;
import lombok.Value;

/**
 * Request for JSON-RPC method getLedgerEntries.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/data/rpc/api-reference/methods/getLedgerEntries"
 *     target="_blank">getLedgerEntries documentation</a>
 */
@Value
public class GetLedgerEntriesRequest {
  Collection<String> keys;
}
