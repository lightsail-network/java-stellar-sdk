package org.stellar.sdk.responses;

import junit.framework.TestCase;
import org.junit.Test;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeCreditAlphaNum;
import org.stellar.sdk.xdr.AssetType;

public class AssetDeserializerTest extends TestCase {
  @Test
  public void testDeserializeNative() {
    String json = "{\"asset_type\": \"native\"}";
    Asset asset = GsonSingleton.getInstance().fromJson(json, Asset.class);
    assertEquals(asset.getType(), AssetType.ASSET_TYPE_NATIVE);
  }

  @Test
  public void testDeserializeCredit() {
    String json =
        "{\n"
            + "  \"asset_type\": \"credit_alphanum4\",\n"
            + "  \"asset_code\": \"CNY\",\n"
            + "  \"asset_issuer\": \"GAREELUB43IRHWEASCFBLKHURCGMHE5IF6XSE7EXDLACYHGRHM43RFOX\"\n"
            + "}";
    Asset asset = GsonSingleton.getInstance().fromJson(json, Asset.class);
    assertEquals(asset.getType(), AssetType.ASSET_TYPE_CREDIT_ALPHANUM4);
    AssetTypeCreditAlphaNum creditAsset = (AssetTypeCreditAlphaNum) asset;
    assertEquals(creditAsset.getCode(), "CNY");
    assertEquals(
        creditAsset.getIssuer(), "GAREELUB43IRHWEASCFBLKHURCGMHE5IF6XSE7EXDLACYHGRHM43RFOX");
  }

  @Test
  public void testDeserializeCanonicalStringNative() {
    String json = "\"native\"";
    Asset asset = GsonSingleton.getInstance().fromJson(json, Asset.class);
    assertEquals(asset.getType(), AssetType.ASSET_TYPE_NATIVE);
  }

  @Test
  public void testDeserializeCanonicalStringCredit() {
    String json = "\"CNY:GAREELUB43IRHWEASCFBLKHURCGMHE5IF6XSE7EXDLACYHGRHM43RFOX\"";
    Asset asset = GsonSingleton.getInstance().fromJson(json, Asset.class);
    assertEquals(asset.getType(), AssetType.ASSET_TYPE_CREDIT_ALPHANUM4);
    AssetTypeCreditAlphaNum creditAsset = (AssetTypeCreditAlphaNum) asset;
    assertEquals(creditAsset.getCode(), "CNY");
    assertEquals(
        creditAsset.getIssuer(), "GAREELUB43IRHWEASCFBLKHURCGMHE5IF6XSE7EXDLACYHGRHM43RFOX");
  }
}
