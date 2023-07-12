package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import org.stellar.sdk.Asset;

public class AssetResponse extends Response implements Pageable {
  @SerializedName("asset_type")
  private final String assetType;

  @SerializedName("asset_code")
  private final String assetCode;

  @SerializedName("asset_issuer")
  private final String assetIssuer;

  @SerializedName("paging_token")
  private final String pagingToken;

  @SerializedName("accounts")
  private final AssetResponse.Accounts accounts;

  @SerializedName("balances")
  private final AssetResponse.Balances balances;

  @SerializedName("amount")
  private final String amount;

  @SerializedName("claimable_balances_amount")
  private final String claimableBalancesAmount;

  @SerializedName("liquidity_pools_amount")
  private final String liquidityPoolsAmount;

  @SerializedName("num_accounts")
  private final int numAccounts;

  @SerializedName("num_claimable_balances")
  private final int numClaimableBalances;

  @SerializedName("num_liquidity_pools")
  private final int numLiquidityPools;

  @SerializedName("flags")
  private final AssetResponse.Flags flags;

  @SerializedName("_links")
  private final AssetResponse.Links links;

  public AssetResponse(
      String assetType,
      String assetCode,
      String assetIssuer,
      String pagingToken,
      Accounts accounts,
      Balances balances,
      String amount,
      String claimableBalancesAmount,
      String liquidityPoolsAmount,
      int numAccounts,
      int numClaimableBalances,
      int numLiquidityPools,
      Flags flags,
      Links links) {
    this.assetType = assetType;
    this.assetCode = assetCode;
    this.assetIssuer = assetIssuer;
    this.pagingToken = pagingToken;
    this.accounts = accounts;
    this.balances = balances;
    this.amount = amount;
    this.claimableBalancesAmount = claimableBalancesAmount;
    this.liquidityPoolsAmount = liquidityPoolsAmount;
    this.numAccounts = numAccounts;
    this.numClaimableBalances = numClaimableBalances;
    this.numLiquidityPools = numLiquidityPools;
    this.flags = flags;
    this.links = links;
  }

  public String getAssetType() {
    return assetType;
  }

  public String getAssetCode() {
    return assetCode;
  }

  public String getAssetIssuer() {
    return assetIssuer;
  }

  public Asset getAsset() {
    return Asset.create(this.assetType, this.assetCode, this.assetIssuer);
  }

  public String getPagingToken() {
    return pagingToken;
  }

  public Accounts getAccounts() {
    return accounts;
  }

  public Balances getBalances() {
    return balances;
  }

  public String getClaimableBalancesAmount() {
    return claimableBalancesAmount;
  }

  public String getLiquidityPoolsAmount() {
    return liquidityPoolsAmount;
  }

  public int getNumClaimableBalances() {
    return numClaimableBalances;
  }

  public int getNumLiquidityPools() {
    return numLiquidityPools;
  }

  public String getAmount() {
    return amount;
  }

  public int getNumAccounts() {
    return numAccounts;
  }

  public Flags getFlags() {
    return flags;
  }

  public Links getLinks() {
    return links;
  }

  /** Accounts describe asset accounts. */
  public static class Accounts {
    @SerializedName("authorized")
    private final int authorized;

    @SerializedName("authorized_to_maintain_liabilities")
    private final int authorizedToMaintainLiabilities;

    @SerializedName("unauthorized")
    private final int unauthorized;

    public Accounts(int authorized, int authorizedToMaintainLiabilities, int unauthorized) {
      this.authorized = authorized;
      this.authorizedToMaintainLiabilities = authorizedToMaintainLiabilities;
      this.unauthorized = unauthorized;
    }

    public int authorized() {
      return authorized;
    }

    public int authorizedToMaintainLiabilities() {
      return authorizedToMaintainLiabilities;
    }

    public int unauthorized() {
      return unauthorized;
    }
  }

  /** Balances describe asset balances. */
  public static class Balances {
    @SerializedName("authorized")
    private final String authorized;

    @SerializedName("authorized_to_maintain_liabilities")
    private final String authorizedToMaintainLiabilities;

    @SerializedName("unauthorized")
    private final String unauthorized;

    public Balances(
        String authorized, String authorizedToMaintainLiabilities, String unauthorized) {
      this.authorized = authorized;
      this.authorizedToMaintainLiabilities = authorizedToMaintainLiabilities;
      this.unauthorized = unauthorized;
    }

    public String authorized() {
      return authorized;
    }

    public String authorizedToMaintainLiabilities() {
      return authorizedToMaintainLiabilities;
    }

    public String unauthorized() {
      return unauthorized;
    }
  }

  /** Flags describe asset flags. */
  public static class Flags {
    @SerializedName("auth_required")
    private final boolean authRequired;

    @SerializedName("auth_revocable")
    private final boolean authRevocable;

    public Flags(boolean authRequired, boolean authRevocable) {
      this.authRequired = authRequired;
      this.authRevocable = authRevocable;
    }

    public boolean isAuthRequired() {
      return authRequired;
    }

    public boolean isAuthRevocable() {
      return authRevocable;
    }
  }

  /** Links connected to asset. */
  public static class Links {
    @SerializedName("toml")
    private final Link toml;

    public Links(Link toml) {
      this.toml = toml;
    }

    public Link getToml() {
      return toml;
    }
  }
}
