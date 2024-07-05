package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Represents root endpoint response.
 *
 * @see org.stellar.sdk.Server#root()
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class RootResponse extends Response {
  @SerializedName("horizon_version")
  String horizonVersion;

  @SerializedName("core_version")
  String stellarCoreVersion;

  @SerializedName("ingest_latest_ledger")
  Long ingestLatestLedger;

  @SerializedName("history_latest_ledger")
  Long historyLatestLedger;

  @SerializedName("history_latest_ledger_closed_at")
  String historyLatestLedgerClosedAt;

  @SerializedName("history_elder_ledger")
  Long historyElderLedger;

  @SerializedName("core_latest_ledger")
  Long coreLatestLedger;

  @SerializedName("network_passphrase")
  String networkPassphrase;

  @SerializedName("current_protocol_version")
  Integer currentProtocolVersion;

  @SerializedName("supported_protocol_version")
  Long supportedProtocolVersion;

  @SerializedName("core_supported_protocol_version")
  Integer coreSupportedProtocolVersion;

  @SerializedName("_links")
  Links links;

  @Value
  public static class Links {

    @SerializedName("account")
    Link account;

    @SerializedName("accounts")
    Link accounts;

    @SerializedName("account_transactions")
    Link accountTransactions;

    @SerializedName("claimable_balances")
    Link claimableBalances;

    @SerializedName("assets")
    Link assets;

    @SerializedName("effects")
    Link effects;

    @SerializedName("fee_stats")
    Link feeStats;

    @SerializedName("friendbot")
    Link friendbot;

    @SerializedName("ledger")
    Link ledger;

    @SerializedName("ledgers")
    Link ledgers;

    @SerializedName("liquidity_pools")
    Link liquidityPools;

    @SerializedName("offer")
    Link offer;

    @SerializedName("offers")
    Link offers;

    @SerializedName("operation")
    Link operation;

    @SerializedName("operations")
    Link operations;

    @SerializedName("order_book")
    Link orderBook;

    @SerializedName("payments")
    Link payments;

    @SerializedName("self")
    Link self;

    @SerializedName("strict_receive_paths")
    Link strictReceivePaths;

    @SerializedName("strict_send_paths")
    Link strictSendPaths;

    @SerializedName("trade_aggregations")
    Link tradeAggregations;

    @SerializedName("trades")
    Link trades;

    @SerializedName("transaction")
    Link transaction;

    @SerializedName("transactions")
    Link transactions;
  }
}
