package org.stellar.sdk;

import lombok.NonNull;
import lombok.Value;

/** Represents an entity who is eligible to claim the claimable balance. */
@Value
public class Claimant {
  /**
   * The destination account id.
   *
   * @return the destination account ID
   */
  @NonNull String destination;

  /**
   * The predicate for this claimable balance.
   *
   * @return the claim predicate
   */
  @NonNull Predicate predicate;
}
