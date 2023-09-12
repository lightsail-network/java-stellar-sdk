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

//  struct ConfigSettingContractMetaDataV0
//  {
//      // Maximum size of extended meta data produced by a transaction
//      uint32 txMaxExtendedMetaDataSizeBytes;
//      // Fee for generating 1KB of extended meta data
//      int64 feeExtendedMetaData1KB;
//  };

//  ===========================================================================
public class ConfigSettingContractMetaDataV0 implements XdrElement {
  public ConfigSettingContractMetaDataV0() {}

  private Uint32 txMaxExtendedMetaDataSizeBytes;

  public Uint32 getTxMaxExtendedMetaDataSizeBytes() {
    return this.txMaxExtendedMetaDataSizeBytes;
  }

  public void setTxMaxExtendedMetaDataSizeBytes(Uint32 value) {
    this.txMaxExtendedMetaDataSizeBytes = value;
  }

  private Int64 feeExtendedMetaData1KB;

  public Int64 getFeeExtendedMetaData1KB() {
    return this.feeExtendedMetaData1KB;
  }

  public void setFeeExtendedMetaData1KB(Int64 value) {
    this.feeExtendedMetaData1KB = value;
  }

  public static void encode(
      XdrDataOutputStream stream,
      ConfigSettingContractMetaDataV0 encodedConfigSettingContractMetaDataV0)
      throws IOException {
    Uint32.encode(stream, encodedConfigSettingContractMetaDataV0.txMaxExtendedMetaDataSizeBytes);
    Int64.encode(stream, encodedConfigSettingContractMetaDataV0.feeExtendedMetaData1KB);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static ConfigSettingContractMetaDataV0 decode(XdrDataInputStream stream)
      throws IOException {
    ConfigSettingContractMetaDataV0 decodedConfigSettingContractMetaDataV0 =
        new ConfigSettingContractMetaDataV0();
    decodedConfigSettingContractMetaDataV0.txMaxExtendedMetaDataSizeBytes = Uint32.decode(stream);
    decodedConfigSettingContractMetaDataV0.feeExtendedMetaData1KB = Int64.decode(stream);
    return decodedConfigSettingContractMetaDataV0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.txMaxExtendedMetaDataSizeBytes, this.feeExtendedMetaData1KB);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof ConfigSettingContractMetaDataV0)) {
      return false;
    }

    ConfigSettingContractMetaDataV0 other = (ConfigSettingContractMetaDataV0) object;
    return Objects.equals(this.txMaxExtendedMetaDataSizeBytes, other.txMaxExtendedMetaDataSizeBytes)
        && Objects.equals(this.feeExtendedMetaData1KB, other.feeExtendedMetaData1KB);
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

  public static ConfigSettingContractMetaDataV0 fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64.getDecoder().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static ConfigSettingContractMetaDataV0 fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  public static final class Builder {
    private Uint32 txMaxExtendedMetaDataSizeBytes;
    private Int64 feeExtendedMetaData1KB;

    public Builder txMaxExtendedMetaDataSizeBytes(Uint32 txMaxExtendedMetaDataSizeBytes) {
      this.txMaxExtendedMetaDataSizeBytes = txMaxExtendedMetaDataSizeBytes;
      return this;
    }

    public Builder feeExtendedMetaData1KB(Int64 feeExtendedMetaData1KB) {
      this.feeExtendedMetaData1KB = feeExtendedMetaData1KB;
      return this;
    }

    public ConfigSettingContractMetaDataV0 build() {
      ConfigSettingContractMetaDataV0 val = new ConfigSettingContractMetaDataV0();
      val.setTxMaxExtendedMetaDataSizeBytes(this.txMaxExtendedMetaDataSizeBytes);
      val.setFeeExtendedMetaData1KB(this.feeExtendedMetaData1KB);
      return val;
    }
  }
}
