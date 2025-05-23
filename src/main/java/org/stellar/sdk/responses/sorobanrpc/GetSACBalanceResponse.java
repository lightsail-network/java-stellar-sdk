package org.stellar.sdk.responses.sorobanrpc;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class GetSACBalanceResponse {
  Long latestLedger;
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
