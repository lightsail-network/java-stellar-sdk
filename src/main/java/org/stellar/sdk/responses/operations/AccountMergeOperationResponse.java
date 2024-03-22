package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.responses.MuxedAccount;

/**
 * Represents AccountMerge operation response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/operations/" target="_blank">Operation
 *     documentation</a>
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

  public Optional<MuxedAccount> getAccountMuxed() {
    if (this.accountMuxed == null || this.accountMuxed.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(new MuxedAccount(this.accountMuxed, this.account, this.accountMuxedId));
  }

  public Optional<MuxedAccount> getIntoMuxed() {
    if (this.intoMuxed == null || this.intoMuxed.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(new MuxedAccount(this.intoMuxed, this.into, this.intoMuxedId));
  }
}
