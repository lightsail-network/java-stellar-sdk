package org.stellar.sdk.responses;

import com.google.gson.reflect.TypeToken;

import okhttp3.OkHttpClient;
import org.junit.Assert;
import org.junit.Test;
import org.stellar.sdk.Asset;
import org.stellar.sdk.KeyPair;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PathsPageDeserializerTest {

  @Test
  public void testDeserialize() throws IOException, URISyntaxException {
    Page<PathResponse> pathsPage = GsonSingleton.getInstance().fromJson(json, new TypeToken<Page<PathResponse>>() {}.getType());

    // We're checking if next page is null so we can pass any OkHttpClient object - it won't be used anyway.
    assertNull(pathsPage.getNextPage(new OkHttpClient()));

    assertEquals(pathsPage.getRecords().get(0).getDestinationAmount(), "20.0000000");
    Assert.assertEquals(pathsPage.getRecords().get(0).getDestinationAsset(), Asset.createNonNativeAsset("EUR", KeyPair.fromAccountId("GDSBCQO34HWPGUGQSP3QBFEXVTSR2PW46UIGTHVWGWJGQKH3AFNHXHXN")));
    assertEquals(pathsPage.getRecords().get(0).getPath().size(), 0);
    assertEquals(pathsPage.getRecords().get(0).getSourceAmount(), "30.0000000");
    assertEquals(pathsPage.getRecords().get(0).getSourceAsset(), Asset.createNonNativeAsset("USD", KeyPair.fromAccountId("GDSBCQO34HWPGUGQSP3QBFEXVTSR2PW46UIGTHVWGWJGQKH3AFNHXHXN")));

    assertEquals(pathsPage.getRecords().get(1).getDestinationAmount(), "50.0000000");
    assertEquals(pathsPage.getRecords().get(1).getDestinationAsset(), Asset.createNonNativeAsset("EUR", KeyPair.fromAccountId("GBFMFKDUFYYITWRQXL4775CVUV3A3WGGXNJUAP4KTXNEQ2HG7JRBITGH")));
    assertEquals(pathsPage.getRecords().get(1).getPath().size(), 1);
    assertEquals(pathsPage.getRecords().get(1).getPath().get(0), Asset.createNonNativeAsset("GBP", KeyPair.fromAccountId("GDSBCQO34HWPGUGQSP3QBFEXVTSR2PW46UIGTHVWGWJGQKH3AFNHXHXN")));
    assertEquals(pathsPage.getRecords().get(1).getSourceAmount(), "60.0000000");
    assertEquals(pathsPage.getRecords().get(1).getSourceAsset(), Asset.createNonNativeAsset("USD", KeyPair.fromAccountId("GBRAOXQDNQZRDIOK64HZI4YRDTBFWNUYH3OIHQLY4VEK5AIGMQHCLGXI")));

    assertEquals(pathsPage.getRecords().get(2).getDestinationAmount(), "200.0000000");
    assertEquals(pathsPage.getRecords().get(2).getDestinationAsset(), Asset.createNonNativeAsset("EUR", KeyPair.fromAccountId("GBRCOBK7C7UE72PB5JCPQU3ZI45ZCEM7HKQ3KYV3YD3XB7EBOPBEDN2G")));
    assertEquals(pathsPage.getRecords().get(2).getPath().size(), 2);
    assertEquals(pathsPage.getRecords().get(2).getPath().get(0), Asset.createNonNativeAsset("GBP", KeyPair.fromAccountId("GAX7B3ZT3EOZW5POAMV4NGPPKCYUOYW2QQDIAF23JAXF72NMGRYPYOPM")));
    assertEquals(pathsPage.getRecords().get(2).getPath().get(1), Asset.createNonNativeAsset("PLN", KeyPair.fromAccountId("GACWIA2XGDFWWN3WKPX63JTK4S2J5NDPNOIVYMZY6RVTS7LWF2VHZLV3")));
    assertEquals(pathsPage.getRecords().get(2).getSourceAmount(), "300.0000000");
    assertEquals(pathsPage.getRecords().get(2).getSourceAsset(), Asset.createNonNativeAsset("USD", KeyPair.fromAccountId("GC7J5IHS3GABSX7AZLRINXWLHFTL3WWXLU4QX2UGSDEAIAQW2Q72U3KH")));
  }

  String json = "{\n" +
          "    \"_embedded\": {\n" +
          "        \"records\": [\n" +
          "            {\n" +
          "                \"destination_amount\": \"20.0000000\",\n" +
          "                \"destination_asset_code\": \"EUR\",\n" +
          "                \"destination_asset_issuer\": \"GDSBCQO34HWPGUGQSP3QBFEXVTSR2PW46UIGTHVWGWJGQKH3AFNHXHXN\",\n" +
          "                \"destination_asset_type\": \"credit_alphanum4\",\n" +
          "                \"path\": [],\n" +
          "                \"source_amount\": \"30.0000000\",\n" +
          "                \"source_asset_code\": \"USD\",\n" +
          "                \"source_asset_issuer\": \"GDSBCQO34HWPGUGQSP3QBFEXVTSR2PW46UIGTHVWGWJGQKH3AFNHXHXN\",\n" +
          "                \"source_asset_type\": \"credit_alphanum4\"\n" +
          "            },\n" +
          "            {\n" +
          "                \"destination_amount\": \"50.0000000\",\n" +
          "                \"destination_asset_code\": \"EUR\",\n" +
          "                \"destination_asset_issuer\": \"GBFMFKDUFYYITWRQXL4775CVUV3A3WGGXNJUAP4KTXNEQ2HG7JRBITGH\",\n" +
          "                \"destination_asset_type\": \"credit_alphanum4\",\n" +
          "                \"path\": [\n" +
          "                    {\n" +
          "                        \"asset_code\": \"GBP\",\n" +
          "                        \"asset_issuer\": \"GDSBCQO34HWPGUGQSP3QBFEXVTSR2PW46UIGTHVWGWJGQKH3AFNHXHXN\",\n" +
          "                        \"asset_type\": \"credit_alphanum4\"\n" +
          "                    }\n" +
          "                ],\n" +
          "                \"source_amount\": \"60.0000000\",\n" +
          "                \"source_asset_code\": \"USD\",\n" +
          "                \"source_asset_issuer\": \"GBRAOXQDNQZRDIOK64HZI4YRDTBFWNUYH3OIHQLY4VEK5AIGMQHCLGXI\",\n" +
          "                \"source_asset_type\": \"credit_alphanum4\"\n" +
          "            },\n" +
          "            {\n" +
          "                \"destination_amount\": \"200.0000000\",\n" +
          "                \"destination_asset_code\": \"EUR\",\n" +
          "                \"destination_asset_issuer\": \"GBRCOBK7C7UE72PB5JCPQU3ZI45ZCEM7HKQ3KYV3YD3XB7EBOPBEDN2G\",\n" +
          "                \"destination_asset_type\": \"credit_alphanum4\",\n" +
          "                \"path\": [\n" +
          "                    {\n" +
          "                        \"asset_code\": \"GBP\",\n" +
          "                        \"asset_issuer\": \"GAX7B3ZT3EOZW5POAMV4NGPPKCYUOYW2QQDIAF23JAXF72NMGRYPYOPM\",\n" +
          "                        \"asset_type\": \"credit_alphanum4\"\n" +
          "                    },\n" +
          "                    {\n" +
          "                        \"asset_code\": \"PLN\",\n" +
          "                        \"asset_issuer\": \"GACWIA2XGDFWWN3WKPX63JTK4S2J5NDPNOIVYMZY6RVTS7LWF2VHZLV3\",\n" +
          "                        \"asset_type\": \"credit_alphanum4\"\n" +
          "                    }\n" +
          "                ],\n" +
          "                \"source_amount\": \"300.0000000\",\n" +
          "                \"source_asset_code\": \"USD\",\n" +
          "                \"source_asset_issuer\": \"GC7J5IHS3GABSX7AZLRINXWLHFTL3WWXLU4QX2UGSDEAIAQW2Q72U3KH\",\n" +
          "                \"source_asset_type\": \"credit_alphanum4\"\n" +
          "            }        ]\n" +
          "    },\n" +
          "    \"_links\": {\n" +
          "        \"self\": {\n" +
          "            \"href\": \"/paths\"\n" +
          "        }\n" +
          "    }\n" +
          "}";
}
