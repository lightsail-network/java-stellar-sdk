package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;
import org.stellar.sdk.AssetAmount;
import org.stellar.sdk.LiquidityPoolID;
import org.stellar.sdk.xdr.LiquidityPoolType;

public class LiquidityPool {
  @SerializedName("id")
  protected final LiquidityPoolID id;

  @SerializedName("fee_bp")
  protected final Integer feeBP;

  @SerializedName("type")
  protected final LiquidityPoolType type;

  @SerializedName("total_trustlines")
  protected final Long totalTrustlines;

  @SerializedName("total_shares")
  protected final String totalShares;

  @SerializedName("reserves")
  protected final AssetAmount[] reserves;

  public LiquidityPool(
      LiquidityPoolID id,
      Integer feeBP,
      LiquidityPoolType type,
      Long totalTrustlines,
      String totalShares,
      AssetAmount[] reserves) {
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

  public LiquidityPoolType getType() {
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
