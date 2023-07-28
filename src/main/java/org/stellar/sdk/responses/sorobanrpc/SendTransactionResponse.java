package org.stellar.sdk.responses.sorobanrpc;

import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class SendTransactionResponse {
  SendTransactionStatus status;

  String errorResultXdr;

  String hash;

  Long latestLedger;

  Long latestLedgerCloseTime;

  public enum SendTransactionStatus {
    PENDING,
    DUPLICATE,
    TRY_AGAIN_LATER,
    ERROR
  }
}