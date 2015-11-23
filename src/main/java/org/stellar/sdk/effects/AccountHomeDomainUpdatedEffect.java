package org.stellar.sdk.effects;

import com.google.gson.annotations.SerializedName;

public class AccountHomeDomainUpdatedEffect extends Effect {
  @SerializedName("home_domain")
  protected final String homeDomain;

  public AccountHomeDomainUpdatedEffect(String homeDomain) {
    this.homeDomain = homeDomain;
  }

  public String getHomeDomain() {
    return homeDomain;
  }
}
