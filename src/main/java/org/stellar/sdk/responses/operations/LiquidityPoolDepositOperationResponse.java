package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;
import java.util.Arrays;
import java.util.Objects;
import org.stellar.sdk.AssetAmount;
import org.stellar.sdk.LiquidityPoolID;
import org.stellar.sdk.Price;

public class LiquidityPoolDepositOperationResponse extends OperationResponse {
  @SerializedName("liquidity_pool_id")
  private final LiquidityPoolID liquidityPoolId;

  @SerializedName("reserves_max")
  private final AssetAmount[] reservesMax;

  @SerializedName("min_price")
  private final String minPrice;

  @SerializedName("min_price_r")
  private final Price minPriceR;

  @SerializedName("max_price")
  private final String maxPrice;

  @SerializedName("max_price_r")
  private final Price maxPriceR;

  @SerializedName("reserves_deposited")
  private final AssetAmount[] reservesDeposited;

  @SerializedName("shares_received")
  private final String sharesReceived;

  LiquidityPoolDepositOperationResponse(
      LiquidityPoolID liquidityPoolId,
      AssetAmount[] reservesMax,
      String minPrice,
      Price minPriceR,
      String maxPrice,
      Price maxPriceR,
      AssetAmount[] reservesDeposited,
      String sharesReceived) {
    this.liquidityPoolId = liquidityPoolId;
    this.reservesMax = reservesMax;
    this.minPrice = minPrice;
    this.minPriceR = minPriceR;
    this.maxPrice = maxPrice;
    this.maxPriceR = maxPriceR;
    this.reservesDeposited = reservesDeposited;
    this.sharesReceived = sharesReceived;
  }

  public LiquidityPoolID getLiquidityPoolId() {
    return liquidityPoolId;
  }

  public AssetAmount[] getReservesMax() {
    return reservesMax;
  }

  public String getMinPrice() {
    return minPrice;
  }

  public Price getMinPriceR() {
    return minPriceR;
  }

  public String getMaxPrice() {
    return maxPrice;
  }

  public Price getMaxPriceR() {
    return maxPriceR;
  }

  public AssetAmount[] getReservesDeposited() {
    return reservesDeposited;
  }

  public String getSharesReceived() {
    return sharesReceived;
  }

  public int hashCode() {
    return Objects.hash(
        liquidityPoolId,
        Arrays.hashCode(reservesMax),
        minPrice,
        minPriceR,
        maxPrice,
        maxPriceR,
        Arrays.hashCode(reservesDeposited),
        sharesReceived);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof LiquidityPoolDepositOperationResponse)) {
      return false;
    }

    LiquidityPoolDepositOperationResponse o = (LiquidityPoolDepositOperationResponse) object;
    return Objects.equals(this.getLiquidityPoolId(), o.getLiquidityPoolId())
        && Arrays.equals(this.getReservesMax(), o.getReservesMax())
        && Objects.equals(this.getMaxPrice(), o.getMaxPrice())
        && Objects.equals(this.getMinPrice(), o.getMinPrice())
        && Objects.equals(this.getMaxPriceR(), o.getMaxPriceR())
        && Objects.equals(this.getMinPriceR(), o.getMinPriceR())
        && Arrays.equals(this.getReservesDeposited(), o.getReservesDeposited())
        && Objects.equals(this.getSharesReceived(), o.getSharesReceived());
  }
}
