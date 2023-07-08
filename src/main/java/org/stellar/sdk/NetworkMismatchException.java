package org.stellar.sdk;

public class NetworkMismatchException extends RuntimeException {
  public NetworkMismatchException(Network serverNetwork, Network transactionNetwork) {
    super(
        "Server network "
            + serverNetwork.toString()
            + " does not match transaction network "
            + transactionNetwork.toString());
  }
}
