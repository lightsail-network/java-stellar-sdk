package org.stellar.sdk;

import lombok.Getter;

/** Exception thrown when trying to load an account that doesn't exist on the Stellar network. */
@Getter
public class AccountNotFoundException extends Exception {
  // The account that was not found.
  private final String accountId;

  public AccountNotFoundException(String accountId) {
    super("Account not found, accountId: " + accountId);
    this.accountId = accountId;
  }
}
