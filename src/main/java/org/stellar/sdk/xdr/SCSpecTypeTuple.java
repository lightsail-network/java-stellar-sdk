// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.IOException;
import java.util.Arrays;

// === xdr source ============================================================

//  struct SCSpecTypeTuple
//  {
//      SCSpecTypeDef valueTypes<12>;
//  };

//  ===========================================================================
public class SCSpecTypeTuple implements XdrElement {
  public SCSpecTypeTuple() {}

  private SCSpecTypeDef[] valueTypes;

  public SCSpecTypeDef[] getValueTypes() {
    return this.valueTypes;
  }

  public void setValueTypes(SCSpecTypeDef[] value) {
    this.valueTypes = value;
  }

  public static void encode(XdrDataOutputStream stream, SCSpecTypeTuple encodedSCSpecTypeTuple)
      throws IOException {
    int valueTypessize = encodedSCSpecTypeTuple.getValueTypes().length;
    stream.writeInt(valueTypessize);
    for (int i = 0; i < valueTypessize; i++) {
      SCSpecTypeDef.encode(stream, encodedSCSpecTypeTuple.valueTypes[i]);
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static SCSpecTypeTuple decode(XdrDataInputStream stream) throws IOException {
    SCSpecTypeTuple decodedSCSpecTypeTuple = new SCSpecTypeTuple();
    int valueTypessize = stream.readInt();
    decodedSCSpecTypeTuple.valueTypes = new SCSpecTypeDef[valueTypessize];
    for (int i = 0; i < valueTypessize; i++) {
      decodedSCSpecTypeTuple.valueTypes[i] = SCSpecTypeDef.decode(stream);
    }
    return decodedSCSpecTypeTuple;
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(this.valueTypes);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof SCSpecTypeTuple)) {
      return false;
    }

    SCSpecTypeTuple other = (SCSpecTypeTuple) object;
    return Arrays.equals(this.valueTypes, other.valueTypes);
  }

  public static final class Builder {
    private SCSpecTypeDef[] valueTypes;

    public Builder valueTypes(SCSpecTypeDef[] valueTypes) {
      this.valueTypes = valueTypes;
      return this;
    }

    public SCSpecTypeTuple build() {
      SCSpecTypeTuple val = new SCSpecTypeTuple();
      val.setValueTypes(this.valueTypes);
      return val;
    }
  }
}