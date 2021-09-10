package org.stellar.sdk;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.stellar.sdk.xdr.*;

/**
 * Base LiquidityPoolID class.
 * @see <a href="https://www.stellar.org/developers/learn/concepts/assets.html" target="_blank">Assets</a>
 */
public final class LiquidityPoolID {
  protected final byte[] hash;

  LiquidityPoolID(LiquidityPoolType type, Asset a, Asset b, int fee) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    try {
      LiquidityPoolParameters.create(type, a, b, fee).toXdr().encode(xdrDataOutputStream);
    } catch (IOException e) {
      throw new IllegalArgumentException("invalid liquidity pool id.", e);
    }
    hash = Util.hash(byteArrayOutputStream.toByteArray());
  }

  LiquidityPoolID(String hex) {
    hash = Util.hexToBytes(hex.toUpperCase());
  }

  LiquidityPoolID(byte[] bytes) {
    hash = bytes;
  }

  /**
   * Generates LiquidityPoolID object from a given XDR object
   * @param xdr XDR object
   */
  public static LiquidityPoolID fromXdr(org.stellar.sdk.xdr.PoolID xdr) {
    return new LiquidityPoolID(xdr.getPoolID().getHash());
  }

  @Override
  public String toString() {
    return Util.bytesToHex(hash).toLowerCase();
  }

  @Override
  public boolean equals(Object object) {
    if (object == null || !this.getClass().equals(object.getClass())) {
      return false;
    }

    LiquidityPoolID o = (LiquidityPoolID) object;

    return this.toString() == o.toString();
  }

  /**
   * Generates XDR object from a given LiquidityPoolID object
   */
  public org.stellar.sdk.xdr.PoolID toXdr() {
    org.stellar.sdk.xdr.PoolID xdr = new org.stellar.sdk.xdr.PoolID();
    xdr.setPoolID(new org.stellar.sdk.xdr.Hash(hash));
    return xdr;
  }
}
