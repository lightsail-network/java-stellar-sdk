package org.stellar.sdk.responses;

import com.google.gson.reflect.TypeToken;
import junit.framework.TestCase;
import org.junit.Test;

public class TradesPageDeserializerTest extends TestCase {
    @Test
    public void testDeserialize() {
        Page<TradeResponse> tradesPage = GsonSingleton.getInstance().fromJson(json, new TypeToken<Page<TradeResponse>>() {}.getType());

        assertEquals(tradesPage.getLinks().getNext().getHref(), "https://horizon.stellar.org/order_book/trades?order=asc&limit=10&cursor=43186647780560897-5");
        assertEquals(tradesPage.getLinks().getPrev().getHref(), "https://horizon.stellar.org/order_book/trades?order=desc&limit=10&cursor=37129640086605825-1");
        assertEquals(tradesPage.getLinks().getSelf().getHref(), "https://horizon.stellar.org/order_book/trades?order=asc&limit=10&cursor=");

        assertEquals(tradesPage.getRecords().get(0).getId(), "37129640086605825-1");
        assertEquals(tradesPage.getRecords().get(0).getPagingToken(), "37129640086605825-1");
        assertEquals(tradesPage.getRecords().get(1).getSeller().getAccountId(), "GCI7ILB37OFVHLLSA74UCXZFCTPEBJOZK7YCNBI7DKH7D76U4CRJBL2A");
    }

    String json = "{\n" +
            "  \"_links\": {\n" +
            "    \"self\": {\n" +
            "      \"href\": \"https://horizon.stellar.org/order_book/trades?order=asc\\u0026limit=10\\u0026cursor=\"\n" +
            "    },\n" +
            "    \"next\": {\n" +
            "      \"href\": \"https://horizon.stellar.org/order_book/trades?order=asc\\u0026limit=10\\u0026cursor=43186647780560897-5\"\n" +
            "    },\n" +
            "    \"prev\": {\n" +
            "      \"href\": \"https://horizon.stellar.org/order_book/trades?order=desc\\u0026limit=10\\u0026cursor=37129640086605825-1\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"_embedded\": {\n" +
            "    \"records\": [\n" +
            "      {\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GCI7ILB37OFVHLLSA74UCXZFCTPEBJOZK7YCNBI7DKH7D76U4CRJBL2A\"\n" +
            "          },\n" +
            "          \"seller\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GCI7ILB37OFVHLLSA74UCXZFCTPEBJOZK7YCNBI7DKH7D76U4CRJBL2A\"\n" +
            "          },\n" +
            "          \"buyer\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GDFGO6BQAYGGBC4IANIMTAUUOITROGFO42ZPPG5J2SAF5UHNGRABVAE3\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"37129640086605825-1\",\n" +
            "        \"paging_token\": \"37129640086605825-1\",\n" +
            "        \"seller\": \"GCI7ILB37OFVHLLSA74UCXZFCTPEBJOZK7YCNBI7DKH7D76U4CRJBL2A\",\n" +
            "        \"sold_amount\": \"1520.0000000\",\n" +
            "        \"sold_asset_type\": \"native\",\n" +
            "        \"buyer\": \"GDFGO6BQAYGGBC4IANIMTAUUOITROGFO42ZPPG5J2SAF5UHNGRABVAE3\",\n" +
            "        \"bought_amount\": \"3.6854018\",\n" +
            "        \"bought_asset_type\": \"credit_alphanum4\",\n" +
            "        \"bought_asset_code\": \"DEMO\",\n" +
            "        \"bought_asset_issuer\": \"GBAMBOOZDWZPVV52RCLJQYMQNXOBLOXWNQAY2IF2FREV2WL46DBCH3BE\",\n" +
            "        \"created_at\": \"2017-01-17T21:28:19Z\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GCI7ILB37OFVHLLSA74UCXZFCTPEBJOZK7YCNBI7DKH7D76U4CRJBL2A\"\n" +
            "          },\n" +
            "          \"seller\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GCI7ILB37OFVHLLSA74UCXZFCTPEBJOZK7YCNBI7DKH7D76U4CRJBL2A\"\n" +
            "          },\n" +
            "          \"buyer\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GDFGO6BQAYGGBC4IANIMTAUUOITROGFO42ZPPG5J2SAF5UHNGRABVAE3\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"37130752483135489-2\",\n" +
            "        \"paging_token\": \"37130752483135489-2\",\n" +
            "        \"seller\": \"GCI7ILB37OFVHLLSA74UCXZFCTPEBJOZK7YCNBI7DKH7D76U4CRJBL2A\",\n" +
            "        \"sold_amount\": \"100.5759309\",\n" +
            "        \"sold_asset_type\": \"native\",\n" +
            "        \"buyer\": \"GDFGO6BQAYGGBC4IANIMTAUUOITROGFO42ZPPG5J2SAF5UHNGRABVAE3\",\n" +
            "        \"bought_amount\": \"0.2508100\",\n" +
            "        \"bought_asset_type\": \"credit_alphanum4\",\n" +
            "        \"bought_asset_code\": \"DEMO\",\n" +
            "        \"bought_asset_issuer\": \"GBAMBOOZDWZPVV52RCLJQYMQNXOBLOXWNQAY2IF2FREV2WL46DBCH3BE\",\n" +
            "        \"created_at\": \"2017-01-17T21:49:54Z\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GCI7ILB37OFVHLLSA74UCXZFCTPEBJOZK7YCNBI7DKH7D76U4CRJBL2A\"\n" +
            "          },\n" +
            "          \"seller\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GCI7ILB37OFVHLLSA74UCXZFCTPEBJOZK7YCNBI7DKH7D76U4CRJBL2A\"\n" +
            "          },\n" +
            "          \"buyer\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GDFGO6BQAYGGBC4IANIMTAUUOITROGFO42ZPPG5J2SAF5UHNGRABVAE3\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"37141648815165441-1\",\n" +
            "        \"paging_token\": \"37141648815165441-1\",\n" +
            "        \"seller\": \"GCI7ILB37OFVHLLSA74UCXZFCTPEBJOZK7YCNBI7DKH7D76U4CRJBL2A\",\n" +
            "        \"sold_amount\": \"25.0000000\",\n" +
            "        \"sold_asset_type\": \"native\",\n" +
            "        \"buyer\": \"GDFGO6BQAYGGBC4IANIMTAUUOITROGFO42ZPPG5J2SAF5UHNGRABVAE3\",\n" +
            "        \"bought_amount\": \"0.0623425\",\n" +
            "        \"bought_asset_type\": \"credit_alphanum4\",\n" +
            "        \"bought_asset_code\": \"DEMO\",\n" +
            "        \"bought_asset_issuer\": \"GBAMBOOZDWZPVV52RCLJQYMQNXOBLOXWNQAY2IF2FREV2WL46DBCH3BE\",\n" +
            "        \"created_at\": \"2017-01-18T01:21:09Z\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GCI7ILB37OFVHLLSA74UCXZFCTPEBJOZK7YCNBI7DKH7D76U4CRJBL2A\"\n" +
            "          },\n" +
            "          \"seller\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GCI7ILB37OFVHLLSA74UCXZFCTPEBJOZK7YCNBI7DKH7D76U4CRJBL2A\"\n" +
            "          },\n" +
            "          \"buyer\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GDFGO6BQAYGGBC4IANIMTAUUOITROGFO42ZPPG5J2SAF5UHNGRABVAE3\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"37141717534642177-2\",\n" +
            "        \"paging_token\": \"37141717534642177-2\",\n" +
            "        \"seller\": \"GCI7ILB37OFVHLLSA74UCXZFCTPEBJOZK7YCNBI7DKH7D76U4CRJBL2A\",\n" +
            "        \"sold_amount\": \"100.5759309\",\n" +
            "        \"sold_asset_type\": \"native\",\n" +
            "        \"buyer\": \"GDFGO6BQAYGGBC4IANIMTAUUOITROGFO42ZPPG5J2SAF5UHNGRABVAE3\",\n" +
            "        \"bought_amount\": \"0.2508100\",\n" +
            "        \"bought_asset_type\": \"credit_alphanum4\",\n" +
            "        \"bought_asset_code\": \"DEMO\",\n" +
            "        \"bought_asset_issuer\": \"GBAMBOOZDWZPVV52RCLJQYMQNXOBLOXWNQAY2IF2FREV2WL46DBCH3BE\",\n" +
            "        \"created_at\": \"2017-01-18T01:22:31Z\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GCI7ILB37OFVHLLSA74UCXZFCTPEBJOZK7YCNBI7DKH7D76U4CRJBL2A\"\n" +
            "          },\n" +
            "          \"seller\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GCI7ILB37OFVHLLSA74UCXZFCTPEBJOZK7YCNBI7DKH7D76U4CRJBL2A\"\n" +
            "          },\n" +
            "          \"buyer\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GDFGO6BQAYGGBC4IANIMTAUUOITROGFO42ZPPG5J2SAF5UHNGRABVAE3\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"37228243945787393-2\",\n" +
            "        \"paging_token\": \"37228243945787393-2\",\n" +
            "        \"seller\": \"GCI7ILB37OFVHLLSA74UCXZFCTPEBJOZK7YCNBI7DKH7D76U4CRJBL2A\",\n" +
            "        \"sold_amount\": \"10.0001205\",\n" +
            "        \"sold_asset_type\": \"native\",\n" +
            "        \"buyer\": \"GDFGO6BQAYGGBC4IANIMTAUUOITROGFO42ZPPG5J2SAF5UHNGRABVAE3\",\n" +
            "        \"bought_amount\": \"0.0250930\",\n" +
            "        \"bought_asset_type\": \"credit_alphanum4\",\n" +
            "        \"bought_asset_code\": \"DEMO\",\n" +
            "        \"bought_asset_issuer\": \"GBAMBOOZDWZPVV52RCLJQYMQNXOBLOXWNQAY2IF2FREV2WL46DBCH3BE\",\n" +
            "        \"created_at\": \"2017-01-19T05:20:43Z\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GCI7ILB37OFVHLLSA74UCXZFCTPEBJOZK7YCNBI7DKH7D76U4CRJBL2A\"\n" +
            "          },\n" +
            "          \"seller\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GCI7ILB37OFVHLLSA74UCXZFCTPEBJOZK7YCNBI7DKH7D76U4CRJBL2A\"\n" +
            "          },\n" +
            "          \"buyer\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GAKLCFRTFDXKOEEUSBS23FBSUUVJRMDQHGCHNGGGJZQRK7BCPIMHUC4P\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"42890518375436289-1\",\n" +
            "        \"paging_token\": \"42890518375436289-1\",\n" +
            "        \"seller\": \"GCI7ILB37OFVHLLSA74UCXZFCTPEBJOZK7YCNBI7DKH7D76U4CRJBL2A\",\n" +
            "        \"sold_amount\": \"0.0001000\",\n" +
            "        \"sold_asset_type\": \"native\",\n" +
            "        \"buyer\": \"GAKLCFRTFDXKOEEUSBS23FBSUUVJRMDQHGCHNGGGJZQRK7BCPIMHUC4P\",\n" +
            "        \"bought_amount\": \"0.0000002\",\n" +
            "        \"bought_asset_type\": \"credit_alphanum4\",\n" +
            "        \"bought_asset_code\": \"DEMO\",\n" +
            "        \"bought_asset_issuer\": \"GBAMBOOZDWZPVV52RCLJQYMQNXOBLOXWNQAY2IF2FREV2WL46DBCH3BE\",\n" +
            "        \"created_at\": \"2017-04-05T23:28:35Z\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GCI7ILB37OFVHLLSA74UCXZFCTPEBJOZK7YCNBI7DKH7D76U4CRJBL2A\"\n" +
            "          },\n" +
            "          \"seller\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GCI7ILB37OFVHLLSA74UCXZFCTPEBJOZK7YCNBI7DKH7D76U4CRJBL2A\"\n" +
            "          },\n" +
            "          \"buyer\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GAKLCFRTFDXKOEEUSBS23FBSUUVJRMDQHGCHNGGGJZQRK7BCPIMHUC4P\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"42891175505436673-1\",\n" +
            "        \"paging_token\": \"42891175505436673-1\",\n" +
            "        \"seller\": \"GCI7ILB37OFVHLLSA74UCXZFCTPEBJOZK7YCNBI7DKH7D76U4CRJBL2A\",\n" +
            "        \"sold_amount\": \"1.0000000\",\n" +
            "        \"sold_asset_type\": \"native\",\n" +
            "        \"buyer\": \"GAKLCFRTFDXKOEEUSBS23FBSUUVJRMDQHGCHNGGGJZQRK7BCPIMHUC4P\",\n" +
            "        \"bought_amount\": \"0.0024937\",\n" +
            "        \"bought_asset_type\": \"credit_alphanum4\",\n" +
            "        \"bought_asset_code\": \"DEMO\",\n" +
            "        \"bought_asset_issuer\": \"GBAMBOOZDWZPVV52RCLJQYMQNXOBLOXWNQAY2IF2FREV2WL46DBCH3BE\",\n" +
            "        \"created_at\": \"2017-04-05T23:41:17Z\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GCI7ILB37OFVHLLSA74UCXZFCTPEBJOZK7YCNBI7DKH7D76U4CRJBL2A\"\n" +
            "          },\n" +
            "          \"seller\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GCI7ILB37OFVHLLSA74UCXZFCTPEBJOZK7YCNBI7DKH7D76U4CRJBL2A\"\n" +
            "          },\n" +
            "          \"buyer\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GCPP2T36NFH4D7OOCYCHKJSL3IWVRQC6CGHWADUB6JL6XKSZZP3KXACP\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"43186647780560897-1\",\n" +
            "        \"paging_token\": \"43186647780560897-1\",\n" +
            "        \"seller\": \"GCI7ILB37OFVHLLSA74UCXZFCTPEBJOZK7YCNBI7DKH7D76U4CRJBL2A\",\n" +
            "        \"sold_amount\": \"73.9999198\",\n" +
            "        \"sold_asset_type\": \"native\",\n" +
            "        \"buyer\": \"GCPP2T36NFH4D7OOCYCHKJSL3IWVRQC6CGHWADUB6JL6XKSZZP3KXACP\",\n" +
            "        \"bought_amount\": \"0.1845336\",\n" +
            "        \"bought_asset_type\": \"credit_alphanum4\",\n" +
            "        \"bought_asset_code\": \"DEMO\",\n" +
            "        \"bought_asset_issuer\": \"GBAMBOOZDWZPVV52RCLJQYMQNXOBLOXWNQAY2IF2FREV2WL46DBCH3BE\",\n" +
            "        \"created_at\": \"2017-04-09T09:57:43Z\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GCI7ILB37OFVHLLSA74UCXZFCTPEBJOZK7YCNBI7DKH7D76U4CRJBL2A\"\n" +
            "          },\n" +
            "          \"seller\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GCI7ILB37OFVHLLSA74UCXZFCTPEBJOZK7YCNBI7DKH7D76U4CRJBL2A\"\n" +
            "          },\n" +
            "          \"buyer\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GCPP2T36NFH4D7OOCYCHKJSL3IWVRQC6CGHWADUB6JL6XKSZZP3KXACP\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"43186647780560897-3\",\n" +
            "        \"paging_token\": \"43186647780560897-3\",\n" +
            "        \"seller\": \"GCI7ILB37OFVHLLSA74UCXZFCTPEBJOZK7YCNBI7DKH7D76U4CRJBL2A\",\n" +
            "        \"sold_amount\": \"100.0000000\",\n" +
            "        \"sold_asset_type\": \"native\",\n" +
            "        \"buyer\": \"GCPP2T36NFH4D7OOCYCHKJSL3IWVRQC6CGHWADUB6JL6XKSZZP3KXACP\",\n" +
            "        \"bought_amount\": \"0.2493700\",\n" +
            "        \"bought_asset_type\": \"credit_alphanum4\",\n" +
            "        \"bought_asset_code\": \"DEMO\",\n" +
            "        \"bought_asset_issuer\": \"GBAMBOOZDWZPVV52RCLJQYMQNXOBLOXWNQAY2IF2FREV2WL46DBCH3BE\",\n" +
            "        \"created_at\": \"2017-04-09T09:57:43Z\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GCI7ILB37OFVHLLSA74UCXZFCTPEBJOZK7YCNBI7DKH7D76U4CRJBL2A\"\n" +
            "          },\n" +
            "          \"seller\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GCI7ILB37OFVHLLSA74UCXZFCTPEBJOZK7YCNBI7DKH7D76U4CRJBL2A\"\n" +
            "          },\n" +
            "          \"buyer\": {\n" +
            "            \"href\": \"https://horizon.stellar.org/accounts/GCPP2T36NFH4D7OOCYCHKJSL3IWVRQC6CGHWADUB6JL6XKSZZP3KXACP\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"id\": \"43186647780560897-5\",\n" +
            "        \"paging_token\": \"43186647780560897-5\",\n" +
            "        \"seller\": \"GCI7ILB37OFVHLLSA74UCXZFCTPEBJOZK7YCNBI7DKH7D76U4CRJBL2A\",\n" +
            "        \"sold_amount\": \"274.0057219\",\n" +
            "        \"sold_asset_type\": \"native\",\n" +
            "        \"buyer\": \"GCPP2T36NFH4D7OOCYCHKJSL3IWVRQC6CGHWADUB6JL6XKSZZP3KXACP\",\n" +
            "        \"bought_amount\": \"0.6800000\",\n" +
            "        \"bought_asset_type\": \"credit_alphanum4\",\n" +
            "        \"bought_asset_code\": \"DEMO\",\n" +
            "        \"bought_asset_issuer\": \"GBAMBOOZDWZPVV52RCLJQYMQNXOBLOXWNQAY2IF2FREV2WL46DBCH3BE\",\n" +
            "        \"created_at\": \"2017-04-09T09:57:43Z\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";
}
