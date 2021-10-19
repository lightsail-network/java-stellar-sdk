package org.stellar.sdk.responses.effects;

import org.stellar.sdk.AssetAmount;
import org.stellar.sdk.LiquidityPoolID;

import com.google.gson.annotations.SerializedName;

public class LiquidityPool {
  @SerializedName("id")
  protected final LiquidityPoolID id;
  @SerializedName("fee_bp")
  protected final Integer feeBP;
  @SerializedName("type")
  protected final String type;
  @SerializedName("total_trustlines")
  protected final Long totalTrustlines;
  @SerializedName("total_shares")
  protected final String totalShares;
  @SerializedName("reserves")
  protected final AssetAmount[] reserves;

  public LiquidityPool(LiquidityPoolID id, Integer feeBP, String type, Long totalTrustlines, String totalShares, AssetAmount[] reserves) {
    this.id = id;
    this.feeBP = feeBP;
    this.type = type;
    this.totalTrustlines = totalTrustlines;
    this.totalShares = totalShares;
    this.reserves = reserves;
  }

  public LiquidityPoolID getID() {
    return id;
  }

  public Integer getFeeBP() {
    return feeBP;
  }

  public String getType() {
    return type;
  }

  public Long getTotalTrustlines() {
    return totalTrustlines;
  }

  public String getTotalShares() {
    return totalShares;
  }

  public AssetAmount[] getReserves() {
    return reserves;
  }
}
