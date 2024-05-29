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
 * StateArchivalSettings's original definition in the XDR file is:
 *
 * <pre>
 * struct StateArchivalSettings {
 *     uint32 maxEntryTTL;
 *     uint32 minTemporaryTTL;
 *     uint32 minPersistentTTL;
 *
 *     // rent_fee = wfee_rate_average / rent_rate_denominator_for_type
 *     int64 persistentRentRateDenominator;
 *     int64 tempRentRateDenominator;
 *
 *     // max number of entries that emit archival meta in a single ledger
 *     uint32 maxEntriesToArchive;
 *
 *     // Number of snapshots to use when calculating average BucketList size
 *     uint32 bucketListSizeWindowSampleSize;
 *
 *     // How often to sample the BucketList size for the average, in ledgers
 *     uint32 bucketListWindowSamplePeriod;
 *
 *     // Maximum number of bytes that we scan for eviction per ledger
 *     uint32 evictionScanSize;
 *
 *     // Lowest BucketList level to be scanned to evict entries
 *     uint32 startingEvictionScanLevel;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class StateArchivalSettings implements XdrElement {
  private Uint32 maxEntryTTL;
  private Uint32 minTemporaryTTL;
  private Uint32 minPersistentTTL;
  private Int64 persistentRentRateDenominator;
  private Int64 tempRentRateDenominator;
  private Uint32 maxEntriesToArchive;
  private Uint32 bucketListSizeWindowSampleSize;
  private Uint32 bucketListWindowSamplePeriod;
  private Uint32 evictionScanSize;
  private Uint32 startingEvictionScanLevel;

  public void encode(XdrDataOutputStream stream) throws IOException {
    maxEntryTTL.encode(stream);
    minTemporaryTTL.encode(stream);
    minPersistentTTL.encode(stream);
    persistentRentRateDenominator.encode(stream);
    tempRentRateDenominator.encode(stream);
    maxEntriesToArchive.encode(stream);
    bucketListSizeWindowSampleSize.encode(stream);
    bucketListWindowSamplePeriod.encode(stream);
    evictionScanSize.encode(stream);
    startingEvictionScanLevel.encode(stream);
  }

  public static StateArchivalSettings decode(XdrDataInputStream stream) throws IOException {
    StateArchivalSettings decodedStateArchivalSettings = new StateArchivalSettings();
    decodedStateArchivalSettings.maxEntryTTL = Uint32.decode(stream);
    decodedStateArchivalSettings.minTemporaryTTL = Uint32.decode(stream);
    decodedStateArchivalSettings.minPersistentTTL = Uint32.decode(stream);
    decodedStateArchivalSettings.persistentRentRateDenominator = Int64.decode(stream);
    decodedStateArchivalSettings.tempRentRateDenominator = Int64.decode(stream);
    decodedStateArchivalSettings.maxEntriesToArchive = Uint32.decode(stream);
    decodedStateArchivalSettings.bucketListSizeWindowSampleSize = Uint32.decode(stream);
    decodedStateArchivalSettings.bucketListWindowSamplePeriod = Uint32.decode(stream);
    decodedStateArchivalSettings.evictionScanSize = Uint32.decode(stream);
    decodedStateArchivalSettings.startingEvictionScanLevel = Uint32.decode(stream);
    return decodedStateArchivalSettings;
  }

  public static StateArchivalSettings fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static StateArchivalSettings fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
