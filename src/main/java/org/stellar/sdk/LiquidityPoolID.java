package org.stellar.sdk;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.stellar.sdk.xdr.*;

/**
 * Base LiquidityPoolID class.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/encyclopedia/liquidity-on-stellar-sdex-liquidity-pools#liquidity-pools"
 *     target="_blank">Liquidity Pool</a>
 */
@EqualsAndHashCode
@AllArgsConstructor
public final class LiquidityPoolID {
  private final byte[] hash;

  /**
   * Creates LiquidityPoolID object from a given parameters.
   *
   * @param type The type of the pool
   * @param a First asset in the pool
   * @param b Second asset in the pool
   * @param fee Fee amount in base-points
   */
  public LiquidityPoolID(LiquidityPoolType type, Asset a, Asset b, int fee) {
    if (a.compareTo(b) >= 0) {
      throw new RuntimeException("AssetA must be < AssetB");
    }

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    try {
      LiquidityPoolParameters.create(type, a, b, fee).toXdr().encode(xdrDataOutputStream);
    } catch (IOException e) {
      throw new IllegalArgumentException("invalid liquidity pool id.", e);
    }
    hash = Util.hash(byteArrayOutputStream.toByteArray());
  }

  /**
   * Creates LiquidityPoolID object from a given hex-encoded pool ID.
   *
   * @param hex hex string
   */
  public LiquidityPoolID(String hex) {
    hash = Util.hexToBytes(hex.toUpperCase());
  }

  /**
   * Generates LiquidityPoolID object from a given XDR object
   *
   * @param xdr XDR object
   */
  public static LiquidityPoolID fromXdr(org.stellar.sdk.xdr.PoolID xdr) {
    return new LiquidityPoolID(xdr.getPoolID().getHash());
  }

  @Override
  public String toString() {
    return Util.bytesToHex(hash).toLowerCase();
  }

  /** Generates XDR object from a given LiquidityPoolID object */
  public org.stellar.sdk.xdr.PoolID toXdr() {
    org.stellar.sdk.xdr.PoolID xdr = new org.stellar.sdk.xdr.PoolID();
    xdr.setPoolID(new org.stellar.sdk.xdr.Hash(hash));
    return xdr;
  }
}
