package org.stellar.sdk.exception;

/** Indicates that the object that has to be signed has not enough signatures. */
public class NotEnoughSignaturesException extends SdkException {
  public NotEnoughSignaturesException() {
    super();
  }

  public NotEnoughSignaturesException(String message) {
    super(message);
  }
}
