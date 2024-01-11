package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.AssetAmount;
import org.stellar.sdk.LiquidityPoolID;

@Value
@EqualsAndHashCode(callSuper = true)
public class LiquidityPoolWithdrawOperationResponse extends OperationResponse {
  @SerializedName("liquidity_pool_id")
  LiquidityPoolID liquidityPoolId;

  @SerializedName("reserves_min")
  AssetAmount[] reservesMin;

  @SerializedName("reserves_received")
  AssetAmount[] reservesReceived;

  @SerializedName("shares")
  String shares;
}
