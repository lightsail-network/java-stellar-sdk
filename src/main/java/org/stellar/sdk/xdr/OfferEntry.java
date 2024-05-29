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
 * OfferEntry's original definition in the XDR file is:
 *
 * <pre>
 * struct OfferEntry
 * {
 *     AccountID sellerID;
 *     int64 offerID;
 *     Asset selling; // A
 *     Asset buying;  // B
 *     int64 amount;  // amount of A
 *
 *     /&#42; price for this offer:
 *         price of A in terms of B
 *         price=AmountB/AmountA=priceNumerator/priceDenominator
 *         price is after fees
 *     &#42;/
 *     Price price;
 *     uint32 flags; // see OfferEntryFlags
 *
 *     // reserved for future use
 *     union switch (int v)
 *     {
 *     case 0:
 *         void;
 *     }
 *     ext;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class OfferEntry implements XdrElement {
  private AccountID sellerID;
  private Int64 offerID;
  private Asset selling;
  private Asset buying;
  private Int64 amount;
  private Price price;
  private Uint32 flags;
  private OfferEntryExt ext;

  public void encode(XdrDataOutputStream stream) throws IOException {
    sellerID.encode(stream);
    offerID.encode(stream);
    selling.encode(stream);
    buying.encode(stream);
    amount.encode(stream);
    price.encode(stream);
    flags.encode(stream);
    ext.encode(stream);
  }

  public static OfferEntry decode(XdrDataInputStream stream) throws IOException {
    OfferEntry decodedOfferEntry = new OfferEntry();
    decodedOfferEntry.sellerID = AccountID.decode(stream);
    decodedOfferEntry.offerID = Int64.decode(stream);
    decodedOfferEntry.selling = Asset.decode(stream);
    decodedOfferEntry.buying = Asset.decode(stream);
    decodedOfferEntry.amount = Int64.decode(stream);
    decodedOfferEntry.price = Price.decode(stream);
    decodedOfferEntry.flags = Uint32.decode(stream);
    decodedOfferEntry.ext = OfferEntryExt.decode(stream);
    return decodedOfferEntry;
  }

  public static OfferEntry fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static OfferEntry fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }

  /**
   * OfferEntryExt's original definition in the XDR file is:
   *
   * <pre>
   * union switch (int v)
   *     {
   *     case 0:
   *         void;
   *     }
   * </pre>
   */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder(toBuilder = true)
  public static class OfferEntryExt implements XdrElement {
    private Integer discriminant;

    public void encode(XdrDataOutputStream stream) throws IOException {
      stream.writeInt(discriminant);
      switch (discriminant) {
        case 0:
          break;
      }
    }

    public static OfferEntryExt decode(XdrDataInputStream stream) throws IOException {
      OfferEntryExt decodedOfferEntryExt = new OfferEntryExt();
      Integer discriminant = stream.readInt();
      decodedOfferEntryExt.setDiscriminant(discriminant);
      switch (decodedOfferEntryExt.getDiscriminant()) {
        case 0:
          break;
      }
      return decodedOfferEntryExt;
    }

    public static OfferEntryExt fromXdrBase64(String xdr) throws IOException {
      byte[] bytes = Base64Factory.getInstance().decode(xdr);
      return fromXdrByteArray(bytes);
    }

    public static OfferEntryExt fromXdrByteArray(byte[] xdr) throws IOException {
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
      XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
      return decode(xdrDataInputStream);
    }
  }
}
