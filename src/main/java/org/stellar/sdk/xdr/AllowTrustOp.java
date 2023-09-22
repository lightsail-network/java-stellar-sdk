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

//  struct AllowTrustOp
//  {
//      AccountID trustor;
//      AssetCode asset;
//
//      // One of 0, AUTHORIZED_FLAG, or AUTHORIZED_TO_MAINTAIN_LIABILITIES_FLAG
//      uint32 authorize;
//  };

//  ===========================================================================
public class AllowTrustOp implements XdrElement {
  public AllowTrustOp() {}

  private AccountID trustor;

  public AccountID getTrustor() {
    return this.trustor;
  }

  public void setTrustor(AccountID value) {
    this.trustor = value;
  }

  private AssetCode asset;

  public AssetCode getAsset() {
    return this.asset;
  }

  public void setAsset(AssetCode value) {
    this.asset = value;
  }

  private Uint32 authorize;

  public Uint32 getAuthorize() {
    return this.authorize;
  }

  public void setAuthorize(Uint32 value) {
    this.authorize = value;
  }

  public static void encode(XdrDataOutputStream stream, AllowTrustOp encodedAllowTrustOp)
      throws IOException {
    AccountID.encode(stream, encodedAllowTrustOp.trustor);
    AssetCode.encode(stream, encodedAllowTrustOp.asset);
    Uint32.encode(stream, encodedAllowTrustOp.authorize);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static AllowTrustOp decode(XdrDataInputStream stream) throws IOException {
    AllowTrustOp decodedAllowTrustOp = new AllowTrustOp();
    decodedAllowTrustOp.trustor = AccountID.decode(stream);
    decodedAllowTrustOp.asset = AssetCode.decode(stream);
    decodedAllowTrustOp.authorize = Uint32.decode(stream);
    return decodedAllowTrustOp;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.trustor, this.asset, this.authorize);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof AllowTrustOp)) {
      return false;
    }

    AllowTrustOp other = (AllowTrustOp) object;
    return Objects.equals(this.trustor, other.trustor)
        && Objects.equals(this.asset, other.asset)
        && Objects.equals(this.authorize, other.authorize);
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

  public static AllowTrustOp fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static AllowTrustOp fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  public static final class Builder {
    private AccountID trustor;
    private AssetCode asset;
    private Uint32 authorize;

    public Builder trustor(AccountID trustor) {
      this.trustor = trustor;
      return this;
    }

    public Builder asset(AssetCode asset) {
      this.asset = asset;
      return this;
    }

    public Builder authorize(Uint32 authorize) {
      this.authorize = authorize;
      return this;
    }

    public AllowTrustOp build() {
      AllowTrustOp val = new AllowTrustOp();
      val.setTrustor(this.trustor);
      val.setAsset(this.asset);
      val.setAuthorize(this.authorize);
      return val;
    }
  }
}
