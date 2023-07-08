package org.stellar.sdk.xdr;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import com.google.common.io.BaseEncoding;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.Test;

public class AccountEntryDecodeTest {

  BaseEncoding base64Encoding = BaseEncoding.base64();

  @Test
  public void testDecodeSignerPayload() throws IOException {
    AccountEntry.Builder bldr = new AccountEntry.Builder();
    Signer signer = new Signer();
    SignerKey signerKey = new SignerKey();
    signerKey.setDiscriminant(SignerKeyType.SIGNER_KEY_TYPE_ED25519_SIGNED_PAYLOAD);
    signerKey.setEd25519SignedPayload(new SignerKey.SignerKeyEd25519SignedPayload());
    signerKey.getEd25519SignedPayload().setPayload(new byte[] {1, 2, 3, 4});
    signerKey.getEd25519SignedPayload().setEd25519(new Uint256(new byte[32]));
    signer.setKey(signerKey);
    signer.setWeight(new Uint32(1));
    bldr.signers(new Signer[] {signer});
    bldr.accountID(
        new AccountID(
            new PublicKey.Builder()
                .discriminant(PublicKeyType.PUBLIC_KEY_TYPE_ED25519)
                .ed25519(new Uint256(new byte[32]))
                .build()));
    bldr.seqNum(new SequenceNumber(new Int64(1L)));
    bldr.balance(new Int64(0L));
    bldr.numSubEntries(new Uint32(0));
    bldr.flags(new Uint32(0));
    bldr.homeDomain(new String32(new XdrString("")));
    bldr.thresholds(new Thresholds(new byte[3]));
    bldr.ext(
        new AccountEntry.AccountEntryExt.Builder()
            .discriminant(1)
            .v1(
                new AccountEntryExtensionV1.Builder()
                    .liabilities(
                        new Liabilities.Builder()
                            .buying(new Int64(0L))
                            .selling(new Int64(0L))
                            .build())
                    .ext(
                        new AccountEntryExtensionV1.AccountEntryExtensionV1Ext.Builder()
                            .discriminant(2)
                            .v2(
                                new AccountEntryExtensionV2.Builder()
                                    .numSponsored(new Uint32(0))
                                    .numSponsoring(new Uint32(0))
                                    .signerSponsoringIDs(new SponsorshipDescriptor[] {})
                                    .ext(
                                        new AccountEntryExtensionV2.AccountEntryExtensionV2Ext
                                                .Builder()
                                            .discriminant(3)
                                            .v3(
                                                new AccountEntryExtensionV3.Builder()
                                                    .seqLedger(new Uint32(1))
                                                    .seqTime(new TimePoint(new Uint64(2L)))
                                                    .ext(
                                                        new ExtensionPoint.Builder()
                                                            .discriminant(0)
                                                            .build())
                                                    .build())
                                            .build())
                                    .build())
                            .build())
                    .build())
            .build());

    AccountEntry xdr = bldr.build();

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    XdrDataOutputStream outputStream = new XdrDataOutputStream(baos);
    AccountEntry.encode(outputStream, xdr);
    String encodedXdr = base64Encoding.encode(baos.toByteArray());

    byte[] decodedbBytes = base64Encoding.decode(encodedXdr);

    AccountEntry accountEntry =
        AccountEntry.decode(new XdrDataInputStream(new ByteArrayInputStream(decodedbBytes)));
    assertArrayEquals(
        new byte[32],
        accountEntry.getSigners()[0].getKey().getEd25519SignedPayload().getEd25519().getUint256());
    assertArrayEquals(
        new byte[] {1, 2, 3, 4},
        accountEntry.getSigners()[0].getKey().getEd25519SignedPayload().getPayload());
    assertEquals(
        1,
        accountEntry
            .getExt()
            .getV1()
            .getExt()
            .getV2()
            .getExt()
            .getV3()
            .getSeqLedger()
            .getUint32()
            .longValue());
    assertEquals(
        2L,
        accountEntry
            .getExt()
            .getV1()
            .getExt()
            .getV2()
            .getExt()
            .getV3()
            .getSeqTime()
            .getTimePoint()
            .getUint64()
            .longValue());
  }
}
