package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import lombok.Value;
import org.stellar.sdk.Util;
import org.stellar.sdk.xdr.TransactionResult;

/**
 * Represents the response from the "Submit a Transaction Asynchronously" endpoint of Horizon API.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/data/horizon/api-reference/submit-async-transaction">
 *     Submit Transaction Asynchronously</a>
 */
@Value
public class SubmitTransactionAsyncResponse {
  String hash;

  @SerializedName("tx_status")
  TransactionStatus txStatus;

  @SerializedName("error_result_xdr")
  String errorResultXdr;

  /**
   * Parses the {@code errorResultXdr} field from a string to an {@link
   * org.stellar.sdk.xdr.TransactionResult} object.
   *
   * @return the parsed {@link org.stellar.sdk.xdr.TransactionResult} object
   */
  public TransactionResult parseErrorResultXdr() {
    return Util.parseXdr(errorResultXdr, TransactionResult::fromXdrBase64);
  }

  public enum TransactionStatus {
    ERROR,
    PENDING,
    DUPLICATE,
    TRY_AGAIN_LATER,
  }
}
