package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
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
  String contractId;

  @SerializedName("num_accounts")
  Integer numAccounts;

  @SerializedName("num_claimable_balances")
  Integer numClaimableBalances;

  @SerializedName("num_liquidity_pools")
  Integer numLiquidityPools;

  @SerializedName("num_contracts")
  Integer numContracts;

  @SerializedName("num_archived_contracts")
  Integer numArchivedContracts;

  @SerializedName("amount")
  String amount;

  @SerializedName("accounts")
  AssetResponse.Accounts accounts;

  @SerializedName("claimable_balances_amount")
  String claimableBalancesAmount;

  @SerializedName("liquidity_pools_amount")
  String liquidityPoolsAmount;

  @SerializedName("contracts_amount")
  String contractsAmount;

  @SerializedName("archived_contracts_amount")
  String archivedContractsAmount;

  @SerializedName("balances")
  AssetResponse.Balances balances;

  @SerializedName("flags")
  AssetResponse.Flags flags;

  @SerializedName("_links")
  AssetResponse.Links links;

  public Asset getAsset() {
    return Asset.create(this.assetType, this.assetCode, this.assetIssuer);
  }

  /** Accounts describe asset accounts. */
  @Value
  public static class Accounts {
    @SerializedName("authorized")
    Integer authorized;

    @SerializedName("authorized_to_maintain_liabilities")
    Integer authorizedToMaintainLiabilities;

    @SerializedName("unauthorized")
    Integer unauthorized;
  }

  /** Balances describe asset balances. */
  @Value
  public static class Balances {
    @SerializedName("authorized")
    String authorized;

    @SerializedName("authorized_to_maintain_liabilities")
    String authorizedToMaintainLiabilities;

    @SerializedName("unauthorized")
    String unauthorized;
  }

  /** Flags describe asset flags. */
  @Value
  public static class Flags {
    @SerializedName("auth_required")
    Boolean authRequired;

    @SerializedName("auth_revocable")
    Boolean authRevocable;

    @SerializedName("auth_immutable")
    Boolean authImmutable;

    @SerializedName("auth_clawback_enabled")
    Boolean authClawbackEnabled;
  }

  /** Links connected to asset. */
  @Value
  public static class Links {
    @SerializedName("toml")
    Link toml;
  }
}
