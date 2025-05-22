package org.stellar.sdk.responses.sorobanrpc;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class GetSacBalanceResponse {
  long latestLedger;
  BalanceEntry balanceEntry;

  @Value
  @Builder
  public static class BalanceEntry {
    Long liveUntilLedgerSeq;
    Long lastModifiedLedgerSeq;
    String amount;
    Boolean authorized;
    Boolean clawback;
  }
}
