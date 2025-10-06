package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.responses.AssetAmount;
import org.stellar.sdk.responses.Price;

/**
 * Represents LiquidityPoolDeposit operation response.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/data/apis/horizon/api-reference/resources/operations/object/liquidity-pool-deposit"
 *     target="_blank">Operation documentation</a>
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class LiquidityPoolDepositOperationResponse extends OperationResponse {
  @SerializedName("liquidity_pool_id")
  String liquidityPoolId;

  @SerializedName("reserves_max")
  List<AssetAmount> reservesMax;

  @SerializedName("min_price")
  String minPrice;

  @SerializedName("min_price_r")
  Price minPriceR;

  @SerializedName("max_price")
  String maxPrice;

  @SerializedName("max_price_r")
  Price maxPriceR;

  @SerializedName("reserves_deposited")
  List<AssetAmount> reservesDeposited;

  @SerializedName("shares_received")
  String sharesReceived;
}
