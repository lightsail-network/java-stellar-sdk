// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;


import java.io.IOException;

import com.google.common.base.Objects;

// === xdr source ============================================================

//  struct CreateAccountOp
//  {
//      AccountID destination; // account to create
//      int64 startingBalance; // amount they end up with
//  };

//  ===========================================================================
public class CreateAccountOp implements XdrElement {
  public CreateAccountOp () {}
  private AccountID destination;
  public AccountID getDestination() {
    return this.destination;
  }
  public void setDestination(AccountID value) {
    this.destination = value;
  }
  private Int64 startingBalance;
  public Int64 getStartingBalance() {
    return this.startingBalance;
  }
  public void setStartingBalance(Int64 value) {
    this.startingBalance = value;
  }
  public static void encode(XdrDataOutputStream stream, CreateAccountOp encodedCreateAccountOp) throws IOException{
    AccountID.encode(stream, encodedCreateAccountOp.destination);
    Int64.encode(stream, encodedCreateAccountOp.startingBalance);
  }
  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }
  public static CreateAccountOp decode(XdrDataInputStream stream) throws IOException {
    CreateAccountOp decodedCreateAccountOp = new CreateAccountOp();
    decodedCreateAccountOp.destination = AccountID.decode(stream);
    decodedCreateAccountOp.startingBalance = Int64.decode(stream);
    return decodedCreateAccountOp;
  }
  @Override
  public int hashCode() {
    return Objects.hashCode(this.destination, this.startingBalance);
  }
  @Override
  public boolean equals(Object object) {
    if (object == null || !(object instanceof CreateAccountOp)) {
      return false;
    }

    CreateAccountOp other = (CreateAccountOp) object;
    return Objects.equal(this.destination, other.destination) && Objects.equal(this.startingBalance, other.startingBalance);
  }
}
