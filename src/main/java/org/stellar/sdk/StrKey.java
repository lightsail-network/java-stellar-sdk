package org.stellar.sdk;

import com.google.common.io.BaseEncoding;

import java.io.*;
import java.util.Arrays;

class StrKey {
    public enum VersionByte {
        ACCOUNT_ID((byte)(6 << 3)), // G
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
    }

    private static BaseEncoding base32Encoding = BaseEncoding.base32().upperCase().omitPadding();

    public static String encodeStellarAccountId(byte[] data) {
        char[] encoded = encodeCheck(VersionByte.ACCOUNT_ID, data);
        return String.valueOf(encoded);
    }

    public static byte[] decodeStellarAccountId(String data) {
        return decodeCheck(VersionByte.ACCOUNT_ID, data.toCharArray());
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
        byte[] bytes = new byte[encoded.length];
        for (int i = 0; i < encoded.length; i++) {
            if (encoded[i] > 127) {
                throw new IllegalArgumentException("Illegal characters in encoded char array.");
            }
            bytes[i] = (byte) encoded[i];
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
}
