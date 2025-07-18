package org.stellar.sdk;

import static org.junit.Assert.*;

import org.junit.Test;
import org.stellar.sdk.operations.BumpSequenceOperation;
import org.stellar.sdk.xdr.SignerKeyType;
import org.stellar.sdk.xdr.Uint256;

public class SignerKeyTest {

  @Test
  public void testEd25519PublicKey() {
    String accountId = "GCC3U63F5OJIG4VS6XCFUJGCQRRMNCVGASDGIZZEPA3AZ242K4JVPIYV";
    byte[] signerKeyData =
        Util.hexToBytes("85ba7b65eb928372b2f5c45a24c28462c68aa6048664672478360ceb9a571357");
    SignerKeyType signerKeyType = SignerKeyType.SIGNER_KEY_TYPE_ED25519;
    SignerKey signerKey = new SignerKey(signerKeyData, signerKeyType);

    org.stellar.sdk.xdr.SignerKey signerKeyXdr =
        org.stellar.sdk.xdr.SignerKey.builder()
            .discriminant(org.stellar.sdk.xdr.SignerKeyType.SIGNER_KEY_TYPE_ED25519)
            .ed25519(new Uint256(signerKeyData))
            .build();

    assertEquals(signerKeyXdr, signerKey.toXdr());
    assertEquals(accountId, signerKey.getEncodedSignerKey());
    assertEquals(signerKey, SignerKey.fromEncodedSignerKey(accountId));
    assertEquals(signerKey, SignerKey.fromXdr(signerKeyXdr));
    assertEquals(signerKey, SignerKey.fromEd25519PublicKey(accountId));
    assertEquals(signerKey, SignerKey.fromEd25519PublicKey(signerKeyData));
  }

  @Test
  public void testPreAuthTx() {
    String preAuthTx = "TCC3U63F5OJIG4VS6XCFUJGCQRRMNCVGASDGIZZEPA3AZ242K4JVOVKE";
    byte[] signerKeyData =
        Util.hexToBytes("85ba7b65eb928372b2f5c45a24c28462c68aa6048664672478360ceb9a571357");
    SignerKeyType signerKeyType = SignerKeyType.SIGNER_KEY_TYPE_PRE_AUTH_TX;
    SignerKey signerKey = new SignerKey(signerKeyData, signerKeyType);

    org.stellar.sdk.xdr.SignerKey signerKeyXdr =
        org.stellar.sdk.xdr.SignerKey.builder()
            .discriminant(org.stellar.sdk.xdr.SignerKeyType.SIGNER_KEY_TYPE_PRE_AUTH_TX)
            .preAuthTx(new Uint256(signerKeyData))
            .build();

    assertEquals(signerKeyXdr, signerKey.toXdr());
    assertEquals(preAuthTx, signerKey.getEncodedSignerKey());
    assertEquals(signerKey, SignerKey.fromEncodedSignerKey(preAuthTx));
    assertEquals(signerKey, SignerKey.fromXdr(signerKeyXdr));
    assertEquals(signerKey, SignerKey.fromPreAuthTx(preAuthTx));
    assertEquals(signerKey, SignerKey.fromPreAuthTx(signerKeyData));
  }

  @Test
  public void testSha256Hash() {
    String sha256Hash = "XCC3U63F5OJIG4VS6XCFUJGCQRRMNCVGASDGIZZEPA3AZ242K4JVPRP5";
    byte[] signerKeyData =
        Util.hexToBytes("85ba7b65eb928372b2f5c45a24c28462c68aa6048664672478360ceb9a571357");
    SignerKeyType signerKeyType = SignerKeyType.SIGNER_KEY_TYPE_HASH_X;
    SignerKey signerKey = new SignerKey(signerKeyData, signerKeyType);

    org.stellar.sdk.xdr.SignerKey signerKeyXdr =
        org.stellar.sdk.xdr.SignerKey.builder()
            .discriminant(org.stellar.sdk.xdr.SignerKeyType.SIGNER_KEY_TYPE_HASH_X)
            .hashX(new Uint256(signerKeyData))
            .build();

    assertEquals(signerKeyXdr, signerKey.toXdr());
    assertEquals(sha256Hash, signerKey.getEncodedSignerKey());
    assertEquals(signerKey, SignerKey.fromEncodedSignerKey(sha256Hash));
    assertEquals(signerKey, SignerKey.fromXdr(signerKeyXdr));
    assertEquals(signerKey, SignerKey.fromSha256Hash(sha256Hash));
    assertEquals(signerKey, SignerKey.fromSha256Hash(signerKeyData));
  }

  @Test
  public void testEd25519SignedPayload() {
    String ed25519SignedPayload =
        "PA3D5KRYM6CB7OWQ6TWYRR3Z4T7GNZLKERYNZGGA5SOAOPIFY6YQGAAAAAEQAAAAAAAAAAAAAAAAAABBXA";
    byte[] signerKeyData =
        Util.hexToBytes(
            "363eaa3867841fbad0f4ed88c779e4fe66e56a2470dc98c0ec9c073d05c7b10300000009000000000000000000000000");
    SignerKeyType signerKeyType = SignerKeyType.SIGNER_KEY_TYPE_ED25519_SIGNED_PAYLOAD;
    SignerKey signerKey = new SignerKey(signerKeyData, signerKeyType);

    byte[] ed25519PublicKeyData =
        Util.hexToBytes("363eaa3867841fbad0f4ed88c779e4fe66e56a2470dc98c0ec9c073d05c7b103");
    byte[] payload = Util.hexToBytes("000000000000000000");

    org.stellar.sdk.xdr.SignerKey signerKeyXdr =
        org.stellar.sdk.xdr.SignerKey.builder()
            .discriminant(org.stellar.sdk.xdr.SignerKeyType.SIGNER_KEY_TYPE_ED25519_SIGNED_PAYLOAD)
            .ed25519SignedPayload(
                org.stellar.sdk.xdr.SignerKey.SignerKeyEd25519SignedPayload.builder()
                    .ed25519(new Uint256(ed25519PublicKeyData))
                    .payload(payload)
                    .build())
            .build();

    assertEquals(signerKeyXdr, signerKey.toXdr());
    assertEquals(ed25519SignedPayload, signerKey.getEncodedSignerKey());
    assertEquals(signerKey, SignerKey.fromEncodedSignerKey(ed25519SignedPayload));
    assertEquals(signerKey, SignerKey.fromXdr(signerKeyXdr));
    assertEquals(signerKey, SignerKey.fromEd25519SignedPayload(ed25519SignedPayload));
    assertEquals(signerKey, SignerKey.fromEd25519SignedPayload(signerKeyData));
  }

  @Test
  public void testEd25519SignedPayloadFromStringAndBytes() {
    String accountId = "GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVSGZ";
    String payload1Hex = "0102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f20";
    String payload2Hex = "0102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d";
    String strkey1 =
        "PA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAQACAQDAQCQMBYIBEFAWDANBYHRAEISCMKBKFQXDAMRUGY4DUPB6IBZGM";
    String strkey2 =
        "PA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAOQCAQDAQCQMBYIBEFAWDANBYHRAEISCMKBKFQXDAMRUGY4DUAAAAFGBU";

    byte[] payload1 = Util.hexToBytes(payload1Hex);
    byte[] payload2 = Util.hexToBytes(payload2Hex);

    SignerKey signerKey1 = SignerKey.fromEd25519SignedPayload(accountId, payload1);
    SignerKey signerKey2 = SignerKey.fromEd25519SignedPayload(accountId, payload2);

    assertEquals(strkey1, signerKey1.getEncodedSignerKey());
    assertEquals(strkey2, signerKey2.getEncodedSignerKey());
    assertEquals(signerKey1, SignerKey.fromEncodedSignerKey(strkey1));
    assertEquals(signerKey2, SignerKey.fromEncodedSignerKey(strkey2));
  }

  @Test
  public void testEd25519SignedPayloadFromSignedPayloadSigner() {
    String accountId = "GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVSGZ";
    byte[] publicKeyBytes = StrKey.decodeEd25519PublicKey(accountId);
    byte[] payload =
        Util.hexToBytes("0102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f20");
    String strkey =
        "PA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAQACAQDAQCQMBYIBEFAWDANBYHRAEISCMKBKFQXDAMRUGY4DUPB6IBZGM";

    SignerKey.Ed25519SignedPayload ed25519SignedPayload =
        new SignerKey.Ed25519SignedPayload(publicKeyBytes, payload);
    SignerKey signerKey = SignerKey.fromEd25519SignedPayload(ed25519SignedPayload);

    assertEquals(strkey, signerKey.getEncodedSignerKey());

    // Test conversion back to Ed25519SignedPayload
    SignerKey.Ed25519SignedPayload convertedBack = signerKey.toEd25519SignedPayload();
    assertArrayEquals(publicKeyBytes, convertedBack.getEd25519PublicKey());
    assertArrayEquals(payload, convertedBack.getPayload());
    assertEquals(accountId, convertedBack.getEncodedEd25519PublicKey());
  }

  @Test
  public void testEd25519SignedPayloadXdrConversion() {
    String accountId = "GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVSGZ";
    byte[] payload =
        Util.hexToBytes("0102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f20");

    SignerKey signerKey = SignerKey.fromEd25519SignedPayload(accountId, payload);
    org.stellar.sdk.xdr.SignerKey xdrObj = signerKey.toXdr();

    assertEquals(
        org.stellar.sdk.xdr.SignerKeyType.SIGNER_KEY_TYPE_ED25519_SIGNED_PAYLOAD,
        xdrObj.getDiscriminant());
    assertNotNull(xdrObj.getEd25519SignedPayload());
    assertArrayEquals(
        StrKey.decodeEd25519PublicKey(accountId),
        xdrObj.getEd25519SignedPayload().getEd25519().getUint256());
    assertArrayEquals(payload, xdrObj.getEd25519SignedPayload().getPayload());

    SignerKey.Ed25519SignedPayload ed25519SignedPayload = signerKey.toEd25519SignedPayload();
    assertEquals(accountId, ed25519SignedPayload.getEncodedEd25519PublicKey());
    assertArrayEquals(payload, ed25519SignedPayload.getPayload());

    // Test round-trip conversion
    SignerKey reconstructed = SignerKey.fromXdr(xdrObj);
    assertEquals(signerKey, reconstructed);
  }

  @Test
  public void testToEd25519SignedPayloadWithWrongType() {
    byte[] signerKeyData =
        Util.hexToBytes("85ba7b65eb928372b2f5c45a24c28462c68aa6048664672478360ceb9a571357");
    SignerKey signerKey = new SignerKey(signerKeyData, SignerKeyType.SIGNER_KEY_TYPE_HASH_X);

    try {
      signerKey.toEd25519SignedPayload();
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertTrue(
          e.getMessage().contains("SignerKey type must be SIGNER_KEY_TYPE_ED25519_SIGNED_PAYLOAD"));
    }
  }

  @Test
  public void testEd25519SignedPayloadConstructorValidation() {
    byte[] publicKey = new byte[32];
    byte[] validPayload = new byte[64];
    byte[] invalidPayload = new byte[65]; // Too large

    // Valid construction
    SignerKey.Ed25519SignedPayload validSignedPayload =
        new SignerKey.Ed25519SignedPayload(publicKey, validPayload);
    assertArrayEquals(publicKey, validSignedPayload.getEd25519PublicKey());
    assertArrayEquals(validPayload, validSignedPayload.getPayload());

    // Invalid construction - payload too large
    try {
      new SignerKey.Ed25519SignedPayload(publicKey, invalidPayload);
      fail("Expected IllegalArgumentException for payload too large");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("Invalid payload length, must be less than 64"));
    }
  }

  @Test
  public void testFromEncodedSignerKeyInvalid() {
    try {
      SignerKey.fromEncodedSignerKey("INVALID_KEY");
      fail("Expected IllegalArgumentException for invalid signer key");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("Invalid encoded signer key"));
    }
  }

  @Test
  public void testFromEncodedSignerKeyWithValidKeys() {
    // Test with different types of valid keys
    String ed25519Key = "GCC3U63F5OJIG4VS6XCFUJGCQRRMNCVGASDGIZZEPA3AZ242K4JVPIYV";
    String preAuthTxKey = "TCC3U63F5OJIG4VS6XCFUJGCQRRMNCVGASDGIZZEPA3AZ242K4JVOVKE";
    String sha256HashKey = "XCC3U63F5OJIG4VS6XCFUJGCQRRMNCVGASDGIZZEPA3AZ242K4JVPRP5";

    SignerKey ed25519SignerKey = SignerKey.fromEncodedSignerKey(ed25519Key);
    assertEquals(SignerKeyType.SIGNER_KEY_TYPE_ED25519, ed25519SignerKey.getType());

    SignerKey preAuthTxSignerKey = SignerKey.fromEncodedSignerKey(preAuthTxKey);
    assertEquals(SignerKeyType.SIGNER_KEY_TYPE_PRE_AUTH_TX, preAuthTxSignerKey.getType());

    SignerKey sha256HashSignerKey = SignerKey.fromEncodedSignerKey(sha256HashKey);
    assertEquals(SignerKeyType.SIGNER_KEY_TYPE_HASH_X, sha256HashSignerKey.getType());
  }

  @Test
  public void testPreAuthTxFromTransaction() {
    // Create a simple test transaction
    Network network = Network.TESTNET;
    KeyPair source = KeyPair.random();
    Account account = new Account(source.getAccountId(), 1L);
    Transaction transaction =
        new TransactionBuilder(account, network)
            .addOperation(BumpSequenceOperation.builder().bumpTo(123).build())
            .setTimeout(180)
            .setBaseFee(Transaction.MIN_BASE_FEE)
            .build();

    SignerKey preAuthTxSignerKey = SignerKey.fromPreAuthTx(transaction);
    assertEquals(SignerKeyType.SIGNER_KEY_TYPE_PRE_AUTH_TX, preAuthTxSignerKey.getType());
    assertArrayEquals(transaction.hash(), preAuthTxSignerKey.getKey());
  }

  @Test
  public void testEqualsAndHashCode() {
    byte[] keyData =
        Util.hexToBytes("85ba7b65eb928372b2f5c45a24c28462c68aa6048664672478360ceb9a571357");

    SignerKey signerKey1 = new SignerKey(keyData, SignerKeyType.SIGNER_KEY_TYPE_ED25519);
    SignerKey signerKey2 = new SignerKey(keyData, SignerKeyType.SIGNER_KEY_TYPE_ED25519);
    SignerKey signerKey3 = new SignerKey(keyData, SignerKeyType.SIGNER_KEY_TYPE_PRE_AUTH_TX);

    assertEquals(signerKey1, signerKey2);
    assertEquals(signerKey1.hashCode(), signerKey2.hashCode());
    assertNotEquals(signerKey1, signerKey3);
    assertNotEquals(signerKey1.hashCode(), signerKey3.hashCode());
  }
}
