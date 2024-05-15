// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import org.stellar.sdk.Base64Factory;

/**
 * BucketEntry's original definition in the XDR file is:
 *
 * <pre>
 * union BucketEntry switch (BucketEntryType type)
 * {
 * case LIVEENTRY:
 * case INITENTRY:
 *     LedgerEntry liveEntry;
 *
 * case DEADENTRY:
 *     LedgerKey deadEntry;
 * case METAENTRY:
 *     BucketMetadata metaEntry;
 * };
 * </pre>
 */
public class BucketEntry implements XdrElement {
  public BucketEntry() {}

  BucketEntryType type;

  public BucketEntryType getDiscriminant() {
    return this.type;
  }

  public void setDiscriminant(BucketEntryType value) {
    this.type = value;
  }

  private LedgerEntry liveEntry;

  public LedgerEntry getLiveEntry() {
    return this.liveEntry;
  }

  public void setLiveEntry(LedgerEntry value) {
    this.liveEntry = value;
  }

  private LedgerKey deadEntry;

  public LedgerKey getDeadEntry() {
    return this.deadEntry;
  }

  public void setDeadEntry(LedgerKey value) {
    this.deadEntry = value;
  }

  private BucketMetadata metaEntry;

  public BucketMetadata getMetaEntry() {
    return this.metaEntry;
  }

  public void setMetaEntry(BucketMetadata value) {
    this.metaEntry = value;
  }

  public static final class Builder {
    private BucketEntryType discriminant;
    private LedgerEntry liveEntry;
    private LedgerKey deadEntry;
    private BucketMetadata metaEntry;

    public Builder discriminant(BucketEntryType discriminant) {
      this.discriminant = discriminant;
      return this;
    }

    public Builder liveEntry(LedgerEntry liveEntry) {
      this.liveEntry = liveEntry;
      return this;
    }

    public Builder deadEntry(LedgerKey deadEntry) {
      this.deadEntry = deadEntry;
      return this;
    }

    public Builder metaEntry(BucketMetadata metaEntry) {
      this.metaEntry = metaEntry;
      return this;
    }

    public BucketEntry build() {
      BucketEntry val = new BucketEntry();
      val.setDiscriminant(discriminant);
      val.setLiveEntry(this.liveEntry);
      val.setDeadEntry(this.deadEntry);
      val.setMetaEntry(this.metaEntry);
      return val;
    }
  }

  public static void encode(XdrDataOutputStream stream, BucketEntry encodedBucketEntry)
      throws IOException {
    // Xdrgen::AST::Identifier
    // BucketEntryType
    stream.writeInt(encodedBucketEntry.getDiscriminant().getValue());
    switch (encodedBucketEntry.getDiscriminant()) {
      case LIVEENTRY:
      case INITENTRY:
        LedgerEntry.encode(stream, encodedBucketEntry.liveEntry);
        break;
      case DEADENTRY:
        LedgerKey.encode(stream, encodedBucketEntry.deadEntry);
        break;
      case METAENTRY:
        BucketMetadata.encode(stream, encodedBucketEntry.metaEntry);
        break;
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static BucketEntry decode(XdrDataInputStream stream) throws IOException {
    BucketEntry decodedBucketEntry = new BucketEntry();
    BucketEntryType discriminant = BucketEntryType.decode(stream);
    decodedBucketEntry.setDiscriminant(discriminant);
    switch (decodedBucketEntry.getDiscriminant()) {
      case LIVEENTRY:
      case INITENTRY:
        decodedBucketEntry.liveEntry = LedgerEntry.decode(stream);
        break;
      case DEADENTRY:
        decodedBucketEntry.deadEntry = LedgerKey.decode(stream);
        break;
      case METAENTRY:
        decodedBucketEntry.metaEntry = BucketMetadata.decode(stream);
        break;
    }
    return decodedBucketEntry;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.liveEntry, this.deadEntry, this.metaEntry, this.type);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof BucketEntry)) {
      return false;
    }

    BucketEntry other = (BucketEntry) object;
    return Objects.equals(this.liveEntry, other.liveEntry)
        && Objects.equals(this.deadEntry, other.deadEntry)
        && Objects.equals(this.metaEntry, other.metaEntry)
        && Objects.equals(this.type, other.type);
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

  public static BucketEntry fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static BucketEntry fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
