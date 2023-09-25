package org.stellar.sdk.responses;

import java.math.BigInteger;
import java.util.Objects;

public class MuxedAccount {
  private final String muxedAddress;
  private final String unmuxedAddress;
  private final BigInteger id;

  public MuxedAccount(String muxedAddress, String unmuxedAddress, BigInteger id) {
    this.muxedAddress = muxedAddress;
    this.unmuxedAddress = unmuxedAddress;
    this.id = id;
  }

  @Override
  public String toString() {
    return muxedAddress;
  }

  public BigInteger getId() {
    return id;
  }

  public String getUnmuxedAddress() {
    return unmuxedAddress;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    return (getClass() == o.getClass())
        && Objects.equals(muxedAddress, ((MuxedAccount) o).muxedAddress)
        && Objects.equals(unmuxedAddress, ((MuxedAccount) o).unmuxedAddress)
        && Objects.equals(id, ((MuxedAccount) o).id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(muxedAddress, unmuxedAddress, id);
  }
}
