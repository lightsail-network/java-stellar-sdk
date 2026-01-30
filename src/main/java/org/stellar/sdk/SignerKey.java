package org.stellar.sdk;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import lombok.Value;
import org.stellar.sdk.exception.UnexpectedException;
import org.stellar.sdk.xdr.SignerKeyType;
import org.stellar.sdk.xdr.Uint256;

/**
 * Represents a Stellar signer key used for transaction authorization.
 *
 * <p>This class supports four types of signers as defined in the Stellar protocol:
 *
 * <ul>
 *   <li><strong>ED25519</strong> - Standard Ed25519 public key signer
 *   <li><strong>PRE_AUTH_TX</strong> - Pre-authorized transaction hash signer
 *   <li><strong>HASH_X</strong> - SHA-256 hash preimage signer
 *   <li><strong>ED25519_SIGNED_PAYLOAD</strong> - Ed25519 signed payload signer (CAP-40)
 * </ul>
 *
 * <p>The Ed25519 Signed Payload Signer (introduced in CAP-40) is particularly useful for
 * multi-party contracts like payment channels, as it allows all parties to share a set of
 * transactions for signing and guarantees that if one transaction is signed and submitted,
 * information is revealed that allows all other transactions in the set to be authorized.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * // Create an Ed25519 signer
 * SignerKey ed25519Signer = SignerKey.fromEd25519PublicKey("GCEZWKCA5VLDNRLN3RPRJMRZOX3Z6G5CHCGSNFHEYVXM3XOJMDS674JZ");
 *
 * // Create a signed payload signer
 * byte[] payload = "transaction_hash".getBytes();
 * Ed25519SignedPayload signedPayload = new Ed25519SignedPayload(publicKeyBytes, payload);
 * SignerKey payloadSigner = SignerKey.fromEd25519SignedPayload(signedPayload);
 * }</pre>
 *
 * @see <a href="https://github.com/stellar/stellar-protocol/blob/master/core/cap-0040.md">CAP-40:
 *     Ed25519 Signed Payload Signer</a>
 */
@Value
public class SignerKey {
  /**
   * Maximum payload length for Ed25519 signed payload signers. As defined in CAP-40, the payload
   * has a maximum size of 64 bytes.
   */
  public static final int SIGNED_PAYLOAD_MAX_PAYLOAD_LENGTH = 64;

  /**
   * The raw key bytes for this signer. The format depends on the signer type:
   *
   * <ul>
   *   <li>ED25519: 32-byte public key
   *   <li>PRE_AUTH_TX: 32-byte transaction hash
   *   <li>HASH_X: 32-byte SHA-256 hash
   *   <li>ED25519_SIGNED_PAYLOAD: Variable length encoded payload with public key
   * </ul>
   */
  byte[] key;

  /** The type of this signer key. */
  SignerKeyType type;

  /**
   * Gets the encoded string representation of this signer key.
   *
   * @return The StrKey-encoded representation of this signer key
   * @throws IllegalArgumentException if the signer key type is unknown
   */
  public String getEncodedSignerKey() {
    switch (type) {
      case SIGNER_KEY_TYPE_ED25519:
        return StrKey.encodeEd25519PublicKey(key);
      case SIGNER_KEY_TYPE_PRE_AUTH_TX:
        return StrKey.encodePreAuthTx(key);
      case SIGNER_KEY_TYPE_HASH_X:
        return StrKey.encodeSha256Hash(key);
      case SIGNER_KEY_TYPE_ED25519_SIGNED_PAYLOAD:
        return StrKey.encodeSignedPayload(key);
      default:
        throw new IllegalArgumentException("Unknown SignerKeyType: " + type);
    }
  }

  /**
   * Creates a SignerKey from an encoded signer key string.
   *
   * <p>This method automatically detects the signer key type based on the encoded string format and
   * creates the appropriate SignerKey instance.
   *
   * @param encodedSignerKey The StrKey-encoded signer key string
   * @return A new SignerKey instance
   * @throws IllegalArgumentException if the encoded signer key is invalid
   */
  public static SignerKey fromEncodedSignerKey(String encodedSignerKey) {
    if (StrKey.isValidEd25519PublicKey(encodedSignerKey)) {
      return fromEd25519PublicKey(encodedSignerKey);
    } else if (StrKey.isValidPreAuthTx(encodedSignerKey)) {
      return fromPreAuthTx(encodedSignerKey);
    } else if (StrKey.isValidSha256Hash(encodedSignerKey)) {
      return fromSha256Hash(encodedSignerKey);
    } else if (StrKey.isValidSignedPayload(encodedSignerKey)) {
      return fromEd25519SignedPayload(encodedSignerKey);
    } else {
      throw new IllegalArgumentException("Invalid encoded signer key: " + encodedSignerKey);
    }
  }

  /**
   * Creates a SignerKey from an Ed25519 public key.
   *
   * @param key The 32-byte Ed25519 public key
   * @return A new SignerKey instance of type ED25519
   */
  public static SignerKey fromEd25519PublicKey(byte[] key) {
    return new SignerKey(key, SignerKeyType.SIGNER_KEY_TYPE_ED25519);
  }

  /**
   * Creates a SignerKey from an encoded Ed25519 public key string.
   *
   * @param key The StrKey-encoded Ed25519 public key (starts with 'G')
   * @return A new SignerKey instance of type ED25519
   * @throws IllegalArgumentException if the key is not a valid Ed25519 public key
   */
  public static SignerKey fromEd25519PublicKey(String key) {
    byte[] rawKey = StrKey.decodeEd25519PublicKey(key);
    return fromEd25519PublicKey(rawKey);
  }

  /**
   * Creates a SignerKey from a pre-authorized transaction hash.
   *
   * <p>Pre-authorized transaction signers allow a transaction to be authorized by including the
   * hash of a specific transaction as a signer. This is useful for creating transactions that can
   * only be executed if a specific other transaction is also executed.
   *
   * @param key The 32-byte transaction hash
   * @return A new SignerKey instance of type PRE_AUTH_TX
   */
  public static SignerKey fromPreAuthTx(byte[] key) {
    return new SignerKey(key, SignerKeyType.SIGNER_KEY_TYPE_PRE_AUTH_TX);
  }

  /**
   * Creates a SignerKey from an encoded pre-authorized transaction hash string.
   *
   * @param key The StrKey-encoded pre-authorized transaction hash (starts with 'T')
   * @return A new SignerKey instance of type PRE_AUTH_TX
   * @throws IllegalArgumentException if the key is not a valid pre-authorized transaction hash
   */
  public static SignerKey fromPreAuthTx(String key) {
    byte[] rawKey = StrKey.decodePreAuthTx(key);
    return fromPreAuthTx(rawKey);
  }

  /**
   * Creates a SignerKey from a pre-authorized transaction.
   *
   * @param key The pre-authorized transaction
   * @return A new SignerKey instance of type PRE_AUTH_TX
   */
  public static SignerKey fromPreAuthTx(Transaction key) {
    return fromPreAuthTx(key.hash());
  }

  /**
   * Creates a SignerKey from a SHA-256 hash.
   *
   * <p>Hash-X signers allow a transaction to be authorized by revealing the preimage of a specific
   * SHA-256 hash. This is useful for creating hashlocks and other cryptographic puzzles.
   *
   * @param key The 32-byte SHA-256 hash
   * @return A new SignerKey instance of type HASH_X
   */
  public static SignerKey fromSha256Hash(byte[] key) {
    return new SignerKey(key, SignerKeyType.SIGNER_KEY_TYPE_HASH_X);
  }

  /**
   * Creates a SignerKey from an encoded SHA-256 hash string.
   *
   * @param key The StrKey-encoded SHA-256 hash (starts with 'X')
   * @return A new SignerKey instance of type HASH_X
   * @throws IllegalArgumentException if the key is not a valid SHA-256 hash
   */
  public static SignerKey fromSha256Hash(String key) {
    byte[] rawKey = StrKey.decodeSha256Hash(key);
    return fromSha256Hash(rawKey);
  }

  /**
   * Creates a SignerKey from an Ed25519 signed payload.
   *
   * <p>Ed25519 signed payload signers (CAP-40) allow a transaction to be authorized by providing a
   * signature of a specific payload using an Ed25519 key. This is particularly useful for
   * multi-party contracts where signing one transaction reveals information that allows other
   * transactions to be authorized.
   *
   * @param key The encoded payload containing the Ed25519 public key and payload
   * @return A new SignerKey instance of type ED25519_SIGNED_PAYLOAD
   */
  public static SignerKey fromEd25519SignedPayload(byte[] key) {
    return new SignerKey(key, SignerKeyType.SIGNER_KEY_TYPE_ED25519_SIGNED_PAYLOAD);
  }

  /**
   * Creates a SignerKey from an Ed25519 public key and payload.
   *
   * <p>This method encodes the Ed25519 public key and payload into the binary format required by
   * the Stellar protocol, including proper padding for the payload.
   *
   * @param ed25519PublicKey The StrKey-encoded Ed25519 public key (starts with 'G')
   * @param payload The payload to be signed (up to 64 bytes)
   * @return A new SignerKey instance of type ED25519_SIGNED_PAYLOAD
   * @throws IllegalArgumentException if the payload length exceeds the maximum allowed
   */
  public static SignerKey fromEd25519SignedPayload(String ed25519PublicKey, byte[] payload) {
    byte[] publicKeyBytes = StrKey.decodeEd25519PublicKey(ed25519PublicKey);
    return fromEd25519SignedPayload(new Ed25519SignedPayload(publicKeyBytes, payload));
  }

  /**
   * Creates a SignerKey from an Ed25519SignedPayload object.
   *
   * <p>This method encodes the Ed25519 public key and payload into the binary format required by
   * the Stellar protocol, including proper padding for the payload.
   *
   * @param ed25519SignedPayload The Ed25519SignedPayload containing the public key and payload
   * @return A new SignerKey instance of type ED25519_SIGNED_PAYLOAD
   * @throws UnexpectedException if an I/O error occurs during encoding
   */
  public static SignerKey fromEd25519SignedPayload(Ed25519SignedPayload ed25519SignedPayload) {
    int payloadLength = ed25519SignedPayload.payload.length;
    int paddingSize = (4 - payloadLength % 4) % 4;
    byte[] paddedPayload = new byte[payloadLength + paddingSize];
    System.arraycopy(ed25519SignedPayload.payload, 0, paddedPayload, 0, payloadLength);
    byte[] payloadLengthBytes = ByteBuffer.allocate(4).putInt(payloadLength).array();

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    try {
      outputStream.write(ed25519SignedPayload.ed25519PublicKey);
      outputStream.write(payloadLengthBytes);
      outputStream.write(paddedPayload);
    } catch (IOException e) {
      throw new UnexpectedException(e);
    }
    return new SignerKey(
        outputStream.toByteArray(), SignerKeyType.SIGNER_KEY_TYPE_ED25519_SIGNED_PAYLOAD);
  }

  /**
   * Creates a SignerKey from an encoded Ed25519 signed payload string.
   *
   * @param key The StrKey-encoded Ed25519 signed payload (starts with 'P')
   * @return A new SignerKey instance of type ED25519_SIGNED_PAYLOAD
   * @throws IllegalArgumentException if the key is not a valid Ed25519 signed payload
   */
  public static SignerKey fromEd25519SignedPayload(String key) {
    byte[] rawKey = StrKey.decodeSignedPayload(key);
    return fromEd25519SignedPayload(rawKey);
  }

  /**
   * Converts this SignerKey to an Ed25519SignedPayload object.
   *
   * <p>This method extracts the Ed25519 public key and payload from the encoded binary format used
   * by Ed25519 signed payload signers.
   *
   * @return An Ed25519SignedPayload object containing the public key and payload
   * @throws IllegalArgumentException if this SignerKey is not of type ED25519_SIGNED_PAYLOAD
   */
  public Ed25519SignedPayload toEd25519SignedPayload() {
    if (type != SignerKeyType.SIGNER_KEY_TYPE_ED25519_SIGNED_PAYLOAD) {
      throw new IllegalArgumentException(
          "SignerKey type must be SIGNER_KEY_TYPE_ED25519_SIGNED_PAYLOAD");
    }

    // Validate key length: min 40 bytes (32 + 4 + 4), max 100 bytes (32 + 4 + 64)
    if (key.length < 40 || key.length > 100) {
      throw new IllegalArgumentException(
          "Invalid signed payload key length, must be between 40 and 100 bytes, got " + key.length);
    }

    byte[] lengthBytes = Arrays.copyOfRange(key, 32, 36);
    int payloadLength = ByteBuffer.wrap(lengthBytes).getInt();

    // Validate payload length: must be between 1 and 64 bytes
    if (payloadLength < 1 || payloadLength > SIGNED_PAYLOAD_MAX_PAYLOAD_LENGTH) {
      throw new IllegalArgumentException(
          "Invalid payload length, must be between 1 and "
              + SIGNED_PAYLOAD_MAX_PAYLOAD_LENGTH
              + ", got "
              + payloadLength);
    }

    // Validate total length matches expected (32 + 4 + payloadLength + padding)
    int padding = (4 - payloadLength % 4) % 4;
    int expectedLength = 32 + 4 + payloadLength + padding;
    if (key.length != expectedLength) {
      throw new IllegalArgumentException(
          "Invalid signed payload key length, expected "
              + expectedLength
              + " bytes, got "
              + key.length);
    }

    // Validate padding bytes are all zeros
    for (int i = 36 + payloadLength; i < key.length; i++) {
      if (key[i] != 0) {
        throw new IllegalArgumentException(
            "Invalid signed payload key, padding bytes must be zero");
      }
    }

    byte[] publicKeyBytes = Arrays.copyOfRange(key, 0, 32);
    byte[] payload = Arrays.copyOfRange(key, 36, 36 + payloadLength);
    return new Ed25519SignedPayload(publicKeyBytes, payload);
  }

  /**
   * Converts this SignerKey to its XDR representation.
   *
   * @return The XDR representation of this signer key
   * @throws IllegalArgumentException if the signer key type is unknown
   */
  public org.stellar.sdk.xdr.SignerKey toXdr() {
    org.stellar.sdk.xdr.SignerKey.SignerKeyBuilder builder =
        org.stellar.sdk.xdr.SignerKey.builder().discriminant(type);
    switch (type) {
      case SIGNER_KEY_TYPE_ED25519:
        return builder.ed25519(new org.stellar.sdk.xdr.Uint256(key)).build();
      case SIGNER_KEY_TYPE_PRE_AUTH_TX:
        return builder.preAuthTx(new org.stellar.sdk.xdr.Uint256(key)).build();
      case SIGNER_KEY_TYPE_HASH_X:
        return builder.hashX(new org.stellar.sdk.xdr.Uint256(key)).build();
      case SIGNER_KEY_TYPE_ED25519_SIGNED_PAYLOAD:
        Ed25519SignedPayload ed25519SignedPayload = toEd25519SignedPayload();
        return builder
            .ed25519SignedPayload(
                new org.stellar.sdk.xdr.SignerKey.SignerKeyEd25519SignedPayload(
                    new Uint256(ed25519SignedPayload.ed25519PublicKey),
                    ed25519SignedPayload.payload))
            .build();
      default:
        throw new IllegalArgumentException("Unknown SignerKeyType: " + type);
    }
  }

  /**
   * Creates a SignerKey from its XDR representation.
   *
   * @param signerKey The XDR representation of a signer key
   * @return A new SignerKey instance
   * @throws IllegalArgumentException if the XDR signer key type is unknown
   */
  public static SignerKey fromXdr(org.stellar.sdk.xdr.SignerKey signerKey) {
    switch (signerKey.getDiscriminant()) {
      case SIGNER_KEY_TYPE_ED25519:
        return fromEd25519PublicKey(signerKey.getEd25519().getUint256());
      case SIGNER_KEY_TYPE_PRE_AUTH_TX:
        return fromPreAuthTx(signerKey.getPreAuthTx().getUint256());
      case SIGNER_KEY_TYPE_HASH_X:
        return fromSha256Hash(signerKey.getHashX().getUint256());
      case SIGNER_KEY_TYPE_ED25519_SIGNED_PAYLOAD:
        org.stellar.sdk.xdr.SignerKey.SignerKeyEd25519SignedPayload ed25519SignedPayload =
            signerKey.getEd25519SignedPayload();
        return fromEd25519SignedPayload(
            new Ed25519SignedPayload(
                ed25519SignedPayload.getEd25519().getUint256(), ed25519SignedPayload.getPayload()));
      default:
        throw new IllegalArgumentException("Unknown SignerKeyType: " + signerKey.getDiscriminant());
    }
  }

  /**
   * Represents an Ed25519 signed payload as defined in CAP-40.
   *
   * <p>An Ed25519 signed payload consists of an Ed25519 public key and a payload of up to 64 bytes.
   * The signature for this signer type is produced by Ed25519 signing the payload with the private
   * key that corresponds to the public key.
   *
   * <p>This signer type is particularly useful for multi-party contracts such as payment channels,
   * where it allows all parties to share signatures for a set of transactions while ensuring that
   * signing one transaction reveals the information needed to authorize related transactions.
   *
   * <p>The signature hint for this signer type is calculated as the XOR of the last 4 bytes of the
   * Ed25519 public key and the last 4 bytes of the payload (padded with zeros if the payload is
   * less than 4 bytes).
   *
   * @see <a href="https://github.com/stellar/stellar-protocol/blob/master/core/cap-0040.md">CAP-40:
   *     Ed25519 Signed Payload Signer</a>
   */
  @Value
  public static class Ed25519SignedPayload {
    /** The Ed25519 public key (32 bytes) that must sign the payload. */
    byte[] ed25519PublicKey;

    /** The payload to be signed (up to 64 bytes). */
    byte[] payload;

    /**
     * Creates a new Ed25519SignedPayload.
     *
     * @param ed25519PublicKey The 32-byte Ed25519 public key
     * @param payload The payload to be signed (1-64 bytes)
     * @throws IllegalArgumentException if the payload length is not between 1 and 64
     */
    public Ed25519SignedPayload(byte[] ed25519PublicKey, byte[] payload) {
      if (payload.length < 1 || payload.length > SIGNED_PAYLOAD_MAX_PAYLOAD_LENGTH) {
        throw new IllegalArgumentException(
            "Invalid payload length, must be between 1 and "
                + SIGNED_PAYLOAD_MAX_PAYLOAD_LENGTH
                + ", got "
                + payload.length);
      }
      this.ed25519PublicKey = ed25519PublicKey;
      this.payload = payload;
    }

    /**
     * Gets the StrKey-encoded representation of the Ed25519 public key.
     *
     * @return The StrKey-encoded Ed25519 public key (starts with 'G')
     */
    public String getEncodedEd25519PublicKey() {
      return StrKey.encodeEd25519PublicKey(ed25519PublicKey);
    }
  }
}
