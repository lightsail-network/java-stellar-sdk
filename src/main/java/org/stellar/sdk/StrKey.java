package org.stellar.sdk;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;
import org.stellar.sdk.exception.UnexpectedException;
import org.stellar.sdk.xdr.AccountID;
import org.stellar.sdk.xdr.CryptoKeyType;
import org.stellar.sdk.xdr.MuxedAccount;
import org.stellar.sdk.xdr.PublicKey;
import org.stellar.sdk.xdr.PublicKeyType;
import org.stellar.sdk.xdr.Uint256;
import org.stellar.sdk.xdr.Uint64;
import org.stellar.sdk.xdr.XdrDataInputStream;
import org.stellar.sdk.xdr.XdrUnsignedHyperInteger;

/**
 * StrKey is a helper class that allows encoding and decoding Stellar keys to/from strings, i.e.
 * between their binary and string (i.e. "GABCD...", etc.) representations.
 */
public class StrKey {

  private static final byte[] b32Table = decodingTable();
  private static final Base32 base32Codec = Base32Factory.getInstance();

  /**
   * Encodes raw data to strkey ed25519 public key (G...)
   *
   * @param data data to encode
   * @return "G..." representation of the key
   */
  public static String encodeEd25519PublicKey(byte[] data) {
    char[] encoded = encodeCheck(VersionByte.ACCOUNT_ID, data);
    return String.valueOf(encoded);
  }

  /**
   * Decodes strkey ed25519 public key (G...) to raw data
   *
   * @param data data to decode
   * @return raw bytes
   */
  public static byte[] decodeEd25519PublicKey(String data) {
    return decodeCheck(VersionByte.ACCOUNT_ID, data.toCharArray());
  }

  /**
   * Checks validity of Stellar account ID (G...).
   *
   * @param accountID the account ID to check
   * @return true if the given Stellar account ID is a valid Stellar account ID, false otherwise
   */
  public static boolean isValidEd25519PublicKey(String accountID) {
    try {
      decodeEd25519PublicKey(accountID);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Encodes raw data to strkey ed25519 seed in char array (S...)
   *
   * @param data data to encode
   * @return "S..." representation of the key in char array
   */
  public static char[] encodeEd25519SecretSeed(byte[] data) {
    return encodeCheck(VersionByte.SEED, data);
  }

  /**
   * Decodes strkey ed25519 seed char array (S...) to raw bytes
   *
   * @param data data to decode
   * @return raw bytes
   */
  public static byte[] decodeEd25519SecretSeed(char[] data) {
    return decodeCheck(VersionByte.SEED, data);
  }

  /**
   * Checks validity of seed (S...).
   *
   * @param seed the seed to check
   * @return true if the given seed is a valid seed, false otherwise
   */
  public static boolean isValidEd25519SecretSeed(char[] seed) {
    try {
      decodeEd25519SecretSeed(seed);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Encodes raw data to strkey PreAuthTx (T...)
   *
   * @param data data to encode
   * @return "T..." representation of the key
   */
  public static String encodePreAuthTx(byte[] data) {
    char[] encoded = encodeCheck(VersionByte.PRE_AUTH_TX, data);
    return String.valueOf(encoded);
  }

  /**
   * Decodes strkey PreAuthTx (T...) to raw bytes
   *
   * @param data data to decode
   * @return raw bytes
   */
  public static byte[] decodePreAuthTx(String data) {
    return decodeCheck(VersionByte.PRE_AUTH_TX, data.toCharArray());
  }

  /**
   * Checks validity of PreAuthTx (T...).
   *
   * @param preAuthTx the PreAuthTx to check
   * @return true if the given PreAuthTx is a valid PreAuthTx, false otherwise
   */
  public static boolean isValidPreAuthTx(String preAuthTx) {
    try {
      decodePreAuthTx(preAuthTx);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Encodes raw data to strkey SHA256 hash (X...)
   *
   * @param data data to encode
   * @return "X..." representation of the key
   */
  public static String encodeSha256Hash(byte[] data) {
    char[] encoded = encodeCheck(VersionByte.SHA256_HASH, data);
    return String.valueOf(encoded);
  }

  /**
   * Decodes strkey SHA256 hash (X...) to raw bytes
   *
   * @param data data to decode
   * @return raw bytes
   */
  public static byte[] decodeSha256Hash(String data) {
    return decodeCheck(VersionByte.SHA256_HASH, data.toCharArray());
  }

  /**
   * Checks validity of SHA256 hash (X...).
   *
   * @param sha256Hash the SHA256 hash to check
   * @return true if the given SHA256 hash is a valid SHA256 hash, false otherwise
   */
  public static boolean isValidSha256Hash(String sha256Hash) {
    try {
      decodeSha256Hash(sha256Hash);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Encodes raw data to strkey signed payload (P...)
   *
   * @param data data to encode
   * @return "P..." representation of the key
   */
  public static String encodeSignedPayload(byte[] data) {
    char[] encoded = encodeCheck(VersionByte.SIGNED_PAYLOAD, data);
    return String.valueOf(encoded);
  }

  /**
   * Decodes strkey signed payload (P...) to raw bytes.
   *
   * @param data data to decode
   * @return raw bytes
   */
  public static byte[] decodeSignedPayload(String data) {
    return decodeCheck(VersionByte.SIGNED_PAYLOAD, data.toCharArray());
  }

  /**
   * Checks validity of signed payload (P...).
   *
   * @param signedPayload the signed payload to check
   * @return true if the given signed payload is a valid signed payload, false otherwise
   */
  public static boolean isValidSignedPayload(String signedPayload) {
    try {
      decodeSignedPayload(signedPayload);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Encodes raw data to strkey contract ID (C...)
   *
   * @param data data to encode
   * @return "C..." representation of the key
   */
  public static String encodeContract(byte[] data) {
    char[] encoded = encodeCheck(VersionByte.CONTRACT, data);
    return String.valueOf(encoded);
  }

  /**
   * Decodes strkey contract ID (C...) to raw bytes.
   *
   * @param data data to decode
   * @return raw bytes
   */
  public static byte[] decodeContract(String data) {
    return decodeCheck(VersionByte.CONTRACT, data.toCharArray());
  }

  /**
   * Checks validity of contract (C...) address.
   *
   * @param contractId the contract ID to check
   * @return true if the given contract ID is a valid contract ID, false otherwise
   */
  public static boolean isValidContract(String contractId) {
    try {
      decodeContract(contractId);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Encodes raw data to strkey liquidity pool ID (L...)
   *
   * @param data data to encode
   * @return "L..." representation of the key
   */
  public static String encodeLiquidityPool(byte[] data) {
    char[] encoded = encodeCheck(VersionByte.LIQUIDITY_POOL, data);
    return String.valueOf(encoded);
  }

  /**
   * Decodes strkey liquidity pool ID (L...) to raw bytes.
   *
   * @param data data to decode
   * @return raw bytes
   */
  public static byte[] decodeLiquidityPool(String data) {
    return decodeCheck(VersionByte.LIQUIDITY_POOL, data.toCharArray());
  }

  /**
   * Checks validity of liquidity pool (L...) address.
   *
   * @param liquidityPoolId the liquidity pool ID to check
   * @return true if the given liquidity pool ID is a valid liquidity pool ID, false otherwise
   */
  public static boolean isValidLiquidityPool(String liquidityPoolId) {
    try {
      decodeLiquidityPool(liquidityPoolId);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Encodes raw data to strkey claimable balance ID (B...)
   *
   * @param data data to encode
   * @return "B..." representation of the key
   */
  public static String encodeClaimableBalance(byte[] data) {
    char[] encoded = encodeCheck(VersionByte.CLAIMABLE_BALANCE, data);
    return String.valueOf(encoded);
  }

  /**
   * Decodes strkey claimable balance ID (B...) to raw bytes.
   *
   * @param data data to decode
   * @return raw bytes
   */
  public static byte[] decodeClaimableBalance(String data) {
    return decodeCheck(VersionByte.CLAIMABLE_BALANCE, data.toCharArray());
  }

  /**
   * Checks validity of claimable balance (B...) address.
   *
   * @param claimableBalanceId the claimable balance ID to check
   * @return true if the given claimable balance ID is a valid claimable balance ID, false otherwise
   */
  public static boolean isValidClaimableBalance(String claimableBalanceId) {
    try {
      decodeClaimableBalance(claimableBalanceId);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Encodes raw data to strkey Stellar med25519 public key (M...)
   *
   * @param data data to encode
   * @return "M..." representation of the key
   */
  public static String encodeMed25519PublicKey(byte[] data) {
    char[] encoded = encodeCheck(VersionByte.MED25519_PUBLIC_KEY, data);
    return String.valueOf(encoded);
  }

  /**
   * Decodes strkey Stellar med25519 public key (M...) to raw bytes
   *
   * @param data data to decode
   * @return raw bytes
   */
  public static byte[] decodeMed25519PublicKey(String data) {
    return decodeCheck(VersionByte.MED25519_PUBLIC_KEY, data.toCharArray());
  }

  /**
   * Checks validity of med25519 public key (M...).
   *
   * @param med25519PublicKey the med25519 public key to check
   * @return true if the given med25519 public key is a valid med25519 public key, false otherwise
   */
  public static boolean isValidMed25519PublicKey(String med25519PublicKey) {
    try {
      decodeMed25519PublicKey(med25519PublicKey);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Encodes raw data to strkey Stellar account ID (G...)
   *
   * <p>This method is deprecated and will be removed in future versions, use {@link
   * #encodeEd25519PublicKey(byte[])} instead.
   *
   * @param accountID data to encode
   * @return "G..." representation of the key
   */
  @Deprecated
  public static String encodeEd25519PublicKey(AccountID accountID) {
    char[] encoded =
        encodeCheck(VersionByte.ACCOUNT_ID, accountID.getAccountID().getEd25519().getUint256());
    return String.valueOf(encoded);
  }

  /**
   * Encodes raw data to strkey Stellar muxed account ID (M... or G...)
   *
   * <p>This method is deprecated and will be removed in future versions, use {@link
   * org.stellar.sdk.MuxedAccount} instead.
   *
   * @param muxedAccount the muxed account to encode
   * @return "M..." or "G..." representation of the key
   */
  @Deprecated
  public static String encodeMuxedAccount(MuxedAccount muxedAccount) {
    switch (muxedAccount.getDiscriminant()) {
      case KEY_TYPE_MUXED_ED25519:
        return String.valueOf(
            encodeCheck(
                VersionByte.MED25519_PUBLIC_KEY, getMuxedEd25519AccountBytes(muxedAccount)));
      case KEY_TYPE_ED25519:
        return String.valueOf(
            encodeCheck(VersionByte.ACCOUNT_ID, muxedAccount.getEd25519().getUint256()));
      default:
        throw new IllegalArgumentException("invalid discriminant");
    }
  }

  /**
   * Decodes strkey Stellar account ID (G...) or muxed account ID (M...) to {@link MuxedAccount}.
   *
   * <p>This method is deprecated and will be removed in future versions, use {@link
   * org.stellar.sdk.MuxedAccount} instead.
   *
   * @param address the address to decode
   * @return {@link MuxedAccount} representation of the key
   */
  @Deprecated
  public static MuxedAccount decodeMuxedAccount(String address) {
    return encodeToXDRMuxedAccount(address);
  }

  /**
   * Encodes strkey Stellar account ID (G...) to {@link AccountID}.
   *
   * <p>This method is deprecated and will be removed in future versions, use {@link
   * KeyPair#getXdrAccountId()} instead.
   *
   * @param data the data to encode
   * @return {@link AccountID} representation of the key
   */
  @Deprecated
  public static AccountID encodeToXDRAccountId(String data) {
    AccountID accountID = new AccountID();
    PublicKey publicKey = new PublicKey();
    publicKey.setDiscriminant(PublicKeyType.PUBLIC_KEY_TYPE_ED25519);
    try {
      publicKey.setEd25519(Uint256.fromXdrByteArray(decodeEd25519PublicKey(data)));
    } catch (IOException e) {
      throw new IllegalArgumentException("invalid address: " + data, e);
    }
    accountID.setAccountID(publicKey);
    return accountID;
  }

  /**
   * Encodes strkey Stellar account ID (G...) or muxed account ID (M...) to {@link MuxedAccount}.
   *
   * <p>This method is deprecated and will be removed in future versions, use {@link
   * org.stellar.sdk.MuxedAccount} instead.
   *
   * @param data the data to encode
   * @return {@link MuxedAccount} representation of the key
   */
  @Deprecated
  public static MuxedAccount encodeToXDRMuxedAccount(String data) {
    MuxedAccount muxed = new MuxedAccount();

    if (data.isEmpty()) {
      throw new IllegalArgumentException("address is empty");
    }
    switch (decodeVersionByte(data)) {
      case ACCOUNT_ID:
        muxed.setDiscriminant(CryptoKeyType.KEY_TYPE_ED25519);
        byte[] rawEd25519PublicKey = decodeEd25519PublicKey(data);
        try {
          muxed.setEd25519(Uint256.fromXdrByteArray(rawEd25519PublicKey));
        } catch (IOException e) {
          throw new IllegalArgumentException("invalid address: " + data, e);
        }
        break;
      case MED25519_PUBLIC_KEY:
        XdrDataInputStream input =
            new XdrDataInputStream(
                new ByteArrayInputStream(
                    decodeCheck(VersionByte.MED25519_PUBLIC_KEY, data.toCharArray())));
        muxed.setDiscriminant(CryptoKeyType.KEY_TYPE_MUXED_ED25519);
        MuxedAccount.MuxedAccountMed25519 med = new MuxedAccount.MuxedAccountMed25519();
        try {
          med.setEd25519(Uint256.decode(input));
          med.setId(new Uint64(XdrUnsignedHyperInteger.decode(input)));
        } catch (IOException e) {
          throw new IllegalArgumentException("invalid address: " + data, e);
        }
        muxed.setMed25519(med);
        break;
      default:
        throw new IllegalArgumentException("Version byte is invalid");
    }
    return muxed;
  }

  static VersionByte decodeVersionByte(String data) {
    byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
    byte[] decoded = base32decode(dataBytes);
    byte decodedVersionByte = decoded[0];
    Optional<VersionByte> versionByteOptional = VersionByte.findByValue(decodedVersionByte);
    if (!versionByteOptional.isPresent()) {
      throw new IllegalArgumentException("Version byte is invalid");
    }
    return versionByteOptional.get();
  }

  static char[] encodeCheck(VersionByte versionByte, byte[] data) {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    outputStream.write(versionByte.getValue());
    try {
      outputStream.write(data);
    } catch (IOException e) {
      throw new UnexpectedException(e);
    }

    byte[] payload = outputStream.toByteArray();
    byte[] checksum = StrKey.calculateChecksum(payload);
    try {
      outputStream.write(checksum);
    } catch (IOException e) {
      throw new UnexpectedException(e);
    }

    byte[] unencoded = outputStream.toByteArray();

    byte[] encodedBytes = base32Codec.encode(unencoded);
    byte[] unpaddedEncodedBytes = removeBase32Padding(encodedBytes);
    char[] charsEncoded = bytesToChars(unpaddedEncodedBytes);

    if (VersionByte.SEED != versionByte) {
      return charsEncoded;
    }

    // Erase all data from memory
    Arrays.fill(unencoded, (byte) 0);
    Arrays.fill(payload, (byte) 0);
    Arrays.fill(checksum, (byte) 0);
    Arrays.fill(encodedBytes, (byte) 0);
    Arrays.fill(unpaddedEncodedBytes, (byte) 0);

    return charsEncoded;
  }

  static byte[] decodeCheck(VersionByte versionByte, char[] encoded) {
    byte[] bytes = new byte[encoded.length];
    for (int i = 0; i < encoded.length; i++) {
      bytes[i] = (byte) encoded[i];
    }

    // The minimal binary decoded length is 3 bytes (version byte and 2-byte CRC) which,
    // in unpadded base32 (since each character provides 5 bits) corresponds to ceiling(8*3/5) = 5
    if (bytes.length < 5) {
      throw new IllegalArgumentException("Encoded char array must have a length of at least 5.");
    }

    int leftoverBits = (bytes.length * 5) % 8;
    // 1. Make sure there is no full unused leftover byte at the end
    //   (i.e. there shouldn't be 5 or more leftover bits)
    if (leftoverBits >= 5) {
      throw new IllegalArgumentException("Encoded char array has leftover character.");
    }

    if (leftoverBits > 0) {
      byte lastChar = bytes[bytes.length - 1];
      byte decodedLastChar = b32Table[lastChar];

      byte leftoverBitsMask = (byte) (0x0f >> (4 - leftoverBits));
      if ((decodedLastChar & leftoverBitsMask) != 0) {
        throw new IllegalArgumentException("Unused bits should be set to 0.");
      }
    }

    byte[] decoded = base32decode(bytes);
    byte decodedVersionByte = decoded[0];
    VersionByte decodedVersionByteEnum =
        VersionByte.findByValue(decodedVersionByte)
            .orElseThrow(() -> new IllegalArgumentException("Version byte is invalid"));
    byte[] payload = Arrays.copyOfRange(decoded, 0, decoded.length - 2);
    byte[] data = Arrays.copyOfRange(payload, 1, payload.length);
    byte[] checksum = Arrays.copyOfRange(decoded, decoded.length - 2, decoded.length);

    // Check the data length
    switch (decodedVersionByteEnum) {
      case SIGNED_PAYLOAD:
        if (data.length < 32 + 4 + 4 || data.length > 32 + 4 + 64) {
          throw new IllegalArgumentException(
              "Invalid data length, the length should be between 40 and 100 bytes, got "
                  + data.length);
        }
        break;
      case MED25519_PUBLIC_KEY:
        if (data.length != 32 + 8) {
          throw new IllegalArgumentException(
              "Invalid data length, expected 40 bytes, got " + data.length);
        }
        break;
      case CLAIMABLE_BALANCE:
        if (data.length != 32 + 1) {
          // If we are encoding a claimable balance, the binary bytes of the key has a length of
          // 33-bytes:
          // 1-byte value indicating the type of claimable balance, where 0x00 maps to V0, and a
          // 32-byte SHA256 hash.
          throw new IllegalArgumentException(
              "Invalid data length, expected 33 bytes, got " + data.length);
        }
        break;
      default:
        if (data.length != 32) {
          throw new IllegalArgumentException(
              "Invalid data length, expected 32 bytes, got " + data.length);
        }
        break;
    }

    if (decodedVersionByteEnum != versionByte) {
      throw new IllegalArgumentException("Version byte mismatch");
    }

    byte[] expectedChecksum = StrKey.calculateChecksum(payload);

    if (!Arrays.equals(expectedChecksum, checksum)) {
      throw new IllegalArgumentException("Checksum invalid");
    }

    if (VersionByte.SIGNED_PAYLOAD.getValue() == decodedVersionByte) {
      byte[] lengthBytes = Arrays.copyOfRange(data, 32, 36);
      int payloadLength = ByteBuffer.wrap(lengthBytes).getInt();
      int padding = (4 - payloadLength % 4) % 4;
      if (data.length % 4 != 0 || payloadLength + padding != data.length - 36) {
        throw new IllegalArgumentException("Invalid Ed25519 Signed Payload Key");
      }
    }

    if (VersionByte.SEED.getValue() == decodedVersionByte) {
      Arrays.fill(bytes, (byte) 0);
      Arrays.fill(decoded, (byte) 0);
      Arrays.fill(payload, (byte) 0);
    }
    return data;
  }

  private static byte[] calculateChecksum(byte[] bytes) {
    // This code calculates CRC16-XModem checksum
    // Ported from https://github.com/alexgorbatchev/node-crc
    int crc = 0x0000;
    int count = bytes.length;
    int i = 0;
    int code;

    while (count > 0) {
      code = crc >>> 8 & 0xFF;
      code ^= bytes[i++] & 0xFF;
      code ^= code >>> 4;
      crc = crc << 8 & 0xFFFF;
      crc ^= code;
      code = code << 5 & 0xFFFF;
      crc ^= code;
      code = code << 7 & 0xFFFF;
      crc ^= code;
      count--;
    }

    // little-endian
    return new byte[] {(byte) crc, (byte) (crc >>> 8)};
  }

  private static byte[] decodingTable() {
    byte[] table = new byte[256];
    for (int i = 0; i < 256; i++) {
      table[i] = (byte) 0xff;
    }
    String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
    for (int i = 0; i < alphabet.length(); i++) {
      table[alphabet.charAt(i)] = (byte) i;
    }
    return table;
  }

  @Deprecated
  private static byte[] getMuxedEd25519AccountBytes(MuxedAccount muxedAccount) {
    byte[] accountBytes = muxedAccount.getMed25519().getEd25519().getUint256();
    byte[] idBytes = muxedAccount.getMed25519().getId().getUint64().getNumber().toByteArray();
    byte[] idPaddedBytes = new byte[8];
    int idNumBytesToCopy = Math.min(idBytes.length, 8);
    int idCopyStartIndex = idBytes.length - idNumBytesToCopy;
    System.arraycopy(
        idBytes, idCopyStartIndex, idPaddedBytes, 8 - idNumBytesToCopy, idNumBytesToCopy);
    byte[] result = new byte[accountBytes.length + idPaddedBytes.length];
    System.arraycopy(accountBytes, 0, result, 0, accountBytes.length);
    System.arraycopy(idPaddedBytes, 0, result, accountBytes.length, idPaddedBytes.length);
    return result;
  }

  private static byte[] removeBase32Padding(byte[] data) {
    // Calculate the length of unpadded data
    int unpaddedLength = data.length;
    while (unpaddedLength > 0 && data[unpaddedLength - 1] == '=') {
      unpaddedLength--;
    }

    // Create a copy of the data without padding bytes
    return Arrays.copyOf(data, unpaddedLength);
  }

  private static char[] bytesToChars(byte[] data) {
    char[] chars = new char[data.length];
    for (int i = 0; i < data.length; i++) {
      chars[i] = (char) (data[i] & 0xFF);
    }
    return chars;
  }

  private static byte[] base32decode(byte[] data) {
    // Apache commons codec Base32 class will auto remove the illegal characters, this is
    // what we don't want, so we need to check the data before decoding
    if (!isInAlphabet(data)) {
      throw new IllegalArgumentException("Invalid base32 encoded string");
    }
    return base32Codec.decode(data);
  }

  private static boolean isInAlphabet(final byte[] arrayOctet) {
    for (final byte octet : arrayOctet) {
      if (!(octet >= 0 && octet < b32Table.length && b32Table[octet] != -1)) {
        return false;
      }
    }
    return true;
  }

  enum VersionByte {
    ACCOUNT_ID((byte) (6 << 3)), // G
    MED25519_PUBLIC_KEY((byte) (12 << 3)), // M
    SEED((byte) (18 << 3)), // S
    PRE_AUTH_TX((byte) (19 << 3)), // T
    SHA256_HASH((byte) (23 << 3)), // X
    SIGNED_PAYLOAD((byte) (15 << 3)), // P
    CONTRACT((byte) (2 << 3)), // C
    LIQUIDITY_POOL((byte) (11 << 3)), // L
    CLAIMABLE_BALANCE((byte) (1 << 3)); // B

    private final byte value;

    VersionByte(byte value) {
      this.value = value;
    }

    public static Optional<VersionByte> findByValue(byte value) {
      for (VersionByte versionByte : values()) {
        if (value == versionByte.value) {
          return Optional.of(versionByte);
        }
      }
      return Optional.empty();
    }

    public int getValue() {
      return value;
    }
  }
}
