package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;

import org.stellar.sdk.Asset;
import org.stellar.sdk.LiquidityPoolID;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents liquidity pool response.
 * @see <a href="https://www.stellar.org/developers/horizon/reference/resources/liquiditypool.html" target="_blank">Liquidity Pool documentation</a>
 * @see org.stellar.sdk.requests.LiquidityPoolsRequestBuilder
 * @see org.stellar.sdk.Server#liquidityPools()
 */
public class LiquidityPoolResponse extends Response {
  @SerializedName("id")
  private LiquidityPoolID liquidityPoolID;
  @SerializedName("paging_token")
  private String pagingToken;
  @SerializedName("fee_bp")
  private Integer feeBP;
  @SerializedName("type")
  private String type;
  @SerializedName("total_trustlines")
  private String totalTrustlines;
  @SerializedName("total_shares")
  private String totalShares;
  @SerializedName("reserves")
  private Reserve[] reserves;
  @SerializedName("_links")
  private Links links;

  LiquidityPoolResponse(LiquidityPoolID liquidityPoolID) {
    this.liquidityPoolID = liquidityPoolID;
  }

  public LiquidityPoolID getLiquidityPoolID() {
    return liquidityPoolID;
  }

  public String getPagingToken() {
    return pagingToken;
  }

  public Integer getFeeBP() {
    return feeBP;
  }

  public String getType() {
    return type;
  }

  public String getTotalTrustlines() {
    return totalTrustlines;
  }

  public String getTotalShares() {
    return totalShares;
  }

  public Reserve[] getReserves() {
    return reserves;
  }

  /**
   * Represents liquidity pool reserves.
   */
  public static class Reserve {
    @SerializedName("amount")
    private final String amount;
    @SerializedName("asset")
    private final String asset;

    Reserve(String amount, String asset) {
      this.amount = checkNotNull(amount, "amount cannot be null");
      this.asset = checkNotNull(asset, "asset cannot be null");
    }

    public Asset getAsset() {
      return Asset.create(asset);
    }

    public String getAmount() {
      return amount;
    }
  }

  public Links getLinks() {
    return links;
  }

  /**
   * Links connected to account.
   */
  public static class Links {
    @SerializedName("effects")
    private final Link effects;
    @SerializedName("operations")
    private final Link operations;
    @SerializedName("self")
    private final Link self;
    @SerializedName("transactions")
    private final Link transactions;

    Links(Link effects, Link operations, Link self, Link transactions) {
      this.effects = effects;
      this.operations = operations;
      this.self = self;
      this.transactions = transactions;
    }

    public Link getEffects() {
      return effects;
    }

    public Link getOperations() {
      return operations;
    }

    public Link getSelf() {
      return self;
    }

    public Link getTransactions() {
      return transactions;
    }
  }
}
