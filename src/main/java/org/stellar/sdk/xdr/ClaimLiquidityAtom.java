// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.stellar.sdk.Base64Factory;

/**
 * ClaimLiquidityAtom's original definition in the XDR file is:
 *
 * <pre>
 * struct ClaimLiquidityAtom
 * {
 *     PoolID liquidityPoolID;
 *
 *     // amount and asset taken from the pool
 *     Asset assetSold;
 *     int64 amountSold;
 *
 *     // amount and asset sent to the pool
 *     Asset assetBought;
 *     int64 amountBought;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ClaimLiquidityAtom implements XdrElement {
  private PoolID liquidityPoolID;
  private Asset assetSold;
  private Int64 amountSold;
  private Asset assetBought;
  private Int64 amountBought;

  public void encode(XdrDataOutputStream stream) throws IOException {
    liquidityPoolID.encode(stream);
    assetSold.encode(stream);
    amountSold.encode(stream);
    assetBought.encode(stream);
    amountBought.encode(stream);
  }

  public static ClaimLiquidityAtom decode(XdrDataInputStream stream) throws IOException {
    ClaimLiquidityAtom decodedClaimLiquidityAtom = new ClaimLiquidityAtom();
    decodedClaimLiquidityAtom.liquidityPoolID = PoolID.decode(stream);
    decodedClaimLiquidityAtom.assetSold = Asset.decode(stream);
    decodedClaimLiquidityAtom.amountSold = Int64.decode(stream);
    decodedClaimLiquidityAtom.assetBought = Asset.decode(stream);
    decodedClaimLiquidityAtom.amountBought = Int64.decode(stream);
    return decodedClaimLiquidityAtom;
  }

  public static ClaimLiquidityAtom fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static ClaimLiquidityAtom fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
