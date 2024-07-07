package org.stellar.sdk.responses;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
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
import org.stellar.sdk.responses.effects.OfferCreatedEffectResponse;
import org.stellar.sdk.responses.effects.OfferRemovedEffectResponse;
import org.stellar.sdk.responses.effects.OfferUpdatedEffectResponse;
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

public class EffectResponseTest {
  // TODO: test common

  @Test
  public void testAccountCreated() throws IOException {
    String filePath = "src/test/resources/responses/effects/account_created.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    AccountCreatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, AccountCreatedEffectResponse.class);

    assertEquals("account_created", response.getType());
  }

  @Test
  public void testAccountCredited() throws IOException {
    String filePath = "src/test/resources/responses/effects/account_credited.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    AccountCreditedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, AccountCreditedEffectResponse.class);

    assertEquals("account_credited", response.getType());
  }

  @Test
  public void testAccountDebited() throws IOException {
    String filePath = "src/test/resources/responses/effects/account_debited.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    AccountDebitedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, AccountDebitedEffectResponse.class);

    assertEquals("account_debited", response.getType());
  }

  @Test
  public void testAccountFlagsUpdated() throws IOException {
    String filePath = "src/test/resources/responses/effects/account_flags_updated.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    AccountFlagsUpdatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, AccountFlagsUpdatedEffectResponse.class);

    assertEquals("account_flags_updated", response.getType());
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
  }

  @Test
  public void testAccountSponsorshipRemoved() throws IOException {
    String filePath = "src/test/resources/responses/effects/account_sponsorship_removed.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    AccountSponsorshipRemovedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, AccountSponsorshipRemovedEffectResponse.class);

    assertEquals("account_sponsorship_removed", response.getType());
  }

  @Test
  public void testAccountSponsorshipUpdated() throws IOException {
    String filePath = "src/test/resources/responses/effects/account_sponsorship_updated.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    AccountSponsorshipUpdatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, AccountSponsorshipUpdatedEffectResponse.class);

    assertEquals("account_sponsorship_updated", response.getType());
  }

  @Test
  public void testAccountThresholdsUpdated() throws IOException {
    String filePath = "src/test/resources/responses/effects/account_thresholds_updated.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    AccountThresholdsUpdatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, AccountThresholdsUpdatedEffectResponse.class);

    assertEquals("account_thresholds_updated", response.getType());
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
  }

  @Test
  public void testClaimableBalanceClaimed() throws IOException {
    String filePath = "src/test/resources/responses/effects/claimable_balance_claimed.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    ClaimableBalanceClaimedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, ClaimableBalanceClaimedEffectResponse.class);

    assertEquals("claimable_balance_claimed", response.getType());
  }

  @Test
  public void testClaimableBalanceClawedBack() throws IOException {
    String filePath = "src/test/resources/responses/effects/claimable_balance_clawed_back.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    ClaimableBalanceClawedBackEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, ClaimableBalanceClawedBackEffectResponse.class);

    assertEquals("claimable_balance_clawed_back", response.getType());
  }

  @Test
  public void testClaimableBalanceCreated() throws IOException {
    String filePath = "src/test/resources/responses/effects/claimable_balance_created.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    ClaimableBalanceCreatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, ClaimableBalanceCreatedEffectResponse.class);

    assertEquals("claimable_balance_created", response.getType());
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
  }

  @Test
  public void testContractCredited() throws IOException {
    String filePath = "src/test/resources/responses/effects/contract_credited.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    ContractCreditedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, ContractCreditedEffectResponse.class);

    assertEquals("contract_credited", response.getType());
  }

  @Test
  public void testContractDebited() throws IOException {
    String filePath = "src/test/resources/responses/effects/contract_debited.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    ContractDebitedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, ContractDebitedEffectResponse.class);

    assertEquals("contract_debited", response.getType());
  }

  @Test
  public void testDataCreated() throws IOException {
    String filePath = "src/test/resources/responses/effects/data_created.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    DataCreatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, DataCreatedEffectResponse.class);

    assertEquals("data_created", response.getType());
  }

  @Test
  public void testDataRemoved() throws IOException {
    String filePath = "src/test/resources/responses/effects/data_removed.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    DataRemovedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, DataRemovedEffectResponse.class);

    assertEquals("data_removed", response.getType());
  }

  @Test
  public void testDataSponsorshipCreated() throws IOException {
    String filePath = "src/test/resources/responses/effects/data_sponsorship_created.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    DataSponsorshipCreatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, DataSponsorshipCreatedEffectResponse.class);

    assertEquals("data_sponsorship_created", response.getType());
  }

  @Test
  public void testDataSponsorshipRemoved() throws IOException {
    String filePath = "src/test/resources/responses/effects/data_sponsorship_removed.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    DataSponsorshipRemovedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, DataSponsorshipRemovedEffectResponse.class);

    assertEquals("data_sponsorship_removed", response.getType());
  }

  @Test
  public void testDataSponsorshipUpdated() throws IOException {
    String filePath = "src/test/resources/responses/effects/data_sponsorship_updated.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    DataSponsorshipUpdatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, DataSponsorshipUpdatedEffectResponse.class);

    assertEquals("data_sponsorship_updated", response.getType());
  }

  @Test
  public void testDataUpdated() throws IOException {
    String filePath = "src/test/resources/responses/effects/data_updated.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    DataUpdatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, DataUpdatedEffectResponse.class);

    assertEquals("data_updated", response.getType());
  }

  @Test
  public void testLiquidityPoolCreated() throws IOException {
    String filePath = "src/test/resources/responses/effects/liquidity_pool_created.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    LiquidityPoolCreatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, LiquidityPoolCreatedEffectResponse.class);

    assertEquals("liquidity_pool_created", response.getType());
  }

  @Test
  public void testLiquidityPoolDeposited() throws IOException {
    String filePath = "src/test/resources/responses/effects/liquidity_pool_deposited.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    LiquidityPoolDepositedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, LiquidityPoolDepositedEffectResponse.class);

    assertEquals("liquidity_pool_deposited", response.getType());
  }

  @Test
  public void testLiquidityPoolRemoved() throws IOException {
    String filePath = "src/test/resources/responses/effects/liquidity_pool_removed.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    LiquidityPoolRemovedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, LiquidityPoolRemovedEffectResponse.class);

    assertEquals("liquidity_pool_removed", response.getType());
  }

  @Test
  public void testLiquidityPoolRevoked() throws IOException {
    String filePath = "src/test/resources/responses/effects/liquidity_pool_revoked.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    LiquidityPoolRevokedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, LiquidityPoolRevokedEffectResponse.class);

    assertEquals("liquidity_pool_revoked", response.getType());
  }

  @Test
  public void testLiquidityPoolTrade() throws IOException {
    String filePath = "src/test/resources/responses/effects/liquidity_pool_trade.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    LiquidityPoolTradeEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, LiquidityPoolTradeEffectResponse.class);

    assertEquals("liquidity_pool_trade", response.getType());
  }

  @Test
  public void testLiquidityPoolWithdrew() throws IOException {
    String filePath = "src/test/resources/responses/effects/liquidity_pool_withdrew.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    LiquidityPoolWithdrewEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, LiquidityPoolWithdrewEffectResponse.class);

    assertEquals("liquidity_pool_withdrew", response.getType());
  }

  @Test
  public void testOfferCreated() throws IOException {
    String filePath = "src/test/resources/responses/effects/offer_created.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    OfferCreatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, OfferCreatedEffectResponse.class);

    assertEquals("offer_created", response.getType());
  }

  @Test
  public void testOfferRemoved() throws IOException {
    String filePath = "src/test/resources/responses/effects/offer_removed.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    OfferRemovedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, OfferRemovedEffectResponse.class);

    assertEquals("offer_removed", response.getType());
  }

  @Test
  public void testOfferUpdated() throws IOException {
    String filePath = "src/test/resources/responses/effects/offer_updated.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    OfferUpdatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, OfferUpdatedEffectResponse.class);

    assertEquals("offer_updated", response.getType());
  }

  @Test
  public void testSequenceBumped() throws IOException {
    String filePath = "src/test/resources/responses/effects/sequence_bumped.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    SequenceBumpedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, SequenceBumpedEffectResponse.class);

    assertEquals("sequence_bumped", response.getType());
  }

  @Test
  public void testSignerCreated() throws IOException {
    String filePath = "src/test/resources/responses/effects/signer_created.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    SignerCreatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, SignerCreatedEffectResponse.class);

    assertEquals("signer_created", response.getType());
  }

  @Test
  public void testSignerRemoved() throws IOException {
    String filePath = "src/test/resources/responses/effects/signer_removed.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    SignerRemovedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, SignerRemovedEffectResponse.class);

    assertEquals("signer_removed", response.getType());
  }

  @Test
  public void testSignerSponsorshipCreated() throws IOException {
    String filePath = "src/test/resources/responses/effects/signer_sponsorship_created.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    SignerSponsorshipCreatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, SignerSponsorshipCreatedEffectResponse.class);

    assertEquals("signer_sponsorship_created", response.getType());
  }

  @Test
  public void testSignerSponsorshipRemoved() throws IOException {
    String filePath = "src/test/resources/responses/effects/signer_sponsorship_removed.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    SignerSponsorshipRemovedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, SignerSponsorshipRemovedEffectResponse.class);

    assertEquals("signer_sponsorship_removed", response.getType());
  }

  @Test
  public void testSignerSponsorshipUpdated() throws IOException {
    String filePath = "src/test/resources/responses/effects/signer_sponsorship_updated.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    SignerSponsorshipUpdatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, SignerSponsorshipUpdatedEffectResponse.class);

    assertEquals("signer_sponsorship_updated", response.getType());
  }

  @Test
  public void testSignerUpdated() throws IOException {
    String filePath = "src/test/resources/responses/effects/signer_updated.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    SignerUpdatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, SignerUpdatedEffectResponse.class);

    assertEquals("signer_updated", response.getType());
  }

  @Test
  public void testTrade() throws IOException {
    String filePath = "src/test/resources/responses/effects/trade.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TradeEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, TradeEffectResponse.class);

    assertEquals("trade", response.getType());
  }

  @Test
  public void testTrustlineAuthorized() throws IOException {
    String filePath = "src/test/resources/responses/effects/trustline_authorized.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TrustlineAuthorizedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, TrustlineAuthorizedEffectResponse.class);

    assertEquals("trustline_authorized", response.getType());
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
  }

  @Test
  public void testTrustlineCreated() throws IOException {
    String filePath = "src/test/resources/responses/effects/trustline_created.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TrustlineCreatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, TrustlineCreatedEffectResponse.class);

    assertEquals("trustline_created", response.getType());
  }

  @Test
  public void testTrustlineDeauthorized() throws IOException {
    String filePath = "src/test/resources/responses/effects/trustline_deauthorized.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TrustlineDeauthorizedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, TrustlineDeauthorizedEffectResponse.class);

    assertEquals("trustline_deauthorized", response.getType());
  }

  @Test
  public void testTrustlineFlagsUpdated() throws IOException {
    String filePath = "src/test/resources/responses/effects/trustline_flags_updated.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TrustlineFlagsUpdatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, TrustlineFlagsUpdatedEffectResponse.class);

    assertEquals("trustline_flags_updated", response.getType());
  }

  @Test
  public void testTrustlineRemoved() throws IOException {
    String filePath = "src/test/resources/responses/effects/trustline_removed.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TrustlineRemovedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, TrustlineRemovedEffectResponse.class);

    assertEquals("trustline_removed", response.getType());
  }

  @Test
  public void testTrustlineSponsorshipCreated() throws IOException {
    String filePath = "src/test/resources/responses/effects/trustline_sponsorship_created.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TrustlineSponsorshipCreatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, TrustlineSponsorshipCreatedEffectResponse.class);

    assertEquals("trustline_sponsorship_created", response.getType());
  }

  @Test
  public void testTrustlineSponsorshipRemoved() throws IOException {
    String filePath = "src/test/resources/responses/effects/trustline_sponsorship_removed.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TrustlineSponsorshipRemovedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, TrustlineSponsorshipRemovedEffectResponse.class);

    assertEquals("trustline_sponsorship_removed", response.getType());
  }

  @Test
  public void testTrustlineSponsorshipUpdated() throws IOException {
    String filePath = "src/test/resources/responses/effects/trustline_sponsorship_updated.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TrustlineSponsorshipUpdatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, TrustlineSponsorshipUpdatedEffectResponse.class);

    assertEquals("trustline_sponsorship_updated", response.getType());
  }

  @Test
  public void testTrustlineUpdated() throws IOException {
    String filePath = "src/test/resources/responses/effects/trustline_updated.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    TrustlineUpdatedEffectResponse response =
        GsonSingleton.getInstance().fromJson(json, TrustlineUpdatedEffectResponse.class);

    assertEquals("trustline_updated", response.getType());
  }
}
