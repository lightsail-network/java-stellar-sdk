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
 * ExistenceProofBody's original definition in the XDR file is:
 *
 * <pre>
 * struct ExistenceProofBody
 * {
 *     LedgerKey keysToProve&lt;&gt;;
 *
 *     // Bounds for each key being proved, where bound[n]
 *     // corresponds to keysToProve[n]
 *     ColdArchiveBucketEntry lowBoundEntries&lt;&gt;;
 *     ColdArchiveBucketEntry highBoundEntries&lt;&gt;;
 *
 *     // Vector of vectors, where proofLevels[level]
 *     // contains all HashNodes that correspond with that level
 *     ProofLevel proofLevels&lt;&gt;;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ExistenceProofBody implements XdrElement {
  private LedgerKey[] keysToProve;
  private ColdArchiveBucketEntry[] lowBoundEntries;
  private ColdArchiveBucketEntry[] highBoundEntries;
  private ProofLevel[] proofLevels;

  public void encode(XdrDataOutputStream stream) throws IOException {
    int keysToProveSize = getKeysToProve().length;
    stream.writeInt(keysToProveSize);
    for (int i = 0; i < keysToProveSize; i++) {
      keysToProve[i].encode(stream);
    }
    int lowBoundEntriesSize = getLowBoundEntries().length;
    stream.writeInt(lowBoundEntriesSize);
    for (int i = 0; i < lowBoundEntriesSize; i++) {
      lowBoundEntries[i].encode(stream);
    }
    int highBoundEntriesSize = getHighBoundEntries().length;
    stream.writeInt(highBoundEntriesSize);
    for (int i = 0; i < highBoundEntriesSize; i++) {
      highBoundEntries[i].encode(stream);
    }
    int proofLevelsSize = getProofLevels().length;
    stream.writeInt(proofLevelsSize);
    for (int i = 0; i < proofLevelsSize; i++) {
      proofLevels[i].encode(stream);
    }
  }

  public static ExistenceProofBody decode(XdrDataInputStream stream) throws IOException {
    ExistenceProofBody decodedExistenceProofBody = new ExistenceProofBody();
    int keysToProveSize = stream.readInt();
    decodedExistenceProofBody.keysToProve = new LedgerKey[keysToProveSize];
    for (int i = 0; i < keysToProveSize; i++) {
      decodedExistenceProofBody.keysToProve[i] = LedgerKey.decode(stream);
    }
    int lowBoundEntriesSize = stream.readInt();
    decodedExistenceProofBody.lowBoundEntries = new ColdArchiveBucketEntry[lowBoundEntriesSize];
    for (int i = 0; i < lowBoundEntriesSize; i++) {
      decodedExistenceProofBody.lowBoundEntries[i] = ColdArchiveBucketEntry.decode(stream);
    }
    int highBoundEntriesSize = stream.readInt();
    decodedExistenceProofBody.highBoundEntries = new ColdArchiveBucketEntry[highBoundEntriesSize];
    for (int i = 0; i < highBoundEntriesSize; i++) {
      decodedExistenceProofBody.highBoundEntries[i] = ColdArchiveBucketEntry.decode(stream);
    }
    int proofLevelsSize = stream.readInt();
    decodedExistenceProofBody.proofLevels = new ProofLevel[proofLevelsSize];
    for (int i = 0; i < proofLevelsSize; i++) {
      decodedExistenceProofBody.proofLevels[i] = ProofLevel.decode(stream);
    }
    return decodedExistenceProofBody;
  }

  public static ExistenceProofBody fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static ExistenceProofBody fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}