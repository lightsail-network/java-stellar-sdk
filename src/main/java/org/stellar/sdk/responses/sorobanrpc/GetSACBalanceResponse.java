package org.stellar.sdk.responses.sorobanrpc;

import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;
import org.stellar.sdk.Asset;
import org.stellar.sdk.Network;

/** Response for {@link org.stellar.sdk.SorobanServer#getSACBalance(String, Asset, Network)}. */
@Builder
@Value
public class GetSACBalanceResponse {
  Long latestLedger;

  /**
   * The balance entry for the account. If there is not a valid balance entry, this will be null.
   */
  @Nullable BalanceEntry balanceEntry;

  @Builder
  @Value
  public static class BalanceEntry {
    Long liveUntilLedgerSeq;
    Long lastModifiedLedgerSeq;
    String amount;
    Boolean authorized;
    Boolean clawback;
  }
}
