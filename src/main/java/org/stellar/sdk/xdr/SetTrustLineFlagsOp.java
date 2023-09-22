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

//  struct SetTrustLineFlagsOp
//  {
//      AccountID trustor;
//      Asset asset;
//
//      uint32 clearFlags; // which flags to clear
//      uint32 setFlags;   // which flags to set
//  };

//  ===========================================================================
public class SetTrustLineFlagsOp implements XdrElement {
  public SetTrustLineFlagsOp() {}

  private AccountID trustor;

  public AccountID getTrustor() {
    return this.trustor;
  }

  public void setTrustor(AccountID value) {
    this.trustor = value;
  }

  private Asset asset;

  public Asset getAsset() {
    return this.asset;
  }

  public void setAsset(Asset value) {
    this.asset = value;
  }

  private Uint32 clearFlags;

  public Uint32 getClearFlags() {
    return this.clearFlags;
  }

  public void setClearFlags(Uint32 value) {
    this.clearFlags = value;
  }

  private Uint32 setFlags;

  public Uint32 getSetFlags() {
    return this.setFlags;
  }

  public void setSetFlags(Uint32 value) {
    this.setFlags = value;
  }

  public static void encode(
      XdrDataOutputStream stream, SetTrustLineFlagsOp encodedSetTrustLineFlagsOp)
      throws IOException {
    AccountID.encode(stream, encodedSetTrustLineFlagsOp.trustor);
    Asset.encode(stream, encodedSetTrustLineFlagsOp.asset);
    Uint32.encode(stream, encodedSetTrustLineFlagsOp.clearFlags);
    Uint32.encode(stream, encodedSetTrustLineFlagsOp.setFlags);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static SetTrustLineFlagsOp decode(XdrDataInputStream stream) throws IOException {
    SetTrustLineFlagsOp decodedSetTrustLineFlagsOp = new SetTrustLineFlagsOp();
    decodedSetTrustLineFlagsOp.trustor = AccountID.decode(stream);
    decodedSetTrustLineFlagsOp.asset = Asset.decode(stream);
    decodedSetTrustLineFlagsOp.clearFlags = Uint32.decode(stream);
    decodedSetTrustLineFlagsOp.setFlags = Uint32.decode(stream);
    return decodedSetTrustLineFlagsOp;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.trustor, this.asset, this.clearFlags, this.setFlags);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof SetTrustLineFlagsOp)) {
      return false;
    }

    SetTrustLineFlagsOp other = (SetTrustLineFlagsOp) object;
    return Objects.equals(this.trustor, other.trustor)
        && Objects.equals(this.asset, other.asset)
        && Objects.equals(this.clearFlags, other.clearFlags)
        && Objects.equals(this.setFlags, other.setFlags);
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

  public static SetTrustLineFlagsOp fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static SetTrustLineFlagsOp fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  public static final class Builder {
    private AccountID trustor;
    private Asset asset;
    private Uint32 clearFlags;
    private Uint32 setFlags;

    public Builder trustor(AccountID trustor) {
      this.trustor = trustor;
      return this;
    }

    public Builder asset(Asset asset) {
      this.asset = asset;
      return this;
    }

    public Builder clearFlags(Uint32 clearFlags) {
      this.clearFlags = clearFlags;
      return this;
    }

    public Builder setFlags(Uint32 setFlags) {
      this.setFlags = setFlags;
      return this;
    }

    public SetTrustLineFlagsOp build() {
      SetTrustLineFlagsOp val = new SetTrustLineFlagsOp();
      val.setTrustor(this.trustor);
      val.setAsset(this.asset);
      val.setClearFlags(this.clearFlags);
      val.setSetFlags(this.setFlags);
      return val;
    }
  }
}
