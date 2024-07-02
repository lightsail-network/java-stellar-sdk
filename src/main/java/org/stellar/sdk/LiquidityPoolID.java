package org.stellar.sdk;

import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.stellar.sdk.exception.UnexpectedException;
import org.stellar.sdk.xdr.*;
import org.stellar.sdk.xdr.LiquidityPoolParameters;

/**
 * Base LiquidityPoolID class.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/learn/encyclopedia/sdex/liquidity-on-stellar-sdex-liquidity-pools#liquidity-pools"
 *     target="_blank">Liquidity Pool</a>
 */
@EqualsAndHashCode
@AllArgsConstructor
public final class LiquidityPoolID {
  private final byte[] hash;

  /**
   * Creates LiquidityPoolID object from a given hex-encoded pool ID.
   *
   * @param hex hex string
   */
  public LiquidityPoolID(String hex) {
    hash = Util.hexToBytes(hex.toUpperCase());
  }

  public LiquidityPoolID(LiquidityPool pool) {
    LiquidityPoolParameters liquidityPoolParameters = pool.toXdr();
    try {
      hash = Util.hash(liquidityPoolParameters.toXdrByteArray());
    } catch (IOException e) {
      throw new UnexpectedException(e);
    }
  }

  /**
   * Generates LiquidityPoolID object from a given XDR object
   *
   * @param xdr XDR object
   */
  public static LiquidityPoolID fromXdr(org.stellar.sdk.xdr.PoolID xdr) {
    return new LiquidityPoolID(xdr.getPoolID().getHash());
  }

  /** Generates XDR object from a given LiquidityPoolID object */
  public org.stellar.sdk.xdr.PoolID toXdr() {
    org.stellar.sdk.xdr.PoolID xdr = new org.stellar.sdk.xdr.PoolID();
    xdr.setPoolID(new org.stellar.sdk.xdr.Hash(hash));
    return xdr;
  }

  @Override
  public String toString() {
    return Util.bytesToHex(hash).toLowerCase();
  }
}
