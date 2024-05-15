// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import org.stellar.sdk.Base64Factory;

/**
 * AuthCert's original definition in the XDR file is:
 *
 * <pre>
 * struct AuthCert
 * {
 *     Curve25519Public pubkey;
 *     uint64 expiration;
 *     Signature sig;
 * };
 * </pre>
 */
public class AuthCert implements XdrElement {
  public AuthCert() {}

  private Curve25519Public pubkey;

  public Curve25519Public getPubkey() {
    return this.pubkey;
  }

  public void setPubkey(Curve25519Public value) {
    this.pubkey = value;
  }

  private Uint64 expiration;

  public Uint64 getExpiration() {
    return this.expiration;
  }

  public void setExpiration(Uint64 value) {
    this.expiration = value;
  }

  private Signature sig;

  public Signature getSig() {
    return this.sig;
  }

  public void setSig(Signature value) {
    this.sig = value;
  }

  public static void encode(XdrDataOutputStream stream, AuthCert encodedAuthCert)
      throws IOException {
    Curve25519Public.encode(stream, encodedAuthCert.pubkey);
    Uint64.encode(stream, encodedAuthCert.expiration);
    Signature.encode(stream, encodedAuthCert.sig);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static AuthCert decode(XdrDataInputStream stream) throws IOException {
    AuthCert decodedAuthCert = new AuthCert();
    decodedAuthCert.pubkey = Curve25519Public.decode(stream);
    decodedAuthCert.expiration = Uint64.decode(stream);
    decodedAuthCert.sig = Signature.decode(stream);
    return decodedAuthCert;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.pubkey, this.expiration, this.sig);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof AuthCert)) {
      return false;
    }

    AuthCert other = (AuthCert) object;
    return Objects.equals(this.pubkey, other.pubkey)
        && Objects.equals(this.expiration, other.expiration)
        && Objects.equals(this.sig, other.sig);
  }

  @Override
  public String toXdrBase64() throws IOException {
    return Base64Factory.getInstance().encodeToString(toXdrByteArray());
  }

  @Override
  public byte[] toXdrByteArray() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    encode(xdrDataOutputStream);
    return byteArrayOutputStream.toByteArray();
  }

  public static AuthCert fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static AuthCert fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  public static final class Builder {
    private Curve25519Public pubkey;
    private Uint64 expiration;
    private Signature sig;

    public Builder pubkey(Curve25519Public pubkey) {
      this.pubkey = pubkey;
      return this;
    }

    public Builder expiration(Uint64 expiration) {
      this.expiration = expiration;
      return this;
    }

    public Builder sig(Signature sig) {
      this.sig = sig;
      return this;
    }

    public AuthCert build() {
      AuthCert val = new AuthCert();
      val.setPubkey(this.pubkey);
      val.setExpiration(this.expiration);
      val.setSig(this.sig);
      return val;
    }
  }
}
