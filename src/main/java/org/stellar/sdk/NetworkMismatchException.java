package org.stellar.sdk;

/** Exception thrown when network passphrase of transaction does not match server's network. */
public class NetworkMismatchException extends RuntimeException {
  public NetworkMismatchException(Network serverNetwork, Network transactionNetwork) {
    super(
        "Server network "
            + serverNetwork.toString()
            + " does not match transaction network "
            + transactionNetwork.toString());
  }
}
