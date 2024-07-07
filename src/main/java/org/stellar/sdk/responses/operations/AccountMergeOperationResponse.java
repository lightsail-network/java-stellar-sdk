package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Represents AccountMerge operation response.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/data/horizon/api-reference/resources/operations/object/account-merge"
 *     target="_blank">Operation documentation</a>
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class AccountMergeOperationResponse extends OperationResponse {
  @SerializedName("account")
  String account;

  @SerializedName("account_muxed")
  String accountMuxed;

  @SerializedName("account_muxed_id")
  BigInteger accountMuxedId;

  @SerializedName("into")
  String into;

  @SerializedName("into_muxed")
  String intoMuxed;

  @SerializedName("into_muxed_id")
  BigInteger intoMuxedId;
}
