package org.stellar.sdk;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.stellar.sdk.xdr.AccountID;
import org.stellar.sdk.xdr.CryptoKeyType;
import org.stellar.sdk.xdr.MuxedAccount;

public class StrKeyTest {
  @Test
  public void testDecodeEncode() {

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
    } catch (IllegalArgumentException e) {
    }
  }

  @Test()
  public void testDecodeInvalidSeed() {
    String seed = "SAA6NXOBOXP3RXGAXBW6PGFI5BPK4ODVAWITS4VDOMN5C2M4B66ZML";
    try {
      StrKey.decodeCheck(StrKey.VersionByte.SEED, seed.toCharArray());
      fail();
    } catch (Exception e) {
    }
  }

  @Test()
  public void testDecodedVersionByte() {
    assertEquals(
        StrKey.decodeVersionByte("GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR"),
        StrKey.VersionByte.ACCOUNT_ID);
    assertEquals(
        StrKey.decodeVersionByte("SDJHRQF4GCMIIKAAAQ6IHY42X73FQFLHUULAPSKKD4DFDM7UXWWCRHBE"),
        StrKey.VersionByte.SEED);
    assertEquals(
        StrKey.decodeVersionByte(
            "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVAAAAAAAAAAAAAJLK"),
        StrKey.VersionByte.MED25519_PUBLIC_KEY);
    assertEquals(
        StrKey.decodeVersionByte("TAQCSRX2RIDJNHFIFHWD63X7D7D6TRT5Y2S6E3TEMXTG5W3OECHZ2OG4"),
        StrKey.VersionByte.PRE_AUTH_TX);
    assertEquals(
        StrKey.decodeVersionByte("XDRPF6NZRR7EEVO7ESIWUDXHAOMM2QSKIQQBJK6I2FB7YKDZES5UCLWD"),
        StrKey.VersionByte.SHA256_HASH);
    assertEquals(
        StrKey.decodeVersionByte(
            "PDPYP7E6NEYZSVOTV6M23OFM2XRIMPDUJABHGHHH2Y67X7JL25GW6AAAAAAAAAAAAAAJEVA"),
        StrKey.VersionByte.SIGNED_PAYLOAD);
    assertEquals(
        StrKey.decodeVersionByte("CA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUWDA"),
        StrKey.VersionByte.CONTRACT);
  }

  @Test()
  public void testDecodedVersionByteThrows() {
    try {
      StrKey.decodeVersionByte("INVALIDBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6INVALID");
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Version byte is invalid", e.getMessage());
    }
  }

  private byte[] rawBytes(int... values) {
    byte[] data = new byte[values.length];
    for (int i = 0; i < values.length; i++) {
      data[i] = (byte) values[i];
    }
    return data;
  }

  @Test
  public void testRoundTripAccountIdFromBytes() {
    byte[] data =
        rawBytes(
            0x36, 0x3e, 0xaa, 0x38, 0x67, 0x84, 0x1f, 0xba, 0xd0, 0xf4, 0xed, 0x88, 0xc7, 0x79,
            0xe4, 0xfe, 0x66, 0xe5, 0x6a, 0x24, 0x70, 0xdc, 0x98, 0xc0, 0xec, 0x9c, 0x07, 0x3d,
            0x05, 0xc7, 0xb1, 0x03);
    String accountId = "GA3D5KRYM6CB7OWQ6TWYRR3Z4T7GNZLKERYNZGGA5SOAOPIFY6YQHES5";
    assertEquals(
        accountId, String.valueOf(StrKey.encodeCheck(StrKey.VersionByte.ACCOUNT_ID, data)));
    assertArrayEquals(
        data, StrKey.decodeCheck(StrKey.VersionByte.ACCOUNT_ID, accountId.toCharArray()));
  }

  @Test
  public void testRoundTripSeedFromBytes() {
    byte[] data =
        rawBytes(
            0x69, 0xa8, 0xc4, 0xcb, 0xb9, 0xf6, 0x4e, 0x8a, 0x07, 0x98, 0xf6, 0xe1, 0xac, 0x65,
            0xd0, 0x6c, 0x31, 0x62, 0x92, 0x90, 0x56, 0xbc, 0xf4, 0xcd, 0xb7, 0xd3, 0x73, 0x8d,
            0x18, 0x55, 0xf3, 0x63);
    String seed = "SBU2RRGLXH3E5CQHTD3ODLDF2BWDCYUSSBLLZ5GNW7JXHDIYKXZWHOKR";
    assertEquals(seed, String.valueOf(StrKey.encodeCheck(StrKey.VersionByte.SEED, data)));
    assertArrayEquals(data, StrKey.decodeCheck(StrKey.VersionByte.SEED, seed.toCharArray()));
  }

  @Test
  public void testRoundTripHashTxFromBytes() {
    byte[] data =
        rawBytes(
            0x69, 0xa8, 0xc4, 0xcb, 0xb9, 0xf6, 0x4e, 0x8a, 0x07, 0x98, 0xf6, 0xe1, 0xac, 0x65,
            0xd0, 0x6c, 0x31, 0x62, 0x92, 0x90, 0x56, 0xbc, 0xf4, 0xcd, 0xb7, 0xd3, 0x73, 0x8d,
            0x18, 0x55, 0xf3, 0x63);
    String hashTx = "TBU2RRGLXH3E5CQHTD3ODLDF2BWDCYUSSBLLZ5GNW7JXHDIYKXZWHXL7";
    assertEquals(hashTx, String.valueOf(StrKey.encodeCheck(StrKey.VersionByte.PRE_AUTH_TX, data)));
    assertArrayEquals(
        data, StrKey.decodeCheck(StrKey.VersionByte.PRE_AUTH_TX, hashTx.toCharArray()));
  }

  @Test
  public void testRoundTripHashXFromBytes() {
    byte[] data =
        rawBytes(
            0x69, 0xa8, 0xc4, 0xcb, 0xb9, 0xf6, 0x4e, 0x8a, 0x07, 0x98, 0xf6, 0xe1, 0xac, 0x65,
            0xd0, 0x6c, 0x31, 0x62, 0x92, 0x90, 0x56, 0xbc, 0xf4, 0xcd, 0xb7, 0xd3, 0x73, 0x8d,
            0x18, 0x55, 0xf3, 0x63);
    String hashX = "XBU2RRGLXH3E5CQHTD3ODLDF2BWDCYUSSBLLZ5GNW7JXHDIYKXZWGTOG";
    assertEquals(hashX, String.valueOf(StrKey.encodeCheck(StrKey.VersionByte.SHA256_HASH, data)));
    assertArrayEquals(
        data, StrKey.decodeCheck(StrKey.VersionByte.SHA256_HASH, hashX.toCharArray()));
  }

  @Test
  public void testRoundTripSignedPayloadVersionByte() {
    byte[] data =
        rawBytes(
            // ed25519
            0x36,
            0x3e,
            0xaa,
            0x38,
            0x67,
            0x84,
            0x1f,
            0xba,
            0xd0,
            0xf4,
            0xed,
            0x88,
            0xc7,
            0x79,
            0xe4,
            0xfe,
            0x66,
            0xe5,
            0x6a,
            0x24,
            0x70,
            0xdc,
            0x98,
            0xc0,
            0xec,
            0x9c,
            0x07,
            0x3d,
            0x05,
            0xc7,
            0xb1,
            0x03,
            // payload length
            0x00,
            0x00,
            0x00,
            0x09,
            // payload
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            0x00,
            // padding
            0x00,
            0x00,
            0x00);

    String hashX =
        "PA3D5KRYM6CB7OWQ6TWYRR3Z4T7GNZLKERYNZGGA5SOAOPIFY6YQGAAAAAEQAAAAAAAAAAAAAAAAAABBXA";
    assertEquals(
        hashX, String.valueOf(StrKey.encodeCheck(StrKey.VersionByte.SIGNED_PAYLOAD, data)));
    assertArrayEquals(
        data, StrKey.decodeCheck(StrKey.VersionByte.SIGNED_PAYLOAD, hashX.toCharArray()));
  }

  @Test
  public void testRoundTripContract() {
    byte[] data =
        rawBytes(
            0x3f, 0x0c, 0x34, 0xbf, 0x93, 0xad, 0x0d, 0x99, 0x71, 0xd0, 0x4c, 0xcc, 0x90, 0xf7,
            0x05, 0x51, 0x1c, 0x83, 0x8a, 0xad, 0x97, 0x34, 0xa4, 0xa2, 0xfb, 0x0d, 0x7a, 0x03,
            0xfc, 0x7f, 0xe8, 0x9a);

    String encoded = "CA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUWDA";
    assertEquals(encoded, StrKey.encodeContract(data));
    assertArrayEquals(data, StrKey.decodeContract(encoded));
  }

  @Test
  public void testDecodeEmpty() {
    try {
      StrKey.decodeCheck(StrKey.VersionByte.ACCOUNT_ID, "".toCharArray());
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Encoded char array must have a length of at least 5.", e.getMessage());
    }
  }

  @Test
  public void testCorruptedChecksum() {
    try {
      StrKey.decodeCheck(
          StrKey.VersionByte.ACCOUNT_ID,
          "GA3D5KRYM6CB7OWQ6TWYRR3Z4T7GNZLKERYNZGGA5SOAOPIFY6YQHE55".toCharArray());
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Checksum invalid", e.getMessage());
    }
  }

  @Test
  public void testCorruptedPayload() {
    try {
      StrKey.decodeCheck(
          StrKey.VersionByte.ACCOUNT_ID,
          "GA3D5KRYM6CB7OWOOOORR3Z4T7GNZLKERYNZGGA5SOAOPIFY6YQHES5".toCharArray());
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Unused bits should be set to 0.", e.getMessage());
    }
  }

  @Test
  public void testEncodeToXDRMuxedAccountMAddress() {
    String unmuxedAddress = "GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVSGZ";
    AccountID account = StrKey.encodeToXDRAccountId(unmuxedAddress);

    String muxedAddress = "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVAAAAAAAAAAAAAJLK";
    MuxedAccount muxedAccount = StrKey.encodeToXDRMuxedAccount(muxedAddress);
    assertEquals(CryptoKeyType.KEY_TYPE_MUXED_ED25519, muxedAccount.getDiscriminant());
    assertEquals(account.getAccountID().getEd25519(), muxedAccount.getMed25519().getEd25519());
    assertEquals(
        -9223372036854775808L,
        muxedAccount.getMed25519().getId().getUint64().getNumber().longValue());

    assertEquals(muxedAddress, StrKey.encodeMuxedAccount(muxedAccount));
  }

  @Test
  public void testEncodeAccountIdToMuxed() {
    String unmuxedAddress = "GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVSGZ";
    AccountID account = StrKey.encodeToXDRAccountId(unmuxedAddress);

    MuxedAccount muxedAccount = StrKey.encodeToXDRMuxedAccount(unmuxedAddress);
    assertEquals(CryptoKeyType.KEY_TYPE_ED25519, muxedAccount.getDiscriminant());
    assertEquals(account.getAccountID().getEd25519(), muxedAccount.getEd25519());
  }

  @Test
  public void testEncodeToXDRMuxedAccountInvalidAddress() {
    // https://github.com/stellar/go/blob/2b876cd781b6dd0c218dcdd4f300900f87b3889e/strkey/main_test.go#L86
    try {
      StrKey.encodeToXDRMuxedAccount("XBU2RRGLXH3E5CQHTD3ODLDF2BWDCYUSSBLLZ5GNW7JXHDIYKXZWGTOG");
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Version byte is invalid", e.getMessage());
    }

    try {
      StrKey.encodeToXDRMuxedAccount("MBU2RRGLXH3E5CQHTD3ODLDF2BWDCYUSSBLLZ5GNW7JXHDIYKXZWGTOG");
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid data length, expected 40 bytes, got 32", e.getMessage());
    }

    try {
      StrKey.encodeToXDRMuxedAccount(
          "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAAAAAAAACJUR");
      fail();
    } catch (IllegalArgumentException ignored) {
    }

    try {
      StrKey.encodeToXDRMuxedAccount("GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVSGZA");
      fail();
    } catch (IllegalArgumentException ignored) {
    }

    try {
      StrKey.encodeToXDRMuxedAccount("G47QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVP2I");
      fail();
    } catch (IllegalArgumentException ignored) {
    }

    try {
      StrKey.encodeToXDRMuxedAccount(
          "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVAAAAAAAAAAAAAJLKA");
      fail();
    } catch (IllegalArgumentException ignored) {
    }

    try {
      StrKey.encodeToXDRMuxedAccount(
          "M47QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAAAAAAAACJUQ");
      fail();
    } catch (IllegalArgumentException ignored) {
    }

    try {
      StrKey.encodeToXDRMuxedAccount(
          "M47QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAAAAAAAACJUQ");
      fail();
    } catch (IllegalArgumentException ignored) {
    }

    try {
      StrKey.encodeToXDRMuxedAccount(
          "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAAAAAAAACJUK");
      fail();
    } catch (IllegalArgumentException ignored) {
    }

    try {
      StrKey.encodeToXDRMuxedAccount(
          "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAAAAAAAACJUO");
      fail();
    } catch (IllegalArgumentException ignored) {
    }

    try {
      StrKey.encodeToXDRMuxedAccount("");
      fail();
    } catch (IllegalArgumentException ignored) {
    }

    try {
      StrKey.encodeToXDRMuxedAccount("SBCVMMCBEDB64TVJZFYJOJAERZC4YVVUOE6SYR2Y76CBTENGUSGWRRVO");
      fail();
    } catch (IllegalArgumentException ignored) {
    }

    try {
      StrKey.encodeToXDRMuxedAccount(
          "MCEO75Y6YKE53HM6N46IJYH3LK3YYFZ4QWGNUKCSSIQSH3KOAD7BEAAAAAAAAAAAPNT2W___");
      fail();
    } catch (IllegalArgumentException ignored) {
    }

    try {
      StrKey.encodeToXDRMuxedAccount(
          "MDWZCOEQRODFCH6ISYQPWY67L3ULLWS5ISXYYL5GH43W7YFMTLB64AAAAAAAAAAAAHGLW===");
      fail();
    } catch (IllegalArgumentException ignored) {
    }

    try {
      StrKey.encodeToXDRMuxedAccount(
          " MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVAAAAAAAAAAAAAJLK");
      fail();
    } catch (IllegalArgumentException ignored) {
    }
  }

  @Test
  public void testEncodeAndDecodeEd25519PublicKey() {
    byte[] rawData = {
      (byte) 0x52,
      (byte) 0x23,
      (byte) 0xd1,
      (byte) 0x59,
      (byte) 0x64,
      (byte) 0xcb,
      (byte) 0x25,
      (byte) 0xb9,
      (byte) 0x8d,
      (byte) 0x17,
      (byte) 0xdf,
      (byte) 0xc9,
      (byte) 0xcb,
      (byte) 0x95,
      (byte) 0x4a,
      (byte) 0x43,
      (byte) 0x31,
      (byte) 0x61,
      (byte) 0x7b,
      (byte) 0xba,
      (byte) 0xa4,
      (byte) 0xe5,
      (byte) 0xdc,
      (byte) 0x14,
      (byte) 0x4c,
      (byte) 0x87,
      (byte) 0xdf,
      (byte) 0xb,
      (byte) 0x8b,
      (byte) 0x3b,
      (byte) 0x47,
      (byte) 0xd9
    };
    String strKey = "GBJCHUKZMTFSLOMNC7P4TS4VJJBTCYL3XKSOLXAUJSD56C4LHND5TWUC";
    assertEquals(strKey, StrKey.encodeEd25519PublicKey(rawData));
    assertArrayEquals(rawData, StrKey.decodeEd25519PublicKey(strKey));
  }

  @Test
  public void testEncodeAndDecodeEd25519SecretSeed() {
    byte[] rawData = {
      (byte) 0x7d,
      (byte) 0xd6,
      (byte) 0x1c,
      (byte) 0xa0,
      (byte) 0xd6,
      (byte) 0x77,
      (byte) 0xcd,
      (byte) 0xe,
      (byte) 0xfc,
      (byte) 0x2b,
      (byte) 0x54,
      (byte) 0x73,
      (byte) 0xe9,
      (byte) 0x42,
      (byte) 0x6c,
      (byte) 0x7a,
      (byte) 0x98,
      (byte) 0xb3,
      (byte) 0xe6,
      (byte) 0x45,
      (byte) 0x67,
      (byte) 0x68,
      (byte) 0x2d,
      (byte) 0x46,
      (byte) 0xee,
      (byte) 0x7c,
      (byte) 0xbd,
      (byte) 0x80,
      (byte) 0x7,
      (byte) 0xe5,
      (byte) 0x32,
      (byte) 0xbc
    };
    String strKey = "SB65MHFA2Z342DX4FNKHH2KCNR5JRM7GIVTWQLKG5Z6L3AAH4UZLZV4E";
    assertEquals(strKey, String.valueOf(StrKey.encodeEd25519SecretSeed(rawData)));
    assertArrayEquals(rawData, StrKey.decodeEd25519SecretSeed(strKey.toCharArray()));
  }

  @Test
  public void isValidEd25519SecretSeed() {
    String validSeed = "SB65MHFA2Z342DX4FNKHH2KCNR5JRM7GIVTWQLKG5Z6L3AAH4UZLZV4E";
    assertTrue(StrKey.isValidEd25519SecretSeed(validSeed.toCharArray()));
    String[] invalidSeeds = {
      "SB65MHFA2Z342DX4FNKHH2KCNR5JRM7GIVTWQLKG5Z6L3AAH4UZLZV4F",
      "SB65MHFA2Z342DX4FNKHH2KCNR5JRM7GIVTWQLKG5Z6L3AAH4UZLZV",
      "SB65MHFA2Z342DX4FNKHH2KCNR5JRM7GIVTWQLKG5Z6L3AAH4UZLZV4E2",
      "SB65MHFA2Z342DX4FNKHH2KCNR5JRM7GIVTWQLKG5Z6L3AAH4UZLZV4E!",
      "GBJCHUKZMTFSLOMNC7P4TS4VJJBTCYL3XKSOLXAUJSD56C4LHND5TWUC"
    };
    for (String seed : invalidSeeds) {
      assertFalse(StrKey.isValidEd25519SecretSeed(seed.toCharArray()));
    }
  }

  @Test
  public void testEncodeAndDecodePreAuthTx() {
    byte[] rawData = {
      (byte) 0x7d,
      (byte) 0xd6,
      (byte) 0x1c,
      (byte) 0xa0,
      (byte) 0xd6,
      (byte) 0x77,
      (byte) 0xcd,
      (byte) 0xe,
      (byte) 0xfc,
      (byte) 0x2b,
      (byte) 0x54,
      (byte) 0x73,
      (byte) 0xe9,
      (byte) 0x42,
      (byte) 0x6c,
      (byte) 0x7a,
      (byte) 0x98,
      (byte) 0xb3,
      (byte) 0xe6,
      (byte) 0x45,
      (byte) 0x67,
      (byte) 0x68,
      (byte) 0x2d,
      (byte) 0x46,
      (byte) 0xee,
      (byte) 0x7c,
      (byte) 0xbd,
      (byte) 0x80,
      (byte) 0x7,
      (byte) 0xe5,
      (byte) 0x32,
      (byte) 0xbc
    };
    String strKey = "TB65MHFA2Z342DX4FNKHH2KCNR5JRM7GIVTWQLKG5Z6L3AAH4UZLZM5K";
    assertEquals(strKey, StrKey.encodePreAuthTx(rawData));
    assertArrayEquals(rawData, StrKey.decodePreAuthTx(strKey));
  }

  @Test
  public void isValidPreAuthTx() {
    String validKey = "TB65MHFA2Z342DX4FNKHH2KCNR5JRM7GIVTWQLKG5Z6L3AAH4UZLZM5K";
    assertTrue(StrKey.isValidPreAuthTx(validKey));
    String[] invalidKeys = {
      "TB65MHFA2Z342DX4FNKHH2KCNR5JRM7GIVTWQLKG5Z6L3AAH4UZLZM5",
      "TB65MHFA2Z342DX4FNKHH2KCNR5JRM7GIVTWQLKG5Z6L3AAH4UZLZM5K2",
      "GBJCHUKZMTFSLOMNC7P4TS4VJJBTCYL3XKSOLXAUJSD56C4LHND5TWUC"
    };
    for (String key : invalidKeys) {
      assertFalse(StrKey.isValidPreAuthTx(key));
    }
  }

  @Test
  public void testEncodeAndDecodeSha256Hash() {
    byte[] rawData = {
      (byte) 0x7d,
      (byte) 0xd6,
      (byte) 0x1c,
      (byte) 0xa0,
      (byte) 0xd6,
      (byte) 0x77,
      (byte) 0xcd,
      (byte) 0xe,
      (byte) 0xfc,
      (byte) 0x2b,
      (byte) 0x54,
      (byte) 0x73,
      (byte) 0xe9,
      (byte) 0x42,
      (byte) 0x6c,
      (byte) 0x7a,
      (byte) 0x98,
      (byte) 0xb3,
      (byte) 0xe6,
      (byte) 0x45,
      (byte) 0x67,
      (byte) 0x68,
      (byte) 0x2d,
      (byte) 0x46,
      (byte) 0xee,
      (byte) 0x7c,
      (byte) 0xbd,
      (byte) 0x80,
      (byte) 0x7,
      (byte) 0xe5,
      (byte) 0x32,
      (byte) 0xbc
    };
    String strKey = "XB65MHFA2Z342DX4FNKHH2KCNR5JRM7GIVTWQLKG5Z6L3AAH4UZLYIYT";
    assertEquals(strKey, StrKey.encodeSha256Hash(rawData));
    assertArrayEquals(rawData, StrKey.decodeSha256Hash(strKey));
  }

  @Test
  public void isValidSha256Hash() {
    String validKey = "XB65MHFA2Z342DX4FNKHH2KCNR5JRM7GIVTWQLKG5Z6L3AAH4UZLYIYT";
    assertTrue(StrKey.isValidSha256Hash(validKey));
    String[] invalidKeys = {
      "XB65MHFA2Z342DX4FNKHH2KCNR5JRM7GIVTWQLKG5Z6L3AAH4UZLYIY",
      "XB65MHFA2Z342DX4FNKHH2KCNR5JRM7GIVTWQLKG5Z6L3AAH4UZLYIYT2",
      "GBJCHUKZMTFSLOMNC7P4TS4VJJBTCYL3XKSOLXAUJSD56C4LHND5TWUC"
    };
    for (String key : invalidKeys) {
      assertFalse(StrKey.isValidSha256Hash(key));
    }
  }

  @Test
  public void testEncodeAndDecodeContract() {
    byte[] rawData = {
      (byte) 0x7d,
      (byte) 0xd6,
      (byte) 0x1c,
      (byte) 0xa0,
      (byte) 0xd6,
      (byte) 0x77,
      (byte) 0xcd,
      (byte) 0xe,
      (byte) 0xfc,
      (byte) 0x2b,
      (byte) 0x54,
      (byte) 0x73,
      (byte) 0xe9,
      (byte) 0x42,
      (byte) 0x6c,
      (byte) 0x7a,
      (byte) 0x98,
      (byte) 0xb3,
      (byte) 0xe6,
      (byte) 0x45,
      (byte) 0x67,
      (byte) 0x68,
      (byte) 0x2d,
      (byte) 0x46,
      (byte) 0xee,
      (byte) 0x7c,
      (byte) 0xbd,
      (byte) 0x80,
      (byte) 0x7,
      (byte) 0xe5,
      (byte) 0x32,
      (byte) 0xbc
    };
    String strKey = "CB65MHFA2Z342DX4FNKHH2KCNR5JRM7GIVTWQLKG5Z6L3AAH4UZLZVKC";
    assertEquals(strKey, StrKey.encodeContract(rawData));
    assertArrayEquals(rawData, StrKey.decodeContract(strKey));
  }

  @Test
  public void testIsValidContract() {
    String validKey = "CB65MHFA2Z342DX4FNKHH2KCNR5JRM7GIVTWQLKG5Z6L3AAH4UZLZVKC";
    assertTrue(StrKey.isValidContract(validKey));
    String[] invalidKeys = {
      "CB65MHFA2Z342DX4FNKHH2KCNR5JRM7GIVTWQLKG5Z6L3AAH4UZLZVK",
      "CB65MHFA2Z342DX4FNKHH2KCNR5JRM7GIVTWQLKG5Z6L3AAH4UZLZVKC2",
      "GBJCHUKZMTFSLOMNC7P4TS4VJJBTCYL3XKSOLXAUJSD56C4LHND5TWUC"
    };
    for (String key : invalidKeys) {
      assertFalse(StrKey.isValidContract(key));
    }
  }

  @Test
  public void testEncodeAndDecodeLiquidityPool() {
    byte[] rawData =
        Util.hexToBytes("3f0c34bf93ad0d9971d04ccc90f705511c838aad9734a4a2fb0d7a03fc7fe89a");
    String strKey = "LA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUPJN";
    assertEquals(strKey, StrKey.encodeLiquidityPool(rawData));
    assertArrayEquals(rawData, StrKey.decodeLiquidityPool(strKey));
  }

  @Test
  public void testIsValidLiquidityPool() {
    assertFalse(StrKey.isValidLiquidityPool("LA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA"));
    assertFalse(StrKey.isValidLiquidityPool(""));
    assertFalse(
        StrKey.isValidLiquidityPool("SBCVMMCBEDB64TVJZFYJOJAERZC4YVVUOE6SYR2Y76CBTENGUSGWRRVO"));
    assertFalse(
        StrKey.isValidLiquidityPool(
            "MAQAA5L65LSYH7CQ3VTJ7F3HHLGCL3DSLAR2Y47263D56MNNGHSQSAAAAAAAAAAE2LP26"));
    assertTrue(
        StrKey.isValidLiquidityPool("LA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUPJN"));
  }

  @Test
  public void testEncodeAndDecodeClaimableBalance() {
    byte[] rawData =
        Util.hexToBytes("003f0c34bf93ad0d9971d04ccc90f705511c838aad9734a4a2fb0d7a03fc7fe89a");
    String strKey = "BAAD6DBUX6J22DMZOHIEZTEQ64CVCHEDRKWZONFEUL5Q26QD7R76RGR4TU";
    assertEquals(strKey, StrKey.encodeClaimableBalance(rawData));
    assertArrayEquals(rawData, StrKey.decodeClaimableBalance(strKey));
  }

  @Test
  public void tesIsValidClaimableBalance() {
    assertFalse(
        StrKey.isValidClaimableBalance(
            "BAAD6DBUX6J22DMZOHIEZTEQ64CVCHEDRKWZONFEUL5Q26QD7R76RGR4T"));
    assertFalse(StrKey.isValidClaimableBalance(""));
    assertFalse(
        StrKey.isValidClaimableBalance("SBCVMMCBEDB64TVJZFYJOJAERZC4YVVUOE6SYR2Y76CBTENGUSGWRRVO"));
    assertFalse(
        StrKey.isValidClaimableBalance(
            "MAQAA5L65LSYH7CQ3VTJ7F3HHLGCL3DSLAR2Y47263D56MNNGHSQSAAAAAAAAAAE2LP26"));
    assertTrue(
        StrKey.isValidClaimableBalance(
            "BAAD6DBUX6J22DMZOHIEZTEQ64CVCHEDRKWZONFEUL5Q26QD7R76RGR4TU"));
  }

  @Test
  public void testEncodeAndDecodeMed25519PublicKey() {
    byte[] rawData =
        Util.hexToBytes(
            "2000757eeae583fc50dd669f97673acc25ec725823ac73faf6c7df31ad31e50900000000000004d2");
    String strKey = "MAQAA5L65LSYH7CQ3VTJ7F3HHLGCL3DSLAR2Y47263D56MNNGHSQSAAAAAAAAAAE2LP26";
    assertEquals(strKey, StrKey.encodeMed25519PublicKey(rawData));
    assertArrayEquals(rawData, StrKey.decodeMed25519PublicKey(strKey));
  }

  @Test
  public void testIsValidMed25519PublicKey() {
    String[] invalidKeys = {
      "XBU2RRGLXH3E5CQHTD3ODLDF2BWDCYUSSBLLZ5GNW7JXHDIYKXZWGTOG",
      "MBU2RRGLXH3E5CQHTD3ODLDF2BWDCYUSSBLLZ5GNW7JXHDIYKXZWGTOG",
      "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAAAAAAAACJUR",
      "GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVSGZA",
      "G47QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVP2I",
      "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVAAAAAAAAAAAAAJLKA",
      "M47QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAAAAAAAACJUQ",
      "M47QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAAAAAAAACJUQ",
      "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAAAAAAAACJUK",
      "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAAAAAAAACJUO",
      "SBCVMMCBEDB64TVJZFYJOJAERZC4YVVUOE6SYR2Y76CBTENGUSGWRRVO",
      "MCEO75Y6YKE53HM6N46IJYH3LK3YYFZ4QWGNUKCSSIQSH3KOAD7BEAAAAAAAAAAAPNT2W___",
      "MDWZCOEQRODFCH6ISYQPWY67L3ULLWS5ISXYYL5GH43W7YFMTLB64AAAAAAAAAAAAHGLW===",
      " MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVAAAAAAAAAAAAAJLK"
    };
    for (String key : invalidKeys) {
      assertFalse(StrKey.isValidMed25519PublicKey(key));
    }

    String[] validKeys = {
      "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAAAAAAAACJUQ",
      "MAQAA5L65LSYH7CQ3VTJ7F3HHLGCL3DSLAR2Y47263D56MNNGHSQSAAAAAAAAAAE2LP26"
    };
    for (String key : validKeys) {
      assertTrue(StrKey.isValidMed25519PublicKey(key));
    }
  }

  @Test
  public void testEncodeAndDecodeSignedPayload() {
    byte[] rawData =
        Util.hexToBytes(
            "363eaa3867841fbad0f4ed88c779e4fe66e56a2470dc98c0ec9c073d05c7b10300000009000000000000000000000000");
    String strKey =
        "PA3D5KRYM6CB7OWQ6TWYRR3Z4T7GNZLKERYNZGGA5SOAOPIFY6YQGAAAAAEQAAAAAAAAAAAAAAAAAABBXA";
    assertEquals(strKey, StrKey.encodeSignedPayload(rawData));
    assertArrayEquals(rawData, StrKey.decodeSignedPayload(strKey));
  }

  @Test
  public void testIsValidSignedPayload() {
    String[] validKeys = {
      "PA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAQACAQDAQCQMBYIBEFAWDANBYHRAEISCMKBKFQXDAMRUGY4DUPB6IBZGM",
      "PA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAOQCAQDAQCQMBYIBEFAWDANBYHRAEISCMKBKFQXDAMRUGY4DUAAAAFGBU"
    };
    for (String key : validKeys) {
      assertTrue(StrKey.isValidSignedPayload(key));
    }

    String[] invalidKeys = {
      "PA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAQACAQDAQCQMBYIBEFAWDANBYHRAEISCMKBKFQXDAMRUGY4DUPB6IAAAAAAAAPM",
      "PA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAOQCAQDAQCQMBYIBEFAWDANBYHRAEISCMKBKFQXDAMRUGY4Z2PQ",
      "PA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAOQCAQDAQCQMBYIBEFAWDANBYHRAEISCMKBKFQXDAMRUGY4DXFH6",
      "",
      "PDPYP7E6NEYZSVOTV6M23OFM2XRIMPDUJABHGHHH2Y67X7JL25GW6AAAAAAAAAAAAAAJE",
      "GAQAA5L65LSYH7CQ3VTJ7F3HHLGCL3DSLAR2Y47263D56MNNGHSQSTVY",
      "PA3D5KRYM6CB7OWQ6TWYRR3Z4T7GNZLKERYNZGGA5SOAOPIFY6YQGAAAAAAGK7I"
    };
    for (String key : invalidKeys) {
      assertFalse(key, StrKey.isValidSignedPayload(key));
    }
  }
}
