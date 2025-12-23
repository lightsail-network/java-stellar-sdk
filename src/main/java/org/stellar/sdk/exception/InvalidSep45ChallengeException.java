package org.stellar.sdk.exception;

/** Thrown when building, reading, or verifying a SEP-0045 challenge fails. */
public class InvalidSep45ChallengeException extends SdkException {
  public InvalidSep45ChallengeException() {
    super();
  }

  public InvalidSep45ChallengeException(String message) {
    super(message);
  }

  public InvalidSep45ChallengeException(String message, Throwable cause) {
    super(message, cause);
  }
}
