// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.stellar.sdk.Base64Factory;

/**
 * LiquidityPoolDepositOp's original definition in the XDR file is:
 *
 * <pre>
 * struct LiquidityPoolDepositOp
 * {
 *     PoolID liquidityPoolID;
 *     int64 maxAmountA; // maximum amount of first asset to deposit
 *     int64 maxAmountB; // maximum amount of second asset to deposit
 *     Price minPrice;   // minimum depositA/depositB
 *     Price maxPrice;   // maximum depositA/depositB
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LiquidityPoolDepositOp implements XdrElement {
  private PoolID liquidityPoolID;
  private Int64 maxAmountA;
  private Int64 maxAmountB;
  private Price minPrice;
  private Price maxPrice;

  public static void encode(
      XdrDataOutputStream stream, LiquidityPoolDepositOp encodedLiquidityPoolDepositOp)
      throws IOException {
    PoolID.encode(stream, encodedLiquidityPoolDepositOp.liquidityPoolID);
    Int64.encode(stream, encodedLiquidityPoolDepositOp.maxAmountA);
    Int64.encode(stream, encodedLiquidityPoolDepositOp.maxAmountB);
    Price.encode(stream, encodedLiquidityPoolDepositOp.minPrice);
    Price.encode(stream, encodedLiquidityPoolDepositOp.maxPrice);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static LiquidityPoolDepositOp decode(XdrDataInputStream stream) throws IOException {
    LiquidityPoolDepositOp decodedLiquidityPoolDepositOp = new LiquidityPoolDepositOp();
    decodedLiquidityPoolDepositOp.liquidityPoolID = PoolID.decode(stream);
    decodedLiquidityPoolDepositOp.maxAmountA = Int64.decode(stream);
    decodedLiquidityPoolDepositOp.maxAmountB = Int64.decode(stream);
    decodedLiquidityPoolDepositOp.minPrice = Price.decode(stream);
    decodedLiquidityPoolDepositOp.maxPrice = Price.decode(stream);
    return decodedLiquidityPoolDepositOp;
  }

  @Override
  public String toXdrBase64() throws IOException {
    return Base64Factory.getInstance().encodeToString(toXdrByteArray());
  }

  @Override
  public byte[] toXdrByteArray() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    encode(xdrDataOutputStream);
    return byteArrayOutputStream.toByteArray();
  }

  public static LiquidityPoolDepositOp fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static LiquidityPoolDepositOp fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
