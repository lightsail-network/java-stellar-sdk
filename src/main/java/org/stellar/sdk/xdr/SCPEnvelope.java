// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;


import java.io.IOException;

import com.google.common.base.Objects;

// === xdr source ============================================================

//  struct SCPEnvelope
//  {
//      SCPStatement statement;
//      Signature signature;
//  };

//  ===========================================================================
public class SCPEnvelope implements XdrElement {
  public SCPEnvelope () {}
  private SCPStatement statement;
  public SCPStatement getStatement() {
    return this.statement;
  }
  public void setStatement(SCPStatement value) {
    this.statement = value;
  }
  private Signature signature;
  public Signature getSignature() {
    return this.signature;
  }
  public void setSignature(Signature value) {
    this.signature = value;
  }
  public static void encode(XdrDataOutputStream stream, SCPEnvelope encodedSCPEnvelope) throws IOException{
    SCPStatement.encode(stream, encodedSCPEnvelope.statement);
    Signature.encode(stream, encodedSCPEnvelope.signature);
  }
  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }
  public static SCPEnvelope decode(XdrDataInputStream stream) throws IOException {
    SCPEnvelope decodedSCPEnvelope = new SCPEnvelope();
    decodedSCPEnvelope.statement = SCPStatement.decode(stream);
    decodedSCPEnvelope.signature = Signature.decode(stream);
    return decodedSCPEnvelope;
  }
  @Override
  public int hashCode() {
    return Objects.hashCode(this.statement, this.signature);
  }
  @Override
  public boolean equals(Object object) {
    if (!(object instanceof SCPEnvelope)) {
      return false;
    }

    SCPEnvelope other = (SCPEnvelope) object;
    return Objects.equal(this.statement, other.statement) && Objects.equal(this.signature, other.signature);
  }

  public static final class Builder {
    private SCPStatement statement;
    private Signature signature;

    public Builder statement(SCPStatement statement) {
      this.statement = statement;
      return this;
    }

    public Builder signature(Signature signature) {
      this.signature = signature;
      return this;
    }

    public SCPEnvelope build() {
      SCPEnvelope val = new SCPEnvelope();
      val.setStatement(statement);
      val.setSignature(signature);
      return val;
    }
  }
}
