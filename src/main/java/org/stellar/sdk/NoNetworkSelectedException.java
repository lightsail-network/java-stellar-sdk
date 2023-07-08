package org.stellar.sdk;

/** Indicates that no network was selected. */
public class NoNetworkSelectedException extends RuntimeException {
  public NoNetworkSelectedException() {
    super("No network selected.");
  }
}
