package org.stellar.sdk;

import lombok.Getter;

/**
 * AccountRequiresMemoException is thrown when a transaction is trying to submit an operation to an
 * account which requires a memo.
 *
 * <p>See <a href="https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0029.md"
 * target="_blank">SEP-0029</a> for more information.
 */
@Getter
public class AccountRequiresMemoException extends Exception {
  /** the account requiring the memo * */
  private final String accountId;

  /** the operation where the account is the destination * */
  private final int operationIndex;

  /**
   * @param message error message
   * @param accountId the account requiring the memo
   * @param operationIndex the operation where the account is the destination
   */
  public AccountRequiresMemoException(String message, String accountId, int operationIndex) {
    super(message);
    this.accountId = accountId;
    this.operationIndex = operationIndex;
  }
}
