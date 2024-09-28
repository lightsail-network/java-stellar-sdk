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
 * NonexistenceProofBody's original definition in the XDR file is:
 *
 * <pre>
 * struct NonexistenceProofBody
 * {
 *     ColdArchiveBucketEntry entriesToProve&lt;&gt;;
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
public class NonexistenceProofBody implements XdrElement {
  private ColdArchiveBucketEntry[] entriesToProve;
  private ProofLevel[] proofLevels;

  public void encode(XdrDataOutputStream stream) throws IOException {
    int entriesToProveSize = getEntriesToProve().length;
    stream.writeInt(entriesToProveSize);
    for (int i = 0; i < entriesToProveSize; i++) {
      entriesToProve[i].encode(stream);
    }
    int proofLevelsSize = getProofLevels().length;
    stream.writeInt(proofLevelsSize);
    for (int i = 0; i < proofLevelsSize; i++) {
      proofLevels[i].encode(stream);
    }
  }

  public static NonexistenceProofBody decode(XdrDataInputStream stream) throws IOException {
    NonexistenceProofBody decodedNonexistenceProofBody = new NonexistenceProofBody();
    int entriesToProveSize = stream.readInt();
    decodedNonexistenceProofBody.entriesToProve = new ColdArchiveBucketEntry[entriesToProveSize];
    for (int i = 0; i < entriesToProveSize; i++) {
      decodedNonexistenceProofBody.entriesToProve[i] = ColdArchiveBucketEntry.decode(stream);
    }
    int proofLevelsSize = stream.readInt();
    decodedNonexistenceProofBody.proofLevels = new ProofLevel[proofLevelsSize];
    for (int i = 0; i < proofLevelsSize; i++) {
      decodedNonexistenceProofBody.proofLevels[i] = ProofLevel.decode(stream);
    }
    return decodedNonexistenceProofBody;
  }

  public static NonexistenceProofBody fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static NonexistenceProofBody fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
