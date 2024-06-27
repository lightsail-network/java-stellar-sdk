package org.stellar.sdk.exception;

/**
 * This exception is thrown to indicate that an unexpected condition has occurred which
 * theoretically should never happen during the normal execution of the program.
 *
 * <p>Use this exception to signal that the code has reached a state that was not anticipated and
 * should be addressed during development or testing.
 */
public class UnexpectedException extends SdkException {
  /** Constructs a new UnexpectedException. */
  public UnexpectedException() {}

  /**
   * Constructs a new UnexpectedException with the specified detail message.
   *
   * @param message the detail message, providing additional information about the error.
   */
  public UnexpectedException(String message) {
    super(message);
  }

  /**
   * Constructs a new UnexpectedException with the specified detail message and cause.
   *
   * @param message the detail message, providing additional information about the error.
   * @param cause the cause of the exception, which can be retrieved later by the {@link
   *     Throwable#getCause()} method.
   */
  public UnexpectedException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs a new UnexpectedException with the cause.
   *
   * @param cause the cause of the exception, which can be retrieved later by the {@link
   *     Throwable#getCause()} method.
   */
  public UnexpectedException(Throwable cause) {
    super(cause);
  }

  /**
   * Constructs a new UnexpectedException with the specified detail message, cause, suppression
   * enabled or disabled, and writable stack trace enabled or disabled.
   *
   * @param message the detail message, providing additional information about the error.
   * @param cause the cause of the exception, which can be retrieved later by the {@link
   *     Throwable#getCause()} method. A {@code null} value is permitted, and indicates that the
   *     cause is nonexistent or unknown.
   * @param enableSuppression whether suppression is enabled or disabled.
   * @param writableStackTrace whether the stack trace should be writable.
   */
  public UnexpectedException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
