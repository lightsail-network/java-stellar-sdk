package org.stellar.sdk;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class StrKeyTest {
    @Test
    public void testDecodeEncode() throws IOException, FormatException {
        String seed = "SDJHRQF4GCMIIKAAAQ6IHY42X73FQFLHUULAPSKKD4DFDM7UXWWCRHBE";
        byte[] secret = StrKey.decodeCheck(StrKey.VersionByte.SEED, seed.toCharArray());
        char[] encoded = StrKey.encodeCheck(StrKey.VersionByte.SEED, secret);
        assertEquals(seed, String.valueOf(encoded));
    }

    @Test()
    public void testDecodeInvalidVersionByte() {
        String address = "GCZHXL5HXQX5ABDM26LHYRCQZ5OJFHLOPLZX47WEBP3V2PF5AVFK2A5D";
        try {
            StrKey.decodeCheck(StrKey.VersionByte.SEED, address.toCharArray());
            fail();
        } catch (FormatException e) {}
    }

    @Test()
    public void testDecodeInvalidSeed() {
        String seed = "SAA6NXOBOXP3RXGAXBW6PGFI5BPK4ODVAWITS4VDOMN5C2M4B66ZML";
        try {
            StrKey.decodeCheck(StrKey.VersionByte.SEED, seed.toCharArray());
            fail();
        } catch (Exception e) {}
    }

    @Test()
    public void testDecodedVersionByte() {
        assertEquals(StrKey.decodeVersionByte("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR"), StrKey.VersionByte.ACCOUNT_ID);
        assertEquals(StrKey.decodeVersionByte("SDJHRQF4GCMIIKAAAQ6IHY42X73FQFLHUULAPSKKD4DFDM7UXWWCRHBE"), StrKey.VersionByte.SEED);
        assertEquals(StrKey.decodeVersionByte("TAQCSRX2RIDJNHFIFHWD63X7D7D6TRT5Y2S6E3TEMXTG5W3OECHZ2OG4"), StrKey.VersionByte.PRE_AUTH_TX);
        assertEquals(StrKey.decodeVersionByte("XDRPF6NZRR7EEVO7ESIWUDXHAOMM2QSKIQQBJK6I2FB7YKDZES5UCLWD"), StrKey.VersionByte.SHA256_HASH);
    }

    @Test()
    public void testDecodedVersionByteThrows() {
        try {
            StrKey.decodeVersionByte("INVALIDBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6INVALID");
            fail();
        } catch (FormatException e) {
            assertEquals("Version byte is invalid", e.getMessage());
        }
    }


    // TODO more tests
}
