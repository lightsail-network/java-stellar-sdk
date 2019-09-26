// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;


import java.io.IOException;

import com.google.common.base.Objects;

// === xdr source ============================================================

//  typedef int64 SequenceNumber;

//  ===========================================================================
public class SequenceNumber implements XdrElement {
  private Int64 SequenceNumber;
  public Int64 getSequenceNumber() {
    return this.SequenceNumber;
  }
  public void setSequenceNumber(Int64 value) {
    this.SequenceNumber = value;
  }
  public static void encode(XdrDataOutputStream stream, SequenceNumber  encodedSequenceNumber) throws IOException {
  Int64.encode(stream, encodedSequenceNumber.SequenceNumber);
  }
  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }
  public static SequenceNumber decode(XdrDataInputStream stream) throws IOException {
    SequenceNumber decodedSequenceNumber = new SequenceNumber();
  decodedSequenceNumber.SequenceNumber = Int64.decode(stream);
    return decodedSequenceNumber;
  }
  @Override
  public int hashCode() {
    return Objects.hashCode(this.SequenceNumber);
  }
  @Override
  public boolean equals(Object object) {
    if (object == null || !(object instanceof SequenceNumber)) {
      return false;
    }

    SequenceNumber other = (SequenceNumber) object;
    return Objects.equal(this.SequenceNumber, other.SequenceNumber);
  }
}
