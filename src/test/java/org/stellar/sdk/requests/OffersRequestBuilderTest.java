package org.stellar.sdk.requests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Test;
import org.stellar.sdk.*;

public class OffersRequestBuilderTest {
  private static final String offerResponse =
      "{\n"
          + "\"_links\": {\n"
          + "\"self\": {\n"
          + "\"href\": \"https://horizon.stellar.org/offers/12345\"\n"
          + "},\n"
          + "\"offer_maker\": {\n"
          + "\"href\": \"https://horizon.stellar.org/accounts/GDCY6FXA2FZU4OZWQEES5DAQFQPYODNT53KVDBHVAZLZTSSMSITZDJ7Q\"\n"
          + "}\n"
          + "},\n"
          + "\"id\": 12345,\n"
          + "\"paging_token\": \"12345\",\n"
          + "\"seller\": \"GDCY6FXA2FZU4OZWQEES5DAQFQPYODNT53KVDBHVAZLZTSSMSITZDJ7Q\",\n"
          + "\"selling\": {\n"
          + "\"asset_type\": \"native\"\n"
          + "},\n"
          + "\"buying\": {\n"
          + "\"asset_type\": \"credit_alphanum4\",\n"
          + "\"asset_code\": \"XCN\",\n"
          + "\"asset_issuer\": \"GCNY5OXYSY4FKHOPT2SPOQZAOEIGXB5LBYW3HVU3OWSTQITS65M5RCNY\"\n"
          + "},\n"
          + "\"amount\": \"2000.0000000\",\n"
          + "\"price_r\": {\n"
          + "\"n\": 5,\n"
          + "\"d\": 1\n"
          + "},\n"
          + "\"price\": \"5.0000000\",\n"
          + "\"last_modified_ledger\": 19967410,\n"
          + "\"last_modified_time\": \"2018-09-13T16:00:06Z\"\n"
          + "}";

  @Test
  public void testForAccount() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    HttpUrl uri =
        server
            .offers()
            .forAccount("GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H")
            .limit(200)
            .order(RequestBuilder.Order.DESC)
            .buildUri();
    assertEquals(
        "https://horizon-testnet.stellar.org/accounts/GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H/offers?limit=200&order=desc",
        uri.toString());
  }

  @Test
  public void testWithoutParams() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    HttpUrl uri = server.offers().buildUri();
    assertEquals("https://horizon-testnet.stellar.org/offers", uri.toString());
  }

  @Test
  public void testForSeller() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    HttpUrl uri =
        server
            .offers()
            .forSeller("GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H")
            .buildUri();
    assertEquals(
        "https://horizon-testnet.stellar.org/offers?seller=GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H",
        uri.toString());
  }

  @Test
  public void testForSponsor() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    HttpUrl uri =
        server
            .offers()
            .forSponsor("GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H")
            .buildUri();
    assertEquals(
        "https://horizon-testnet.stellar.org/offers?sponsor=GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H",
        uri.toString());
  }

  @Test
  public void testForSellingCreditAlphanum4Asset() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    Asset selling =
        new AssetTypeCreditAlphaNum4(
            "USD", "GDVDKQFP665JAO7A2LSHNLQIUNYNAAIGJ6FYJVMG4DT3YJQQJSRBLQDG");
    HttpUrl uri = server.offers().forSellingAsset(selling).buildUri();
    assertEquals(
        "https://horizon-testnet.stellar.org/offers?selling=USD%3AGDVDKQFP665JAO7A2LSHNLQIUNYNAAIGJ6FYJVMG4DT3YJQQJSRBLQDG",
        uri.toString());
  }

  @Test
  public void testForBuyingCreditAlphanum4Asset() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    Asset buying =
        new AssetTypeCreditAlphaNum4(
            "XCN", "GCNY5OXYSY4FKHOPT2SPOQZAOEIGXB5LBYW3HVU3OWSTQITS65M5RCNY");
    HttpUrl uri = server.offers().forBuyingAsset(buying).buildUri();
    assertEquals(
        "https://horizon-testnet.stellar.org/offers?buying=XCN%3AGCNY5OXYSY4FKHOPT2SPOQZAOEIGXB5LBYW3HVU3OWSTQITS65M5RCNY",
        uri.toString());
  }

  @Test
  public void testForSellingCreditAlphanum12Asset() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    Asset selling =
        new AssetTypeCreditAlphaNum12(
            "STELLAR", "GDVDKQFP665JAO7A2LSHNLQIUNYNAAIGJ6FYJVMG4DT3YJQQJSRBLQDG");
    HttpUrl uri = server.offers().forSellingAsset(selling).buildUri();
    assertEquals(
        "https://horizon-testnet.stellar.org/offers?selling=STELLAR%3AGDVDKQFP665JAO7A2LSHNLQIUNYNAAIGJ6FYJVMG4DT3YJQQJSRBLQDG",
        uri.toString());
  }

  @Test
  public void testForBuyingCreditAlphanum12Asset() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    Asset buying =
        new AssetTypeCreditAlphaNum12(
            "STELLAR", "GDVDKQFP665JAO7A2LSHNLQIUNYNAAIGJ6FYJVMG4DT3YJQQJSRBLQDG");
    HttpUrl uri = server.offers().forBuyingAsset(buying).buildUri();
    assertEquals(
        "https://horizon-testnet.stellar.org/offers?buying=STELLAR%3AGDVDKQFP665JAO7A2LSHNLQIUNYNAAIGJ6FYJVMG4DT3YJQQJSRBLQDG",
        uri.toString());
  }

  @Test
  public void testForSellingNativeAsset() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    Asset selling = new AssetTypeNative();
    HttpUrl uri = server.offers().forSellingAsset(selling).buildUri();
    assertEquals("https://horizon-testnet.stellar.org/offers?selling=native", uri.toString());
  }

  @Test
  public void testForBuyingNativeAsset() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    Asset buying = new AssetTypeNative();
    HttpUrl uri = server.offers().forBuyingAsset(buying).buildUri();
    assertEquals("https://horizon-testnet.stellar.org/offers?buying=native", uri.toString());
  }

  @Test
  public void testForSellingAssetAndBuyingAsset() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    Asset selling =
        new AssetTypeCreditAlphaNum4(
            "USD", "GDVDKQFP665JAO7A2LSHNLQIUNYNAAIGJ6FYJVMG4DT3YJQQJSRBLQDG");
    Asset buying =
        new AssetTypeCreditAlphaNum4(
            "XCN", "GCNY5OXYSY4FKHOPT2SPOQZAOEIGXB5LBYW3HVU3OWSTQITS65M5RCNY");
    HttpUrl uri = server.offers().forSellingAsset(selling).forBuyingAsset(buying).buildUri();
    assertEquals(
        "https://horizon-testnet.stellar.org/offers?selling=USD%3AGDVDKQFP665JAO7A2LSHNLQIUNYNAAIGJ6FYJVMG4DT3YJQQJSRBLQDG&buying=XCN%3AGCNY5OXYSY4FKHOPT2SPOQZAOEIGXB5LBYW3HVU3OWSTQITS65M5RCNY",
        uri.toString());
  }

  @Test
  public void testOffer() throws IOException, InterruptedException {
    MockWebServer mockWebServer = new MockWebServer();
    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(offerResponse));
    String mockUrl =
        String.format("http://%s:%d", mockWebServer.getHostName(), mockWebServer.getPort());
    Server server = new Server(mockUrl);
    long offerId = 12345L;
    server.offers().offer(offerId);
    RecordedRequest recordedRequest = mockWebServer.takeRequest();
    assertEquals(String.format("/offers/%s", offerId), recordedRequest.getPath());
  }
}
