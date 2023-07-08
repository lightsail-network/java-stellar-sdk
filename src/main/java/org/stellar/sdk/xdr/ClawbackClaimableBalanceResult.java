// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import com.google.common.base.Objects;
import java.io.IOException;

// === xdr source ============================================================

//  union ClawbackClaimableBalanceResult switch (
//      ClawbackClaimableBalanceResultCode code)
//  {
//  case CLAWBACK_CLAIMABLE_BALANCE_SUCCESS:
//      void;
//  default:
//      void;
//  };

//  ===========================================================================
public class ClawbackClaimableBalanceResult implements XdrElement {
  public ClawbackClaimableBalanceResult() {}

  ClawbackClaimableBalanceResultCode code;

  public ClawbackClaimableBalanceResultCode getDiscriminant() {
    return this.code;
  }

  public void setDiscriminant(ClawbackClaimableBalanceResultCode value) {
    this.code = value;
  }

  public static final class Builder {
    private ClawbackClaimableBalanceResultCode discriminant;

    public Builder discriminant(ClawbackClaimableBalanceResultCode discriminant) {
      this.discriminant = discriminant;
      return this;
    }

    public ClawbackClaimableBalanceResult build() {
      ClawbackClaimableBalanceResult val = new ClawbackClaimableBalanceResult();
      val.setDiscriminant(discriminant);
      return val;
    }
  }

  public static void encode(
      XdrDataOutputStream stream,
      ClawbackClaimableBalanceResult encodedClawbackClaimableBalanceResult)
      throws IOException {
    // Xdrgen::AST::Identifier
    // ClawbackClaimableBalanceResultCode
    stream.writeInt(encodedClawbackClaimableBalanceResult.getDiscriminant().getValue());
    switch (encodedClawbackClaimableBalanceResult.getDiscriminant()) {
      case CLAWBACK_CLAIMABLE_BALANCE_SUCCESS:
        break;
      default:
        break;
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static ClawbackClaimableBalanceResult decode(XdrDataInputStream stream)
      throws IOException {
    ClawbackClaimableBalanceResult decodedClawbackClaimableBalanceResult =
        new ClawbackClaimableBalanceResult();
    ClawbackClaimableBalanceResultCode discriminant =
        ClawbackClaimableBalanceResultCode.decode(stream);
    decodedClawbackClaimableBalanceResult.setDiscriminant(discriminant);
    switch (decodedClawbackClaimableBalanceResult.getDiscriminant()) {
      case CLAWBACK_CLAIMABLE_BALANCE_SUCCESS:
        break;
      default:
        break;
    }
    return decodedClawbackClaimableBalanceResult;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.code);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof ClawbackClaimableBalanceResult)) {
      return false;
    }

    ClawbackClaimableBalanceResult other = (ClawbackClaimableBalanceResult) object;
    return Objects.equal(this.code, other.code);
  }
}
