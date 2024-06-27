package org.stellar.sdk.operations;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stellar.sdk.AccountConverter;
import org.stellar.sdk.KeyPair;

public class AllowTrustOperationTest {
  @Test
  @SuppressWarnings("deprecation")
  public void testAllowTrustOperationWithAuthorizeFlag() {
    // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
    KeyPair source =
        KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");
    // GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR
    KeyPair trustor =
        KeyPair.fromSecretSeed("SDHZGHURAYXKU2KMVHPOXI6JG2Q4BSQUQCEOY72O3QQTCLR2T455PMII");

    String assetCode = "USDA";
    AllowTrustOperation.TrustLineEntryFlag authorize =
        AllowTrustOperation.TrustLineEntryFlag.AUTHORIZED_FLAG;

    AllowTrustOperation operation =
        AllowTrustOperation.builder()
            .trustor(trustor.getAccountId())
            .assetCode(assetCode)
            .authorize(authorize)
            .sourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    AllowTrustOperation parsedOperation =
        (AllowTrustOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);

    assertEquals(source.getAccountId(), parsedOperation.getSourceAccount());
    assertEquals(trustor.getAccountId(), parsedOperation.getTrustor());
    assertEquals(assetCode, parsedOperation.getAssetCode());
    assertEquals(authorize, parsedOperation.getAuthorize());

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAAAcAAAAA7eBSYbzcL5UKo7oXO24y1ckX+XuCtkDsyNHOp1n1bxAAAAABVVNEQQAAAAE=",
        operation.toXdrBase64(AccountConverter.enableMuxed()));
  }

  @Test
  @SuppressWarnings("deprecation")
  public void testAllowTrustOperationWithAuthorizedToMaintainLiabilitiesFlag() {
    // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
    KeyPair source =
        KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");
    // GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR
    KeyPair trustor =
        KeyPair.fromSecretSeed("SDHZGHURAYXKU2KMVHPOXI6JG2Q4BSQUQCEOY72O3QQTCLR2T455PMII");

    String assetCode = "USDA";
    AllowTrustOperation.TrustLineEntryFlag authorize =
        AllowTrustOperation.TrustLineEntryFlag.AUTHORIZED_TO_MAINTAIN_LIABILITIES_FLAG;

    AllowTrustOperation operation =
        AllowTrustOperation.builder()
            .trustor(trustor.getAccountId())
            .assetCode(assetCode)
            .authorize(authorize)
            .sourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    AllowTrustOperation parsedOperation =
        (AllowTrustOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);

    assertEquals(source.getAccountId(), parsedOperation.getSourceAccount());
    assertEquals(trustor.getAccountId(), parsedOperation.getTrustor());
    assertEquals(assetCode, parsedOperation.getAssetCode());
    assertEquals(authorize, parsedOperation.getAuthorize());

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAAAcAAAAA7eBSYbzcL5UKo7oXO24y1ckX+XuCtkDsyNHOp1n1bxAAAAABVVNEQQAAAAI=",
        operation.toXdrBase64(AccountConverter.enableMuxed()));
  }

  @Test
  @SuppressWarnings("deprecation")
  public void testAllowTrustOperationWithUnauthorizeFlag() {
    // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
    KeyPair source =
        KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");
    // GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR
    KeyPair trustor =
        KeyPair.fromSecretSeed("SDHZGHURAYXKU2KMVHPOXI6JG2Q4BSQUQCEOY72O3QQTCLR2T455PMII");

    String assetCode = "USDA";
    AllowTrustOperation.TrustLineEntryFlag authorize =
        AllowTrustOperation.TrustLineEntryFlag.UNAUTHORIZED_FLAG;

    AllowTrustOperation operation =
        AllowTrustOperation.builder()
            .trustor(trustor.getAccountId())
            .assetCode(assetCode)
            .authorize(authorize)
            .sourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    AllowTrustOperation parsedOperation =
        (AllowTrustOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);

    assertEquals(source.getAccountId(), parsedOperation.getSourceAccount());
    assertEquals(trustor.getAccountId(), parsedOperation.getTrustor());
    assertEquals(assetCode, parsedOperation.getAssetCode());
    assertEquals(authorize, parsedOperation.getAuthorize());

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAAAcAAAAA7eBSYbzcL5UKo7oXO24y1ckX+XuCtkDsyNHOp1n1bxAAAAABVVNEQQAAAAA=",
        operation.toXdrBase64(AccountConverter.enableMuxed()));
  }

  @Test
  @SuppressWarnings("deprecation")
  public void testAllowTrustOperationAssetCodeBuffer() {
    // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
    KeyPair source =
        KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");
    // GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR
    KeyPair trustor =
        KeyPair.fromSecretSeed("SDHZGHURAYXKU2KMVHPOXI6JG2Q4BSQUQCEOY72O3QQTCLR2T455PMII");

    String assetCode = "USDABC";
    AllowTrustOperation.TrustLineEntryFlag authorize =
        AllowTrustOperation.TrustLineEntryFlag.AUTHORIZED_FLAG;

    AllowTrustOperation operation =
        AllowTrustOperation.builder()
            .trustor(trustor.getAccountId())
            .assetCode(assetCode)
            .authorize(authorize)
            .sourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    AllowTrustOperation parsedOperation =
        (AllowTrustOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);

    assertEquals(assetCode, parsedOperation.getAssetCode());
  }
}
