// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;


import java.io.IOException;

import java.util.Arrays;
import com.google.common.base.Objects;

// === xdr source ============================================================

//  struct ManageOfferSuccessResult
//  {
//      // offers that got claimed while creating this offer
//      ClaimOfferAtom offersClaimed<>;
//  
//      union switch (ManageOfferEffect effect)
//      {
//      case MANAGE_OFFER_CREATED:
//      case MANAGE_OFFER_UPDATED:
//          OfferEntry offer;
//      default:
//          void;
//      }
//      offer;
//  };

//  ===========================================================================
public class ManageOfferSuccessResult implements XdrElement {
  public ManageOfferSuccessResult () {}
  private ClaimOfferAtom[] offersClaimed;
  public ClaimOfferAtom[] getOffersClaimed() {
    return this.offersClaimed;
  }
  public void setOffersClaimed(ClaimOfferAtom[] value) {
    this.offersClaimed = value;
  }
  private ManageOfferSuccessResultOffer offer;
  public ManageOfferSuccessResultOffer getOffer() {
    return this.offer;
  }
  public void setOffer(ManageOfferSuccessResultOffer value) {
    this.offer = value;
  }
  public static void encode(XdrDataOutputStream stream, ManageOfferSuccessResult encodedManageOfferSuccessResult) throws IOException{
    int offersClaimedsize = encodedManageOfferSuccessResult.getOffersClaimed().length;
    stream.writeInt(offersClaimedsize);
    for (int i = 0; i < offersClaimedsize; i++) {
      ClaimOfferAtom.encode(stream, encodedManageOfferSuccessResult.offersClaimed[i]);
    }
    ManageOfferSuccessResultOffer.encode(stream, encodedManageOfferSuccessResult.offer);
  }
  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }
  public static ManageOfferSuccessResult decode(XdrDataInputStream stream) throws IOException {
    ManageOfferSuccessResult decodedManageOfferSuccessResult = new ManageOfferSuccessResult();
    int offersClaimedsize = stream.readInt();
    decodedManageOfferSuccessResult.offersClaimed = new ClaimOfferAtom[offersClaimedsize];
    for (int i = 0; i < offersClaimedsize; i++) {
      decodedManageOfferSuccessResult.offersClaimed[i] = ClaimOfferAtom.decode(stream);
    }
    decodedManageOfferSuccessResult.offer = ManageOfferSuccessResultOffer.decode(stream);
    return decodedManageOfferSuccessResult;
  }
  @Override
  public int hashCode() {
    return Objects.hashCode(Arrays.hashCode(this.offersClaimed), this.offer);
  }
  @Override
  public boolean equals(Object object) {
    if (!(object instanceof ManageOfferSuccessResult)) {
      return false;
    }

    ManageOfferSuccessResult other = (ManageOfferSuccessResult) object;
    return Arrays.equals(this.offersClaimed, other.offersClaimed) && Objects.equal(this.offer, other.offer);
  }

  public static final class Builder {
    private ClaimOfferAtom[] offersClaimed;
    private ManageOfferSuccessResultOffer offer;

    public Builder offersClaimed(ClaimOfferAtom[] offersClaimed) {
      this.offersClaimed = offersClaimed;
      return this;
    }

    public Builder offer(ManageOfferSuccessResultOffer offer) {
      this.offer = offer;
      return this;
    }

    public ManageOfferSuccessResult build() {
      ManageOfferSuccessResult val = new ManageOfferSuccessResult();
      val.setOffersClaimed(offersClaimed);
      val.setOffer(offer);
      return val;
    }
  }

  public static class ManageOfferSuccessResultOffer {
    public ManageOfferSuccessResultOffer () {}
    ManageOfferEffect effect;
    public ManageOfferEffect getDiscriminant() {
      return this.effect;
    }
    public void setDiscriminant(ManageOfferEffect value) {
      this.effect = value;
    }
    private OfferEntry offer;
    public OfferEntry getOffer() {
      return this.offer;
    }
    public void setOffer(OfferEntry value) {
      this.offer = value;
    }

    public static final class Builder {
      private ManageOfferEffect discriminant;
      private OfferEntry offer;

      public Builder discriminant(ManageOfferEffect discriminant) {
        this.discriminant = discriminant;
        return this;
      }

      public Builder offer(OfferEntry offer) {
        this.offer = offer;
        return this;
      }

      public ManageOfferSuccessResultOffer build() {
        ManageOfferSuccessResultOffer val = new ManageOfferSuccessResultOffer();
        val.setDiscriminant(discriminant);
        val.setOffer(offer);
        return val;
      }
    }

    public static void encode(XdrDataOutputStream stream, ManageOfferSuccessResultOffer encodedManageOfferSuccessResultOffer) throws IOException {
    //Xdrgen::AST::Identifier
    //ManageOfferEffect
    stream.writeInt(encodedManageOfferSuccessResultOffer.getDiscriminant().getValue());
    switch (encodedManageOfferSuccessResultOffer.getDiscriminant()) {
    case MANAGE_OFFER_CREATED:
    case MANAGE_OFFER_UPDATED:
    OfferEntry.encode(stream, encodedManageOfferSuccessResultOffer.offer);
    break;
    default:
    break;
    }
    }
    public void encode(XdrDataOutputStream stream) throws IOException {
      encode(stream, this);
    }
    public static ManageOfferSuccessResultOffer decode(XdrDataInputStream stream) throws IOException {
    ManageOfferSuccessResultOffer decodedManageOfferSuccessResultOffer = new ManageOfferSuccessResultOffer();
    ManageOfferEffect discriminant = ManageOfferEffect.decode(stream);
    decodedManageOfferSuccessResultOffer.setDiscriminant(discriminant);
    switch (decodedManageOfferSuccessResultOffer.getDiscriminant()) {
    case MANAGE_OFFER_CREATED:
    case MANAGE_OFFER_UPDATED:
    decodedManageOfferSuccessResultOffer.offer = OfferEntry.decode(stream);
    break;
    default:
    break;
    }
      return decodedManageOfferSuccessResultOffer;
    }
    @Override
    public int hashCode() {
      return Objects.hashCode(this.offer, this.effect);
    }
    @Override
    public boolean equals(Object object) {
      if (!(object instanceof ManageOfferSuccessResultOffer)) {
        return false;
      }

      ManageOfferSuccessResultOffer other = (ManageOfferSuccessResultOffer) object;
      return Objects.equal(this.offer, other.offer) && Objects.equal(this.effect, other.effect);
    }

  }
}
