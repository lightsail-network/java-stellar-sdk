/**
 * Exceptions specific to smart contract APIs.
 *
 * <p>This package contains two families of exceptions:
 *
 * <ul>
 *   <li>Exceptions thrown by {@link org.stellar.sdk.contract.AssembledTransaction} during the
 *       lifecycle of a contract invocation (simulation failures, signing issues, submission errors,
 *       expired state). These all extend {@link
 *       org.stellar.sdk.contract.exception.AssembledTransactionException}.
 *   <li>Exceptions thrown by contract introspection APIs ({@link
 *       org.stellar.sdk.contract.ContractMeta}, {@link org.stellar.sdk.contract.ContractSpec},
 *       {@link org.stellar.sdk.contract.ContractInfo}, and related {@link
 *       org.stellar.sdk.SorobanServer} methods). These all extend {@link
 *       org.stellar.sdk.contract.exception.ContractIntrospectionException}.
 * </ul>
 *
 * @see org.stellar.sdk.contract.AssembledTransaction
 * @see org.stellar.sdk.contract.ContractInfo
 */
package org.stellar.sdk.contract.exception;
