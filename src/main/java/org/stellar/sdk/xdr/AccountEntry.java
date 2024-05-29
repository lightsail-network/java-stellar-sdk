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
 * AccountEntry's original definition in the XDR file is:
 *
 * <pre>
 * struct AccountEntry
 * {
 *     AccountID accountID;      // master public key for this account
 *     int64 balance;            // in stroops
 *     SequenceNumber seqNum;    // last sequence number used for this account
 *     uint32 numSubEntries;     // number of sub-entries this account has
 *                               // drives the reserve
 *     AccountID&#42; inflationDest; // Account to vote for during inflation
 *     uint32 flags;             // see AccountFlags
 *
 *     string32 homeDomain; // can be used for reverse federation and memo lookup
 *
 *     // fields used for signatures
 *     // thresholds stores unsigned bytes: [weight of master|low|medium|high]
 *     Thresholds thresholds;
 *
 *     Signer signers&lt;MAX_SIGNERS&gt;; // possible signers for this account
 *
 *     // reserved for future use
 *     union switch (int v)
 *     {
 *     case 0:
 *         void;
 *     case 1:
 *         AccountEntryExtensionV1 v1;
 *     }
 *     ext;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AccountEntry implements XdrElement {
  private AccountID accountID;
  private Int64 balance;
  private SequenceNumber seqNum;
  private Uint32 numSubEntries;
  private AccountID inflationDest;
  private Uint32 flags;
  private String32 homeDomain;
  private Thresholds thresholds;
  private Signer[] signers;
  private AccountEntryExt ext;

  public void encode(XdrDataOutputStream stream) throws IOException {
    accountID.encode(stream);
    balance.encode(stream);
    seqNum.encode(stream);
    numSubEntries.encode(stream);
    if (inflationDest != null) {
      stream.writeInt(1);
      inflationDest.encode(stream);
    } else {
      stream.writeInt(0);
    }
    flags.encode(stream);
    homeDomain.encode(stream);
    thresholds.encode(stream);
    int signersSize = getSigners().length;
    stream.writeInt(signersSize);
    for (int i = 0; i < signersSize; i++) {
      signers[i].encode(stream);
    }
    ext.encode(stream);
  }

  public static AccountEntry decode(XdrDataInputStream stream) throws IOException {
    AccountEntry decodedAccountEntry = new AccountEntry();
    decodedAccountEntry.accountID = AccountID.decode(stream);
    decodedAccountEntry.balance = Int64.decode(stream);
    decodedAccountEntry.seqNum = SequenceNumber.decode(stream);
    decodedAccountEntry.numSubEntries = Uint32.decode(stream);
    int inflationDestPresent = stream.readInt();
    if (inflationDestPresent != 0) {
      decodedAccountEntry.inflationDest = AccountID.decode(stream);
    }
    decodedAccountEntry.flags = Uint32.decode(stream);
    decodedAccountEntry.homeDomain = String32.decode(stream);
    decodedAccountEntry.thresholds = Thresholds.decode(stream);
    int signersSize = stream.readInt();
    decodedAccountEntry.signers = new Signer[signersSize];
    for (int i = 0; i < signersSize; i++) {
      decodedAccountEntry.signers[i] = Signer.decode(stream);
    }
    decodedAccountEntry.ext = AccountEntryExt.decode(stream);
    return decodedAccountEntry;
  }

  public static AccountEntry fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static AccountEntry fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  /**
   * AccountEntryExt's original definition in the XDR file is:
   *
   * <pre>
   * union switch (int v)
   *     {
   *     case 0:
   *         void;
   *     case 1:
   *         AccountEntryExtensionV1 v1;
   *     }
   * </pre>
   */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder(toBuilder = true)
  public static class AccountEntryExt implements XdrElement {
    private Integer discriminant;
    private AccountEntryExtensionV1 v1;

    public void encode(XdrDataOutputStream stream) throws IOException {
      stream.writeInt(discriminant);
      switch (discriminant) {
        case 0:
          break;
        case 1:
          v1.encode(stream);
          break;
      }
    }

    public static AccountEntryExt decode(XdrDataInputStream stream) throws IOException {
      AccountEntryExt decodedAccountEntryExt = new AccountEntryExt();
      Integer discriminant = stream.readInt();
      decodedAccountEntryExt.setDiscriminant(discriminant);
      switch (decodedAccountEntryExt.getDiscriminant()) {
        case 0:
          break;
        case 1:
          decodedAccountEntryExt.v1 = AccountEntryExtensionV1.decode(stream);
          break;
      }
      return decodedAccountEntryExt;
    }

    public static AccountEntryExt fromXdrBase64(String xdr) throws IOException {
      byte[] bytes = Base64Factory.getInstance().decode(xdr);
      return fromXdrByteArray(bytes);
    }

    public static AccountEntryExt fromXdrByteArray(byte[] xdr) throws IOException {
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
      XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
      return decode(xdrDataInputStream);
    }
  }
}
