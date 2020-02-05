package org.stellar.sdk.requests;

import okhttp3.HttpUrl;
import org.junit.Test;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeCreditAlphaNum4;
import org.stellar.sdk.Server;

import static org.junit.Assert.assertEquals;

public class AccountsRequestBuilderTest {
  @Test
  public void testAccounts() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    HttpUrl uri = server.accounts()
            .cursor("13537736921089")
            .limit(200)
            .order(RequestBuilder.Order.ASC)
            .buildUri();
    assertEquals("https://horizon-testnet.stellar.org/accounts?cursor=13537736921089&limit=200&order=asc", uri.toString());
  }

  @Test
  public void testForSigner() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    HttpUrl uri = server.accounts()
            .forSigner("GDSBCQO34HWPGUGQSP3QBFEXVTSR2PW46UIGTHVWGWJGQKH3AFNHXHXN")
            .buildUri();
    assertEquals("https://horizon-testnet.stellar.org/accounts?signer=GDSBCQO34HWPGUGQSP3QBFEXVTSR2PW46UIGTHVWGWJGQKH3AFNHXHXN", uri.toString());
  }

  @Test
  public void testForAsset() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    Asset asset = new AssetTypeCreditAlphaNum4("USD", "GDVDKQFP665JAO7A2LSHNLQIUNYNAAIGJ6FYJVMG4DT3YJQQJSRBLQDG");
    HttpUrl uri = server.accounts()
            .forAsset(asset)
            .buildUri();
    assertEquals("https://horizon-testnet.stellar.org/accounts?asset=USD%3AGDVDKQFP665JAO7A2LSHNLQIUNYNAAIGJ6FYJVMG4DT3YJQQJSRBLQDG", uri.toString());
  }
}
