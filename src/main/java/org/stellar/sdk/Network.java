package org.stellar.sdk;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import java.nio.charset.Charset;

/**
 * Network class is used to specify which Stellar network you want to use. Each network has a <code>
 * networkPassphrase</code> which is hashed to every transaction id.
 */
public class Network {
  public static final Network PUBLIC =
      new Network("Public Global Stellar Network ; September 2015");
  public static final Network TESTNET = new Network("Test SDF Network ; September 2015");

  private final String networkPassphrase;

  /**
   * Creates a new Network object to represent a network with a given passphrase
   *
   * @param networkPassphrase
   */
  public Network(String networkPassphrase) {
    this.networkPassphrase = checkNotNull(networkPassphrase, "networkPassphrase cannot be null");
  }

  /** Returns network passphrase */
  public String getNetworkPassphrase() {
    return networkPassphrase;
  }

  /** Returns network id (SHA-256 hashed <code>networkPassphrase</code>). */
  public byte[] getNetworkId() {
    return Util.hash(this.networkPassphrase.getBytes(Charset.forName("UTF-8")));
  }

  @Override
  public int hashCode() {
    return this.networkPassphrase.hashCode();
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Network)) {
      return false;
    }

    Network other = (Network) object;
    return Objects.equal(this.networkPassphrase, other.networkPassphrase);
  }

  @Override
  public String toString() {
    return this.networkPassphrase;
  }
}
