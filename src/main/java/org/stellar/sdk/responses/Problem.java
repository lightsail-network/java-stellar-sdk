package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.Util;
import org.stellar.sdk.xdr.TransactionEnvelope;
import org.stellar.sdk.xdr.TransactionResult;

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
     * Parses the {@code envelopeXdr} field from a string to an {@link
     * org.stellar.sdk.xdr.TransactionEnvelope} object.
     *
     * @return the parsed {@link org.stellar.sdk.xdr.TransactionEnvelope} object
     */
    public TransactionEnvelope parseEnvelopeXdr() {
      return Util.parseXdr(envelopeXdr, TransactionEnvelope::fromXdrBase64);
    }

    /**
     * Parses the {@code resultXdr} field from a string to an {@link
     * org.stellar.sdk.xdr.TransactionResult} object.
     *
     * @return the parsed {@link org.stellar.sdk.xdr.TransactionResult} object
     */
    public TransactionResult parseResultXdr() {
      return Util.parseXdr(resultXdr, TransactionResult::fromXdrBase64);
    }

    /**
     * Contains result codes for this transaction.
     *
     * @see <a
     *     href="https://github.com/stellar/go/blob/master/services/horizon/internal/codes/main.go"
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
