package org.stellar.sdk;

import com.google.common.io.BaseEncoding;
import com.google.common.base.Optional;
import org.stellar.sdk.xdr.*;

import java.io.*;
import java.util.Arrays;

class StrKey {

    public static final int ACCOUNT_ID_ADDRESS_LENGTH = 56;
    public static final int MUXED_ACCOUNT_ADDRESS_LENGTH = 69;

    public enum VersionByte {
        ACCOUNT_ID((byte)(6 << 3)), // G
        MUXED_ACCOUNT((byte)(12 << 3)), // M
        SEED((byte)(18 << 3)), // S
        PRE_AUTH_TX((byte)(19 << 3)), // T
        SHA256_HASH((byte)(23 << 3)); // X
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

    public static String encodeStellarAccountId(byte[] data) {
        char[] encoded = encodeCheck(VersionByte.ACCOUNT_ID, data);
        return String.valueOf(encoded);
    }

    public static String encodeStellarAccountId(AccountID accountID) {
        char[] encoded = encodeCheck(VersionByte.ACCOUNT_ID, accountID.getAccountID().getEd25519().getUint256());
        return String.valueOf(encoded);
    }


    public static String encodeStellarMuxedAccount(MuxedAccount account) {
        if (account.getDiscriminant().equals(CryptoKeyType.KEY_TYPE_ED25519)) {
            return encodeStellarAccountId(account.getEd25519().getUint256());
        } else if (account.getDiscriminant().equals(CryptoKeyType.KEY_TYPE_MUXED_ED25519)) {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

            try {
                account.getMed25519().getId().encode(new XdrDataOutputStream(byteStream));
                byteStream.write(account.getMed25519().getEd25519().getUint256());
            } catch (IOException e) {
                throw new IllegalArgumentException("invalid muxed account", e);
            }

            char[] encoded = encodeCheck(VersionByte.MUXED_ACCOUNT, byteStream.toByteArray());
            return String.valueOf(encoded);
        }
        throw new IllegalArgumentException("invalid muxed account type: "+account.getDiscriminant());
    }


    public static AccountID encodeToXDRAccountId(String data) {
        AccountID accountID = new AccountID();
        PublicKey publicKey = new PublicKey();
        publicKey.setDiscriminant(PublicKeyType.PUBLIC_KEY_TYPE_ED25519);
        Uint256 uint256 = new Uint256();
        uint256.setUint256(decodeStellarAccountId(data));
        publicKey.setEd25519(uint256);
        accountID.setAccountID(publicKey);
        return accountID;
    }

    public static MuxedAccount encodeToXDRMuxedAccount(String data) {
        if (data.length() == ACCOUNT_ID_ADDRESS_LENGTH) {
            MuxedAccount accountID = new MuxedAccount();
            accountID.setDiscriminant(CryptoKeyType.KEY_TYPE_ED25519);
            Uint256 uint256 = new Uint256();
            uint256.setUint256(decodeStellarAccountId(data));
            accountID.setEd25519(uint256);
            return accountID;
        } else if (data.length() == MUXED_ACCOUNT_ADDRESS_LENGTH) {
            byte[] decoded = decodeStellarMuxedAccount(data);

            MuxedAccount muxedAccount = new MuxedAccount();
            muxedAccount.setDiscriminant(CryptoKeyType.KEY_TYPE_MUXED_ED25519);
            MuxedAccount.MuxedAccountMed25519 m = new MuxedAccount.MuxedAccountMed25519();
            try {
                m.setId(Uint64.decode(
                    new XdrDataInputStream(new ByteArrayInputStream(decoded, 0, 8))
                ));
                m.setEd25519(Uint256.decode(
                    new XdrDataInputStream(new ByteArrayInputStream(decoded, 8, decoded.length - 8))
                ));
            } catch (IOException e) {
                throw new IllegalArgumentException("invalid address: "+data, e);
            }
            muxedAccount.setMed25519(m);
            return muxedAccount;
        }
        throw new IllegalArgumentException("invalid address length: "+data);
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

    public static byte[] decodeStellarMuxedAccount(String data) {
        return decodeCheck(VersionByte.MUXED_ACCOUNT, data.toCharArray());
    }


    public static char[] encodeStellarSecretSeed(byte[] data) {
        return encodeCheck(VersionByte.SEED, data);
    }

    public static byte[] decodeStellarSecretSeed(char[] data) {
        return decodeCheck(VersionByte.SEED, data);
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
            byte payload[] = outputStream.toByteArray();
            byte checksum[] = StrKey.calculateChecksum(payload);
            outputStream.write(checksum);
            byte unencoded[] = outputStream.toByteArray();

            if (VersionByte.SEED != versionByte) {
                return StrKey.base32Encoding.encode(unencoded).toCharArray();
            }

            // Why not use base32Encoding.encode here?
            // We don't want secret seed to be stored as String in memory because of security reasons. It's impossible
            // to erase it from memory when we want it to be erased (ASAP).
            CharArrayWriter charArrayWriter = new CharArrayWriter(unencoded.length);
            OutputStream charOutputStream = StrKey.base32Encoding.encodingStream(charArrayWriter);
            charOutputStream.write(unencoded);
            char[] charsEncoded = charArrayWriter.toCharArray();

            if (VersionByte.SEED == versionByte) {
                Arrays.fill(unencoded, (byte) 0);
                Arrays.fill(payload, (byte) 0);
                Arrays.fill(checksum, (byte) 0);

                // Clean charArrayWriter internal buffer
                int bufferSize = charArrayWriter.size();
                char[] zeros = new char[bufferSize];
                Arrays.fill(zeros, '0');
                charArrayWriter.reset();
                charArrayWriter.write(zeros);
            }

            return charsEncoded;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    protected static byte[] decodeCheck(VersionByte versionByte, char[] encoded) {
        if (encoded.length == 0) {
            throw new IllegalArgumentException("Encoded char array cannot be empty.");
        }

        byte[] bytes = new byte[encoded.length];
        for (int i = 0; i < encoded.length; i++) {
            if (encoded[i] > 127) {
                throw new IllegalArgumentException("Illegal characters in encoded char array.");
            }
            bytes[i] = (byte) encoded[i];
        }

        int leftoverBits = (bytes.length * 5) % 8;
        if (leftoverBits > 0) {
            byte lastChar = bytes[bytes.length-1];
            byte decodedLastChar = b32Table[lastChar];



            byte leftoverBitsMask = (byte)(0x0f >> (4 - leftoverBits));
            if ((decodedLastChar & leftoverBitsMask) != 0) {
                throw new IllegalArgumentException("Unused bits should be set to 0.");
            }
        }

        byte[] decoded = StrKey.base32Encoding.decode(java.nio.CharBuffer.wrap(encoded));
        byte decodedVersionByte = decoded[0];
        byte[] payload  = Arrays.copyOfRange(decoded, 0, decoded.length-2);
        byte[] data     = Arrays.copyOfRange(payload, 1, payload.length);
        byte[] checksum = Arrays.copyOfRange(decoded, decoded.length-2, decoded.length);

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
        return new byte[] {
            (byte)crc,
            (byte)(crc >>> 8)};
    }

    private static final byte[] b32Table = decodingTable();
    private static byte[] decodingTable() {
        byte[] table = new byte[256];
        for (int i=0; i <256; i++) {
            table[i] = (byte)0xff;
        }
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
        for(int i=0; i < alphabet.length(); i++) {
            table[(int)alphabet.charAt(i)] = (byte)i;
        }
        return table;
    }
}
