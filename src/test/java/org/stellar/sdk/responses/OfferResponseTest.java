package org.stellar.sdk.responses;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import org.stellar.sdk.AssetTypeCreditAlphaNum12;
import org.stellar.sdk.responses.gson.GsonSingleton;
import org.stellar.sdk.xdr.AssetType;

public class OfferResponseTest {
  @Test
  public void testDeserialize() throws IOException {
    String filePath = "src/test/resources/responses/offer.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    OfferResponse offerResponse = GsonSingleton.getInstance().fromJson(json, OfferResponse.class);

    assertEquals(1577055064, offerResponse.getId().longValue());
    assertEquals("1577055064", offerResponse.getPagingToken());
    assertEquals(
        "GAXXBGVWDE5BA74QCLEMOXXNXJ7XYPHWAE5FUOMCMD3D75LXEDRZTZVU", offerResponse.getSeller());

    assertEquals(AssetType.ASSET_TYPE_CREDIT_ALPHANUM12, offerResponse.getSelling().getType());
    assertEquals("CANNACOIN", ((AssetTypeCreditAlphaNum12) offerResponse.getSelling()).getCode());
    assertEquals(
        "GBLJ4223KUWIMV7RAPQKBA7YGR4I7H2BIV4KIMMXMQWYQBOZ6HLZR3RQ",
        ((AssetTypeCreditAlphaNum12) offerResponse.getSelling()).getIssuer());

    assertEquals(AssetType.ASSET_TYPE_NATIVE, offerResponse.getBuying().getType());

    assertEquals("30.3261978", offerResponse.getAmount());
    assertEquals(572849L, offerResponse.getPriceR().getNumerator());
    assertEquals(878046827L, offerResponse.getPriceR().getDenominator());
    assertEquals("0.0006524", offerResponse.getPrice());
    assertEquals(52425928L, offerResponse.getLastModifiedLedger().longValue());
    assertEquals("2024-07-05T00:50:03Z", offerResponse.getLastModifiedTime());
  }
}
