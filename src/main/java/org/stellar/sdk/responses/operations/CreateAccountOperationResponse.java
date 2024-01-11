package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.responses.MuxedAccount;

/**
 * Represents CreateAccount operation response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/operations/" target="_blank">Operation
 *     documentation</a>
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class CreateAccountOperationResponse extends OperationResponse {
  @SerializedName("account")
  String account;

  @SerializedName("funder")
  String funder;

  @SerializedName("funder_muxed")
  String funderAccountMuxed;

  @SerializedName("funder_muxed_id")
  BigInteger funderAccountMuxedId;

  @SerializedName("starting_balance")
  String startingBalance;

  public Optional<MuxedAccount> getFunderMuxed() {
    if (this.funderAccountMuxed == null || this.funderAccountMuxed.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(
        new MuxedAccount(this.funderAccountMuxed, this.funder, this.funderAccountMuxedId));
  }
}
