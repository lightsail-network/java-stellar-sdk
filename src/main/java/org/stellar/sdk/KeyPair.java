package org.stellar.sdk;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.System.arraycopy;

import com.google.common.base.Objects;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Arrays;
import net.i2p.crypto.eddsa.EdDSAEngine;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.KeyPairGenerator;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveSpec;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAPrivateKeySpec;
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec;
import org.stellar.sdk.xdr.DecoratedSignature;
import org.stellar.sdk.xdr.PublicKey;
import org.stellar.sdk.xdr.PublicKeyType;
import org.stellar.sdk.xdr.SignatureHint;
import org.stellar.sdk.xdr.SignerKey;
import org.stellar.sdk.xdr.SignerKeyType;
import org.stellar.sdk.xdr.Uint256;
import org.stellar.sdk.xdr.XdrDataOutputStream;

/** Holds a Stellar keypair. */
public class KeyPair {

  private static final EdDSANamedCurveSpec ed25519 = EdDSANamedCurveTable.ED_25519_CURVE_SPEC;

  private final EdDSAPublicKey mPublicKey;
  private final EdDSAPrivateKey mPrivateKey;

  /**
   * Creates a new KeyPair without a private key. Useful to simply verify a signature from a given
   * public address.
   *
   * @param publicKey
   */
  public KeyPair(EdDSAPublicKey publicKey) {
    this(publicKey, null);
  }

  /**
   * Creates a new KeyPair from the given public and private keys.
   *
   * @param publicKey
   * @param privateKey
   */
  public KeyPair(EdDSAPublicKey publicKey, EdDSAPrivateKey privateKey) {
    mPublicKey = checkNotNull(publicKey, "publicKey cannot be null");
    mPrivateKey = privateKey;
  }

  /** Returns true if this Keypair is capable of signing */
  public boolean canSign() {
    return mPrivateKey != null;
  }

  /**
   * Creates a new Stellar KeyPair from a strkey encoded Stellar secret seed.
   *
   * @param seed Char array containing strkey encoded Stellar secret seed.
   * @return {@link KeyPair}
   */
  public static KeyPair fromSecretSeed(char[] seed) {
    byte[] decoded = StrKey.decodeStellarSecretSeed(seed);
    KeyPair keypair = fromSecretSeed(decoded);
    return keypair;
  }

  /**
   * <strong>Insecure</strong> Creates a new Stellar KeyPair from a strkey encoded Stellar secret
   * seed. This method is <u>insecure</u>. Use only if you are aware of security implications.
   *
   * @see <a
   *     href="http://docs.oracle.com/javase/1.5.0/docs/guide/security/jce/JCERefGuide.html#PBEEx"
   *     target="_blank">Using Password-Based Encryption</a>
   * @param seed The strkey encoded Stellar secret seed.
   * @return {@link KeyPair}
   */
  public static KeyPair fromSecretSeed(String seed) {
    char[] charSeed = seed.toCharArray();
    byte[] decoded = StrKey.decodeStellarSecretSeed(charSeed);
    KeyPair keypair = fromSecretSeed(decoded);
    Arrays.fill(charSeed, ' ');
    return keypair;
  }

  /**
   * Creates a new Stellar keypair from a raw 32 byte secret seed.
   *
   * @param seed The 32 byte secret seed.
   * @return {@link KeyPair}
   */
  public static KeyPair fromSecretSeed(byte[] seed) {
    EdDSAPrivateKeySpec privKeySpec = new EdDSAPrivateKeySpec(seed, ed25519);
    EdDSAPublicKeySpec publicKeySpec =
        new EdDSAPublicKeySpec(privKeySpec.getA().toByteArray(), ed25519);
    return new KeyPair(new EdDSAPublicKey(publicKeySpec), new EdDSAPrivateKey(privKeySpec));
  }

  /**
   * Creates a new Stellar KeyPair from a strkey encoded Stellar account ID.
   *
   * @param accountId The strkey encoded Stellar account ID.
   * @return {@link KeyPair}
   */
  public static KeyPair fromAccountId(String accountId) {
    byte[] decoded = StrKey.decodeStellarAccountId(accountId);
    return fromPublicKey(decoded);
  }

  /**
   * Creates a new Stellar keypair from a 32 byte address.
   *
   * @param publicKey The 32 byte public key.
   * @return {@link KeyPair}
   */
  public static KeyPair fromPublicKey(byte[] publicKey) {
    EdDSAPublicKeySpec publicKeySpec;
    try {
      publicKeySpec = new EdDSAPublicKeySpec(publicKey, ed25519);
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("Public key is invalid");
    }
    return new KeyPair(new EdDSAPublicKey(publicKeySpec));
  }

  /**
   * Finds the KeyPair for the path m/44'/148'/accountNumber' using the method described in <a
   * href="https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0005.md">SEP-0005</a>.
   *
   * @param bip39Seed The output of BIP0039
   * @param accountNumber The number of the account
   * @return KeyPair with secret
   */
  public static KeyPair fromBip39Seed(byte[] bip39Seed, int accountNumber) {
    try {
      return KeyPair.fromSecretSeed(
          SLIP10.deriveEd25519PrivateKey(bip39Seed, 44, 148, accountNumber));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Generates a random Stellar keypair.
   *
   * @return a random Stellar keypair.
   */
  public static KeyPair random() {
    java.security.KeyPair keypair = new KeyPairGenerator().generateKeyPair();
    return new KeyPair(
        (EdDSAPublicKey) keypair.getPublic(), (EdDSAPrivateKey) keypair.getPrivate());
  }

  /** Returns the human readable account ID encoded in strkey. */
  public String getAccountId() {
    return StrKey.encodeStellarAccountId(mPublicKey.getAbyte());
  }

  /** Returns the human readable secret seed encoded in strkey. */
  public char[] getSecretSeed() {
    return StrKey.encodeStellarSecretSeed(mPrivateKey.getSeed());
  }

  public byte[] getPublicKey() {
    return mPublicKey.getAbyte();
  }

  public SignatureHint getSignatureHint() {
    try {
      ByteArrayOutputStream publicKeyBytesStream = new ByteArrayOutputStream();
      XdrDataOutputStream xdrOutputStream = new XdrDataOutputStream(publicKeyBytesStream);
      PublicKey.encode(xdrOutputStream, this.getXdrPublicKey());
      byte[] publicKeyBytes = publicKeyBytesStream.toByteArray();
      byte[] signatureHintBytes =
          Arrays.copyOfRange(publicKeyBytes, publicKeyBytes.length - 4, publicKeyBytes.length);

      SignatureHint signatureHint = new SignatureHint();
      signatureHint.setSignatureHint(signatureHintBytes);
      return signatureHint;
    } catch (IOException e) {
      throw new AssertionError(e);
    }
  }

  public PublicKey getXdrPublicKey() {
    PublicKey publicKey = new PublicKey();
    publicKey.setDiscriminant(PublicKeyType.PUBLIC_KEY_TYPE_ED25519);
    Uint256 uint256 = new Uint256();
    uint256.setUint256(getPublicKey());
    publicKey.setEd25519(uint256);
    return publicKey;
  }

  public SignerKey getXdrSignerKey() {
    SignerKey signerKey = new SignerKey();
    signerKey.setDiscriminant(SignerKeyType.SIGNER_KEY_TYPE_ED25519);
    Uint256 uint256 = new Uint256();
    uint256.setUint256(getPublicKey());
    signerKey.setEd25519(uint256);
    return signerKey;
  }

  public static KeyPair fromXdrPublicKey(PublicKey key) {
    return KeyPair.fromPublicKey(key.getEd25519().getUint256());
  }

  public static KeyPair fromXdrSignerKey(SignerKey key) {
    return KeyPair.fromPublicKey(key.getEd25519().getUint256());
  }

  /**
   * Sign the provided data with the keypair's private key.
   *
   * @param data The data to sign.
   * @return signed bytes, null if the private key for this keypair is null.
   */
  public byte[] sign(byte[] data) {
    if (mPrivateKey == null) {
      throw new RuntimeException(
          "KeyPair does not contain secret key. Use KeyPair.fromSecretSeed method to create a new KeyPair with a secret key.");
    }
    try {
      Signature sgr = new EdDSAEngine(MessageDigest.getInstance("SHA-512"));
      sgr.initSign(mPrivateKey);
      sgr.update(data);
      return sgr.sign();
    } catch (GeneralSecurityException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Sign the provided data with the keypair's private key and returns {@link DecoratedSignature}.
   *
   * @param data the data to sign
   * @return DecoratedSignature
   */
  public DecoratedSignature signDecorated(byte[] data) {
    byte[] signatureBytes = this.sign(data);

    org.stellar.sdk.xdr.Signature signature = new org.stellar.sdk.xdr.Signature();
    signature.setSignature(signatureBytes);

    DecoratedSignature decoratedSignature = new DecoratedSignature();
    decoratedSignature.setHint(this.getSignatureHint());
    decoratedSignature.setSignature(signature);
    return decoratedSignature;
  }

  /**
   * Sign the provided payload data for payload signer where the input is the data being signed. Per
   * the <a
   * href="https://github.com/stellar/stellar-protocol/blob/master/core/cap-0040.md#signature-hint"
   * CAP-40 Signature spec</a> {@link DecoratedSignature}.
   *
   * @param signerPayload the payload signers raw data to sign
   * @return DecoratedSignature
   */
  public DecoratedSignature signPayloadDecorated(byte[] signerPayload) {
    DecoratedSignature payloadSignature = signDecorated(signerPayload);

    byte[] hint = new byte[4];

    // copy the last four bytes of the payload into the new hint
    if (signerPayload.length >= hint.length) {
      arraycopy(signerPayload, signerPayload.length - hint.length, hint, 0, hint.length);
    } else {
      arraycopy(signerPayload, 0, hint, 0, signerPayload.length);
    }

    // XOR the new hint with this keypair's public key hint
    for (int i = 0; i < hint.length; i++) {
      hint[i] ^= payloadSignature.getHint().getSignatureHint()[i];
    }
    payloadSignature.getHint().setSignatureHint(hint);
    return payloadSignature;
  }

  /**
   * Verify the provided data and signature match this keypair's public key.
   *
   * @param data The data that was signed.
   * @param signature The signature.
   * @return True if they match, false otherwise.
   * @throws RuntimeException
   */
  public boolean verify(byte[] data, byte[] signature) {
    try {
      Signature sgr = new EdDSAEngine(MessageDigest.getInstance("SHA-512"));
      sgr.initVerify(mPublicKey);
      sgr.update(data);
      return sgr.verify(signature);
    } catch (SignatureException e) {
      return false;
    } catch (GeneralSecurityException e) {
      throw new RuntimeException(e);
    }
  }

  public int hashCode() {
    return Objects.hashCode(this.mPrivateKey, this.mPublicKey);
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof KeyPair)) {
      return false;
    }

    KeyPair other = (KeyPair) object;
    return Objects.equal(this.mPrivateKey, other.mPrivateKey)
        && this.mPublicKey.equals(other.mPublicKey);
  }
}
