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
 * LedgerCloseMetaExtV1's original definition in the XDR file is:
 *
 * <pre>
 * struct LedgerCloseMetaExtV1
 * {
 *     ExtensionPoint ext;
 *     int64 sorobanFeeWrite1KB;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LedgerCloseMetaExtV1 implements XdrElement {
  private ExtensionPoint ext;
  private Int64 sorobanFeeWrite1KB;

  public static void encode(
      XdrDataOutputStream stream, LedgerCloseMetaExtV1 encodedLedgerCloseMetaExtV1)
      throws IOException {
    ExtensionPoint.encode(stream, encodedLedgerCloseMetaExtV1.ext);
    Int64.encode(stream, encodedLedgerCloseMetaExtV1.sorobanFeeWrite1KB);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static LedgerCloseMetaExtV1 decode(XdrDataInputStream stream) throws IOException {
    LedgerCloseMetaExtV1 decodedLedgerCloseMetaExtV1 = new LedgerCloseMetaExtV1();
    decodedLedgerCloseMetaExtV1.ext = ExtensionPoint.decode(stream);
    decodedLedgerCloseMetaExtV1.sorobanFeeWrite1KB = Int64.decode(stream);
    return decodedLedgerCloseMetaExtV1;
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

  public static LedgerCloseMetaExtV1 fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static LedgerCloseMetaExtV1 fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
