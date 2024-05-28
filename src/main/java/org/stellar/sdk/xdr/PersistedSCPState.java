// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.stellar.sdk.Base64Factory;

/**
 * PersistedSCPState's original definition in the XDR file is:
 *
 * <pre>
 * union PersistedSCPState switch (int v)
 * {
 * case 0:
 * 	PersistedSCPStateV0 v0;
 * case 1:
 * 	PersistedSCPStateV1 v1;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PersistedSCPState implements XdrElement {
  private Integer discriminant;
  private PersistedSCPStateV0 v0;
  private PersistedSCPStateV1 v1;

  public static void encode(XdrDataOutputStream stream, PersistedSCPState encodedPersistedSCPState)
      throws IOException {
    // Xdrgen::AST::Typespecs::Int
    // Integer
    stream.writeInt(encodedPersistedSCPState.getDiscriminant().intValue());
    switch (encodedPersistedSCPState.getDiscriminant()) {
      case 0:
        PersistedSCPStateV0.encode(stream, encodedPersistedSCPState.v0);
        break;
      case 1:
        PersistedSCPStateV1.encode(stream, encodedPersistedSCPState.v1);
        break;
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static PersistedSCPState decode(XdrDataInputStream stream) throws IOException {
    PersistedSCPState decodedPersistedSCPState = new PersistedSCPState();
    Integer discriminant = stream.readInt();
    decodedPersistedSCPState.setDiscriminant(discriminant);
    switch (decodedPersistedSCPState.getDiscriminant()) {
      case 0:
        decodedPersistedSCPState.v0 = PersistedSCPStateV0.decode(stream);
        break;
      case 1:
        decodedPersistedSCPState.v1 = PersistedSCPStateV1.decode(stream);
        break;
    }
    return decodedPersistedSCPState;
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

  public static PersistedSCPState fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static PersistedSCPState fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
