// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

  public static void encode(XdrDataOutputStream stream, StellarValue encodedStellarValue)
      throws IOException {
    Hash.encode(stream, encodedStellarValue.txSetHash);
    TimePoint.encode(stream, encodedStellarValue.closeTime);
    int upgradessize = encodedStellarValue.getUpgrades().length;
    stream.writeInt(upgradessize);
    for (int i = 0; i < upgradessize; i++) {
      UpgradeType.encode(stream, encodedStellarValue.upgrades[i]);
    }
    StellarValueExt.encode(stream, encodedStellarValue.ext);
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static StellarValue decode(XdrDataInputStream stream) throws IOException {
    StellarValue decodedStellarValue = new StellarValue();
    decodedStellarValue.txSetHash = Hash.decode(stream);
    decodedStellarValue.closeTime = TimePoint.decode(stream);
    int upgradessize = stream.readInt();
    decodedStellarValue.upgrades = new UpgradeType[upgradessize];
    for (int i = 0; i < upgradessize; i++) {
      decodedStellarValue.upgrades[i] = UpgradeType.decode(stream);
    }
    decodedStellarValue.ext = StellarValueExt.decode(stream);
    return decodedStellarValue;
  }

  @Override
  public String toXdrBase64() throws IOException {
    return Base64Factory.getInstance().encodeToString(toXdrByteArray());
  }

  @Override
  public byte[] toXdrByteArray() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    encode(xdrDataOutputStream);
    return byteArrayOutputStream.toByteArray();
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

    public static void encode(XdrDataOutputStream stream, StellarValueExt encodedStellarValueExt)
        throws IOException {
      // Xdrgen::AST::Identifier
      // StellarValueType
      stream.writeInt(encodedStellarValueExt.getDiscriminant().getValue());
      switch (encodedStellarValueExt.getDiscriminant()) {
        case STELLAR_VALUE_BASIC:
          break;
        case STELLAR_VALUE_SIGNED:
          LedgerCloseValueSignature.encode(stream, encodedStellarValueExt.lcValueSignature);
          break;
      }
    }

    public void encode(XdrDataOutputStream stream) throws IOException {
      encode(stream, this);
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

    @Override
    public String toXdrBase64() throws IOException {
      return Base64Factory.getInstance().encodeToString(toXdrByteArray());
    }

    @Override
    public byte[] toXdrByteArray() throws IOException {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
      encode(xdrDataOutputStream);
      return byteArrayOutputStream.toByteArray();
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
