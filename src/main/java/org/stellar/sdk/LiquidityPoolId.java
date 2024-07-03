package org.stellar.sdk;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

/**
 * Represents a Liquidity Pool ID on the Stellar network.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/learn/encyclopedia/sdex/liquidity-on-stellar-sdex-liquidity-pools#liquidity-pools"
 *     target="_blank">Liquidity Pool</a>
 */
@EqualsAndHashCode
@AllArgsConstructor
public final class LiquidityPoolId {
  /** The pool id. */
  private final byte[] poolId;

  /**
   * Creates a LiquidityPoolID object from a given hex-encoded pool ID.
   *
   * @param hex A hex-encoded string representing the pool ID
   */
  public LiquidityPoolId(String hex) {
    poolId = Util.hexToBytes(hex.toUpperCase());
  }

  /**
   * Returns the pool ID as a lowercase hex-encoded string.
   *
   * @return The pool ID as a hex string
   */
  public String getPoolId() {
    return Util.bytesToHex(poolId).toLowerCase();
  }

  /**
   * Generates LiquidityPoolID object from a given XDR object
   *
   * @param xdr XDR object
   */
  public static LiquidityPoolId fromXdr(org.stellar.sdk.xdr.PoolID xdr) {
    return new LiquidityPoolId(xdr.getPoolID().getHash());
  }

  /** Generates XDR object from a given LiquidityPoolID object */
  public org.stellar.sdk.xdr.PoolID toXdr() {
    org.stellar.sdk.xdr.PoolID xdr = new org.stellar.sdk.xdr.PoolID();
    xdr.setPoolID(new org.stellar.sdk.xdr.Hash(poolId));
    return xdr;
  }

  @Override
  public String toString() {
    return getPoolId();
  }
}
