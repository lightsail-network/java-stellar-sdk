package org.stellar.sdk.responses;

import com.google.common.base.Optional;
import com.google.gson.reflect.TypeToken;
import junit.framework.TestCase;
import org.junit.Test;
import org.stellar.sdk.LiquidityPoolID;

public class AccountsPageDeserializerTest extends TestCase {
  @Test
  public void testDeserialize() {
    Page<AccountResponse> accountsPage =
        GsonSingleton.getInstance()
            .fromJson(json, new TypeToken<Page<AccountResponse>>() {}.getType());

    assertEquals(
        accountsPage.getRecords().get(0).getAccountId(),
        "GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7");
    assertEquals(
        accountsPage.getRecords().get(9).getAccountId(),
        "GACFGMEV7A5H44O3K4EN6GRQ4SA543YJBZTKGNKPEMEQEAJFO4Q7ENG6");
    assertEquals(
        accountsPage.getLinks().getNext().getHref(),
        "/accounts?order=asc&limit=10&cursor=86861418598401");
    assertEquals(
        accountsPage.getLinks().getPrev().getHref(), "/accounts?order=desc&limit=10&cursor=1");
    assertEquals(
        accountsPage.getLinks().getSelf().getHref(), "/accounts?order=asc&limit=10&cursor=");
  }

  @Test
  public void testDeserializeWithLiquidityPoolBalance() {
    Page<AccountResponse> accountsPage =
        GsonSingleton.getInstance()
            .fromJson(jsonLiquidityPool, new TypeToken<Page<AccountResponse>>() {}.getType());

    assertEquals(
        accountsPage.getRecords().get(0).getAccountId(),
        "GDZZYLIIJ24HWAVWAQ2PJVNKHUJLJVVLFY2SSLYINBHDY5KZTLPTEST");
    assertEquals(
        accountsPage.getRecords().get(0).getBalances()[0].getLiquidityPoolID(),
        Optional.of(
            new LiquidityPoolID(
                "a468d41d8e9b8f3c7209651608b74b7db7ac9952dcae0cdf24871d1d9c7b0088")));
    assertEquals(
        accountsPage.getRecords().get(0).getBalances()[1].getLiquidityPoolID(), Optional.absent());
  }

  String json =
      "{\n"
          + "  \"_embedded\": {\n"
          + "    \"records\": [\n"
          + "      {\n"
          + "        \"id\": \"GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7\",\n"
          + "        \"paging_token\": \"1\",\n"
          + "        \"account_id\": \"GAAZI4TCR3TY5OJHCTJC2A4QSY6CJWJH5IAJTGKIN2ER7LBNVKOCCWN7\"\n"
          + "      },\n"
          + "      {\n"
          + "        \"id\": \"GALPCCZN4YXA3YMJHKL6CVIECKPLJJCTVMSNYWBTKJW4K5HQLYLDMZTB\",\n"
          + "        \"paging_token\": \"12884905985\",\n"
          + "        \"account_id\": \"GALPCCZN4YXA3YMJHKL6CVIECKPLJJCTVMSNYWBTKJW4K5HQLYLDMZTB\"\n"
          + "      },\n"
          + "      {\n"
          + "        \"id\": \"GAP2KHWUMOHY7IO37UJY7SEBIITJIDZS5DRIIQRPEUT4VUKHZQGIRWS4\",\n"
          + "        \"paging_token\": \"33676838572033\",\n"
          + "        \"account_id\": \"GAP2KHWUMOHY7IO37UJY7SEBIITJIDZS5DRIIQRPEUT4VUKHZQGIRWS4\"\n"
          + "      },\n"
          + "      {\n"
          + "        \"id\": \"GCZTBYH66ISTZKUPVJWTMHWBH4S4JIJ7WNLQJXCTQJKWY3FIT34BWZCJ\",\n"
          + "        \"paging_token\": \"33676838572034\",\n"
          + "        \"account_id\": \"GCZTBYH66ISTZKUPVJWTMHWBH4S4JIJ7WNLQJXCTQJKWY3FIT34BWZCJ\"\n"
          + "      },\n"
          + "      {\n"
          + "        \"id\": \"GBEZOC5U4TVH7ZY5N3FLYHTCZSI6VFGTULG7PBITLF5ZEBPJXFT46YZM\",\n"
          + "        \"paging_token\": \"33676838572035\",\n"
          + "        \"account_id\": \"GBEZOC5U4TVH7ZY5N3FLYHTCZSI6VFGTULG7PBITLF5ZEBPJXFT46YZM\"\n"
          + "      },\n"
          + "      {\n"
          + "        \"id\": \"GAENIE5LBJIXLMJIAJ7225IUPA6CX7EGHUXRX5FLCZFFAQSG2ZUYSWFK\",\n"
          + "        \"paging_token\": \"37288906067969\",\n"
          + "        \"account_id\": \"GAENIE5LBJIXLMJIAJ7225IUPA6CX7EGHUXRX5FLCZFFAQSG2ZUYSWFK\"\n"
          + "      },\n"
          + "      {\n"
          + "        \"id\": \"GBCXF42Q26WFS2KJ5XDM5KGOWR5M4GHR3DBTFBJVRYKRUYJK4DBIH3RX\",\n"
          + "        \"paging_token\": \"84340272795649\",\n"
          + "        \"account_id\": \"GBCXF42Q26WFS2KJ5XDM5KGOWR5M4GHR3DBTFBJVRYKRUYJK4DBIH3RX\"\n"
          + "      },\n"
          + "      {\n"
          + "        \"id\": \"GDVXG2FMFFSUMMMBIUEMWPZAIU2FNCH7QNGJMWRXRD6K5FZK5KJS4DDR\",\n"
          + "        \"paging_token\": \"85164906516481\",\n"
          + "        \"account_id\": \"GDVXG2FMFFSUMMMBIUEMWPZAIU2FNCH7QNGJMWRXRD6K5FZK5KJS4DDR\"\n"
          + "      },\n"
          + "      {\n"
          + "        \"id\": \"GAORN5O6AQUHW3F6ZVOTN67RAZSONRNKP7WOHZ4XBHDMRKKLBTFTSNC6\",\n"
          + "        \"paging_token\": \"85718957297665\",\n"
          + "        \"account_id\": \"GAORN5O6AQUHW3F6ZVOTN67RAZSONRNKP7WOHZ4XBHDMRKKLBTFTSNC6\"\n"
          + "      },\n"
          + "      {\n"
          + "        \"id\": \"GACFGMEV7A5H44O3K4EN6GRQ4SA543YJBZTKGNKPEMEQEAJFO4Q7ENG6\",\n"
          + "        \"paging_token\": \"86861418598401\",\n"
          + "        \"account_id\": \"GACFGMEV7A5H44O3K4EN6GRQ4SA543YJBZTKGNKPEMEQEAJFO4Q7ENG6\"\n"
          + "      }\n"
          + "    ]\n"
          + "  },\n"
          + "  \"_links\": {\n"
          + "    \"next\": {\n"
          + "      \"href\": \"/accounts?order=asc\\u0026limit=10\\u0026cursor=86861418598401\"\n"
          + "    },\n"
          + "    \"prev\": {\n"
          + "      \"href\": \"/accounts?order=desc\\u0026limit=10\\u0026cursor=1\"\n"
          + "    },\n"
          + "    \"self\": {\n"
          + "      \"href\": \"/accounts?order=asc\\u0026limit=10\\u0026cursor=\"\n"
          + "    }\n"
          + "  }\n"
          + "}";

  String jsonLiquidityPool =
      "{\n"
          + "  \"_links\": {\n"
          + "    \"self\": {\n"
          + "      \"href\": \"https://horizon.publicnode.org/accounts/?asset=USDC%3AGA5ZSEJYB37JRC5AVCIA5MOP4RHTM335X2KGX3IHOJAPP5RE34K4KZVN&cursor=&limit=1&order=desc\"\n"
          + "    },\n"
          + "    \"next\": {\n"
          + "      \"href\": \"https://horizon.publicnode.org/accounts/?asset=USDC%3AGA5ZSEJYB37JRC5AVCIA5MOP4RHTM335X2KGX3IHOJAPP5RE34K4KZVN&cursor=GDZZYLIIJ24HWAVWAQ2PJVNKHUJLJVVLFY2SSLYINBHDY5KZTLPTEST&limit=1&order=desc\"\n"
          + "    },\n"
          + "    \"prev\": {\n"
          + "      \"href\": \"https://horizon.publicnode.org/accounts/?asset=USDC%3AGA5ZSEJYB37JRC5AVCIA5MOP4RHTM335X2KGX3IHOJAPP5RE34K4KZVN&cursor=GDZZYLIIJ24HWAVWAQ2PJVNKHUJLJVVLFY2SSLYINBHDY5KZTLPTEST&limit=1&order=asc\"\n"
          + "    }\n"
          + "  },\n"
          + "  \"_embedded\": {\n"
          + "    \"records\": [\n"
          + "      {\n"
          + "        \"_links\": {\n"
          + "          \"self\": {\n"
          + "            \"href\": \"https://horizon.publicnode.org/accounts/GDZZYLIIJ24HWAVWAQ2PJVNKHUJLJVVLFY2SSLYINBHDY5KZTLPTEST\"\n"
          + "          },\n"
          + "          \"transactions\": {\n"
          + "            \"href\": \"https://horizon.publicnode.org/accounts/GDZZYLIIJ24HWAVWAQ2PJVNKHUJLJVVLFY2SSLYINBHDY5KZTLPTEST/transactions{?cursor,limit,order}\",\n"
          + "            \"templated\": true\n"
          + "          },\n"
          + "          \"operations\": {\n"
          + "            \"href\": \"https://horizon.publicnode.org/accounts/GDZZYLIIJ24HWAVWAQ2PJVNKHUJLJVVLFY2SSLYINBHDY5KZTLPTEST/operations{?cursor,limit,order}\",\n"
          + "            \"templated\": true\n"
          + "          },\n"
          + "          \"payments\": {\n"
          + "            \"href\": \"https://horizon.publicnode.org/accounts/GDZZYLIIJ24HWAVWAQ2PJVNKHUJLJVVLFY2SSLYINBHDY5KZTLPTEST/payments{?cursor,limit,order}\",\n"
          + "            \"templated\": true\n"
          + "          },\n"
          + "          \"effects\": {\n"
          + "            \"href\": \"https://horizon.publicnode.org/accounts/GDZZYLIIJ24HWAVWAQ2PJVNKHUJLJVVLFY2SSLYINBHDY5KZTLPTEST/effects{?cursor,limit,order}\",\n"
          + "            \"templated\": true\n"
          + "          },\n"
          + "          \"offers\": {\n"
          + "            \"href\": \"https://horizon.publicnode.org/accounts/GDZZYLIIJ24HWAVWAQ2PJVNKHUJLJVVLFY2SSLYINBHDY5KZTLPTEST/offers{?cursor,limit,order}\",\n"
          + "            \"templated\": true\n"
          + "          },\n"
          + "          \"trades\": {\n"
          + "            \"href\": \"https://horizon.publicnode.org/accounts/GDZZYLIIJ24HWAVWAQ2PJVNKHUJLJVVLFY2SSLYINBHDY5KZTLPTEST/trades{?cursor,limit,order}\",\n"
          + "            \"templated\": true\n"
          + "          },\n"
          + "          \"data\": {\n"
          + "            \"href\": \"https://horizon.publicnode.org/accounts/GDZZYLIIJ24HWAVWAQ2PJVNKHUJLJVVLFY2SSLYINBHDY5KZTLPTEST/data/{key}\",\n"
          + "            \"templated\": true\n"
          + "          }\n"
          + "        },\n"
          + "        \"id\": \"GDZZYLIIJ24HWAVWAQ2PJVNKHUJLJVVLFY2SSLYINBHDY5KZTLPTEST\",\n"
          + "        \"account_id\": \"GDZZYLIIJ24HWAVWAQ2PJVNKHUJLJVVLFY2SSLYINBHDY5KZTLPTEST\",\n"
          + "        \"sequence\": \"166506652181200896\",\n"
          + "        \"subentry_count\": 5,\n"
          + "        \"last_modified_ledger\": 38767851,\n"
          + "        \"last_modified_time\": \"2021-12-17T20:22:00Z\",\n"
          + "        \"thresholds\": {\n"
          + "          \"low_threshold\": 20,\n"
          + "          \"med_threshold\": 20,\n"
          + "          \"high_threshold\": 20\n"
          + "        },\n"
          + "        \"flags\": {\n"
          + "          \"auth_required\": false,\n"
          + "          \"auth_revocable\": false,\n"
          + "          \"auth_immutable\": false,\n"
          + "          \"auth_clawback_enabled\": false\n"
          + "        },\n"
          + "        \"balances\": [\n"
          + "        {\n"
          + "            \"balance\": \"974462.6017340\",\n"
          + "            \"liquidity_pool_id\": \"a468d41d8e9b8f3c7209651608b74b7db7ac9952dcae0cdf24871d1d9c7b0088\",\n"
          + "            \"limit\": \"922337203685.4775807\",\n"
          + "            \"last_modified_ledger\": 38809512,\n"
          + "            \"is_authorized\": false,\n"
          + "            \"is_authorized_to_maintain_liabilities\": false,\n"
          + "            \"asset_type\": \"liquidity_pool_shares\"\n"
          + "            },\n"
          + "          {\n"
          + "            \"balance\": \"0.0000000\",\n"
          + "            \"limit\": \"922337203685.4775807\",\n"
          + "            \"buying_liabilities\": \"0.0000000\",\n"
          + "            \"selling_liabilities\": \"0.0000000\",\n"
          + "            \"sponsor\": \"GCZGSFPITKVJPJERJIVLCQK5YIHYTDXCY45ZHU3IRPBP53SXSCALTEST\",\n"
          + "            \"last_modified_ledger\": 38767851,\n"
          + "            \"is_authorized\": true,\n"
          + "            \"is_authorized_to_maintain_liabilities\": true,\n"
          + "            \"asset_type\": \"credit_alphanum4\",\n"
          + "            \"asset_code\": \"USDC\",\n"
          + "            \"asset_issuer\": \"GA5ZSEJYB37JRC5AVCIA5MOP4RHTM335X2KGX3IHOJAPP5RE34K4KZVN\"\n"
          + "          },\n"
          + "          {\n"
          + "            \"balance\": \"0.0000000\",\n"
          + "            \"buying_liabilities\": \"0.0000000\",\n"
          + "            \"selling_liabilities\": \"0.0000000\",\n"
          + "            \"asset_type\": \"native\"\n"
          + "          }\n"
          + "        ],\n"
          + "        \"signers\": [\n"
          + "          {\n"
          + "            \"weight\": 10,\n"
          + "            \"key\": \"GDZZYLIIJ24HWAVWAQ2PJVNKHUJLJVVLFY2SSLYINBHDY5KZTLPTEST\",\n"
          + "            \"type\": \"ed25519_public_key\"\n"
          + "          }\n"
          + "        ],\n"
          + "        \"data\": {},\n"
          + "        \"num_sponsoring\": 0,\n"
          + "        \"num_sponsored\": 7,\n"
          + "        \"sponsor\": \"GCZGSFPITKVJPJERJIVLCQK5YIHYTDXCY45ZHU3IRPBP53SXSCALTEST\",\n"
          + "        \"paging_token\": \"GDZZYLIIJ24HWAVWAQ2PJVNKHUJLJVVLFY2SSLYINBHDY5KZTLPTEST\"\n"
          + "      }\n"
          + "    ]\n"
          + "  }\n"
          + "}";
}
