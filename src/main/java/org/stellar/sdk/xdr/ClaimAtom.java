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
 * ClaimAtom's original definition in the XDR file is:
 *
 * <pre>
 * union ClaimAtom switch (ClaimAtomType type)
 * {
 * case CLAIM_ATOM_TYPE_V0:
 *     ClaimOfferAtomV0 v0;
 * case CLAIM_ATOM_TYPE_ORDER_BOOK:
 *     ClaimOfferAtom orderBook;
 * case CLAIM_ATOM_TYPE_LIQUIDITY_POOL:
 *     ClaimLiquidityAtom liquidityPool;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ClaimAtom implements XdrElement {
  private ClaimAtomType discriminant;
  private ClaimOfferAtomV0 v0;
  private ClaimOfferAtom orderBook;
  private ClaimLiquidityAtom liquidityPool;

  public void encode(XdrDataOutputStream stream) throws IOException {
    stream.writeInt(discriminant.getValue());
    switch (discriminant) {
      case CLAIM_ATOM_TYPE_V0:
        v0.encode(stream);
        break;
      case CLAIM_ATOM_TYPE_ORDER_BOOK:
        orderBook.encode(stream);
        break;
      case CLAIM_ATOM_TYPE_LIQUIDITY_POOL:
        liquidityPool.encode(stream);
        break;
    }
  }

  public static ClaimAtom decode(XdrDataInputStream stream) throws IOException {
    ClaimAtom decodedClaimAtom = new ClaimAtom();
    ClaimAtomType discriminant = ClaimAtomType.decode(stream);
    decodedClaimAtom.setDiscriminant(discriminant);
    switch (decodedClaimAtom.getDiscriminant()) {
      case CLAIM_ATOM_TYPE_V0:
        decodedClaimAtom.v0 = ClaimOfferAtomV0.decode(stream);
        break;
      case CLAIM_ATOM_TYPE_ORDER_BOOK:
        decodedClaimAtom.orderBook = ClaimOfferAtom.decode(stream);
        break;
      case CLAIM_ATOM_TYPE_LIQUIDITY_POOL:
        decodedClaimAtom.liquidityPool = ClaimLiquidityAtom.decode(stream);
        break;
    }
    return decodedClaimAtom;
  }

  public static ClaimAtom fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static ClaimAtom fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
