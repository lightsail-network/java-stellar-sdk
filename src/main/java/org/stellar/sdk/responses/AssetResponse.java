package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.Asset;

@Value
@EqualsAndHashCode(callSuper = false)
public class AssetResponse extends Response implements Pageable {
  @SerializedName("asset_type")
  String assetType;

  @SerializedName("asset_code")
  String assetCode;

  @SerializedName("asset_issuer")
  String assetIssuer;

  @SerializedName("paging_token")
  String pagingToken;

  @SerializedName("contract_id")
  String contractID;

  @SerializedName("accounts")
  AssetResponse.Accounts accounts;

  @SerializedName("balances")
  AssetResponse.Balances balances;

  @SerializedName("amount")
  String amount;

  @SerializedName("claimable_balances_amount")
  String claimableBalancesAmount;

  @SerializedName("liquidity_pools_amount")
  String liquidityPoolsAmount;

  @SerializedName("contracts_amount")
  String contractsAmount;

  @SerializedName("num_accounts")
  int numAccounts;

  @SerializedName("num_claimable_balances")
  int numClaimableBalances;

  @SerializedName("num_liquidity_pools")
  int numLiquidityPools;

  @SerializedName("num_contracts")
  int numContracts;

  @SerializedName("flags")
  AssetResponse.Flags flags;

  @SerializedName("_links")
  AssetResponse.Links links;

  public Asset getAsset() {
    return Asset.create(this.assetType, this.assetCode, this.assetIssuer);
  }

  /** Accounts describe asset accounts. */
  @EqualsAndHashCode
  @AllArgsConstructor
  public static class Accounts {
    @SerializedName("authorized")
    int authorized;

    @SerializedName("authorized_to_maintain_liabilities")
    int authorizedToMaintainLiabilities;

    @SerializedName("unauthorized")
    int unauthorized;

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
  @EqualsAndHashCode
  @AllArgsConstructor
  public static class Balances {
    @SerializedName("authorized")
    String authorized;

    @SerializedName("authorized_to_maintain_liabilities")
    String authorizedToMaintainLiabilities;

    @SerializedName("unauthorized")
    String unauthorized;

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
  @Value
  public static class Flags {
    @SerializedName("auth_required")
    boolean authRequired;

    @SerializedName("auth_revocable")
    boolean authRevocable;
  }

  /** Links connected to asset. */
  @Value
  public static class Links {
    @SerializedName("toml")
    Link toml;
  }
}
