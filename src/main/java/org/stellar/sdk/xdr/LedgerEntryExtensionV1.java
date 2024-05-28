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
 * LedgerEntryExtensionV1's original definition in the XDR file is:
 *
 * <pre>
 * struct LedgerEntryExtensionV1
 * {
 *     SponsorshipDescriptor sponsoringID;
 *
 *     union switch (int v)
 *     {
 *     case 0:
 *         void;
 *     }
 *     ext;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LedgerEntryExtensionV1 implements XdrElement {
  private SponsorshipDescriptor sponsoringID;
  private LedgerEntryExtensionV1Ext ext;

  public static void encode(
      XdrDataOutputStream stream, LedgerEntryExtensionV1 encodedLedgerEntryExtensionV1)
      throws IOException {
    SponsorshipDescriptor.encode(stream, encodedLedgerEntryExtensionV1.sponsoringID);
    LedgerEntryExtensionV1Ext.encode(stream, encodedLedgerEntryExtensionV1.ext);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static LedgerEntryExtensionV1 decode(XdrDataInputStream stream) throws IOException {
    LedgerEntryExtensionV1 decodedLedgerEntryExtensionV1 = new LedgerEntryExtensionV1();
    decodedLedgerEntryExtensionV1.sponsoringID = SponsorshipDescriptor.decode(stream);
    decodedLedgerEntryExtensionV1.ext = LedgerEntryExtensionV1Ext.decode(stream);
    return decodedLedgerEntryExtensionV1;
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

  public static LedgerEntryExtensionV1 fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static LedgerEntryExtensionV1 fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  /**
   * LedgerEntryExtensionV1Ext's original definition in the XDR file is:
   *
   * <pre>
   * union switch (int v)
   *     {
   *     case 0:
   *         void;
   *     }
   * </pre>
   */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder(toBuilder = true)
  public static class LedgerEntryExtensionV1Ext implements XdrElement {
    private Integer discriminant;

    public static void encode(
        XdrDataOutputStream stream, LedgerEntryExtensionV1Ext encodedLedgerEntryExtensionV1Ext)
        throws IOException {
      // Xdrgen::AST::Typespecs::Int
      // Integer
      stream.writeInt(encodedLedgerEntryExtensionV1Ext.getDiscriminant().intValue());
      switch (encodedLedgerEntryExtensionV1Ext.getDiscriminant()) {
        case 0:
          break;
      }
    }

    public void encode(XdrDataOutputStream stream) throws IOException {
      encode(stream, this);
    }

    public static LedgerEntryExtensionV1Ext decode(XdrDataInputStream stream) throws IOException {
      LedgerEntryExtensionV1Ext decodedLedgerEntryExtensionV1Ext = new LedgerEntryExtensionV1Ext();
      Integer discriminant = stream.readInt();
      decodedLedgerEntryExtensionV1Ext.setDiscriminant(discriminant);
      switch (decodedLedgerEntryExtensionV1Ext.getDiscriminant()) {
        case 0:
          break;
      }
      return decodedLedgerEntryExtensionV1Ext;
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

    public static LedgerEntryExtensionV1Ext fromXdrBase64(String xdr) throws IOException {
      byte[] bytes = Base64Factory.getInstance().decode(xdr);
      return fromXdrByteArray(bytes);
    }

    public static LedgerEntryExtensionV1Ext fromXdrByteArray(byte[] xdr) throws IOException {
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
      XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
      return decode(xdrDataInputStream);
    }
  }
}
