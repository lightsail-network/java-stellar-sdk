package org.stellar.sdk.responses;

import java.math.BigInteger;
import lombok.Value;

@Value
public class MuxedAccount {
  String muxedAddress;
  String unmuxedAddress;
  BigInteger id;

  @Override
  public String toString() {
    return muxedAddress;
  }
}
