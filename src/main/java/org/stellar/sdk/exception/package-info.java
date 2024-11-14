/**
 * Exceptions that can be thrown by the Stellar SDK.
 *
 * <p>The base exception class is {@link org.stellar.sdk.exception.SdkException}, which extends
 * {@link java.lang.RuntimeException}. All custom exceptions defined in the SDK extend {@link
 * org.stellar.sdk.exception.SdkException}. The SDK may also throw standard Java runtime exceptions
 * such as {@link java.lang.IllegalArgumentException} where appropriate.
 *
 * <p>The exception hierarchy is as follows:
 *
 * <ul>
 *   <li>{@link org.stellar.sdk.exception.SdkException}
 *       <ul>
 *         <li>{@link org.stellar.sdk.exception.AccountRequiresMemoException}
 *         <li>{@link org.stellar.sdk.exception.InvalidSep10ChallengeException}
 *         <li>{@link org.stellar.sdk.exception.PrepareTransactionException}
 *         <li>{@link org.stellar.sdk.exception.UnexpectedException}
 *         <li>{@link org.stellar.sdk.exception.NetworkException}
 *             <ul>
 *               <li>{@link org.stellar.sdk.exception.AccountNotFoundException}
 *               <li>{@link org.stellar.sdk.exception.BadRequestException}
 *               <li>{@link org.stellar.sdk.exception.BadResponseException}
 *               <li>{@link org.stellar.sdk.exception.ConnectionErrorException}
 *               <li>{@link org.stellar.sdk.exception.RequestTimeoutException}
 *               <li>{@link org.stellar.sdk.exception.SorobanRpcException}
 *               <li>{@link org.stellar.sdk.exception.TooManyRequestsException}
 *               <li>{@link org.stellar.sdk.exception.UnknownResponseException}
 *             </ul>
 *       </ul>
 * </ul>
 *
 * <p>The Stellar SDK uses unchecked exceptions (extending {@code RuntimeException}) for the
 * following reasons:
 *
 * <ul>
 *   <li>Stellar SDK methods may throw exceptions as a result of errors that occur in the Stellar
 *       network, which are out of the control of the SDK itself. Forcing developers to catch such
 *       exceptions would lead to cluttered code.
 *   <li>Most methods in the SDK throw exceptions to indicate errors that cannot be handled by the
 *       method itself. Propagating these exceptions as unchecked exceptions allows developers to
 *       handle them at an appropriate point in their code.
 *   <li>Unchecked exceptions are more suitable for an API like the Stellar SDK, where most
 *       exceptions indicate programming errors or errors in interaction with the Stellar network,
 *       rather than recoverable conditions.
 * </ul>
 *
 * <p>While the SDK uses unchecked exceptions, developers are still encouraged to catch and handle
 * these exceptions appropriately in their code. The specific exception classes thrown by the SDK
 * provide useful information about the nature of the error that occurred.
 *
 * @see org.stellar.sdk.exception.SdkException
 * @see java.lang.RuntimeException
 */
package org.stellar.sdk.exception;
