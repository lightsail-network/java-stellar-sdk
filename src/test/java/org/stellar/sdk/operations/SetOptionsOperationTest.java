package org.stellar.sdk.operations;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import org.junit.Test;
import org.stellar.sdk.Account;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Network;
import org.stellar.sdk.SignerKey;
import org.stellar.sdk.Transaction;
import org.stellar.sdk.TransactionBuilder;
import org.stellar.sdk.TransactionPreconditions;
import org.stellar.sdk.Util;

public class SetOptionsOperationTest {

  @Test
  public void testSetOptionsOperation() {
    // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
    KeyPair source =
        KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");
    // GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR
    KeyPair inflationDestination =
        KeyPair.fromSecretSeed("SDHZGHURAYXKU2KMVHPOXI6JG2Q4BSQUQCEOY72O3QQTCLR2T455PMII");
    // GBCP5W2VS7AEWV2HFRN7YYC623LTSV7VSTGIHFXDEJU7S5BAGVCSETRR
    SignerKey signer =
        SignerKey.fromEd25519PublicKey(
            KeyPair.fromSecretSeed("SA64U7C5C7BS5IHWEPA7YWFN3Z6FE5L6KAMYUIT4AQ7KVTVLD23C6HEZ")
                .getAccountId());

    Integer clearFlags = 1;
    Integer setFlags = 1;
    Integer masterKeyWeight = 1;
    Integer lowThreshold = 2;
    Integer mediumThreshold = 3;
    Integer highThreshold = 4;
    String homeDomain = "stellar.org";
    Integer signerWeight = 1;

    SetOptionsOperation operation =
        SetOptionsOperation.builder()
            .inflationDestination(inflationDestination.getAccountId())
            .clearFlags(clearFlags)
            .setFlags(setFlags)
            .masterKeyWeight(masterKeyWeight)
            .lowThreshold(lowThreshold)
            .mediumThreshold(mediumThreshold)
            .highThreshold(highThreshold)
            .homeDomain(homeDomain)
            .signer(signer)
            .signerWeight(signerWeight)
            .sourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr();
    SetOptionsOperation parsedOperation = (SetOptionsOperation) SetOptionsOperation.fromXdr(xdr);

    assertEquals(inflationDestination.getAccountId(), parsedOperation.getInflationDestination());
    assertEquals(clearFlags, parsedOperation.getClearFlags());
    assertEquals(setFlags, parsedOperation.getSetFlags());
    assertEquals(masterKeyWeight, parsedOperation.getMasterKeyWeight());
    assertEquals(lowThreshold, parsedOperation.getLowThreshold());
    assertEquals(mediumThreshold, parsedOperation.getMediumThreshold());
    assertEquals(highThreshold, parsedOperation.getHighThreshold());
    assertEquals(homeDomain, parsedOperation.getHomeDomain());
    assert parsedOperation.getSigner() != null;
    assertEquals(signer, parsedOperation.getSigner());
    assertEquals(signerWeight, parsedOperation.getSignerWeight());
    assertEquals(source.getAccountId(), parsedOperation.getSourceAccount());

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAAAUAAAABAAAAAO3gUmG83C+VCqO6FztuMtXJF/l7grZA7MjRzqdZ9W8QAAAAAQAAAAEAAAABAAAAAQAAAAEAAAABAAAAAQAAAAIAAAABAAAAAwAAAAEAAAAEAAAAAQAAAAtzdGVsbGFyLm9yZwAAAAABAAAAAET+21WXwEtXRyxb/GBe1tc5V/WUzIOW4yJp+XQgNUUiAAAAAQ==",
        operation.toXdrBase64());
  }

  @Test
  public void testSetOptionsOperationSingleField() {
    // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
    KeyPair source =
        KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");

    String homeDomain = "stellar.org";

    SetOptionsOperation operation =
        SetOptionsOperation.builder()
            .homeDomain(homeDomain)
            .sourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr();
    SetOptionsOperation parsedOperation = (SetOptionsOperation) SetOptionsOperation.fromXdr(xdr);

    assertNull(parsedOperation.getInflationDestination());
    assertNull(parsedOperation.getClearFlags());
    assertNull(parsedOperation.getSetFlags());
    assertNull(parsedOperation.getMasterKeyWeight());
    assertNull(parsedOperation.getLowThreshold());
    assertNull(parsedOperation.getMediumThreshold());
    assertNull(parsedOperation.getHighThreshold());
    assertEquals(homeDomain, parsedOperation.getHomeDomain());
    assertNull(parsedOperation.getSigner());
    assertNull(parsedOperation.getSignerWeight());
    assertEquals(source.getAccountId(), parsedOperation.getSourceAccount());

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAAAUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAtzdGVsbGFyLm9yZwAAAAAA",
        operation.toXdrBase64());
  }

  @Test
  public void testSetOptionsOperationSignerSha256() {
    // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
    KeyPair source =
        KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");

    byte[] preimage = "stellar.org".getBytes();
    byte[] hash = Util.hash(preimage);

    SetOptionsOperation operation =
        SetOptionsOperation.builder()
            .signer(SignerKey.fromSha256Hash(hash))
            .signerWeight(10)
            .sourceAccount(source.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr();
    SetOptionsOperation parsedOperation = (SetOptionsOperation) SetOptionsOperation.fromXdr(xdr);

    assertNull(parsedOperation.getInflationDestination());
    assertNull(parsedOperation.getClearFlags());
    assertNull(parsedOperation.getSetFlags());
    assertNull(parsedOperation.getMasterKeyWeight());
    assertNull(parsedOperation.getLowThreshold());
    assertNull(parsedOperation.getMediumThreshold());
    assertNull(parsedOperation.getHighThreshold());
    assertNull(parsedOperation.getHomeDomain());
    assert parsedOperation.getSigner() != null;
    assertArrayEquals(hash, parsedOperation.getSigner().toXdr().getHashX().getUint256());
    assert parsedOperation.getSignerWeight() != null;
    assertEquals(10, parsedOperation.getSignerWeight().intValue());
    assertEquals(source.getAccountId(), parsedOperation.getSourceAccount());

    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAAAUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAACbpRqMkaQAfCYSk/n3xIl4fCoHfKqxF34ht2iuvSYEJQAAAAK",
        operation.toXdrBase64());
  }

  @Test
  public void testSetOptionsOperationPreAuthTxSigner() {
    // GBPMKIRA2OQW2XZZQUCQILI5TMVZ6JNRKM423BSAISDM7ZFWQ6KWEBC4
    KeyPair source =
        KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    KeyPair destination =
        KeyPair.fromAccountId("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR");

    long sequenceNumber = 2908908335136768L;
    Account account = new Account(source.getAccountId(), sequenceNumber);
    Transaction transaction =
        new TransactionBuilder(account, Network.TESTNET)
            .addOperation(
                CreateAccountOperation.builder()
                    .destination(destination.getAccountId())
                    .startingBalance(BigDecimal.valueOf(2000))
                    .build())
            .setTimeout(TransactionPreconditions.TIMEOUT_INFINITE)
            .setBaseFee(Transaction.MIN_BASE_FEE)
            .build();

    // GC5SIC4E3V56VOHJ3OZAX5SJDTWY52JYI2AFK6PUGSXFVRJQYQXXZBZF
    KeyPair opSource =
        KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");

    SetOptionsOperation operation =
        SetOptionsOperation.builder()
            .signer(SignerKey.fromPreAuthTx(transaction))
            .signerWeight(10)
            .sourceAccount(opSource.getAccountId())
            .build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr();
    SetOptionsOperation parsedOperation = (SetOptionsOperation) SetOptionsOperation.fromXdr(xdr);

    assertEquals(operation.getInflationDestination(), parsedOperation.getInflationDestination());
    assertEquals(operation.getClearFlags(), parsedOperation.getClearFlags());
    assertEquals(operation.getSetFlags(), parsedOperation.getSetFlags());
    assertEquals(operation.getMasterKeyWeight(), parsedOperation.getMasterKeyWeight());
    assertEquals(operation.getLowThreshold(), parsedOperation.getLowThreshold());
    assertEquals(operation.getMediumThreshold(), parsedOperation.getMediumThreshold());
    assertEquals(operation.getHighThreshold(), parsedOperation.getHighThreshold());
    assertEquals(operation.getHomeDomain(), parsedOperation.getHomeDomain());
    assert parsedOperation.getSigner() != null;
    assertArrayEquals(
        transaction.hash(), parsedOperation.getSigner().toXdr().getPreAuthTx().getUint256());
    assertEquals(operation.getSignerWeight(), parsedOperation.getSignerWeight());
    assertEquals(operation.getSourceAccount(), parsedOperation.getSourceAccount());
  }

  @Test
  public void testPaylodSignerKey() {
    KeyPair source =
        KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");
    SetOptionsOperation.SetOptionsOperationBuilder<?, ?> builder = SetOptionsOperation.builder();
    String payloadSignerStrKey = "GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVSGZ";

    byte[] payload =
        Util.hexToBytes(
            "0102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f20".toUpperCase());
    SignerKey signerKey = SignerKey.fromEd25519SignedPayload(payloadSignerStrKey, payload);

    builder.signer(signerKey);
    builder.signerWeight(1);
    builder.sourceAccount(source.getAccountId());

    SetOptionsOperation operation = builder.build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr();
    SetOptionsOperation parsedOperation = (SetOptionsOperation) Operation.fromXdr(xdr);

    // verify round trip between xdr and pojo
    assertEquals(source.getAccountId(), parsedOperation.getSourceAccount());
    assert parsedOperation.getSigner() != null;
    assertEquals(signerKey, parsedOperation.getSigner());

    // verify serialized xdr emitted with signed payload
    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAAAUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
            + "AAAAAAAAAAAAAAAEAAAADPww0v5OtDZlx0EzMkPcFURyDiq2XNKSi+w16A/x/6JoAAAAgAQIDBAUGBwgJCgsMDQ4PEBES"
            + "ExQVFhcYGRobHB0eHyAAAAAB",
        operation.toXdrBase64());
  }
}
