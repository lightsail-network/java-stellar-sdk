package org.stellar.sdk.responses;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import org.stellar.sdk.AssetTypeCreditAlphaNum12;
import org.stellar.sdk.AssetTypeCreditAlphaNum4;
import org.stellar.sdk.AssetTypeNative;

public class TradeResponseTest {
  @Test
  public void testDeserializeWithOrderBook() throws IOException {
    String filePath = "src/test/resources/responses/trade_orderbook.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TradeResponse tradeResponse = GsonSingleton.getInstance().fromJson(json, TradeResponse.class);

    assertEquals(
        "https://horizon.stellar.org/accounts/GAREHPOLONP4N5WJPF6EG676FAH3IEDUF5AXMZXGSVU3262XJETXOTLW",
        tradeResponse.getLinks().getBase().getHref());
    assertEquals(
        "https://horizon.stellar.org/accounts/GB4FWDMQZEMDNAYUHOUOOXCG4TOWCNDUUEAI7ZUHNNPAK2236444OXHD",
        tradeResponse.getLinks().getCounter().getHref());
    assertEquals(
        "https://horizon.stellar.org/operations/150615114272284677",
        tradeResponse.getLinks().getOperation().getHref());

    // Assert other fields
    assertEquals("150615114272284677-0", tradeResponse.getId());
    assertEquals("150615114272284677-0", tradeResponse.getPagingToken());
    assertEquals("2021-04-23T13:55:52Z", tradeResponse.getLedgerCloseTime());
    assertEquals("orderbook", tradeResponse.getTradeType());
    assertEquals(544656471, tradeResponse.getOfferId().longValue());
    assertEquals(4762301132699672581L, tradeResponse.getBaseOfferId().longValue());
    assertEquals(
        "GAREHPOLONP4N5WJPF6EG676FAH3IEDUF5AXMZXGSVU3262XJETXOTLW", tradeResponse.getBaseAccount());
    assertEquals("0.4355160", tradeResponse.getBaseAmount());
    assertEquals("native", tradeResponse.getBaseAssetType());
    assertNull(tradeResponse.getBaseAssetCode());
    assertNull(tradeResponse.getBaseAssetIssuer());
    assertEquals(new AssetTypeNative(), tradeResponse.getBaseAsset());
    assertEquals(544656471, tradeResponse.getCounterOfferId().longValue());
    assertEquals(
        "GB4FWDMQZEMDNAYUHOUOOXCG4TOWCNDUUEAI7ZUHNNPAK2236444OXHD",
        tradeResponse.getCounterAccount());
    assertEquals("0.1841505", tradeResponse.getCounterAmount());
    assertEquals("credit_alphanum4", tradeResponse.getCounterAssetType());
    assertEquals("USD", tradeResponse.getCounterAssetCode());
    assertEquals(
        "GDUKMGUGDZQK6YHYA5Z6AY2G4XDSZPSZ3SW5UN3ARVMO6QSRDWP5YLEX",
        tradeResponse.getCounterAssetIssuer());
    assertEquals(
        new AssetTypeCreditAlphaNum4(
            "USD", "GDUKMGUGDZQK6YHYA5Z6AY2G4XDSZPSZ3SW5UN3ARVMO6QSRDWP5YLEX"),
        tradeResponse.getCounterAsset());
    assertFalse(tradeResponse.getBaseIsSeller());
    assertEquals(200, tradeResponse.getPrice().getNumerator());
    assertEquals(473, tradeResponse.getPrice().getDenominator());
  }

  @Test
  public void testDeserializeWithLiquidityPool() throws IOException {
    String filePath = "src/test/resources/responses/trade_liquidity_pool.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TradeResponse tradeResponse = GsonSingleton.getInstance().fromJson(json, TradeResponse.class);

    assertEquals(
        "https://horizon-testnet.stellar.org/accounts/GCUGVB6HXX56LMAYLTD2OJRJYGRGQRHQ4H5WFZRCWG4WOPCJSLFCWUZA",
        tradeResponse.getLinks().getBase().getHref());
    assertEquals(
        "https://horizon-testnet.stellar.org/liquidity_pools/2389378a6156eedfac66daa000d24c926431c3e667b9f754771964f27a6da6ab",
        tradeResponse.getLinks().getCounter().getHref());
    assertEquals(
        "https://horizon-testnet.stellar.org/operations/1567602933510145",
        tradeResponse.getLinks().getOperation().getHref());

    assertEquals("1567602933510145-0", tradeResponse.getId());
    assertEquals("1567602933510145-0", tradeResponse.getPagingToken());
    assertEquals("2021-10-07T14:13:20Z", tradeResponse.getLedgerCloseTime());
    assertEquals("liquidity_pool", tradeResponse.getTradeType());
    assertEquals(30, tradeResponse.getLiquidityPoolFeeBP().intValue());
    assertEquals(4613253621360898049L, tradeResponse.getBaseOfferId().longValue());
    assertEquals(
        "GCUGVB6HXX56LMAYLTD2OJRJYGRGQRHQ4H5WFZRCWG4WOPCJSLFCWUZA", tradeResponse.getBaseAccount());
    assertEquals("10.0000000", tradeResponse.getBaseAmount());
    assertEquals("native", tradeResponse.getBaseAssetType());
    assertEquals(new AssetTypeNative(), tradeResponse.getBaseAsset());

    assertNull(tradeResponse.getBaseAssetCode());
    assertNull(tradeResponse.getBaseAssetIssuer());
    assertEquals(
        "2389378a6156eedfac66daa000d24c926431c3e667b9f754771964f27a6da6ab",
        tradeResponse.getCounterLiquidityPoolID().getPoolId());
    assertEquals("6475.7567056", tradeResponse.getCounterAmount());
    assertEquals("credit_alphanum12", tradeResponse.getCounterAssetType());
    assertEquals("BTCLN", tradeResponse.getCounterAssetCode());
    assertEquals(
        "GD4UTLD46SWG5KCUVBRWFSFCUDISYNEJ2AF5FNB67ORAE6K7ZSAXH5O7",
        tradeResponse.getCounterAssetIssuer());
    assertEquals(
        new AssetTypeCreditAlphaNum12(
            "BTCLN", "GD4UTLD46SWG5KCUVBRWFSFCUDISYNEJ2AF5FNB67ORAE6K7ZSAXH5O7"),
        tradeResponse.getCounterAsset());
    assertFalse(tradeResponse.getBaseIsSeller());

    // Assert price
    assertEquals(140271366, tradeResponse.getPrice().getNumerator());
    assertEquals(21661, tradeResponse.getPrice().getDenominator());
  }
}
