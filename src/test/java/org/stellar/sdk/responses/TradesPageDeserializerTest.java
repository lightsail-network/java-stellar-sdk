package org.stellar.sdk.responses;

import com.google.gson.reflect.TypeToken;
import junit.framework.TestCase;
import org.junit.Test;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeNative;
import org.stellar.sdk.KeyPair;

public class TradesPageDeserializerTest extends TestCase {
    @Test
    public void testDeserialize() {
        Page<TradeResponse> tradesPage = GsonSingleton.getInstance().fromJson(json, new TypeToken<Page<TradeResponse>>() {}.getType());

        assertEquals(tradesPage.getLinks().getNext().getHref(), "https://horizon.stellar.org/trades?cursor=3748308153536513-0&limit=10&order=asc");
        assertEquals(tradesPage.getLinks().getPrev().getHref(), "https://horizon.stellar.org/trades?cursor=3697472920621057-0&limit=10&order=desc");
        assertEquals(tradesPage.getLinks().getSelf().getHref(), "https://horizon.stellar.org/trades?cursor=&limit=10&order=asc");

        assertEquals(tradesPage.getRecords().get(0).getId(), "3697472920621057-0");
        assertEquals(tradesPage.getRecords().get(0).getPagingToken(), "3697472920621057-0");
        assertEquals(tradesPage.getRecords().get(0).getLedgerCloseTime(), "2015-11-18T03:47:47Z");
        assertEquals(tradesPage.getRecords().get(0).getOfferId(), "9");
        assertEquals(tradesPage.getRecords().get(0).getBaseOfferId(), "10");
        assertEquals(tradesPage.getRecords().get(0).getCounterOfferId(), "11");
        assertEquals(tradesPage.getRecords().get(0).getBaseAsset(), new AssetTypeNative());
        assertEquals(tradesPage.getRecords().get(0).getCounterAsset(), Asset.createNonNativeAsset("JPY", "GBVAOIACNSB7OVUXJYC5UE2D4YK2F7A24T7EE5YOMN4CE6GCHUTOUQXM"));
        assertEquals(tradesPage.getRecords().get(0).getPrice().getNumerator(), 267);
        assertEquals(tradesPage.getRecords().get(0).getPrice().getDenominator(), 1000);

        assertEquals(tradesPage.getRecords().get(1).getBaseAccount(), "GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G");
    }

    String json = "{\n" +
            "  \"_links\": {\n" +
            "    \"self\": {\n" +
            "      \"href\": \"https://horizon.stellar.org/trades?cursor=\\u0026limit=10\\u0026order=asc\"\n" +
            "    },\n" +
            "    \"next\": {\n" +
            "      \"href\": \"https://horizon.stellar.org/trades?cursor=3748308153536513-0\\u0026limit=10\\u0026order=asc\"\n" +
            "    },\n" +
            "    \"prev\": {\n" +
            "      \"href\": \"https://horizon.stellar.org/trades?cursor=3697472920621057-0\\u0026limit=10\\u0026order=desc\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"_embedded\": {\n" +
            "    \"records\": [\n" +
            "      {\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"\"\n" +
            "          },\n" +
            "          \"base\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G\"\n" +
            "          },\n" +
            "          \"counter\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L\"\n" +
            "          },\n" +
            "          \"operation\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/operations/3697472920621057\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"3697472920621057-0\",\n" +
            "        \"paging_token\": \"3697472920621057-0\",\n" +
            "        \"ledger_close_time\": \"2015-11-18T03:47:47Z\",\n" +
            "        \"offer_id\": \"9\",\n" +
            "        \"base_offer_id\": \"10\",\n" +
            "        \"base_account\": \"GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G\",\n" +
            "        \"base_amount\": \"10.0000000\",\n" +
            "        \"base_asset_type\": \"native\",\n" +
            "        \"counter_offer_id\": \"11\",\n" +
            "        \"counter_account\": \"GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L\",\n" +
            "        \"counter_amount\": \"2.6700000\",\n" +
            "        \"counter_asset_type\": \"credit_alphanum4\",\n" +
            "        \"counter_asset_code\": \"JPY\",\n" +
            "        \"counter_asset_issuer\": \"GBVAOIACNSB7OVUXJYC5UE2D4YK2F7A24T7EE5YOMN4CE6GCHUTOUQXM\",\n" +
            "        \"base_is_seller\": true,\n" +
            "        \"price\": {\n" +
            "          \"n\": 267,\n" +
            "          \"d\": 1000\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"\"\n" +
            "          },\n" +
            "          \"base\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G\"\n" +
            "          },\n" +
            "          \"counter\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L\"\n" +
            "          },\n" +
            "          \"operation\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/operations/3697472920621057\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"3697472920621057-1\",\n" +
            "        \"paging_token\": \"3697472920621057-1\",\n" +
            "        \"ledger_close_time\": \"2015-11-18T03:47:47Z\",\n" +
            "        \"offer_id\": \"4\",\n" +
            "        \"base_account\": \"GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G\",\n" +
            "        \"base_amount\": \"10.0000000\",\n" +
            "        \"base_asset_type\": \"native\",\n" +
            "        \"counter_account\": \"GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L\",\n" +
            "        \"counter_amount\": \"2.6800000\",\n" +
            "        \"counter_asset_type\": \"credit_alphanum4\",\n" +
            "        \"counter_asset_code\": \"JPY\",\n" +
            "        \"counter_asset_issuer\": \"GBVAOIACNSB7OVUXJYC5UE2D4YK2F7A24T7EE5YOMN4CE6GCHUTOUQXM\",\n" +
            "        \"base_is_seller\": true,\n" +
            "        \"price\": {\n" +
            "          \"n\": 67,\n" +
            "          \"d\": 250\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"\"\n" +
            "          },\n" +
            "          \"base\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G\"\n" +
            "          },\n" +
            "          \"counter\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L\"\n" +
            "          },\n" +
            "          \"operation\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/operations/3697472920621057\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"3697472920621057-2\",\n" +
            "        \"paging_token\": \"3697472920621057-2\",\n" +
            "        \"ledger_close_time\": \"2015-11-18T03:47:47Z\",\n" +
            "        \"offer_id\": \"8\",\n" +
            "        \"base_account\": \"GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G\",\n" +
            "        \"base_amount\": \"20.0000000\",\n" +
            "        \"base_asset_type\": \"native\",\n" +
            "        \"counter_account\": \"GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L\",\n" +
            "        \"counter_amount\": \"5.3600000\",\n" +
            "        \"counter_asset_type\": \"credit_alphanum4\",\n" +
            "        \"counter_asset_code\": \"JPY\",\n" +
            "        \"counter_asset_issuer\": \"GBVAOIACNSB7OVUXJYC5UE2D4YK2F7A24T7EE5YOMN4CE6GCHUTOUQXM\",\n" +
            "        \"base_is_seller\": true,\n" +
            "        \"price\": {\n" +
            "          \"n\": 67,\n" +
            "          \"d\": 250\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"\"\n" +
            "          },\n" +
            "          \"base\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G\"\n" +
            "          },\n" +
            "          \"counter\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L\"\n" +
            "          },\n" +
            "          \"operation\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/operations/3712329212497921\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"3712329212497921-0\",\n" +
            "        \"paging_token\": \"3712329212497921-0\",\n" +
            "        \"ledger_close_time\": \"2015-11-18T07:26:21Z\",\n" +
            "        \"offer_id\": \"36\",\n" +
            "        \"base_account\": \"GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G\",\n" +
            "        \"base_amount\": \"5.0000000\",\n" +
            "        \"base_asset_type\": \"native\",\n" +
            "        \"counter_account\": \"GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L\",\n" +
            "        \"counter_amount\": \"1.2000192\",\n" +
            "        \"counter_asset_type\": \"credit_alphanum4\",\n" +
            "        \"counter_asset_code\": \"JPY\",\n" +
            "        \"counter_asset_issuer\": \"GBVAOIACNSB7OVUXJYC5UE2D4YK2F7A24T7EE5YOMN4CE6GCHUTOUQXM\",\n" +
            "        \"base_is_seller\": false,\n" +
            "        \"price\": {\n" +
            "          \"n\": 5000,\n" +
            "          \"d\": 20833\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"\"\n" +
            "          },\n" +
            "          \"base\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L\"\n" +
            "          },\n" +
            "          \"counter\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G\"\n" +
            "          },\n" +
            "          \"operation\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/operations/3716237632737281\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"3716237632737281-0\",\n" +
            "        \"paging_token\": \"3716237632737281-0\",\n" +
            "        \"ledger_close_time\": \"2015-11-18T08:27:26Z\",\n" +
            "        \"offer_id\": \"37\",\n" +
            "        \"base_account\": \"GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L\",\n" +
            "        \"base_amount\": \"10.0000000\",\n" +
            "        \"base_asset_type\": \"native\",\n" +
            "        \"counter_account\": \"GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G\",\n" +
            "        \"counter_amount\": \"2.4500000\",\n" +
            "        \"counter_asset_type\": \"credit_alphanum4\",\n" +
            "        \"counter_asset_code\": \"JPY\",\n" +
            "        \"counter_asset_issuer\": \"GBVAOIACNSB7OVUXJYC5UE2D4YK2F7A24T7EE5YOMN4CE6GCHUTOUQXM\",\n" +
            "        \"base_is_seller\": true,\n" +
            "        \"price\": {\n" +
            "          \"n\": 49,\n" +
            "          \"d\": 200\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"\"\n" +
            "          },\n" +
            "          \"base\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L\"\n" +
            "          },\n" +
            "          \"counter\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G\"\n" +
            "          },\n" +
            "          \"operation\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/operations/3716302057246721\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"3716302057246721-0\",\n" +
            "        \"paging_token\": \"3716302057246721-0\",\n" +
            "        \"ledger_close_time\": \"2015-11-18T08:28:40Z\",\n" +
            "        \"offer_id\": \"35\",\n" +
            "        \"base_account\": \"GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L\",\n" +
            "        \"base_amount\": \"10.0000000\",\n" +
            "        \"base_asset_type\": \"native\",\n" +
            "        \"counter_account\": \"GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G\",\n" +
            "        \"counter_amount\": \"2.5000000\",\n" +
            "        \"counter_asset_type\": \"credit_alphanum4\",\n" +
            "        \"counter_asset_code\": \"JPY\",\n" +
            "        \"counter_asset_issuer\": \"GBVAOIACNSB7OVUXJYC5UE2D4YK2F7A24T7EE5YOMN4CE6GCHUTOUQXM\",\n" +
            "        \"base_is_seller\": true,\n" +
            "        \"price\": {\n" +
            "          \"n\": 1,\n" +
            "          \"d\": 4\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"\"\n" +
            "          },\n" +
            "          \"base\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L\"\n" +
            "          },\n" +
            "          \"counter\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G\"\n" +
            "          },\n" +
            "          \"operation\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/operations/3716302057246721\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"3716302057246721-1\",\n" +
            "        \"paging_token\": \"3716302057246721-1\",\n" +
            "        \"ledger_close_time\": \"2015-11-18T08:28:40Z\",\n" +
            "        \"offer_id\": \"34\",\n" +
            "        \"base_account\": \"GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L\",\n" +
            "        \"base_amount\": \"10.0000000\",\n" +
            "        \"base_asset_type\": \"native\",\n" +
            "        \"counter_account\": \"GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G\",\n" +
            "        \"counter_amount\": \"3.0000000\",\n" +
            "        \"counter_asset_type\": \"credit_alphanum4\",\n" +
            "        \"counter_asset_code\": \"JPY\",\n" +
            "        \"counter_asset_issuer\": \"GBVAOIACNSB7OVUXJYC5UE2D4YK2F7A24T7EE5YOMN4CE6GCHUTOUQXM\",\n" +
            "        \"base_is_seller\": true,\n" +
            "        \"price\": {\n" +
            "          \"n\": 3,\n" +
            "          \"d\": 10\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"\"\n" +
            "          },\n" +
            "          \"base\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G\"\n" +
            "          },\n" +
            "          \"counter\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L\"\n" +
            "          },\n" +
            "          \"operation\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/operations/3725961438695425\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"3725961438695425-0\",\n" +
            "        \"paging_token\": \"3725961438695425-0\",\n" +
            "        \"ledger_close_time\": \"2015-11-18T11:09:30Z\",\n" +
            "        \"offer_id\": \"47\",\n" +
            "        \"base_account\": \"GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G\",\n" +
            "        \"base_amount\": \"5.0000000\",\n" +
            "        \"base_asset_type\": \"native\",\n" +
            "        \"counter_account\": \"GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L\",\n" +
            "        \"counter_amount\": \"1.0000000\",\n" +
            "        \"counter_asset_type\": \"credit_alphanum4\",\n" +
            "        \"counter_asset_code\": \"JPY\",\n" +
            "        \"counter_asset_issuer\": \"GBVAOIACNSB7OVUXJYC5UE2D4YK2F7A24T7EE5YOMN4CE6GCHUTOUQXM\",\n" +
            "        \"base_is_seller\": false,\n" +
            "        \"price\": {\n" +
            "          \"n\": 1,\n" +
            "          \"d\": 5\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"\"\n" +
            "          },\n" +
            "          \"base\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L\"\n" +
            "          },\n" +
            "          \"counter\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G\"\n" +
            "          },\n" +
            "          \"operation\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/operations/3748080520269825\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"3748080520269825-0\",\n" +
            "        \"paging_token\": \"3748080520269825-0\",\n" +
            "        \"ledger_close_time\": \"2015-11-18T16:56:09Z\",\n" +
            "        \"offer_id\": \"53\",\n" +
            "        \"base_account\": \"GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L\",\n" +
            "        \"base_amount\": \"10.0000000\",\n" +
            "        \"base_asset_type\": \"native\",\n" +
            "        \"counter_account\": \"GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G\",\n" +
            "        \"counter_amount\": \"3.0000000\",\n" +
            "        \"counter_asset_type\": \"credit_alphanum4\",\n" +
            "        \"counter_asset_code\": \"JPY\",\n" +
            "        \"counter_asset_issuer\": \"GBVAOIACNSB7OVUXJYC5UE2D4YK2F7A24T7EE5YOMN4CE6GCHUTOUQXM\",\n" +
            "        \"base_is_seller\": true,\n" +
            "        \"price\": {\n" +
            "          \"n\": 3,\n" +
            "          \"d\": 10\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"\"\n" +
            "          },\n" +
            "          \"base\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G\"\n" +
            "          },\n" +
            "          \"counter\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L\"\n" +
            "          },\n" +
            "          \"operation\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/operations/3748308153536513\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"3748308153536513-0\",\n" +
            "        \"paging_token\": \"3748308153536513-0\",\n" +
            "        \"ledger_close_time\": \"2015-11-18T16:59:37Z\",\n" +
            "        \"offer_id\": \"59\",\n" +
            "        \"base_account\": \"GAVH5JM5OKXGMQDS7YPRJ4MQCPXJUGH26LYQPQJ4SOMOJ4SXY472ZM7G\",\n" +
            "        \"base_amount\": \"30.0000000\",\n" +
            "        \"base_asset_type\": \"native\",\n" +
            "        \"counter_account\": \"GBB4JST32UWKOLGYYSCEYBHBCOFL2TGBHDVOMZP462ET4ZRD4ULA7S2L\",\n" +
            "        \"counter_amount\": \"9.0000000\",\n" +
            "        \"counter_asset_type\": \"credit_alphanum4\",\n" +
            "        \"counter_asset_code\": \"JPY\",\n" +
            "        \"counter_asset_issuer\": \"GBVAOIACNSB7OVUXJYC5UE2D4YK2F7A24T7EE5YOMN4CE6GCHUTOUQXM\",\n" +
            "        \"base_is_seller\": true,\n" +
            "        \"price\": {\n" +
            "          \"n\": 3,\n" +
            "          \"d\": 10\n" +
            "        }\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";
}
