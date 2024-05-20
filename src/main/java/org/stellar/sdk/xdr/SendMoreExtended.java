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
 * SendMoreExtended's original definition in the XDR file is:
 *
 * <pre>
 * struct SendMoreExtended
 * {
 *     uint32 numMessages;
 *     uint32 numBytes;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SendMoreExtended implements XdrElement {
  private Uint32 numMessages;
  private Uint32 numBytes;

  public static void encode(XdrDataOutputStream stream, SendMoreExtended encodedSendMoreExtended)
      throws IOException {
    Uint32.encode(stream, encodedSendMoreExtended.numMessages);
    Uint32.encode(stream, encodedSendMoreExtended.numBytes);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static SendMoreExtended decode(XdrDataInputStream stream) throws IOException {
    SendMoreExtended decodedSendMoreExtended = new SendMoreExtended();
    decodedSendMoreExtended.numMessages = Uint32.decode(stream);
    decodedSendMoreExtended.numBytes = Uint32.decode(stream);
    return decodedSendMoreExtended;
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

  public static SendMoreExtended fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SendMoreExtended fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
