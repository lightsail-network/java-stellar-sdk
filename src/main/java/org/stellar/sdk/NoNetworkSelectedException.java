package org.stellar.sdk;

/**
 * Indicates that no network was selected.
 */
public class NoNetworkSelectedException extends RuntimeException {
  public NoNetworkSelectedException() {
    super("No network selected. Use `Network.use`, `Network.usePublicNetwork` or `Network.useTestNetwork` helper methods to select network, you can also use a constructor that includes network options.");
  }
}
