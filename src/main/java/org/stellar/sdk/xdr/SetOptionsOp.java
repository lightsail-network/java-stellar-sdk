// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;


import java.io.IOException;

import com.google.common.base.Objects;

// === xdr source ============================================================

//  struct SetOptionsOp
//  {
//      AccountID* inflationDest; // sets the inflation destination
//  
//      uint32* clearFlags; // which flags to clear
//      uint32* setFlags;   // which flags to set
//  
//      // account threshold manipulation
//      uint32* masterWeight; // weight of the master account
//      uint32* lowThreshold;
//      uint32* medThreshold;
//      uint32* highThreshold;
//  
//      string32* homeDomain; // sets the home domain
//  
//      // Add, update or remove a signer for the account
//      // signer is deleted if the weight is 0
//      Signer* signer;
//  };

//  ===========================================================================
public class SetOptionsOp implements XdrElement {
  public SetOptionsOp () {}
  private AccountID inflationDest;
  public AccountID getInflationDest() {
    return this.inflationDest;
  }
  public void setInflationDest(AccountID value) {
    this.inflationDest = value;
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
  private Uint32 masterWeight;
  public Uint32 getMasterWeight() {
    return this.masterWeight;
  }
  public void setMasterWeight(Uint32 value) {
    this.masterWeight = value;
  }
  private Uint32 lowThreshold;
  public Uint32 getLowThreshold() {
    return this.lowThreshold;
  }
  public void setLowThreshold(Uint32 value) {
    this.lowThreshold = value;
  }
  private Uint32 medThreshold;
  public Uint32 getMedThreshold() {
    return this.medThreshold;
  }
  public void setMedThreshold(Uint32 value) {
    this.medThreshold = value;
  }
  private Uint32 highThreshold;
  public Uint32 getHighThreshold() {
    return this.highThreshold;
  }
  public void setHighThreshold(Uint32 value) {
    this.highThreshold = value;
  }
  private String32 homeDomain;
  public String32 getHomeDomain() {
    return this.homeDomain;
  }
  public void setHomeDomain(String32 value) {
    this.homeDomain = value;
  }
  private Signer signer;
  public Signer getSigner() {
    return this.signer;
  }
  public void setSigner(Signer value) {
    this.signer = value;
  }
  public static void encode(XdrDataOutputStream stream, SetOptionsOp encodedSetOptionsOp) throws IOException{
    if (encodedSetOptionsOp.inflationDest != null) {
    stream.writeInt(1);
    AccountID.encode(stream, encodedSetOptionsOp.inflationDest);
    } else {
    stream.writeInt(0);
    }
    if (encodedSetOptionsOp.clearFlags != null) {
    stream.writeInt(1);
    Uint32.encode(stream, encodedSetOptionsOp.clearFlags);
    } else {
    stream.writeInt(0);
    }
    if (encodedSetOptionsOp.setFlags != null) {
    stream.writeInt(1);
    Uint32.encode(stream, encodedSetOptionsOp.setFlags);
    } else {
    stream.writeInt(0);
    }
    if (encodedSetOptionsOp.masterWeight != null) {
    stream.writeInt(1);
    Uint32.encode(stream, encodedSetOptionsOp.masterWeight);
    } else {
    stream.writeInt(0);
    }
    if (encodedSetOptionsOp.lowThreshold != null) {
    stream.writeInt(1);
    Uint32.encode(stream, encodedSetOptionsOp.lowThreshold);
    } else {
    stream.writeInt(0);
    }
    if (encodedSetOptionsOp.medThreshold != null) {
    stream.writeInt(1);
    Uint32.encode(stream, encodedSetOptionsOp.medThreshold);
    } else {
    stream.writeInt(0);
    }
    if (encodedSetOptionsOp.highThreshold != null) {
    stream.writeInt(1);
    Uint32.encode(stream, encodedSetOptionsOp.highThreshold);
    } else {
    stream.writeInt(0);
    }
    if (encodedSetOptionsOp.homeDomain != null) {
    stream.writeInt(1);
    String32.encode(stream, encodedSetOptionsOp.homeDomain);
    } else {
    stream.writeInt(0);
    }
    if (encodedSetOptionsOp.signer != null) {
    stream.writeInt(1);
    Signer.encode(stream, encodedSetOptionsOp.signer);
    } else {
    stream.writeInt(0);
    }
  }
  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }
  public static SetOptionsOp decode(XdrDataInputStream stream) throws IOException {
    SetOptionsOp decodedSetOptionsOp = new SetOptionsOp();
    int inflationDestPresent = stream.readInt();
    if (inflationDestPresent != 0) {
    decodedSetOptionsOp.inflationDest = AccountID.decode(stream);
    }
    int clearFlagsPresent = stream.readInt();
    if (clearFlagsPresent != 0) {
    decodedSetOptionsOp.clearFlags = Uint32.decode(stream);
    }
    int setFlagsPresent = stream.readInt();
    if (setFlagsPresent != 0) {
    decodedSetOptionsOp.setFlags = Uint32.decode(stream);
    }
    int masterWeightPresent = stream.readInt();
    if (masterWeightPresent != 0) {
    decodedSetOptionsOp.masterWeight = Uint32.decode(stream);
    }
    int lowThresholdPresent = stream.readInt();
    if (lowThresholdPresent != 0) {
    decodedSetOptionsOp.lowThreshold = Uint32.decode(stream);
    }
    int medThresholdPresent = stream.readInt();
    if (medThresholdPresent != 0) {
    decodedSetOptionsOp.medThreshold = Uint32.decode(stream);
    }
    int highThresholdPresent = stream.readInt();
    if (highThresholdPresent != 0) {
    decodedSetOptionsOp.highThreshold = Uint32.decode(stream);
    }
    int homeDomainPresent = stream.readInt();
    if (homeDomainPresent != 0) {
    decodedSetOptionsOp.homeDomain = String32.decode(stream);
    }
    int signerPresent = stream.readInt();
    if (signerPresent != 0) {
    decodedSetOptionsOp.signer = Signer.decode(stream);
    }
    return decodedSetOptionsOp;
  }
  @Override
  public int hashCode() {
    return Objects.hashCode(this.inflationDest, this.clearFlags, this.setFlags, this.masterWeight, this.lowThreshold, this.medThreshold, this.highThreshold, this.homeDomain, this.signer);
  }
  @Override
  public boolean equals(Object object) {
    if (object == null || !(object instanceof SetOptionsOp)) {
      return false;
    }

    SetOptionsOp other = (SetOptionsOp) object;
    return Objects.equal(this.inflationDest, other.inflationDest) && Objects.equal(this.clearFlags, other.clearFlags) && Objects.equal(this.setFlags, other.setFlags) && Objects.equal(this.masterWeight, other.masterWeight) && Objects.equal(this.lowThreshold, other.lowThreshold) && Objects.equal(this.medThreshold, other.medThreshold) && Objects.equal(this.highThreshold, other.highThreshold) && Objects.equal(this.homeDomain, other.homeDomain) && Objects.equal(this.signer, other.signer);
  }
}
