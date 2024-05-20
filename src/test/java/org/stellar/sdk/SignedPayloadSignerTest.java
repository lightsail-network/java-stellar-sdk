package org.stellar.sdk;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.stellar.sdk.xdr.AccountID;
import org.stellar.sdk.xdr.PublicKey;
import org.stellar.sdk.xdr.Uint256;

public class SignedPayloadSignerTest {
  @Test
  public void itFailsWhenAccoutIDIsNull() {
    try {
      new SignedPayloadSigner((AccountID) null, new byte[] {});
      fail("should not create when accountid is null");
    } catch (NullPointerException ignored) {
    }
  }

  @Test
  public void itFailsWhenPayloadLengthTooBig() {
    String accountStrKey = "GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVSGZ";
    byte[] payload =
        Util.hexToBytes(
            "0102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f200102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f2001"
                .toUpperCase());
    try {
      new SignedPayloadSigner(StrKey.decodeEd25519PublicKey(accountStrKey), payload);
      fail("should not create a payload signer if payload > max length");
    } catch (IllegalArgumentException ignored) {
    }
  }

  @Test
  public void itFailsWhenSignerNotED25519() {
    try {
      new SignedPayloadSigner(
          new AccountID(PublicKey.builder().ed25519(new Uint256(new byte[] {})).build()),
          new byte[] {});
      fail("should not create a payload signer if signer wasn't ed25519 type");
    } catch (IllegalArgumentException ignored) {
    }
  }
}
