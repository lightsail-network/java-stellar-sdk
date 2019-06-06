package org.stellar.sdk;


import org.junit.jupiter.api.Test;


import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        assertThrows(FormatException.class, () -> StrKey.decodeCheck(StrKey.VersionByte.SEED, address.toCharArray()));
    }

    @Test()
    public void testDecodeInvalidSeed() {
        String seed = "SAA6NXOBOXP3RXGAXBW6PGFI5BPK4ODVAWITS4VDOMN5C2M4B66ZML";
        assertThrows(IllegalArgumentException.class, () -> StrKey.decodeCheck(StrKey.VersionByte.SEED, seed.toCharArray()));
    }

    // TODO more tests
}
