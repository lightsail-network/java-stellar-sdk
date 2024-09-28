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
 * ArchivalProof's original definition in the XDR file is:
 *
 * <pre>
 * struct ArchivalProof
 * {
 *     uint32 epoch; // AST Subtree for this proof
 *
 *     union switch (ArchivalProofType t)
 *     {
 *     case EXISTENCE:
 *         NonexistenceProofBody nonexistenceProof;
 *     case NONEXISTENCE:
 *         ExistenceProofBody existenceProof;
 *     } body;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ArchivalProof implements XdrElement {
  private Uint32 epoch;
  private ArchivalProofBody body;

  public void encode(XdrDataOutputStream stream) throws IOException {
    epoch.encode(stream);
    body.encode(stream);
  }

  public static ArchivalProof decode(XdrDataInputStream stream) throws IOException {
    ArchivalProof decodedArchivalProof = new ArchivalProof();
    decodedArchivalProof.epoch = Uint32.decode(stream);
    decodedArchivalProof.body = ArchivalProofBody.decode(stream);
    return decodedArchivalProof;
  }

  public static ArchivalProof fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static ArchivalProof fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  /**
   * ArchivalProofBody's original definition in the XDR file is:
   *
   * <pre>
   * union switch (ArchivalProofType t)
   *     {
   *     case EXISTENCE:
   *         NonexistenceProofBody nonexistenceProof;
   *     case NONEXISTENCE:
   *         ExistenceProofBody existenceProof;
   *     }
   * </pre>
   */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder(toBuilder = true)
  public static class ArchivalProofBody implements XdrElement {
    private ArchivalProofType discriminant;
    private NonexistenceProofBody nonexistenceProof;
    private ExistenceProofBody existenceProof;

    public void encode(XdrDataOutputStream stream) throws IOException {
      stream.writeInt(discriminant.getValue());
      switch (discriminant) {
        case EXISTENCE:
          nonexistenceProof.encode(stream);
          break;
        case NONEXISTENCE:
          existenceProof.encode(stream);
          break;
      }
    }

    public static ArchivalProofBody decode(XdrDataInputStream stream) throws IOException {
      ArchivalProofBody decodedArchivalProofBody = new ArchivalProofBody();
      ArchivalProofType discriminant = ArchivalProofType.decode(stream);
      decodedArchivalProofBody.setDiscriminant(discriminant);
      switch (decodedArchivalProofBody.getDiscriminant()) {
        case EXISTENCE:
          decodedArchivalProofBody.nonexistenceProof = NonexistenceProofBody.decode(stream);
          break;
        case NONEXISTENCE:
          decodedArchivalProofBody.existenceProof = ExistenceProofBody.decode(stream);
          break;
      }
      return decodedArchivalProofBody;
    }

    public static ArchivalProofBody fromXdrBase64(String xdr) throws IOException {
      byte[] bytes = Base64Factory.getInstance().decode(xdr);
      return fromXdrByteArray(bytes);
    }

    public static ArchivalProofBody fromXdrByteArray(byte[] xdr) throws IOException {
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
      XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
      return decode(xdrDataInputStream);
    }
  }
}
