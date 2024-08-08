package org.stellar.sdk;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.stellar.sdk.exception.StrKeyException;
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
    } catch (StrKeyException e) {
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
        StrKey.VersionByte.MUXED);
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
    } catch (StrKeyException e) {
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
  public void testValidSignedPayloadEncode() {
    // Valid signed payload with an ed25519 public key and a 32-byte payload.
    byte[] payload =
        Util.hexToBytes(
            "0102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f20".toUpperCase());
    SignedPayloadSigner signedPayloadSigner =
        new SignedPayloadSigner(
            StrKey.decodeEd25519PublicKey(
                "GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVSGZ"),
            payload);
    String encoded = StrKey.encodeSignedPayload(signedPayloadSigner);
    assertEquals(
        encoded,
        "PA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAQACAQDAQCQMBYIBEFAWDANBYHRAEISCMKBKFQXDAMRUGY4DUPB6IBZGM");

    // Valid signed payload with an ed25519 public key and a 29-byte payload.
    payload =
        Util.hexToBytes("0102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d".toUpperCase());
    signedPayloadSigner =
        new SignedPayloadSigner(
            StrKey.decodeEd25519PublicKey(
                "GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVSGZ"),
            payload);
    encoded = StrKey.encodeSignedPayload(signedPayloadSigner);
    assertEquals(
        encoded,
        "PA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAOQCAQDAQCQMBYIBEFAWDANBYHRAEISCMKBKFQXDAMRUGY4DUAAAAFGBU");
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
    } catch (StrKeyException e) {
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
    } catch (StrKeyException e) {
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
    } catch (StrKeyException e) {
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
    } catch (StrKeyException e) {
      assertEquals("Version byte is invalid", e.getMessage());
    }

    try {
      StrKey.encodeToXDRMuxedAccount("MBU2RRGLXH3E5CQHTD3ODLDF2BWDCYUSSBLLZ5GNW7JXHDIYKXZWGTOG");
      fail();
    } catch (StrKeyException e) {
      assertEquals("Checksum invalid", e.getMessage());
    }

    try {
      StrKey.encodeToXDRMuxedAccount(
          "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAAAAAAAACJUR");
      fail();
    } catch (StrKeyException ignored) {
    }

    try {
      StrKey.encodeToXDRMuxedAccount("GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVSGZA");
      fail();
    } catch (StrKeyException ignored) {
    }

    try {
      StrKey.encodeToXDRMuxedAccount("G47QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVP2I");
      fail();
    } catch (StrKeyException ignored) {
    }

    try {
      StrKey.encodeToXDRMuxedAccount(
          "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVAAAAAAAAAAAAAJLKA");
      fail();
    } catch (StrKeyException ignored) {
    }

    try {
      StrKey.encodeToXDRMuxedAccount(
          "M47QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAAAAAAAACJUQ");
      fail();
    } catch (StrKeyException ignored) {
    }

    try {
      StrKey.encodeToXDRMuxedAccount(
          "M47QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAAAAAAAACJUQ");
      fail();
    } catch (StrKeyException ignored) {
    }

    try {
      StrKey.encodeToXDRMuxedAccount(
          "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAAAAAAAACJUK");
      fail();
    } catch (StrKeyException ignored) {
    }

    try {
      StrKey.encodeToXDRMuxedAccount(
          "MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUAAAAAAAAAAAACJUO");
      fail();
    } catch (StrKeyException ignored) {
    }

    try {
      StrKey.encodeToXDRMuxedAccount("");
      fail();
    } catch (StrKeyException ignored) {
    }

    try {
      StrKey.encodeToXDRMuxedAccount("SBCVMMCBEDB64TVJZFYJOJAERZC4YVVUOE6SYR2Y76CBTENGUSGWRRVO");
      fail();
    } catch (StrKeyException ignored) {
    }

    try {
      StrKey.encodeToXDRMuxedAccount(
          "MCEO75Y6YKE53HM6N46IJYH3LK3YYFZ4QWGNUKCSSIQSH3KOAD7BEAAAAAAAAAAAPNT2W___");
      fail();
    } catch (StrKeyException ignored) {
    }

    try {
      StrKey.encodeToXDRMuxedAccount(
          "MDWZCOEQRODFCH6ISYQPWY67L3ULLWS5ISXYYL5GH43W7YFMTLB64AAAAAAAAAAAAHGLW===");
      fail();
    } catch (StrKeyException ignored) {
    }

    try {
      StrKey.encodeToXDRMuxedAccount(
          " MA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVAAAAAAAAAAAAAJLK");
      fail();
    } catch (StrKeyException ignored) {
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
  public void testEncodeAndDecodeSignedPayload() {
    AccountID accountID =
        KeyPair.fromAccountId("GBJCHUKZMTFSLOMNC7P4TS4VJJBTCYL3XKSOLXAUJSD56C4LHND5TWUC")
            .getXdrAccountId();
    byte[] payload = {
      (byte) 0xd5,
      (byte) 0xf1,
      (byte) 0x3d,
      (byte) 0xbd,
      (byte) 0x95,
      (byte) 0x5e,
      (byte) 0x99,
      (byte) 0xf9,
      (byte) 0xd
    };
    SignedPayloadSigner signedPayloadSigner = new SignedPayloadSigner(accountID, payload);
    String expected =
        "PBJCHUKZMTFSLOMNC7P4TS4VJJBTCYL3XKSOLXAUJSD56C4LHND5SAAAAAE5L4J5XWKV5GPZBUAAAAAYQ4";
    assertEquals(expected, StrKey.encodeSignedPayload(signedPayloadSigner));
    assertArrayEquals(payload, StrKey.decodeSignedPayload(expected).getPayload());
    assertEquals(accountID, StrKey.decodeSignedPayload(expected).getSignerAccountId());
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
}
