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
 * Curve25519Public's original definition in the XDR file is:
 *
 * <pre>
 * struct Curve25519Public
 * {
 *     opaque key[32];
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Curve25519Public implements XdrElement {
  private byte[] key;

  public static void encode(XdrDataOutputStream stream, Curve25519Public encodedCurve25519Public)
      throws IOException {
    int keySize = encodedCurve25519Public.key.length;
    stream.write(encodedCurve25519Public.getKey(), 0, keySize);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static Curve25519Public decode(XdrDataInputStream stream) throws IOException {
    Curve25519Public decodedCurve25519Public = new Curve25519Public();
    int keySize = 32;
    decodedCurve25519Public.key = new byte[keySize];
    stream.read(decodedCurve25519Public.key, 0, keySize);
    return decodedCurve25519Public;
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

  public static Curve25519Public fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static Curve25519Public fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
