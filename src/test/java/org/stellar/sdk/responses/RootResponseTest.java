package org.stellar.sdk.responses;

import org.junit.Test;
import org.stellar.sdk.AssetTypeCreditAlphaNum4;
import org.stellar.sdk.AssetTypeNative;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RootResponseTest {
    @Test
    public void testDeserialize() throws IOException {
        String filePath = "src/test/resources/responses/root.json";
        String json = new String(Files.readAllBytes(Paths.get(filePath)));
        RootResponse rootResponse =
                GsonSingleton.getInstance().fromJson(json, RootResponse.class);

        assertEquals("2.31.0-d1d911f6ed7396fd705d8f2068f70f468012102e", rootResponse.getHorizonVersion());
        assertEquals("stellar-core 21.1.0 (b3aeb14cc798f6d11deb2be913041be916f3b0cc)", rootResponse.getStellarCoreVersion());
        assertEquals(52426477L, rootResponse.getIngestLatestLedger().longValue());
        assertEquals(52426477, rootResponse.getHistoryLatestLedger().longValue());
        assertEquals("2024-07-05T01:43:41Z", rootResponse.getHistoryLatestLedgerClosedAt());
        assertEquals(2, rootResponse.getHistoryElderLedger().longValue());
        assertEquals(52426478, rootResponse.getCoreLatestLedger().longValue());
        assertEquals("Public Global Stellar Network ; September 2015", rootResponse.getNetworkPassphrase());
        assertEquals(21, rootResponse.getCurrentProtocolVersion().intValue());
        assertEquals(21L, rootResponse.getSupportedProtocolVersion().longValue());
        assertEquals(21, rootResponse.getCoreSupportedProtocolVersion().intValue());

        RootResponse.Links links = rootResponse.getLinks();
        assertEquals("https://horizon.stellar.org/accounts/{account_id}", links.getAccount().getHref());
        assertTrue(links.getAccount().isTemplated());
        assertEquals("https://horizon.stellar.org/accounts{?signer,sponsor,asset,liquidity_pool,cursor,limit,order}", links.getAccounts().getHref());
        assertTrue(links.getAccounts().isTemplated());
        assertEquals("https://horizon.stellar.org/accounts/{account_id}/transactions{?cursor,limit,order}", links.getAccountTransactions().getHref());
        assertTrue(links.getAccountTransactions().isTemplated());
        assertEquals("https://horizon.stellar.org/claimable_balances{?asset,sponsor,claimant,cursor,limit,order}", links.getClaimableBalances().getHref());
        assertTrue(links.getClaimableBalances().isTemplated());
        assertEquals("https://horizon.stellar.org/assets{?asset_code,asset_issuer,cursor,limit,order}", links.getAssets().getHref());
        assertTrue(links.getAssets().isTemplated());
        assertEquals("https://horizon.stellar.org/effects{?cursor,limit,order}", links.getEffects().getHref());
        assertTrue(links.getEffects().isTemplated());
        assertEquals("https://horizon.stellar.org/fee_stats", links.getFeeStats().getHref());
        assertFalse(links.getFeeStats().isTemplated());
        assertEquals("https://horizon.stellar.org/ledgers/{sequence}", links.getLedger().getHref());
        assertTrue(links.getLedger().isTemplated());
        assertEquals("https://horizon.stellar.org/ledgers{?cursor,limit,order}", links.getLedgers().getHref());
        assertTrue(links.getLedgers().isTemplated());
        assertEquals("https://horizon.stellar.org/liquidity_pools{?reserves,account,cursor,limit,order}", links.getLiquidityPools().getHref());
        assertTrue(links.getLiquidityPools().isTemplated());
        assertEquals("https://horizon.stellar.org/offers/{offer_id}", links.getOffer().getHref());
        assertTrue(links.getOffer().isTemplated());
        assertEquals("https://horizon.stellar.org/offers{?selling,buying,seller,sponsor,cursor,limit,order}", links.getOffers().getHref());
        assertTrue(links.getOffers().isTemplated());
        assertEquals("https://horizon.stellar.org/operations/{id}", links.getOperation().getHref());
        assertTrue(links.getOperation().isTemplated());
        assertEquals("https://horizon.stellar.org/operations{?cursor,limit,order,include_failed}", links.getOperations().getHref());
        assertTrue(links.getOperations().isTemplated());
        assertEquals("https://horizon.stellar.org/order_book{?selling_asset_type,selling_asset_code,selling_asset_issuer,buying_asset_type,buying_asset_code,buying_asset_issuer,limit}", links.getOrderBook().getHref());
        assertTrue(links.getOrderBook().isTemplated());
        assertEquals("https://horizon.stellar.org/payments{?cursor,limit,order,include_failed}", links.getPayments().getHref());
        assertTrue(links.getPayments().isTemplated());
        assertEquals("https://horizon.stellar.org/", links.getSelf().getHref());
        assertFalse(links.getSelf().isTemplated());
        assertEquals("https://horizon.stellar.org/paths/strict-receive{?source_assets,source_account,destination_account,destination_asset_type,destination_asset_issuer,destination_asset_code,destination_amount}", links.getStrictReceivePaths().getHref());
        assertTrue(links.getStrictReceivePaths().isTemplated());
        assertEquals("https://horizon.stellar.org/paths/strict-send{?destination_account,destination_assets,source_asset_type,source_asset_issuer,source_asset_code,source_amount}", links.getStrictSendPaths().getHref());
        assertTrue(links.getStrictSendPaths().isTemplated());
        assertEquals("https://horizon.stellar.org/trade_aggregations?base_asset_type={base_asset_type}&base_asset_code={base_asset_code}&base_asset_issuer={base_asset_issuer}&counter_asset_type={counter_asset_type}&counter_asset_code={counter_asset_code}&counter_asset_issuer={counter_asset_issuer}", links.getTradeAggregations().getHref());
        assertTrue(links.getTradeAggregations().isTemplated());
        assertEquals("https://horizon.stellar.org/trades?base_asset_type={base_asset_type}&base_asset_code={base_asset_code}&base_asset_issuer={base_asset_issuer}&counter_asset_type={counter_asset_type}&counter_asset_code={counter_asset_code}&counter_asset_issuer={counter_asset_issuer}", links.getTrades().getHref());
        assertTrue(links.getTrades().isTemplated());
        assertEquals("https://horizon.stellar.org/transactions/{hash}", links.getTransaction().getHref());
        assertTrue(links.getTransaction().isTemplated());
        assertEquals("https://horizon.stellar.org/transactions{?cursor,limit,order}", links.getTransactions().getHref());
        assertTrue(links.getTransactions().isTemplated());
    }
}