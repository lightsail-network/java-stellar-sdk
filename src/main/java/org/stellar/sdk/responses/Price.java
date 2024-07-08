package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import lombok.Value;

// NOTE: The n and d returned by horizon will exceed int32 ranges.

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
