package org.stellar.sdk;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import org.stellar.sdk.xdr.Int32;

/** Represents Price. Price in Stellar is represented as a fraction. */
public class Price {
  @SerializedName("n")
  private final int n;

  @SerializedName("d")
  private final int d;

  /**
   * Create a new price. Price in Stellar is represented as a fraction.
   *
   * @param n numerator
   * @param d denominator
   */
  public Price(int n, int d) {
    this.n = n;
    this.d = d;
  }

  /** Returns numerator. */
  public int getNumerator() {
    return n;
  }

  /** Returns denominator */
  public int getDenominator() {
    return d;
  }

  /**
   * Approximates <code>price</code> to a fraction. Please remember that this function can give
   * unexpected results for values that cannot be represented as a fraction with 32-bit numerator
   * and denominator. It's safer to create a Price object using the constructor.
   *
   * @param price Ex. "1.25"
   */
  public static Price fromString(String price) {
    checkNotNull(price, "price cannot be null");
    BigDecimal maxInt = new BigDecimal(Integer.MAX_VALUE);
    BigDecimal number = new BigDecimal(price);
    BigDecimal a;
    BigDecimal f;
    List<BigDecimal[]> fractions = new ArrayList<BigDecimal[]>();
    fractions.add(new BigDecimal[] {new BigDecimal(0), new BigDecimal(1)});
    fractions.add(new BigDecimal[] {new BigDecimal(1), new BigDecimal(0)});
    int i = 2;
    while (true) {
      if (number.compareTo(maxInt) > 0) {
        break;
      }
      a = number.setScale(0, BigDecimal.ROUND_FLOOR);
      f = number.subtract(a);
      BigDecimal h = a.multiply(fractions.get(i - 1)[0]).add(fractions.get(i - 2)[0]);
      BigDecimal k = a.multiply(fractions.get(i - 1)[1]).add(fractions.get(i - 2)[1]);
      if (h.compareTo(maxInt) > 0 || k.compareTo(maxInt) > 0) {
        break;
      }
      fractions.add(new BigDecimal[] {h, k});
      if (f.compareTo(BigDecimal.ZERO) == 0) {
        break;
      }
      number = new BigDecimal(1).divide(f, 20, BigDecimal.ROUND_HALF_UP);
      i = i + 1;
    }
    BigDecimal n = fractions.get(fractions.size() - 1)[0];
    BigDecimal d = fractions.get(fractions.size() - 1)[1];
    return new Price(n.intValue(), d.intValue());
  }

  /** Generates a Price SDK object from the XDR representation. */
  public static Price fromXdr(org.stellar.sdk.xdr.Price price) {
    return new Price(price.getN().getInt32().intValue(), price.getD().getInt32().intValue());
  }

  /** Generates Price XDR object. */
  public org.stellar.sdk.xdr.Price toXdr() {
    org.stellar.sdk.xdr.Price xdr = new org.stellar.sdk.xdr.Price();
    Int32 n = new Int32();
    Int32 d = new Int32();
    n.setInt32(this.n);
    d.setInt32(this.d);
    xdr.setN(n);
    xdr.setD(d);
    return xdr;
  }

  /** Returns price as a string. */
  public String toString() {
    MathContext mc = MathContext.DECIMAL64;
    BigDecimal result = new BigDecimal(this.n).divide(new BigDecimal(this.d), mc);

    return result.toString();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.getNumerator(), this.getDenominator());
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Price)) {
      return false;
    }

    Price other = (Price) object;
    return this.getNumerator() == other.getNumerator()
        && this.getDenominator() == other.getDenominator();
  }
}
