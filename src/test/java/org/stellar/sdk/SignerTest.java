package org.stellar.sdk;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import com.google.common.io.BaseEncoding;
import org.junit.Test;
import org.stellar.sdk.xdr.SignerKey;

public class SignerTest {

  @Test
  public void itCreatesSignedPayloadSigner() {
    String accountStrKey = "GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVSGZ";

    byte[] payload =
        BaseEncoding.base16()
            .decode(
                "0102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f20".toUpperCase());
    SignedPayloadSigner signedPayloadSigner =
        new SignedPayloadSigner(StrKey.decodeStellarAccountId(accountStrKey), payload);
    SignerKey signerKey = Signer.signedPayload(signedPayloadSigner);

    assertArrayEquals(signerKey.getEd25519SignedPayload().getPayload(), payload);
    assertEquals(
        signerKey.getEd25519SignedPayload().getEd25519(),
        signedPayloadSigner.getSignerAccountId().getAccountID().getEd25519());
  }
}
