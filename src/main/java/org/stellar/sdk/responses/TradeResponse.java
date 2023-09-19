package org.stellar.sdk.responses;

import com.google.common.base.Optional;
import com.google.gson.annotations.SerializedName;
import org.stellar.sdk.Asset;
import org.stellar.sdk.LiquidityPoolID;

/**
 * Represents trades response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/trades/" target="_blank">Trades
 *     documentation</a>
 * @see org.stellar.sdk.requests.TradesRequestBuilder
 * @see org.stellar.sdk.Server#trades()
 */
public class TradeResponse extends Response implements Pageable {
  @SerializedName("id")
  private final String id;

  @SerializedName("paging_token")
  private final String pagingToken;

  @SerializedName("ledger_close_time")
  private final String ledgerCloseTime;

  @SerializedName("offer_id")
  private final Long offerId;

  @SerializedName("base_is_seller")
  protected final boolean baseIsSeller;

  @SerializedName("base_account")
  protected String baseAccount;

  @SerializedName("base_liquidity_pool_id")
  protected LiquidityPoolID baseLiquidityPoolID;

  @SerializedName("base_offer_id")
  private Long baseOfferId;

  @SerializedName("base_amount")
  protected final String baseAmount;

  @SerializedName("base_asset_type")
  protected final String baseAssetType;

  @SerializedName("base_asset_code")
  protected final String baseAssetCode;

  @SerializedName("base_asset_issuer")
  protected final String baseAssetIssuer;

  @SerializedName("counter_account")
  protected String counterAccount;

  @SerializedName("counter_liquidity_pool_id")
  protected LiquidityPoolID counterLiquidityPoolID;

  @SerializedName("counter_offer_id")
  private Long counterOfferId;

  @SerializedName("counter_amount")
  protected final String counterAmount;

  @SerializedName("counter_asset_type")
  protected final String counterAssetType;

  @SerializedName("counter_asset_code")
  protected final String counterAssetCode;

  @SerializedName("counter_asset_issuer")
  protected final String counterAssetIssuer;

  @SerializedName("price")
  protected final TradePrice price;

  @SerializedName("_links")
  private TradeResponse.Links links;

  public TradeResponse(
      String id,
      String pagingToken,
      String ledgerCloseTime,
      Long offerId,
      boolean baseIsSeller,
      String baseAccount,
      LiquidityPoolID baseLiquidityPoolID,
      Long baseOfferId,
      String baseAmount,
      String baseAssetType,
      String baseAssetCode,
      String baseAssetIssuer,
      String counterAccount,
      LiquidityPoolID counterLiquidityPoolID,
      Long counterOfferId,
      String counterAmount,
      String counterAssetType,
      String counterAssetCode,
      String counterAssetIssuer,
      TradePrice price) {
    this.id = id;
    this.pagingToken = pagingToken;
    this.ledgerCloseTime = ledgerCloseTime;
    this.offerId = offerId;
    this.baseIsSeller = baseIsSeller;
    this.baseAccount = baseAccount;
    this.baseLiquidityPoolID = baseLiquidityPoolID;
    this.baseOfferId = baseOfferId;
    this.baseAmount = baseAmount;
    this.baseAssetType = baseAssetType;
    this.baseAssetCode = baseAssetCode;
    this.baseAssetIssuer = baseAssetIssuer;
    this.counterAccount = counterAccount;
    this.counterLiquidityPoolID = counterLiquidityPoolID;
    this.counterOfferId = counterOfferId;
    this.counterAmount = counterAmount;
    this.counterAssetType = counterAssetType;
    this.counterAssetCode = counterAssetCode;
    this.counterAssetIssuer = counterAssetIssuer;
    this.price = price;
  }

  public String getId() {
    return id;
  }

  public String getPagingToken() {
    return pagingToken;
  }

  public String getLedgerCloseTime() {
    return ledgerCloseTime;
  }

  public Long getOfferId() {
    return offerId;
  }

  public boolean isBaseSeller() {
    return baseIsSeller;
  }

  public Optional<Long> getBaseOfferId() {
    return Optional.fromNullable(baseOfferId);
  }

  public Optional<String> getBaseAccount() {
    return Optional.fromNullable(baseAccount);
  }

  public Optional<LiquidityPoolID> getBaseLiquidityPoolID() {
    return Optional.fromNullable(baseLiquidityPoolID);
  }

  public String getBaseAmount() {
    return baseAmount;
  }

  public Asset getBaseAsset() {
    return Asset.create(this.baseAssetType, this.baseAssetCode, this.baseAssetIssuer);
  }

  public String getBaseAssetType() {
    return baseAssetType;
  }

  public String getBaseAssetCode() {
    return baseAssetCode;
  }

  public String getBaseAssetIssuer() {
    return baseAssetIssuer;
  }

  public Optional<String> getCounterAccount() {
    return Optional.fromNullable(counterAccount);
  }

  public Optional<LiquidityPoolID> getCounterLiquidityPoolID() {
    return Optional.fromNullable(counterLiquidityPoolID);
  }

  public Optional<Long> getCounterOfferId() {
    return Optional.fromNullable(counterOfferId);
  }

  public Asset getCounterAsset() {
    return Asset.create(this.counterAssetType, this.counterAssetCode, this.counterAssetIssuer);
  }

  public String getCounterAmount() {
    return counterAmount;
  }

  public String getCounterAssetType() {
    return counterAssetType;
  }

  public String getCounterAssetCode() {
    return counterAssetCode;
  }

  public String getCounterAssetIssuer() {
    return counterAssetIssuer;
  }

  public TradePrice getPrice() {
    return price;
  }

  public Links getLinks() {
    return links;
  }

  /** Links connected to a trade. */
  public static class Links {
    @SerializedName("base")
    private final Link base;

    @SerializedName("counter")
    private final Link counter;

    @SerializedName("operation")
    private final Link operation;

    public Links(Link base, Link counter, Link operation) {
      this.base = base;
      this.counter = counter;
      this.operation = operation;
    }

    public Link getBase() {
      return base;
    }

    public Link getCounter() {
      return counter;
    }

    public Link getOperation() {
      return operation;
    }
  }
}
