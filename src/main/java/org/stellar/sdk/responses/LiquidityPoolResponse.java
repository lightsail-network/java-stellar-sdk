package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;
import org.stellar.sdk.Asset;
import org.stellar.sdk.LiquidityPoolID;
import org.stellar.sdk.xdr.LiquidityPoolType;

/**
 * Represents liquidity pool response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/liquiditypools/"
 *     target="_blank">Liquidity Pool documentation</a>
 * @see org.stellar.sdk.requests.LiquidityPoolsRequestBuilder
 * @see org.stellar.sdk.Server#liquidityPools()
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class LiquidityPoolResponse extends Response {
  @SerializedName("id")
  LiquidityPoolID id;

  @SerializedName("paging_token")
  String pagingToken;

  @SerializedName("fee_bp")
  Integer feeBP;

  @SerializedName("type")
  LiquidityPoolType type;

  @SerializedName("total_trustlines")
  Long totalTrustlines;

  @SerializedName("total_shares")
  String totalShares;

  @SerializedName("reserves")
  Reserve[] reserves;

  @SerializedName("_links")
  Links links;

  public LiquidityPoolID getID() {
    // For backwards compatibility
    return id;
  }

  /** Represents liquidity pool reserves. */
  @Value
  @AllArgsConstructor
  public static class Reserve {
    @SerializedName("amount")
    String amount;

    @SerializedName("asset")
    Asset asset;

    public Reserve(@NonNull String amount, @NonNull String asset) {
      this(amount, Asset.create(asset));
    }
  }

  /** Links connected to account. */
  @Value
  public static class Links {
    @SerializedName("effects")
    Link effects;

    @SerializedName("operations")
    Link operations;

    @SerializedName("self")
    Link self;

    @SerializedName("transactions")
    Link transactions;
  }
}
