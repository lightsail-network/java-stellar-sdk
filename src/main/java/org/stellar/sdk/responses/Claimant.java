package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import lombok.Value;
import org.stellar.sdk.Predicate;

/** Represents an entity who is eligible to claim the claimable balance. */
@Value
public class Claimant {
  /** The destination account id. */
  @SerializedName("destination")
  String destination;

  /** The predicate for this claimable balance. */
  @SerializedName("predicate")
  Predicate predicate;
}
