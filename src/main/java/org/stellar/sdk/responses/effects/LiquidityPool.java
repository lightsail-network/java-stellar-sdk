package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Value;
import org.stellar.sdk.AssetAmount;
import org.stellar.sdk.LiquidityPoolID;

@Value
public class LiquidityPool {
  @SerializedName("id")
  LiquidityPoolID id;

  @SerializedName("fee_bp")
  Integer feeBP;

  @SerializedName("type")
  String type;

  @SerializedName("total_trustlines")
  Long totalTrustlines;

  @SerializedName("total_shares")
  String totalShares;

  @SerializedName("reserves")
  List<AssetAmount> reserves;
}
