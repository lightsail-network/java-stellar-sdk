// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;


import java.io.IOException;

import com.google.common.base.Objects;

// === xdr source ============================================================

//  union SetTrustLineFlagsResult switch (SetTrustLineFlagsResultCode code)
//  {
//  case SET_TRUST_LINE_FLAGS_SUCCESS:
//      void;
//  default:
//      void;
//  };

//  ===========================================================================
public class SetTrustLineFlagsResult implements XdrElement {
  public SetTrustLineFlagsResult () {}
  SetTrustLineFlagsResultCode code;
  public SetTrustLineFlagsResultCode getDiscriminant() {
    return this.code;
  }
  public void setDiscriminant(SetTrustLineFlagsResultCode value) {
    this.code = value;
  }

  public static final class Builder {
    private SetTrustLineFlagsResultCode discriminant;

    public Builder discriminant(SetTrustLineFlagsResultCode discriminant) {
      this.discriminant = discriminant;
      return this;
    }

    public SetTrustLineFlagsResult build() {
      SetTrustLineFlagsResult val = new SetTrustLineFlagsResult();
      val.setDiscriminant(discriminant);
      return val;
    }
  }

  public static void encode(XdrDataOutputStream stream, SetTrustLineFlagsResult encodedSetTrustLineFlagsResult) throws IOException {
  //Xdrgen::AST::Identifier
  //SetTrustLineFlagsResultCode
  stream.writeInt(encodedSetTrustLineFlagsResult.getDiscriminant().getValue());
  switch (encodedSetTrustLineFlagsResult.getDiscriminant()) {
  case SET_TRUST_LINE_FLAGS_SUCCESS:
  break;
  default:
  break;
  }
  }
  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }
  public static SetTrustLineFlagsResult decode(XdrDataInputStream stream) throws IOException {
  SetTrustLineFlagsResult decodedSetTrustLineFlagsResult = new SetTrustLineFlagsResult();
  SetTrustLineFlagsResultCode discriminant = SetTrustLineFlagsResultCode.decode(stream);
  decodedSetTrustLineFlagsResult.setDiscriminant(discriminant);
  switch (decodedSetTrustLineFlagsResult.getDiscriminant()) {
  case SET_TRUST_LINE_FLAGS_SUCCESS:
  break;
  default:
  break;
  }
    return decodedSetTrustLineFlagsResult;
  }
  @Override
  public int hashCode() {
    return Objects.hashCode(this.code);
  }
  @Override
  public boolean equals(Object object) {
    if (!(object instanceof SetTrustLineFlagsResult)) {
      return false;
    }

    SetTrustLineFlagsResult other = (SetTrustLineFlagsResult) object;
    return Objects.equal(this.code, other.code);
  }
}
