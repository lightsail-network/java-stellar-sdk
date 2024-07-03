package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.AssetAmount;
import org.stellar.sdk.LiquidityPoolID;
import org.stellar.sdk.Price;

@Value
@EqualsAndHashCode(callSuper = true)
public class LiquidityPoolDepositOperationResponse extends OperationResponse {
  @SerializedName("liquidity_pool_id")
  LiquidityPoolID liquidityPoolId;

  @SerializedName("reserves_max")
  AssetAmount[] reservesMax;

  @SerializedName("min_price")
  String minPrice;

  @SerializedName("min_price_r")
  Price minPriceR;

  @SerializedName("max_price")
  String maxPrice;

  @SerializedName("max_price_r")
  Price maxPriceR;

  @SerializedName("reserves_deposited")
  AssetAmount[] reservesDeposited;

  @SerializedName("shares_received")
  String sharesReceived;
}
