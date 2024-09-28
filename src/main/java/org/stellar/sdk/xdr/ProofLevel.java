// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.stellar.sdk.Base64Factory;

/**
 * ProofLevel's original definition in the XDR file is:
 *
 * <pre>
 * typedef ArchivalProofNode ProofLevel&lt;&gt;;
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProofLevel implements XdrElement {
  private ArchivalProofNode[] ProofLevel;

  public void encode(XdrDataOutputStream stream) throws IOException {
    int ProofLevelSize = getProofLevel().length;
    stream.writeInt(ProofLevelSize);
    for (int i = 0; i < ProofLevelSize; i++) {
      ProofLevel[i].encode(stream);
    }
  }

  public static ProofLevel decode(XdrDataInputStream stream) throws IOException {
    ProofLevel decodedProofLevel = new ProofLevel();
    int ProofLevelSize = stream.readInt();
    decodedProofLevel.ProofLevel = new ArchivalProofNode[ProofLevelSize];
    for (int i = 0; i < ProofLevelSize; i++) {
      decodedProofLevel.ProofLevel[i] = ArchivalProofNode.decode(stream);
    }
    return decodedProofLevel;
  }

  public static ProofLevel fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static ProofLevel fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}