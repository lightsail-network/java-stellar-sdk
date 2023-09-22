// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import org.stellar.sdk.Base64;

// === xdr source ============================================================

//  union SorobanAuthorizedFunction switch (SorobanAuthorizedFunctionType type)
//  {
//  case SOROBAN_AUTHORIZED_FUNCTION_TYPE_CONTRACT_FN:
//      InvokeContractArgs contractFn;
//  case SOROBAN_AUTHORIZED_FUNCTION_TYPE_CREATE_CONTRACT_HOST_FN:
//      CreateContractArgs createContractHostFn;
//  };

//  ===========================================================================
public class SorobanAuthorizedFunction implements XdrElement {
  public SorobanAuthorizedFunction() {}

  SorobanAuthorizedFunctionType type;

  public SorobanAuthorizedFunctionType getDiscriminant() {
    return this.type;
  }

  public void setDiscriminant(SorobanAuthorizedFunctionType value) {
    this.type = value;
  }

  private InvokeContractArgs contractFn;

  public InvokeContractArgs getContractFn() {
    return this.contractFn;
  }

  public void setContractFn(InvokeContractArgs value) {
    this.contractFn = value;
  }

  private CreateContractArgs createContractHostFn;

  public CreateContractArgs getCreateContractHostFn() {
    return this.createContractHostFn;
  }

  public void setCreateContractHostFn(CreateContractArgs value) {
    this.createContractHostFn = value;
  }

  public static final class Builder {
    private SorobanAuthorizedFunctionType discriminant;
    private InvokeContractArgs contractFn;
    private CreateContractArgs createContractHostFn;

    public Builder discriminant(SorobanAuthorizedFunctionType discriminant) {
      this.discriminant = discriminant;
      return this;
    }

    public Builder contractFn(InvokeContractArgs contractFn) {
      this.contractFn = contractFn;
      return this;
    }

    public Builder createContractHostFn(CreateContractArgs createContractHostFn) {
      this.createContractHostFn = createContractHostFn;
      return this;
    }

    public SorobanAuthorizedFunction build() {
      SorobanAuthorizedFunction val = new SorobanAuthorizedFunction();
      val.setDiscriminant(discriminant);
      val.setContractFn(this.contractFn);
      val.setCreateContractHostFn(this.createContractHostFn);
      return val;
    }
  }

  public static void encode(
      XdrDataOutputStream stream, SorobanAuthorizedFunction encodedSorobanAuthorizedFunction)
      throws IOException {
    // Xdrgen::AST::Identifier
    // SorobanAuthorizedFunctionType
    stream.writeInt(encodedSorobanAuthorizedFunction.getDiscriminant().getValue());
    switch (encodedSorobanAuthorizedFunction.getDiscriminant()) {
      case SOROBAN_AUTHORIZED_FUNCTION_TYPE_CONTRACT_FN:
        InvokeContractArgs.encode(stream, encodedSorobanAuthorizedFunction.contractFn);
        break;
      case SOROBAN_AUTHORIZED_FUNCTION_TYPE_CREATE_CONTRACT_HOST_FN:
        CreateContractArgs.encode(stream, encodedSorobanAuthorizedFunction.createContractHostFn);
        break;
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static SorobanAuthorizedFunction decode(XdrDataInputStream stream) throws IOException {
    SorobanAuthorizedFunction decodedSorobanAuthorizedFunction = new SorobanAuthorizedFunction();
    SorobanAuthorizedFunctionType discriminant = SorobanAuthorizedFunctionType.decode(stream);
    decodedSorobanAuthorizedFunction.setDiscriminant(discriminant);
    switch (decodedSorobanAuthorizedFunction.getDiscriminant()) {
      case SOROBAN_AUTHORIZED_FUNCTION_TYPE_CONTRACT_FN:
        decodedSorobanAuthorizedFunction.contractFn = InvokeContractArgs.decode(stream);
        break;
      case SOROBAN_AUTHORIZED_FUNCTION_TYPE_CREATE_CONTRACT_HOST_FN:
        decodedSorobanAuthorizedFunction.createContractHostFn = CreateContractArgs.decode(stream);
        break;
    }
    return decodedSorobanAuthorizedFunction;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.contractFn, this.createContractHostFn, this.type);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof SorobanAuthorizedFunction)) {
      return false;
    }

    SorobanAuthorizedFunction other = (SorobanAuthorizedFunction) object;
    return Objects.equals(this.contractFn, other.contractFn)
        && Objects.equals(this.createContractHostFn, other.createContractHostFn)
        && Objects.equals(this.type, other.type);
  }

  @Override
  public String toXdrBase64() throws IOException {
    return Base64.encodeToString(toXdrByteArray());
  }

  @Override
  public byte[] toXdrByteArray() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    encode(xdrDataOutputStream);
    return byteArrayOutputStream.toByteArray();
  }

  public static SorobanAuthorizedFunction fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SorobanAuthorizedFunction fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
