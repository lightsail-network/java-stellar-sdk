package org.stellar.sdk;

import com.google.gson.annotations.SerializedName;
import java.util.Objects;

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
    return Objects.hash(asset, amount);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof AssetAmount)) {
      return false;
    }

    AssetAmount o = (AssetAmount) object;
    return Objects.equals(this.getAsset(), o.getAsset())
        && Objects.equals(this.getAmount(), o.getAmount());
  }
}
