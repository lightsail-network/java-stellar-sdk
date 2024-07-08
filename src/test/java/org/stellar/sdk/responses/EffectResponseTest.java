package org.stellar.sdk.responses;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeCreditAlphaNum12;
import org.stellar.sdk.AssetTypeCreditAlphaNum4;
import org.stellar.sdk.AssetTypeNative;
import org.stellar.sdk.Predicate;
import org.stellar.sdk.TrustLineAsset;
import org.stellar.sdk.responses.effects.AccountCreatedEffectResponse;
import org.stellar.sdk.responses.effects.AccountCreditedEffectResponse;
import org.stellar.sdk.responses.effects.AccountDebitedEffectResponse;
import org.stellar.sdk.responses.effects.AccountFlagsUpdatedEffectResponse;
import org.stellar.sdk.responses.effects.AccountHomeDomainUpdatedEffectResponse;
import org.stellar.sdk.responses.effects.AccountInflationDestinationUpdatedEffectResponse;
import org.stellar.sdk.responses.effects.AccountRemovedEffectResponse;
import org.stellar.sdk.responses.effects.AccountSponsorshipCreatedEffectResponse;
import org.stellar.sdk.responses.effects.AccountSponsorshipRemovedEffectResponse;
import org.stellar.sdk.responses.effects.AccountSponsorshipUpdatedEffectResponse;
import org.stellar.sdk.responses.effects.AccountThresholdsUpdatedEffectResponse;
import org.stellar.sdk.responses.effects.ClaimableBalanceClaimantCreatedEffectResponse;
import org.stellar.sdk.responses.effects.ClaimableBalanceClaimedEffectResponse;
import org.stellar.sdk.responses.effects.ClaimableBalanceClawedBackEffectResponse;
import org.stellar.sdk.responses.effects.ClaimableBalanceCreatedEffectResponse;
import org.stellar.sdk.responses.effects.ClaimableBalanceSponsorshipCreatedEffectResponse;
import org.stellar.sdk.responses.effects.ClaimableBalanceSponsorshipRemovedEffectResponse;
import org.stellar.sdk.responses.effects.ClaimableBalanceSponsorshipUpdatedEffectResponse;
import org.stellar.sdk.responses.effects.ContractCreditedEffectResponse;
import org.stellar.sdk.responses.effects.ContractDebitedEffectResponse;
import org.stellar.sdk.responses.effects.DataCreatedEffectResponse;
import org.stellar.sdk.responses.effects.DataRemovedEffectResponse;
import org.stellar.sdk.responses.effects.DataSponsorshipCreatedEffectResponse;
import org.stellar.sdk.responses.effects.DataSponsorshipRemovedEffectResponse;
import org.stellar.sdk.responses.effects.DataSponsorshipUpdatedEffectResponse;
import org.stellar.sdk.responses.effects.DataUpdatedEffectResponse;
import org.stellar.sdk.responses.effects.LiquidityPoolCreatedEffectResponse;
import org.stellar.sdk.responses.effects.LiquidityPoolDepositedEffectResponse;
import org.stellar.sdk.responses.effects.LiquidityPoolRemovedEffectResponse;
import org.stellar.sdk.responses.effects.LiquidityPoolRevokedEffectResponse;
import org.stellar.sdk.responses.effects.LiquidityPoolTradeEffectResponse;
import org.stellar.sdk.responses.effects.LiquidityPoolWithdrewEffectResponse;
import org.stellar.sdk.responses.effects.SequenceBumpedEffectResponse;
import org.stellar.sdk.responses.effects.SignerCreatedEffectResponse;
import org.stellar.sdk.responses.effects.SignerRemovedEffectResponse;
import org.stellar.sdk.responses.effects.SignerSponsorshipCreatedEffectResponse;
import org.stellar.sdk.responses.effects.SignerSponsorshipRemovedEffectResponse;
import org.stellar.sdk.responses.effects.SignerSponsorshipUpdatedEffectResponse;
import org.stellar.sdk.responses.effects.SignerUpdatedEffectResponse;
import org.stellar.sdk.responses.effects.TradeEffectResponse;
import org.stellar.sdk.responses.effects.TrustlineAuthorizedEffectResponse;
import org.stellar.sdk.responses.effects.TrustlineAuthorizedToMaintainLiabilitiesEffectResponse;
import org.stellar.sdk.responses.effects.TrustlineCreatedEffectResponse;
import org.stellar.sdk.responses.effects.TrustlineDeauthorizedEffectResponse;
import org.stellar.sdk.responses.effects.TrustlineFlagsUpdatedEffectResponse;
import org.stellar.sdk.responses.effects.TrustlineRemovedEffectResponse;
import org.stellar.sdk.responses.effects.TrustlineSponsorshipCreatedEffectResponse;
import org.stellar.sdk.responses.effects.TrustlineSponsorshipRemovedEffectResponse;
import org.stellar.sdk.responses.effects.TrustlineSponsorshipUpdatedEffectResponse;
import org.stellar.sdk.responses.effects.TrustlineUpdatedEffectResponse;
import org.stellar.sdk.responses.gson.GsonSingleton;

public class EffectResponseTest {
  @Test
  public void testBaseEffect() throws IOException {
    String filePath = "src/test/resources/responses/effects/liquidity_pool_withdrew.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    LiquidityPoolWithdrewEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, LiquidityPoolWithdrewEffectResponse.class);

    assertEquals(
        "https://horizon-testnet.stellar.org/operations/1579096265998337",
        response.getLinks().getOperation().getHref());
    assertEquals(
        "https://horizon-testnet.stellar.org/effects?order=desc&cursor=1579096265998337-1",
        response.getLinks().getSucceeds().getHref());
    assertEquals(
        "https://horizon-testnet.stellar.org/effects?order=asc&cursor=1579096265998337-1",
        response.getLinks().getPrecedes().getHref());

    assertEquals("0001579096265998337-0000000001", response.getId());
    assertEquals("1579096265998337-1", response.getPagingToken());
    assertEquals("GAQXAWHCM4A7SQCT3BOSVEGRI2OOB7LO2CMFOYFF6YRXU4VQSB5V2V2K", response.getAccount());
    assertEquals(
        "MAQXAWHCM4A7SQCT3BOSVEGRI2OOB7LO2CMFOYFF6YRXU4VQSB5V2AAAAAAAAE4DUGF2O",
        response.getAccountMuxed());
    assertEquals(1278881, response.getAccountMuxedId().longValue());
    assertEquals("liquidity_pool_withdrew", response.getType());
    assertEquals("2021-10-07T18:07:37Z", response.getCreatedAt());
  }

  @Test
  public void testAccountCreated() throws IOException {
    String filePath = "src/test/resources/responses/effects/account_created.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    AccountCreatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, AccountCreatedEffectResponse.class);

    assertEquals("account_created", response.getType());
    assertEquals("15.8675013", response.getStartingBalance());
  }

  @Test
  public void testAccountCredited() throws IOException {
    String filePath = "src/test/resources/responses/effects/account_credited.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    AccountCreditedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, AccountCreditedEffectResponse.class);

    assertEquals("account_credited", response.getType());
    assertEquals(new AssetTypeNative(), response.getAsset());
    assertEquals("3.9999700", response.getAmount());
  }

  @Test
  public void testAccountDebited() throws IOException {
    String filePath = "src/test/resources/responses/effects/account_debited.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    AccountDebitedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, AccountDebitedEffectResponse.class);

    assertEquals("account_debited", response.getType());
    assertEquals(new AssetTypeNative(), response.getAsset());
    assertEquals("15.8675013", response.getAmount());
  }

  @Test
  public void testAccountFlagsUpdated() throws IOException {
    String filePath = "src/test/resources/responses/effects/account_flags_updated.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    AccountFlagsUpdatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, AccountFlagsUpdatedEffectResponse.class);

    assertEquals("account_flags_updated", response.getType());
    assertTrue(response.getAuthRequiredFlag());
    assertFalse(response.getAuthRevokableFlag());
  }

  @Test
  public void testAccountHomeDomainUpdated() throws IOException {
    String filePath = "src/test/resources/responses/effects/account_home_domain_updated.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    AccountHomeDomainUpdatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, AccountHomeDomainUpdatedEffectResponse.class);

    assertEquals("account_home_domain_updated", response.getType());
  }

  @Test
  public void testAccountInflationDestinationUpdated() throws IOException {
    String filePath =
        "src/test/resources/responses/effects/account_inflation_destination_updated.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    AccountInflationDestinationUpdatedEffectResponse response =
        GsonSingleton.getInstance()
            .fromJson(json, AccountInflationDestinationUpdatedEffectResponse.class);

    assertEquals("account_inflation_destination_updated", response.getType());
  }

  @Test
  public void testAccountRemoved() throws IOException {
    String filePath = "src/test/resources/responses/effects/account_removed.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    AccountRemovedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, AccountRemovedEffectResponse.class);

    assertEquals("account_removed", response.getType());
  }

  @Test
  public void testAccountSponsorshipCreated() throws IOException {
    String filePath = "src/test/resources/responses/effects/account_sponsorship_created.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    AccountSponsorshipCreatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, AccountSponsorshipCreatedEffectResponse.class);

    assertEquals("account_sponsorship_created", response.getType());
    assertEquals("GCZGSFPITKVJPJERJIVLCQK5YIHYTDXCY45ZHU3IRCUC53SXSCAL44JV", response.getSponsor());
  }

  @Test
  public void testAccountSponsorshipRemoved() throws IOException {
    String filePath = "src/test/resources/responses/effects/account_sponsorship_removed.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    AccountSponsorshipRemovedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, AccountSponsorshipRemovedEffectResponse.class);

    assertEquals("account_sponsorship_removed", response.getType());
    assertEquals(
        "GA7PT6IPFVC4FGG273ZHGCNGG2O52F3B6CLVSI4SNIYOXLUNIOSFCK4F", response.getFormerSponsor());
  }

  @Test
  public void testAccountSponsorshipUpdated() throws IOException {
    String filePath = "src/test/resources/responses/effects/account_sponsorship_updated.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    AccountSponsorshipUpdatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, AccountSponsorshipUpdatedEffectResponse.class);

    assertEquals("account_sponsorship_updated", response.getType());
    assertEquals(
        "GA7PT6IPFVC4FGG273ZHGCNGG2O52F3B6CLVSI4SNIYOXLUNIOSFCK4F", response.getFormerSponsor());
    assertEquals(
        "GCZGSFPITKVJPJERJIVLCQK5YIHYTDXCY45ZHU3IRCUC53SXSCAL44JV", response.getNewSponsor());
  }

  @Test
  public void testAccountThresholdsUpdated() throws IOException {
    String filePath = "src/test/resources/responses/effects/account_thresholds_updated.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    AccountThresholdsUpdatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, AccountThresholdsUpdatedEffectResponse.class);

    assertEquals("account_thresholds_updated", response.getType());
    assertEquals(10, response.getLowThreshold().intValue());
    assertEquals(20, response.getMedThreshold().intValue());
    assertEquals(30, response.getHighThreshold().intValue());
  }

  @Test
  public void testClaimableBalanceClaimantCreated() throws IOException {
    String filePath =
        "src/test/resources/responses/effects/claimable_balance_claimant_created.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    ClaimableBalanceClaimantCreatedEffectResponse response =
        GsonSingleton.getInstance()
            .fromJson(json, ClaimableBalanceClaimantCreatedEffectResponse.class);

    assertEquals("claimable_balance_claimant_created", response.getType());
    assertEquals(
        Asset.create("USDPEND:GBHNGLLIE3KWGKCHIKMHJ5HVZHYIK7WTBE4QF5PLAKL4CJGSEU7HZIW5"),
        response.getAsset());
    assertEquals(
        "0000000048a70acdec712be9547d19f7e58adc22e35e0f5bcf3897a0353ab5dd4c5d61f4",
        response.getBalanceId());
    assertEquals("900.0000000", response.getAmount());
    assertEquals(new Predicate.Not(new Predicate.AbsBefore(1619409600)), response.getPredicate());
  }

  @Test
  public void testClaimableBalanceClaimed() throws IOException {
    String filePath = "src/test/resources/responses/effects/claimable_balance_claimed.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    ClaimableBalanceClaimedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, ClaimableBalanceClaimedEffectResponse.class);

    assertEquals("claimable_balance_claimed", response.getType());
    assertEquals(
        Asset.create("USDC:GA5ZSEJYB37JRC5AVCIA5MOP4RHTM335X2KGX3IHOJAPP5RE34K4KZVN"),
        response.getAsset());
    assertEquals(
        "0000000016cbeff27945d389e9123231ec916f7bb848c0579ceca12e2bfab5c34ce0da24",
        response.getBalanceId());
    assertEquals("1.0000000", response.getAmount());
  }

  @Test
  public void testClaimableBalanceClawedBack() throws IOException {
    String filePath = "src/test/resources/responses/effects/claimable_balance_clawed_back.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    ClaimableBalanceClawedBackEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, ClaimableBalanceClawedBackEffectResponse.class);

    assertEquals("claimable_balance_clawed_back", response.getType());
    assertEquals(
        "000000001fe36f3ce6ab6a6423b18b5947ce8890157ae77bb17faeb765814ad040b74ce1",
        response.getBalanceId());
  }

  @Test
  public void testClaimableBalanceCreated() throws IOException {
    String filePath = "src/test/resources/responses/effects/claimable_balance_created.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    ClaimableBalanceCreatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, ClaimableBalanceCreatedEffectResponse.class);

    assertEquals("claimable_balance_created", response.getType());
    assertEquals(
        Asset.create("USDPEND:GBHNGLLIE3KWGKCHIKMHJ5HVZHYIK7WTBE4QF5PLAKL4CJGSEU7HZIW5"),
        response.getAsset());
    assertEquals(
        "0000000048a70acdec712be9547d19f7e58adc22e35e0f5bcf3897a0353ab5dd4c5d61f4",
        response.getBalanceId());
    assertEquals("900.0000000", response.getAmount());
  }

  @Test
  public void testClaimableBalanceSponsorshipCreated() throws IOException {
    String filePath =
        "src/test/resources/responses/effects/claimable_balance_sponsorship_created.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    ClaimableBalanceSponsorshipCreatedEffectResponse response =
        GsonSingleton.getInstance()
            .fromJson(json, ClaimableBalanceSponsorshipCreatedEffectResponse.class);

    assertEquals("claimable_balance_sponsorship_created", response.getType());
    assertEquals(
        "0000000048a70acdec712be9547d19f7e58adc22e35e0f5bcf3897a0353ab5dd4c5d61f4",
        response.getBalanceId());
    assertEquals("GBGJB2WEIQCCUZYISUKAFRPR46LQ62O7W6CDKN52NVROG44LLL3L73X2", response.getSponsor());
  }

  @Test
  public void testClaimableBalanceSponsorshipRemoved() throws IOException {
    String filePath =
        "src/test/resources/responses/effects/claimable_balance_sponsorship_removed.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    ClaimableBalanceSponsorshipRemovedEffectResponse response =
        GsonSingleton.getInstance()
            .fromJson(json, ClaimableBalanceSponsorshipRemovedEffectResponse.class);

    assertEquals("claimable_balance_sponsorship_removed", response.getType());
    assertEquals(
        "0000000016cbeff27945d389e9123231ec916f7bb848c0579ceca12e2bfab5c34ce0da24",
        response.getBalanceId());
    assertEquals(
        "GDDGK5C7UQWC7AEFZZVO7KXRXZVP2BBQJ2IQFAIROKME2O3XQR2CMVC7", response.getFormerSponsor());
  }

  @Test
  public void testClaimableBalanceSponsorshipUpdated() throws IOException {
    String filePath =
        "src/test/resources/responses/effects/claimable_balance_sponsorship_updated.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    ClaimableBalanceSponsorshipUpdatedEffectResponse response =
        GsonSingleton.getInstance()
            .fromJson(json, ClaimableBalanceSponsorshipUpdatedEffectResponse.class);

    assertEquals("claimable_balance_sponsorship_updated", response.getType());
    assertEquals(
        "0000000016cbeff27945d389e9123231ec916f7bb848c0579ceca12e2bfab5c34ce0da24",
        response.getBalanceId());
    assertEquals(
        "GDDGK5C7UQWC7AEFZZVO7KXRXZVP2BBQJ2IQFAIROKME2O3XQR2CMVC7", response.getFormerSponsor());
    assertEquals(
        "GBGJB2WEIQCCUZYISUKAFRPR46LQ62O7W6CDKN52NVROG44LLL3L73X2", response.getNewSponsor());
  }

  @Test
  public void testContractCredited() throws IOException {
    String filePath = "src/test/resources/responses/effects/contract_credited.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    ContractCreditedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, ContractCreditedEffectResponse.class);

    assertEquals("contract_credited", response.getType());
    assertEquals(new AssetTypeNative(), response.getAsset());
    assertEquals("100.0000000", response.getAmount());
    assertEquals(
        "CDCYWK73YTYFJZZSJ5V7EDFNHYBG4QN3VUNG2IGD27KJDDPNCZKBCBXK", response.getContract());
  }

  @Test
  public void testContractDebited() throws IOException {
    String filePath = "src/test/resources/responses/effects/contract_debited.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    ContractDebitedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, ContractDebitedEffectResponse.class);

    assertEquals("contract_debited", response.getType());
    assertEquals(new AssetTypeNative(), response.getAsset());
    assertEquals("100.0000000", response.getAmount());
    assertEquals(
        "CDCYWK73YTYFJZZSJ5V7EDFNHYBG4QN3VUNG2IGD27KJDDPNCZKBCBXK", response.getContract());
  }

  @Test
  public void testDataCreated() throws IOException {
    String filePath = "src/test/resources/responses/effects/data_created.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    DataCreatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, DataCreatedEffectResponse.class);

    assertEquals("data_created", response.getType());
    assertEquals("MESSAGE_DATA_0", response.getName());
    assertEquals(
        "UW1mNkhpemtkRTRWdDdrTmFDRzhiUnJ4WnlvamtQd2ZIcHdUUE1WQzlzZTNHbw==", response.getValue());
    assertArrayEquals(
        "Qmf6HizkdE4Vt7kNaCG8bRrxZyojkPwfHpwTPMVC9se3Go".getBytes(StandardCharsets.UTF_8),
        response.getDecodedValue());
  }

  @Test
  public void testDataRemoved() throws IOException {
    String filePath = "src/test/resources/responses/effects/data_removed.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    DataRemovedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, DataRemovedEffectResponse.class);

    assertEquals("data_removed", response.getType());
    assertEquals("MESSAGE_DATA_0", response.getName());
  }

  @Test
  public void testDataSponsorshipCreated() throws IOException {
    String filePath = "src/test/resources/responses/effects/data_sponsorship_created.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    DataSponsorshipCreatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, DataSponsorshipCreatedEffectResponse.class);

    assertEquals("data_sponsorship_created", response.getType());
    assertEquals("hello", response.getDataName());
    assertEquals("GDDQTK5V3E3JFGLZZTJTKURTVY7QJPNQLTR5QS5HIWZWY5XPYIO5YELN", response.getSponsor());
  }

  @Test
  public void testDataSponsorshipRemoved() throws IOException {
    String filePath = "src/test/resources/responses/effects/data_sponsorship_removed.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    DataSponsorshipRemovedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, DataSponsorshipRemovedEffectResponse.class);

    assertEquals("data_sponsorship_removed", response.getType());
    assertEquals("hello", response.getDataName());
    assertEquals(
        "GDDQTK5V3E3JFGLZZTJTKURTVY7QJPNQLTR5QS5HIWZWY5XPYIO5YELN", response.getFormerSponsor());
  }

  @Test
  public void testDataSponsorshipUpdated() throws IOException {
    String filePath = "src/test/resources/responses/effects/data_sponsorship_updated.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    DataSponsorshipUpdatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, DataSponsorshipUpdatedEffectResponse.class);

    assertEquals("data_sponsorship_updated", response.getType());
    assertEquals("hello", response.getDataName());
    assertEquals(
        "GDDQTK5V3E3JFGLZZTJTKURTVY7QJPNQLTR5QS5HIWZWY5XPYIO5YELN", response.getFormerSponsor());
    assertEquals(
        "GBLZGNXYDTZN3Q2FP4WWEKFO5DZJ2ED43S6YMUFD7ZO6B6LNIEOKWUPX", response.getNewSponsor());
  }

  @Test
  public void testDataUpdated() throws IOException {
    String filePath = "src/test/resources/responses/effects/data_updated.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    DataUpdatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, DataUpdatedEffectResponse.class);

    assertEquals("data_updated", response.getType());
    assertEquals("MESSAGE_DATA_1", response.getName());
    assertEquals(
        "UW1VNnU3UWZZcG9iZkRUTGt6N0VYTmpxZ1o5bkYza2lMTllQWHl1VzE0YTNGcw==", response.getValue());
    assertArrayEquals(
        "QmU6u7QfYpobfDTLkz7EXNjqgZ9nF3kiLNYPXyuW14a3Fs".getBytes(StandardCharsets.UTF_8),
        response.getDecodedValue());
  }

  @Test
  public void testLiquidityPoolCreated() throws IOException {
    String filePath = "src/test/resources/responses/effects/liquidity_pool_created.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    LiquidityPoolCreatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, LiquidityPoolCreatedEffectResponse.class);

    assertEquals("liquidity_pool_created", response.getType());
    assertEquals(
        "2c0bfa623845dd101cbf074a1ca1ae4b2458cc8d0104ad65939ebe2cd9054355",
        response.getLiquidityPool().getId());
    assertEquals(30, response.getLiquidityPool().getFeeBP().intValue());
    assertEquals("constant_product", response.getLiquidityPool().getType());
    assertEquals(1, response.getLiquidityPool().getTotalTrustlines().intValue());
    assertEquals("0.0000000", response.getLiquidityPool().getTotalShares());
    assertEquals(2, response.getLiquidityPool().getReserves().size());
    assertEquals(
        Asset.create("COOL:GAZKB7OEYRUVL6TSBXI74D2IZS4JRCPBXJZ37MDDYAEYBOMHXUYIX5YL"),
        response.getLiquidityPool().getReserves().get(0).getAsset());
    assertEquals("0.0000000", response.getLiquidityPool().getReserves().get(0).getAmount());
    assertEquals(
        Asset.create("SONESO:GAOF7ARG3ZAVUA63GCLXG5JQTMBAH3ZFYHGLGJLDXGDSXQRHD72LLGOB"),
        response.getLiquidityPool().getReserves().get(1).getAsset());
    assertEquals("0.0000000", response.getLiquidityPool().getReserves().get(1).getAmount());
  }

  @Test
  public void testLiquidityPoolDeposited() throws IOException {
    String filePath = "src/test/resources/responses/effects/liquidity_pool_deposited.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    LiquidityPoolDepositedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, LiquidityPoolDepositedEffectResponse.class);

    assertEquals("liquidity_pool_deposited", response.getType());
    assertEquals(
        "2c0bfa623845dd101cbf074a1ca1ae4b2458cc8d0104ad65939ebe2cd9054355",
        response.getLiquidityPool().getId());
    assertEquals(30, response.getLiquidityPool().getFeeBP().intValue());
    assertEquals("constant_product", response.getLiquidityPool().getType());
    assertEquals(1, response.getLiquidityPool().getTotalTrustlines().intValue());
    assertEquals("200.0000000", response.getLiquidityPool().getTotalShares());
    assertEquals(2, response.getLiquidityPool().getReserves().size());
    assertEquals(
        Asset.create("COOL:GAZKB7OEYRUVL6TSBXI74D2IZS4JRCPBXJZ37MDDYAEYBOMHXUYIX5YL"),
        response.getLiquidityPool().getReserves().get(0).getAsset());
    assertEquals("250.0000000", response.getLiquidityPool().getReserves().get(0).getAmount());
    assertEquals(
        Asset.create("SONESO:GAOF7ARG3ZAVUA63GCLXG5JQTMBAH3ZFYHGLGJLDXGDSXQRHD72LLGOB"),
        response.getLiquidityPool().getReserves().get(1).getAsset());
    assertEquals("250.0000000", response.getLiquidityPool().getReserves().get(1).getAmount());
    assertEquals(2, response.getReservesDeposited().size());
    assertEquals(
        Asset.create("COOL:GAZKB7OEYRUVL6TSBXI74D2IZS4JRCPBXJZ37MDDYAEYBOMHXUYIX5YL"),
        response.getReservesDeposited().get(0).getAsset());
    assertEquals("250.0000000", response.getReservesDeposited().get(0).getAmount());
    assertEquals(
        Asset.create("SONESO:GAOF7ARG3ZAVUA63GCLXG5JQTMBAH3ZFYHGLGJLDXGDSXQRHD72LLGOB"),
        response.getReservesDeposited().get(1).getAsset());
    assertEquals("250.0000000", response.getReservesDeposited().get(1).getAmount());
    assertEquals("250.0000000", response.getSharesReceived());
  }

  @Test
  public void testLiquidityPoolRemoved() throws IOException {
    String filePath = "src/test/resources/responses/effects/liquidity_pool_removed.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    LiquidityPoolRemovedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, LiquidityPoolRemovedEffectResponse.class);

    assertEquals("liquidity_pool_removed", response.getType());
    assertEquals(
        "89c11017d16552c152536092d7440a2cd4cf4bf7df2c7e7552b56e6bcac98d95",
        response.getLiquidityPoolId());
  }

  @Test
  public void testLiquidityPoolRevoked() throws IOException {
    String filePath = "src/test/resources/responses/effects/liquidity_pool_revoked.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    LiquidityPoolRevokedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, LiquidityPoolRevokedEffectResponse.class);

    assertEquals("liquidity_pool_revoked", response.getType());
    assertEquals(
        "a6cad36777565bf0d52f89319416fb5e73149d07b9814c5baaddea0d53ef2baa",
        response.getLiquidityPool().getId());
    assertEquals(30, response.getLiquidityPool().getFeeBP().intValue());
    assertEquals("constant_product", response.getLiquidityPool().getType());
    assertEquals(1, response.getLiquidityPool().getTotalTrustlines().intValue());
    assertEquals("1.0000000", response.getLiquidityPool().getTotalShares());
    assertEquals(2, response.getLiquidityPool().getReserves().size());
    assertEquals(
        Asset.create("native"), response.getLiquidityPool().getReserves().get(0).getAsset());
    assertEquals("0.0000011", response.getLiquidityPool().getReserves().get(0).getAmount());
    assertEquals(
        Asset.create("BTC:GAMQXNIL2IV7YV3GIBQG56RCJAGTCW3WU64XDOM2M5N7A3OJWQZT5BNB"),
        response.getLiquidityPool().getReserves().get(1).getAsset());
    assertEquals("1000502.0030091", response.getLiquidityPool().getReserves().get(1).getAmount());
    assertEquals(2, response.getReservesRevoked().size());
    assertEquals(Asset.create("native"), response.getReservesRevoked().get(0).getAsset());
    assertEquals("0.0000011", response.getReservesRevoked().get(0).getAmount());
    assertEquals(
        "00000000b69563dc3491932aa21baf799f7f1831831c7fc4b21ea8eac97578b48ddc884c",
        response.getReservesRevoked().get(0).getClaimableBalanceID());
    assertEquals(
        Asset.create("BTC:GAMQXNIL2IV7YV3GIBQG56RCJAGTCW3WU64XDOM2M5N7A3OJWQZT5BNB"),
        response.getReservesRevoked().get(1).getAsset());
    assertEquals("1000502.0030091", response.getReservesRevoked().get(1).getAmount());
    assertEquals(
        "000000006708d006dc9d6b8601249383b25ac17198596493ff80c8dd8e6218b0c44ef472",
        response.getReservesRevoked().get(1).getClaimableBalanceID());
    assertEquals("0.5000000", response.getSharesRevoked());
  }

  @Test
  public void testLiquidityPoolTrade() throws IOException {
    String filePath = "src/test/resources/responses/effects/liquidity_pool_trade.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    LiquidityPoolTradeEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, LiquidityPoolTradeEffectResponse.class);

    assertEquals("liquidity_pool_trade", response.getType());
    assertEquals(
        "2c0bfa623845dd101cbf074a1ca1ae4b2458cc8d0104ad65939ebe2cd9054355",
        response.getLiquidityPool().getId());
    assertEquals(30, response.getLiquidityPool().getFeeBP().intValue());
    assertEquals("constant_product", response.getLiquidityPool().getType());
    assertEquals(1, response.getLiquidityPool().getTotalTrustlines().intValue());
    assertEquals("400.0000000", response.getLiquidityPool().getTotalShares());
    assertEquals(2, response.getLiquidityPool().getReserves().size());
    assertEquals(
        Asset.create("COOL:GAZKB7OEYRUVL6TSBXI74D2IZS4JRCPBXJZ37MDDYAEYBOMHXUYIX5YL"),
        response.getLiquidityPool().getReserves().get(0).getAsset());
    assertEquals("381.0068105", response.getLiquidityPool().getReserves().get(0).getAmount());
    assertEquals(
        Asset.create("SONESO:GAOF7ARG3ZAVUA63GCLXG5JQTMBAH3ZFYHGLGJLDXGDSXQRHD72LLGOB"),
        response.getLiquidityPool().getReserves().get(1).getAsset());
    assertEquals("420.0000000", response.getLiquidityPool().getReserves().get(1).getAmount());
    assertEquals(
        Asset.create("COOL:GAZKB7OEYRUVL6TSBXI74D2IZS4JRCPBXJZ37MDDYAEYBOMHXUYIX5YL"),
        response.getSold().getAsset());
    assertEquals("18.9931895", response.getSold().getAmount());
    assertEquals(
        Asset.create("SONESO:GAOF7ARG3ZAVUA63GCLXG5JQTMBAH3ZFYHGLGJLDXGDSXQRHD72LLGOB"),
        response.getBought().getAsset());
    assertEquals("20.0000000", response.getBought().getAmount());
  }

  @Test
  public void testLiquidityPoolWithdrew() throws IOException {
    String filePath = "src/test/resources/responses/effects/liquidity_pool_withdrew.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    LiquidityPoolWithdrewEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, LiquidityPoolWithdrewEffectResponse.class);

    assertEquals("liquidity_pool_withdrew", response.getType());
    assertEquals(
        "2c0bfa623845dd101cbf074a1ca1ae4b2458cc8d0104ad65939ebe2cd9054355",
        response.getLiquidityPool().getId());
    assertEquals(30, response.getLiquidityPool().getFeeBP().intValue());
    assertEquals("constant_product", response.getLiquidityPool().getType());
    assertEquals(1, response.getLiquidityPool().getTotalTrustlines().intValue());
    assertEquals("400.0000000", response.getLiquidityPool().getTotalShares());
    assertEquals(2, response.getLiquidityPool().getReserves().size());
    assertEquals(
        Asset.create("COOL:GAZKB7OEYRUVL6TSBXI74D2IZS4JRCPBXJZ37MDDYAEYBOMHXUYIX5YL"),
        response.getLiquidityPool().getReserves().get(0).getAsset());
    assertEquals("400.0000000", response.getLiquidityPool().getReserves().get(0).getAmount());
    assertEquals(
        Asset.create("SONESO:GAOF7ARG3ZAVUA63GCLXG5JQTMBAH3ZFYHGLGJLDXGDSXQRHD72LLGOB"),
        response.getLiquidityPool().getReserves().get(1).getAsset());
    assertEquals("400.0000000", response.getLiquidityPool().getReserves().get(1).getAmount());
    assertEquals(2, response.getReservesReceived().size());
    assertEquals(
        Asset.create("COOL:GAZKB7OEYRUVL6TSBXI74D2IZS4JRCPBXJZ37MDDYAEYBOMHXUYIX5YL"),
        response.getReservesReceived().get(0).getAsset());
    assertEquals("100.0000000", response.getReservesReceived().get(0).getAmount());
    assertEquals(
        Asset.create("SONESO:GAOF7ARG3ZAVUA63GCLXG5JQTMBAH3ZFYHGLGJLDXGDSXQRHD72LLGOB"),
        response.getReservesReceived().get(1).getAsset());
    assertEquals("100.0000000", response.getReservesReceived().get(1).getAmount());
    assertEquals("100.0000000", response.getSharesRedeemed());
  }

  // https://github.com/stellar/go/blob/188558412d74c122f9cbc6f76ff575d12f9a396d/protocols/horizon/effects/main.go#L96
  // unused
  // offer_created
  // offer_removed
  // offer_updated

  @Test
  public void testSequenceBumped() throws IOException {
    String filePath = "src/test/resources/responses/effects/sequence_bumped.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    SequenceBumpedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, SequenceBumpedEffectResponse.class);

    assertEquals("sequence_bumped", response.getType());
    assertEquals(108136397361122527L, response.getNewSequence().longValue());
  }

  @Test
  public void testSignerCreated() throws IOException {
    String filePath = "src/test/resources/responses/effects/signer_created.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    SignerCreatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, SignerCreatedEffectResponse.class);

    assertEquals("signer_created", response.getType());
    assertEquals(1, response.getWeight().intValue());
    assertEquals(
        "GCQPKZEC6VNFPDJMK73ET7JKKMN65BWYHCWF3Z65ZZPAL4E7DPWHP3YY", response.getPublicKey());
  }

  @Test
  public void testSignerRemoved() throws IOException {
    String filePath = "src/test/resources/responses/effects/signer_removed.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    SignerRemovedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, SignerRemovedEffectResponse.class);

    assertEquals("signer_removed", response.getType());
    assertEquals(0, response.getWeight().intValue());
    assertEquals(
        "GDUQFAHWHQ6AUP6Q5MDAHILRG222CRF35HPUVRI66L7HXKHFJAQGHICR", response.getPublicKey());
  }

  @Test
  public void testSignerSponsorshipCreated() throws IOException {
    String filePath = "src/test/resources/responses/effects/signer_sponsorship_created.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    SignerSponsorshipCreatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, SignerSponsorshipCreatedEffectResponse.class);

    assertEquals("signer_sponsorship_created", response.getType());
    assertEquals("GD6632TYLXUKGVFNQYSC2AC752YZWR7VFNJZ5X7HYPKBLZKK5YVWQ54S", response.getSigner());
    assertEquals("GCZGSFPITKVJPJERJIVLCQK5YIHYTDXCY45ZHU3IRCUC53SXSCAL44JV", response.getSponsor());
  }

  @Test
  public void testSignerSponsorshipRemoved() throws IOException {
    String filePath = "src/test/resources/responses/effects/signer_sponsorship_removed.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    SignerSponsorshipRemovedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, SignerSponsorshipRemovedEffectResponse.class);

    assertEquals("signer_sponsorship_removed", response.getType());
    assertEquals("GBIQ43HRJ3HKDRR3AYV25VYWQAQHZ7RWFBNUOU755FNY2O5UIFQD5TRD", response.getSigner());
    assertEquals(
        "GCZGSFPITKVJPJERJIVLCQK5YIHYTDXCY45ZHU3IRCUC53SXSCAL44JV", response.getFormerSponsor());
  }

  @Test
  public void testSignerSponsorshipUpdated() throws IOException {
    String filePath = "src/test/resources/responses/effects/signer_sponsorship_updated.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    SignerSponsorshipUpdatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, SignerSponsorshipUpdatedEffectResponse.class);

    assertEquals("signer_sponsorship_updated", response.getType());
    assertEquals("GBIQ43HRJ3HKDRR3AYV25VYWQAQHZ7RWFBNUOU755FNY2O5UIFQD5TRD", response.getSigner());
    assertEquals(
        "GCZGSFPITKVJPJERJIVLCQK5YIHYTDXCY45ZHU3IRCUC53SXSCAL44JV", response.getFormerSponsor());
    assertEquals(
        "GAZKB7OEYRUVL6TSBXI74D2IZS4JRCPBXJZ37MDDYAEYBOMHXUYIX5YL", response.getNewSponsor());
  }

  @Test
  public void testSignerUpdated() throws IOException {
    String filePath = "src/test/resources/responses/effects/signer_updated.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    SignerUpdatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, SignerUpdatedEffectResponse.class);

    assertEquals("signer_updated", response.getType());
    assertEquals(10, response.getWeight().intValue());
    assertEquals(
        "GC52GNOWS6DHRDVNA3ZZ7J6S52FZAQR3V5GIKVJPNMD6MOSQINEP672L", response.getPublicKey());
  }

  @Test
  public void testTrade() throws IOException {
    String filePath = "src/test/resources/responses/effects/trade.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TradeEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, TradeEffectResponse.class);

    assertEquals("trade", response.getType());
    assertEquals("GCMYQPCR2FZ44ARPCWDX65TEYIQFXPOWTMHZCXAT4MDDEOBPI5S5EBX2", response.getSeller());
    assertEquals(545489795L, response.getOfferId().longValue());
    assertEquals("1.9295894", response.getSoldAmount());
    assertEquals("credit_alphanum12", response.getSoldAssetType());
    assertEquals("BUSD1", response.getSoldAssetCode());
    assertEquals(
        "GCIEGKAZ4ZUM4PEBTDXUMG6N4ZXOTCCK3UMSJCJ33QE5SVMCTTBKCVNY", response.getSoldAssetIssuer());
    assertEquals(
        new AssetTypeCreditAlphaNum12(
            "BUSD1", "GCIEGKAZ4ZUM4PEBTDXUMG6N4ZXOTCCK3UMSJCJ33QE5SVMCTTBKCVNY"),
        response.getSoldAsset());
    assertEquals("4.3525833", response.getBoughtAmount());
    assertEquals("native", response.getBoughtAssetType());
    assertEquals(new AssetTypeNative(), response.getBoughtAsset());
  }

  @Test
  public void testTrustlineAuthorized() throws IOException {
    String filePath = "src/test/resources/responses/effects/trustline_authorized.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TrustlineAuthorizedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, TrustlineAuthorizedEffectResponse.class);

    assertEquals("trustline_authorized", response.getType());
    assertEquals("GBL73HAKZGDGPSLOHI543CSK7FVJSMLHSIRUZRBH7SV43GM7IQWS7QET", response.getTrustor());
    assertEquals("credit_alphanum12", response.getAssetType());
    assertEquals("MSCIW", response.getAssetCode());
  }

  @Test
  public void testTrustlineAuthorizedToMaintainLiabilities() throws IOException {
    String filePath =
        "src/test/resources/responses/effects/trustline_authorized_to_maintain_liabilities.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TrustlineAuthorizedToMaintainLiabilitiesEffectResponse response =
        GsonSingleton.getInstance()
            .fromJson(json, TrustlineAuthorizedToMaintainLiabilitiesEffectResponse.class);

    assertEquals("trustline_authorized_to_maintain_liabilities", response.getType());
    assertEquals("GBL73HAKZGDGPSLOHI543CSK7FVJSMLHSIRUZRBH7SV43GM7IQWS7QET", response.getTrustor());
    assertEquals("credit_alphanum12", response.getAssetType());
    assertEquals("MSCIW", response.getAssetCode());
  }

  @Test
  public void testTrustlineCreated() throws IOException {
    String filePath = "src/test/resources/responses/effects/trustline_created.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TrustlineCreatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, TrustlineCreatedEffectResponse.class);

    assertEquals("trustline_created", response.getType());
    assertEquals("credit_alphanum12", response.getAssetType());
    assertEquals("DOGET", response.getAssetCode());
    assertEquals(
        "GDOEVDDBU6OBWKL7VHDAOKD77UP4DKHQYKOKJJT5PR3WRDBTX35HUEUX", response.getAssetIssuer());
    assertEquals("922337203685.4775807", response.getLimit());
  }

  @Test
  public void testTrustlineDeauthorized() throws IOException {
    String filePath = "src/test/resources/responses/effects/trustline_deauthorized.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TrustlineDeauthorizedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, TrustlineDeauthorizedEffectResponse.class);

    assertEquals("trustline_deauthorized", response.getType());
    assertEquals("GCLF6MCQFP2XJ7M46JUCO3CFZDNVXXC6NNKGFSPQXED6OVMUOUZ3HLNE", response.getTrustor());
    assertEquals("credit_alphanum4", response.getAssetType());
    assertEquals("LPTK", response.getAssetCode());
  }

  @Test
  public void testTrustlineFlagsUpdated() throws IOException {
    String filePath = "src/test/resources/responses/effects/trustline_flags_updated.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TrustlineFlagsUpdatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, TrustlineFlagsUpdatedEffectResponse.class);

    assertEquals("trustline_flags_updated", response.getType());
    assertEquals("credit_alphanum12", response.getAssetType());
    assertEquals("MSCIW", response.getAssetCode());
    assertEquals(
        "GBRDHSZL4ZKOI2PTUMM53N3NICZXC5OX3KPCD4WD4NG4XGCBC2ZA3KAG", response.getAssetIssuer());
    assertEquals("GBL73HAKZGDGPSLOHI543CSK7FVJSMLHSIRUZRBH7SV43GM7IQWS7QET", response.getTrustor());
    assertTrue(response.getAuthorizedFlag());
    assertFalse(response.getAuthorizedToMaintainLiabilitiesFlag());
    assertTrue(response.getClawbackEnabledFlag());
  }

  @Test
  public void testTrustlineRemoved() throws IOException {
    String filePath = "src/test/resources/responses/effects/trustline_removed.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TrustlineRemovedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, TrustlineRemovedEffectResponse.class);

    assertEquals("trustline_removed", response.getType());
    assertEquals("credit_alphanum4", response.getAssetType());
    assertEquals("TERN", response.getAssetCode());
    assertEquals(
        "GDGQDVO6XPFSY4NMX75A7AOVYCF5JYGW2SHCJJNWCQWIDGOZB53DGP6C", response.getAssetIssuer());
    assertEquals("0.0000000", response.getLimit());
  }

  @Test
  public void testTrustlineSponsorshipCreated() throws IOException {
    String filePath = "src/test/resources/responses/effects/trustline_sponsorship_created.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TrustlineSponsorshipCreatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, TrustlineSponsorshipCreatedEffectResponse.class);

    assertEquals("trustline_sponsorship_created", response.getType());
    assertEquals("credit_alphanum4", response.getAssetType());
    assertEquals(
        new AssetTypeCreditAlphaNum4(
            "USDC", "GA5ZSEJYB37JRC5AVCIA5MOP4RHTM335X2KGX3IHOJAPP5RE34K4KZVN"),
        response.getAsset());
    assertEquals("GCZGSFPITKVJPJERJIVLCQK5YIHYTDXCY45ZHU3IRCUC53SXSCAL44JV", response.getSponsor());
  }

  @Test
  public void testTrustlineSponsorshipRemoved() throws IOException {
    String filePath = "src/test/resources/responses/effects/trustline_sponsorship_removed.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TrustlineSponsorshipRemovedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, TrustlineSponsorshipRemovedEffectResponse.class);

    assertEquals("trustline_sponsorship_removed", response.getType());
    assertEquals("credit_alphanum12", response.getAssetType());
    assertEquals(
        new AssetTypeCreditAlphaNum12(
            "SQ0202", "GCZODXV5HXRHHOZHWE57LMWIELKAXPKC64SOEBGTK7BV4GMRMOKYDIQQ"),
        response.getAsset());
    assertEquals(
        "GA7PT6IPFVC4FGG273ZHGCNGG2O52F3B6CLVSI4SNIYOXLUNIOSFCK4F", response.getFormerSponsor());
  }

  @Test
  public void testTrustlineSponsorshipUpdated() throws IOException {
    String filePath = "src/test/resources/responses/effects/trustline_sponsorship_updated.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TrustlineSponsorshipUpdatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, TrustlineSponsorshipUpdatedEffectResponse.class);

    assertEquals("trustline_sponsorship_updated", response.getType());
    assertEquals("credit_alphanum12", response.getAssetType());
    assertEquals(
        new AssetTypeCreditAlphaNum12(
            "SQ0202", "GCZODXV5HXRHHOZHWE57LMWIELKAXPKC64SOEBGTK7BV4GMRMOKYDIQQ"),
        response.getAsset());
    assertEquals(
        "GA7PT6IPFVC4FGG273ZHGCNGG2O52F3B6CLVSI4SNIYOXLUNIOSFCK4F", response.getFormerSponsor());
    assertEquals(
        "GB6JGOVUP3UXRPA2BUAUF6YGGZSRWQUMPTHVRSRATVYGFOYUYRPWJACK", response.getNewSponsor());
  }

  @Test
  public void testTrustlineUpdated() throws IOException {
    String filePath = "src/test/resources/responses/effects/trustline_updated.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TrustlineUpdatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, TrustlineUpdatedEffectResponse.class);

    assertEquals("trustline_updated", response.getType());
    assertEquals("credit_alphanum12", response.getAssetType());
    assertEquals("DOGET", response.getAssetCode());
    assertEquals(
        "GDOEVDDBU6OBWKL7VHDAOKD77UP4DKHQYKOKJJT5PR3WRDBTX35HUEUX", response.getAssetIssuer());
    assertEquals("922337203685.4775807", response.getLimit());
    assertEquals(
        new TrustLineAsset(
            new AssetTypeCreditAlphaNum12(
                "DOGET", "GDOEVDDBU6OBWKL7VHDAOKD77UP4DKHQYKOKJJT5PR3WRDBTX35HUEUX")),
        response.getTrustLineAsset());
    // TODO: test liquidity pool id
  }
}
