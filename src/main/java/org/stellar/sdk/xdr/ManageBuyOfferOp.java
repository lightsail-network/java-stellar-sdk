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
 * ManageBuyOfferOp's original definition in the XDR file is:
 *
 * <pre>
 * struct ManageBuyOfferOp
 * {
 *     Asset selling;
 *     Asset buying;
 *     int64 buyAmount; // amount being bought. if set to 0, delete the offer
 *     Price price;     // price of thing being bought in terms of what you are
 *                      // selling
 *
 *     // 0=create a new offer, otherwise edit an existing offer
 *     int64 offerID;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ManageBuyOfferOp implements XdrElement {
  private Asset selling;
  private Asset buying;
  private Int64 buyAmount;
  private Price price;
  private Int64 offerID;

  public void encode(XdrDataOutputStream stream) throws IOException {
    selling.encode(stream);
    buying.encode(stream);
    buyAmount.encode(stream);
    price.encode(stream);
    offerID.encode(stream);
  }

  public static ManageBuyOfferOp decode(XdrDataInputStream stream) throws IOException {
    ManageBuyOfferOp decodedManageBuyOfferOp = new ManageBuyOfferOp();
    decodedManageBuyOfferOp.selling = Asset.decode(stream);
    decodedManageBuyOfferOp.buying = Asset.decode(stream);
    decodedManageBuyOfferOp.buyAmount = Int64.decode(stream);
    decodedManageBuyOfferOp.price = Price.decode(stream);
    decodedManageBuyOfferOp.offerID = Int64.decode(stream);
    return decodedManageBuyOfferOp;
  }

  public static ManageBuyOfferOp fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static ManageBuyOfferOp fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
