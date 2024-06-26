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
 * LiquidityPoolWithdrawOp's original definition in the XDR file is:
 *
 * <pre>
 * struct LiquidityPoolWithdrawOp
 * {
 *     PoolID liquidityPoolID;
 *     int64 amount;     // amount of pool shares to withdraw
 *     int64 minAmountA; // minimum amount of first asset to withdraw
 *     int64 minAmountB; // minimum amount of second asset to withdraw
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LiquidityPoolWithdrawOp implements XdrElement {
  private PoolID liquidityPoolID;
  private Int64 amount;
  private Int64 minAmountA;
  private Int64 minAmountB;

  public void encode(XdrDataOutputStream stream) throws IOException {
    liquidityPoolID.encode(stream);
    amount.encode(stream);
    minAmountA.encode(stream);
    minAmountB.encode(stream);
  }

  public static LiquidityPoolWithdrawOp decode(XdrDataInputStream stream) throws IOException {
    LiquidityPoolWithdrawOp decodedLiquidityPoolWithdrawOp = new LiquidityPoolWithdrawOp();
    decodedLiquidityPoolWithdrawOp.liquidityPoolID = PoolID.decode(stream);
    decodedLiquidityPoolWithdrawOp.amount = Int64.decode(stream);
    decodedLiquidityPoolWithdrawOp.minAmountA = Int64.decode(stream);
    decodedLiquidityPoolWithdrawOp.minAmountB = Int64.decode(stream);
    return decodedLiquidityPoolWithdrawOp;
  }

  public static LiquidityPoolWithdrawOp fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static LiquidityPoolWithdrawOp fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
