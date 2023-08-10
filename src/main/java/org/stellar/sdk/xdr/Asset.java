// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import com.google.common.base.Objects;
import com.google.common.io.BaseEncoding;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

// === xdr source ============================================================

//  union Asset switch (AssetType type)
//  {
//  case ASSET_TYPE_NATIVE: // Not credit
//      void;
//
//  case ASSET_TYPE_CREDIT_ALPHANUM4:
//      AlphaNum4 alphaNum4;
//
//  case ASSET_TYPE_CREDIT_ALPHANUM12:
//      AlphaNum12 alphaNum12;
//
//      // add other asset types here in the future
//  };

//  ===========================================================================
public class Asset implements XdrElement {
  public Asset() {}

  AssetType type;

  public AssetType getDiscriminant() {
    return this.type;
  }

  public void setDiscriminant(AssetType value) {
    this.type = value;
  }

  private AlphaNum4 alphaNum4;

  public AlphaNum4 getAlphaNum4() {
    return this.alphaNum4;
  }

  public void setAlphaNum4(AlphaNum4 value) {
    this.alphaNum4 = value;
  }

  private AlphaNum12 alphaNum12;

  public AlphaNum12 getAlphaNum12() {
    return this.alphaNum12;
  }

  public void setAlphaNum12(AlphaNum12 value) {
    this.alphaNum12 = value;
  }

  public static final class Builder {
    private AssetType discriminant;
    private AlphaNum4 alphaNum4;
    private AlphaNum12 alphaNum12;

    public Builder discriminant(AssetType discriminant) {
      this.discriminant = discriminant;
      return this;
    }

    public Builder alphaNum4(AlphaNum4 alphaNum4) {
      this.alphaNum4 = alphaNum4;
      return this;
    }

    public Builder alphaNum12(AlphaNum12 alphaNum12) {
      this.alphaNum12 = alphaNum12;
      return this;
    }

    public Asset build() {
      Asset val = new Asset();
      val.setDiscriminant(discriminant);
      val.setAlphaNum4(this.alphaNum4);
      val.setAlphaNum12(this.alphaNum12);
      return val;
    }
  }

  public static void encode(XdrDataOutputStream stream, Asset encodedAsset) throws IOException {
    // Xdrgen::AST::Identifier
    // AssetType
    stream.writeInt(encodedAsset.getDiscriminant().getValue());
    switch (encodedAsset.getDiscriminant()) {
      case ASSET_TYPE_NATIVE:
        break;
      case ASSET_TYPE_CREDIT_ALPHANUM4:
        AlphaNum4.encode(stream, encodedAsset.alphaNum4);
        break;
      case ASSET_TYPE_CREDIT_ALPHANUM12:
        AlphaNum12.encode(stream, encodedAsset.alphaNum12);
        break;
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static Asset decode(XdrDataInputStream stream) throws IOException {
    Asset decodedAsset = new Asset();
    AssetType discriminant = AssetType.decode(stream);
    decodedAsset.setDiscriminant(discriminant);
    switch (decodedAsset.getDiscriminant()) {
      case ASSET_TYPE_NATIVE:
        break;
      case ASSET_TYPE_CREDIT_ALPHANUM4:
        decodedAsset.alphaNum4 = AlphaNum4.decode(stream);
        break;
      case ASSET_TYPE_CREDIT_ALPHANUM12:
        decodedAsset.alphaNum12 = AlphaNum12.decode(stream);
        break;
    }
    return decodedAsset;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.alphaNum4, this.alphaNum12, this.type);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Asset)) {
      return false;
    }

    Asset other = (Asset) object;
    return Objects.equal(this.alphaNum4, other.alphaNum4)
        && Objects.equal(this.alphaNum12, other.alphaNum12)
        && Objects.equal(this.type, other.type);
  }

  @Override
  public String toXdrBase64() throws IOException {
    BaseEncoding base64Encoding = BaseEncoding.base64();
    return base64Encoding.encode(toXdrByteArray());
  }

  @Override
  public byte[] toXdrByteArray() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    encode(xdrDataOutputStream);
    return byteArrayOutputStream.toByteArray();
  }

  public static Asset fromXdrBase64(String xdr) throws IOException {
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] bytes = base64Encoding.decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static Asset fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
