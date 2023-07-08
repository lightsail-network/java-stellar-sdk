package org.stellar.sdk;

import com.google.gson.annotations.SerializedName;

/** Represents an entity who is eligible to claim the claimable balance. */
public class Claimant {
  @SerializedName("destination")
  private final String destination;

  @SerializedName("predicate")
  private final Predicate predicate;

  public Claimant(String destination, Predicate predicate) {
    this.destination = destination;
    this.predicate = predicate;
  }

  public String getDestination() {
    return destination;
  }

  public Predicate getPredicate() {
    return predicate;
  }
}
