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

//  struct InvokeContractArgs {
//      SCAddress contractAddress;
//      SCSymbol functionName;
//      SCVal args<>;
//  };

//  ===========================================================================
public class InvokeContractArgs implements XdrElement {
  public InvokeContractArgs() {}

  private SCAddress contractAddress;

  public SCAddress getContractAddress() {
    return this.contractAddress;
  }

  public void setContractAddress(SCAddress value) {
    this.contractAddress = value;
  }

  private SCSymbol functionName;

  public SCSymbol getFunctionName() {
    return this.functionName;
  }

  public void setFunctionName(SCSymbol value) {
    this.functionName = value;
  }

  private SCVal[] args;

  public SCVal[] getArgs() {
    return this.args;
  }

  public void setArgs(SCVal[] value) {
    this.args = value;
  }

  public static void encode(
      XdrDataOutputStream stream, InvokeContractArgs encodedInvokeContractArgs) throws IOException {
    SCAddress.encode(stream, encodedInvokeContractArgs.contractAddress);
    SCSymbol.encode(stream, encodedInvokeContractArgs.functionName);
    int argssize = encodedInvokeContractArgs.getArgs().length;
    stream.writeInt(argssize);
    for (int i = 0; i < argssize; i++) {
      SCVal.encode(stream, encodedInvokeContractArgs.args[i]);
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static InvokeContractArgs decode(XdrDataInputStream stream) throws IOException {
    InvokeContractArgs decodedInvokeContractArgs = new InvokeContractArgs();
    decodedInvokeContractArgs.contractAddress = SCAddress.decode(stream);
    decodedInvokeContractArgs.functionName = SCSymbol.decode(stream);
    int argssize = stream.readInt();
    decodedInvokeContractArgs.args = new SCVal[argssize];
    for (int i = 0; i < argssize; i++) {
      decodedInvokeContractArgs.args[i] = SCVal.decode(stream);
    }
    return decodedInvokeContractArgs;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.contractAddress, this.functionName, Arrays.hashCode(this.args));
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof InvokeContractArgs)) {
      return false;
    }

    InvokeContractArgs other = (InvokeContractArgs) object;
    return Objects.equals(this.contractAddress, other.contractAddress)
        && Objects.equals(this.functionName, other.functionName)
        && Arrays.equals(this.args, other.args);
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

  public static InvokeContractArgs fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.getDecoder().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static InvokeContractArgs fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  public static final class Builder {
    private SCAddress contractAddress;
    private SCSymbol functionName;
    private SCVal[] args;

    public Builder contractAddress(SCAddress contractAddress) {
      this.contractAddress = contractAddress;
      return this;
    }

    public Builder functionName(SCSymbol functionName) {
      this.functionName = functionName;
      return this;
    }

    public Builder args(SCVal[] args) {
      this.args = args;
      return this;
    }

    public InvokeContractArgs build() {
      InvokeContractArgs val = new InvokeContractArgs();
      val.setContractAddress(this.contractAddress);
      val.setFunctionName(this.functionName);
      val.setArgs(this.args);
      return val;
    }
  }
}
