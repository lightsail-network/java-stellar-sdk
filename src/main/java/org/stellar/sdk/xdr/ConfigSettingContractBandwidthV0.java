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
 * ConfigSettingContractBandwidthV0's original definition in the XDR file is:
 *
 * <pre>
 * struct ConfigSettingContractBandwidthV0
 * {
 *     // Maximum sum of all transaction sizes in the ledger in bytes
 *     uint32 ledgerMaxTxsSizeBytes;
 *     // Maximum size in bytes for a transaction
 *     uint32 txMaxSizeBytes;
 *
 *     // Fee for 1 KB of transaction size
 *     int64 feeTxSize1KB;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ConfigSettingContractBandwidthV0 implements XdrElement {
  private Uint32 ledgerMaxTxsSizeBytes;
  private Uint32 txMaxSizeBytes;
  private Int64 feeTxSize1KB;

  public void encode(XdrDataOutputStream stream) throws IOException {
    ledgerMaxTxsSizeBytes.encode(stream);
    txMaxSizeBytes.encode(stream);
    feeTxSize1KB.encode(stream);
  }

  public static ConfigSettingContractBandwidthV0 decode(XdrDataInputStream stream)
      throws IOException {
    ConfigSettingContractBandwidthV0 decodedConfigSettingContractBandwidthV0 =
        new ConfigSettingContractBandwidthV0();
    decodedConfigSettingContractBandwidthV0.ledgerMaxTxsSizeBytes = Uint32.decode(stream);
    decodedConfigSettingContractBandwidthV0.txMaxSizeBytes = Uint32.decode(stream);
    decodedConfigSettingContractBandwidthV0.feeTxSize1KB = Int64.decode(stream);
    return decodedConfigSettingContractBandwidthV0;
  }

  public static ConfigSettingContractBandwidthV0 fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static ConfigSettingContractBandwidthV0 fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
