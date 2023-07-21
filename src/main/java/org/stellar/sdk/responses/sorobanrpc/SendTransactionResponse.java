package org.stellar.sdk.responses.sorobanrpc;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class SendTransactionResponse {
  @SerializedName("status")
  SendTransactionStatus status;

  @SerializedName("errorResultXdr")
  String errorResultXdr;

  @SerializedName("hash")
  String hash;

  @SerializedName("latestLedger")
  Long latestLedger;

  @SerializedName("latestLedgerCloseTime")
  Long latestLedgerCloseTime;

  public enum SendTransactionStatus {
    @SerializedName("PENDING")
    PENDING,

    @SerializedName("DUPLICATE")
    DUPLICATE,

    @SerializedName("TRY_AGAIN_LATER")
    TRY_AGAIN_LATER,

    @SerializedName("ERROR")
    ERROR
  }
}
