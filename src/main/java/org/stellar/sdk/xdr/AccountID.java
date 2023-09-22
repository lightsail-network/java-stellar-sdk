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

//  typedef PublicKey AccountID;

//  ===========================================================================
public class AccountID implements XdrElement {
  private PublicKey AccountID;

  public AccountID() {}

  public AccountID(PublicKey AccountID) {
    this.AccountID = AccountID;
  }

  public PublicKey getAccountID() {
    return this.AccountID;
  }

  public void setAccountID(PublicKey value) {
    this.AccountID = value;
  }

  public static void encode(XdrDataOutputStream stream, AccountID encodedAccountID)
      throws IOException {
    PublicKey.encode(stream, encodedAccountID.AccountID);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static AccountID decode(XdrDataInputStream stream) throws IOException {
    AccountID decodedAccountID = new AccountID();
    decodedAccountID.AccountID = PublicKey.decode(stream);
    return decodedAccountID;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.AccountID);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof AccountID)) {
      return false;
    }

    AccountID other = (AccountID) object;
    return Objects.equals(this.AccountID, other.AccountID);
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

  public static AccountID fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static AccountID fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
