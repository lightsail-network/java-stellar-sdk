/**
 * Provides classes representing the Stellar network operations.
 *
 * <p>Each class in this package corresponds to a specific Stellar operation type (e.g., {@link
 * org.stellar.sdk.operations.CreateAccountOperation}, {@link
 * org.stellar.sdk.operations.PaymentOperation}, {@link
 * org.stellar.sdk.operations.ChangeTrustOperation}). Operations are the individual commands that
 * modify the Stellar ledger and are included in transactions built via {@link
 * org.stellar.sdk.TransactionBuilder}.
 *
 * <p>All operation classes extend {@link org.stellar.sdk.operations.Operation}, which provides
 * common functionality such as source account management and XDR serialization.
 *
 * @see org.stellar.sdk.operations.Operation
 * @see org.stellar.sdk.TransactionBuilder
 * @see <a
 *     href="https://developers.stellar.org/docs/learn/fundamentals/transactions/list-of-operations">List
 *     of operations</a>
 */
package org.stellar.sdk.operations;
