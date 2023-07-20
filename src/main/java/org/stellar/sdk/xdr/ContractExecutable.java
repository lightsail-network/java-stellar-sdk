// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import com.google.common.base.Objects;
import java.io.IOException;

// === xdr source ============================================================

//  union ContractExecutable switch (ContractExecutableType type)
//  {
//  case CONTRACT_EXECUTABLE_WASM:
//      Hash wasm_hash;
//  case CONTRACT_EXECUTABLE_TOKEN:
//      void;
//  };

//  ===========================================================================
public class ContractExecutable implements XdrElement {
  public ContractExecutable() {}

  ContractExecutableType type;

  public ContractExecutableType getDiscriminant() {
    return this.type;
  }

  public void setDiscriminant(ContractExecutableType value) {
    this.type = value;
  }

  private Hash wasm_hash;

  public Hash getWasm_hash() {
    return this.wasm_hash;
  }

  public void setWasm_hash(Hash value) {
    this.wasm_hash = value;
  }

  public static final class Builder {
    private ContractExecutableType discriminant;
    private Hash wasm_hash;

    public Builder discriminant(ContractExecutableType discriminant) {
      this.discriminant = discriminant;
      return this;
    }

    public Builder wasm_hash(Hash wasm_hash) {
      this.wasm_hash = wasm_hash;
      return this;
    }

    public ContractExecutable build() {
      ContractExecutable val = new ContractExecutable();
      val.setDiscriminant(discriminant);
      val.setWasm_hash(this.wasm_hash);
      return val;
    }
  }

  public static void encode(
      XdrDataOutputStream stream, ContractExecutable encodedContractExecutable) throws IOException {
    // Xdrgen::AST::Identifier
    // ContractExecutableType
    stream.writeInt(encodedContractExecutable.getDiscriminant().getValue());
    switch (encodedContractExecutable.getDiscriminant()) {
      case CONTRACT_EXECUTABLE_WASM:
        Hash.encode(stream, encodedContractExecutable.wasm_hash);
        break;
      case CONTRACT_EXECUTABLE_TOKEN:
        break;
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static ContractExecutable decode(XdrDataInputStream stream) throws IOException {
    ContractExecutable decodedContractExecutable = new ContractExecutable();
    ContractExecutableType discriminant = ContractExecutableType.decode(stream);
    decodedContractExecutable.setDiscriminant(discriminant);
    switch (decodedContractExecutable.getDiscriminant()) {
      case CONTRACT_EXECUTABLE_WASM:
        decodedContractExecutable.wasm_hash = Hash.decode(stream);
        break;
      case CONTRACT_EXECUTABLE_TOKEN:
        break;
    }
    return decodedContractExecutable;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.wasm_hash, this.type);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof ContractExecutable)) {
      return false;
    }

    ContractExecutable other = (ContractExecutable) object;
    return Objects.equal(this.wasm_hash, other.wasm_hash) && Objects.equal(this.type, other.type);
  }
}