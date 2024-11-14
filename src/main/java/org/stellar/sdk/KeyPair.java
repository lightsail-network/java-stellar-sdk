package org.stellar.sdk;

import static java.lang.System.arraycopy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;
import java.util.Objects;
import lombok.NonNull;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jetbrains.annotations.Nullable;
import org.stellar.sdk.exception.UnexpectedException;
import org.stellar.sdk.xdr.AccountID;
import org.stellar.sdk.xdr.DecoratedSignature;
import org.stellar.sdk.xdr.PublicKeyType;
import org.stellar.sdk.xdr.SignatureHint;
import org.stellar.sdk.xdr.SignerKey;
import org.stellar.sdk.xdr.SignerKeyType;
import org.stellar.sdk.xdr.Uint256;
import org.stellar.sdk.xdr.XdrDataOutputStream;

/** Holds a Stellar keypair. */
public class KeyPair {
  @NonNull private final Ed25519PublicKeyParameters publicKey;
  @Nullable private final Ed25519PrivateKeyParameters privateKey;

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  /**
   * Creates a new KeyPair from the given public and private keys.
   *
   * @param publicKey The public key for this KeyPair.
   * @param privateKey The private key for this KeyPair or null if you want a public key only
   */
  private KeyPair(
      @NonNull Ed25519PublicKeyParameters publicKey,
      @Nullable Ed25519PrivateKeyParameters privateKey) {
    this.publicKey = publicKey;
    this.privateKey = privateKey;
  }

  /** Returns true if this Keypair is capable of signing */
  public boolean canSign() {
    return privateKey != null;
  }

  /**
   * Creates a new Stellar KeyPair from a strkey encoded Stellar secret seed.
   *
   * @param seed Char array containing strkey encoded Stellar secret seed.
   * @return {@link KeyPair}
   */
  public static KeyPair fromSecretSeed(char[] seed) {
    byte[] decoded = StrKey.decodeEd25519SecretSeed(seed);
    return fromSecretSeed(decoded);
  }

  /**
   * <strong>Insecure</strong> Creates a new Stellar KeyPair from a strkey encoded Stellar secret
   * seed. This method is <u>insecure</u>. Use only if you are aware of security implications.
   *
   * @param seed The strkey encoded Stellar secret seed.
   * @return {@link KeyPair}
   * @see <a href=
   *     "http://docs.oracle.com/javase/1.5.0/docs/guide/security/jce/JCERefGuide.html#PBEEx"
   *     target="_blank">Using Password-Based Encryption</a>
   */
  public static KeyPair fromSecretSeed(String seed) {
    char[] charSeed = seed.toCharArray();
    byte[] decoded = StrKey.decodeEd25519SecretSeed(charSeed);
    KeyPair keypair = fromSecretSeed(decoded);
    Arrays.fill(charSeed, '\0');
    return keypair;
  }

  /**
   * Creates a new Stellar keypair from a raw 32 byte secret seed.
   *
   * @param seed The 32 byte secret seed.
   * @return {@link KeyPair}
   */
  public static KeyPair fromSecretSeed(byte[] seed) {
    Ed25519PrivateKeyParameters privateKey = new Ed25519PrivateKeyParameters(seed, 0);
    Ed25519PublicKeyParameters publicKey = privateKey.generatePublicKey();
    return new KeyPair(publicKey, privateKey);
  }

  /**
   * Creates a new Stellar KeyPair from a strkey encoded Stellar account ID.
   *
   * @param accountId The strkey encoded Stellar account ID.
   * @return {@link KeyPair}
   */
  public static KeyPair fromAccountId(String accountId) {
    byte[] decoded = StrKey.decodeEd25519PublicKey(accountId);
    return fromPublicKey(decoded);
  }

  /**
   * Creates a new Stellar keypair from a 32 byte address.
   *
   * @param publicKey The 32 byte public key.
   * @return {@link KeyPair}
   */
  public static KeyPair fromPublicKey(byte[] publicKey) {
    Ed25519PublicKeyParameters ed25519PublicKeyParameters;
    try {
      ed25519PublicKeyParameters = new Ed25519PublicKeyParameters(publicKey, 0);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Public key is invalid", e);
    }
    return new KeyPair(ed25519PublicKeyParameters, null);
  }

  /**
   * Finds the KeyPair for the path m/44'/148'/accountNumber' using the method described in <a href=
   * "https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0005.md">SEP-0005</a>.
   *
   * <p>You can generate a BIP39 seed using a library like <a
   * href="https://github.com/lightsail-network/mnemonic4j">mnemonic4j</a>.
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
      throw new UnexpectedException(e);
    }
  }

  /**
   * Generates a random Stellar keypair.
   *
   * @return a random Stellar keypair.
   */
  public static KeyPair random() {
    Ed25519PrivateKeyParameters privateKey = new Ed25519PrivateKeyParameters(new SecureRandom());
    Ed25519PublicKeyParameters publicKey = privateKey.generatePublicKey();
    return new KeyPair(publicKey, privateKey);
  }

  /** Returns the human-readable account ID encoded in strkey. */
  public String getAccountId() {
    return StrKey.encodeEd25519PublicKey(getPublicKey());
  }

  /**
   * Returns the human-readable secret seed encoded in strkey.
   *
   * <p>WARNING: This method returns the secret seed of the keypair. The secret seed should be
   * handled with care and not be exposed to anyone else. Exposing the secret seed can lead to the
   * theft of the account.
   *
   * @return char[] The secret seed of the keypair. If the keypair was created without a secret
   *     seed, this method will return null.
   */
  public char[] getSecretSeed() {
    if (privateKey == null) {
      return null;
    }
    return StrKey.encodeEd25519SecretSeed(privateKey.getEncoded());
  }

  /** Returns the raw 32 byte public key. */
  public byte[] getPublicKey() {
    return publicKey.getEncoded();
  }

  /** Returns the signature hint for this keypair. */
  public SignatureHint getSignatureHint() {
    try {
      ByteArrayOutputStream publicKeyBytesStream = new ByteArrayOutputStream();
      XdrDataOutputStream xdrOutputStream = new XdrDataOutputStream(publicKeyBytesStream);
      this.getXdrPublicKey().encode(xdrOutputStream);
      byte[] publicKeyBytes = publicKeyBytesStream.toByteArray();
      byte[] signatureHintBytes =
          Arrays.copyOfRange(publicKeyBytes, publicKeyBytes.length - 4, publicKeyBytes.length);
      SignatureHint signatureHint = new SignatureHint();
      signatureHint.setSignatureHint(signatureHintBytes);
      return signatureHint;
    } catch (IOException e) {
      throw new UnexpectedException(e);
    }
  }

  /** Returns the XDR {@link org.stellar.sdk.xdr.PublicKey} for this keypair. */
  public org.stellar.sdk.xdr.PublicKey getXdrPublicKey() {
    org.stellar.sdk.xdr.PublicKey publicKey = new org.stellar.sdk.xdr.PublicKey();
    publicKey.setDiscriminant(PublicKeyType.PUBLIC_KEY_TYPE_ED25519);
    Uint256 uint256 = new Uint256();
    uint256.setUint256(getPublicKey());
    publicKey.setEd25519(uint256);
    return publicKey;
  }

  /** Returns the XDR {@link AccountID} for this keypair. */
  public AccountID getXdrAccountId() {
    AccountID accountID = new AccountID();
    accountID.setAccountID(getXdrPublicKey());
    return accountID;
  }

  /** Returns the XDR {@link SignerKey} for this keypair. */
  public SignerKey getXdrSignerKey() {
    SignerKey signerKey = new SignerKey();
    signerKey.setDiscriminant(SignerKeyType.SIGNER_KEY_TYPE_ED25519);
    Uint256 uint256 = new Uint256();
    uint256.setUint256(getPublicKey());
    signerKey.setEd25519(uint256);
    return signerKey;
  }

  /**
   * Creates a new KeyPair from an XDR {@link org.stellar.sdk.xdr.PublicKey}.
   *
   * @param key The XDR {@link org.stellar.sdk.xdr.PublicKey} object.
   * @return KeyPair
   */
  public static KeyPair fromXdrPublicKey(org.stellar.sdk.xdr.PublicKey key) {
    return KeyPair.fromPublicKey(key.getEd25519().getUint256());
  }

  /**
   * Creates a new KeyPair from an XDR {@link SignerKey}.
   *
   * @param key The XDR {@link SignerKey} object.
   * @return KeyPair
   */
  public static KeyPair fromXdrSignerKey(SignerKey key) {
    return KeyPair.fromPublicKey(key.getEd25519().getUint256());
  }

  /**
   * Sign the provided data with the keypair's private key.
   *
   * @param data The data to sign.
   * @return signed bytes, null if the private key for this keypair is null.
   * @throws IllegalStateException if the private key for this keypair is null.
   */
  public byte[] sign(byte[] data) {
    if (privateKey == null) {
      throw new IllegalStateException(
          "KeyPair does not contain secret key. Use KeyPair.fromSecretSeed method to create a new KeyPair with a secret key.");
    }
    Ed25519Signer signer = new Ed25519Signer();
    signer.init(true, privateKey);
    signer.update(data, 0, data.length);
    return signer.generateSignature();
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
   * the <a href=
   * "https://github.com/stellar/stellar-protocol/blob/master/core/cap-0040.md#signature-hint">CAP-40
   * Signature spec</a> {@link DecoratedSignature}.
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
   */
  public boolean verify(byte[] data, byte[] signature) {
    Ed25519Signer verifier = new Ed25519Signer();
    verifier.init(false, publicKey);
    verifier.update(data, 0, data.length);
    return verifier.verifySignature(signature);
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    KeyPair keyPair = (KeyPair) object;
    if (!Arrays.equals(publicKey.getEncoded(), keyPair.publicKey.getEncoded())) {
      return false;
    }
    // privateKey can be null
    return Arrays.equals(
        privateKey == null ? null : privateKey.getEncoded(),
        keyPair.privateKey == null ? null : keyPair.privateKey.getEncoded());
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        Arrays.hashCode(publicKey.getEncoded()),
        privateKey == null ? null : Arrays.hashCode(privateKey.getEncoded()));
  }
}
