package org.stellar.sdk;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by andrewrogers on 7/1/15.
 */
public class AssetTest {

  @Test
  public void testAssetTypeNative() {
    AssetTypeNative asset = new AssetTypeNative();
    org.stellar.sdk.xdr.Asset xdr = asset.toXdr();
    Asset parsedAsset = Asset.fromXdr(xdr);
    assertTrue(parsedAsset instanceof AssetTypeNative);
  }

  @Test
  public void testAssetTypeCreditAlphaNum4() {
    String code = "USDA";
    KeyPair issuer = KeyPair.random();
    AssetTypeCreditAlphaNum4 asset = new AssetTypeCreditAlphaNum4(code, issuer);
    org.stellar.sdk.xdr.Asset xdr = asset.toXdr();
    AssetTypeCreditAlphaNum4 parsedAsset = (AssetTypeCreditAlphaNum4) Asset.fromXdr(xdr);
    assertEquals(code, asset.getCode());
    assertEquals(issuer.getAccountId(), parsedAsset.getIssuer().getAccountId());
  }

  @Test
  public void testAssetTypeCreditAlphaNum12() {
    String code = "TESTTEST";
    KeyPair issuer = KeyPair.random();
    AssetTypeCreditAlphaNum12 asset = new AssetTypeCreditAlphaNum12(code, issuer);
    org.stellar.sdk.xdr.Asset xdr = asset.toXdr();
    AssetTypeCreditAlphaNum12 parsedAsset = (AssetTypeCreditAlphaNum12) Asset.fromXdr(xdr);
    assertEquals(code, asset.getCode());
    assertEquals(issuer.getAccountId(), parsedAsset.getIssuer().getAccountId());
  }

  @Test
  public void testAssetEquals() {
    KeyPair issuer1 = KeyPair.random();
    KeyPair issuer2 = KeyPair.random();

    assertTrue(new AssetTypeNative().equals(new AssetTypeNative()));
    assertTrue(new AssetTypeCreditAlphaNum4("USD", issuer1).equals(new AssetTypeCreditAlphaNum4("USD", issuer1)));
    assertTrue(new AssetTypeCreditAlphaNum12("ABCDE", issuer1).equals(new AssetTypeCreditAlphaNum12("ABCDE", issuer1)));

    assertFalse(new AssetTypeNative().equals(new AssetTypeCreditAlphaNum4("USD", issuer1)));
    assertFalse(new AssetTypeNative().equals(new AssetTypeCreditAlphaNum12("ABCDE", issuer1)));
    assertFalse(new AssetTypeCreditAlphaNum4("EUR", issuer1).equals(new AssetTypeCreditAlphaNum4("USD", issuer1)));
    assertFalse(new AssetTypeCreditAlphaNum4("EUR", issuer1).equals(new AssetTypeCreditAlphaNum4("EUR", issuer2)));
    assertFalse(new AssetTypeCreditAlphaNum12("ABCDE", issuer1).equals(new AssetTypeCreditAlphaNum12("EDCBA", issuer1)));
    assertFalse(new AssetTypeCreditAlphaNum12("ABCDE", issuer1).equals(new AssetTypeCreditAlphaNum12("ABCDE", issuer2)));
  }
}
