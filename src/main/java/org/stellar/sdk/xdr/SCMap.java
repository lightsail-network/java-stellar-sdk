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
 * SCMap's original definition in the XDR file is:
 *
 * <pre>
 * typedef SCMapEntry SCMap&lt;&gt;;
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SCMap implements XdrElement {
  private SCMapEntry[] SCMap;

  public static void encode(XdrDataOutputStream stream, SCMap encodedSCMap) throws IOException {
    int SCMapSize = encodedSCMap.getSCMap().length;
    stream.writeInt(SCMapSize);
    for (int i = 0; i < SCMapSize; i++) {
      SCMapEntry.encode(stream, encodedSCMap.SCMap[i]);
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static SCMap decode(XdrDataInputStream stream) throws IOException {
    SCMap decodedSCMap = new SCMap();
    int SCMapSize = stream.readInt();
    decodedSCMap.SCMap = new SCMapEntry[SCMapSize];
    for (int i = 0; i < SCMapSize; i++) {
      decodedSCMap.SCMap[i] = SCMapEntry.decode(stream);
    }
    return decodedSCMap;
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

  public static SCMap fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SCMap fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
