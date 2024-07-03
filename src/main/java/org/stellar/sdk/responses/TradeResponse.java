package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.Asset;
import org.stellar.sdk.LiquidityPoolId;

/**
 * Represents trades response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/trades/" target="_blank">Trades
 *     documentation</a>
 * @see org.stellar.sdk.requests.TradesRequestBuilder
 * @see org.stellar.sdk.Server#trades()
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class TradeResponse extends Response implements Pageable {
  @SerializedName("id")
  String id;

  @SerializedName("paging_token")
  String pagingToken;

  @SerializedName("ledger_close_time")
  String ledgerCloseTime;

  @SerializedName("offer_id")
  Long offerId;

  @SerializedName("trade_type")
  String tradeType;

  @SerializedName("liquidity_pool_fee_bp")
  Long liquidityPoolFeeBP;

  @SerializedName("base_liquidity_pool_id")
  LiquidityPoolId baseLiquidityPoolId;

  @SerializedName("base_offer_id")
  Long baseOfferId;

  @SerializedName("base_account")
  String baseAccount;

  @SerializedName("base_amount")
  String baseAmount;

  @SerializedName("base_asset_type")
  String baseAssetType;

  @SerializedName("base_asset_code")
  String baseAssetCode;

  @SerializedName("base_asset_issuer")
  String baseAssetIssuer;

  @SerializedName("counter_liquidity_pool_id")
  LiquidityPoolId counterLiquidityPoolId;

  @SerializedName("counter_offer_id")
  Long counterOfferId;

  @SerializedName("counter_account")
  String counterAccount;

  @SerializedName("counter_amount")
  String counterAmount;

  @SerializedName("counter_asset_type")
  String counterAssetType;

  @SerializedName("counter_asset_code")
  String counterAssetCode;

  @SerializedName("counter_asset_issuer")
  String counterAssetIssuer;

  @SerializedName("base_is_seller")
  boolean baseIsSeller;

  @SerializedName("price")
  TradePrice price;

  @SerializedName("_links")
  TradeResponse.Links links;

  public boolean isBaseSeller() {
    return baseIsSeller;
  }

  public Optional<Long> getBaseOfferId() {
    return Optional.ofNullable(baseOfferId);
  }

  public Optional<String> getBaseAccount() {
    return Optional.ofNullable(baseAccount);
  }

  public Optional<LiquidityPoolId> getBaseLiquidityPoolId() {
    return Optional.ofNullable(baseLiquidityPoolId);
  }

  public Asset getBaseAsset() {
    return Asset.create(this.baseAssetType, this.baseAssetCode, this.baseAssetIssuer);
  }

  public Optional<String> getCounterAccount() {
    return Optional.ofNullable(counterAccount);
  }

  public Optional<LiquidityPoolId> getCounterLiquidityPoolId() {
    return Optional.ofNullable(counterLiquidityPoolId);
  }

  public Optional<Long> getCounterOfferId() {
    return Optional.ofNullable(counterOfferId);
  }

  public Asset getCounterAsset() {
    return Asset.create(this.counterAssetType, this.counterAssetCode, this.counterAssetIssuer);
  }

  /** Links connected to a trade. */
  @Value
  public static class Links {
    @SerializedName("base")
    Link base;

    @SerializedName("counter")
    Link counter;

    @SerializedName("operation")
    Link operation;
  }
}
