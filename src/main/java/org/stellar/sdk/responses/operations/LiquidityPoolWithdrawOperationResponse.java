package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.responses.AssetAmount;

/**
 * Represents LiquidityPoolWithdraw operation response.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/data/apis/horizon/api-reference/resources/operations/object/liquidity-pool-withdraw"
 *     target="_blank">Operation documentation</a>
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class LiquidityPoolWithdrawOperationResponse extends OperationResponse {
  @SerializedName("liquidity_pool_id")
  String liquidityPoolId;

  @SerializedName("reserves_min")
  List<AssetAmount> reservesMin;

  @SerializedName("reserves_received")
  List<AssetAmount> reservesReceived;

  @SerializedName("shares")
  String shares;
}
