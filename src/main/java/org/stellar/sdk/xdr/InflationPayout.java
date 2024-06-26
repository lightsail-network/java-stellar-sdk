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
 * InflationPayout's original definition in the XDR file is:
 *
 * <pre>
 * struct InflationPayout // or use PaymentResultAtom to limit types?
 * {
 *     AccountID destination;
 *     int64 amount;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class InflationPayout implements XdrElement {
  private AccountID destination;
  private Int64 amount;

  public void encode(XdrDataOutputStream stream) throws IOException {
    destination.encode(stream);
    amount.encode(stream);
  }

  public static InflationPayout decode(XdrDataInputStream stream) throws IOException {
    InflationPayout decodedInflationPayout = new InflationPayout();
    decodedInflationPayout.destination = AccountID.decode(stream);
    decodedInflationPayout.amount = Int64.decode(stream);
    return decodedInflationPayout;
  }

  public static InflationPayout fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static InflationPayout fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
