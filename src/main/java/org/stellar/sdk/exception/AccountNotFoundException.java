package org.stellar.sdk.exception;

import lombok.Getter;

/** Exception thrown when trying to load an account that doesn't exist on the Stellar network. */
@Getter
public class AccountNotFoundException extends NetworkException {
  // The account that was not found.
  private final String accountId;

  public AccountNotFoundException(String accountId) {
    super("Account not found, accountId: " + accountId, 404, null);
    this.accountId = accountId;
  }
}
