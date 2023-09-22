// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import org.stellar.sdk.Base64;

// === xdr source ============================================================

//  struct ContractCodeEntry {
//      ExtensionPoint ext;
//
//      Hash hash;
//      opaque code<>;
//  };

//  ===========================================================================
public class ContractCodeEntry implements XdrElement {
  public ContractCodeEntry() {}

  private ExtensionPoint ext;

  public ExtensionPoint getExt() {
    return this.ext;
  }

  public void setExt(ExtensionPoint value) {
    this.ext = value;
  }

  private Hash hash;

  public Hash getHash() {
    return this.hash;
  }

  public void setHash(Hash value) {
    this.hash = value;
  }

  private byte[] code;

  public byte[] getCode() {
    return this.code;
  }

  public void setCode(byte[] value) {
    this.code = value;
  }

  public static void encode(XdrDataOutputStream stream, ContractCodeEntry encodedContractCodeEntry)
      throws IOException {
    ExtensionPoint.encode(stream, encodedContractCodeEntry.ext);
    Hash.encode(stream, encodedContractCodeEntry.hash);
    int codesize = encodedContractCodeEntry.code.length;
    stream.writeInt(codesize);
    stream.write(encodedContractCodeEntry.getCode(), 0, codesize);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static ContractCodeEntry decode(XdrDataInputStream stream) throws IOException {
    ContractCodeEntry decodedContractCodeEntry = new ContractCodeEntry();
    decodedContractCodeEntry.ext = ExtensionPoint.decode(stream);
    decodedContractCodeEntry.hash = Hash.decode(stream);
    int codesize = stream.readInt();
    decodedContractCodeEntry.code = new byte[codesize];
    stream.read(decodedContractCodeEntry.code, 0, codesize);
    return decodedContractCodeEntry;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.ext, this.hash, Arrays.hashCode(this.code));
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof ContractCodeEntry)) {
      return false;
    }

    ContractCodeEntry other = (ContractCodeEntry) object;
    return Objects.equals(this.ext, other.ext)
        && Objects.equals(this.hash, other.hash)
        && Arrays.equals(this.code, other.code);
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

  public static ContractCodeEntry fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static ContractCodeEntry fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  public static final class Builder {
    private ExtensionPoint ext;
    private Hash hash;
    private byte[] code;

    public Builder ext(ExtensionPoint ext) {
      this.ext = ext;
      return this;
    }

    public Builder hash(Hash hash) {
      this.hash = hash;
      return this;
    }

    public Builder code(byte[] code) {
      this.code = code;
      return this;
    }

    public ContractCodeEntry build() {
      ContractCodeEntry val = new ContractCodeEntry();
      val.setExt(this.ext);
      val.setHash(this.hash);
      val.setCode(this.code);
      return val;
    }
  }
}
