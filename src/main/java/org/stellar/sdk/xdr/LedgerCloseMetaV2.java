// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

// === xdr source ============================================================

//  struct LedgerCloseMetaV2
//  {
//      // We forgot to add an ExtensionPoint in v1 but at least
//      // we can add one now in v2.
//      ExtensionPoint ext;
//
//      LedgerHeaderHistoryEntry ledgerHeader;
//
//      GeneralizedTransactionSet txSet;
//
//      // NB: transactions are sorted in apply order here
//      // fees for all transactions are processed first
//      // followed by applying transactions
//      TransactionResultMeta txProcessing<>;
//
//      // upgrades are applied last
//      UpgradeEntryMeta upgradesProcessing<>;
//
//      // other misc information attached to the ledger close
//      SCPHistoryEntry scpInfo<>;
//
//      // Size in bytes of BucketList, to support downstream
//      // systems calculating storage fees correctly.
//      uint64 totalByteSizeOfBucketList;
//
//      // Expired temp keys that are being evicted at this ledger.
//      LedgerKey evictedTemporaryLedgerKeys<>;
//
//      // Expired restorable ledger entries that are being
//      // evicted at this ledger.
//      LedgerEntry evictedPersistentLedgerEntries<>;
//  };

//  ===========================================================================
public class LedgerCloseMetaV2 implements XdrElement {
  public LedgerCloseMetaV2() {}

  private ExtensionPoint ext;

  public ExtensionPoint getExt() {
    return this.ext;
  }

  public void setExt(ExtensionPoint value) {
    this.ext = value;
  }

  private LedgerHeaderHistoryEntry ledgerHeader;

  public LedgerHeaderHistoryEntry getLedgerHeader() {
    return this.ledgerHeader;
  }

  public void setLedgerHeader(LedgerHeaderHistoryEntry value) {
    this.ledgerHeader = value;
  }

  private GeneralizedTransactionSet txSet;

  public GeneralizedTransactionSet getTxSet() {
    return this.txSet;
  }

  public void setTxSet(GeneralizedTransactionSet value) {
    this.txSet = value;
  }

  private TransactionResultMeta[] txProcessing;

  public TransactionResultMeta[] getTxProcessing() {
    return this.txProcessing;
  }

  public void setTxProcessing(TransactionResultMeta[] value) {
    this.txProcessing = value;
  }

  private UpgradeEntryMeta[] upgradesProcessing;

  public UpgradeEntryMeta[] getUpgradesProcessing() {
    return this.upgradesProcessing;
  }

  public void setUpgradesProcessing(UpgradeEntryMeta[] value) {
    this.upgradesProcessing = value;
  }

  private SCPHistoryEntry[] scpInfo;

  public SCPHistoryEntry[] getScpInfo() {
    return this.scpInfo;
  }

  public void setScpInfo(SCPHistoryEntry[] value) {
    this.scpInfo = value;
  }

  private Uint64 totalByteSizeOfBucketList;

  public Uint64 getTotalByteSizeOfBucketList() {
    return this.totalByteSizeOfBucketList;
  }

  public void setTotalByteSizeOfBucketList(Uint64 value) {
    this.totalByteSizeOfBucketList = value;
  }

  private LedgerKey[] evictedTemporaryLedgerKeys;

  public LedgerKey[] getEvictedTemporaryLedgerKeys() {
    return this.evictedTemporaryLedgerKeys;
  }

  public void setEvictedTemporaryLedgerKeys(LedgerKey[] value) {
    this.evictedTemporaryLedgerKeys = value;
  }

  private LedgerEntry[] evictedPersistentLedgerEntries;

  public LedgerEntry[] getEvictedPersistentLedgerEntries() {
    return this.evictedPersistentLedgerEntries;
  }

  public void setEvictedPersistentLedgerEntries(LedgerEntry[] value) {
    this.evictedPersistentLedgerEntries = value;
  }

  public static void encode(XdrDataOutputStream stream, LedgerCloseMetaV2 encodedLedgerCloseMetaV2)
      throws IOException {
    ExtensionPoint.encode(stream, encodedLedgerCloseMetaV2.ext);
    LedgerHeaderHistoryEntry.encode(stream, encodedLedgerCloseMetaV2.ledgerHeader);
    GeneralizedTransactionSet.encode(stream, encodedLedgerCloseMetaV2.txSet);
    int txProcessingsize = encodedLedgerCloseMetaV2.getTxProcessing().length;
    stream.writeInt(txProcessingsize);
    for (int i = 0; i < txProcessingsize; i++) {
      TransactionResultMeta.encode(stream, encodedLedgerCloseMetaV2.txProcessing[i]);
    }
    int upgradesProcessingsize = encodedLedgerCloseMetaV2.getUpgradesProcessing().length;
    stream.writeInt(upgradesProcessingsize);
    for (int i = 0; i < upgradesProcessingsize; i++) {
      UpgradeEntryMeta.encode(stream, encodedLedgerCloseMetaV2.upgradesProcessing[i]);
    }
    int scpInfosize = encodedLedgerCloseMetaV2.getScpInfo().length;
    stream.writeInt(scpInfosize);
    for (int i = 0; i < scpInfosize; i++) {
      SCPHistoryEntry.encode(stream, encodedLedgerCloseMetaV2.scpInfo[i]);
    }
    Uint64.encode(stream, encodedLedgerCloseMetaV2.totalByteSizeOfBucketList);
    int evictedTemporaryLedgerKeyssize =
        encodedLedgerCloseMetaV2.getEvictedTemporaryLedgerKeys().length;
    stream.writeInt(evictedTemporaryLedgerKeyssize);
    for (int i = 0; i < evictedTemporaryLedgerKeyssize; i++) {
      LedgerKey.encode(stream, encodedLedgerCloseMetaV2.evictedTemporaryLedgerKeys[i]);
    }
    int evictedPersistentLedgerEntriessize =
        encodedLedgerCloseMetaV2.getEvictedPersistentLedgerEntries().length;
    stream.writeInt(evictedPersistentLedgerEntriessize);
    for (int i = 0; i < evictedPersistentLedgerEntriessize; i++) {
      LedgerEntry.encode(stream, encodedLedgerCloseMetaV2.evictedPersistentLedgerEntries[i]);
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static LedgerCloseMetaV2 decode(XdrDataInputStream stream) throws IOException {
    LedgerCloseMetaV2 decodedLedgerCloseMetaV2 = new LedgerCloseMetaV2();
    decodedLedgerCloseMetaV2.ext = ExtensionPoint.decode(stream);
    decodedLedgerCloseMetaV2.ledgerHeader = LedgerHeaderHistoryEntry.decode(stream);
    decodedLedgerCloseMetaV2.txSet = GeneralizedTransactionSet.decode(stream);
    int txProcessingsize = stream.readInt();
    decodedLedgerCloseMetaV2.txProcessing = new TransactionResultMeta[txProcessingsize];
    for (int i = 0; i < txProcessingsize; i++) {
      decodedLedgerCloseMetaV2.txProcessing[i] = TransactionResultMeta.decode(stream);
    }
    int upgradesProcessingsize = stream.readInt();
    decodedLedgerCloseMetaV2.upgradesProcessing = new UpgradeEntryMeta[upgradesProcessingsize];
    for (int i = 0; i < upgradesProcessingsize; i++) {
      decodedLedgerCloseMetaV2.upgradesProcessing[i] = UpgradeEntryMeta.decode(stream);
    }
    int scpInfosize = stream.readInt();
    decodedLedgerCloseMetaV2.scpInfo = new SCPHistoryEntry[scpInfosize];
    for (int i = 0; i < scpInfosize; i++) {
      decodedLedgerCloseMetaV2.scpInfo[i] = SCPHistoryEntry.decode(stream);
    }
    decodedLedgerCloseMetaV2.totalByteSizeOfBucketList = Uint64.decode(stream);
    int evictedTemporaryLedgerKeyssize = stream.readInt();
    decodedLedgerCloseMetaV2.evictedTemporaryLedgerKeys =
        new LedgerKey[evictedTemporaryLedgerKeyssize];
    for (int i = 0; i < evictedTemporaryLedgerKeyssize; i++) {
      decodedLedgerCloseMetaV2.evictedTemporaryLedgerKeys[i] = LedgerKey.decode(stream);
    }
    int evictedPersistentLedgerEntriessize = stream.readInt();
    decodedLedgerCloseMetaV2.evictedPersistentLedgerEntries =
        new LedgerEntry[evictedPersistentLedgerEntriessize];
    for (int i = 0; i < evictedPersistentLedgerEntriessize; i++) {
      decodedLedgerCloseMetaV2.evictedPersistentLedgerEntries[i] = LedgerEntry.decode(stream);
    }
    return decodedLedgerCloseMetaV2;
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        this.ext,
        this.ledgerHeader,
        this.txSet,
        Arrays.hashCode(this.txProcessing),
        Arrays.hashCode(this.upgradesProcessing),
        Arrays.hashCode(this.scpInfo),
        this.totalByteSizeOfBucketList,
        Arrays.hashCode(this.evictedTemporaryLedgerKeys),
        Arrays.hashCode(this.evictedPersistentLedgerEntries));
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof LedgerCloseMetaV2)) {
      return false;
    }

    LedgerCloseMetaV2 other = (LedgerCloseMetaV2) object;
    return Objects.equals(this.ext, other.ext)
        && Objects.equals(this.ledgerHeader, other.ledgerHeader)
        && Objects.equals(this.txSet, other.txSet)
        && Arrays.equals(this.txProcessing, other.txProcessing)
        && Arrays.equals(this.upgradesProcessing, other.upgradesProcessing)
        && Arrays.equals(this.scpInfo, other.scpInfo)
        && Objects.equals(this.totalByteSizeOfBucketList, other.totalByteSizeOfBucketList)
        && Arrays.equals(this.evictedTemporaryLedgerKeys, other.evictedTemporaryLedgerKeys)
        && Arrays.equals(this.evictedPersistentLedgerEntries, other.evictedPersistentLedgerEntries);
  }

  @Override
  public String toXdrBase64() throws IOException {
    return Base64.getEncoder().encodeToString(toXdrByteArray());
  }

  @Override
  public byte[] toXdrByteArray() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    encode(xdrDataOutputStream);
    return byteArrayOutputStream.toByteArray();
  }

  public static LedgerCloseMetaV2 fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.getDecoder().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static LedgerCloseMetaV2 fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  public static final class Builder {
    private ExtensionPoint ext;
    private LedgerHeaderHistoryEntry ledgerHeader;
    private GeneralizedTransactionSet txSet;
    private TransactionResultMeta[] txProcessing;
    private UpgradeEntryMeta[] upgradesProcessing;
    private SCPHistoryEntry[] scpInfo;
    private Uint64 totalByteSizeOfBucketList;
    private LedgerKey[] evictedTemporaryLedgerKeys;
    private LedgerEntry[] evictedPersistentLedgerEntries;

    public Builder ext(ExtensionPoint ext) {
      this.ext = ext;
      return this;
    }

    public Builder ledgerHeader(LedgerHeaderHistoryEntry ledgerHeader) {
      this.ledgerHeader = ledgerHeader;
      return this;
    }

    public Builder txSet(GeneralizedTransactionSet txSet) {
      this.txSet = txSet;
      return this;
    }

    public Builder txProcessing(TransactionResultMeta[] txProcessing) {
      this.txProcessing = txProcessing;
      return this;
    }

    public Builder upgradesProcessing(UpgradeEntryMeta[] upgradesProcessing) {
      this.upgradesProcessing = upgradesProcessing;
      return this;
    }

    public Builder scpInfo(SCPHistoryEntry[] scpInfo) {
      this.scpInfo = scpInfo;
      return this;
    }

    public Builder totalByteSizeOfBucketList(Uint64 totalByteSizeOfBucketList) {
      this.totalByteSizeOfBucketList = totalByteSizeOfBucketList;
      return this;
    }

    public Builder evictedTemporaryLedgerKeys(LedgerKey[] evictedTemporaryLedgerKeys) {
      this.evictedTemporaryLedgerKeys = evictedTemporaryLedgerKeys;
      return this;
    }

    public Builder evictedPersistentLedgerEntries(LedgerEntry[] evictedPersistentLedgerEntries) {
      this.evictedPersistentLedgerEntries = evictedPersistentLedgerEntries;
      return this;
    }

    public LedgerCloseMetaV2 build() {
      LedgerCloseMetaV2 val = new LedgerCloseMetaV2();
      val.setExt(this.ext);
      val.setLedgerHeader(this.ledgerHeader);
      val.setTxSet(this.txSet);
      val.setTxProcessing(this.txProcessing);
      val.setUpgradesProcessing(this.upgradesProcessing);
      val.setScpInfo(this.scpInfo);
      val.setTotalByteSizeOfBucketList(this.totalByteSizeOfBucketList);
      val.setEvictedTemporaryLedgerKeys(this.evictedTemporaryLedgerKeys);
      val.setEvictedPersistentLedgerEntries(this.evictedPersistentLedgerEntries);
      return val;
    }
  }
}
