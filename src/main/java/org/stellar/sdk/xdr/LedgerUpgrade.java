// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

// === xdr source ============================================================

//  union LedgerUpgrade switch (LedgerUpgradeType type)
//  {
//  case LEDGER_UPGRADE_VERSION:
//      uint32 newLedgerVersion; // update ledgerVersion
//  case LEDGER_UPGRADE_BASE_FEE:
//      uint32 newBaseFee; // update baseFee
//  case LEDGER_UPGRADE_MAX_TX_SET_SIZE:
//      uint32 newMaxTxSetSize; // update maxTxSetSize
//  case LEDGER_UPGRADE_BASE_RESERVE:
//      uint32 newBaseReserve; // update baseReserve
//  case LEDGER_UPGRADE_FLAGS:
//      uint32 newFlags; // update flags
//  case LEDGER_UPGRADE_CONFIG:
//      // Update arbitrary `ConfigSetting` entries identified by the key.
//      ConfigUpgradeSetKey newConfig;
//  case LEDGER_UPGRADE_MAX_SOROBAN_TX_SET_SIZE:
//      // Update ConfigSettingContractExecutionLanesV0.ledgerMaxTxCount without
//      // using `LEDGER_UPGRADE_CONFIG`.
//      uint32 newMaxSorobanTxSetSize;
//  };

//  ===========================================================================
public class LedgerUpgrade implements XdrElement {
  public LedgerUpgrade() {}

  LedgerUpgradeType type;

  public LedgerUpgradeType getDiscriminant() {
    return this.type;
  }

  public void setDiscriminant(LedgerUpgradeType value) {
    this.type = value;
  }

  private Uint32 newLedgerVersion;

  public Uint32 getNewLedgerVersion() {
    return this.newLedgerVersion;
  }

  public void setNewLedgerVersion(Uint32 value) {
    this.newLedgerVersion = value;
  }

  private Uint32 newBaseFee;

  public Uint32 getNewBaseFee() {
    return this.newBaseFee;
  }

  public void setNewBaseFee(Uint32 value) {
    this.newBaseFee = value;
  }

  private Uint32 newMaxTxSetSize;

  public Uint32 getNewMaxTxSetSize() {
    return this.newMaxTxSetSize;
  }

  public void setNewMaxTxSetSize(Uint32 value) {
    this.newMaxTxSetSize = value;
  }

  private Uint32 newBaseReserve;

  public Uint32 getNewBaseReserve() {
    return this.newBaseReserve;
  }

  public void setNewBaseReserve(Uint32 value) {
    this.newBaseReserve = value;
  }

  private Uint32 newFlags;

  public Uint32 getNewFlags() {
    return this.newFlags;
  }

  public void setNewFlags(Uint32 value) {
    this.newFlags = value;
  }

  private ConfigUpgradeSetKey newConfig;

  public ConfigUpgradeSetKey getNewConfig() {
    return this.newConfig;
  }

  public void setNewConfig(ConfigUpgradeSetKey value) {
    this.newConfig = value;
  }

  private Uint32 newMaxSorobanTxSetSize;

  public Uint32 getNewMaxSorobanTxSetSize() {
    return this.newMaxSorobanTxSetSize;
  }

  public void setNewMaxSorobanTxSetSize(Uint32 value) {
    this.newMaxSorobanTxSetSize = value;
  }

  public static final class Builder {
    private LedgerUpgradeType discriminant;
    private Uint32 newLedgerVersion;
    private Uint32 newBaseFee;
    private Uint32 newMaxTxSetSize;
    private Uint32 newBaseReserve;
    private Uint32 newFlags;
    private ConfigUpgradeSetKey newConfig;
    private Uint32 newMaxSorobanTxSetSize;

    public Builder discriminant(LedgerUpgradeType discriminant) {
      this.discriminant = discriminant;
      return this;
    }

    public Builder newLedgerVersion(Uint32 newLedgerVersion) {
      this.newLedgerVersion = newLedgerVersion;
      return this;
    }

    public Builder newBaseFee(Uint32 newBaseFee) {
      this.newBaseFee = newBaseFee;
      return this;
    }

    public Builder newMaxTxSetSize(Uint32 newMaxTxSetSize) {
      this.newMaxTxSetSize = newMaxTxSetSize;
      return this;
    }

    public Builder newBaseReserve(Uint32 newBaseReserve) {
      this.newBaseReserve = newBaseReserve;
      return this;
    }

    public Builder newFlags(Uint32 newFlags) {
      this.newFlags = newFlags;
      return this;
    }

    public Builder newConfig(ConfigUpgradeSetKey newConfig) {
      this.newConfig = newConfig;
      return this;
    }

    public Builder newMaxSorobanTxSetSize(Uint32 newMaxSorobanTxSetSize) {
      this.newMaxSorobanTxSetSize = newMaxSorobanTxSetSize;
      return this;
    }

    public LedgerUpgrade build() {
      LedgerUpgrade val = new LedgerUpgrade();
      val.setDiscriminant(discriminant);
      val.setNewLedgerVersion(this.newLedgerVersion);
      val.setNewBaseFee(this.newBaseFee);
      val.setNewMaxTxSetSize(this.newMaxTxSetSize);
      val.setNewBaseReserve(this.newBaseReserve);
      val.setNewFlags(this.newFlags);
      val.setNewConfig(this.newConfig);
      val.setNewMaxSorobanTxSetSize(this.newMaxSorobanTxSetSize);
      return val;
    }
  }

  public static void encode(XdrDataOutputStream stream, LedgerUpgrade encodedLedgerUpgrade)
      throws IOException {
    // Xdrgen::AST::Identifier
    // LedgerUpgradeType
    stream.writeInt(encodedLedgerUpgrade.getDiscriminant().getValue());
    switch (encodedLedgerUpgrade.getDiscriminant()) {
      case LEDGER_UPGRADE_VERSION:
        Uint32.encode(stream, encodedLedgerUpgrade.newLedgerVersion);
        break;
      case LEDGER_UPGRADE_BASE_FEE:
        Uint32.encode(stream, encodedLedgerUpgrade.newBaseFee);
        break;
      case LEDGER_UPGRADE_MAX_TX_SET_SIZE:
        Uint32.encode(stream, encodedLedgerUpgrade.newMaxTxSetSize);
        break;
      case LEDGER_UPGRADE_BASE_RESERVE:
        Uint32.encode(stream, encodedLedgerUpgrade.newBaseReserve);
        break;
      case LEDGER_UPGRADE_FLAGS:
        Uint32.encode(stream, encodedLedgerUpgrade.newFlags);
        break;
      case LEDGER_UPGRADE_CONFIG:
        ConfigUpgradeSetKey.encode(stream, encodedLedgerUpgrade.newConfig);
        break;
      case LEDGER_UPGRADE_MAX_SOROBAN_TX_SET_SIZE:
        Uint32.encode(stream, encodedLedgerUpgrade.newMaxSorobanTxSetSize);
        break;
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static LedgerUpgrade decode(XdrDataInputStream stream) throws IOException {
    LedgerUpgrade decodedLedgerUpgrade = new LedgerUpgrade();
    LedgerUpgradeType discriminant = LedgerUpgradeType.decode(stream);
    decodedLedgerUpgrade.setDiscriminant(discriminant);
    switch (decodedLedgerUpgrade.getDiscriminant()) {
      case LEDGER_UPGRADE_VERSION:
        decodedLedgerUpgrade.newLedgerVersion = Uint32.decode(stream);
        break;
      case LEDGER_UPGRADE_BASE_FEE:
        decodedLedgerUpgrade.newBaseFee = Uint32.decode(stream);
        break;
      case LEDGER_UPGRADE_MAX_TX_SET_SIZE:
        decodedLedgerUpgrade.newMaxTxSetSize = Uint32.decode(stream);
        break;
      case LEDGER_UPGRADE_BASE_RESERVE:
        decodedLedgerUpgrade.newBaseReserve = Uint32.decode(stream);
        break;
      case LEDGER_UPGRADE_FLAGS:
        decodedLedgerUpgrade.newFlags = Uint32.decode(stream);
        break;
      case LEDGER_UPGRADE_CONFIG:
        decodedLedgerUpgrade.newConfig = ConfigUpgradeSetKey.decode(stream);
        break;
      case LEDGER_UPGRADE_MAX_SOROBAN_TX_SET_SIZE:
        decodedLedgerUpgrade.newMaxSorobanTxSetSize = Uint32.decode(stream);
        break;
    }
    return decodedLedgerUpgrade;
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        this.newLedgerVersion,
        this.newBaseFee,
        this.newMaxTxSetSize,
        this.newBaseReserve,
        this.newFlags,
        this.newConfig,
        this.newMaxSorobanTxSetSize,
        this.type);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof LedgerUpgrade)) {
      return false;
    }

    LedgerUpgrade other = (LedgerUpgrade) object;
    return Objects.equals(this.newLedgerVersion, other.newLedgerVersion)
        && Objects.equals(this.newBaseFee, other.newBaseFee)
        && Objects.equals(this.newMaxTxSetSize, other.newMaxTxSetSize)
        && Objects.equals(this.newBaseReserve, other.newBaseReserve)
        && Objects.equals(this.newFlags, other.newFlags)
        && Objects.equals(this.newConfig, other.newConfig)
        && Objects.equals(this.newMaxSorobanTxSetSize, other.newMaxSorobanTxSetSize)
        && Objects.equals(this.type, other.type);
  }

  @Override
  public String toXdrBase64() throws IOException {
    return Base64.getEncoder().encodeToString(toXdrByteArray());
  }

  @Override
  public byte[] toXdrByteArray() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    encode(xdrDataOutputStream);
    return byteArrayOutputStream.toByteArray();
  }

  public static LedgerUpgrade fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.getDecoder().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static LedgerUpgrade fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
