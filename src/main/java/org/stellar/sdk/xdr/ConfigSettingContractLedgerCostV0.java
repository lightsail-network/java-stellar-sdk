// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.stellar.sdk.Base64Factory;

/**
 * ConfigSettingContractLedgerCostV0's original definition in the XDR file is:
 *
 * <pre>
 * struct ConfigSettingContractLedgerCostV0
 * {
 *     // Maximum number of ledger entry read operations per ledger
 *     uint32 ledgerMaxReadLedgerEntries;
 *     // Maximum number of bytes that can be read per ledger
 *     uint32 ledgerMaxReadBytes;
 *     // Maximum number of ledger entry write operations per ledger
 *     uint32 ledgerMaxWriteLedgerEntries;
 *     // Maximum number of bytes that can be written per ledger
 *     uint32 ledgerMaxWriteBytes;
 *
 *     // Maximum number of ledger entry read operations per transaction
 *     uint32 txMaxReadLedgerEntries;
 *     // Maximum number of bytes that can be read per transaction
 *     uint32 txMaxReadBytes;
 *     // Maximum number of ledger entry write operations per transaction
 *     uint32 txMaxWriteLedgerEntries;
 *     // Maximum number of bytes that can be written per transaction
 *     uint32 txMaxWriteBytes;
 *
 *     int64 feeReadLedgerEntry;  // Fee per ledger entry read
 *     int64 feeWriteLedgerEntry; // Fee per ledger entry write
 *
 *     int64 feeRead1KB;  // Fee for reading 1KB
 *
 *     // The following parameters determine the write fee per 1KB.
 *     // Write fee grows linearly until bucket list reaches this size
 *     int64 bucketListTargetSizeBytes;
 *     // Fee per 1KB write when the bucket list is empty
 *     int64 writeFee1KBBucketListLow;
 *     // Fee per 1KB write when the bucket list has reached `bucketListTargetSizeBytes`
 *     int64 writeFee1KBBucketListHigh;
 *     // Write fee multiplier for any additional data past the first `bucketListTargetSizeBytes`
 *     uint32 bucketListWriteFeeGrowthFactor;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ConfigSettingContractLedgerCostV0 implements XdrElement {
  private Uint32 ledgerMaxReadLedgerEntries;
  private Uint32 ledgerMaxReadBytes;
  private Uint32 ledgerMaxWriteLedgerEntries;
  private Uint32 ledgerMaxWriteBytes;
  private Uint32 txMaxReadLedgerEntries;
  private Uint32 txMaxReadBytes;
  private Uint32 txMaxWriteLedgerEntries;
  private Uint32 txMaxWriteBytes;
  private Int64 feeReadLedgerEntry;
  private Int64 feeWriteLedgerEntry;
  private Int64 feeRead1KB;
  private Int64 bucketListTargetSizeBytes;
  private Int64 writeFee1KBBucketListLow;
  private Int64 writeFee1KBBucketListHigh;
  private Uint32 bucketListWriteFeeGrowthFactor;

  public void encode(XdrDataOutputStream stream) throws IOException {
    ledgerMaxReadLedgerEntries.encode(stream);
    ledgerMaxReadBytes.encode(stream);
    ledgerMaxWriteLedgerEntries.encode(stream);
    ledgerMaxWriteBytes.encode(stream);
    txMaxReadLedgerEntries.encode(stream);
    txMaxReadBytes.encode(stream);
    txMaxWriteLedgerEntries.encode(stream);
    txMaxWriteBytes.encode(stream);
    feeReadLedgerEntry.encode(stream);
    feeWriteLedgerEntry.encode(stream);
    feeRead1KB.encode(stream);
    bucketListTargetSizeBytes.encode(stream);
    writeFee1KBBucketListLow.encode(stream);
    writeFee1KBBucketListHigh.encode(stream);
    bucketListWriteFeeGrowthFactor.encode(stream);
  }

  public static ConfigSettingContractLedgerCostV0 decode(XdrDataInputStream stream)
      throws IOException {
    ConfigSettingContractLedgerCostV0 decodedConfigSettingContractLedgerCostV0 =
        new ConfigSettingContractLedgerCostV0();
    decodedConfigSettingContractLedgerCostV0.ledgerMaxReadLedgerEntries = Uint32.decode(stream);
    decodedConfigSettingContractLedgerCostV0.ledgerMaxReadBytes = Uint32.decode(stream);
    decodedConfigSettingContractLedgerCostV0.ledgerMaxWriteLedgerEntries = Uint32.decode(stream);
    decodedConfigSettingContractLedgerCostV0.ledgerMaxWriteBytes = Uint32.decode(stream);
    decodedConfigSettingContractLedgerCostV0.txMaxReadLedgerEntries = Uint32.decode(stream);
    decodedConfigSettingContractLedgerCostV0.txMaxReadBytes = Uint32.decode(stream);
    decodedConfigSettingContractLedgerCostV0.txMaxWriteLedgerEntries = Uint32.decode(stream);
    decodedConfigSettingContractLedgerCostV0.txMaxWriteBytes = Uint32.decode(stream);
    decodedConfigSettingContractLedgerCostV0.feeReadLedgerEntry = Int64.decode(stream);
    decodedConfigSettingContractLedgerCostV0.feeWriteLedgerEntry = Int64.decode(stream);
    decodedConfigSettingContractLedgerCostV0.feeRead1KB = Int64.decode(stream);
    decodedConfigSettingContractLedgerCostV0.bucketListTargetSizeBytes = Int64.decode(stream);
    decodedConfigSettingContractLedgerCostV0.writeFee1KBBucketListLow = Int64.decode(stream);
    decodedConfigSettingContractLedgerCostV0.writeFee1KBBucketListHigh = Int64.decode(stream);
    decodedConfigSettingContractLedgerCostV0.bucketListWriteFeeGrowthFactor = Uint32.decode(stream);
    return decodedConfigSettingContractLedgerCostV0;
  }

  public static ConfigSettingContractLedgerCostV0 fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static ConfigSettingContractLedgerCostV0 fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
