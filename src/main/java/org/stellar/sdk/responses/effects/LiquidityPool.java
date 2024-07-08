package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Value;
import org.stellar.sdk.AssetAmount;
import org.stellar.sdk.LiquidityPoolID;
import org.stellar.sdk.xdr.LiquidityPoolType;

@Value
public class LiquidityPool {
  @SerializedName("id")
  LiquidityPoolID id;

  @SerializedName("fee_bp")
  Integer feeBP;

  // TODO: string?
  @SerializedName("type")
  LiquidityPoolType type;

  @SerializedName("total_trustlines")
  Long totalTrustlines;

  @SerializedName("total_shares")
  String totalShares;

  @SerializedName("reserves")
  List<AssetAmount> reserves;
}
