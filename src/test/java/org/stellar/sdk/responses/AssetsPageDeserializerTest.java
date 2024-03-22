package org.stellar.sdk.responses;

import com.google.gson.reflect.TypeToken;
import junit.framework.TestCase;
import org.junit.Test;

public class AssetsPageDeserializerTest extends TestCase {
  @Test
  public void testDeserialize() {
    Page<AssetResponse> page =
        GsonSingleton.getInstance()
            .fromJson(json, new TypeToken<Page<AssetResponse>>() {}.getType());

    assertEquals(
        page.getLinks().getSelf().getHref(),
        "https://horizon-futurenet.stellar.org/assets?cursor=FRS1_GC6HLY2JXKXYXUU3XYC63O2RJNH4E3GEW26ABTHDF6AF6MY32B5QRISO_credit_alphanum4&limit=5&order=asc");
    assertEquals(
        page.getLinks().getNext().getHref(),
        "https://horizon-futurenet.stellar.org/assets?cursor=Fsdk_GBP5VJVRLEZWRMOARGWIPZKV26PBR7SQPHC43ZNJNTATFDOPTOQB4P7I_credit_alphanum4&limit=5&order=asc");

    assertEquals(page.getRecords().get(0).getAssetType(), "credit_alphanum4");
    assertEquals(page.getRecords().get(0).getAssetCode(), "Fsdk");
    assertEquals(
        page.getRecords().get(0).getAssetIssuer(),
        "GAO5OFEWI5ABK66MSPQ6HBXSXNCQLDTF6J444ICGYIQYAPFIWZ7V4MED");
    assertEquals(
        page.getRecords().get(0).getPagingToken(),
        "Fsdk_GAO5OFEWI5ABK66MSPQ6HBXSXNCQLDTF6J444ICGYIQYAPFIWZ7V4MED_credit_alphanum4");
    assertEquals(page.getRecords().get(0).getAccounts().authorized(), 1);
    assertEquals(page.getRecords().get(0).getAccounts().authorizedToMaintainLiabilities(), 0);
    assertEquals(page.getRecords().get(0).getAccounts().unauthorized(), 0);
    assertEquals(page.getRecords().get(0).getBalances().authorized(), "200.0000000");
    assertEquals(
        page.getRecords().get(0).getBalances().authorizedToMaintainLiabilities(), "0.0000000");
    assertEquals(page.getRecords().get(0).getBalances().unauthorized(), "0.0000000");
    assertEquals(page.getRecords().get(0).getNumClaimableBalances(), 0);
    assertEquals(page.getRecords().get(0).getClaimableBalancesAmount(), "0.0000000");
    assertEquals(page.getRecords().get(0).getAmount(), "200.0000000");
    assertEquals(page.getRecords().get(0).getNumAccounts(), 1);
    assertEquals(page.getRecords().get(0).getNumArchivedContracts(), 3);
    assertEquals(page.getRecords().get(0).getArchivedContractsAmount(), "1.3330000");
    assertEquals(
        page.getRecords().get(0).getLinks().getToml().getHref(),
        "https://www.stellar.org/.well-known/stellar.toml");
    assertTrue(page.getRecords().get(0).getFlags().isAuthRequired());
    assertFalse(page.getRecords().get(0).getFlags().isAuthRevocable());
    assertFalse(page.getRecords().get(0).getFlags().isAuthImmutable());
    assertFalse(page.getRecords().get(0).getFlags().isAuthClawbackEnabled());
    assertEquals(
        page.getRecords().get(0).getContractID(),
        "CAKBXQ7XXRMY5F6YFAY4D45HY25VHNKOGWHS3KZX5ULP6E5TRVITMJS2");
    assertEquals(page.getRecords().get(0).getNumContracts(), 2);
    assertEquals(page.getRecords().get(0).getContractsAmount(), "25.1230000");
  }

  String json =
      "{\n"
          + "  \"_links\": {\n"
          + "    \"self\": {\n"
          + "      \"href\": \"https://horizon-futurenet.stellar.org/assets?cursor=FRS1_GC6HLY2JXKXYXUU3XYC63O2RJNH4E3GEW26ABTHDF6AF6MY32B5QRISO_credit_alphanum4&limit=5&order=asc\"\n"
          + "    },\n"
          + "    \"next\": {\n"
          + "      \"href\": \"https://horizon-futurenet.stellar.org/assets?cursor=Fsdk_GBP5VJVRLEZWRMOARGWIPZKV26PBR7SQPHC43ZNJNTATFDOPTOQB4P7I_credit_alphanum4&limit=5&order=asc\"\n"
          + "    },\n"
          + "    \"prev\": {\n"
          + "      \"href\": \"https://horizon-futurenet.stellar.org/assets?cursor=Fsdk_GAO5OFEWI5ABK66MSPQ6HBXSXNCQLDTF6J444ICGYIQYAPFIWZ7V4MED_credit_alphanum4&limit=5&order=desc\"\n"
          + "    }\n"
          + "  },\n"
          + "  \"_embedded\": {\n"
          + "    \"records\": [\n"
          + "      {\n"
          + "        \"_links\": {\n"
          + "          \"toml\": {\n"
          + "            \"href\": \"https://www.stellar.org/.well-known/stellar.toml\"\n"
          + "          }\n"
          + "        },\n"
          + "        \"asset_type\": \"credit_alphanum4\",\n"
          + "        \"asset_code\": \"Fsdk\",\n"
          + "        \"asset_issuer\": \"GAO5OFEWI5ABK66MSPQ6HBXSXNCQLDTF6J444ICGYIQYAPFIWZ7V4MED\",\n"
          + "        \"paging_token\": \"Fsdk_GAO5OFEWI5ABK66MSPQ6HBXSXNCQLDTF6J444ICGYIQYAPFIWZ7V4MED_credit_alphanum4\",\n"
          + "        \"contract_id\": \"CAKBXQ7XXRMY5F6YFAY4D45HY25VHNKOGWHS3KZX5ULP6E5TRVITMJS2\",\n"
          + "        \"num_accounts\": 1,\n"
          + "        \"num_claimable_balances\": 0,\n"
          + "        \"num_liquidity_pools\": 0,\n"
          + "        \"num_contracts\": 2,\n"
          + "        \"num_archived_contracts\": 3,\n"
          + "        \"amount\": \"200.0000000\",\n"
          + "        \"accounts\": {\n"
          + "          \"authorized\": 1,\n"
          + "          \"authorized_to_maintain_liabilities\": 0,\n"
          + "          \"unauthorized\": 0\n"
          + "        },\n"
          + "        \"claimable_balances_amount\": \"0.0000000\",\n"
          + "        \"liquidity_pools_amount\": \"0.0000000\",\n"
          + "        \"contracts_amount\": \"25.1230000\",\n"
          + "        \"archived_contracts_amount\": \"1.3330000\",\n"
          + "        \"balances\": {\n"
          + "          \"authorized\": \"200.0000000\",\n"
          + "          \"authorized_to_maintain_liabilities\": \"0.0000000\",\n"
          + "          \"unauthorized\": \"0.0000000\"\n"
          + "        },\n"
          + "        \"flags\": {\n"
          + "          \"auth_required\": true,\n"
          + "          \"auth_revocable\": false,\n"
          + "          \"auth_immutable\": false,\n"
          + "          \"auth_clawback_enabled\": false\n"
          + "        }\n"
          + "      },\n"
          + "      {\n"
          + "        \"_links\": {\n"
          + "          \"toml\": {\n"
          + "            \"href\": \"\"\n"
          + "          }\n"
          + "        },\n"
          + "        \"asset_type\": \"credit_alphanum4\",\n"
          + "        \"asset_code\": \"Fsdk\",\n"
          + "        \"asset_issuer\": \"GAPMSH3ZYNL4JB4OBKESIHWK3WZ4IFV4TDNHJRUNI62RT56I7RVUSBWI\",\n"
          + "        \"paging_token\": \"Fsdk_GAPMSH3ZYNL4JB4OBKESIHWK3WZ4IFV4TDNHJRUNI62RT56I7RVUSBWI_credit_alphanum4\",\n"
          + "        \"num_accounts\": 1,\n"
          + "        \"num_claimable_balances\": 0,\n"
          + "        \"num_liquidity_pools\": 0,\n"
          + "        \"num_contracts\": 0,\n"
          + "        \"num_archived_contracts\": 0,\n"
          + "        \"amount\": \"200.0000000\",\n"
          + "        \"accounts\": {\n"
          + "          \"authorized\": 1,\n"
          + "          \"authorized_to_maintain_liabilities\": 0,\n"
          + "          \"unauthorized\": 0\n"
          + "        },\n"
          + "        \"claimable_balances_amount\": \"0.0000000\",\n"
          + "        \"liquidity_pools_amount\": \"0.0000000\",\n"
          + "        \"contracts_amount\": \"0.0000000\",\n"
          + "        \"archived_contracts_amount\": \"0.0000000\",\n"
          + "        \"balances\": {\n"
          + "          \"authorized\": \"200.0000000\",\n"
          + "          \"authorized_to_maintain_liabilities\": \"0.0000000\",\n"
          + "          \"unauthorized\": \"0.0000000\"\n"
          + "        },\n"
          + "        \"flags\": {\n"
          + "          \"auth_required\": true,\n"
          + "          \"auth_revocable\": false,\n"
          + "          \"auth_immutable\": false,\n"
          + "          \"auth_clawback_enabled\": false\n"
          + "        }\n"
          + "      },\n"
          + "      {\n"
          + "        \"_links\": {\n"
          + "          \"toml\": {\n"
          + "            \"href\": \"\"\n"
          + "          }\n"
          + "        },\n"
          + "        \"asset_type\": \"credit_alphanum4\",\n"
          + "        \"asset_code\": \"Fsdk\",\n"
          + "        \"asset_issuer\": \"GAZGEVSF73ZUCD7FIZFZK7NRZHYB3TGKLVZIWBD7XPKFF6HKUHKXZ4R4\",\n"
          + "        \"paging_token\": \"Fsdk_GAZGEVSF73ZUCD7FIZFZK7NRZHYB3TGKLVZIWBD7XPKFF6HKUHKXZ4R4_credit_alphanum4\",\n"
          + "        \"contract_id\": \"CCENU3B5K7TXRXRGSF3R32R4FTANRK3KRPLPMN6PDNCKA6MW7B5JOXBJ\",\n"
          + "        \"num_accounts\": 1,\n"
          + "        \"num_claimable_balances\": 0,\n"
          + "        \"num_liquidity_pools\": 0,\n"
          + "        \"num_contracts\": 0,\n"
          + "        \"num_archived_contracts\": 0,\n"
          + "        \"amount\": \"200.0000000\",\n"
          + "        \"accounts\": {\n"
          + "          \"authorized\": 1,\n"
          + "          \"authorized_to_maintain_liabilities\": 0,\n"
          + "          \"unauthorized\": 0\n"
          + "        },\n"
          + "        \"claimable_balances_amount\": \"0.0000000\",\n"
          + "        \"liquidity_pools_amount\": \"0.0000000\",\n"
          + "        \"contracts_amount\": \"0.0000000\",\n"
          + "        \"archived_contracts_amount\": \"0.0000000\",\n"
          + "        \"balances\": {\n"
          + "          \"authorized\": \"200.0000000\",\n"
          + "          \"authorized_to_maintain_liabilities\": \"0.0000000\",\n"
          + "          \"unauthorized\": \"0.0000000\"\n"
          + "        },\n"
          + "        \"flags\": {\n"
          + "          \"auth_required\": false,\n"
          + "          \"auth_revocable\": false,\n"
          + "          \"auth_immutable\": false,\n"
          + "          \"auth_clawback_enabled\": false\n"
          + "        }\n"
          + "      },\n"
          + "      {\n"
          + "        \"_links\": {\n"
          + "          \"toml\": {\n"
          + "            \"href\": \"\"\n"
          + "          }\n"
          + "        },\n"
          + "        \"asset_type\": \"credit_alphanum4\",\n"
          + "        \"asset_code\": \"Fsdk\",\n"
          + "        \"asset_issuer\": \"GBENWRA65WGOL3OK7DMQXQJWXSO34WQRP5QKFW4LYLCPG5BLFVEMYPF5\",\n"
          + "        \"paging_token\": \"Fsdk_GBENWRA65WGOL3OK7DMQXQJWXSO34WQRP5QKFW4LYLCPG5BLFVEMYPF5_credit_alphanum4\",\n"
          + "        \"contract_id\": \"CC7GMZZM4YJK32V6ZZOWELQMBH7BTKVF52PYWFUR7GGBFAVEAZPQTUJA\",\n"
          + "        \"num_accounts\": 1,\n"
          + "        \"num_claimable_balances\": 0,\n"
          + "        \"num_liquidity_pools\": 0,\n"
          + "        \"num_contracts\": 0,\n"
          + "        \"num_archived_contracts\": 0,\n"
          + "        \"amount\": \"200.0000000\",\n"
          + "        \"accounts\": {\n"
          + "          \"authorized\": 1,\n"
          + "          \"authorized_to_maintain_liabilities\": 0,\n"
          + "          \"unauthorized\": 0\n"
          + "        },\n"
          + "        \"claimable_balances_amount\": \"0.0000000\",\n"
          + "        \"liquidity_pools_amount\": \"0.0000000\",\n"
          + "        \"contracts_amount\": \"0.0000000\",\n"
          + "        \"archived_contracts_amount\": \"0.0000000\",\n"
          + "        \"balances\": {\n"
          + "          \"authorized\": \"200.0000000\",\n"
          + "          \"authorized_to_maintain_liabilities\": \"0.0000000\",\n"
          + "          \"unauthorized\": \"0.0000000\"\n"
          + "        },\n"
          + "        \"flags\": {\n"
          + "          \"auth_required\": false,\n"
          + "          \"auth_revocable\": false,\n"
          + "          \"auth_immutable\": false,\n"
          + "          \"auth_clawback_enabled\": false\n"
          + "        }\n"
          + "      },\n"
          + "      {\n"
          + "        \"_links\": {\n"
          + "          \"toml\": {\n"
          + "            \"href\": \"\"\n"
          + "          }\n"
          + "        },\n"
          + "        \"asset_type\": \"credit_alphanum4\",\n"
          + "        \"asset_code\": \"Fsdk\",\n"
          + "        \"asset_issuer\": \"GBP5VJVRLEZWRMOARGWIPZKV26PBR7SQPHC43ZNJNTATFDOPTOQB4P7I\",\n"
          + "        \"paging_token\": \"Fsdk_GBP5VJVRLEZWRMOARGWIPZKV26PBR7SQPHC43ZNJNTATFDOPTOQB4P7I_credit_alphanum4\",\n"
          + "        \"contract_id\": \"CC52PADO3R5DBLXXEBVTKWOKZ7TRBLTPSJV6WQT62JL3MRKDWOJDTT6J\",\n"
          + "        \"num_accounts\": 1,\n"
          + "        \"num_claimable_balances\": 0,\n"
          + "        \"num_liquidity_pools\": 0,\n"
          + "        \"num_contracts\": 0,\n"
          + "        \"num_archived_contracts\": 0,\n"
          + "        \"amount\": \"200.0000000\",\n"
          + "        \"accounts\": {\n"
          + "          \"authorized\": 1,\n"
          + "          \"authorized_to_maintain_liabilities\": 0,\n"
          + "          \"unauthorized\": 0\n"
          + "        },\n"
          + "        \"claimable_balances_amount\": \"0.0000000\",\n"
          + "        \"liquidity_pools_amount\": \"0.0000000\",\n"
          + "        \"contracts_amount\": \"0.0000000\",\n"
          + "        \"archived_contracts_amount\": \"0.0000000\",\n"
          + "        \"balances\": {\n"
          + "          \"authorized\": \"200.0000000\",\n"
          + "          \"authorized_to_maintain_liabilities\": \"0.0000000\",\n"
          + "          \"unauthorized\": \"0.0000000\"\n"
          + "        },\n"
          + "        \"flags\": {\n"
          + "          \"auth_required\": false,\n"
          + "          \"auth_revocable\": false,\n"
          + "          \"auth_immutable\": false,\n"
          + "          \"auth_clawback_enabled\": false\n"
          + "        }\n"
          + "      }\n"
          + "    ]\n"
          + "  }\n"
          + "}\n";
}
