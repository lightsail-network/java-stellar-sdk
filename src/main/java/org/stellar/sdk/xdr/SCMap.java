// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.IOException;
import java.util.Arrays;

// === xdr source ============================================================

//  typedef SCMapEntry SCMap<>;

//  ===========================================================================
public class SCMap implements XdrElement {
  private SCMapEntry[] SCMap;

  public SCMap() {}

  public SCMap(SCMapEntry[] SCMap) {
    this.SCMap = SCMap;
  }

  public SCMapEntry[] getSCMap() {
    return this.SCMap;
  }

  public void setSCMap(SCMapEntry[] value) {
    this.SCMap = value;
  }

  public static void encode(XdrDataOutputStream stream, SCMap encodedSCMap) throws IOException {
    int SCMapsize = encodedSCMap.getSCMap().length;
    stream.writeInt(SCMapsize);
    for (int i = 0; i < SCMapsize; i++) {
      SCMapEntry.encode(stream, encodedSCMap.SCMap[i]);
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static SCMap decode(XdrDataInputStream stream) throws IOException {
    SCMap decodedSCMap = new SCMap();
    int SCMapsize = stream.readInt();
    decodedSCMap.SCMap = new SCMapEntry[SCMapsize];
    for (int i = 0; i < SCMapsize; i++) {
      decodedSCMap.SCMap[i] = SCMapEntry.decode(stream);
    }
    return decodedSCMap;
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(this.SCMap);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof SCMap)) {
      return false;
    }

    SCMap other = (SCMap) object;
    return Arrays.equals(this.SCMap, other.SCMap);
  }
}