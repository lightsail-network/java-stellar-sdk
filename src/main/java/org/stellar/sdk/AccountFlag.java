package org.stellar.sdk;

import lombok.Getter;
import org.stellar.sdk.xdr.AccountFlags;

// TODO: move to operations?
/**
 * AccountFlag is the <code>enum</code> that can be used in {@link
 * org.stellar.sdk.operations.SetOptionsOperation}.
 *
 * @see <a href="https://developers.stellar.org/docs/glossary#flags" target="_blank">Account
 *     Flags</a>
 */
@Getter
public enum AccountFlag {
  /**
   * Authorization required (0x1): Requires the issuing account to give other accounts permission
   * before they can hold the issuing account’s credit.
   */
  AUTH_REQUIRED_FLAG(AccountFlags.AUTH_REQUIRED_FLAG.getValue()),
  /**
   * Authorization revocable (0x2): Allows the issuing account to revoke its credit held by other
   * accounts.
   */
  AUTH_REVOCABLE_FLAG(AccountFlags.AUTH_REVOCABLE_FLAG.getValue()),
  /**
   * Authorization immutable (0x4): If this is set then none of the authorization flags can be set
   * and the account can never be deleted.
   */
  AUTH_IMMUTABLE_FLAG(AccountFlags.AUTH_IMMUTABLE_FLAG.getValue());

  private final int value;

  AccountFlag(int value) {
    this.value = value;
  }
}
