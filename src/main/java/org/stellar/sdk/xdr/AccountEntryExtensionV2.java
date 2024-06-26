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
 * AccountEntryExtensionV2's original definition in the XDR file is:
 *
 * <pre>
 * struct AccountEntryExtensionV2
 * {
 *     uint32 numSponsored;
 *     uint32 numSponsoring;
 *     SponsorshipDescriptor signerSponsoringIDs&lt;MAX_SIGNERS&gt;;
 *
 *     union switch (int v)
 *     {
 *     case 0:
 *         void;
 *     case 3:
 *         AccountEntryExtensionV3 v3;
 *     }
 *     ext;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AccountEntryExtensionV2 implements XdrElement {
  private Uint32 numSponsored;
  private Uint32 numSponsoring;
  private SponsorshipDescriptor[] signerSponsoringIDs;
  private AccountEntryExtensionV2Ext ext;

  public void encode(XdrDataOutputStream stream) throws IOException {
    numSponsored.encode(stream);
    numSponsoring.encode(stream);
    int signerSponsoringIDsSize = getSignerSponsoringIDs().length;
    stream.writeInt(signerSponsoringIDsSize);
    for (int i = 0; i < signerSponsoringIDsSize; i++) {
      signerSponsoringIDs[i].encode(stream);
    }
    ext.encode(stream);
  }

  public static AccountEntryExtensionV2 decode(XdrDataInputStream stream) throws IOException {
    AccountEntryExtensionV2 decodedAccountEntryExtensionV2 = new AccountEntryExtensionV2();
    decodedAccountEntryExtensionV2.numSponsored = Uint32.decode(stream);
    decodedAccountEntryExtensionV2.numSponsoring = Uint32.decode(stream);
    int signerSponsoringIDsSize = stream.readInt();
    decodedAccountEntryExtensionV2.signerSponsoringIDs =
        new SponsorshipDescriptor[signerSponsoringIDsSize];
    for (int i = 0; i < signerSponsoringIDsSize; i++) {
      decodedAccountEntryExtensionV2.signerSponsoringIDs[i] = SponsorshipDescriptor.decode(stream);
    }
    decodedAccountEntryExtensionV2.ext = AccountEntryExtensionV2Ext.decode(stream);
    return decodedAccountEntryExtensionV2;
  }

  public static AccountEntryExtensionV2 fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static AccountEntryExtensionV2 fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  /**
   * AccountEntryExtensionV2Ext's original definition in the XDR file is:
   *
   * <pre>
   * union switch (int v)
   *     {
   *     case 0:
   *         void;
   *     case 3:
   *         AccountEntryExtensionV3 v3;
   *     }
   * </pre>
   */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder(toBuilder = true)
  public static class AccountEntryExtensionV2Ext implements XdrElement {
    private Integer discriminant;
    private AccountEntryExtensionV3 v3;

    public void encode(XdrDataOutputStream stream) throws IOException {
      stream.writeInt(discriminant);
      switch (discriminant) {
        case 0:
          break;
        case 3:
          v3.encode(stream);
          break;
      }
    }

    public static AccountEntryExtensionV2Ext decode(XdrDataInputStream stream) throws IOException {
      AccountEntryExtensionV2Ext decodedAccountEntryExtensionV2Ext =
          new AccountEntryExtensionV2Ext();
      Integer discriminant = stream.readInt();
      decodedAccountEntryExtensionV2Ext.setDiscriminant(discriminant);
      switch (decodedAccountEntryExtensionV2Ext.getDiscriminant()) {
        case 0:
          break;
        case 3:
          decodedAccountEntryExtensionV2Ext.v3 = AccountEntryExtensionV3.decode(stream);
          break;
      }
      return decodedAccountEntryExtensionV2Ext;
    }

    public static AccountEntryExtensionV2Ext fromXdrBase64(String xdr) throws IOException {
      byte[] bytes = Base64Factory.getInstance().decode(xdr);
      return fromXdrByteArray(bytes);
    }

    public static AccountEntryExtensionV2Ext fromXdrByteArray(byte[] xdr) throws IOException {
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
      XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
      return decode(xdrDataInputStream);
    }
  }
}
