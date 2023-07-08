package org.stellar.sdk;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import com.google.common.io.BaseEncoding;
import org.junit.Test;
import org.stellar.sdk.xdr.SignerKey;

public class SetOptionsOperationTest {
  KeyPair source =
      KeyPair.fromSecretSeed("SC4CGETADVYTCR5HEAVZRB3DZQY5Y4J7RFNJTRA6ESMHIPEZUSTE2QDK");

  @Test
  public void testPaylodSignerKey() {
    SetOptionsOperation.Builder builder = new SetOptionsOperation.Builder();
    String payloadSignerStrKey = "GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVSGZ";

    byte[] payload =
        BaseEncoding.base16()
            .decode(
                "0102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f20".toUpperCase());
    SignedPayloadSigner signedPayloadSigner =
        new SignedPayloadSigner(StrKey.decodeStellarAccountId(payloadSignerStrKey), payload);
    SignerKey signerKey = Signer.signedPayload(signedPayloadSigner);

    builder.setSigner(signerKey, 1);
    builder.setSourceAccount(source.getAccountId());

    SetOptionsOperation operation = builder.build();

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr(AccountConverter.enableMuxed());
    SetOptionsOperation parsedOperation =
        (SetOptionsOperation) Operation.fromXdr(AccountConverter.enableMuxed(), xdr);

    // verify round trip between xdr and pojo
    assertEquals(source.getAccountId(), parsedOperation.getSourceAccount());
    assertEquals(
        signedPayloadSigner.getSignerAccountId().getAccountID().getEd25519(),
        parsedOperation.getSigner().getEd25519SignedPayload().getEd25519());
    assertArrayEquals(
        signedPayloadSigner.getPayload(),
        parsedOperation.getSigner().getEd25519SignedPayload().getPayload());

    // verify serialized xdr emitted with signed payload
    assertEquals(
        "AAAAAQAAAAC7JAuE3XvquOnbsgv2SRztjuk4RoBVefQ0rlrFMMQvfAAAAAUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
            + "AAAAAAAAAAAAAAAEAAAADPww0v5OtDZlx0EzMkPcFURyDiq2XNKSi+w16A/x/6JoAAAAgAQIDBAUGBwgJCgsMDQ4PEBES"
            + "ExQVFhcYGRobHB0eHyAAAAAB",
        operation.toXdrBase64(AccountConverter.enableMuxed()));
  }
}
