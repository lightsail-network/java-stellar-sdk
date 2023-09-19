package org.stellar.sdk.requests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.stellar.sdk.Asset.create;

import com.google.common.collect.Lists;
import java.util.List;
import okhttp3.HttpUrl;
import org.junit.Test;
import org.stellar.sdk.Asset;
import org.stellar.sdk.Server;

public class PathsRequestBuilderTest {
  @Test
  public void testStrictReceiveWithSourceAccount() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    HttpUrl uri =
        server
            .strictReceivePaths()
            .destinationAccount("GB24QI3BJNKBY4YNJZ2I37HFIYK56BL2OURFML76X46RQQKDLVT7WKJF")
            .sourceAccount("GD4KO3IOYYWIYVI236Y35K2DU6VNYRH3BPNFJSH57J5BLLCQHBIOK3IN")
            .destinationAmount("20.50")
            .destinationAsset(
                create(null, "USD", "GAYSHLG75RPSMXWJ5KX7O7STE6RSZTD6NE4CTWAXFZYYVYIFRUVJIBJH"))
            .cursor("13537736921089")
            .limit(200)
            .order(RequestBuilder.Order.ASC)
            .buildUri();

    assertEquals(
        "https://horizon-testnet.stellar.org/paths/strict-receive?"
            + "destination_account=GB24QI3BJNKBY4YNJZ2I37HFIYK56BL2OURFML76X46RQQKDLVT7WKJF&"
            + "source_account=GD4KO3IOYYWIYVI236Y35K2DU6VNYRH3BPNFJSH57J5BLLCQHBIOK3IN&"
            + "destination_amount=20.50&"
            + "destination_asset_type=credit_alphanum4&"
            + "destination_asset_code=USD&"
            + "destination_asset_issuer=GAYSHLG75RPSMXWJ5KX7O7STE6RSZTD6NE4CTWAXFZYYVYIFRUVJIBJH&"
            + "cursor=13537736921089&"
            + "limit=200&"
            + "order=asc",
        uri.toString());
  }

  @Test
  public void testStrictReceiveWithSourceAssets() {
    List<Asset> assets =
        Lists.newArrayList(
            create("native", "", ""),
            create(
                "credit_alphanum4",
                "USD",
                "GAYSHLG75RPSMXWJ5KX7O7STE6RSZTD6NE4CTWAXFZYYVYIFRUVJIBJH"),
            create(
                "credit_alphanum4",
                "EUR",
                "GAYSHLG75RPSMXWJ5KX7O7STE6RSZTD6NE4CTWAXFZYYVYIFRUVJIBJH"));
    Server server = new Server("https://horizon-testnet.stellar.org");
    HttpUrl uri =
        server
            .strictReceivePaths()
            .destinationAccount("GB24QI3BJNKBY4YNJZ2I37HFIYK56BL2OURFML76X46RQQKDLVT7WKJF")
            .sourceAssets(assets)
            .destinationAmount("20.50")
            .destinationAsset(
                create(null, "USD", "GAYSHLG75RPSMXWJ5KX7O7STE6RSZTD6NE4CTWAXFZYYVYIFRUVJIBJH"))
            .cursor("13537736921089")
            .limit(3)
            .order(RequestBuilder.Order.ASC)
            .buildUri();

    assertEquals(
        "https://horizon-testnet.stellar.org/paths/strict-receive?"
            + "destination_account=GB24QI3BJNKBY4YNJZ2I37HFIYK56BL2OURFML76X46RQQKDLVT7WKJF&"
            + "source_assets=native%2CUSD%3AGAYSHLG75RPSMXWJ5KX7O7STE6RSZTD6NE4CTWAXFZYYVYIFRUVJIBJH%2CEUR%3AGAYSHLG75RPSMXWJ5KX7O7STE6RSZTD6NE4CTWAXFZYYVYIFRUVJIBJH&"
            + "destination_amount=20.50&"
            + "destination_asset_type=credit_alphanum4&"
            + "destination_asset_code=USD&"
            + "destination_asset_issuer=GAYSHLG75RPSMXWJ5KX7O7STE6RSZTD6NE4CTWAXFZYYVYIFRUVJIBJH&"
            + "cursor=13537736921089&"
            + "limit=3&"
            + "order=asc",
        uri.toString());
  }

  @Test
  public void testStrictReceiveWithSourceAccountAndSourceAssets() {
    List<Asset> assets =
        Lists.newArrayList(
            create("native", "", ""),
            create(
                "credit_alphanum4",
                "USD",
                "GAYSHLG75RPSMXWJ5KX7O7STE6RSZTD6NE4CTWAXFZYYVYIFRUVJIBJH"),
            create(
                "credit_alphanum4",
                "EUR",
                "GAYSHLG75RPSMXWJ5KX7O7STE6RSZTD6NE4CTWAXFZYYVYIFRUVJIBJH"));
    Server server = new Server("https://horizon-testnet.stellar.org");

    try {
      server
          .strictReceivePaths()
          .sourceAssets(assets)
          .sourceAccount("GD4KO3IOYYWIYVI236Y35K2DU6VNYRH3BPNFJSH57J5BLLCQHBIOK3IN");
      fail();
    } catch (RuntimeException e) {
      assertTrue(e.getMessage().contains("cannot set both source_assets and source_account"));
    }

    try {
      server
          .strictReceivePaths()
          .sourceAccount("GD4KO3IOYYWIYVI236Y35K2DU6VNYRH3BPNFJSH57J5BLLCQHBIOK3IN")
          .sourceAssets(assets);
      fail();
    } catch (RuntimeException e) {
      assertTrue(e.getMessage().contains("cannot set both source_assets and source_account"));
    }
  }

  @Test
  public void testStrictSendWithDestinationAccount() {
    Server server = new Server("https://horizon-testnet.stellar.org");
    HttpUrl uri =
        server
            .strictSendPaths()
            .destinationAccount("GB24QI3BJNKBY4YNJZ2I37HFIYK56BL2OURFML76X46RQQKDLVT7WKJF")
            .sourceAmount("20.50")
            .sourceAsset(
                create(null, "USD", "GAYSHLG75RPSMXWJ5KX7O7STE6RSZTD6NE4CTWAXFZYYVYIFRUVJIBJH"))
            .cursor("13537736921089")
            .limit(200)
            .order(RequestBuilder.Order.ASC)
            .buildUri();

    assertEquals(
        "https://horizon-testnet.stellar.org/paths/strict-send?"
            + "destination_account=GB24QI3BJNKBY4YNJZ2I37HFIYK56BL2OURFML76X46RQQKDLVT7WKJF&"
            + "source_amount=20.50&"
            + "source_asset_type=credit_alphanum4&"
            + "source_asset_code=USD&"
            + "source_asset_issuer=GAYSHLG75RPSMXWJ5KX7O7STE6RSZTD6NE4CTWAXFZYYVYIFRUVJIBJH&"
            + "cursor=13537736921089&"
            + "limit=200&"
            + "order=asc",
        uri.toString());
  }

  @Test
  public void testStrictSendWithDestinationAssets() {
    List<Asset> assets =
        Lists.newArrayList(
            create("native", "", ""),
            create(
                "credit_alphanum4",
                "USD",
                "GAYSHLG75RPSMXWJ5KX7O7STE6RSZTD6NE4CTWAXFZYYVYIFRUVJIBJH"),
            create(
                "credit_alphanum4",
                "EUR",
                "GAYSHLG75RPSMXWJ5KX7O7STE6RSZTD6NE4CTWAXFZYYVYIFRUVJIBJH"));
    Server server = new Server("https://horizon-testnet.stellar.org");
    HttpUrl uri =
        server
            .strictSendPaths()
            .destinationAssets(assets)
            .sourceAmount("20.50")
            .sourceAsset(
                create(null, "USD", "GAYSHLG75RPSMXWJ5KX7O7STE6RSZTD6NE4CTWAXFZYYVYIFRUVJIBJH"))
            .cursor("13537736921089")
            .limit(3)
            .order(RequestBuilder.Order.ASC)
            .buildUri();

    assertEquals(
        "https://horizon-testnet.stellar.org/paths/strict-send?"
            + "destination_assets=native%2CUSD%3AGAYSHLG75RPSMXWJ5KX7O7STE6RSZTD6NE4CTWAXFZYYVYIFRUVJIBJH%2CEUR%3AGAYSHLG75RPSMXWJ5KX7O7STE6RSZTD6NE4CTWAXFZYYVYIFRUVJIBJH&"
            + "source_amount=20.50&"
            + "source_asset_type=credit_alphanum4&"
            + "source_asset_code=USD&"
            + "source_asset_issuer=GAYSHLG75RPSMXWJ5KX7O7STE6RSZTD6NE4CTWAXFZYYVYIFRUVJIBJH&"
            + "cursor=13537736921089&"
            + "limit=3&"
            + "order=asc",
        uri.toString());
  }

  @Test
  public void testStrictSendWithDestinationAccountAndDestinationAssets() {
    List<Asset> assets =
        Lists.newArrayList(
            create("native", "", ""),
            create(
                "credit_alphanum4",
                "USD",
                "GAYSHLG75RPSMXWJ5KX7O7STE6RSZTD6NE4CTWAXFZYYVYIFRUVJIBJH"),
            create(
                "credit_alphanum4",
                "EUR",
                "GAYSHLG75RPSMXWJ5KX7O7STE6RSZTD6NE4CTWAXFZYYVYIFRUVJIBJH"));
    Server server = new Server("https://horizon-testnet.stellar.org");

    try {
      server
          .strictSendPaths()
          .destinationAssets(assets)
          .destinationAccount("GD4KO3IOYYWIYVI236Y35K2DU6VNYRH3BPNFJSH57J5BLLCQHBIOK3IN");
      fail();
    } catch (RuntimeException e) {
      assertTrue(
          e.getMessage().contains("cannot set both destination_assets and destination_account"));
    }

    try {
      server
          .strictSendPaths()
          .destinationAccount("GD4KO3IOYYWIYVI236Y35K2DU6VNYRH3BPNFJSH57J5BLLCQHBIOK3IN")
          .destinationAssets(assets);
      fail();
    } catch (RuntimeException e) {
      assertTrue(
          e.getMessage().contains("cannot set both destination_assets and destination_account"));
    }
  }
}
