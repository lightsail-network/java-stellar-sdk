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
 * TTLEntry's original definition in the XDR file is:
 *
 * <pre>
 * struct TTLEntry {
 *     // Hash of the LedgerKey that is associated with this TTLEntry
 *     Hash keyHash;
 *     uint32 liveUntilLedgerSeq;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TTLEntry implements XdrElement {
  private Hash keyHash;
  private Uint32 liveUntilLedgerSeq;

  public void encode(XdrDataOutputStream stream) throws IOException {
    keyHash.encode(stream);
    liveUntilLedgerSeq.encode(stream);
  }

  public static TTLEntry decode(XdrDataInputStream stream) throws IOException {
    TTLEntry decodedTTLEntry = new TTLEntry();
    decodedTTLEntry.keyHash = Hash.decode(stream);
    decodedTTLEntry.liveUntilLedgerSeq = Uint32.decode(stream);
    return decodedTTLEntry;
  }

  public static TTLEntry fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static TTLEntry fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
