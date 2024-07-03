package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;
import lombok.Value;
import org.stellar.sdk.AssetAmount;
import org.stellar.sdk.LiquidityPoolId;
import org.stellar.sdk.xdr.LiquidityPoolType;

@Value
public class LiquidityPool {
  @SerializedName("id")
  LiquidityPoolId id;

  @SerializedName("fee_bp")
  Integer feeBP;

  @SerializedName("type")
  LiquidityPoolType type;

  @SerializedName("total_trustlines")
  Long totalTrustlines;

  @SerializedName("total_shares")
  String totalShares;

  @SerializedName("reserves")
  AssetAmount[] reserves;

  public LiquidityPoolId getID() {
    // For backwards compatibility
    return id;
  }
}
