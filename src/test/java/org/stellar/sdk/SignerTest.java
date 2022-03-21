package org.stellar.sdk;

import com.google.common.io.BaseEncoding;
import org.junit.Test;
import org.stellar.sdk.xdr.SignerKey;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

public class SignerTest {

    @Test
    public void itCreatesSignedPayloadSigner() {
        String accountStrKey = "GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVSGZ";

        byte[] payload = BaseEncoding.base16().decode("0102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f20".toUpperCase());
        SignedPayloadSigner signedPayloadSigner = new SignedPayloadSigner(accountStrKey, payload);
        SignerKey signerKey = Signer.signedPayload(signedPayloadSigner);

        assertArrayEquals(signerKey.getEd25519SignedPayload().getPayload(), payload);
        assertArrayEquals(signerKey.getEd25519SignedPayload().getEd25519().getUint256(),signedPayloadSigner.getDecodedAccountId());
    }

    @Test
    public void itFailsWhenInvalidParameters() {
        String accountStrKey = "GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTnotgood";
        byte[] payload = BaseEncoding.base16().decode("0102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f20".toUpperCase());
        SignedPayloadSigner signedPayloadSigner = new SignedPayloadSigner(accountStrKey, payload);

        try {
            SignerKey signerKey = Signer.signedPayload(signedPayloadSigner);
            fail("should not create a payload signer if invalid account");
        } catch (IllegalArgumentException ignored) {}

        accountStrKey = "GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVSGZ";
        payload = BaseEncoding.base16().decode("0102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f200102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f2001".toUpperCase());
        signedPayloadSigner = new SignedPayloadSigner(accountStrKey, payload);
        try {
            SignerKey signerKey = Signer.signedPayload(signedPayloadSigner);
            fail("should not create a payload signer if payload > max length");
        } catch (IllegalArgumentException ignored) {}

    }

}
