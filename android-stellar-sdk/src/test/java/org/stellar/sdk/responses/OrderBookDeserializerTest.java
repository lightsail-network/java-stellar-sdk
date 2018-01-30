package org.stellar.sdk.responses;

import junit.framework.TestCase;
import org.junit.Test;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeNative;
import org.stellar.sdk.KeyPair;

public class OrderBookDeserializerTest extends TestCase {
    @Test
    public void testDeserialize() {
        OrderBookResponse orderBook = GsonSingleton.getInstance().fromJson(json, OrderBookResponse.class);

        assertEquals(orderBook.getBase(), new AssetTypeNative());
        assertEquals(orderBook.getCounter(), Asset.createNonNativeAsset("DEMO", KeyPair.fromAccountId("GBAMBOOZDWZPVV52RCLJQYMQNXOBLOXWNQAY2IF2FREV2WL46DBCH3BE")));

        assertEquals(orderBook.getBids()[0].getAmount(), "31.4007644");
        assertEquals(orderBook.getBids()[0].getPrice(), "0.0024224");
        assertEquals(orderBook.getBids()[0].getPriceR().getNumerator(), 4638606);
        assertEquals(orderBook.getBids()[0].getPriceR().getDenominator(), 1914900241);

        assertEquals(orderBook.getBids()[1].getAmount(), "5.9303650");
        assertEquals(orderBook.getBids()[1].getPrice(), "0.0024221");

        assertEquals(orderBook.getAsks()[0].getAmount(), "541.4550766");
        assertEquals(orderBook.getAsks()[0].getPrice(), "0.0025093");

        assertEquals(orderBook.getAsks()[1].getAmount(), "121.9999600");
        assertEquals(orderBook.getAsks()[1].getPrice(), "0.0025093");
    }

    String json = "{\n" +
            "  \"bids\": [\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 4638606,\n" +
            "        \"d\": 1914900241\n" +
            "      },\n" +
            "      \"price\": \"0.0024224\",\n" +
            "      \"amount\": \"31.4007644\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 2048926,\n" +
            "        \"d\": 845926319\n" +
            "      },\n" +
            "      \"price\": \"0.0024221\",\n" +
            "      \"amount\": \"5.9303650\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 4315154,\n" +
            "        \"d\": 1782416791\n" +
            "      },\n" +
            "      \"price\": \"0.0024210\",\n" +
            "      \"amount\": \"2.6341583\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 3360181,\n" +
            "        \"d\": 1397479136\n" +
            "      },\n" +
            "      \"price\": \"0.0024045\",\n" +
            "      \"amount\": \"5.9948532\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 2367836,\n" +
            "        \"d\": 985908229\n" +
            "      },\n" +
            "      \"price\": \"0.0024017\",\n" +
            "      \"amount\": \"3.8896537\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 4687363,\n" +
            "        \"d\": 1952976585\n" +
            "      },\n" +
            "      \"price\": \"0.0024001\",\n" +
            "      \"amount\": \"1.5747618\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 903753,\n" +
            "        \"d\": 380636870\n" +
            "      },\n" +
            "      \"price\": \"0.0023743\",\n" +
            "      \"amount\": \"1.6182054\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 2562439,\n" +
            "        \"d\": 1081514977\n" +
            "      },\n" +
            "      \"price\": \"0.0023693\",\n" +
            "      \"amount\": \"15.1310429\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 2588843,\n" +
            "        \"d\": 1129671233\n" +
            "      },\n" +
            "      \"price\": \"0.0022917\",\n" +
            "      \"amount\": \"2.7172038\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 3249035,\n" +
            "        \"d\": 1425861493\n" +
            "      },\n" +
            "      \"price\": \"0.0022786\",\n" +
            "      \"amount\": \"6.7610234\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 629489,\n" +
            "        \"d\": 284529942\n" +
            "      },\n" +
            "      \"price\": \"0.0022124\",\n" +
            "      \"amount\": \"8.6216043\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 1428194,\n" +
            "        \"d\": 664535371\n" +
            "      },\n" +
            "      \"price\": \"0.0021492\",\n" +
            "      \"amount\": \"11.0263350\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 1653667,\n" +
            "        \"d\": 771446377\n" +
            "      },\n" +
            "      \"price\": \"0.0021436\",\n" +
            "      \"amount\": \"26.0527506\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 3613348,\n" +
            "        \"d\": 1709911165\n" +
            "      },\n" +
            "      \"price\": \"0.0021132\",\n" +
            "      \"amount\": \"1.6923954\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 2674223,\n" +
            "        \"d\": 1280335392\n" +
            "      },\n" +
            "      \"price\": \"0.0020887\",\n" +
            "      \"amount\": \"0.9882259\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 3594842,\n" +
            "        \"d\": 1769335169\n" +
            "      },\n" +
            "      \"price\": \"0.0020317\",\n" +
            "      \"amount\": \"6.6846233\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 1526497,\n" +
            "        \"d\": 751849545\n" +
            "      },\n" +
            "      \"price\": \"0.0020303\",\n" +
            "      \"amount\": \"3.5964310\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 3935093,\n" +
            "        \"d\": 1962721319\n" +
            "      },\n" +
            "      \"price\": \"0.0020049\",\n" +
            "      \"amount\": \"8.9918771\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 1314209,\n" +
            "        \"d\": 815844646\n" +
            "      },\n" +
            "      \"price\": \"0.0016109\",\n" +
            "      \"amount\": \"11.9847854\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"asks\": [\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 2017413,\n" +
            "        \"d\": 803984111\n" +
            "      },\n" +
            "      \"price\": \"0.0025093\",\n" +
            "      \"amount\": \"541.4550766\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 25093,\n" +
            "        \"d\": 10000000\n" +
            "      },\n" +
            "      \"price\": \"0.0025093\",\n" +
            "      \"amount\": \"121.9999600\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 2687972,\n" +
            "        \"d\": 1067615183\n" +
            "      },\n" +
            "      \"price\": \"0.0025177\",\n" +
            "      \"amount\": \"6286.5014925\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 845303,\n" +
            "        \"d\": 332925720\n" +
            "      },\n" +
            "      \"price\": \"0.0025390\",\n" +
            "      \"amount\": \"1203.8364195\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 5147713,\n" +
            "        \"d\": 2017340695\n" +
            "      },\n" +
            "      \"price\": \"0.0025517\",\n" +
            "      \"amount\": \"668.0464888\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 13,\n" +
            "        \"d\": 5000\n" +
            "      },\n" +
            "      \"price\": \"0.0026000\",\n" +
            "      \"amount\": \"2439.9999300\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 2372938,\n" +
            "        \"d\": 877879233\n" +
            "      },\n" +
            "      \"price\": \"0.0027030\",\n" +
            "      \"amount\": \"4953.5042925\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 5177131,\n" +
            "        \"d\": 1895808254\n" +
            "      },\n" +
            "      \"price\": \"0.0027308\",\n" +
            "      \"amount\": \"3691.8772552\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 2219932,\n" +
            "        \"d\": 812231813\n" +
            "      },\n" +
            "      \"price\": \"0.0027331\",\n" +
            "      \"amount\": \"1948.1788496\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 4285123,\n" +
            "        \"d\": 1556796383\n" +
            "      },\n" +
            "      \"price\": \"0.0027525\",\n" +
            "      \"amount\": \"5274.3733332\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 3945179,\n" +
            "        \"d\": 1402780548\n" +
            "      },\n" +
            "      \"price\": \"0.0028124\",\n" +
            "      \"amount\": \"1361.9590574\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 4683683,\n" +
            "        \"d\": 1664729678\n" +
            "      },\n" +
            "      \"price\": \"0.0028135\",\n" +
            "      \"amount\": \"5076.0909147\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 1489326,\n" +
            "        \"d\": 524639179\n" +
            "      },\n" +
            "      \"price\": \"0.0028388\",\n" +
            "      \"amount\": \"2303.2370107\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 3365104,\n" +
            "        \"d\": 1176168157\n" +
            "      },\n" +
            "      \"price\": \"0.0028611\",\n" +
            "      \"amount\": \"8080.5751770\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 2580607,\n" +
            "        \"d\": 899476885\n" +
            "      },\n" +
            "      \"price\": \"0.0028690\",\n" +
            "      \"amount\": \"3733.5054174\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 5213871,\n" +
            "        \"d\": 1788590825\n" +
            "      },\n" +
            "      \"price\": \"0.0029151\",\n" +
            "      \"amount\": \"485.7370041\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 4234565,\n" +
            "        \"d\": 1447134374\n" +
            "      },\n" +
            "      \"price\": \"0.0029262\",\n" +
            "      \"amount\": \"7936.6430110\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 674413,\n" +
            "        \"d\": 230022877\n" +
            "      },\n" +
            "      \"price\": \"0.0029319\",\n" +
            "      \"amount\": \"101.5325328\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 1554515,\n" +
            "        \"d\": 514487004\n" +
            "      },\n" +
            "      \"price\": \"0.0030215\",\n" +
            "      \"amount\": \"5407.8562112\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"price_r\": {\n" +
            "        \"n\": 5638983,\n" +
            "        \"d\": 1850050675\n" +
            "      },\n" +
            "      \"price\": \"0.0030480\",\n" +
            "      \"amount\": \"3024.9341116\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"base\": {\n" +
            "    \"asset_type\": \"native\"\n" +
            "  },\n" +
            "  \"counter\": {\n" +
            "    \"asset_type\": \"credit_alphanum4\",\n" +
            "    \"asset_code\": \"DEMO\",\n" +
            "    \"asset_issuer\": \"GBAMBOOZDWZPVV52RCLJQYMQNXOBLOXWNQAY2IF2FREV2WL46DBCH3BE\"\n" +
            "  }\n" +
            "}";
}
