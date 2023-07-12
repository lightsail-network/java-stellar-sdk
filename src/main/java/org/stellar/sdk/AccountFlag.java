package org.stellar.sdk;

import org.stellar.sdk.xdr.AccountFlags;

/**
 * AccountFlag is the <code>enum</code> that can be used in {@link SetOptionsOperation}.
 *
 * @see <a href="https://developers.stellar.org/docs/glossary/accounts/#flags"
 *     target="_blank">Account Flags</a>
 */
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
  AUTH_IMMUTABLE_FLAG(AccountFlags.AUTH_IMMUTABLE_FLAG.getValue()),
  ;

  private final int value;

  AccountFlag(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
