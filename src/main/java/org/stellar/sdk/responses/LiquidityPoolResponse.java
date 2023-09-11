package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import java.util.Objects;
import lombok.NonNull;
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
public class LiquidityPoolResponse extends Response {
  @SerializedName("id")
  private LiquidityPoolID id;

  @SerializedName("paging_token")
  private String pagingToken;

  @SerializedName("fee_bp")
  private Integer feeBP;

  @SerializedName("type")
  private LiquidityPoolType type;

  @SerializedName("total_trustlines")
  private Long totalTrustlines;

  @SerializedName("total_shares")
  private String totalShares;

  @SerializedName("reserves")
  private Reserve[] reserves;

  @SerializedName("_links")
  private Links links;

  LiquidityPoolResponse(LiquidityPoolID id) {
    this.id = id;
  }

  public LiquidityPoolID getID() {
    return id;
  }

  public String getPagingToken() {
    return pagingToken;
  }

  public Integer getFeeBP() {
    return feeBP;
  }

  public LiquidityPoolType getType() {
    return type;
  }

  public Long getTotalTrustlines() {
    return totalTrustlines;
  }

  public String getTotalShares() {
    return totalShares;
  }

  public Reserve[] getReserves() {
    return reserves;
  }

  /** Represents liquidity pool reserves. */
  public static class Reserve {
    @SerializedName("amount")
    private final String amount;

    @SerializedName("asset")
    private final Asset asset;

    public Reserve(@NonNull String amount, @NonNull String asset) {
      this.amount = amount;
      this.asset = Asset.create(asset);
    }

    public Reserve(@NonNull String amount, @NonNull Asset asset) {
      this.amount = amount;
      this.asset = asset;
    }

    public Asset getAsset() {
      return asset;
    }

    public String getAmount() {
      return amount;
    }

    @Override
    public boolean equals(Object other) {
      if (!(other instanceof LiquidityPoolResponse.Reserve)) {
        return false;
      }

      LiquidityPoolResponse.Reserve o = (LiquidityPoolResponse.Reserve) other;
      return Objects.equals(this.getAsset(), o.getAsset())
          && Objects.equals(this.getAmount(), o.getAmount());
    }
  }

  public Links getLinks() {
    return links;
  }

  /** Links connected to account. */
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
