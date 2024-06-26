package org.stellar.sdk.exception;

/** If the SEP-0010 validation fails, the exception will be thrown. */
public class InvalidSep10ChallengeException extends SdkException {
  public InvalidSep10ChallengeException() {
    super();
  }

  public InvalidSep10ChallengeException(String message) {
    super(message);
  }

  public InvalidSep10ChallengeException(String message, Throwable cause) {
    super(message, cause);
  }
}
