package org.stellar.sdk.responses;

import com.google.gson.reflect.TypeToken;
import junit.framework.TestCase;
import org.junit.Test;

public class AssetsPageDeserializerTest extends TestCase {
    @Test
    public void testDeserialize() {
        Page<AssetResponse> page = GsonSingleton.getInstance().fromJson(json, new TypeToken<Page<AssetResponse>>() {}.getType());

        assertEquals(page.getLinks().getSelf().getHref(), "https://horizon.stellar.org/assets?order=asc&limit=10&cursor=");
        assertEquals(page.getLinks().getNext().getHref(), "https://horizon.stellar.org/assets?order=asc&limit=10&cursor=AsrtoDollar_GDJWXY5XUASXNL4ABCONR6T5MOXJ2S4HD6WDNAJDSDKQ4VS3TVUQJEDJ_credit_alphanum12");

        assertEquals(page.getRecords().get(0).getAssetType(), "credit_alphanum12");
        assertEquals(page.getRecords().get(0).getAssetCode(), "6497847");
        assertEquals(page.getRecords().get(0).getAssetIssuer(), "GCGNWKCJ3KHRLPM3TM6N7D3W5YKDJFL6A2YCXFXNMRTZ4Q66MEMZ6FI2");
        assertEquals(page.getRecords().get(0).getPagingToken(), "6497847_GCGNWKCJ3KHRLPM3TM6N7D3W5YKDJFL6A2YCXFXNMRTZ4Q66MEMZ6FI2_credit_alphanum12");
        assertEquals(page.getRecords().get(0).getAmount(), "0.0000000");
        assertEquals(page.getRecords().get(0).getNumAccounts(), 1);
        assertEquals(page.getRecords().get(0).getLinks().getToml().getHref(), "https://www.stellar.org/.well-known/stellar.toml");
        assertEquals(page.getRecords().get(0).getFlags().isAuthRequired(), true);
        assertEquals(page.getRecords().get(0).getFlags().isAuthRevocable(), false);
    }

    String json = "{\n" +
            "  \"_links\": {\n" +
            "    \"self\": {\n" +
            "      \"href\": \"https://horizon.stellar.org/assets?order=asc\\u0026limit=10\\u0026cursor=\"\n" +
            "    },\n" +
            "    \"next\": {\n" +
            "      \"href\": \"https://horizon.stellar.org/assets?order=asc\\u0026limit=10\\u0026cursor=AsrtoDollar_GDJWXY5XUASXNL4ABCONR6T5MOXJ2S4HD6WDNAJDSDKQ4VS3TVUQJEDJ_credit_alphanum12\"\n" +
            "    },\n" +
            "    \"prev\": {\n" +
            "      \"href\": \"https://horizon.stellar.org/assets?order=desc\\u0026limit=10\\u0026cursor=6497847_GCGNWKCJ3KHRLPM3TM6N7D3W5YKDJFL6A2YCXFXNMRTZ4Q66MEMZ6FI2_credit_alphanum12\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"_embedded\": {\n" +
            "    \"records\": [\n" +
            "      {\n" +
            "        \"_links\": {\n" +
            "          \"toml\": {\n" +
            "            \"href\": \"https://www.stellar.org/.well-known/stellar.toml\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"asset_type\": \"credit_alphanum12\",\n" +
            "        \"asset_code\": \"6497847\",\n" +
            "        \"asset_issuer\": \"GCGNWKCJ3KHRLPM3TM6N7D3W5YKDJFL6A2YCXFXNMRTZ4Q66MEMZ6FI2\",\n" +
            "        \"paging_token\": \"6497847_GCGNWKCJ3KHRLPM3TM6N7D3W5YKDJFL6A2YCXFXNMRTZ4Q66MEMZ6FI2_credit_alphanum12\",\n" +
            "        \"amount\": \"0.0000000\",\n" +
            "        \"num_accounts\": 1,\n" +
            "        \"flags\": {\n" +
            "          \"auth_required\": true,\n" +
            "          \"auth_revocable\": false\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"_links\": {\n" +
            "          \"toml\": {\n" +
            "            \"href\": \"\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"asset_type\": \"credit_alphanum12\",\n" +
            "        \"asset_code\": \"9HORIZONS\",\n" +
            "        \"asset_issuer\": \"GB2HXY7UEDCSHOWZ4553QFGFILNU73OFS2P4HU5IB3UUU66TWPBPVTGW\",\n" +
            "        \"paging_token\": \"9HORIZONS_GB2HXY7UEDCSHOWZ4553QFGFILNU73OFS2P4HU5IB3UUU66TWPBPVTGW_credit_alphanum12\",\n" +
            "        \"amount\": \"1000000.0000000\",\n" +
            "        \"num_accounts\": 3,\n" +
            "        \"flags\": {\n" +
            "          \"auth_required\": false,\n" +
            "          \"auth_revocable\": false\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"_links\": {\n" +
            "          \"toml\": {\n" +
            "            \"href\": \"\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"asset_type\": \"credit_alphanum4\",\n" +
            "        \"asset_code\": \"AIR\",\n" +
            "        \"asset_issuer\": \"GB2SQ74JCS6F4MVDU4BF4L4S4Z5Z36ABOTP6DF5JJOFGFE3ETZAUVUQK\",\n" +
            "        \"paging_token\": \"AIR_GB2SQ74JCS6F4MVDU4BF4L4S4Z5Z36ABOTP6DF5JJOFGFE3ETZAUVUQK_credit_alphanum4\",\n" +
            "        \"amount\": \"100000000000.0000000\",\n" +
            "        \"num_accounts\": 2,\n" +
            "        \"flags\": {\n" +
            "          \"auth_required\": false,\n" +
            "          \"auth_revocable\": false\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"_links\": {\n" +
            "          \"toml\": {\n" +
            "            \"href\": \"\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"asset_type\": \"credit_alphanum12\",\n" +
            "        \"asset_code\": \"AlambLedgerS\",\n" +
            "        \"asset_issuer\": \"GCMXATSZBEYTNPFQXHFQXUYXOTHA4HA5L2YZEKKOVGYWTUT24KIHECG3\",\n" +
            "        \"paging_token\": \"AlambLedgerS_GCMXATSZBEYTNPFQXHFQXUYXOTHA4HA5L2YZEKKOVGYWTUT24KIHECG3_credit_alphanum12\",\n" +
            "        \"amount\": \"0.0000000\",\n" +
            "        \"num_accounts\": 0,\n" +
            "        \"flags\": {\n" +
            "          \"auth_required\": false,\n" +
            "          \"auth_revocable\": false\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"_links\": {\n" +
            "          \"toml\": {\n" +
            "            \"href\": \"\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"asset_type\": \"credit_alphanum4\",\n" +
            "        \"asset_code\": \"AMO\",\n" +
            "        \"asset_issuer\": \"GBOMFBZG5PWUXDIIW5ITVRVEL6YCIC6ZDXLNBH33BNPCX3D7AXDCDKHF\",\n" +
            "        \"paging_token\": \"AMO_GBOMFBZG5PWUXDIIW5ITVRVEL6YCIC6ZDXLNBH33BNPCX3D7AXDCDKHF_credit_alphanum4\",\n" +
            "        \"amount\": \"10000000.0000000\",\n" +
            "        \"num_accounts\": 1,\n" +
            "        \"flags\": {\n" +
            "          \"auth_required\": false,\n" +
            "          \"auth_revocable\": false\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"_links\": {\n" +
            "          \"toml\": {\n" +
            "            \"href\": \"\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"asset_type\": \"credit_alphanum4\",\n" +
            "        \"asset_code\": \"AMO\",\n" +
            "        \"asset_issuer\": \"GDIAIZ7S7L2OBEQBH62KE7IWXK76XA7ES7XCH7JCPXQGV7VB3V6VETOX\",\n" +
            "        \"paging_token\": \"AMO_GDIAIZ7S7L2OBEQBH62KE7IWXK76XA7ES7XCH7JCPXQGV7VB3V6VETOX_credit_alphanum4\",\n" +
            "        \"amount\": \"0.0000000\",\n" +
            "        \"num_accounts\": 1,\n" +
            "        \"flags\": {\n" +
            "          \"auth_required\": false,\n" +
            "          \"auth_revocable\": false\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"_links\": {\n" +
            "          \"toml\": {\n" +
            "            \"href\": \"\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"asset_type\": \"credit_alphanum4\",\n" +
            "        \"asset_code\": \"ASD\",\n" +
            "        \"asset_issuer\": \"GAOMRMILWSX7UXZMYC4X7B7BVJXORYV36XUK3EURVJF7DA6B77ABFVOJ\",\n" +
            "        \"paging_token\": \"ASD_GAOMRMILWSX7UXZMYC4X7B7BVJXORYV36XUK3EURVJF7DA6B77ABFVOJ_credit_alphanum4\",\n" +
            "        \"amount\": \"0.0000000\",\n" +
            "        \"num_accounts\": 0,\n" +
            "        \"flags\": {\n" +
            "          \"auth_required\": false,\n" +
            "          \"auth_revocable\": false\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"_links\": {\n" +
            "          \"toml\": {\n" +
            "            \"href\": \"https://mystellar.tools/.well-known/stellar.toml\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"asset_type\": \"credit_alphanum4\",\n" +
            "        \"asset_code\": \"ASD\",\n" +
            "        \"asset_issuer\": \"GDP4SJE5Y5ODX627DO2F7ZNBAPVXRFHKKR3W4UJ6I4XMW3S3OH2XRWYD\",\n" +
            "        \"paging_token\": \"ASD_GDP4SJE5Y5ODX627DO2F7ZNBAPVXRFHKKR3W4UJ6I4XMW3S3OH2XRWYD_credit_alphanum4\",\n" +
            "        \"amount\": \"0.0000000\",\n" +
            "        \"num_accounts\": 0,\n" +
            "        \"flags\": {\n" +
            "          \"auth_required\": false,\n" +
            "          \"auth_revocable\": false\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"_links\": {\n" +
            "          \"toml\": {\n" +
            "            \"href\": \"\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"asset_type\": \"credit_alphanum12\",\n" +
            "        \"asset_code\": \"AsrtoDollar\",\n" +
            "        \"asset_issuer\": \"GBPGO557IQWSWOIKHWB7YJ5QIBWVF4QS6SPGWT5YBGDUPE6QKOD7RR7S\",\n" +
            "        \"paging_token\": \"AsrtoDollar_GBPGO557IQWSWOIKHWB7YJ5QIBWVF4QS6SPGWT5YBGDUPE6QKOD7RR7S_credit_alphanum12\",\n" +
            "        \"amount\": \"0.0000000\",\n" +
            "        \"num_accounts\": 0,\n" +
            "        \"flags\": {\n" +
            "          \"auth_required\": false,\n" +
            "          \"auth_revocable\": false\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"_links\": {\n" +
            "          \"toml\": {\n" +
            "            \"href\": \"\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"asset_type\": \"credit_alphanum12\",\n" +
            "        \"asset_code\": \"AsrtoDollar\",\n" +
            "        \"asset_issuer\": \"GDJWXY5XUASXNL4ABCONR6T5MOXJ2S4HD6WDNAJDSDKQ4VS3TVUQJEDJ\",\n" +
            "        \"paging_token\": \"AsrtoDollar_GDJWXY5XUASXNL4ABCONR6T5MOXJ2S4HD6WDNAJDSDKQ4VS3TVUQJEDJ_credit_alphanum12\",\n" +
            "        \"amount\": \"0.0000000\",\n" +
            "        \"num_accounts\": 0,\n" +
            "        \"flags\": {\n" +
            "          \"auth_required\": false,\n" +
            "          \"auth_revocable\": false\n" +
            "        }\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";
}
