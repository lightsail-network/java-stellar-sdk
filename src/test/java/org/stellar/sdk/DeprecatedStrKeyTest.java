package org.stellar.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.stellar.sdk.xdr.AccountID;
import org.stellar.sdk.xdr.CryptoKeyType;
import org.stellar.sdk.xdr.MuxedAccount;

public class DeprecatedStrKeyTest {
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
}
