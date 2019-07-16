package org.stellar.sdk.responses;

import com.google.gson.reflect.TypeToken;

import junit.framework.TestCase;

import org.junit.Test;
import org.stellar.sdk.Asset;
import org.stellar.sdk.KeyPair;

public class OfferPageDeserializerTest extends TestCase {
  @Test
  public void testDeserialize() {
    Page<OfferResponse> transactionsPage = GsonSingleton.getInstance().fromJson(json, new TypeToken<Page<OfferResponse>>() {}.getType());

    assertEquals(transactionsPage.getRecords().get(0).getId(), new Long(241));
    assertEquals(transactionsPage.getRecords().get(0).getSeller(), "GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD");
    assertEquals(transactionsPage.getRecords().get(0).getPagingToken(), "241");
    assertEquals(transactionsPage.getRecords().get(0).getSelling(), Asset.createNonNativeAsset("INR", "GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD"));
    assertEquals(transactionsPage.getRecords().get(0).getBuying(), Asset.createNonNativeAsset("USD", "GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD"));
    assertEquals(transactionsPage.getRecords().get(0).getAmount(), "10.0000000");
    assertEquals(transactionsPage.getRecords().get(0).getPrice(), "11.0000000");
    assertEquals(transactionsPage.getRecords().get(0).getLastModifiedLedger(), new Integer(22200794));
    assertEquals(transactionsPage.getRecords().get(0).getLastModifiedTime(), "2019-01-28T12:30:38Z");

    assertEquals(transactionsPage.getLinks().getNext().getHref(), "https://horizon-testnet.stellar.org/accounts/GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD/offers?order=asc&limit=10&cursor=241");
    assertEquals(transactionsPage.getLinks().getPrev().getHref(), "https://horizon-testnet.stellar.org/accounts/GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD/offers?order=desc&limit=10&cursor=241");
    assertEquals(transactionsPage.getLinks().getSelf().getHref(), "https://horizon-testnet.stellar.org/accounts/GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD/offers?order=asc&limit=10&cursor=");
  }

  String json = "{\n" +
          "  \"_links\": {\n" +
          "    \"self\": {\n" +
          "      \"href\": \"https://horizon-testnet.stellar.org/accounts/GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD/offers?order=asc\\u0026limit=10\\u0026cursor=\"\n" +
          "    },\n" +
          "    \"next\": {\n" +
          "      \"href\": \"https://horizon-testnet.stellar.org/accounts/GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD/offers?order=asc\\u0026limit=10\\u0026cursor=241\"\n" +
          "    },\n" +
          "    \"prev\": {\n" +
          "      \"href\": \"https://horizon-testnet.stellar.org/accounts/GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD/offers?order=desc\\u0026limit=10\\u0026cursor=241\"\n" +
          "    }\n" +
          "  },\n" +
          "  \"_embedded\": {\n" +
          "    \"records\": [\n" +
          "      {\n" +
          "        \"_links\": {\n" +
          "          \"self\": {\n" +
          "            \"href\": \"https://horizon-testnet.stellar.org/offers/241\"\n" +
          "          },\n" +
          "          \"offer_maker\": {\n" +
          "            \"href\": \"https://horizon-testnet.stellar.org/accounts/GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD\"\n" +
          "          }\n" +
          "        },\n" +
          "        \"id\": 241,\n" +
          "        \"paging_token\": \"241\",\n" +
          "        \"seller\": \"GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD\",\n" +
          "        \"selling\": {\n" +
          "          \"asset_type\": \"credit_alphanum4\",\n" +
          "          \"asset_code\": \"INR\",\n" +
          "          \"asset_issuer\": \"GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD\"\n" +
          "        },\n" +
          "        \"buying\": {\n" +
          "          \"asset_type\": \"credit_alphanum4\",\n" +
          "          \"asset_code\": \"USD\",\n" +
          "          \"asset_issuer\": \"GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD\"\n" +
          "        },\n" +
          "        \"amount\": \"10.0000000\",\n" +
          "        \"price_r\": {\n" +
          "          \"n\": 10,\n" +
          "          \"d\": 1\n" +
          "        },\n" +
          "        \"price\": \"11.0000000\"\n," +
          "        \"last_modified_ledger\": 22200794,\n" +
          "        \"last_modified_time\": \"2019-01-28T12:30:38Z\"\n"+
          "      }\n" +
          "    ]\n" +
          "  }\n" +
          "}";
}
