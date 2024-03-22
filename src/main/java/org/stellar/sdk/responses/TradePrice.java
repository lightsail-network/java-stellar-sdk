package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;
import java.math.MathContext;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

/** Represents Price. Price in Stellar is represented as a fraction. */
@EqualsAndHashCode
@AllArgsConstructor
public class TradePrice {
  /** numerator */
  @SerializedName("n")
  Long n;

  /** denominator */
  @SerializedName("d")
  Long d;

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
}
