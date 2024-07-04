package org.stellar.sdk.responses;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;

// {
//        "_links": {
//          "toml": {
//            "href": "https://centre.io/.well-known/stellar.toml"
//          }
//        },
//        "asset_type": "credit_alphanum4",
//        "asset_code": "USDC",
//        "asset_issuer": "GA5ZSEJYB37JRC5AVCIA5MOP4RHTM335X2KGX3IHOJAPP5RE34K4KZVN",
//        "paging_token":
// "USDC_GA5ZSEJYB37JRC5AVCIA5MOP4RHTM335X2KGX3IHOJAPP5RE34K4KZVN_credit_alphanum4",
//        "contract_id": "CCW67TSZV3SSS2HXMBQ5JFGCKJNXKZM7UQUWUZPUTHXSTZLEO7SJMI75",
//        "num_accounts": 918786,
//        "num_claimable_balances": 415,
//        "num_liquidity_pools": 384,
//        "num_contracts": 24,
//        "num_archived_contracts": 0,
//        "amount": "196141192.6091517",
//        "accounts": {
//          "authorized": 918786,
//          "authorized_to_maintain_liabilities": 0,
//          "unauthorized": 0
//        },
//        "claimable_balances_amount": "8073.0306754",
//        "liquidity_pools_amount": "3001425.9918924",
//        "contracts_amount": "453241.3236804",
//        "archived_contracts_amount": "0.0000000",
//        "balances": {
//          "authorized": "196141192.6091517",
//          "authorized_to_maintain_liabilities": "0.0000000",
//          "unauthorized": "0.0000000"
//        },
//        "flags": {
//          "auth_required": false,
//          "auth_revocable": true,
//          "auth_immutable": false,
//          "auth_clawback_enabled": false
//        }
//      }
public class AssetResponseTest {
  @Test
  public void testDeserialize() throws IOException {
    String filePath = "src/test/resources/responses/asset_response.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    AssetResponse assetResponse = GsonSingleton.getInstance().fromJson(json, AssetResponse.class);
    assertEquals(assetResponse.getAssetType(), "credit_alphanum4");
    assertEquals(assetResponse.getAssetCode(), "USDC");
    assertEquals(
        assetResponse.getAssetIssuer(), "GA5ZSEJYB37JRC5AVCIA5MOP4RHTM335X2KGX3IHOJAPP5RE34K4KZVN");
    assertEquals(
        assetResponse.getPagingToken(),
        "USDC_GA5ZSEJYB37JRC5AVCIA5MOP4RHTM335X2KGX3IHOJAPP5RE34K4KZVN_credit_alphanum4");
    assertEquals(
        assetResponse.getContractID(), "CCW67TSZV3SSS2HXMBQ5JFGCKJNXKZM7UQUWUZPUTHXSTZLEO7SJMI75");
    assertEquals(assetResponse.getNumAccounts().intValue(), 918786);
    assertEquals(assetResponse.getNumClaimableBalances().intValue(), 415);
    assertEquals(assetResponse.getNumLiquidityPools().intValue(), 384);
    assertEquals(assetResponse.getNumContracts().intValue(), 24);
    assertEquals(assetResponse.getNumArchivedContracts().intValue(), 1);
    assertEquals(assetResponse.getAmount(), "196141192.6091517");
    assertEquals(assetResponse.getAccounts().getAuthorized().intValue(), 918786);
    assertEquals(assetResponse.getAccounts().getAuthorizedToMaintainLiabilities().intValue(), 0);
    assertEquals(assetResponse.getAccounts().getUnauthorized().intValue(), 0);
    assertEquals(assetResponse.getClaimableBalancesAmount(), "8073.0306754");
    assertEquals(assetResponse.getLiquidityPoolsAmount(), "3001425.9918924");
    assertEquals(assetResponse.getContractsAmount(), "453241.3236804");
    assertEquals(assetResponse.getArchivedContractsAmount(), "1.0000000");
    assertEquals(assetResponse.getBalances().getAuthorized(), "196141192.6091517");
    assertEquals(assetResponse.getBalances().getAuthorizedToMaintainLiabilities(), "0.0000000");
    assertEquals(assetResponse.getBalances().getUnauthorized(), "0.0000000");
    assertEquals(assetResponse.getFlags().getAuthRequired(), false);
    assertEquals(assetResponse.getFlags().getAuthRevocable(), true);
    assertEquals(assetResponse.getFlags().getAuthImmutable(), false);
    assertEquals(assetResponse.getFlags().getAuthClawbackEnabled(), false);
  }
}
