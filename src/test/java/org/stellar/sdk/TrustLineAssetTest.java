package org.stellar.sdk;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import org.junit.Test;
import org.stellar.sdk.xdr.AssetType;

public class TrustLineAssetTest {
  @Test
  public void testTrustLineWithNativeAsset() throws IOException {
    // from stellar_sdk import *
    //
    // asset = Asset.native()
    // print(asset.to_trust_line_asset_xdr_object().to_xdr())
    Asset asset = new AssetTypeNative();
    TrustLineAsset trustLineAsset = new TrustLineAsset(asset);
    assertEquals(trustLineAsset.getAssetType(), AssetType.ASSET_TYPE_NATIVE);
    assertEquals(trustLineAsset.getAsset(), asset);
    String expectedXdr = "AAAAAA==";
    assertEquals(trustLineAsset.toXdr().toXdrBase64(), expectedXdr);
    org.stellar.sdk.xdr.TrustLineAsset xdr = trustLineAsset.toXdr();
    TrustLineAsset parsedTrustLineAsset = TrustLineAsset.fromXdr(xdr);
    assertEquals(trustLineAsset, parsedTrustLineAsset);
  }

  @Test
  public void testTrustLineWithANum4Asset() throws IOException {
    // from stellar_sdk import *
    //
    // asset = Asset("ARST", "GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO")
    // print(asset.to_trust_line_asset_xdr_object().to_xdr())
    Asset asset = Asset.create("ARST:GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO");
    TrustLineAsset trustLineAsset = new TrustLineAsset(asset);
    assertEquals(trustLineAsset.getAssetType(), AssetType.ASSET_TYPE_CREDIT_ALPHANUM4);
    assertEquals(trustLineAsset.getAsset(), asset);
    String expectedXdr = "AAAAAUFSU1QAAAAAfzBiNMmJ6dZ/ad/ZMRmLYA89bOa2TCJAcB+2KwiDK4c=";
    assertEquals(trustLineAsset.toXdr().toXdrBase64(), expectedXdr);
    org.stellar.sdk.xdr.TrustLineAsset xdr = trustLineAsset.toXdr();
    TrustLineAsset parsedTrustLineAsset = TrustLineAsset.fromXdr(xdr);
    assertEquals(trustLineAsset, parsedTrustLineAsset);
  }

  @Test
  public void testTrustLineWithANum12Asset() throws IOException {
    // from stellar_sdk import *
    //
    // asset = Asset("ARST123123", "GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO")
    // print(asset.to_trust_line_asset_xdr_object().to_xdr())
    Asset asset =
        Asset.create("ARST123123:GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO");
    TrustLineAsset trustLineAsset = new TrustLineAsset(asset);
    assertEquals(trustLineAsset.getAssetType(), AssetType.ASSET_TYPE_CREDIT_ALPHANUM12);
    assertEquals(trustLineAsset.getAsset(), asset);
    String expectedXdr = "AAAAAkFSU1QxMjMxMjMAAAAAAAB/MGI0yYnp1n9p39kxGYtgDz1s5rZMIkBwH7YrCIMrhw==";
    assertEquals(trustLineAsset.toXdr().toXdrBase64(), expectedXdr);
    org.stellar.sdk.xdr.TrustLineAsset xdr = trustLineAsset.toXdr();
    TrustLineAsset parsedTrustLineAsset = TrustLineAsset.fromXdr(xdr);
    assertEquals(trustLineAsset, parsedTrustLineAsset);
  }

  @Test
  public void testTrustLineWithLiquidityPoolShareAsset() throws IOException {
    // from stellar_sdk import *
    //
    // pool_id = LiquidityPoolId("dd7b1ab831c273310ddbec6f97870aa83c2fbd78ce22aded37ecbf4f3380faca")
    // print(pool_id.to_trust_line_asset_xdr_object().to_xdr())
    LiquidityPoolId poolId =
        new LiquidityPoolId("dd7b1ab831c273310ddbec6f97870aa83c2fbd78ce22aded37ecbf4f3380faca");
    TrustLineAsset trustLineAsset = new TrustLineAsset(poolId);
    assertEquals(trustLineAsset.getAssetType(), AssetType.ASSET_TYPE_POOL_SHARE);
    assertEquals(trustLineAsset.getLiquidityPoolId(), poolId);
    String expectedXdr = "AAAAA917GrgxwnMxDdvsb5eHCqg8L714ziKt7Tfsv08zgPrK";
    assertEquals(trustLineAsset.toXdr().toXdrBase64(), expectedXdr);
    org.stellar.sdk.xdr.TrustLineAsset xdr = trustLineAsset.toXdr();
    TrustLineAsset parsedTrustLineAsset = TrustLineAsset.fromXdr(xdr);
    assertEquals(trustLineAsset, parsedTrustLineAsset);
  }
}
