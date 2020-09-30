// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;


import java.io.IOException;

import com.google.common.base.Objects;

// === xdr source ============================================================

//  union OperationID switch (EnvelopeType type)
//  {
//  case ENVELOPE_TYPE_OP_ID:
//      struct
//      {
//          MuxedAccount sourceAccount;
//          SequenceNumber seqNum;
//          uint32 opNum;
//      } id;
//  };

//  ===========================================================================
public class OperationID implements XdrElement {
  public OperationID () {}
  EnvelopeType type;
  public EnvelopeType getDiscriminant() {
    return this.type;
  }
  public void setDiscriminant(EnvelopeType value) {
    this.type = value;
  }
  private OperationIDId id;
  public OperationIDId getId() {
    return this.id;
  }
  public void setId(OperationIDId value) {
    this.id = value;
  }
  public static void encode(XdrDataOutputStream stream, OperationID encodedOperationID) throws IOException {
  //Xdrgen::AST::Identifier
  //EnvelopeType
  stream.writeInt(encodedOperationID.getDiscriminant().getValue());
  switch (encodedOperationID.getDiscriminant()) {
  case ENVELOPE_TYPE_OP_ID:
  OperationIDId.encode(stream, encodedOperationID.id);
  break;
  }
  }
  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }
  public static OperationID decode(XdrDataInputStream stream) throws IOException {
  OperationID decodedOperationID = new OperationID();
  EnvelopeType discriminant = EnvelopeType.decode(stream);
  decodedOperationID.setDiscriminant(discriminant);
  switch (decodedOperationID.getDiscriminant()) {
  case ENVELOPE_TYPE_OP_ID:
  decodedOperationID.id = OperationIDId.decode(stream);
  break;
  }
    return decodedOperationID;
  }
  @Override
  public int hashCode() {
    return Objects.hashCode(this.id, this.type);
  }
  @Override
  public boolean equals(Object object) {
    if (object == null || !(object instanceof OperationID)) {
      return false;
    }

    OperationID other = (OperationID) object;
    return Objects.equal(this.id, other.id) && Objects.equal(this.type, other.type);
  }

  public static class OperationIDId {
    public OperationIDId () {}
    private MuxedAccount sourceAccount;
    public MuxedAccount getSourceAccount() {
      return this.sourceAccount;
    }
    public void setSourceAccount(MuxedAccount value) {
      this.sourceAccount = value;
    }
    private SequenceNumber seqNum;
    public SequenceNumber getSeqNum() {
      return this.seqNum;
    }
    public void setSeqNum(SequenceNumber value) {
      this.seqNum = value;
    }
    private Uint32 opNum;
    public Uint32 getOpNum() {
      return this.opNum;
    }
    public void setOpNum(Uint32 value) {
      this.opNum = value;
    }
    public static void encode(XdrDataOutputStream stream, OperationIDId encodedOperationIDId) throws IOException{
      MuxedAccount.encode(stream, encodedOperationIDId.sourceAccount);
      SequenceNumber.encode(stream, encodedOperationIDId.seqNum);
      Uint32.encode(stream, encodedOperationIDId.opNum);
    }
    public void encode(XdrDataOutputStream stream) throws IOException {
      encode(stream, this);
    }
    public static OperationIDId decode(XdrDataInputStream stream) throws IOException {
      OperationIDId decodedOperationIDId = new OperationIDId();
      decodedOperationIDId.sourceAccount = MuxedAccount.decode(stream);
      decodedOperationIDId.seqNum = SequenceNumber.decode(stream);
      decodedOperationIDId.opNum = Uint32.decode(stream);
      return decodedOperationIDId;
    }
    @Override
    public int hashCode() {
      return Objects.hashCode(this.sourceAccount, this.seqNum, this.opNum);
    }
    @Override
    public boolean equals(Object object) {
      if (object == null || !(object instanceof OperationIDId)) {
        return false;
      }

      OperationIDId other = (OperationIDId) object;
      return Objects.equal(this.sourceAccount, other.sourceAccount) && Objects.equal(this.seqNum, other.seqNum) && Objects.equal(this.opNum, other.opNum);
    }

  }
}
