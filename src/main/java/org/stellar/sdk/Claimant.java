package org.stellar.sdk;

import lombok.NonNull;
import lombok.Value;

/** Represents an entity who is eligible to claim the claimable balance. */
@Value
public class Claimant {
  /** The destination account id. */
  @NonNull String destination;

  /** The predicate for this claimable balance. */
  @NonNull Predicate predicate;
}
