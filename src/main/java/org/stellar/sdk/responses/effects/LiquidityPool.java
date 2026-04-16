package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Value;
import org.stellar.sdk.responses.AssetAmount;

/**
 * Represents a liquidity pool within a Horizon effect response.
 *
 * <p>Contains the pool's identifier, fee (in basis points), type, total trustlines, total shares,
 * and the list of reserve asset amounts.
 */
@Value
public class LiquidityPool {
  @SerializedName("id")
  String id;

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
