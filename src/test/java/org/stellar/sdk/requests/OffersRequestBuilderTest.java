package org.stellar.sdk.requests;

import okhttp3.HttpUrl;
import org.junit.Test;
import org.stellar.sdk.*;

import static org.junit.Assert.assertEquals;

public class OffersRequestBuilderTest {
  @Test
  public void testForAccount() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    HttpUrl uri = server.offers()
            .forAccount("GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H")
            .limit(200)
            .order(RequestBuilder.Order.DESC)
            .buildUri();
    assertEquals("https://horizon-testnet.stellar.org/accounts/GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H/offers?limit=200&order=desc", uri.toString());
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
    HttpUrl uri = server.offers().forSeller("GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H").buildUri();
    assertEquals("https://horizon-testnet.stellar.org/offers?seller=GBRPYHIL2CI3FNQ4BXLFMNDLFJUNPU2HY3ZMFSHONUCEOASW7QC7OX2H", uri.toString());
  }

  @Test
  public void testForSellingAsset() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    Asset selling = new AssetTypeCreditAlphaNum4("USD", "GDVDKQFP665JAO7A2LSHNLQIUNYNAAIGJ6FYJVMG4DT3YJQQJSRBLQDG");
    HttpUrl uri = server.offers().forSellingAsset(selling).buildUri();
    assertEquals("https://horizon-testnet.stellar.org/offers?selling_asset_type=credit_alphanum4&selling_asset_code=USD&selling_asset_issuer=GDVDKQFP665JAO7A2LSHNLQIUNYNAAIGJ6FYJVMG4DT3YJQQJSRBLQDG", uri.toString());
  }

  @Test
  public void testForBuyingAsset() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    Asset buying = new AssetTypeCreditAlphaNum4("XCN", "GCNY5OXYSY4FKHOPT2SPOQZAOEIGXB5LBYW3HVU3OWSTQITS65M5RCNY");
    HttpUrl uri = server.offers().forBuyingAsset(buying).buildUri();
    assertEquals("https://horizon-testnet.stellar.org/offers?buying_asset_type=credit_alphanum4&buying_asset_code=XCN&buying_asset_issuer=GCNY5OXYSY4FKHOPT2SPOQZAOEIGXB5LBYW3HVU3OWSTQITS65M5RCNY", uri.toString());
  }
}
