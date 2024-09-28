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
 * ColdArchiveBucketEntry's original definition in the XDR file is:
 *
 * <pre>
 * union ColdArchiveBucketEntry switch (ColdArchiveBucketEntryType type)
 * {
 * case COLD_ARCHIVE_METAENTRY:
 *     BucketMetadata metaEntry;
 * case COLD_ARCHIVE_ARCHIVED_LEAF:
 *     ColdArchiveArchivedLeaf archivedLeaf;
 * case COLD_ARCHIVE_DELETED_LEAF:
 *     ColdArchiveDeletedLeaf deletedLeaf;
 * case COLD_ARCHIVE_BOUNDARY_LEAF:
 *     ColdArchiveBoundaryLeaf boundaryLeaf;
 * case COLD_ARCHIVE_HASH:
 *     ColdArchiveHashEntry hashEntry;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ColdArchiveBucketEntry implements XdrElement {
  private ColdArchiveBucketEntryType discriminant;
  private BucketMetadata metaEntry;
  private ColdArchiveArchivedLeaf archivedLeaf;
  private ColdArchiveDeletedLeaf deletedLeaf;
  private ColdArchiveBoundaryLeaf boundaryLeaf;
  private ColdArchiveHashEntry hashEntry;

  public void encode(XdrDataOutputStream stream) throws IOException {
    stream.writeInt(discriminant.getValue());
    switch (discriminant) {
      case COLD_ARCHIVE_METAENTRY:
        metaEntry.encode(stream);
        break;
      case COLD_ARCHIVE_ARCHIVED_LEAF:
        archivedLeaf.encode(stream);
        break;
      case COLD_ARCHIVE_DELETED_LEAF:
        deletedLeaf.encode(stream);
        break;
      case COLD_ARCHIVE_BOUNDARY_LEAF:
        boundaryLeaf.encode(stream);
        break;
      case COLD_ARCHIVE_HASH:
        hashEntry.encode(stream);
        break;
    }
  }

  public static ColdArchiveBucketEntry decode(XdrDataInputStream stream) throws IOException {
    ColdArchiveBucketEntry decodedColdArchiveBucketEntry = new ColdArchiveBucketEntry();
    ColdArchiveBucketEntryType discriminant = ColdArchiveBucketEntryType.decode(stream);
    decodedColdArchiveBucketEntry.setDiscriminant(discriminant);
    switch (decodedColdArchiveBucketEntry.getDiscriminant()) {
      case COLD_ARCHIVE_METAENTRY:
        decodedColdArchiveBucketEntry.metaEntry = BucketMetadata.decode(stream);
        break;
      case COLD_ARCHIVE_ARCHIVED_LEAF:
        decodedColdArchiveBucketEntry.archivedLeaf = ColdArchiveArchivedLeaf.decode(stream);
        break;
      case COLD_ARCHIVE_DELETED_LEAF:
        decodedColdArchiveBucketEntry.deletedLeaf = ColdArchiveDeletedLeaf.decode(stream);
        break;
      case COLD_ARCHIVE_BOUNDARY_LEAF:
        decodedColdArchiveBucketEntry.boundaryLeaf = ColdArchiveBoundaryLeaf.decode(stream);
        break;
      case COLD_ARCHIVE_HASH:
        decodedColdArchiveBucketEntry.hashEntry = ColdArchiveHashEntry.decode(stream);
        break;
    }
    return decodedColdArchiveBucketEntry;
  }

  public static ColdArchiveBucketEntry fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static ColdArchiveBucketEntry fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
