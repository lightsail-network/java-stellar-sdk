package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;

/** Represents an error response. */
@Value
@EqualsAndHashCode(callSuper = false)
public class Problem extends Response {
  @SerializedName("type")
  String type;

  @SerializedName("title")
  String title;

  @SerializedName("status")
  Integer status;

  @SerializedName("detail")
  String detail;

  @SerializedName("extras")
  Extras extras;

  @Value
  public static class Extras {
    @SerializedName("hash")
    String hash;

    @SerializedName("envelope_xdr")
    String envelopeXdr;

    @SerializedName("result_xdr")
    String resultXdr;

    @SerializedName("result_codes")
    ResultCodes resultCodes;

    /**
     * Contains result codes for this transaction.
     *
     * @see <a
     *     href="https://github.com/stellar/horizon/blob/master/src/github.com/stellar/horizon/codes/main.go"
     *     target="_blank">Possible values</a>
     */
    @Value
    public static class ResultCodes {
      @SerializedName("transaction")
      String transactionResultCode;

      @SerializedName("inner_transaction")
      String innerTransactionResultCode;

      @SerializedName("operations")
      List<String> operationsResultCodes;
    }
  }
}
