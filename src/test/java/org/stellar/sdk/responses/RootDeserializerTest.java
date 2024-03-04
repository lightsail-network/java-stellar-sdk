package org.stellar.sdk.responses;

import junit.framework.TestCase;
import org.junit.Test;

public class RootDeserializerTest extends TestCase {
  @Test
  public void testDeserialize() {
    RootResponse root = GsonSingleton.getInstance().fromJson(json, RootResponse.class);

    assertEquals(
        root.getLinks().getAccount().getHref(),
        "https://horizon-testnet.stellar.org/accounts/{account_id}");
    assertEquals(
        root.getLinks().getAccounts().getHref(),
        "https://horizon-testnet.stellar.org/accounts{?signer,sponsor,asset,liquidity_pool,cursor,limit,order}");
    assertEquals(
        root.getLinks().getAccountTransactions().getHref(),
        "https://horizon-testnet.stellar.org/accounts/{account_id}/transactions{?cursor,limit,order}");
    assertEquals(
        root.getLinks().getClaimableBalances().getHref(),
        "https://horizon-testnet.stellar.org/claimable_balances{?asset,sponsor,claimant,cursor,limit,order}");
    assertEquals(
        root.getLinks().getAssets().getHref(),
        "https://horizon-testnet.stellar.org/assets{?asset_code,asset_issuer,cursor,limit,order}");
    assertEquals(
        root.getLinks().getEffects().getHref(),
        "https://horizon-testnet.stellar.org/effects{?cursor,limit,order}");
    assertEquals(
        root.getLinks().getFeeStats().getHref(), "https://horizon-testnet.stellar.org/fee_stats");
    assertEquals(root.getLinks().getFriendbot().getHref(), "https://friendbot.stellar.org/{?addr}");
    assertEquals(
        root.getLinks().getLedger().getHref(),
        "https://horizon-testnet.stellar.org/ledgers/{sequence}");
    assertEquals(
        root.getLinks().getLedgers().getHref(),
        "https://horizon-testnet.stellar.org/ledgers{?cursor,limit,order}");
    assertEquals(
        root.getLinks().getLiquidityPools().getHref(),
        "https://horizon-testnet.stellar.org/liquidity_pools{?reserves,account,cursor,limit,order}");
    assertEquals(
        root.getLinks().getOffer().getHref(),
        "https://horizon-testnet.stellar.org/offers/{offer_id}");
    assertEquals(
        root.getLinks().getOffers().getHref(),
        "https://horizon-testnet.stellar.org/offers{?selling,buying,seller,sponsor,cursor,limit,order}");
    assertEquals(
        root.getLinks().getOperation().getHref(),
        "https://horizon-testnet.stellar.org/operations/{id}");
    assertEquals(
        root.getLinks().getOperations().getHref(),
        "https://horizon-testnet.stellar.org/operations{?cursor,limit,order,include_failed}");
    assertEquals(
        root.getLinks().getOrderBook().getHref(),
        "https://horizon-testnet.stellar.org/order_book{?selling_asset_type,selling_asset_code,selling_asset_issuer,buying_asset_type,buying_asset_code,buying_asset_issuer,limit}");
    assertEquals(
        root.getLinks().getPayments().getHref(),
        "https://horizon-testnet.stellar.org/payments{?cursor,limit,order,include_failed}");
    assertEquals(root.getLinks().getSelf().getHref(), "https://horizon-testnet.stellar.org/");
    assertEquals(
        root.getLinks().getStrictReceivePaths().getHref(),
        "https://horizon-testnet.stellar.org/paths/strict-receive{?source_assets,source_account,destination_account,destination_asset_type,destination_asset_issuer,destination_asset_code,destination_amount}");
    assertEquals(
        root.getLinks().getStrictSendPaths().getHref(),
        "https://horizon-testnet.stellar.org/paths/strict-send{?destination_account,destination_assets,source_asset_type,source_asset_issuer,source_asset_code,source_amount}");
    assertEquals(
        root.getLinks().getTradeAggregations().getHref(),
        "https://horizon-testnet.stellar.org/trade_aggregations?base_asset_type={base_asset_type}&base_asset_code={base_asset_code}&base_asset_issuer={base_asset_issuer}&counter_asset_type={counter_asset_type}&counter_asset_code={counter_asset_code}&counter_asset_issuer={counter_asset_issuer}");
    assertEquals(
        root.getLinks().getTrades().getHref(),
        "https://horizon-testnet.stellar.org/trades?base_asset_type={base_asset_type}&base_asset_code={base_asset_code}&base_asset_issuer={base_asset_issuer}&counter_asset_type={counter_asset_type}&counter_asset_code={counter_asset_code}&counter_asset_issuer={counter_asset_issuer}");
    assertEquals(
        root.getLinks().getTransaction().getHref(),
        "https://horizon-testnet.stellar.org/transactions/{hash}");
    assertEquals(
        root.getLinks().getTransactions().getHref(),
        "https://horizon-testnet.stellar.org/transactions{?cursor,limit,order}");
    assertEquals("2.28.0-93f9d706abadbe1594544093a7065665d26bc5cc", root.getHorizonVersion());
    assertEquals(
        "stellar-core 20.2.0.rc2 (368063acf47ddb85969dd6e5b5cc8d48c8b1342b)",
        root.getStellarCoreVersion());
    assertEquals(323105, root.getIngestLatestLedger().intValue());
    assertEquals(323105, root.getHistoryLatestLedger());
    assertEquals("2024-01-31T09:01:35Z", root.getHistoryLatestLedgerClosedAt());
    assertEquals(2, root.getHistoryElderLedger());
    assertEquals(323105, root.getCoreLatestLedger());
    assertEquals("Test SDF Network ; September 2015", root.getNetworkPassphrase());
    assertEquals(20, root.getCurrentProtocolVersion());
    assertEquals(20, root.getSupportedProtocolVersion().longValue());
    assertEquals(20, root.getCoreSupportedProtocolVersion());
  }

  String json =
      "{\n"
          + "  \"_links\": {\n"
          + "    \"account\": {\n"
          + "      \"href\": \"https://horizon-testnet.stellar.org/accounts/{account_id}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"accounts\": {\n"
          + "      \"href\": \"https://horizon-testnet.stellar.org/accounts{?signer,sponsor,asset,liquidity_pool,cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"account_transactions\": {\n"
          + "      \"href\": \"https://horizon-testnet.stellar.org/accounts/{account_id}/transactions{?cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"claimable_balances\": {\n"
          + "      \"href\": \"https://horizon-testnet.stellar.org/claimable_balances{?asset,sponsor,claimant,cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"assets\": {\n"
          + "      \"href\": \"https://horizon-testnet.stellar.org/assets{?asset_code,asset_issuer,cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"effects\": {\n"
          + "      \"href\": \"https://horizon-testnet.stellar.org/effects{?cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"fee_stats\": {\n"
          + "      \"href\": \"https://horizon-testnet.stellar.org/fee_stats\"\n"
          + "    },\n"
          + "    \"friendbot\": {\n"
          + "      \"href\": \"https://friendbot.stellar.org/{?addr}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"ledger\": {\n"
          + "      \"href\": \"https://horizon-testnet.stellar.org/ledgers/{sequence}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"ledgers\": {\n"
          + "      \"href\": \"https://horizon-testnet.stellar.org/ledgers{?cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"liquidity_pools\": {\n"
          + "      \"href\": \"https://horizon-testnet.stellar.org/liquidity_pools{?reserves,account,cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"offer\": {\n"
          + "      \"href\": \"https://horizon-testnet.stellar.org/offers/{offer_id}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"offers\": {\n"
          + "      \"href\": \"https://horizon-testnet.stellar.org/offers{?selling,buying,seller,sponsor,cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"operation\": {\n"
          + "      \"href\": \"https://horizon-testnet.stellar.org/operations/{id}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"operations\": {\n"
          + "      \"href\": \"https://horizon-testnet.stellar.org/operations{?cursor,limit,order,include_failed}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"order_book\": {\n"
          + "      \"href\": \"https://horizon-testnet.stellar.org/order_book{?selling_asset_type,selling_asset_code,selling_asset_issuer,buying_asset_type,buying_asset_code,buying_asset_issuer,limit}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"payments\": {\n"
          + "      \"href\": \"https://horizon-testnet.stellar.org/payments{?cursor,limit,order,include_failed}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"self\": {\n"
          + "      \"href\": \"https://horizon-testnet.stellar.org/\"\n"
          + "    },\n"
          + "    \"strict_receive_paths\": {\n"
          + "      \"href\": \"https://horizon-testnet.stellar.org/paths/strict-receive{?source_assets,source_account,destination_account,destination_asset_type,destination_asset_issuer,destination_asset_code,destination_amount}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"strict_send_paths\": {\n"
          + "      \"href\": \"https://horizon-testnet.stellar.org/paths/strict-send{?destination_account,destination_assets,source_asset_type,source_asset_issuer,source_asset_code,source_amount}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"trade_aggregations\": {\n"
          + "      \"href\": \"https://horizon-testnet.stellar.org/trade_aggregations?base_asset_type={base_asset_type}\\u0026base_asset_code={base_asset_code}\\u0026base_asset_issuer={base_asset_issuer}\\u0026counter_asset_type={counter_asset_type}\\u0026counter_asset_code={counter_asset_code}\\u0026counter_asset_issuer={counter_asset_issuer}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"trades\": {\n"
          + "      \"href\": \"https://horizon-testnet.stellar.org/trades?base_asset_type={base_asset_type}\\u0026base_asset_code={base_asset_code}\\u0026base_asset_issuer={base_asset_issuer}\\u0026counter_asset_type={counter_asset_type}\\u0026counter_asset_code={counter_asset_code}\\u0026counter_asset_issuer={counter_asset_issuer}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"transaction\": {\n"
          + "      \"href\": \"https://horizon-testnet.stellar.org/transactions/{hash}\",\n"
          + "      \"templated\": true\n"
          + "    },\n"
          + "    \"transactions\": {\n"
          + "      \"href\": \"https://horizon-testnet.stellar.org/transactions{?cursor,limit,order}\",\n"
          + "      \"templated\": true\n"
          + "    }\n"
          + "  },\n"
          + "  \"horizon_version\": \"2.28.0-93f9d706abadbe1594544093a7065665d26bc5cc\",\n"
          + "  \"core_version\": \"stellar-core 20.2.0.rc2 (368063acf47ddb85969dd6e5b5cc8d48c8b1342b)\",\n"
          + "  \"ingest_latest_ledger\": 323105,\n"
          + "  \"history_latest_ledger\": 323105,\n"
          + "  \"history_latest_ledger_closed_at\": \"2024-01-31T09:01:35Z\",\n"
          + "  \"history_elder_ledger\": 2,\n"
          + "  \"core_latest_ledger\": 323105,\n"
          + "  \"network_passphrase\": \"Test SDF Network ; September 2015\",\n"
          + "  \"current_protocol_version\": 20,\n"
          + "  \"supported_protocol_version\": 20,\n"
          + "  \"core_supported_protocol_version\": 20\n"
          + "}";
}
