// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
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
public class AccountEntry implements XdrElement {
  public AccountEntry() {}

  private AccountID accountID;

  public AccountID getAccountID() {
    return this.accountID;
  }

  public void setAccountID(AccountID value) {
    this.accountID = value;
  }

  private Int64 balance;

  public Int64 getBalance() {
    return this.balance;
  }

  public void setBalance(Int64 value) {
    this.balance = value;
  }

  private SequenceNumber seqNum;

  public SequenceNumber getSeqNum() {
    return this.seqNum;
  }

  public void setSeqNum(SequenceNumber value) {
    this.seqNum = value;
  }

  private Uint32 numSubEntries;

  public Uint32 getNumSubEntries() {
    return this.numSubEntries;
  }

  public void setNumSubEntries(Uint32 value) {
    this.numSubEntries = value;
  }

  private AccountID inflationDest;

  public AccountID getInflationDest() {
    return this.inflationDest;
  }

  public void setInflationDest(AccountID value) {
    this.inflationDest = value;
  }

  private Uint32 flags;

  public Uint32 getFlags() {
    return this.flags;
  }

  public void setFlags(Uint32 value) {
    this.flags = value;
  }

  private String32 homeDomain;

  public String32 getHomeDomain() {
    return this.homeDomain;
  }

  public void setHomeDomain(String32 value) {
    this.homeDomain = value;
  }

  private Thresholds thresholds;

  public Thresholds getThresholds() {
    return this.thresholds;
  }

  public void setThresholds(Thresholds value) {
    this.thresholds = value;
  }

  private Signer[] signers;

  public Signer[] getSigners() {
    return this.signers;
  }

  public void setSigners(Signer[] value) {
    this.signers = value;
  }

  private AccountEntryExt ext;

  public AccountEntryExt getExt() {
    return this.ext;
  }

  public void setExt(AccountEntryExt value) {
    this.ext = value;
  }

  public static void encode(XdrDataOutputStream stream, AccountEntry encodedAccountEntry)
      throws IOException {
    AccountID.encode(stream, encodedAccountEntry.accountID);
    Int64.encode(stream, encodedAccountEntry.balance);
    SequenceNumber.encode(stream, encodedAccountEntry.seqNum);
    Uint32.encode(stream, encodedAccountEntry.numSubEntries);
    if (encodedAccountEntry.inflationDest != null) {
      stream.writeInt(1);
      AccountID.encode(stream, encodedAccountEntry.inflationDest);
    } else {
      stream.writeInt(0);
    }
    Uint32.encode(stream, encodedAccountEntry.flags);
    String32.encode(stream, encodedAccountEntry.homeDomain);
    Thresholds.encode(stream, encodedAccountEntry.thresholds);
    int signerssize = encodedAccountEntry.getSigners().length;
    stream.writeInt(signerssize);
    for (int i = 0; i < signerssize; i++) {
      Signer.encode(stream, encodedAccountEntry.signers[i]);
    }
    AccountEntryExt.encode(stream, encodedAccountEntry.ext);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
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
    int signerssize = stream.readInt();
    decodedAccountEntry.signers = new Signer[signerssize];
    for (int i = 0; i < signerssize; i++) {
      decodedAccountEntry.signers[i] = Signer.decode(stream);
    }
    decodedAccountEntry.ext = AccountEntryExt.decode(stream);
    return decodedAccountEntry;
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        this.accountID,
        this.balance,
        this.seqNum,
        this.numSubEntries,
        this.inflationDest,
        this.flags,
        this.homeDomain,
        this.thresholds,
        Arrays.hashCode(this.signers),
        this.ext);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof AccountEntry)) {
      return false;
    }

    AccountEntry other = (AccountEntry) object;
    return Objects.equals(this.accountID, other.accountID)
        && Objects.equals(this.balance, other.balance)
        && Objects.equals(this.seqNum, other.seqNum)
        && Objects.equals(this.numSubEntries, other.numSubEntries)
        && Objects.equals(this.inflationDest, other.inflationDest)
        && Objects.equals(this.flags, other.flags)
        && Objects.equals(this.homeDomain, other.homeDomain)
        && Objects.equals(this.thresholds, other.thresholds)
        && Arrays.equals(this.signers, other.signers)
        && Objects.equals(this.ext, other.ext);
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

  public static AccountEntry fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static AccountEntry fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  public static final class Builder {
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

    public Builder accountID(AccountID accountID) {
      this.accountID = accountID;
      return this;
    }

    public Builder balance(Int64 balance) {
      this.balance = balance;
      return this;
    }

    public Builder seqNum(SequenceNumber seqNum) {
      this.seqNum = seqNum;
      return this;
    }

    public Builder numSubEntries(Uint32 numSubEntries) {
      this.numSubEntries = numSubEntries;
      return this;
    }

    public Builder inflationDest(AccountID inflationDest) {
      this.inflationDest = inflationDest;
      return this;
    }

    public Builder flags(Uint32 flags) {
      this.flags = flags;
      return this;
    }

    public Builder homeDomain(String32 homeDomain) {
      this.homeDomain = homeDomain;
      return this;
    }

    public Builder thresholds(Thresholds thresholds) {
      this.thresholds = thresholds;
      return this;
    }

    public Builder signers(Signer[] signers) {
      this.signers = signers;
      return this;
    }

    public Builder ext(AccountEntryExt ext) {
      this.ext = ext;
      return this;
    }

    public AccountEntry build() {
      AccountEntry val = new AccountEntry();
      val.setAccountID(this.accountID);
      val.setBalance(this.balance);
      val.setSeqNum(this.seqNum);
      val.setNumSubEntries(this.numSubEntries);
      val.setInflationDest(this.inflationDest);
      val.setFlags(this.flags);
      val.setHomeDomain(this.homeDomain);
      val.setThresholds(this.thresholds);
      val.setSigners(this.signers);
      val.setExt(this.ext);
      return val;
    }
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
  public static class AccountEntryExt implements XdrElement {
    public AccountEntryExt() {}

    Integer v;

    public Integer getDiscriminant() {
      return this.v;
    }

    public void setDiscriminant(Integer value) {
      this.v = value;
    }

    private AccountEntryExtensionV1 v1;

    public AccountEntryExtensionV1 getV1() {
      return this.v1;
    }

    public void setV1(AccountEntryExtensionV1 value) {
      this.v1 = value;
    }

    public static final class Builder {
      private Integer discriminant;
      private AccountEntryExtensionV1 v1;

      public Builder discriminant(Integer discriminant) {
        this.discriminant = discriminant;
        return this;
      }

      public Builder v1(AccountEntryExtensionV1 v1) {
        this.v1 = v1;
        return this;
      }

      public AccountEntryExt build() {
        AccountEntryExt val = new AccountEntryExt();
        val.setDiscriminant(discriminant);
        val.setV1(this.v1);
        return val;
      }
    }

    public static void encode(XdrDataOutputStream stream, AccountEntryExt encodedAccountEntryExt)
        throws IOException {
      // Xdrgen::AST::Typespecs::Int
      // Integer
      stream.writeInt(encodedAccountEntryExt.getDiscriminant().intValue());
      switch (encodedAccountEntryExt.getDiscriminant()) {
        case 0:
          break;
        case 1:
          AccountEntryExtensionV1.encode(stream, encodedAccountEntryExt.v1);
          break;
      }
    }

    public void encode(XdrDataOutputStream stream) throws IOException {
      encode(stream, this);
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

    @Override
    public int hashCode() {
      return Objects.hash(this.v1, this.v);
    }

    @Override
    public boolean equals(Object object) {
      if (!(object instanceof AccountEntryExt)) {
        return false;
      }

      AccountEntryExt other = (AccountEntryExt) object;
      return Objects.equals(this.v1, other.v1) && Objects.equals(this.v, other.v);
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
