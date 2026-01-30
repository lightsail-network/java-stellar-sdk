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
    byte[] emptyPayload = new byte[0]; // Too small
    byte[] invalidPayload = new byte[65]; // Too large

    // Valid construction
    SignerKey.Ed25519SignedPayload validSignedPayload =
        new SignerKey.Ed25519SignedPayload(publicKey, validPayload);
    assertArrayEquals(publicKey, validSignedPayload.getEd25519PublicKey());
    assertArrayEquals(validPayload, validSignedPayload.getPayload());

    // Invalid construction - payload too small (empty)
    try {
      new SignerKey.Ed25519SignedPayload(publicKey, emptyPayload);
      fail("Expected IllegalArgumentException for empty payload");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("must be between 1 and 64"));
    }

    // Invalid construction - payload too large
    try {
      new SignerKey.Ed25519SignedPayload(publicKey, invalidPayload);
      fail("Expected IllegalArgumentException for payload too large");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("must be between 1 and 64"));
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

  @Test
  public void testToEd25519SignedPayloadKeyTooShort() {
    // Key length < 40 bytes
    byte[] shortKey = new byte[39];
    SignerKey signerKey =
        new SignerKey(shortKey, SignerKeyType.SIGNER_KEY_TYPE_ED25519_SIGNED_PAYLOAD);

    try {
      signerKey.toEd25519SignedPayload();
      fail("Expected IllegalArgumentException for key too short");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("must be between 40 and 100 bytes"));
    }
  }

  @Test
  public void testToEd25519SignedPayloadKeyTooLong() {
    // Key length > 100 bytes
    byte[] longKey = new byte[101];
    SignerKey signerKey =
        new SignerKey(longKey, SignerKeyType.SIGNER_KEY_TYPE_ED25519_SIGNED_PAYLOAD);

    try {
      signerKey.toEd25519SignedPayload();
      fail("Expected IllegalArgumentException for key too long");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("must be between 40 and 100 bytes"));
    }
  }

  @Test
  public void testToEd25519SignedPayloadNegativePayloadLength() {
    // 32 bytes public key + 4 bytes negative length + 4 bytes padding = 40 bytes
    byte[] key = new byte[40];
    // Set payload length to -1 (0xFFFFFFFF in big-endian)
    key[32] = (byte) 0xFF;
    key[33] = (byte) 0xFF;
    key[34] = (byte) 0xFF;
    key[35] = (byte) 0xFF;

    SignerKey signerKey = new SignerKey(key, SignerKeyType.SIGNER_KEY_TYPE_ED25519_SIGNED_PAYLOAD);

    try {
      signerKey.toEd25519SignedPayload();
      fail("Expected IllegalArgumentException for negative payload length");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("must be between 1 and 64"));
    }
  }

  @Test
  public void testToEd25519SignedPayloadZeroPayloadLength() {
    // 32 bytes public key + 4 bytes zero length + 4 bytes padding = 40 bytes
    byte[] key = new byte[40];
    // payload length is already 0 (default)

    SignerKey signerKey = new SignerKey(key, SignerKeyType.SIGNER_KEY_TYPE_ED25519_SIGNED_PAYLOAD);

    try {
      signerKey.toEd25519SignedPayload();
      fail("Expected IllegalArgumentException for zero payload length");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("must be between 1 and 64"));
    }
  }

  @Test
  public void testToEd25519SignedPayloadPayloadLengthTooLarge() {
    // 32 bytes public key + 4 bytes length (65) + padding = need more space
    byte[] key = new byte[100];
    // Set payload length to 65 (0x00000041 in big-endian)
    key[32] = 0;
    key[33] = 0;
    key[34] = 0;
    key[35] = 65;

    SignerKey signerKey = new SignerKey(key, SignerKeyType.SIGNER_KEY_TYPE_ED25519_SIGNED_PAYLOAD);

    try {
      signerKey.toEd25519SignedPayload();
      fail("Expected IllegalArgumentException for payload length > 64");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("must be between 1 and 64"));
    }
  }

  @Test
  public void testToEd25519SignedPayloadLengthMismatch() {
    // 32 bytes public key + 4 bytes length (1) + 1 byte payload + 3 bytes padding = 40 bytes
    // But we provide 44 bytes total
    byte[] key = new byte[44];
    // Set payload length to 1
    key[35] = 1;

    SignerKey signerKey = new SignerKey(key, SignerKeyType.SIGNER_KEY_TYPE_ED25519_SIGNED_PAYLOAD);

    try {
      signerKey.toEd25519SignedPayload();
      fail("Expected IllegalArgumentException for length mismatch");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("expected 40 bytes"));
    }
  }

  @Test
  public void testToEd25519SignedPayloadNonZeroPadding() {
    // 32 bytes public key + 4 bytes length (1) + 1 byte payload + 3 bytes padding = 40 bytes
    byte[] key = new byte[40];
    // Set payload length to 1
    key[35] = 1;
    // Set payload byte
    key[36] = 0x42;
    // Set non-zero padding (should be zeros)
    key[37] = 0x01; // This should be 0

    SignerKey signerKey = new SignerKey(key, SignerKeyType.SIGNER_KEY_TYPE_ED25519_SIGNED_PAYLOAD);

    try {
      signerKey.toEd25519SignedPayload();
      fail("Expected IllegalArgumentException for non-zero padding");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("padding bytes must be zero"));
    }
  }

  @Test
  public void testToEd25519SignedPayloadValidMinPayload() {
    // 32 bytes public key + 4 bytes length (1) + 1 byte payload + 3 bytes zero padding = 40 bytes
    byte[] key = new byte[40];
    // Set payload length to 1 (big-endian)
    key[35] = 1;
    // Set payload byte
    key[36] = 0x42;
    // Padding bytes key[37], key[38], key[39] are already 0

    SignerKey signerKey = new SignerKey(key, SignerKeyType.SIGNER_KEY_TYPE_ED25519_SIGNED_PAYLOAD);
    SignerKey.Ed25519SignedPayload result = signerKey.toEd25519SignedPayload();

    assertEquals(1, result.getPayload().length);
    assertEquals(0x42, result.getPayload()[0]);
  }

  @Test
  public void testToEd25519SignedPayloadValidMaxPayload() {
    // 32 bytes public key + 4 bytes length (64) + 64 bytes payload + 0 bytes padding = 100 bytes
    byte[] key = new byte[100];
    // Set payload length to 64 (big-endian)
    key[35] = 64;
    // Fill payload with test data
    for (int i = 0; i < 64; i++) {
      key[36 + i] = (byte) i;
    }

    SignerKey signerKey = new SignerKey(key, SignerKeyType.SIGNER_KEY_TYPE_ED25519_SIGNED_PAYLOAD);
    SignerKey.Ed25519SignedPayload result = signerKey.toEd25519SignedPayload();

    assertEquals(64, result.getPayload().length);
    for (int i = 0; i < 64; i++) {
      assertEquals((byte) i, result.getPayload()[i]);
    }
  }
}
