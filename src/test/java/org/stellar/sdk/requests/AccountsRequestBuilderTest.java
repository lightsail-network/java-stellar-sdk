package org.stellar.sdk.requests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import okhttp3.HttpUrl;
import org.junit.Test;
import org.stellar.sdk.*;

public class AccountsRequestBuilderTest {
  @Test
  public void testAccounts() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    HttpUrl uri =
        server
            .accounts()
            .cursor("13537736921089")
            .limit(200)
            .order(RequestBuilder.Order.ASC)
            .buildUri();
    assertEquals(
        "https://horizon-testnet.stellar.org/accounts?cursor=13537736921089&limit=200&order=asc",
        uri.toString());
    server.close();
  }

  @Test
  public void testForSigner() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    HttpUrl uri =
        server
            .accounts()
            .forSigner("GDSBCQO34HWPGUGQSP3QBFEXVTSR2PW46UIGTHVWGWJGQKH3AFNHXHXN")
            .buildUri();
    assertEquals(
        "https://horizon-testnet.stellar.org/accounts?signer=GDSBCQO34HWPGUGQSP3QBFEXVTSR2PW46UIGTHVWGWJGQKH3AFNHXHXN",
        uri.toString());
    server.close();
  }

  @Test
  public void testForSponsor() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    HttpUrl uri =
        server
            .accounts()
            .forSponsor("GDSBCQO34HWPGUGQSP3QBFEXVTSR2PW46UIGTHVWGWJGQKH3AFNHXHXN")
            .buildUri();
    assertEquals(
        "https://horizon-testnet.stellar.org/accounts?sponsor=GDSBCQO34HWPGUGQSP3QBFEXVTSR2PW46UIGTHVWGWJGQKH3AFNHXHXN",
        uri.toString());
    server.close();
  }

  @Test
  public void testForCreditAlphanum4Asset() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    AssetTypeCreditAlphaNum asset =
        new AssetTypeCreditAlphaNum4(
            "USD", "GDVDKQFP665JAO7A2LSHNLQIUNYNAAIGJ6FYJVMG4DT3YJQQJSRBLQDG");
    HttpUrl uri = server.accounts().forAsset(asset).buildUri();
    assertEquals(
        "https://horizon-testnet.stellar.org/accounts?asset=USD%3AGDVDKQFP665JAO7A2LSHNLQIUNYNAAIGJ6FYJVMG4DT3YJQQJSRBLQDG",
        uri.toString());
    server.close();
  }

  @Test
  public void testForCreditAlphanum12Asset() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    AssetTypeCreditAlphaNum asset =
        new AssetTypeCreditAlphaNum12(
            "STELLAR", "GDVDKQFP665JAO7A2LSHNLQIUNYNAAIGJ6FYJVMG4DT3YJQQJSRBLQDG");
    HttpUrl uri = server.accounts().forAsset(asset).buildUri();
    assertEquals(
        "https://horizon-testnet.stellar.org/accounts?asset=STELLAR%3AGDVDKQFP665JAO7A2LSHNLQIUNYNAAIGJ6FYJVMG4DT3YJQQJSRBLQDG",
        uri.toString());
    server.close();
  }

  @Test
  public void testForSignerAndAssetInvalid() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    AssetTypeCreditAlphaNum asset =
        new AssetTypeCreditAlphaNum4(
            "USD", "GDVDKQFP665JAO7A2LSHNLQIUNYNAAIGJ6FYJVMG4DT3YJQQJSRBLQDG");
    try {
      server
          .accounts()
          .forSigner("GDSBCQO34HWPGUGQSP3QBFEXVTSR2PW46UIGTHVWGWJGQKH3AFNHXHXN")
          .forAsset(asset)
          .buildUri();
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("cannot set both signer and asset", e.getMessage());
    }
    server.close();
  }

  @Test
  public void testForAssetAndSponsorInvalid() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    AssetTypeCreditAlphaNum asset =
        new AssetTypeCreditAlphaNum4(
            "USD", "GDVDKQFP665JAO7A2LSHNLQIUNYNAAIGJ6FYJVMG4DT3YJQQJSRBLQDG");
    try {
      server
          .accounts()
          .forSponsor("GDSBCQO34HWPGUGQSP3QBFEXVTSR2PW46UIGTHVWGWJGQKH3AFNHXHXN")
          .forAsset(asset)
          .buildUri();
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("cannot set both sponsor and asset", e.getMessage());
    }
    server.close();
  }

  @Test
  public void testForSignerAndSponsorInvalid() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    try {
      server
          .accounts()
          .forSigner("GDSBCQO34HWPGUGQSP3QBFEXVTSR2PW46UIGTHVWGWJGQKH3AFNHXHXN")
          .forSponsor("GDVDKQFP665JAO7A2LSHNLQIUNYNAAIGJ6FYJVMG4DT3YJQQJSRBLQDG")
          .buildUri();
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("cannot set both signer and sponsor", e.getMessage());
    }
    server.close();
  }

  @Test
  public void testForLiquidityPool() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    String liquidityPoolId = "dd7b1ab831c273310ddbec6f97870aa83c2fbd78ce22aded37ecbf4f3380faca";
    HttpUrl uri = server.accounts().forLiquidityPool(liquidityPoolId).buildUri();
    assertEquals(
        "https://horizon-testnet.stellar.org/accounts?liquidity_pool=dd7b1ab831c273310ddbec6f97870aa83c2fbd78ce22aded37ecbf4f3380faca",
        uri.toString());
    server.close();
  }
}
