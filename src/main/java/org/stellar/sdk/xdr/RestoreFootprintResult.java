// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import com.google.common.base.Objects;
import java.io.IOException;

// === xdr source ============================================================

//  union RestoreFootprintResult switch (RestoreFootprintResultCode code)
//  {
//  case RESTORE_FOOTPRINT_SUCCESS:
//      void;
//  case RESTORE_FOOTPRINT_MALFORMED:
//  case RESTORE_FOOTPRINT_RESOURCE_LIMIT_EXCEEDED:
//      void;
//  };

//  ===========================================================================
public class RestoreFootprintResult implements XdrElement {
  public RestoreFootprintResult() {}

  RestoreFootprintResultCode code;

  public RestoreFootprintResultCode getDiscriminant() {
    return this.code;
  }

  public void setDiscriminant(RestoreFootprintResultCode value) {
    this.code = value;
  }

  public static final class Builder {
    private RestoreFootprintResultCode discriminant;

    public Builder discriminant(RestoreFootprintResultCode discriminant) {
      this.discriminant = discriminant;
      return this;
    }

    public RestoreFootprintResult build() {
      RestoreFootprintResult val = new RestoreFootprintResult();
      val.setDiscriminant(discriminant);
      return val;
    }
  }

  public static void encode(
      XdrDataOutputStream stream, RestoreFootprintResult encodedRestoreFootprintResult)
      throws IOException {
    // Xdrgen::AST::Identifier
    // RestoreFootprintResultCode
    stream.writeInt(encodedRestoreFootprintResult.getDiscriminant().getValue());
    switch (encodedRestoreFootprintResult.getDiscriminant()) {
      case RESTORE_FOOTPRINT_SUCCESS:
        break;
      case RESTORE_FOOTPRINT_MALFORMED:
      case RESTORE_FOOTPRINT_RESOURCE_LIMIT_EXCEEDED:
        break;
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static RestoreFootprintResult decode(XdrDataInputStream stream) throws IOException {
    RestoreFootprintResult decodedRestoreFootprintResult = new RestoreFootprintResult();
    RestoreFootprintResultCode discriminant = RestoreFootprintResultCode.decode(stream);
    decodedRestoreFootprintResult.setDiscriminant(discriminant);
    switch (decodedRestoreFootprintResult.getDiscriminant()) {
      case RESTORE_FOOTPRINT_SUCCESS:
        break;
      case RESTORE_FOOTPRINT_MALFORMED:
      case RESTORE_FOOTPRINT_RESOURCE_LIMIT_EXCEEDED:
        break;
    }
    return decodedRestoreFootprintResult;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.code);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof RestoreFootprintResult)) {
      return false;
    }

    RestoreFootprintResult other = (RestoreFootprintResult) object;
    return Objects.equal(this.code, other.code);
  }
}