package org.stellar.sdk.responses;

import static org.junit.Assert.assertEquals;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import org.stellar.sdk.responses.effects.EffectResponse;
import org.stellar.sdk.responses.gson.GsonSingleton;
import org.stellar.sdk.responses.operations.OperationResponse;

public class PagesTest {
  @Test
  public void testAccounts() throws IOException {
    String filePath = "src/test/resources/responses/pages/accounts.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TypeToken<Page<AccountResponse>> type = new TypeToken<Page<AccountResponse>>() {};
    Page<AccountResponse> page = GsonSingleton.getInstance().fromJson(json, type.getType());
    assertEquals(3, page.getRecords().size());
    assertEquals(
        "GA2224DCGO3WHC4EALA2PR2BZEMAYZPBPTHS243ZYYWQMBWRPJSZH5A6",
        page.getRecords().get(0).getId());
  }

  @Test
  public void testAssets() throws IOException {
    String filePath = "src/test/resources/responses/pages/assets.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TypeToken<Page<AssetResponse>> type = new TypeToken<Page<AssetResponse>>() {};
    Page<AssetResponse> page = GsonSingleton.getInstance().fromJson(json, type.getType());
    assertEquals(3, page.getRecords().size());
    assertEquals(25, page.getRecords().get(0).getNumAccounts().intValue());
  }

  @Test
  public void testClaimableBalances() throws IOException {
    String filePath = "src/test/resources/responses/pages/claimable_balances.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TypeToken<Page<ClaimableBalanceResponse>> type =
        new TypeToken<Page<ClaimableBalanceResponse>>() {};
    Page<ClaimableBalanceResponse> page =
        GsonSingleton.getInstance().fromJson(json, type.getType());
    assertEquals(3, page.getRecords().size());
    assertEquals(
        "00000000178826fbfe339e1f5c53417c6fedfe2c05e8bec14303143ec46b38981b09c3f9",
        page.getRecords().get(0).getId());
  }

  @Test
  public void testEffects() throws IOException {
    String filePath = "src/test/resources/responses/pages/effects.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TypeToken<Page<EffectResponse>> type = new TypeToken<Page<EffectResponse>>() {};
    Page<EffectResponse> page = GsonSingleton.getInstance().fromJson(json, type.getType());
    assertEquals(3, page.getRecords().size());
    assertEquals("0000000012884905985-0000000001", page.getRecords().get(0).getId());
  }

  @Test
  public void testLedgers() throws IOException {
    String filePath = "src/test/resources/responses/pages/ledgers.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TypeToken<Page<LedgerResponse>> type = new TypeToken<Page<LedgerResponse>>() {};
    Page<LedgerResponse> page = GsonSingleton.getInstance().fromJson(json, type.getType());
    assertEquals(3, page.getRecords().size());
    assertEquals(
        "fe0f6bea5f341344fdb5bc6fc4ad719dd63071d9203e9a1e7f17c68ea1ecebde",
        page.getRecords().get(0).getId());
  }

  @Test
  public void testLiquidityPools() throws IOException {
    String filePath = "src/test/resources/responses/pages/liquidity_pools.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TypeToken<Page<LiquidityPoolResponse>> type = new TypeToken<Page<LiquidityPoolResponse>>() {};
    Page<LiquidityPoolResponse> page = GsonSingleton.getInstance().fromJson(json, type.getType());
    assertEquals(3, page.getRecords().size());
    assertEquals(
        "0000a8198b5e25994c1ca5b0556faeb27325ac746296944144e0a7406d501e8a",
        page.getRecords().get(0).getId());
  }

  @Test
  public void testOffers() throws IOException {
    String filePath = "src/test/resources/responses/pages/offers.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TypeToken<Page<OfferResponse>> type = new TypeToken<Page<OfferResponse>>() {};
    Page<OfferResponse> page = GsonSingleton.getInstance().fromJson(json, type.getType());
    assertEquals(3, page.getRecords().size());
    assertEquals(2, page.getRecords().get(0).getId().longValue());
  }

  @Test
  public void testOperations() throws IOException {
    String filePath = "src/test/resources/responses/pages/operations.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TypeToken<Page<OperationResponse>> type = new TypeToken<Page<OperationResponse>>() {};
    Page<OperationResponse> page = GsonSingleton.getInstance().fromJson(json, type.getType());
    assertEquals(3, page.getRecords().size());
    assertEquals(12884905985L, page.getRecords().get(0).getId().longValue());
  }

  @Test
  public void testPayments() throws IOException {
    String filePath = "src/test/resources/responses/pages/payments.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TypeToken<Page<OperationResponse>> type = new TypeToken<Page<OperationResponse>>() {};
    Page<OperationResponse> page = GsonSingleton.getInstance().fromJson(json, type.getType());
    assertEquals(3, page.getRecords().size());
    assertEquals(12884905985L, page.getRecords().get(0).getId().longValue());
  }

  @Test
  public void testStrictReceivePaths() throws IOException {
    String filePath = "src/test/resources/responses/pages/strict_receive_paths.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TypeToken<Page<PathResponse>> type = new TypeToken<Page<PathResponse>>() {};
    Page<PathResponse> page = GsonSingleton.getInstance().fromJson(json, type.getType());
    assertEquals(4, page.getRecords().size());
    assertEquals("0.1000000", page.getRecords().get(0).getSourceAmount());
  }

  @Test
  public void testStrictSendPaths() throws IOException {
    String filePath = "src/test/resources/responses/pages/strict_send_paths.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TypeToken<Page<PathResponse>> type = new TypeToken<Page<PathResponse>>() {};
    Page<PathResponse> page = GsonSingleton.getInstance().fromJson(json, type.getType());
    assertEquals(6, page.getRecords().size());
    assertEquals("0.0100343", page.getRecords().get(0).getDestinationAmount());
  }

  @Test
  public void testTradeAggregations() throws IOException {
    String filePath = "src/test/resources/responses/pages/trade_aggregations.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TypeToken<Page<TradeAggregationResponse>> type =
        new TypeToken<Page<TradeAggregationResponse>>() {};
    Page<TradeAggregationResponse> page =
        GsonSingleton.getInstance().fromJson(json, type.getType());
    assertEquals(3, page.getRecords().size());
    assertEquals("0.0862979", page.getRecords().get(0).getOpen());
  }

  @Test
  public void testTrades() throws IOException {
    String filePath = "src/test/resources/responses/pages/trades.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TypeToken<Page<TradeResponse>> type = new TypeToken<Page<TradeResponse>>() {};
    Page<TradeResponse> page = GsonSingleton.getInstance().fromJson(json, type.getType());
    assertEquals(3, page.getRecords().size());
    assertEquals("3697472920621057-0", page.getRecords().get(0).getId());
  }

  @Test
  public void testTransactions() throws IOException {
    String filePath = "src/test/resources/responses/pages/transactions.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TypeToken<Page<TransactionResponse>> type = new TypeToken<Page<TransactionResponse>>() {};
    Page<TransactionResponse> page = GsonSingleton.getInstance().fromJson(json, type.getType());
    assertEquals(3, page.getRecords().size());
    assertEquals(
        "65bdbaa1d80a8717c0b2f83befac70986b1387164f76853ac1a2f76a94d6eb63",
        page.getRecords().get(0).getId());
    assertEquals(
        "1932d1ca9507ed1399a7b27d881168c003168f98fbe969f667196843dbaa0ee4",
        page.getRecords().get(1).getId());
  }
}
