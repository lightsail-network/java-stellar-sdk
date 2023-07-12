package org.stellar.sdk.responses;

import static org.stellar.sdk.Asset.create;

import com.google.gson.reflect.TypeToken;
import junit.framework.TestCase;
import org.junit.Test;

public class OfferPageDeserializerTest extends TestCase {
  @Test
  public void testDeserialize() {
    Page<OfferResponse> offerPage =
        GsonSingleton.getInstance()
            .fromJson(json, new TypeToken<Page<OfferResponse>>() {}.getType());

    assertEquals(offerPage.getRecords().get(0).getId(), new Long(241));
    assertEquals(
        offerPage.getRecords().get(0).getSeller(),
        "GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD");
    assertEquals(offerPage.getRecords().get(0).getPagingToken(), "241");
    assertEquals(
        offerPage.getRecords().get(0).getSelling(),
        create(null, "INR", "GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD"));
    assertEquals(
        offerPage.getRecords().get(0).getBuying(),
        create(null, "USD", "GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD"));
    assertEquals(offerPage.getRecords().get(0).getAmount(), "10.0000000");
    assertEquals(offerPage.getRecords().get(0).getPrice(), "11.0000000");
    assertEquals(offerPage.getRecords().get(0).getLastModifiedLedger(), new Integer(22200794));
    assertEquals(offerPage.getRecords().get(0).getLastModifiedTime(), "2019-01-28T12:30:38Z");
    assertFalse(offerPage.getRecords().get(0).getSponsor().isPresent());

    assertEquals(
        offerPage.getLinks().getNext().getHref(),
        "https://horizon-testnet.stellar.org/accounts/GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD/offers?order=asc&limit=10&cursor=241");
    assertEquals(
        offerPage.getLinks().getPrev().getHref(),
        "https://horizon-testnet.stellar.org/accounts/GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD/offers?order=desc&limit=10&cursor=241");
    assertEquals(
        offerPage.getLinks().getSelf().getHref(),
        "https://horizon-testnet.stellar.org/accounts/GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD/offers?order=asc&limit=10&cursor=");
  }

  @Test
  public void testDeserializeWithSponsor() {
    Page<OfferResponse> offerPage =
        GsonSingleton.getInstance()
            .fromJson(withSponsor, new TypeToken<Page<OfferResponse>>() {}.getType());

    assertEquals(offerPage.getRecords().get(0).getId(), new Long(241));
    assertEquals(
        offerPage.getRecords().get(0).getSeller(),
        "GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD");
    assertEquals(offerPage.getRecords().get(0).getPagingToken(), "241");
    assertEquals(
        offerPage.getRecords().get(0).getSelling(),
        create(null, "INR", "GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD"));
    assertEquals(
        offerPage.getRecords().get(0).getBuying(),
        create(null, "USD", "GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD"));
    assertEquals(offerPage.getRecords().get(0).getAmount(), "10.0000000");
    assertEquals(offerPage.getRecords().get(0).getPrice(), "11.0000000");
    assertEquals(offerPage.getRecords().get(0).getLastModifiedLedger(), new Integer(22200794));
    assertEquals(offerPage.getRecords().get(0).getLastModifiedTime(), "2019-01-28T12:30:38Z");
    assertEquals(
        offerPage.getRecords().get(0).getSponsor().get(),
        "GCA7RXNKN7FGBLJVETJCUUXGXTCR6L2SJQFXDGMQCDET5YUE6KFNHQHO");

    assertEquals(
        offerPage.getLinks().getNext().getHref(),
        "https://horizon-testnet.stellar.org/accounts/GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD/offers?order=asc&limit=10&cursor=241");
    assertEquals(
        offerPage.getLinks().getPrev().getHref(),
        "https://horizon-testnet.stellar.org/accounts/GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD/offers?order=desc&limit=10&cursor=241");
    assertEquals(
        offerPage.getLinks().getSelf().getHref(),
        "https://horizon-testnet.stellar.org/accounts/GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD/offers?order=asc&limit=10&cursor=");
  }

  String json =
      "{\n"
          + "  \"_links\": {\n"
          + "    \"self\": {\n"
          + "      \"href\": \"https://horizon-testnet.stellar.org/accounts/GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD/offers?order=asc\\u0026limit=10\\u0026cursor=\"\n"
          + "    },\n"
          + "    \"next\": {\n"
          + "      \"href\": \"https://horizon-testnet.stellar.org/accounts/GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD/offers?order=asc\\u0026limit=10\\u0026cursor=241\"\n"
          + "    },\n"
          + "    \"prev\": {\n"
          + "      \"href\": \"https://horizon-testnet.stellar.org/accounts/GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD/offers?order=desc\\u0026limit=10\\u0026cursor=241\"\n"
          + "    }\n"
          + "  },\n"
          + "  \"_embedded\": {\n"
          + "    \"records\": [\n"
          + "      {\n"
          + "        \"_links\": {\n"
          + "          \"self\": {\n"
          + "            \"href\": \"https://horizon-testnet.stellar.org/offers/241\"\n"
          + "          },\n"
          + "          \"offer_maker\": {\n"
          + "            \"href\": \"https://horizon-testnet.stellar.org/accounts/GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD\"\n"
          + "          }\n"
          + "        },\n"
          + "        \"id\": 241,\n"
          + "        \"paging_token\": \"241\",\n"
          + "        \"seller\": \"GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD\",\n"
          + "        \"selling\": {\n"
          + "          \"asset_type\": \"credit_alphanum4\",\n"
          + "          \"asset_code\": \"INR\",\n"
          + "          \"asset_issuer\": \"GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD\"\n"
          + "        },\n"
          + "        \"buying\": {\n"
          + "          \"asset_type\": \"credit_alphanum4\",\n"
          + "          \"asset_code\": \"USD\",\n"
          + "          \"asset_issuer\": \"GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD\"\n"
          + "        },\n"
          + "        \"amount\": \"10.0000000\",\n"
          + "        \"price_r\": {\n"
          + "          \"n\": 10,\n"
          + "          \"d\": 1\n"
          + "        },\n"
          + "        \"price\": \"11.0000000\"\n,"
          + "        \"last_modified_ledger\": 22200794,\n"
          + "        \"last_modified_time\": \"2019-01-28T12:30:38Z\"\n"
          + "      }\n"
          + "    ]\n"
          + "  }\n"
          + "}";

  String withSponsor =
      "{\n"
          + "  \"_links\": {\n"
          + "    \"self\": {\n"
          + "      \"href\": \"https://horizon-testnet.stellar.org/accounts/GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD/offers?order=asc\\u0026limit=10\\u0026cursor=\"\n"
          + "    },\n"
          + "    \"next\": {\n"
          + "      \"href\": \"https://horizon-testnet.stellar.org/accounts/GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD/offers?order=asc\\u0026limit=10\\u0026cursor=241\"\n"
          + "    },\n"
          + "    \"prev\": {\n"
          + "      \"href\": \"https://horizon-testnet.stellar.org/accounts/GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD/offers?order=desc\\u0026limit=10\\u0026cursor=241\"\n"
          + "    }\n"
          + "  },\n"
          + "  \"_embedded\": {\n"
          + "    \"records\": [\n"
          + "      {\n"
          + "        \"_links\": {\n"
          + "          \"self\": {\n"
          + "            \"href\": \"https://horizon-testnet.stellar.org/offers/241\"\n"
          + "          },\n"
          + "          \"offer_maker\": {\n"
          + "            \"href\": \"https://horizon-testnet.stellar.org/accounts/GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD\"\n"
          + "          }\n"
          + "        },\n"
          + "        \"id\": 241,\n"
          + "        \"paging_token\": \"241\",\n"
          + "        \"seller\": \"GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD\",\n"
          + "        \"selling\": {\n"
          + "          \"asset_type\": \"credit_alphanum4\",\n"
          + "          \"asset_code\": \"INR\",\n"
          + "          \"asset_issuer\": \"GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD\"\n"
          + "        },\n"
          + "        \"buying\": {\n"
          + "          \"asset_type\": \"credit_alphanum4\",\n"
          + "          \"asset_code\": \"USD\",\n"
          + "          \"asset_issuer\": \"GA2IYMIZSAMDD6QQTTSIEL73H2BKDJQTA7ENDEEAHJ3LMVF7OYIZPXQD\"\n"
          + "        },\n"
          + "        \"amount\": \"10.0000000\",\n"
          + "        \"price_r\": {\n"
          + "          \"n\": 10,\n"
          + "          \"d\": 1\n"
          + "        },\n"
          + "        \"price\": \"11.0000000\"\n,"
          + "        \"last_modified_ledger\": 22200794,\n"
          + "        \"sponsor\": \"GCA7RXNKN7FGBLJVETJCUUXGXTCR6L2SJQFXDGMQCDET5YUE6KFNHQHO\",\n"
          + "        \"last_modified_time\": \"2019-01-28T12:30:38Z\"\n"
          + "      }\n"
          + "    ]\n"
          + "  }\n"
          + "}";
}
