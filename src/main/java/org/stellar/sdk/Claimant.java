package org.stellar.sdk;

import com.google.gson.annotations.SerializedName;
import lombok.NonNull;
import lombok.Value;

// TODO: recheck the func
/** Represents an entity who is eligible to claim the claimable balance. */
@Value
public class Claimant {
  /** The destination account id. */
  @SerializedName("destination")
  @NonNull
  String destination;

  /** The predicate for this claimable balance. */
  @SerializedName("predicate")
  @NonNull
  Predicate predicate;
}
