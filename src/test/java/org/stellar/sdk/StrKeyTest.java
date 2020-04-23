package org.stellar.sdk;

import com.google.common.primitives.UnsignedLongs;
import org.junit.Test;
import org.stellar.sdk.xdr.AccountID;
import org.stellar.sdk.xdr.CryptoKeyType;
import org.stellar.sdk.xdr.MuxedAccount;

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
    } catch (FormatException e) {
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
    assertEquals(StrKey.decodeVersionByte("MBU2RRGLXH3E5CQHTD3ODLDF2BWDCYUSSBLLZ5GNW7JXHDIYKXZWGTOG"), StrKey.VersionByte.MUXED_ACCOUNT);
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

  private byte[] rawBytes(int... values) {
    byte[] data = new byte[values.length];
    for (int i = 0; i < values.length; i++) {
      data[i] = (byte) values[i];
    }
    return data;
  }

  @Test
  public void testRoundTripAccountIdFromBytes() {
    byte[] data = rawBytes(
        0x36, 0x3e, 0xaa, 0x38, 0x67, 0x84, 0x1f, 0xba,
        0xd0, 0xf4, 0xed, 0x88, 0xc7, 0x79, 0xe4, 0xfe,
        0x66, 0xe5, 0x6a, 0x24, 0x70, 0xdc, 0x98, 0xc0,
        0xec, 0x9c, 0x07, 0x3d, 0x05, 0xc7, 0xb1, 0x03
    );
    String accountId = "GA3D5KRYM6CB7OWQ6TWYRR3Z4T7GNZLKERYNZGGA5SOAOPIFY6YQHES5";
    assertEquals(
        accountId,
        String.valueOf(StrKey.encodeCheck(StrKey.VersionByte.ACCOUNT_ID, data))
    );
    assertArrayEquals(data, StrKey.decodeCheck(StrKey.VersionByte.ACCOUNT_ID, accountId.toCharArray()));
  }

  @Test
  public void testRoundTripSeedFromBytes() {
    byte[] data = rawBytes(
        0x69, 0xa8, 0xc4, 0xcb, 0xb9, 0xf6, 0x4e, 0x8a,
        0x07, 0x98, 0xf6, 0xe1, 0xac, 0x65, 0xd0, 0x6c,
        0x31, 0x62, 0x92, 0x90, 0x56, 0xbc, 0xf4, 0xcd,
        0xb7, 0xd3, 0x73, 0x8d, 0x18, 0x55, 0xf3, 0x63
    );
    String seed = "SBU2RRGLXH3E5CQHTD3ODLDF2BWDCYUSSBLLZ5GNW7JXHDIYKXZWHOKR";
    assertEquals(
        seed,
        String.valueOf(StrKey.encodeCheck(StrKey.VersionByte.SEED, data))
    );
    assertArrayEquals(data, StrKey.decodeCheck(StrKey.VersionByte.SEED, seed.toCharArray()));
  }

  @Test
  public void testRoundTripHashTxFromBytes() {
    byte[] data = rawBytes(
        0x69, 0xa8, 0xc4, 0xcb, 0xb9, 0xf6, 0x4e, 0x8a,
        0x07, 0x98, 0xf6, 0xe1, 0xac, 0x65, 0xd0, 0x6c,
        0x31, 0x62, 0x92, 0x90, 0x56, 0xbc, 0xf4, 0xcd,
        0xb7, 0xd3, 0x73, 0x8d, 0x18, 0x55, 0xf3, 0x63
    );
    String hashTx = "TBU2RRGLXH3E5CQHTD3ODLDF2BWDCYUSSBLLZ5GNW7JXHDIYKXZWHXL7";
    assertEquals(
        hashTx,
        String.valueOf(StrKey.encodeCheck(StrKey.VersionByte.PRE_AUTH_TX, data))
    );
    assertArrayEquals(data, StrKey.decodeCheck(StrKey.VersionByte.PRE_AUTH_TX, hashTx.toCharArray()));
  }

  @Test
  public void testRoundTripHashXFromBytes() {
    byte[] data = rawBytes(
        0x69, 0xa8, 0xc4, 0xcb, 0xb9, 0xf6, 0x4e, 0x8a,
        0x07, 0x98, 0xf6, 0xe1, 0xac, 0x65, 0xd0, 0x6c,
        0x31, 0x62, 0x92, 0x90, 0x56, 0xbc, 0xf4, 0xcd,
        0xb7, 0xd3, 0x73, 0x8d, 0x18, 0x55, 0xf3, 0x63
    );
    String hashX = "XBU2RRGLXH3E5CQHTD3ODLDF2BWDCYUSSBLLZ5GNW7JXHDIYKXZWGTOG";
    assertEquals(
        hashX,
        String.valueOf(StrKey.encodeCheck(StrKey.VersionByte.SHA256_HASH, data))
    );
    assertArrayEquals(data, StrKey.decodeCheck(StrKey.VersionByte.SHA256_HASH, hashX.toCharArray()));
  }

  @Test
  public void testRoundTripMuxedFromBytes() {
    byte[] data = rawBytes(
        0x80, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x3f, 0x0c, 0x34, 0xbf, 0x93, 0xad, 0x0d, 0x99,
        0x71, 0xd0, 0x4c, 0xcc, 0x90, 0xf7, 0x05, 0x51,
        0x1c, 0x83, 0x8a, 0xad, 0x97, 0x34, 0xa4, 0xa2,
        0xfb, 0x0d, 0x7a, 0x03, 0xfc, 0x7f, 0xe8, 0x9a
    );
    String muxed = "MCAAAAAAAAAAAAB7BQ2L7E5NBWMXDUCMZSIPOBKRDSBYVLMXGSSKF6YNPIB7Y77ITKNOG";
    assertEquals(
        muxed,
        String.valueOf(StrKey.encodeCheck(StrKey.VersionByte.MUXED_ACCOUNT, data))
    );
    assertArrayEquals(data, StrKey.decodeCheck(StrKey.VersionByte.MUXED_ACCOUNT, muxed.toCharArray()));
  }


  @Test
  public void testDecodeEmpty() {
    try {
      StrKey.decodeCheck(StrKey.VersionByte.ACCOUNT_ID, "".toCharArray());
      fail();
    } catch (Exception e) {
      assertEquals("Encoded char array cannot be empty.", e.getMessage());
    }
  }

  @Test
  public void testCorruptedChecksum() {
    try {
      StrKey.decodeCheck(StrKey.VersionByte.ACCOUNT_ID, "GA3D5KRYM6CB7OWQ6TWYRR3Z4T7GNZLKERYNZGGA5SOAOPIFY6YQHE55".toCharArray());
      fail();
    } catch (Exception e) {
      assertEquals("Checksum invalid", e.getMessage());
    }
  }

  @Test
  public void testCorruptedPayload() {
    try {
      StrKey.decodeCheck(StrKey.VersionByte.ACCOUNT_ID, "GA3D5KRYM6CB7OWOOOORR3Z4T7GNZLKERYNZGGA5SOAOPIFY6YQHES5".toCharArray());
      fail();
    } catch (Exception e) {
      assertEquals("Unused bits should be set to 0.", e.getMessage());
    }
  }


  @Test
  public void testMuxedDecodeErrors() {
    // non-canonical representation due to extra character
    try {
      StrKey.decodeCheck(
          StrKey.VersionByte.MUXED_ACCOUNT,
          "MCAAAAAAAAAAAAB7BQ2L7E5NBWMXDUCMZSIPOBKRDSBYVLMXGSSKF6YNPIB7Y77ITKNOGA".toCharArray()
      );
      fail();
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("Invalid input length"));

    }

    // non-canonical representation due to leftover bits set to 1 (some of the test strkeys are too short for a muxed account

    // 1 unused bit (length 69)
    try {
      StrKey.decodeCheck(
          StrKey.VersionByte.MUXED_ACCOUNT,
          "MCAAAAAAAAAAAAB7BQ2L7E5NBWMXDUCMZSIPOBKRDSBYVLMXGSSKF6YNPIB7Y77ITKNOH".toCharArray()
      );
      fail();
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("Unused bits should be set to 0."));

    }


    // 4 unused bits (length 68)

    // 'B' is equivalent to 0b00001
    try {
      StrKey.decodeCheck(
          StrKey.VersionByte.MUXED_ACCOUNT,
          "MCAAAAAAAAAAAAB7BQ2L7E5NBWMXDUCMZSIPOBKRDSBYVLMXGSSKF6YNPIB7Y77ITKNB".toCharArray()
      );
      fail();
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("Unused bits should be set to 0."));

    }

    // 'C' is equivalent to 0b00010
    try {
      StrKey.decodeCheck(
          StrKey.VersionByte.MUXED_ACCOUNT,
          "MCAAAAAAAAAAAAB7BQ2L7E5NBWMXDUCMZSIPOBKRDSBYVLMXGSSKF6YNPIB7Y77ITKNC".toCharArray()
      );
      fail();
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("Unused bits should be set to 0."));

    }

    // 'E' is equivalent to 0b00100
    try {
      StrKey.decodeCheck(
          StrKey.VersionByte.MUXED_ACCOUNT,
          "MCAAAAAAAAAAAAB7BQ2L7E5NBWMXDUCMZSIPOBKRDSBYVLMXGSSKF6YNPIB7Y77ITKNE".toCharArray()
      );
      fail();
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("Unused bits should be set to 0."));

    }

    // 'I' is equivalent to 0b01000
    try {
      StrKey.decodeCheck(
          StrKey.VersionByte.MUXED_ACCOUNT,
          "MCAAAAAAAAAAAAB7BQ2L7E5NBWMXDUCMZSIPOBKRDSBYVLMXGSSKF6YNPIB7Y77ITKNI".toCharArray()
      );
      fail();
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("Unused bits should be set to 0."));

    }

    // '7' is equivalent to 0b11111
    try {
      StrKey.decodeCheck(
          StrKey.VersionByte.MUXED_ACCOUNT,
          "MCAAAAAAAAAAAAB7BQ2L7E5NBWMXDUCMZSIPOBKRDSBYVLMXGSSKF6YNPIB7Y77ITKN7".toCharArray()
      );
      fail();
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("Unused bits should be set to 0."));

    }

    // '6' is equivalent to 0b11110
    try {
      StrKey.decodeCheck(
          StrKey.VersionByte.MUXED_ACCOUNT,
          "MCAAAAAAAAAAAAB7BQ2L7E5NBWMXDUCMZSIPOBKRDSBYVLMXGSSKF6YNPIB7Y77ITKN6".toCharArray()
      );
      fail();
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("Unused bits should be set to 0."));

    }

    // '4' is equivalent to 0b11100
    try {
      StrKey.decodeCheck(
          StrKey.VersionByte.MUXED_ACCOUNT,
          "MCAAAAAAAAAAAAB7BQ2L7E5NBWMXDUCMZSIPOBKRDSBYVLMXGSSKF6YNPIB7Y77ITKN4".toCharArray()
      );
      fail();
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("Unused bits should be set to 0."));

    }

    // 'Y' is equivalent to 0b11000
    try {
      StrKey.decodeCheck(
          StrKey.VersionByte.MUXED_ACCOUNT,
          "MCAAAAAAAAAAAAB7BQ2L7E5NBWMXDUCMZSIPOBKRDSBYVLMXGSSKF6YNPIB7Y77ITKNY".toCharArray()
      );
      fail();
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("Unused bits should be set to 0."));

    }


    // 'Q' is equivalent to 0b10000, so there should be no unused bits error
    // However, there will be a checksum error
    try {
      StrKey.decodeCheck(
          StrKey.VersionByte.MUXED_ACCOUNT,
          "MCAAAAAAAAAAAAB7BQ2L7E5NBWMXDUCMZSIPOBKRDSBYVLMXGSSKF6YNPIB7Y77ITKNQ".toCharArray()
      );
    } catch (Exception e) {
      assertEquals("Checksum invalid", e.getMessage());
    }
  }

  @Test
  public void testEncodeToXdrRoundTrip() {
    String address = "GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVSGZ";
    byte[] ed25519 = rawBytes(
        63, 12, 52, 191, 147, 173, 13, 153, 113, 208,
        76, 204, 144, 247, 5, 81, 28, 131, 138, 173, 151, 52,
        164, 162, 251, 13, 122, 3, 252, 127, 232, 154
    );
    AccountID account = StrKey.encodeToXDRAccountId(address);
    assertArrayEquals(ed25519, account.getAccountID().getEd25519().getUint256());
    assertEquals(address, StrKey.encodeStellarAccountId(account));

    MuxedAccount muxedAccount = StrKey.encodeToXDRMuxedAccount(address);
    assertEquals(CryptoKeyType.KEY_TYPE_ED25519, muxedAccount.getDiscriminant());
    assertArrayEquals(ed25519, muxedAccount.getEd25519().getUint256());
    assertEquals(address, StrKey.encodeStellarMuxedAccount(muxedAccount));

    String muxedAddress = "MCAAAAAAAAAAAAB7BQ2L7E5NBWMXDUCMZSIPOBKRDSBYVLMXGSSKF6YNPIB7Y77ITKNOG";
    muxedAccount = StrKey.encodeToXDRMuxedAccount(muxedAddress);
    assertEquals(CryptoKeyType.KEY_TYPE_MUXED_ED25519, muxedAccount.getDiscriminant());
    assertArrayEquals(ed25519, muxedAccount.getMed25519().getEd25519().getUint256());
    long memoId = UnsignedLongs.parseUnsignedLong("9223372036854775808");
    assertEquals(memoId, muxedAccount.getMed25519().getId().getUint64().longValue());
    assertEquals(muxedAddress, StrKey.encodeStellarMuxedAccount(muxedAccount));

    muxedAddress = "MAAAAAAAAAAAAAB7BQ2L7E5NBWMXDUCMZSIPOBKRDSBYVLMXGSSKF6YNPIB7Y77ITLVL6";
    muxedAccount = StrKey.encodeToXDRMuxedAccount(muxedAddress);
    assertEquals(CryptoKeyType.KEY_TYPE_MUXED_ED25519, muxedAccount.getDiscriminant());
    assertArrayEquals(ed25519, muxedAccount.getMed25519().getEd25519().getUint256());
    assertEquals(0, muxedAccount.getMed25519().getId().getUint64().longValue());
    assertEquals(muxedAddress, StrKey.encodeStellarMuxedAccount(muxedAccount));
  }

}
