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
 * InvokeHostFunctionOp's original definition in the XDR file is:
 *
 * <pre>
 * struct InvokeHostFunctionOp
 * {
 *     // Host function to invoke.
 *     HostFunction hostFunction;
 *     // Per-address authorizations for this host function.
 *     SorobanAuthorizationEntry auth&lt;&gt;;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class InvokeHostFunctionOp implements XdrElement {
  private HostFunction hostFunction;
  private SorobanAuthorizationEntry[] auth;

  public void encode(XdrDataOutputStream stream) throws IOException {
    hostFunction.encode(stream);
    int authSize = getAuth().length;
    stream.writeInt(authSize);
    for (int i = 0; i < authSize; i++) {
      auth[i].encode(stream);
    }
  }

  public static InvokeHostFunctionOp decode(XdrDataInputStream stream) throws IOException {
    InvokeHostFunctionOp decodedInvokeHostFunctionOp = new InvokeHostFunctionOp();
    decodedInvokeHostFunctionOp.hostFunction = HostFunction.decode(stream);
    int authSize = stream.readInt();
    decodedInvokeHostFunctionOp.auth = new SorobanAuthorizationEntry[authSize];
    for (int i = 0; i < authSize; i++) {
      decodedInvokeHostFunctionOp.auth[i] = SorobanAuthorizationEntry.decode(stream);
    }
    return decodedInvokeHostFunctionOp;
  }

  public static InvokeHostFunctionOp fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static InvokeHostFunctionOp fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
