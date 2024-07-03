package org.stellar.sdk;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import org.junit.Test;
import org.stellar.sdk.xdr.AssetType;

public class ChangeTrustAssetTest {
  @Test
  public void testChangeTrustAssetWithNativeAsset() throws IOException {
    // from stellar_sdk import *
    //
    // asset = Asset.native()
    // print(asset.to_change_trust_asset_xdr_object().to_xdr())
    Asset asset = new AssetTypeNative();
    ChangeTrustAsset trustLineAsset = new ChangeTrustAsset(asset);
    assertEquals(trustLineAsset.getAssetType(), AssetType.ASSET_TYPE_NATIVE);
    assertEquals(trustLineAsset.getAsset(), asset);
    String expectedXdr = "AAAAAA==";
    assertEquals(trustLineAsset.toXdr().toXdrBase64(), expectedXdr);
    org.stellar.sdk.xdr.ChangeTrustAsset xdr = trustLineAsset.toXdr();
    ChangeTrustAsset parsedChangeTrustAsset = ChangeTrustAsset.fromXdr(xdr);
    assertEquals(trustLineAsset, parsedChangeTrustAsset);
  }

  @Test
  public void testChangeTrustAssetWithANum4Asset() throws IOException {
    // from stellar_sdk import *
    //
    // asset = Asset("ARST", "GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO")
    // print(asset.to_change_trust_asset_xdr_object().to_xdr())
    Asset asset = Asset.create("ARST:GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO");
    ChangeTrustAsset trustLineAsset = new ChangeTrustAsset(asset);
    assertEquals(trustLineAsset.getAssetType(), AssetType.ASSET_TYPE_CREDIT_ALPHANUM4);
    assertEquals(trustLineAsset.getAsset(), asset);
    String expectedXdr = "AAAAAUFSU1QAAAAAfzBiNMmJ6dZ/ad/ZMRmLYA89bOa2TCJAcB+2KwiDK4c=";
    assertEquals(trustLineAsset.toXdr().toXdrBase64(), expectedXdr);
    org.stellar.sdk.xdr.ChangeTrustAsset xdr = trustLineAsset.toXdr();
    ChangeTrustAsset parsedChangeTrustAsset = ChangeTrustAsset.fromXdr(xdr);
    assertEquals(trustLineAsset, parsedChangeTrustAsset);
  }

  @Test
  public void testChangeTrustAssetWithANum12Asset() throws IOException {
    // from stellar_sdk import *
    //
    // asset = Asset("ARST123123", "GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO")
    // print(asset.to_change_trust_asset_xdr_object().to_xdr())
    Asset asset =
        Asset.create("ARST123123:GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO");
    ChangeTrustAsset trustLineAsset = new ChangeTrustAsset(asset);
    assertEquals(trustLineAsset.getAssetType(), AssetType.ASSET_TYPE_CREDIT_ALPHANUM12);
    assertEquals(trustLineAsset.getAsset(), asset);
    String expectedXdr = "AAAAAkFSU1QxMjMxMjMAAAAAAAB/MGI0yYnp1n9p39kxGYtgDz1s5rZMIkBwH7YrCIMrhw==";
    assertEquals(trustLineAsset.toXdr().toXdrBase64(), expectedXdr);
    org.stellar.sdk.xdr.ChangeTrustAsset xdr = trustLineAsset.toXdr();
    ChangeTrustAsset parsedChangeTrustAsset = ChangeTrustAsset.fromXdr(xdr);
    assertEquals(trustLineAsset, parsedChangeTrustAsset);
  }

  @Test
  public void testChangeTrustAssetWithLiquidityPoolShareAsset() throws IOException {
    // from stellar_sdk import LiquidityPoolAsset, Asset, LIQUIDITY_POOL_FEE_V18
    //
    // asset_a = Asset("ARST", "GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO")
    // asset_b = Asset("USD", "GCEZWKCA5VLDNRLN3RPRJMRZOX3Z6G5CHCGSNFHEYVXM3XOJMDS674JZ")
    // fee = LIQUIDITY_POOL_FEE_V18
    //
    // asset = LiquidityPoolAsset(asset_a, asset_b, fee=fee)
    // print(asset.to_change_trust_asset_xdr_object().to_xdr())
    Asset assetA = Asset.create("ARST:GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO");
    Asset assetB = Asset.create("USD:GCEZWKCA5VLDNRLN3RPRJMRZOX3Z6G5CHCGSNFHEYVXM3XOJMDS674JZ");
    LiquidityPool liquidityPool = new LiquidityPool(assetA, assetB);
    ChangeTrustAsset trustLineAsset = new ChangeTrustAsset(liquidityPool);
    String expectedXdr =
        "AAAAAwAAAAAAAAABQVJTVAAAAAB/MGI0yYnp1n9p39kxGYtgDz1s5rZMIkBwH7YrCIMrhwAAAAFVU0QAAAAAAImbKEDtVjbFbdxfFLI5dfefG6I4jSaU5MVuzd3JYOXvAAAAHg==";
    assertEquals(trustLineAsset.getAssetType(), AssetType.ASSET_TYPE_POOL_SHARE);
    assertEquals(trustLineAsset.getLiquidityPool(), liquidityPool);
    assertEquals(trustLineAsset.toXdr().toXdrBase64(), expectedXdr);
    org.stellar.sdk.xdr.ChangeTrustAsset xdr = trustLineAsset.toXdr();
    ChangeTrustAsset parsedChangeTrustAsset = ChangeTrustAsset.fromXdr(xdr);
    assertEquals(trustLineAsset, parsedChangeTrustAsset);
  }
}
