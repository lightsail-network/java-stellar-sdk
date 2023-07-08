package org.stellar.sdk.requests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Test;
import org.stellar.sdk.Asset;
import org.stellar.sdk.Server;
import org.stellar.sdk.responses.ClaimableBalanceResponse;

public class ClaimableBalancesRequestBuilderTest {

  @Test
  public void testForSponsor() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    HttpUrl uri =
        server
            .claimableBalances()
            .forSponsor("GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H")
            .limit(200)
            .order(RequestBuilder.Order.DESC)
            .buildUri();
    assertEquals(
        "https://horizon-testnet.stellar.org/claimable_balances?sponsor=GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H&limit=200&order=desc",
        uri.toString());
  }

  @Test
  public void testWithoutParams() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    HttpUrl uri = server.claimableBalances().buildUri();
    assertEquals("https://horizon-testnet.stellar.org/claimable_balances", uri.toString());
  }

  @Test
  public void testForClaimant() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    HttpUrl uri =
        server
            .claimableBalances()
            .forClaimant("GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H")
            .buildUri();
    assertEquals(
        "https://horizon-testnet.stellar.org/claimable_balances?claimant=GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H",
        uri.toString());
  }

  @Test
  public void testForNativeAsset() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    HttpUrl uri = server.claimableBalances().forAsset(Asset.create("native")).buildUri();
    assertEquals(
        "https://horizon-testnet.stellar.org/claimable_balances?asset=native", uri.toString());
  }

  @Test
  public void testForAsset() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    HttpUrl uri =
        server
            .claimableBalances()
            .forAsset(Asset.create("USD:GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H"))
            .buildUri();
    assertEquals(
        "https://horizon-testnet.stellar.org/claimable_balances?asset=USD%3AGBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H",
        uri.toString());
  }

  @Test
  public void testForAssetAndClaimant() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    HttpUrl uri =
        server
            .claimableBalances()
            .forClaimant("GCNY5OXYSY4FKHOPT2SPOQZAOEIGXB5LBYW3HVU3OWSTQITS65M5RCNY")
            .forAsset(Asset.create("USD:GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H"))
            .buildUri();
    assertEquals(
        "https://horizon-testnet.stellar.org/claimable_balances?claimant=GCNY5OXYSY4FKHOPT2SPOQZAOEIGXB5LBYW3HVU3OWSTQITS65M5RCNY&asset=USD%3AGBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H",
        uri.toString());
  }

  @Test
  public void testSingleClaimableBalance() throws IOException, InterruptedException {
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(json));
    String mockUrl =
        String.format("http://%s:%d", mockWebServer.getHostName(), mockWebServer.getPort());
    Server server = new Server(mockUrl);
    String id = "00000000ae76f49e8513d0922b6bcbc8a3f5c4c0a5161871f27924e08724646acab56cd2";
    ClaimableBalanceResponse response = server.claimableBalances().claimableBalance(id);
    RecordedRequest recordedRequest = mockWebServer.takeRequest();
    assertEquals(String.format("/claimable_balances/%s", id), recordedRequest.getPath());
    assertEquals(response.getId(), id);
    assertEquals(response.getAmount(), "1000.0000000");
  }

  String json =
      "{\n"
          + "  \"_links\": {\n"
          + "    \"self\": {\n"
          + "      \"href\": \"https://horizon-protocol14.stellar.org/claimable_balances/00000000ae76f49e8513d0922b6bcbc8a3f5c4c0a5161871f27924e08724646acab56cd2\"\n"
          + "    }\n"
          + "  },\n"
          + "  \"id\": \"00000000ae76f49e8513d0922b6bcbc8a3f5c4c0a5161871f27924e08724646acab56cd2\",\n"
          + "  \"asset\": \"COP:GB56OJGSA6VHEUFZDX6AL2YDVG2TS5JDZYQJHDYHBDH7PCD5NIQKLSDO\",\n"
          + "  \"amount\": \"1000.0000000\",\n"
          + "  \"sponsor\": \"GB56OJGSA6VHEUFZDX6AL2YDVG2TS5JDZYQJHDYHBDH7PCD5NIQKLSDO\",\n"
          + "  \"last_modified_ledger\": 117663,\n"
          + "  \"claimants\": [\n"
          + "    {\n"
          + "      \"destination\": \"GD3W2FFQQ2WTJTBHXYBLMQS664XCT3LYSRKUBWNU7K4KXBZDBLPY3RAU\",\n"
          + "      \"predicate\": {\n"
          + "        \"abs_before\": \"2020-09-29T23:56:04Z\"\n"
          + "      }\n"
          + "    },\n"
          + "    {\n"
          + "      \"destination\": \"GB56OJGSA6VHEUFZDX6AL2YDVG2TS5JDZYQJHDYHBDH7PCD5NIQKLSDO\",\n"
          + "      \"predicate\": {\n"
          + "        \"or\": [\n"
          + "          {\n"
          + "            \"abs_before\": \"2020-09-28T17:57:04Z\"\n"
          + "          },\n"
          + "          {\n"
          + "            \"not\": {\n"
          + "              \"abs_before\": \"2020-09-29T23:56:04Z\"\n"
          + "            }\n"
          + "          }\n"
          + "        ]\n"
          + "      }\n"
          + "    }\n"
          + "  ],\n"
          + "  \"paging_token\": \"117663-00000000ae76f49e8513d0922b6bcbc8a3f5c4c0a5161871f27924e08724646acab56cd2\"\n"
          + "}";
}
