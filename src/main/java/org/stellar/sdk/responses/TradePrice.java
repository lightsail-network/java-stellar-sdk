package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Objects;

/** Represents Price. Price in Stellar is represented as a fraction. */
public class TradePrice {
  @SerializedName("n")
  private final Long n;

  @SerializedName("d")
  private final Long d;

  /**
   * Create a new price. Price in Stellar is represented as a fraction.
   *
   * @param n numerator
   * @param d denominator
   */
  public TradePrice(Long n, Long d) {
    this.n = n;
    this.d = d;
  }

  /** Returns numerator. */
  public Long getNumerator() {
    return n;
  }

  /** Returns denominator */
  public Long getDenominator() {
    return d;
  }

  /** Returns price as a string. */
  public String toString() {
    MathContext mc = MathContext.DECIMAL64;
    BigDecimal result = new BigDecimal(this.n).divide(new BigDecimal(this.d), mc);

    return result.toString();
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getNumerator(), this.getDenominator());
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof TradePrice)) {
      return false;
    }

    TradePrice other = (TradePrice) object;
    return Objects.equals(this.getNumerator(), other.getNumerator())
        && Objects.equals(this.getDenominator(), other.getDenominator());
  }
}
