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
 * StellarValue's original definition in the XDR file is:
 *
 * <pre>
 * struct StellarValue
 * {
 *     Hash txSetHash;      // transaction set to apply to previous ledger
 *     TimePoint closeTime; // network close time
 *
 *     // upgrades to apply to the previous ledger (usually empty)
 *     // this is a vector of encoded 'LedgerUpgrade' so that nodes can drop
 *     // unknown steps during consensus if needed.
 *     // see notes below on 'LedgerUpgrade' for more detail
 *     // max size is dictated by number of upgrade types (+ room for future)
 *     UpgradeType upgrades&lt;6&gt;;
 *
 *     // reserved for future use
 *     union switch (StellarValueType v)
 *     {
 *     case STELLAR_VALUE_BASIC:
 *         void;
 *     case STELLAR_VALUE_SIGNED:
 *         LedgerCloseValueSignature lcValueSignature;
 *     }
 *     ext;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class StellarValue implements XdrElement {
  private Hash txSetHash;
  private TimePoint closeTime;
  private UpgradeType[] upgrades;
  private StellarValueExt ext;

  public void encode(XdrDataOutputStream stream) throws IOException {
    txSetHash.encode(stream);
    closeTime.encode(stream);
    int upgradesSize = getUpgrades().length;
    stream.writeInt(upgradesSize);
    for (int i = 0; i < upgradesSize; i++) {
      upgrades[i].encode(stream);
    }
    ext.encode(stream);
  }

  public static StellarValue decode(XdrDataInputStream stream) throws IOException {
    StellarValue decodedStellarValue = new StellarValue();
    decodedStellarValue.txSetHash = Hash.decode(stream);
    decodedStellarValue.closeTime = TimePoint.decode(stream);
    int upgradesSize = stream.readInt();
    decodedStellarValue.upgrades = new UpgradeType[upgradesSize];
    for (int i = 0; i < upgradesSize; i++) {
      decodedStellarValue.upgrades[i] = UpgradeType.decode(stream);
    }
    decodedStellarValue.ext = StellarValueExt.decode(stream);
    return decodedStellarValue;
  }

  public static StellarValue fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static StellarValue fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  /**
   * StellarValueExt's original definition in the XDR file is:
   *
   * <pre>
   * union switch (StellarValueType v)
   *     {
   *     case STELLAR_VALUE_BASIC:
   *         void;
   *     case STELLAR_VALUE_SIGNED:
   *         LedgerCloseValueSignature lcValueSignature;
   *     }
   * </pre>
   */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder(toBuilder = true)
  public static class StellarValueExt implements XdrElement {
    private StellarValueType discriminant;
    private LedgerCloseValueSignature lcValueSignature;

    public void encode(XdrDataOutputStream stream) throws IOException {
      stream.writeInt(discriminant.getValue());
      switch (discriminant) {
        case STELLAR_VALUE_BASIC:
          break;
        case STELLAR_VALUE_SIGNED:
          lcValueSignature.encode(stream);
          break;
      }
    }

    public static StellarValueExt decode(XdrDataInputStream stream) throws IOException {
      StellarValueExt decodedStellarValueExt = new StellarValueExt();
      StellarValueType discriminant = StellarValueType.decode(stream);
      decodedStellarValueExt.setDiscriminant(discriminant);
      switch (decodedStellarValueExt.getDiscriminant()) {
        case STELLAR_VALUE_BASIC:
          break;
        case STELLAR_VALUE_SIGNED:
          decodedStellarValueExt.lcValueSignature = LedgerCloseValueSignature.decode(stream);
          break;
      }
      return decodedStellarValueExt;
    }

    public static StellarValueExt fromXdrBase64(String xdr) throws IOException {
      byte[] bytes = Base64Factory.getInstance().decode(xdr);
      return fromXdrByteArray(bytes);
    }

    public static StellarValueExt fromXdrByteArray(byte[] xdr) throws IOException {
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
      XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
      return decode(xdrDataInputStream);
    }
  }
}
