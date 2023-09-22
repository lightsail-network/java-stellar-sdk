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

//  struct ContractDataEntry {
//      ExtensionPoint ext;
//
//      SCAddress contract;
//      SCVal key;
//      ContractDataDurability durability;
//      SCVal val;
//  };

//  ===========================================================================
public class ContractDataEntry implements XdrElement {
  public ContractDataEntry() {}

  private ExtensionPoint ext;

  public ExtensionPoint getExt() {
    return this.ext;
  }

  public void setExt(ExtensionPoint value) {
    this.ext = value;
  }

  private SCAddress contract;

  public SCAddress getContract() {
    return this.contract;
  }

  public void setContract(SCAddress value) {
    this.contract = value;
  }

  private SCVal key;

  public SCVal getKey() {
    return this.key;
  }

  public void setKey(SCVal value) {
    this.key = value;
  }

  private ContractDataDurability durability;

  public ContractDataDurability getDurability() {
    return this.durability;
  }

  public void setDurability(ContractDataDurability value) {
    this.durability = value;
  }

  private SCVal val;

  public SCVal getVal() {
    return this.val;
  }

  public void setVal(SCVal value) {
    this.val = value;
  }

  public static void encode(XdrDataOutputStream stream, ContractDataEntry encodedContractDataEntry)
      throws IOException {
    ExtensionPoint.encode(stream, encodedContractDataEntry.ext);
    SCAddress.encode(stream, encodedContractDataEntry.contract);
    SCVal.encode(stream, encodedContractDataEntry.key);
    ContractDataDurability.encode(stream, encodedContractDataEntry.durability);
    SCVal.encode(stream, encodedContractDataEntry.val);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static ContractDataEntry decode(XdrDataInputStream stream) throws IOException {
    ContractDataEntry decodedContractDataEntry = new ContractDataEntry();
    decodedContractDataEntry.ext = ExtensionPoint.decode(stream);
    decodedContractDataEntry.contract = SCAddress.decode(stream);
    decodedContractDataEntry.key = SCVal.decode(stream);
    decodedContractDataEntry.durability = ContractDataDurability.decode(stream);
    decodedContractDataEntry.val = SCVal.decode(stream);
    return decodedContractDataEntry;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.ext, this.contract, this.key, this.durability, this.val);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof ContractDataEntry)) {
      return false;
    }

    ContractDataEntry other = (ContractDataEntry) object;
    return Objects.equals(this.ext, other.ext)
        && Objects.equals(this.contract, other.contract)
        && Objects.equals(this.key, other.key)
        && Objects.equals(this.durability, other.durability)
        && Objects.equals(this.val, other.val);
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

  public static ContractDataEntry fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static ContractDataEntry fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  public static final class Builder {
    private ExtensionPoint ext;
    private SCAddress contract;
    private SCVal key;
    private ContractDataDurability durability;
    private SCVal val;

    public Builder ext(ExtensionPoint ext) {
      this.ext = ext;
      return this;
    }

    public Builder contract(SCAddress contract) {
      this.contract = contract;
      return this;
    }

    public Builder key(SCVal key) {
      this.key = key;
      return this;
    }

    public Builder durability(ContractDataDurability durability) {
      this.durability = durability;
      return this;
    }

    public Builder val(SCVal val) {
      this.val = val;
      return this;
    }

    public ContractDataEntry build() {
      ContractDataEntry val = new ContractDataEntry();
      val.setExt(this.ext);
      val.setContract(this.contract);
      val.setKey(this.key);
      val.setDurability(this.durability);
      val.setVal(this.val);
      return val;
    }
  }
}
