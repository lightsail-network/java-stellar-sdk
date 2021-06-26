package org.stellar.sdk.responses;

import com.google.common.base.Objects;

public class MuxedAccount {
  private final String muxedAddress;
  private final String accountIdAddress;
  private final Long id;

  public MuxedAccount(String muxedAddress, String accountIdAddress, Long id) {
    this.muxedAddress = muxedAddress;
    this.accountIdAddress = accountIdAddress;
    this.id = id;
  }

  @Override
  public String toString() {
    return muxedAddress;
  }

  public Long getId() {
    return id;
  }

  public String getAccountIdAddress() {
    return accountIdAddress;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    return (getClass() == o.getClass()) && Objects.equal(muxedAddress, ((MuxedAccount)o).muxedAddress)
        && Objects.equal(accountIdAddress, ((MuxedAccount)o).accountIdAddress)
        && Objects.equal(id, ((MuxedAccount)o).id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(muxedAddress, accountIdAddress, id);
  }
}
