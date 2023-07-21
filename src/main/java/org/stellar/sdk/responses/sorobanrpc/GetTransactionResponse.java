package org.stellar.sdk.responses.sorobanrpc;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class GetTransactionResponse {
  @SerializedName("status")
  GetTransactionStatus status;

  @SerializedName("latestLedger")
  Long latestLedger;

  @SerializedName("latestLedgerCloseTime")
  Long latestLedgerCloseTime;

  @SerializedName("oldestLedger")
  Long oldestLedger;

  @SerializedName("oldestLedgerCloseTime")
  Long oldestLedgerCloseTime;

  @SerializedName("applicationOrder")
  Integer applicationOrder;

  @SerializedName("feeBump")
  Boolean feeBump;

  @SerializedName("envelopeXdr")
  String envelopeXdr;

  @SerializedName("resultXdr")
  String resultXdr;

  @SerializedName("resultMetaXdr")
  String resultMetaXdr;

  @SerializedName("ledger")
  Long ledger;

  @SerializedName("createdAt")
  Long createdAt;

  public enum GetTransactionStatus {
    @SerializedName("NOT_FOUND")
    NOT_FOUND,

    @SerializedName("SUCCESS")
    SUCCESS,

    @SerializedName("FAILED")
    FAILED
  }
}
