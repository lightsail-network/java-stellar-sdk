package org.stellar.sdk.requests;

import org.junit.Test;
import org.stellar.base.Asset;
import org.stellar.base.KeyPair;
import org.stellar.sdk.Server;

import java.net.URI;

import static org.junit.Assert.assertEquals;

public class PathsRequestBuilderTest {
  @Test
  public void testAccounts() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    URI uri = server.paths()
            .destinationAccount(KeyPair.fromAddress("GB24QI3BJNKBY4YNJZ2I37HFIYK56BL2OURFML76X46RQQKDLVT7WKJF"))
            .sourceAccount(KeyPair.fromAddress("GD4KO3IOYYWIYVI236Y35K2DU6VNYRH3BPNFJSH57J5BLLCQHBIOK3IN"))
            .destinationAmount("20.50")
            .destinationAsset(Asset.createNonNativeAsset("USD", KeyPair.fromAddress("GAYSHLG75RPSMXWJ5KX7O7STE6RSZTD6NE4CTWAXFZYYVYIFRUVJIBJH")))
            .cursor("13537736921089")
            .limit(200)
            .order(RequestBuilder.Order.ASC)
            .buildUri();

    assertEquals("https://horizon-testnet.stellar.org/paths?" +
            "destination_account=GB24QI3BJNKBY4YNJZ2I37HFIYK56BL2OURFML76X46RQQKDLVT7WKJF&" +
            "source_account=GD4KO3IOYYWIYVI236Y35K2DU6VNYRH3BPNFJSH57J5BLLCQHBIOK3IN&" +
            "destination_amount=20.50&" +
            "destination_asset_type=credit_alphanum4&" +
            "destination_asset_code=USD&" +
            "destination_asset_issuer=GAYSHLG75RPSMXWJ5KX7O7STE6RSZTD6NE4CTWAXFZYYVYIFRUVJIBJH&" +
            "cursor=13537736921089&" +
            "limit=200&" +
            "order=asc", uri.toString());
  }
}
