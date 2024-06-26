package org.stellar.sdk.exception;

/** Indicates that no network was selected. */
public class NoNetworkSelectedException extends SdkException {
  public NoNetworkSelectedException() {
    super("No network selected.");
  }
}
