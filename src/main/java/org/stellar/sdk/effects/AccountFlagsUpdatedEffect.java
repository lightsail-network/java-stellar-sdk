package org.stellar.sdk.effects;

import com.google.gson.annotations.SerializedName;

public class AccountFlagsUpdatedEffect extends Effect {
  @SerializedName("auth_required_flag")
  protected final Boolean authRequiredFlag;
  @SerializedName("auth_revokable_flag")
  protected final Boolean authRevokableFlag;

  public AccountFlagsUpdatedEffect(Boolean authRequiredFlag, Boolean authRevokableFlag) {
    this.authRequiredFlag = authRequiredFlag;
    this.authRevokableFlag = authRevokableFlag;
  }

  public Boolean getAuthRequiredFlag() {
    return authRequiredFlag;
  }

  public Boolean getAuthRevokableFlag() {
    return authRevokableFlag;
  }
}
