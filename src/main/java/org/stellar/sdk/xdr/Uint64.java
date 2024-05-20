// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.stellar.sdk.Base64Factory;

/**
 * Uint64's original definition in the XDR file is:
 *
 * <pre>
 * typedef unsigned hyper uint64;
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Uint64 implements XdrElement {
  private XdrUnsignedHyperInteger uint64;

  public static void encode(XdrDataOutputStream stream, Uint64 encodedUint64) throws IOException {
    encodedUint64.uint64.encode(stream);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static Uint64 decode(XdrDataInputStream stream) throws IOException {
    Uint64 decodedUint64 = new Uint64();
    decodedUint64.uint64 = XdrUnsignedHyperInteger.decode(stream);
    return decodedUint64;
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

  public static Uint64 fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static Uint64 fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
