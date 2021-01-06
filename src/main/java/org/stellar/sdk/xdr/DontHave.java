// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;


import java.io.IOException;

import com.google.common.base.Objects;

// === xdr source ============================================================

//  struct DontHave
//  {
//      MessageType type;
//      uint256 reqHash;
//  };

//  ===========================================================================
public class DontHave implements XdrElement {
  public DontHave () {}
  private MessageType type;
  public MessageType getType() {
    return this.type;
  }
  public void setType(MessageType value) {
    this.type = value;
  }
  private Uint256 reqHash;
  public Uint256 getReqHash() {
    return this.reqHash;
  }
  public void setReqHash(Uint256 value) {
    this.reqHash = value;
  }
  public static void encode(XdrDataOutputStream stream, DontHave encodedDontHave) throws IOException{
    MessageType.encode(stream, encodedDontHave.type);
    Uint256.encode(stream, encodedDontHave.reqHash);
  }
  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }
  public static DontHave decode(XdrDataInputStream stream) throws IOException {
    DontHave decodedDontHave = new DontHave();
    decodedDontHave.type = MessageType.decode(stream);
    decodedDontHave.reqHash = Uint256.decode(stream);
    return decodedDontHave;
  }
  @Override
  public int hashCode() {
    return Objects.hashCode(this.type, this.reqHash);
  }
  @Override
  public boolean equals(Object object) {
    if (!(object instanceof DontHave)) {
      return false;
    }

    DontHave other = (DontHave) object;
    return Objects.equal(this.type, other.type) && Objects.equal(this.reqHash, other.reqHash);
  }

  public static final class Builder {
    private MessageType type;
    private Uint256 reqHash;

    public Builder type(MessageType type) {
      this.type = type;
      return this;
    }

    public Builder reqHash(Uint256 reqHash) {
      this.reqHash = reqHash;
      return this;
    }

    public DontHave build() {
      DontHave val = new DontHave();
      val.setType(type);
      val.setReqHash(reqHash);
      return val;
    }
  }
}
