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
 * SorobanAddressCredentials's original definition in the XDR file is:
 *
 * <pre>
 * struct SorobanAddressCredentials
 * {
 *     SCAddress address;
 *     int64 nonce;
 *     uint32 signatureExpirationLedger;
 *     SCVal signature;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SorobanAddressCredentials implements XdrElement {
  private SCAddress address;
  private Int64 nonce;
  private Uint32 signatureExpirationLedger;
  private SCVal signature;

  public static void encode(
      XdrDataOutputStream stream, SorobanAddressCredentials encodedSorobanAddressCredentials)
      throws IOException {
    SCAddress.encode(stream, encodedSorobanAddressCredentials.address);
    Int64.encode(stream, encodedSorobanAddressCredentials.nonce);
    Uint32.encode(stream, encodedSorobanAddressCredentials.signatureExpirationLedger);
    SCVal.encode(stream, encodedSorobanAddressCredentials.signature);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static SorobanAddressCredentials decode(XdrDataInputStream stream) throws IOException {
    SorobanAddressCredentials decodedSorobanAddressCredentials = new SorobanAddressCredentials();
    decodedSorobanAddressCredentials.address = SCAddress.decode(stream);
    decodedSorobanAddressCredentials.nonce = Int64.decode(stream);
    decodedSorobanAddressCredentials.signatureExpirationLedger = Uint32.decode(stream);
    decodedSorobanAddressCredentials.signature = SCVal.decode(stream);
    return decodedSorobanAddressCredentials;
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

  public static SorobanAddressCredentials fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SorobanAddressCredentials fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
