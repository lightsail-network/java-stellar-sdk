/**
 * Exceptions specific to smart contract transaction assembly and execution.
 *
 * <p>These exceptions are thrown by {@link org.stellar.sdk.contract.AssembledTransaction} during
 * the lifecycle of a contract invocation, including simulation failures, signing issues, submission
 * errors, and expired contract state.
 *
 * <p>All exceptions in this package extend {@link
 * org.stellar.sdk.contract.exception.AssembledTransactionException}.
 *
 * @see org.stellar.sdk.contract.AssembledTransaction
 */
package org.stellar.sdk.contract.exception;
