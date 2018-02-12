package org.stellar.sdk;

import java.nio.charset.Charset;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Network class is used to specify which Stellar network you want to use.
 * Each network has a <code>networkPassphrase</code> which is hashed to
 * every transaction id.
 * There is no default network. You need to specify network when initializing your app by calling
 * {@link Network#use(Network)}, {@link Network#usePublicNetwork()} or {@link Network#useTestNetwork()}.
 */
public class Network {
    private final static String PUBLIC = "Public Global Stellar Network ; September 2015";
    private final static String TESTNET = "Test SDF Network ; September 2015";
    private static Network current;

    private final String networkPassphrase;

    /**
     * Creates a new Network object to represent a network with a given passphrase
     * @param networkPassphrase
     */
    public Network(String networkPassphrase) {
        this.networkPassphrase = checkNotNull(networkPassphrase, "networkPassphrase cannot be null");
    }

    /**
     * Returns network passphrase
     */
    public String getNetworkPassphrase() {
        return networkPassphrase;
    }

    /**
     * Returns network id (SHA-256 hashed <code>networkPassphrase</code>).
     */
    public byte[] getNetworkId() {
        return Util.hash(current.getNetworkPassphrase().getBytes(Charset.forName("UTF-8")));
    }

    /**
     * Returns currently used Network object.
     */
    public static Network current() {
        return current;
    }

    /**
     * Use <code>network</code> as a current network.
     * @param network Network object to set as current network
     */
    public static void use(Network network) {
        current = network;
    }

    /**
     * Use Stellar Public Network
     */
    public static void usePublicNetwork() {
        Network.use(new Network(PUBLIC));
    }

    /**
     * Use Stellar Test Network.
     */
    public static void useTestNetwork() {
        Network.use(new Network(TESTNET));
    }
}
