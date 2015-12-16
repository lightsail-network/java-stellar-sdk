package org.stellar.sdk.operations;

import com.google.gson.annotations.SerializedName;

import org.stellar.base.Keypair;

/**
 * Represents AccountMerge operation response.
 * @see <a href="https://www.stellar.org/developers/horizon/reference/resources/operation.html" target="_blank">Operation documentation</a>
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
public class AccountMergeOperation extends Operation {
  @SerializedName("account")
  protected final Keypair account;
  @SerializedName("into")
  protected final Keypair into;

  AccountMergeOperation(Keypair account, Keypair into) {
    this.account = account;
    this.into = into;
  }

  public Keypair getAccount() {
    return account;
  }

  public Keypair getInto() {
    return into;
  }
}
