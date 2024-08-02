package org.stellar.sdk;

import java.nio.charset.StandardCharsets;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

/**
 * Network class is used to specify which Stellar network you want to use. Each network has a <code>
 * networkPassphrase</code> which is hashed to every transaction id.
 */
@Getter
@EqualsAndHashCode
public class Network {
  public static final Network PUBLIC =
      new Network("Public Global Stellar Network ; September 2015");
  public static final Network TESTNET = new Network("Test SDF Network ; September 2015");
  public static final Network FUTURENET = new Network("Test SDF Future Network ; October 2022");
  public static final Network STANDALONE = new Network("Standalone Network ; February 2017");
  public static final Network SANDBOX =
      new Network("Local Sandbox Stellar Network ; September 2022");

  /** The network passphrase */
  @NonNull private final String networkPassphrase;

  /**
   * Creates a new Network object to represent a network with a given passphrase
   *
   * @param networkPassphrase The network passphrase
   */
  public Network(@NonNull String networkPassphrase) {
    this.networkPassphrase = networkPassphrase;
  }

  /**
   * Returns network id (SHA-256 hashed <code>networkPassphrase</code>).
   *
   * @return network id
   */
  public byte[] getNetworkId() {
    return Util.hash(this.networkPassphrase.getBytes(StandardCharsets.UTF_8));
  }

  @Override
  public String toString() {
    return this.networkPassphrase;
  }
}
