package org.stellar.sdk;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;

public final class AssetAmount {
  @SerializedName("asset")
  private final Asset asset;

  @SerializedName("amount")
  private final String amount;

  public AssetAmount(Asset asset, String amount) {
    this.asset = asset;
    this.amount = amount;
  }

  public Asset getAsset() {
    return this.asset;
  }

  public String getAmount() {
    return this.amount;
  }

  public int hashCode() {
    return Objects.hashCode(asset, amount);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof AssetAmount)) {
      return false;
    }

    AssetAmount o = (AssetAmount) object;
    return Objects.equal(this.getAsset(), o.getAsset())
        && Objects.equal(this.getAmount(), o.getAmount());
  }
}
