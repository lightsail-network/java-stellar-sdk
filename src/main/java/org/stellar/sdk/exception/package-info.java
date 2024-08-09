/**
 * Exceptions that can be thrown by the Stellar SDK.
 *
 * <p>The base exception class is {@link org.stellar.sdk.exception.SdkException}, which extends
 * {@link java.lang.RuntimeException}. Most exceptions thrown by the SDK extend {@link
 * org.stellar.sdk.exception.SdkException}, while some extend {@link
 * java.lang.IllegalArgumentException} directly.
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
 * @see java.lang.IllegalArgumentException
 */
package org.stellar.sdk.exception;
