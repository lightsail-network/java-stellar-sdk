// Automatically generated on 2015-11-05T11:21:06-08:00
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import java.io.IOException;

// === xdr source ============================================================

//  enum ThresholdIndexes
//  {
//      THRESHOLD_MASTER_WEIGHT = 0,
//      THRESHOLD_LOW = 1,
//      THRESHOLD_MED = 2,
//      THRESHOLD_HIGH = 3
//  };

//  ===========================================================================
public enum ThresholdIndices {
  THRESHOLD_MASTER_WEIGHT(0),
  THRESHOLD_LOW(1),
  THRESHOLD_MED(2),
  THRESHOLD_HIGH(3),
  ;
  private int mValue;

  ThresholdIndices(int value) {
    mValue = value;
  }

  public int getValue() {
    return mValue;
  }

  static ThresholdIndices decode(XdrDataInputStream stream) throws IOException {
    int value = stream.readInt();
    switch (value) {
      case 0:
        return THRESHOLD_MASTER_WEIGHT;
      case 1:
        return THRESHOLD_LOW;
      case 2:
        return THRESHOLD_MED;
      case 3:
        return THRESHOLD_HIGH;
      default:
        throw new RuntimeException("Unknown enum value: " + value);
    }
  }

  static void encode(XdrDataOutputStream stream, ThresholdIndices value) throws IOException {
    stream.writeInt(value.getValue());
  }
}
