// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import com.google.common.base.Objects;
import java.io.IOException;

// === xdr source ============================================================

//  struct ConfigSettingContractExecutionLanesV0
//  {
//      // maximum number of Soroban transactions per ledger
//      uint32 ledgerMaxTxCount;
//  };

//  ===========================================================================
public class ConfigSettingContractExecutionLanesV0 implements XdrElement {
  public ConfigSettingContractExecutionLanesV0() {}

  private Uint32 ledgerMaxTxCount;

  public Uint32 getLedgerMaxTxCount() {
    return this.ledgerMaxTxCount;
  }

  public void setLedgerMaxTxCount(Uint32 value) {
    this.ledgerMaxTxCount = value;
  }

  public static void encode(
      XdrDataOutputStream stream,
      ConfigSettingContractExecutionLanesV0 encodedConfigSettingContractExecutionLanesV0)
      throws IOException {
    Uint32.encode(stream, encodedConfigSettingContractExecutionLanesV0.ledgerMaxTxCount);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static ConfigSettingContractExecutionLanesV0 decode(XdrDataInputStream stream)
      throws IOException {
    ConfigSettingContractExecutionLanesV0 decodedConfigSettingContractExecutionLanesV0 =
        new ConfigSettingContractExecutionLanesV0();
    decodedConfigSettingContractExecutionLanesV0.ledgerMaxTxCount = Uint32.decode(stream);
    return decodedConfigSettingContractExecutionLanesV0;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.ledgerMaxTxCount);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof ConfigSettingContractExecutionLanesV0)) {
      return false;
    }

    ConfigSettingContractExecutionLanesV0 other = (ConfigSettingContractExecutionLanesV0) object;
    return Objects.equal(this.ledgerMaxTxCount, other.ledgerMaxTxCount);
  }

  public static final class Builder {
    private Uint32 ledgerMaxTxCount;

    public Builder ledgerMaxTxCount(Uint32 ledgerMaxTxCount) {
      this.ledgerMaxTxCount = ledgerMaxTxCount;
      return this;
    }

    public ConfigSettingContractExecutionLanesV0 build() {
      ConfigSettingContractExecutionLanesV0 val = new ConfigSettingContractExecutionLanesV0();
      val.setLedgerMaxTxCount(this.ledgerMaxTxCount);
      return val;
    }
  }
}