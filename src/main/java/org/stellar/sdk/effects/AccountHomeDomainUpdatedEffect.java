package org.stellar.sdk.effects;

import com.google.gson.annotations.SerializedName;

/**
 * Represents account_home_domain_updated effect response.
 */
public class AccountHomeDomainUpdatedEffect extends Effect {
  @SerializedName("home_domain")
  protected final String homeDomain;

  AccountHomeDomainUpdatedEffect(String homeDomain) {
    this.homeDomain = homeDomain;
  }

  public String getHomeDomain() {
    return homeDomain;
  }
}
