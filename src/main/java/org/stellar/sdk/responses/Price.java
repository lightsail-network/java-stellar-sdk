package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import lombok.Value;

/** Represents price. */
@Value
public class Price {
  /** numerator */
  @SerializedName("n")
  long numerator;

  /** denominator */
  @SerializedName("d")
  long denominator;
}
