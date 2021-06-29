package org.stellar.sdk.responses;

import com.google.common.base.Objects;

public class MuxedAccount {
  private final String muxedAddress;
  private final String unmuxedAddress;
  private final Long id;

  public MuxedAccount(String muxedAddress, String unmuxedAddress, Long id) {
    this.muxedAddress = muxedAddress;
    this.unmuxedAddress = unmuxedAddress;
    this.id = id;
  }

  @Override
  public String toString() {
    return muxedAddress;
  }

  public Long getId() {
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
    return (getClass() == o.getClass()) && Objects.equal(muxedAddress, ((MuxedAccount)o).muxedAddress)
        && Objects.equal(unmuxedAddress, ((MuxedAccount)o).unmuxedAddress)
        && Objects.equal(id, ((MuxedAccount)o).id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(muxedAddress, unmuxedAddress, id);
  }
}
