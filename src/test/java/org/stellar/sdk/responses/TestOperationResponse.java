package org.stellar.sdk.responses;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import org.stellar.sdk.AssetTypeCreditAlphaNum4;
import org.stellar.sdk.LiquidityPoolID;
import org.stellar.sdk.TrustLineAsset;
import org.stellar.sdk.responses.operations.AccountMergeOperationResponse;
import org.stellar.sdk.responses.operations.AllowTrustOperationResponse;
import org.stellar.sdk.responses.operations.BeginSponsoringFutureReservesOperationResponse;
import org.stellar.sdk.responses.operations.BumpSequenceOperationResponse;
import org.stellar.sdk.responses.operations.ChangeTrustOperationResponse;
import org.stellar.sdk.responses.operations.ClaimClaimableBalanceOperationResponse;
import org.stellar.sdk.responses.operations.ClawbackClaimableBalanceOperationResponse;

public class TestOperationResponse {
  @Test
  public void testBaseOperation() throws IOException {
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
        GsonSingleton.getInstance().fromJson(json, AccountMergeOperationResponse.class);

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
        GsonSingleton.getInstance().fromJson(json, AllowTrustOperationResponse.class);

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
        GsonSingleton.getInstance()
            .fromJson(json, BeginSponsoringFutureReservesOperationResponse.class);

    assertEquals("begin_sponsoring_future_reserves", response.getType());
    assertEquals(
        "GDS6ULV46WVSO2USGIVTIUDYBL3ROBPQEEANE3AY6XZION25DCNFIE2R", response.getSponsoredId());
  }

  @Test
  public void testBumpSequenceOperation() throws IOException {
    String filePath = "src/test/resources/responses/operations/bump_sequence.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    BumpSequenceOperationResponse response =
        GsonSingleton.getInstance().fromJson(json, BumpSequenceOperationResponse.class);

    assertEquals("bump_sequence", response.getType());
    assertEquals(136025045943191412L, response.getBumpTo().longValue());
  }

  @Test
  public void testChangeTrustOperationWithAsset() throws IOException {
    String filePath = "src/test/resources/responses/operations/change_trust_asset.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    ChangeTrustOperationResponse response =
        GsonSingleton.getInstance().fromJson(json, ChangeTrustOperationResponse.class);

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
        response.getAsset());
    assertEquals("922337203685.4775807", response.getLimit());
  }

  @Test
  public void testChangeTrustOperationWithLiquidityPoolId() throws IOException {
    String filePath = "src/test/resources/responses/operations/change_trust_liquidity_pool_id.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    ChangeTrustOperationResponse response =
        GsonSingleton.getInstance().fromJson(json, ChangeTrustOperationResponse.class);

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
        response.getAsset());
    assertEquals("922337203685.4775807", response.getLimit());
  }

  @Test
  public void testClaimClaimableBalanceOperation() throws IOException {
    String filePath = "src/test/resources/responses/operations/claim_claimable_balance.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    ClaimClaimableBalanceOperationResponse response =
        GsonSingleton.getInstance().fromJson(json, ClaimClaimableBalanceOperationResponse.class);

    assertEquals("claim_claimable_balance", response.getType());
    assertEquals(
        "00000000a5c8c85c12a32ec1b30fc1792a542ca38702afd78eb4fe524d028887cf6b6952",
        response.getBalanceId());
    assertEquals(
        "GAEY7JFLBBDD6PAUPVRVKMBNSL5W6GYMUOGJKNGHGFSFGJU6CT2IUARS", response.getClaimant());
  }

  @Test
  public void testClawbackClaimableBalanceOperation() throws IOException {
    String filePath = "src/test/resources/responses/operations/clawback_claimable_balance.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    ClawbackClaimableBalanceOperationResponse response =
        GsonSingleton.getInstance().fromJson(json, ClawbackClaimableBalanceOperationResponse.class);

    assertEquals("clawback_claimable_balance", response.getType());
    assertEquals(
        "000000001fe36f3ce6ab6a6423b18b5947ce8890157ae77bb17faeb765814ad040b74ce1",
        response.getBalanceId());
  }
}
