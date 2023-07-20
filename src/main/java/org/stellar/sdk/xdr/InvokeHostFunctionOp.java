// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import com.google.common.base.Objects;
import java.io.IOException;
import java.util.Arrays;

// === xdr source ============================================================

//  struct InvokeHostFunctionOp
//  {
//      // Host function to invoke.
//      HostFunction hostFunction;
//      // Per-address authorizations for this host function.
//      SorobanAuthorizationEntry auth<>;
//  };

//  ===========================================================================
public class InvokeHostFunctionOp implements XdrElement {
  public InvokeHostFunctionOp() {}

  private HostFunction hostFunction;

  public HostFunction getHostFunction() {
    return this.hostFunction;
  }

  public void setHostFunction(HostFunction value) {
    this.hostFunction = value;
  }

  private SorobanAuthorizationEntry[] auth;

  public SorobanAuthorizationEntry[] getAuth() {
    return this.auth;
  }

  public void setAuth(SorobanAuthorizationEntry[] value) {
    this.auth = value;
  }

  public static void encode(
      XdrDataOutputStream stream, InvokeHostFunctionOp encodedInvokeHostFunctionOp)
      throws IOException {
    HostFunction.encode(stream, encodedInvokeHostFunctionOp.hostFunction);
    int authsize = encodedInvokeHostFunctionOp.getAuth().length;
    stream.writeInt(authsize);
    for (int i = 0; i < authsize; i++) {
      SorobanAuthorizationEntry.encode(stream, encodedInvokeHostFunctionOp.auth[i]);
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static InvokeHostFunctionOp decode(XdrDataInputStream stream) throws IOException {
    InvokeHostFunctionOp decodedInvokeHostFunctionOp = new InvokeHostFunctionOp();
    decodedInvokeHostFunctionOp.hostFunction = HostFunction.decode(stream);
    int authsize = stream.readInt();
    decodedInvokeHostFunctionOp.auth = new SorobanAuthorizationEntry[authsize];
    for (int i = 0; i < authsize; i++) {
      decodedInvokeHostFunctionOp.auth[i] = SorobanAuthorizationEntry.decode(stream);
    }
    return decodedInvokeHostFunctionOp;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.hostFunction, Arrays.hashCode(this.auth));
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof InvokeHostFunctionOp)) {
      return false;
    }

    InvokeHostFunctionOp other = (InvokeHostFunctionOp) object;
    return Objects.equal(this.hostFunction, other.hostFunction)
        && Arrays.equals(this.auth, other.auth);
  }

  public static final class Builder {
    private HostFunction hostFunction;
    private SorobanAuthorizationEntry[] auth;

    public Builder hostFunction(HostFunction hostFunction) {
      this.hostFunction = hostFunction;
      return this;
    }

    public Builder auth(SorobanAuthorizationEntry[] auth) {
      this.auth = auth;
      return this;
    }

    public InvokeHostFunctionOp build() {
      InvokeHostFunctionOp val = new InvokeHostFunctionOp();
      val.setHostFunction(this.hostFunction);
      val.setAuth(this.auth);
      return val;
    }
  }
}