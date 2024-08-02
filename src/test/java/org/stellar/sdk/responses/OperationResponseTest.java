package org.stellar.sdk.responses;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.stellar.sdk.AssetTypeCreditAlphaNum12;
import org.stellar.sdk.AssetTypeCreditAlphaNum4;
import org.stellar.sdk.AssetTypeNative;
import org.stellar.sdk.Claimant;
import org.stellar.sdk.LiquidityPoolID;
import org.stellar.sdk.Predicate;
import org.stellar.sdk.TrustLineAsset;
import org.stellar.sdk.responses.gson.GsonSingleton;
import org.stellar.sdk.responses.operations.AccountMergeOperationResponse;
import org.stellar.sdk.responses.operations.AllowTrustOperationResponse;
import org.stellar.sdk.responses.operations.BeginSponsoringFutureReservesOperationResponse;
import org.stellar.sdk.responses.operations.BumpSequenceOperationResponse;
import org.stellar.sdk.responses.operations.ChangeTrustOperationResponse;
import org.stellar.sdk.responses.operations.ClaimClaimableBalanceOperationResponse;
import org.stellar.sdk.responses.operations.ClawbackClaimableBalanceOperationResponse;
import org.stellar.sdk.responses.operations.ClawbackOperationResponse;
import org.stellar.sdk.responses.operations.CreateAccountOperationResponse;
import org.stellar.sdk.responses.operations.CreateClaimableBalanceOperationResponse;
import org.stellar.sdk.responses.operations.CreatePassiveSellOfferOperationResponse;
import org.stellar.sdk.responses.operations.EndSponsoringFutureReservesOperationResponse;
import org.stellar.sdk.responses.operations.ExtendFootprintTTLOperationResponse;
import org.stellar.sdk.responses.operations.InflationOperationResponse;
import org.stellar.sdk.responses.operations.InvokeHostFunctionOperationResponse;
import org.stellar.sdk.responses.operations.LiquidityPoolDepositOperationResponse;
import org.stellar.sdk.responses.operations.LiquidityPoolWithdrawOperationResponse;
import org.stellar.sdk.responses.operations.ManageBuyOfferOperationResponse;
import org.stellar.sdk.responses.operations.ManageDataOperationResponse;
import org.stellar.sdk.responses.operations.ManageSellOfferOperationResponse;
import org.stellar.sdk.responses.operations.OperationResponse;
import org.stellar.sdk.responses.operations.PathPaymentStrictReceiveOperationResponse;
import org.stellar.sdk.responses.operations.PathPaymentStrictSendOperationResponse;
import org.stellar.sdk.responses.operations.PaymentOperationResponse;
import org.stellar.sdk.responses.operations.RestoreFootprintOperationResponse;
import org.stellar.sdk.responses.operations.RevokeSponsorshipOperationResponse;
import org.stellar.sdk.responses.operations.SetOptionsOperationResponse;
import org.stellar.sdk.responses.operations.SetTrustLineFlagsOperationResponse;

public class OperationResponseTest {
  @Test
  public void testBaseOperation() throws IOException {
    // TODO: test all fields
    String filePath = "src/test/resources/responses/operations/account_merge.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    AccountMergeOperationResponse response =
        GsonSingleton.getInstance().fromJson(json, AccountMergeOperationResponse.class);

    assertNotNull(response);
    assertEquals(150661877876133889L, response.getId().longValue());
    assertEquals("150661877876133889", response.getPagingToken());
    assertTrue(response.getTransactionSuccessful());
    assertEquals(
        "GBMZ7GUHNHT6TG4ITOBG46TKA5YMNH7ZKHHJSQRU6PNYBSAELLXNOFDE", response.getSourceAccount());
    assertEquals("account_merge", response.getType());
    assertEquals("2021-04-24T06:18:26Z", response.getCreatedAt());
    assertEquals(
        "f86aaf456dc000e565ac89bd0b21abcedad4d3ab497bdb9fefa782c9e6ce8c98",
        response.getTransactionHash());
    assertEquals("GBMZ7GUHNHT6TG4ITOBG46TKA5YMNH7ZKHHJSQRU6PNYBSAELLXNOFDE", response.getAccount());
    assertEquals("GDYG6NVTFCY6HPBRQ3SQNKTDZUR7SS6WAWNEAKAFJW5EMKDGQLPG523C", response.getInto());
  }

  @Test
  public void testAccountMergeOperation() throws IOException {
    String filePath = "src/test/resources/responses/operations/account_merge.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    AccountMergeOperationResponse response =
        (AccountMergeOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals("account_merge", response.getType());
    assertEquals("GBMZ7GUHNHT6TG4ITOBG46TKA5YMNH7ZKHHJSQRU6PNYBSAELLXNOFDE", response.getAccount());
    assertEquals("GDYG6NVTFCY6HPBRQ3SQNKTDZUR7SS6WAWNEAKAFJW5EMKDGQLPG523C", response.getInto());
    // TODO: muxed
  }

  @Test
  public void testAllowTrustOperation() throws IOException {
    String filePath = "src/test/resources/responses/operations/allow_trust.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    AllowTrustOperationResponse response =
        (AllowTrustOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals("allow_trust", response.getType());
    assertEquals("GBRDHSZL4ZKOI2PTUMM53N3NICZXC5OX3KPCD4WD4NG4XGCBC2ZA3KAG", response.getTrustee());
    assertEquals("GDJ4X5NVRMIB3ZYO2PQ7U2QKZ5C42YKV7ZH3LJYXZWQOP4K5CUBSNGVS", response.getTrustor());
    assertEquals("credit_alphanum4", response.getAssetType());
    assertEquals("4GLD", response.getAssetCode());
    assertEquals(
        "GBRDHSZL4ZKOI2PTUMM53N3NICZXC5OX3KPCD4WD4NG4XGCBC2ZA3KAG", response.getAssetIssuer());
    assertEquals(
        new AssetTypeCreditAlphaNum4(
            "4GLD", "GBRDHSZL4ZKOI2PTUMM53N3NICZXC5OX3KPCD4WD4NG4XGCBC2ZA3KAG"),
        response.getAsset());
    // TODO: recheck it
    assertTrue(response.getAuthorize());
    assertTrue(response.getAuthorizeToMaintainLiabilities());
  }

  @Test
  public void testBeginSponsoringFutureReservesOperation() throws IOException {
    String filePath =
        "src/test/resources/responses/operations/begin_sponsoring_future_reserves.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    BeginSponsoringFutureReservesOperationResponse response =
        (BeginSponsoringFutureReservesOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals("begin_sponsoring_future_reserves", response.getType());
    assertEquals(
        "GDS6ULV46WVSO2USGIVTIUDYBL3ROBPQEEANE3AY6XZION25DCNFIE2R", response.getSponsoredId());
  }

  @Test
  public void testBumpSequenceOperation() throws IOException {
    String filePath = "src/test/resources/responses/operations/bump_sequence.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    BumpSequenceOperationResponse response =
        (BumpSequenceOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals("bump_sequence", response.getType());
    assertEquals(136025045943191412L, response.getBumpTo().longValue());
  }

  @Test
  public void testChangeTrustOperationWithAsset() throws IOException {
    String filePath = "src/test/resources/responses/operations/change_trust_asset.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    ChangeTrustOperationResponse response =
        (ChangeTrustOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals("change_trust", response.getType());
    assertEquals("GA5ZSEJYB37JRC5AVCIA5MOP4RHTM335X2KGX3IHOJAPP5RE34K4KZVN", response.getTrustee());
    assertEquals("GA26UJZUXR5Q2VMTJHAFS2DV6DKFRWBIN7JKDALGYFEXTRNGX5K6DEAZ", response.getTrustor());
    assertEquals("credit_alphanum4", response.getAssetType());
    assertEquals("USDC", response.getAssetCode());
    assertEquals(
        "GA5ZSEJYB37JRC5AVCIA5MOP4RHTM335X2KGX3IHOJAPP5RE34K4KZVN", response.getAssetIssuer());
    assertEquals(
        new TrustLineAsset(
            new AssetTypeCreditAlphaNum4(
                "USDC", "GA5ZSEJYB37JRC5AVCIA5MOP4RHTM335X2KGX3IHOJAPP5RE34K4KZVN")),
        response.getTrustLineAsset());
    assertEquals("922337203685.4775807", response.getLimit());
  }

  @Test
  public void testChangeTrustOperationWithLiquidityPoolId() throws IOException {
    String filePath = "src/test/resources/responses/operations/change_trust_liquidity_pool_id.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    ChangeTrustOperationResponse response =
        (ChangeTrustOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals("change_trust", response.getType());
    assertEquals("GAQXAWHCM4A7SQCT3BOSVEGRI2OOB7LO2CMFOYFF6YRXU4VQSB5V2V2K", response.getTrustor());
    assertEquals(
        "MAQXAWHCM4A7SQCT3BOSVEGRI2OOB7LO2CMFOYFF6YRXU4VQSB5V2AAAAAAAAE4DUGF2O",
        response.getTrustorMuxed());
    assertEquals(1278881L, response.getTrustorMuxedId().longValue());
    assertEquals("liquidity_pool_shares", response.getAssetType());
    assertEquals(
        "2c0bfa623845dd101cbf074a1ca1ae4b2458cc8d0104ad65939ebe2cd9054355",
        response.getLiquidityPoolId());
    assertEquals(
        new TrustLineAsset(
            new LiquidityPoolID(
                "2c0bfa623845dd101cbf074a1ca1ae4b2458cc8d0104ad65939ebe2cd9054355")),
        response.getTrustLineAsset());
    assertEquals("922337203685.4775807", response.getLimit());
  }

  @Test
  public void testClaimClaimableBalanceOperation() throws IOException {
    String filePath = "src/test/resources/responses/operations/claim_claimable_balance.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    ClaimClaimableBalanceOperationResponse response =
        (ClaimClaimableBalanceOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals("claim_claimable_balance", response.getType());
    assertEquals(
        "00000000a5c8c85c12a32ec1b30fc1792a542ca38702afd78eb4fe524d028887cf6b6952",
        response.getBalanceId());
    assertEquals(
        "GAEY7JFLBBDD6PAUPVRVKMBNSL5W6GYMUOGJKNGHGFSFGJU6CT2IUARS", response.getClaimant());
    // TODO: muxed
  }

  @Test
  public void testClawbackClaimableBalanceOperation() throws IOException {
    String filePath = "src/test/resources/responses/operations/clawback_claimable_balance.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    ClawbackClaimableBalanceOperationResponse response =
        (ClawbackClaimableBalanceOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals("clawback_claimable_balance", response.getType());
    assertEquals(
        "000000001fe36f3ce6ab6a6423b18b5947ce8890157ae77bb17faeb765814ad040b74ce1",
        response.getBalanceId());
  }

  @Test
  public void testClawbackOperation() throws IOException {
    String filePath = "src/test/resources/responses/operations/clawback.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    ClawbackOperationResponse response =
        (ClawbackOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals("clawback", response.getType());
    assertEquals("credit_alphanum12", response.getAssetType());
    assertEquals("Hello", response.getAssetCode());
    assertEquals(
        "GCHYD6AWPR2PN66JFFDR63OEFO5RMOUGFL7D3BBUEMTJDMUFHPLNX2SC", response.getAssetIssuer());
    assertEquals(
        new AssetTypeCreditAlphaNum12(
            "Hello", "GCHYD6AWPR2PN66JFFDR63OEFO5RMOUGFL7D3BBUEMTJDMUFHPLNX2SC"),
        response.getAsset());
    assertEquals("GCDG7N63GJMJDI4627LY3XKNZARQNX3QFY6HAWON3JDAS3SCGINGQHEQ", response.getFrom());
    assertEquals("1234.0000000", response.getAmount());
    // TODO: muxed

  }

  @Test
  public void testCreateAccountOperation() throws IOException {
    String filePath = "src/test/resources/responses/operations/create_account.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    CreateAccountOperationResponse response =
        (CreateAccountOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals("create_account", response.getType());
    assertEquals("4.0000000", response.getStartingBalance());
    assertEquals("GDYG6NVTFCY6HPBRQ3SQNKTDZUR7SS6WAWNEAKAFJW5EMKDGQLPG523C", response.getFunder());
    assertEquals("GCYXLFYRSJWRPQ7JYSPHDNFRNXQ377BNUHF3TDZCPCQMVJS2QKMXCT2E", response.getAccount());
    // TODO: muxed
  }

  @Test
  public void testCreateClaimableBalanceOperation() throws IOException {
    String filePath = "src/test/resources/responses/operations/create_claimable_balance.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    CreateClaimableBalanceOperationResponse response =
        (CreateClaimableBalanceOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals("create_claimable_balance", response.getType());
    assertEquals("GCS4PXWRDKC5445PEKTJHIOEOVTCIQK2YMGPXDZLDXUIPK4HOHLQVYXL", response.getSponsor());
    assertEquals("1.2345000", response.getAmount());
    assertEquals(new AssetTypeNative(), response.getAsset());

    Claimant claimant1 =
        new Claimant(
            "GDMTVHLWJTHSUDMZVVMXXH6VJHA2ZV3HNG5LYNAZ6RTWB7GISM6PGTUV",
            new Predicate.And(
                new Predicate.Or(
                    new Predicate.AbsBefore(600),
                    new Predicate.Or(new Predicate.Unconditional(), new Predicate.RelBefore(300))),
                new Predicate.Unconditional()));
    Claimant claimant2 =
        new Claimant(
            "GCS4PXWRDKC5445PEKTJHIOEOVTCIQK2YMGPXDZLDXUIPK4HOHLQVYXL",
            new Predicate.Unconditional());
    List<Claimant> claimants = Arrays.asList(claimant1, claimant2);
    assertEquals(claimants, response.getClaimants());
  }

  @Test
  public void testCreatePassiveSellOfferOperation() throws IOException {
    String filePath = "src/test/resources/responses/operations/create_passive_sell_offer.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    CreatePassiveSellOfferOperationResponse response =
        (CreatePassiveSellOfferOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals("create_passive_sell_offer", response.getType());
    assertEquals("0.0001000", response.getAmount());
    assertEquals("1.0000000", response.getPrice());
    assertEquals(new Price(1L, 1L), response.getPriceR());
    assertEquals("credit_alphanum4", response.getBuyingAssetType());
    assertEquals("USA", response.getBuyingAssetCode());
    assertEquals(
        "GCQPF34D4NP66RYULISYRSBS52M5FFZ2JVJ4LMQTQXDI2XGP6UDSE3IB",
        response.getBuyingAssetIssuer());
    assertEquals(
        new AssetTypeCreditAlphaNum4(
            "USA", "GCQPF34D4NP66RYULISYRSBS52M5FFZ2JVJ4LMQTQXDI2XGP6UDSE3IB"),
        response.getBuyingAsset());
    assertEquals("credit_alphanum12", response.getSellingAssetType());
    assertEquals("MONEY", response.getSellingAssetCode());
    assertEquals(
        "GA2KQTETIRREL66P64GV6KCVICPULLDVHWJDZSIJKDLIAGBXUCIZ6P6E",
        response.getSellingAssetIssuer());
    assertEquals(
        new AssetTypeCreditAlphaNum12(
            "MONEY", "GA2KQTETIRREL66P64GV6KCVICPULLDVHWJDZSIJKDLIAGBXUCIZ6P6E"),
        response.getSellingAsset());
  }

  @Test
  public void testEndSponsoringFutureReservesOperation() throws IOException {
    String filePath = "src/test/resources/responses/operations/end_sponsoring_future_reserves.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    EndSponsoringFutureReservesOperationResponse response =
        (EndSponsoringFutureReservesOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals("end_sponsoring_future_reserves", response.getType());
    assertEquals(
        "GDDUVW72OCNRAVL752HXYJQTXEJQRYHDWU76YKGDEWZ5HCATZBSKJM7Y", response.getBeginSponsor());
    // TODO: muxed
  }

  @Test
  public void testExtendFootprintTTLOperation() throws IOException {
    String filePath = "src/test/resources/responses/operations/extend_footprint_ttl.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    ExtendFootprintTTLOperationResponse response =
        (ExtendFootprintTTLOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals("extend_footprint_ttl", response.getType());
    assertEquals(500000, response.getExtendTo().longValue());
  }

  @Test
  public void testInflationOperation() throws IOException {
    String filePath = "src/test/resources/responses/operations/inflation.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    InflationOperationResponse response =
        (InflationOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);
    assertEquals("inflation", response.getType());
  }

  @Test
  public void testInvokeHostFunctionOperation() throws IOException {
    String filePath = "src/test/resources/responses/operations/invoke_host_function.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    InvokeHostFunctionOperationResponse response =
        (InvokeHostFunctionOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals("invoke_host_function", response.getType());
    assertEquals("HostFunctionTypeHostFunctionTypeInvokeContract", response.getFunction());
    assertEquals(5, response.getParameters().size());
    assertEquals(
        "AAAAEgAAAAGw7oy+G8a9SeTIE5E/EuJYl5JfwF0eZJWk8S7LmE7fwA==",
        response.getParameters().get(0).getValue());
    assertEquals("Address", response.getParameters().get(0).getType());
    assertEquals("AAAACgAAAAAAAAAAAAAAASoF8gA=", response.getParameters().get(4).getValue());
    assertEquals("I128", response.getParameters().get(4).getType());
    assertEquals("", response.getAddress());
    assertEquals("", response.getSalt());
    assertEquals(1, response.getAssetBalanceChanges().size());
    assertEquals("credit_alphanum12", response.getAssetBalanceChanges().get(0).getAssetType());
    assertEquals("Hello", response.getAssetBalanceChanges().get(0).getAssetCode());
    assertEquals(
        "GDJKBIYIPBE2NC5XIZX6GCFZHVWFUA7ONMQUOOVTLIM3BESTI4BYADAN",
        response.getAssetBalanceChanges().get(0).getAssetIssuer());
    assertEquals("transfer", response.getAssetBalanceChanges().get(0).getType());
    assertEquals(
        "GDAT5HWTGIU4TSSZ4752OUC4SABDLTLZFRPZUJ3D6LKBNEPA7V2CIG54",
        response.getAssetBalanceChanges().get(0).getFrom());
    assertEquals(
        "GBMLPRFCZDZJPKUPHUSHCKA737GOZL7ERZLGGMJ6YGHBFJZ6ZKMKCZTM",
        response.getAssetBalanceChanges().get(0).getTo());
    assertEquals("500.0000000", response.getAssetBalanceChanges().get(0).getAmount());
  }

  @Test
  public void testLiquidityPoolDepositOperation() throws IOException {
    String filePath = "src/test/resources/responses/operations/liquidity_pool_deposit.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    LiquidityPoolDepositOperationResponse response =
        (LiquidityPoolDepositOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals("liquidity_pool_deposit", response.getType());
    assertEquals(
        "2c0bfa623845dd101cbf074a1ca1ae4b2458cc8d0104ad65939ebe2cd9054355",
        response.getLiquidityPoolId());

    assertEquals(2, response.getReservesMax().size());
    assertEquals("250.0000000", response.getReservesMax().get(0).getAmount());
    assertEquals(
        new AssetTypeCreditAlphaNum4(
            "COOL", "GAZKB7OEYRUVL6TSBXI74D2IZS4JRCPBXJZ37MDDYAEYBOMHXUYIX5YL"),
        response.getReservesMax().get(0).getAsset());
    assertEquals("250.0000000", response.getReservesMax().get(1).getAmount());
    assertEquals(
        new AssetTypeCreditAlphaNum12(
            "SONESO", "GAOF7ARG3ZAVUA63GCLXG5JQTMBAH3ZFYHGLGJLDXGDSXQRHD72LLGOB"),
        response.getReservesMax().get(1).getAsset());

    assertEquals("1.0000000", response.getMinPrice());
    assertEquals(new Price(1L, 1L), response.getMinPriceR());
    assertEquals("2.0000000", response.getMaxPrice());
    assertEquals(new Price(2L, 1L), response.getMaxPriceR());

    assertEquals("250.0000000", response.getSharesReceived());

    assertEquals(2, response.getReservesDeposited().size());
    assertEquals("250.0000000", response.getReservesDeposited().get(0).getAmount());
    assertEquals(
        new AssetTypeCreditAlphaNum4(
            "COOL", "GAZKB7OEYRUVL6TSBXI74D2IZS4JRCPBXJZ37MDDYAEYBOMHXUYIX5YL"),
        response.getReservesDeposited().get(0).getAsset());
    assertEquals("250.0000000", response.getReservesDeposited().get(1).getAmount());
    assertEquals(
        new AssetTypeCreditAlphaNum12(
            "SONESO", "GAOF7ARG3ZAVUA63GCLXG5JQTMBAH3ZFYHGLGJLDXGDSXQRHD72LLGOB"),
        response.getReservesDeposited().get(1).getAsset());
  }

  @Test
  public void testLiquidityPoolWithdrawOperation() throws IOException {
    String filePath = "src/test/resources/responses/operations/liquidity_pool_withdraw.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    LiquidityPoolWithdrawOperationResponse response =
        (LiquidityPoolWithdrawOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals("liquidity_pool_withdraw", response.getType());
    assertEquals(
        "2c0bfa623845dd101cbf074a1ca1ae4b2458cc8d0104ad65939ebe2cd9054355",
        response.getLiquidityPoolId());

    assertEquals(2, response.getReservesMin().size());
    assertEquals("100.0000000", response.getReservesMin().get(0).getAmount());
    assertEquals(
        new AssetTypeCreditAlphaNum4(
            "COOL", "GAZKB7OEYRUVL6TSBXI74D2IZS4JRCPBXJZ37MDDYAEYBOMHXUYIX5YL"),
        response.getReservesMin().get(0).getAsset());
    assertEquals("100.0000000", response.getReservesMin().get(1).getAmount());
    assertEquals(
        new AssetTypeCreditAlphaNum12(
            "SONESO", "GAOF7ARG3ZAVUA63GCLXG5JQTMBAH3ZFYHGLGJLDXGDSXQRHD72LLGOB"),
        response.getReservesMin().get(1).getAsset());

    assertEquals("100.0000000", response.getShares());

    assertEquals(2, response.getReservesReceived().size());
    assertEquals("100.0000000", response.getReservesReceived().get(0).getAmount());
    assertEquals(
        new AssetTypeCreditAlphaNum4(
            "COOL", "GAZKB7OEYRUVL6TSBXI74D2IZS4JRCPBXJZ37MDDYAEYBOMHXUYIX5YL"),
        response.getReservesReceived().get(0).getAsset());
    assertEquals("100.0000000", response.getReservesReceived().get(1).getAmount());
    assertEquals(
        new AssetTypeCreditAlphaNum12(
            "SONESO", "GAOF7ARG3ZAVUA63GCLXG5JQTMBAH3ZFYHGLGJLDXGDSXQRHD72LLGOB"),
        response.getReservesReceived().get(1).getAsset());
  }

  @Test
  public void testManageBuyOfferOperation() throws IOException {
    String filePath = "src/test/resources/responses/operations/manage_buy_offer.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    ManageBuyOfferOperationResponse response =
        (ManageBuyOfferOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals("manage_buy_offer", response.getType());
    assertEquals("389.4340658", response.getAmount());
    assertEquals("2.3058668", response.getPrice());
    assertEquals(new Price(5764667L, 2500000L), response.getPriceR());
    assertEquals("native", response.getBuyingAssetType());
    assertEquals(new AssetTypeNative(), response.getBuyingAsset());

    assertEquals("credit_alphanum4", response.getSellingAssetType());
    assertEquals("XXA", response.getSellingAssetCode());
    assertEquals(
        "GC4HS4CQCZULIOTGLLPGRAAMSBDLFRR6Y7HCUQG66LNQDISXKIXXADIM",
        response.getSellingAssetIssuer());
    assertEquals(
        new AssetTypeCreditAlphaNum4(
            "XXA", "GC4HS4CQCZULIOTGLLPGRAAMSBDLFRR6Y7HCUQG66LNQDISXKIXXADIM"),
        response.getSellingAsset());
    assertEquals(0, response.getOfferId().longValue());
  }

  @Test
  public void testManageDataOperation() throws IOException {
    String filePath = "src/test/resources/responses/operations/manage_data.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    ManageDataOperationResponse response =
        (ManageDataOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals("manage_data", response.getType());
    assertEquals("MESSAGE_DATA_0", response.getName());
    assertEquals(
        "UW1kaDJ5d2hmclJmWmQ2elFrcnpSdDdTM1dCOUFLQnJ3dHBIQ0RGUm5OWmhTcA==", response.getValue());
    assertArrayEquals(
        "Qmdh2ywhfrRfZd6zQkrzRt7S3WB9AKBrwtpHCDFRnNZhSp".getBytes(StandardCharsets.UTF_8),
        response.getDecodedValue());
  }

  @Test
  public void testManageSellOfferOperation() throws IOException {
    String filePath = "src/test/resources/responses/operations/manage_sell_offer.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    ManageSellOfferOperationResponse response =
        (ManageSellOfferOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals("manage_sell_offer", response.getType());
    assertEquals("478.6351404", response.getAmount());
    assertEquals("32.6639943", response.getPrice());
    assertEquals(new Price(326639943L, 10000000L), response.getPriceR());
    assertEquals("credit_alphanum4", response.getBuyingAssetType());
    assertEquals("RMT", response.getBuyingAssetCode());
    assertEquals(
        "GDEGOXPCHXWFYY234D2YZSPEJ24BX42ESJNVHY5H7TWWQSYRN5ZKZE3N",
        response.getBuyingAssetIssuer());
    assertEquals(
        new AssetTypeCreditAlphaNum4(
            "RMT", "GDEGOXPCHXWFYY234D2YZSPEJ24BX42ESJNVHY5H7TWWQSYRN5ZKZE3N"),
        response.getBuyingAsset());

    assertEquals("native", response.getSellingAssetType());
    assertEquals(new AssetTypeNative(), response.getSellingAsset());
    assertEquals(0, response.getOfferId().longValue());
  }

  @Test
  public void testPathPaymentStrictReceiveOperation() throws IOException {
    String filePath = "src/test/resources/responses/operations/path_payment_strict_receive.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    PathPaymentStrictReceiveOperationResponse response =
        (PathPaymentStrictReceiveOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals("path_payment_strict_receive", response.getType());
    assertEquals("native", response.getAssetType());
    assertEquals(new AssetTypeNative(), response.getAsset());
    assertEquals("GDQLBBDNG5L3TZLR5RPVQFT4CUORSTHWX3GJBHCCADXBJV55ZHW6CL5B", response.getFrom());
    assertEquals("GDQLBBDNG5L3TZLR5RPVQFT4CUORSTHWX3GJBHCCADXBJV55ZHW6CL5B", response.getTo());
    assertEquals(2, response.getPath().size());
    assertEquals(
        new AssetTypeCreditAlphaNum4(
            "USDC", "GA5ZSEJYB37JRC5AVCIA5MOP4RHTM335X2KGX3IHOJAPP5RE34K4KZVN"),
        response.getPath().get(0));
    assertEquals(
        new AssetTypeCreditAlphaNum4(
            "SLT", "GCKA6K5PCQ6PNF5RQBF7PQDJWRHO6UOGFMRLK3DYHDOI244V47XKQ4GP"),
        response.getPath().get(1));
    assertEquals("2386.8885355", response.getAmount());
    assertEquals("0.0000000", response.getSourceAmount());
    assertEquals("2386.8885355", response.getSourceMax());
    assertEquals("native", response.getSourceAssetType());
    assertEquals(new AssetTypeNative(), response.getSourceAsset());
  }

  @Test
  public void testPathPaymentStrictSendOperation() throws IOException {
    String filePath = "src/test/resources/responses/operations/path_payment_strict_send.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    PathPaymentStrictSendOperationResponse response =
        (PathPaymentStrictSendOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals("path_payment_strict_send", response.getType());
    assertEquals("credit_alphanum4", response.getAssetType());
    assertEquals("BTC", response.getAssetCode());
    assertEquals(
        "GAUTUYY2THLF7SGITDFMXJVYH3LHDSMGEAKSBU267M2K7A3W543CKUEF", response.getAssetIssuer());
    assertEquals(
        new AssetTypeCreditAlphaNum4(
            "BTC", "GAUTUYY2THLF7SGITDFMXJVYH3LHDSMGEAKSBU267M2K7A3W543CKUEF"),
        response.getAsset());
    assertEquals("GDBKRQWEUJGRWWCAGK75L4PZXM2S3DQQIG6S43Y2GMN3IUFZWCLSRA46", response.getFrom());
    assertEquals("GDBKRQWEUJGRWWCAGK75L4PZXM2S3DQQIG6S43Y2GMN3IUFZWCLSRA46", response.getTo());
    assertEquals(2, response.getPath().size());
    assertEquals(
        new AssetTypeCreditAlphaNum4(
            "ETH", "GBDEVU63Y6NTHJQQZIKVTC23NWLQVP3WJ2RI2OTSJTNYOIGICST6DUXR"),
        response.getPath().get(0));
    assertEquals(
        new AssetTypeCreditAlphaNum4(
            "USDT", "GCQTGZQQ5G4PTM2GL7CDIFKUBIPEC52BROAQIAPW53XBRJVN6ZJVTG6V"),
        response.getPath().get(1));
    assertEquals("0.0005045", response.getAmount());
    assertEquals("57.4988800", response.getSourceAmount());
    assertEquals("0.0004995", response.getDestinationMin());
    assertEquals("native", response.getSourceAssetType());
    assertEquals(new AssetTypeNative(), response.getSourceAsset());
  }

  @Test
  public void testPaymentOperation() throws IOException {
    String filePath = "src/test/resources/responses/operations/payment.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    PaymentOperationResponse response =
        (PaymentOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals("payment", response.getType());
    assertEquals("credit_alphanum4", response.getAssetType());
    assertEquals("USDC", response.getAssetCode());
    assertEquals(
        "GA5ZSEJYB37JRC5AVCIA5MOP4RHTM335X2KGX3IHOJAPP5RE34K4KZVN", response.getAssetIssuer());
    assertEquals(
        new AssetTypeCreditAlphaNum4(
            "USDC", "GA5ZSEJYB37JRC5AVCIA5MOP4RHTM335X2KGX3IHOJAPP5RE34K4KZVN"),
        response.getAsset());
    assertEquals("GC5LF63GRVIT5ZXXCXLPI3RX2YXKJQFZVBSAO6AUELN3YIMSWPD6Z6FH", response.getFrom());
    assertEquals("GAFK7XFZHMLSNV7OJTBO7BAIZA66X6QIBV5RMZZYXK4Q7ZSO52J5C3WQ", response.getTo());
    assertEquals("39.0000000", response.getAmount());
  }

  @Test
  public void testRestoreFootprintOperation() throws IOException {
    String filePath = "src/test/resources/responses/operations/restore_footprint.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    RestoreFootprintOperationResponse response =
        (RestoreFootprintOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals("restore_footprint", response.getType());
  }

  @Test
  public void testRevokeSponsorshipOperation() throws IOException {
    String filePath = "src/test/resources/responses/operations/revoke_sponsorship.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    RevokeSponsorshipOperationResponse response =
        (RevokeSponsorshipOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals("revoke_sponsorship", response.getType());
    assertEquals(
        "GBAG5JOFNNE7B2FXLZGLV56U5NR2ZFHRS6FALKCX3W5CND27W4DM7TBX", response.getAccountId());
    // TODO: other types.
  }

  @Test
  public void testSetOptionsOperation() throws IOException {
    String filePath = "src/test/resources/responses/operations/set_options.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    SetOptionsOperationResponse response =
        (SetOptionsOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals("set_options", response.getType());
    assertEquals("example.com", response.getHomeDomain());
    assertEquals(
        "GDLSZ37Q6AJN6UNQPXIWNRAB3LETWAASA2H3OWNHRUWMG2LVU3A45S6D",
        response.getInflationDestination());
    assertEquals(10, response.getMasterKeyWeight().intValue());
    assertEquals(
        "GDLSZ37Q6AJN6UNQPXIWNRAB3LETWAASA2H3OWNHRUWMG2LVU3A45S6D", response.getSignerKey());
    assertEquals(10, response.getSignerWeight().intValue());
    assertArrayEquals(new Integer[] {1}, response.getSetFlags().toArray());
    assertArrayEquals(new Integer[] {2}, response.getClearFlags().toArray());
    assertArrayEquals(new String[] {"auth_required"}, response.getSetFlagStrings().toArray());
    assertArrayEquals(new String[] {"auth_revocable"}, response.getClearFlagStrings().toArray());
    assertEquals(1, response.getLowThreshold().intValue());
    assertEquals(10, response.getMedThreshold().intValue());
    assertEquals(20, response.getHighThreshold().intValue());
  }

  @Test
  public void testSetTrustLineFlagsOperation() throws IOException {
    String filePath = "src/test/resources/responses/operations/set_trust_line_flags.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    SetTrustLineFlagsOperationResponse response =
        (SetTrustLineFlagsOperationResponse)
            GsonSingleton.getInstance().fromJson(json, OperationResponse.class);

    assertEquals("set_trust_line_flags", response.getType());
    assertEquals("credit_alphanum12", response.getAssetType());
    assertEquals("Hello", response.getAssetCode());
    assertEquals(
        "GD5YHBKE7FSUUZIOSL4ED6UKMM2HZAYBYGZI7KRCTMFDTOO6SGZCQB4Z", response.getAssetIssuer());
    assertEquals(
        new AssetTypeCreditAlphaNum12(
            "Hello", "GD5YHBKE7FSUUZIOSL4ED6UKMM2HZAYBYGZI7KRCTMFDTOO6SGZCQB4Z"),
        response.getAsset());
    assertEquals("GAYWF2KJ4RVFBACNI7W2YVSLEQOUHEMPGJIZCDXHCF2BFR2V7O55UWBB", response.getTrustor());
    assertArrayEquals(new Integer[] {1}, response.getSetFlags().toArray());
    assertArrayEquals(new String[] {"authorized"}, response.getSetFlagStrings().toArray());
    assertArrayEquals(new Integer[] {4}, response.getClearFlags().toArray());
    assertArrayEquals(new String[] {"clawback_enabled"}, response.getClearFlagStrings().toArray());
  }
}
