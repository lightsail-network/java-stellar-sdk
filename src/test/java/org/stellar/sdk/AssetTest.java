package org.stellar.sdk;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.stellar.sdk.Asset.create;
import static org.stellar.sdk.Asset.createNativeAsset;
import static org.stellar.sdk.Asset.createNonNativeAsset;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

/** Created by andrewrogers on 7/1/15. */
public class AssetTest {
  Asset xlm = createNativeAsset();

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
    AssetTypeCreditAlphaNum4 asset = new AssetTypeCreditAlphaNum4(code, issuer.getAccountId());
    org.stellar.sdk.xdr.Asset xdr = asset.toXdr();
    AssetTypeCreditAlphaNum4 parsedAsset = (AssetTypeCreditAlphaNum4) Asset.fromXdr(xdr);
    assertEquals(code, asset.getCode());
    assertEquals(issuer.getAccountId(), parsedAsset.getIssuer());
  }

  @Test
  public void testAssetTypeCreditAlphaNum12() {
    String code = "TESTTEST";
    KeyPair issuer = KeyPair.random();
    AssetTypeCreditAlphaNum12 asset = new AssetTypeCreditAlphaNum12(code, issuer.getAccountId());
    org.stellar.sdk.xdr.Asset xdr = asset.toXdr();
    AssetTypeCreditAlphaNum12 parsedAsset = (AssetTypeCreditAlphaNum12) Asset.fromXdr(xdr);
    assertEquals(code, asset.getCode());
    assertEquals(issuer.getAccountId(), parsedAsset.getIssuer());
  }

  @Test
  public void testHashCode() {
    String issuer1 = KeyPair.random().getAccountId();
    String issuer2 = KeyPair.random().getAccountId();

    // Equal
    assertEquals(new AssetTypeNative().hashCode(), new AssetTypeNative().hashCode());
    assertEquals(
        new AssetTypeCreditAlphaNum4("USD", issuer1).hashCode(),
        new AssetTypeCreditAlphaNum4("USD", issuer1).hashCode());
    assertEquals(
        new AssetTypeCreditAlphaNum12("ABCDE", issuer1).hashCode(),
        new AssetTypeCreditAlphaNum12("ABCDE", issuer1).hashCode());

    // Not equal
    assertNotEquals(
        new AssetTypeNative().hashCode(), new AssetTypeCreditAlphaNum4("USD", issuer1).hashCode());
    assertNotEquals(
        new AssetTypeNative().hashCode(),
        new AssetTypeCreditAlphaNum12("ABCDE", issuer1).hashCode());
    assertNotEquals(
        new AssetTypeCreditAlphaNum4("EUR", issuer1).hashCode(),
        new AssetTypeCreditAlphaNum4("USD", issuer1).hashCode());
    assertNotEquals(
        new AssetTypeCreditAlphaNum4("EUR", issuer1).hashCode(),
        new AssetTypeCreditAlphaNum4("EUR", issuer2).hashCode());
    assertNotEquals(
        new AssetTypeCreditAlphaNum4("EUR", issuer1).hashCode(),
        new AssetTypeCreditAlphaNum12("EUROPE", issuer1).hashCode());
    assertNotEquals(
        new AssetTypeCreditAlphaNum4("EUR", issuer1).hashCode(),
        new AssetTypeCreditAlphaNum12("EUROPE", issuer2).hashCode());
    assertNotEquals(
        new AssetTypeCreditAlphaNum12("ABCDE", issuer1).hashCode(),
        new AssetTypeCreditAlphaNum12("EDCBA", issuer1).hashCode());
    assertNotEquals(
        new AssetTypeCreditAlphaNum12("ABCDE", issuer1).hashCode(),
        new AssetTypeCreditAlphaNum12("ABCDE", issuer2).hashCode());
  }

  @Test
  public void testAssetEquals() {
    String issuer1 = KeyPair.random().getAccountId();
    String issuer2 = KeyPair.random().getAccountId();

    assertEquals(new AssetTypeNative(), new AssetTypeNative());
    assertEquals(
        new AssetTypeCreditAlphaNum4("USD", issuer1), new AssetTypeCreditAlphaNum4("USD", issuer1));
    assertEquals(
        new AssetTypeCreditAlphaNum12("ABCDE", issuer1),
        new AssetTypeCreditAlphaNum12("ABCDE", issuer1));

    assertNotEquals(new AssetTypeNative(), new AssetTypeCreditAlphaNum4("USD", issuer1));
    assertNotEquals(new AssetTypeNative(), new AssetTypeCreditAlphaNum12("ABCDE", issuer1));
    assertNotEquals(
        new AssetTypeCreditAlphaNum4("EUR", issuer1), new AssetTypeCreditAlphaNum4("USD", issuer1));
    assertNotEquals(
        new AssetTypeCreditAlphaNum4("EUR", issuer1), new AssetTypeCreditAlphaNum4("EUR", issuer2));
    assertNotEquals(
        new AssetTypeCreditAlphaNum12("ABCDE", issuer1),
        new AssetTypeCreditAlphaNum12("EDCBA", issuer1));
    assertNotEquals(
        new AssetTypeCreditAlphaNum12("ABCDE", issuer1),
        new AssetTypeCreditAlphaNum12("ABCDE", issuer2));
  }

  @Test
  public void testAssetCompareTo0IfAssetsEqual() {
    Asset assetA =
        createNonNativeAsset("ARST", "GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO");
    Asset assetB =
        createNonNativeAsset("USD", "GCEZWKCA5VLDNRLN3RPRJMRZOX3Z6G5CHCGSNFHEYVXM3XOJMDS674JZ");

    assertEquals(0, xlm.compareTo(xlm));
    assertEquals(0, assetA.compareTo(assetA));
    assertEquals(0, assetB.compareTo(assetB));
  }

  @Test
  public void testAssetCompareToOrderingByType() {
    Asset anum4 =
        createNonNativeAsset("ARSZ", "GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO");
    Asset anum12 =
        createNonNativeAsset(
            "ARSTANUM12", "GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO");

    assertEquals(0, xlm.compareTo(xlm));
    assertEquals(-1, xlm.compareTo(anum4));
    assertEquals(-1, xlm.compareTo(anum12));

    assertEquals(1, anum4.compareTo(xlm));
    assertEquals(0, anum4.compareTo(anum4));
    assertEquals(-1, anum4.compareTo(anum12));

    assertEquals(1, anum12.compareTo(xlm));
    assertEquals(1, anum12.compareTo(anum4));
    assertEquals(0, anum12.compareTo(anum12));
  }

  @Test
  public void testAssetCompareToOrderingByCode() {
    Asset assetARST =
        createNonNativeAsset("ARST", "GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO");
    Asset assetUSDX =
        createNonNativeAsset("USDX", "GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO");

    assertEquals(0, assetARST.compareTo(assetARST));
    assertTrue(assetARST.compareTo(assetUSDX) < 0);

    assertTrue(assetUSDX.compareTo(assetARST) > 0);
    assertEquals(0, assetUSDX.compareTo(assetUSDX));
  }

  @Test
  public void testAssetCompareToOrderingByIssuer() {
    Asset assetIssuerA =
        createNonNativeAsset("ARST", "GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO");
    Asset assetIssuerB =
        createNonNativeAsset("ARST", "GCEZWKCA5VLDNRLN3RPRJMRZOX3Z6G5CHCGSNFHEYVXM3XOJMDS674JZ");

    assertTrue(assetIssuerA.compareTo(assetIssuerB) < 0);
    assertEquals(0, assetIssuerA.compareTo(assetIssuerA));

    assertTrue(assetIssuerB.compareTo(assetIssuerA) > 0);
    assertEquals(0, assetIssuerB.compareTo(assetIssuerB));
  }

  @Test
  public void testAssetsAreSortable() {
    // Native is always first
    Asset a = create("native");
    // Type is Alphanum4
    Asset b =
        createNonNativeAsset("BCDE", "GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO");

    // Type is Alphanum12
    Asset c =
        createNonNativeAsset("ABCD1", "GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO");

    // Code is >
    Asset d =
        createNonNativeAsset("ABCD2", "GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO");

    // Issuer is >
    Asset e =
        createNonNativeAsset("ABCD2", "GCEZWKCA5VLDNRLN3RPRJMRZOX3Z6G5CHCGSNFHEYVXM3XOJMDS674JZ");

    Asset[] expected = {a, b, c, d, e};

    // Basic sorting check that it doesn't reorder stuff
    List<Asset> assets = Arrays.asList(new Asset[] {a, b, c, d, e});
    Collections.sort(assets);
    assertArrayEquals(expected, assets.toArray());

    // Reverse it and check it still sorts the same
    Collections.reverse(assets);
    Collections.sort(assets);
    assertArrayEquals(expected, assets.toArray());

    // Shuffle it and check it still sorts to the same
    Collections.shuffle(assets);
    Collections.sort(assets);
    assertArrayEquals(expected, assets.toArray());
  }

  @Test
  public void testGetContractId() throws IOException {
    // native + testnet
    assertEquals(
        "CDLZFC3SYJYDZT7K67VZ75HPJVIEUVNIXF47ZG2FB2RMQQVU2HHGCYSC",
        new AssetTypeNative().getContractId(Network.TESTNET));
    // native + public
    assertEquals(
        "CAS3J7GYLGXMF6TDJBBYYSE3HQ6BBSMLNUQ34T6TZMYMW2EVH34XOWMA",
        new AssetTypeNative().getContractId(Network.PUBLIC));
    // alphanum4 + public
    assertEquals(
        "CCW67TSZV3SSS2HXMBQ5JFGCKJNXKZM7UQUWUZPUTHXSTZLEO7SJMI75",
        new AssetTypeCreditAlphaNum4(
                "USDC", "GA5ZSEJYB37JRC5AVCIA5MOP4RHTM335X2KGX3IHOJAPP5RE34K4KZVN")
            .getContractId(Network.PUBLIC));
    // alphanum12 + public
    assertEquals(
        "CDOFW7HNKLUZRLFZST4EW7V3AV4JI5IHMT6BPXXSY2IEFZ4NE5TWU2P4",
        new AssetTypeCreditAlphaNum12(
                "yUSDC", "GDGTVWSM4MGS4T7Z6W4RPWOCHE2I6RDFCIFZGS3DOA63LWQTRNZNTTFF")
            .getContractId(Network.PUBLIC));
  }
}
