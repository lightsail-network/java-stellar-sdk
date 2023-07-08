package org.stellar.sdk;

import com.google.common.base.Optional;
import com.google.common.io.BaseEncoding;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Longs;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import org.stellar.sdk.xdr.AccountID;
import org.stellar.sdk.xdr.CryptoKeyType;
import org.stellar.sdk.xdr.MuxedAccount;
import org.stellar.sdk.xdr.PublicKey;
import org.stellar.sdk.xdr.PublicKeyType;
import org.stellar.sdk.xdr.SignerKey;
import org.stellar.sdk.xdr.Uint256;
import org.stellar.sdk.xdr.Uint64;
import org.stellar.sdk.xdr.XdrDataInputStream;
import org.stellar.sdk.xdr.XdrDataOutputStream;

class StrKey {

  public static final int ACCOUNT_ID_ADDRESS_LENGTH = 56;

  public enum VersionByte {
    ACCOUNT_ID((byte) (6 << 3)), // G
    MUXED((byte) (12 << 3)), // M
    SEED((byte) (18 << 3)), // S
    PRE_AUTH_TX((byte) (19 << 3)), // T
    SHA256_HASH((byte) (23 << 3)), // X
    SIGNED_PAYLOAD((byte) (15 << 3)), // P

    CONTRACT((byte) (2 << 3)); // C

    private final byte value;

    VersionByte(byte value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }

    public static Optional<VersionByte> findByValue(byte value) {
      for (VersionByte versionByte : values()) {
        if (value == versionByte.value) {
          return Optional.of(versionByte);
        }
      }
      return Optional.absent();
    }
  }

  private static BaseEncoding base32Encoding = BaseEncoding.base32().upperCase().omitPadding();

  public static String encodeContractId(byte[] data) {
    char[] encoded = encodeCheck(VersionByte.CONTRACT, data);
    return String.valueOf(encoded);
  }

  public static String encodeStellarAccountId(byte[] data) {
    char[] encoded = encodeCheck(VersionByte.ACCOUNT_ID, data);
    return String.valueOf(encoded);
  }

  public static String encodeStellarAccountId(AccountID accountID) {
    char[] encoded =
        encodeCheck(VersionByte.ACCOUNT_ID, accountID.getAccountID().getEd25519().getUint256());
    return String.valueOf(encoded);
  }

  public static String encodeSignedPayload(SignedPayloadSigner signedPayloadSigner) {
    try {
      SignerKey.SignerKeyEd25519SignedPayload xdrPayloadSigner =
          new SignerKey.SignerKeyEd25519SignedPayload();
      xdrPayloadSigner.setPayload(signedPayloadSigner.getPayload());
      xdrPayloadSigner.setEd25519(
          signedPayloadSigner.getSignerAccountId().getAccountID().getEd25519());

      ByteArrayOutputStream record = new ByteArrayOutputStream();
      xdrPayloadSigner.encode(new XdrDataOutputStream(record));

      char[] encoded = encodeCheck(VersionByte.SIGNED_PAYLOAD, record.toByteArray());
      return String.valueOf(encoded);
    } catch (Exception ex) {
      throw new FormatException(ex.getMessage());
    }
  }

  public static String encodeStellarMuxedAccount(MuxedAccount muxedAccount) {
    switch (muxedAccount.getDiscriminant()) {
      case KEY_TYPE_MUXED_ED25519:
        return String.valueOf(
            encodeCheck(
                VersionByte.MUXED,
                Bytes.concat(
                    muxedAccount.getMed25519().getEd25519().getUint256(),
                    Longs.toByteArray(muxedAccount.getMed25519().getId().getUint64()))));
      case KEY_TYPE_ED25519:
        return String.valueOf(
            encodeCheck(VersionByte.ACCOUNT_ID, muxedAccount.getEd25519().getUint256()));
      default:
        throw new IllegalArgumentException("invalid discriminant");
    }
  }

  public static AccountID muxedAccountToAccountId(MuxedAccount account) {
    AccountID aid = new AccountID();
    PublicKey key = new PublicKey();
    key.setDiscriminant(PublicKeyType.PUBLIC_KEY_TYPE_ED25519);

    if (account.getDiscriminant().equals(CryptoKeyType.KEY_TYPE_ED25519)) {
      key.setEd25519(account.getEd25519());
    } else if (account.getDiscriminant().equals(CryptoKeyType.KEY_TYPE_MUXED_ED25519)) {
      key.setEd25519(account.getMed25519().getEd25519());
    } else {
      throw new IllegalArgumentException(
          "invalid muxed account type: " + account.getDiscriminant());
    }

    aid.setAccountID(key);
    return aid;
  }

  public static AccountID encodeToXDRAccountId(String data) {
    AccountID accountID = new AccountID();
    PublicKey publicKey = new PublicKey();
    publicKey.setDiscriminant(PublicKeyType.PUBLIC_KEY_TYPE_ED25519);
    try {
      publicKey.setEd25519(
          Uint256.decode(
              new XdrDataInputStream(new ByteArrayInputStream(decodeStellarAccountId(data)))));
    } catch (IOException e) {
      throw new IllegalArgumentException("invalid address: " + data, e);
    }
    accountID.setAccountID(publicKey);
    return accountID;
  }

  public static MuxedAccount encodeToXDRMuxedAccount(String data) {
    MuxedAccount muxed = new MuxedAccount();

    if (data.length() == 0) {
      throw new IllegalArgumentException("address is empty");
    }
    switch (decodeVersionByte(data)) {
      case ACCOUNT_ID:
        muxed.setDiscriminant(CryptoKeyType.KEY_TYPE_ED25519);
        try {
          muxed.setEd25519(
              Uint256.decode(
                  new XdrDataInputStream(new ByteArrayInputStream(decodeStellarAccountId(data)))));
        } catch (IOException e) {
          throw new IllegalArgumentException("invalid address: " + data, e);
        }
        break;
      case MUXED:
        XdrDataInputStream input =
            new XdrDataInputStream(
                new ByteArrayInputStream(decodeCheck(VersionByte.MUXED, data.toCharArray())));
        muxed.setDiscriminant(CryptoKeyType.KEY_TYPE_MUXED_ED25519);
        MuxedAccount.MuxedAccountMed25519 med = new MuxedAccount.MuxedAccountMed25519();
        try {
          med.setEd25519(Uint256.decode(input));
          med.setId(Uint64.decode(input));
        } catch (IOException e) {
          throw new IllegalArgumentException("invalid address: " + data, e);
        }
        muxed.setMed25519(med);
        break;
      default:
        throw new FormatException("Version byte is invalid");
    }
    return muxed;
  }

  public static VersionByte decodeVersionByte(String data) {
    byte[] decoded = StrKey.base32Encoding.decode(java.nio.CharBuffer.wrap(data.toCharArray()));
    byte decodedVersionByte = decoded[0];
    Optional<VersionByte> versionByteOptional = VersionByte.findByValue(decodedVersionByte);
    if (!versionByteOptional.isPresent()) {
      throw new FormatException("Version byte is invalid");
    }
    return versionByteOptional.get();
  }

  public static byte[] decodeStellarAccountId(String data) {
    return decodeCheck(VersionByte.ACCOUNT_ID, data.toCharArray());
  }

  public static byte[] decodeContractId(String data) {
    return decodeCheck(VersionByte.CONTRACT, data.toCharArray());
  }

  public static char[] encodeStellarSecretSeed(byte[] data) {
    return encodeCheck(VersionByte.SEED, data);
  }

  public static byte[] decodeStellarSecretSeed(char[] data) {
    return decodeCheck(VersionByte.SEED, data);
  }

  public static SignedPayloadSigner decodeSignedPayload(char[] data) {
    try {
      byte[] signedPayloadRaw = decodeCheck(VersionByte.SIGNED_PAYLOAD, data);

      SignerKey.SignerKeyEd25519SignedPayload xdrPayloadSigner =
          SignerKey.SignerKeyEd25519SignedPayload.decode(
              new XdrDataInputStream(new ByteArrayInputStream(signedPayloadRaw)));

      return new SignedPayloadSigner(
          xdrPayloadSigner.getEd25519().getUint256(), xdrPayloadSigner.getPayload());
    } catch (Exception ex) {
      throw new FormatException(ex.getMessage());
    }
  }

  public static String encodePreAuthTx(byte[] data) {
    char[] encoded = encodeCheck(VersionByte.PRE_AUTH_TX, data);
    return String.valueOf(encoded);
  }

  public static byte[] decodePreAuthTx(String data) {
    return decodeCheck(VersionByte.PRE_AUTH_TX, data.toCharArray());
  }

  public static String encodeSha256Hash(byte[] data) {
    char[] encoded = encodeCheck(VersionByte.SHA256_HASH, data);
    return String.valueOf(encoded);
  }

  public static byte[] decodeSha256Hash(String data) {
    return decodeCheck(VersionByte.SHA256_HASH, data.toCharArray());
  }

  protected static char[] encodeCheck(VersionByte versionByte, byte[] data) {
    try {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      outputStream.write(versionByte.getValue());
      outputStream.write(data);
      byte[] payload = outputStream.toByteArray();
      byte[] checksum = StrKey.calculateChecksum(payload);
      outputStream.write(checksum);
      byte[] unencoded = outputStream.toByteArray();

      if (VersionByte.SEED != versionByte) {
        return base32Encoding.encode(unencoded).toCharArray();
      }

      // Why not use base32Encoding.encode here?
      // We don't want secret seed to be stored as String in memory because of security reasons.
      // It's impossible
      // to erase it from memory when we want it to be erased (ASAP).
      CharArrayWriter charArrayWriter = new CharArrayWriter(unencoded.length);
      OutputStream charOutputStream = base32Encoding.encodingStream(charArrayWriter);
      charOutputStream.write(unencoded);
      char[] charsEncoded = charArrayWriter.toCharArray();

      Arrays.fill(unencoded, (byte) 0);
      Arrays.fill(payload, (byte) 0);
      Arrays.fill(checksum, (byte) 0);

      // Clean charArrayWriter internal buffer
      int bufferSize = charArrayWriter.size();
      char[] zeros = new char[bufferSize];
      Arrays.fill(zeros, '0');
      charArrayWriter.reset();
      charArrayWriter.write(zeros);

      return charsEncoded;
    } catch (IOException e) {
      throw new AssertionError(e);
    }
  }

  protected static byte[] decodeCheck(VersionByte versionByte, char[] encoded) {
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

    byte[] decoded = base32Encoding.decode(java.nio.CharBuffer.wrap(encoded));
    byte decodedVersionByte = decoded[0];
    byte[] payload = Arrays.copyOfRange(decoded, 0, decoded.length - 2);
    byte[] data = Arrays.copyOfRange(payload, 1, payload.length);
    byte[] checksum = Arrays.copyOfRange(decoded, decoded.length - 2, decoded.length);

    if (decodedVersionByte != versionByte.getValue()) {
      throw new FormatException("Version byte is invalid");
    }

    byte[] expectedChecksum = StrKey.calculateChecksum(payload);

    if (!Arrays.equals(expectedChecksum, checksum)) {
      throw new FormatException("Checksum invalid");
    }

    if (VersionByte.SEED.getValue() == decodedVersionByte) {
      Arrays.fill(bytes, (byte) 0);
      Arrays.fill(decoded, (byte) 0);
      Arrays.fill(payload, (byte) 0);
    }

    return data;
  }

  protected static byte[] calculateChecksum(byte[] bytes) {
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

  private static final byte[] b32Table = decodingTable();

  private static byte[] decodingTable() {
    byte[] table = new byte[256];
    for (int i = 0; i < 256; i++) {
      table[i] = (byte) 0xff;
    }
    String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
    for (int i = 0; i < alphabet.length(); i++) {
      table[(int) alphabet.charAt(i)] = (byte) i;
    }
    return table;
  }
}
