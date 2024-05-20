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
 * SorobanResources's original definition in the XDR file is:
 *
 * <pre>
 * struct SorobanResources
 * {
 *     // The ledger footprint of the transaction.
 *     LedgerFootprint footprint;
 *     // The maximum number of instructions this transaction can use
 *     uint32 instructions;
 *
 *     // The maximum number of bytes this transaction can read from ledger
 *     uint32 readBytes;
 *     // The maximum number of bytes this transaction can write to ledger
 *     uint32 writeBytes;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SorobanResources implements XdrElement {
  private LedgerFootprint footprint;
  private Uint32 instructions;
  private Uint32 readBytes;
  private Uint32 writeBytes;

  public static void encode(XdrDataOutputStream stream, SorobanResources encodedSorobanResources)
      throws IOException {
    LedgerFootprint.encode(stream, encodedSorobanResources.footprint);
    Uint32.encode(stream, encodedSorobanResources.instructions);
    Uint32.encode(stream, encodedSorobanResources.readBytes);
    Uint32.encode(stream, encodedSorobanResources.writeBytes);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static SorobanResources decode(XdrDataInputStream stream) throws IOException {
    SorobanResources decodedSorobanResources = new SorobanResources();
    decodedSorobanResources.footprint = LedgerFootprint.decode(stream);
    decodedSorobanResources.instructions = Uint32.decode(stream);
    decodedSorobanResources.readBytes = Uint32.decode(stream);
    decodedSorobanResources.writeBytes = Uint32.decode(stream);
    return decodedSorobanResources;
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

  public static SorobanResources fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SorobanResources fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
