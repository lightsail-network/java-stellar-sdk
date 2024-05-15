// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
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
public class ClaimAtom implements XdrElement {
  public ClaimAtom() {}

  ClaimAtomType type;

  public ClaimAtomType getDiscriminant() {
    return this.type;
  }

  public void setDiscriminant(ClaimAtomType value) {
    this.type = value;
  }

  private ClaimOfferAtomV0 v0;

  public ClaimOfferAtomV0 getV0() {
    return this.v0;
  }

  public void setV0(ClaimOfferAtomV0 value) {
    this.v0 = value;
  }

  private ClaimOfferAtom orderBook;

  public ClaimOfferAtom getOrderBook() {
    return this.orderBook;
  }

  public void setOrderBook(ClaimOfferAtom value) {
    this.orderBook = value;
  }

  private ClaimLiquidityAtom liquidityPool;

  public ClaimLiquidityAtom getLiquidityPool() {
    return this.liquidityPool;
  }

  public void setLiquidityPool(ClaimLiquidityAtom value) {
    this.liquidityPool = value;
  }

  public static final class Builder {
    private ClaimAtomType discriminant;
    private ClaimOfferAtomV0 v0;
    private ClaimOfferAtom orderBook;
    private ClaimLiquidityAtom liquidityPool;

    public Builder discriminant(ClaimAtomType discriminant) {
      this.discriminant = discriminant;
      return this;
    }

    public Builder v0(ClaimOfferAtomV0 v0) {
      this.v0 = v0;
      return this;
    }

    public Builder orderBook(ClaimOfferAtom orderBook) {
      this.orderBook = orderBook;
      return this;
    }

    public Builder liquidityPool(ClaimLiquidityAtom liquidityPool) {
      this.liquidityPool = liquidityPool;
      return this;
    }

    public ClaimAtom build() {
      ClaimAtom val = new ClaimAtom();
      val.setDiscriminant(discriminant);
      val.setV0(this.v0);
      val.setOrderBook(this.orderBook);
      val.setLiquidityPool(this.liquidityPool);
      return val;
    }
  }

  public static void encode(XdrDataOutputStream stream, ClaimAtom encodedClaimAtom)
      throws IOException {
    // Xdrgen::AST::Identifier
    // ClaimAtomType
    stream.writeInt(encodedClaimAtom.getDiscriminant().getValue());
    switch (encodedClaimAtom.getDiscriminant()) {
      case CLAIM_ATOM_TYPE_V0:
        ClaimOfferAtomV0.encode(stream, encodedClaimAtom.v0);
        break;
      case CLAIM_ATOM_TYPE_ORDER_BOOK:
        ClaimOfferAtom.encode(stream, encodedClaimAtom.orderBook);
        break;
      case CLAIM_ATOM_TYPE_LIQUIDITY_POOL:
        ClaimLiquidityAtom.encode(stream, encodedClaimAtom.liquidityPool);
        break;
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
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

  @Override
  public int hashCode() {
    return Objects.hash(this.v0, this.orderBook, this.liquidityPool, this.type);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof ClaimAtom)) {
      return false;
    }

    ClaimAtom other = (ClaimAtom) object;
    return Objects.equals(this.v0, other.v0)
        && Objects.equals(this.orderBook, other.orderBook)
        && Objects.equals(this.liquidityPool, other.liquidityPool)
        && Objects.equals(this.type, other.type);
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
